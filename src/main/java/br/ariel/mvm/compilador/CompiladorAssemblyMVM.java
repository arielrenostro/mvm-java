package br.ariel.mvm.compilador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import br.ariel.mvm.exception.CaractereEsperadoCompiladorException;
import br.ariel.mvm.exception.CompiladorException;
import br.ariel.mvm.exception.MultipleCompiladorExceptions;
import br.ariel.mvm.exception.SemEspacoMemoriaCompiladorException;
import br.ariel.mvm.model.InstrucaoProcessador;
import br.ariel.mvm.utils.Utils;

public class CompiladorAssemblyMVM {

	private static final Pattern PATTERN_NUMEROS = Pattern.compile("\\d+");

	private static final String MOV = "MOV";
	private static final String INIT = "INIT";
	private static final String ADD = "ADD";
	private static final String SUB = "SUB";
	private static final String INC = "INC";
	private static final String DEC = "DEC";
	private static final String JMP = "JMP";
	private static final String TEST = "TEST";
	private static final String CALL = "CALL";
	private static final String RET = "RET";
	private static final String IN = "IN";
	private static final String OUT = "OUT";
	private static final String PUSH = "PUSH";
	private static final String POP = "POP";
	private static final String NOP = "NOP";
	private static final String HALT = "HALT";
	private static final String IRET = "IRET";
	private static final String INT = "INT";

	public byte[] compilar(String codigo) throws CompiladorException {
		List<String> linhasCodigo = getLinhasCodigo(codigo);
		List<LinhaInstrucao> linhasInstrucao = getLinhasInstrucao(linhasCodigo);
		processarLinhasInstrucao(linhasInstrucao);
		return converterLinhasInstrucaoParaArray(linhasInstrucao);
	}

	private void processarLinhasInstrucao(List<LinhaInstrucao> linhasInstrucao) throws CompiladorException {
		MultipleCompiladorExceptions exceptions = new MultipleCompiladorExceptions();
		linhasInstrucao.parallelStream().forEach(linhaInstrucao -> {
			try {
				processarLinhaInstrucao(linhaInstrucao);
			} catch (CompiladorException e) {
				synchronized (exceptions) {
					exceptions.addSuppressed(e);
				}
			}
		});

		if (exceptions.getSuppressed().length > 0) {
			throw exceptions;
		}

		definirIndiceBytes(linhasInstrucao);
		processarLabels(linhasInstrucao);

		validarTamanhoInstrucoes(linhasInstrucao);
	}

	private void definirIndiceBytes(List<LinhaInstrucao> linhasInstrucao) {
		int idx = 0;
		for (LinhaInstrucao instrucao : linhasInstrucao) {
			instrucao.setIdxByte(idx);
			if (!instrucao.isEhLabel()) {
				idx += instrucao.getInstrucao().length;
			}
		}
	}

	private void processarLabels(List<LinhaInstrucao> linhasInstrucao) throws CompiladorException {
		Map<String, Integer> indicePorLabel = linhasInstrucao.parallelStream() //
				.filter(LinhaInstrucao::isEhLabel) //
				.collect(Collectors.toMap(LinhaInstrucao::getLabel, LinhaInstrucao::getIdxByte));

		List<LinhaInstrucao> linhasInstrucaoComLabel = linhasInstrucao.parallelStream() //
				.filter(linha -> !linha.isEhLabel() && !linha.getLabel().isEmpty()) //
				.collect(Collectors.toList());

		for (LinhaInstrucao linha : linhasInstrucaoComLabel) {
			byte instrucao = linha.getInstrucao()[0];
			Integer index = indicePorLabel.get(linha.getLabel());
			if (null == index) {
				throw new CompiladorException("Label [" + linha.getLabel() + "] não declarada!");
			}
			byte[] instrucaoCompleta = getBytesInstrucaoComIndex(instrucao, index);
			linha.setInstrucao(instrucaoCompleta);
		}
	}

	private synchronized void processarLinhaInstrucao(LinhaInstrucao linhaInstrucao) throws CompiladorException {
		String linha = linhaInstrucao.getLinha();
		if (Utils.isEmpty(linha)) {
			return;
		}

		linha = linha.trim();
		if (Utils.isEmpty(linha)) {
			return;
		}

		String comando = getComandoLinha(linha);
		linha = removerComando(linha, comando);
		switch (comando) {
		case MOV:
			processarComandoMOV(linhaInstrucao, linha, comando);
			break;
		case INIT:
			processarComandoINIT(linhaInstrucao, linha, comando);
			break;
		case ADD:
			processarComandoADD(linhaInstrucao, linha, comando);
			break;
		case SUB:
			processarComandoSUB(linhaInstrucao, linha, comando);
			break;
		case INC:
			processarComandoINC(linhaInstrucao, linha, comando);
			break;
		case DEC:
			processarComandoDEC(linhaInstrucao, linha, comando);
			break;
		case JMP:
			processarComandoJMP(linhaInstrucao, linha, comando);
			break;
		case TEST:
			processarComandoTEST(linhaInstrucao, linha, comando);
			break;
		case CALL:
			processarComandoCALL(linhaInstrucao, linha, comando);
			break;
		case RET:
			processarComandoRET(linhaInstrucao, linha, comando);
			break;
		case IN:
			processarComandoIN(linhaInstrucao, linha, comando);
			break;
		case OUT:
			processarComandoOUT(linhaInstrucao, linha, comando);
			break;
		case PUSH:
			processarComandoPUSH(linhaInstrucao, linha, comando);
			break;
		case POP:
			processarComandoPOP(linhaInstrucao, linha, comando);
			break;
		case NOP:
			processarComandoNOP(linhaInstrucao, linha, comando);
			break;
		case HALT:
			processarComandoHALT(linhaInstrucao, linha, comando);
			break;
		case IRET:
			processarComandoIRET(linhaInstrucao, linha, comando);
			break;
		case INT:
			processarComandoINT(linhaInstrucao, linha, comando);
			break;
		default:
			processarLabel(linhaInstrucao, linha, comando);
		}
	}

	private void processarComandoCALL(LinhaInstrucao linhaInstrucao, String linha, String comando) throws CompiladorException {
		if (Utils.isEmpty(linha) || Utils.isNotEmpty(linha)) {
			throw new CompiladorException("CALL inválido na linha [" + linhaInstrucao.getIdxLinha() + "]");
		}

		byte[] bytes = getBytesInstrucaoComIndex(InstrucaoProcessador.CALL, 0);
		linhaInstrucao.setInstrucao(bytes);
		linhaInstrucao.setLabel(linha);
	}

	private void processarComandoJMP(LinhaInstrucao linhaInstrucao, String linha, String comando) throws CompiladorException {
		if (Utils.isEmpty(linha)) {
			throw new CompiladorException("JMP inválido na linha [" + linhaInstrucao.getIdxLinha() + "]");
		}

		byte[] bytes = getBytesInstrucaoComIndex(InstrucaoProcessador.JMP, 0);
		linhaInstrucao.setInstrucao(bytes);
		linhaInstrucao.setLabel(linha);
	}

	private void processarComandoINT(LinhaInstrucao linhaInstrucao, String linha, String comando) throws CompiladorException {
		linha = linha.toUpperCase().trim();

		String primeiroParametro = getPrimeiroParametro(linha);
		linha = linha.substring(primeiroParametro.length()); // TODO VALIDAR
		if (Utils.isEmpty(primeiroParametro) || Utils.isNotEmpty(linha) || Utils.isNotNumber(primeiroParametro)) {
			throw new CompiladorException("INT inválido na linha [" + linhaInstrucao.getIdxLinha() + "]");
		}

		byte[] bytes = getBytesInstrucaoComIndex(InstrucaoProcessador.INT, Integer.valueOf(primeiroParametro));
		linhaInstrucao.setInstrucao(bytes);
	}

	private byte[] getBytesInstrucaoComIndex(InstrucaoProcessador i, Integer index) {
		return getBytesInstrucaoComIndex(i.getCode(), index);
	}

	private byte[] getBytesInstrucaoComIndex(byte instrucao, int index) {
		byte[] bytes = new byte[3];
		bytes[0] = instrucao;
		bytes[1] = (byte) (0x00FF & index);
		bytes[2] = (byte) (0xFF00 & index);
		return bytes;
	}

	private void processarComandoIRET(LinhaInstrucao linhaInstrucao, String linha, String comando) throws CompiladorException {
		if (!linha.trim().isEmpty()) {
			throw new CompiladorException("IRET inválido na linha [" + linhaInstrucao.getIdxLinha() + "]");
		}

		linhaInstrucao.setInstrucao(new byte[] { InstrucaoProcessador.IRET.getCode() });
	}

	private void processarComandoHALT(LinhaInstrucao linhaInstrucao, String linha, String comando) throws CompiladorException {
		if (!linha.trim().isEmpty()) {
			throw new CompiladorException("HALT inválido na linha [" + linhaInstrucao.getIdxLinha() + "]");
		}

		linhaInstrucao.setInstrucao(new byte[] { InstrucaoProcessador.HALT.getCode() });
	}

	private void processarComandoNOP(LinhaInstrucao linhaInstrucao, String linha, String comando) throws CompiladorException {
		if (!linha.trim().isEmpty()) {
			throw new CompiladorException("NOP inválido na linha [" + linhaInstrucao.getIdxLinha() + "]");
		}

		linhaInstrucao.setInstrucao(new byte[] { InstrucaoProcessador.NOP.getCode() });
	}

	private void processarComandoPOP(LinhaInstrucao linhaInstrucao, String linha, String comando) throws CompiladorException {
		linha = linha.toUpperCase().trim();

		String primeiroParametro = getPrimeiroParametro(linha);
		linha = linha.substring(primeiroParametro.length()); // TODO VALIDAR
		if (Utils.isEmpty(primeiroParametro) || Utils.isNotEmpty(linha)) {
			throw new CompiladorException("POP inválido na linha [" + linhaInstrucao.getIdxLinha() + "]");
		}

		StringBuilder enumSB = new StringBuilder();
		enumSB.append("POP_");
		enumSB.append(primeiroParametro);

		InstrucaoProcessador instrucao = getEnumPorStringBuilder(enumSB);
		if (null == instrucao) {
			throw new CompiladorException("POP inválido na linha [" + linhaInstrucao.getIdxLinha() + "]");
		}

		linhaInstrucao.setInstrucao(new byte[] { instrucao.getCode() });
	}

	private void processarComandoPUSH(LinhaInstrucao linhaInstrucao, String linha, String comando) throws CompiladorException {
		linha = linha.toUpperCase().trim();

		String primeiroParametro = getPrimeiroParametro(linha);
		linha = linha.substring(primeiroParametro.length()); // TODO VALIDAR
		if (Utils.isEmpty(primeiroParametro) || Utils.isNotEmpty(linha)) {
			throw new CompiladorException("PUSH inválido na linha [" + linhaInstrucao.getIdxLinha() + "]");
		}

		StringBuilder enumSB = new StringBuilder();
		enumSB.append("PUSH_");
		enumSB.append(primeiroParametro);

		InstrucaoProcessador instrucao = getEnumPorStringBuilder(enumSB);
		if (null == instrucao) {
			throw new CompiladorException("PUSH inválido na linha [" + linhaInstrucao.getIdxLinha() + "]");
		}

		linhaInstrucao.setInstrucao(new byte[] { instrucao.getCode() });
	}

	private void processarComandoOUT(LinhaInstrucao linhaInstrucao, String linha, String comando) throws CompiladorException {
		linha = linha.toUpperCase().trim();

		String primeiroParametro = getPrimeiroParametro(linha);
		linha = linha.substring(primeiroParametro.length()); // TODO VALIDAR
		if (Utils.isEmpty(primeiroParametro) || Utils.isNotEmpty(linha)) {
			throw new CompiladorException("OUT inválido na linha [" + linhaInstrucao.getIdxLinha() + "]");
		}

		StringBuilder enumSB = new StringBuilder();
		enumSB.append("OUT_");
		enumSB.append(primeiroParametro);

		InstrucaoProcessador instrucao = getEnumPorStringBuilder(enumSB);
		if (null == instrucao) {
			throw new CompiladorException("OUT inválido na linha [" + linhaInstrucao.getIdxLinha() + "]");
		}

		linhaInstrucao.setInstrucao(new byte[] { instrucao.getCode() });
	}

	private void processarComandoIN(LinhaInstrucao linhaInstrucao, String linha, String comando) throws CompiladorException {
		linha = linha.toUpperCase().trim();

		String primeiroParametro = getPrimeiroParametro(linha);
		linha = linha.substring(primeiroParametro.length()); // TODO VALIDAR
		if (Utils.isEmpty(primeiroParametro) || Utils.isNotEmpty(linha)) {
			throw new CompiladorException("IN inválido na linha [" + linhaInstrucao.getIdxLinha() + "]");
		}

		StringBuilder enumSB = new StringBuilder();
		enumSB.append("IN_");
		enumSB.append(primeiroParametro);

		InstrucaoProcessador instrucao = getEnumPorStringBuilder(enumSB);
		if (null == instrucao) {
			throw new CompiladorException("IN inválido na linha [" + linhaInstrucao.getIdxLinha() + "]");
		}

		linhaInstrucao.setInstrucao(new byte[] { instrucao.getCode() });
	}

	private void processarComandoRET(LinhaInstrucao linhaInstrucao, String linha, String comando) throws CompiladorException {
		if (!linha.trim().isEmpty()) {
			throw new CompiladorException("RET inválido na linha [" + linhaInstrucao.getIdxLinha() + "]");
		}

		linhaInstrucao.setInstrucao(new byte[] { InstrucaoProcessador.RET.getCode() });
	}

	private void processarComandoTEST(LinhaInstrucao linhaInstrucao, String linha, String comando) throws CompiladorException {
		linha = linha.toUpperCase();

		String primeiroParametro = getPrimeiroParametro(linha);
		if (Utils.isEmpty(primeiroParametro)) {
			throw new CompiladorException("TEST inválido na linha [" + linhaInstrucao.getIdxLinha() + "]");
		}

		StringBuilder enumSB = new StringBuilder();
		enumSB.append("TEST_");
		enumSB.append(primeiroParametro);

		if (temVirgula(linha)) {
			String segundoParametro = getSegundoParametro(linhaInstrucao, linha);
			enumSB.append("_");
			enumSB.append(segundoParametro);
		}

		InstrucaoProcessador instrucao = getEnumPorStringBuilder(enumSB);
		if (null == instrucao) {
			throw new CompiladorException("TEST inválido na linha [" + linhaInstrucao.getIdxLinha() + "]");
		}

		linhaInstrucao.setInstrucao(new byte[] { instrucao.getCode() });
	}

	private boolean temVirgula(String linha) {
		return -1 != linha.indexOf(',');
	}

	private void processarComandoDEC(LinhaInstrucao linhaInstrucao, String linha, String comando) throws CompiladorException {
		linha = linha.toUpperCase();

		String primeiroParametro = getPrimeiroParametro(linha);
		linha = linha.substring(primeiroParametro.length()); // TODO VALIDAR
		if (Utils.isEmpty(primeiroParametro) || Utils.isNotEmpty(linha)) {
			throw new CompiladorException("DEC inválido na linha [" + linhaInstrucao.getIdxLinha() + "]");
		}

		StringBuilder enumSB = new StringBuilder();
		enumSB.append("DEC_");
		enumSB.append(primeiroParametro);

		InstrucaoProcessador instrucao = getEnumPorStringBuilder(enumSB);
		if (null == instrucao) {
			throw new CompiladorException("DEC inválido na linha [" + linhaInstrucao.getIdxLinha() + "]");
		}

		linhaInstrucao.setInstrucao(new byte[] { instrucao.getCode() });
	}

	private void processarComandoINC(LinhaInstrucao linhaInstrucao, String linha, String comando) throws CompiladorException {
		linha = linha.toUpperCase();

		String primeiroParametro = getPrimeiroParametro(linha);
		linha = linha.substring(primeiroParametro.length()); // TODO VALIDAR
		if (Utils.isEmpty(primeiroParametro) || Utils.isNotEmpty(linha)) {
			throw new CompiladorException("INC inválido na linha [" + linhaInstrucao.getIdxLinha() + "]");
		}

		StringBuilder enumSB = new StringBuilder();
		enumSB.append("INC_");
		enumSB.append(primeiroParametro);

		InstrucaoProcessador instrucao = getEnumPorStringBuilder(enumSB);
		if (null == instrucao) {
			throw new CompiladorException("INC inválido na linha [" + linhaInstrucao.getIdxLinha() + "]");
		}

		linhaInstrucao.setInstrucao(new byte[] { instrucao.getCode() });
	}

	private void processarLabel(LinhaInstrucao linhaInstrucao, String linha, String comando) throws CompiladorException {
		if (Utils.isNotEmpty(linha)) {
			throw new CompiladorException("Label ou comando inválido na linha [" + linhaInstrucao.getIdxLinha() + "]");
		}

		if (!comando.endsWith(":")) {
			throw new CompiladorException("Esperado ':' no final da label da linha [" + linhaInstrucao.getIdxLinha() + "]");
		}

		comando = comando.substring(0, comando.length() - 1);

		linhaInstrucao.setLabel(comando);
		linhaInstrucao.setEhLabel(true);
	}

	private void processarComandoSUB(LinhaInstrucao linhaInstrucao, String linha, String comando) throws CompiladorException {
		linha = linha.toUpperCase();

		String primeiroParametro = getPrimeiroParametro(linha);
		String segundoParametro = getSegundoParametro(linhaInstrucao, linha);

		if (Utils.isEmpty(primeiroParametro) || Utils.isEmpty(segundoParametro)) {
			throw new CompiladorException("SUB inválido na linha [" + linhaInstrucao.getIdxLinha() + "]");
		}

		StringBuilder enumSB = new StringBuilder();
		enumSB.append("SUB_");
		enumSB.append(primeiroParametro);
		enumSB.append("_");
		enumSB.append(segundoParametro);

		InstrucaoProcessador instrucao = getEnumPorStringBuilder(enumSB);
		if (null == instrucao) {
			throw new CompiladorException("SUB inválido na linha [" + linhaInstrucao.getIdxLinha() + "]");
		}

		linhaInstrucao.setInstrucao(new byte[] { instrucao.getCode() });
	}

	private void processarComandoADD(LinhaInstrucao linhaInstrucao, String linha, String comando) throws CompiladorException {
		linha = linha.toUpperCase();

		String primeiroParametro = getPrimeiroParametro(linha);
		String segundoParametro = getSegundoParametro(linhaInstrucao, linha);

		if (Utils.isEmpty(primeiroParametro) || Utils.isEmpty(segundoParametro)) {
			throw new CompiladorException("ADD inválido na linha [" + linhaInstrucao.getIdxLinha() + "]");
		}

		StringBuilder enumSB = new StringBuilder();
		enumSB.append("ADD_");
		enumSB.append(primeiroParametro);
		enumSB.append("_");
		enumSB.append(segundoParametro);

		InstrucaoProcessador instrucao = getEnumPorStringBuilder(enumSB);
		if (null == instrucao) {
			throw new CompiladorException("ADD inválido na linha [" + linhaInstrucao.getIdxLinha() + "]");
		}

		linhaInstrucao.setInstrucao(new byte[] { instrucao.getCode() });
	}

	private void processarComandoINIT(LinhaInstrucao linhaInstrucao, String linha, String comando) throws CompiladorException {
		linha = linha.toUpperCase();

		if (!"AX".equalsIgnoreCase(linha)) {
			throw new CompiladorException("Esperado um INIT AX na linha [" + linhaInstrucao.getIdxLinha() + "]");
		}

		linhaInstrucao.setInstrucao(new byte[] { InstrucaoProcessador.INIT_AX.getCode() });
	}

	private void processarComandoMOV(LinhaInstrucao linhaInstrucao, String linha, String comando) throws CompiladorException {
		linha = linha.toUpperCase();

		String primeiroParametro = getPrimeiroParametro(linha);
		String segundoParametro = getSegundoParametro(linhaInstrucao, linha);

		if (Utils.isEmpty(primeiroParametro) || Utils.isEmpty(segundoParametro)) {
			throw new CompiladorException("MOV inválido na linha [" + linhaInstrucao.getIdxLinha() + "]");
		}

		StringBuilder enumSB = new StringBuilder();
		enumSB.append("MOV_");
		adicionarParametroEnumMOV(enumSB, primeiroParametro);
		enumSB.append("_");
		adicionarParametroEnumMOV(enumSB, segundoParametro);

		InstrucaoProcessador instrucao = getEnumPorStringBuilder(enumSB);
		if (null == instrucao) {
			throw new CompiladorException("MOV inválido na linha [" + linhaInstrucao.getIdxLinha() + "]");
		}

		definirInstrucaoMOV(linhaInstrucao, instrucao, primeiroParametro, segundoParametro);
	}

	private InstrucaoProcessador getEnumPorStringBuilder(StringBuilder enumSB) {
		try {
			return InstrucaoProcessador.valueOf(enumSB.toString());
		} catch (Exception e) {
			return null;
		}
	}

	private void definirInstrucaoMOV(LinhaInstrucao linhaInstrucao, InstrucaoProcessador instrucao, String primeiroParametro, String segundoParametro) throws CompiladorException {
		if (instrucao.name().contains("MEM") || instrucao.name().contains("LITERAL")) {
			short valor = getValorParametrosMOV(primeiroParametro, segundoParametro);
			byte[] bytesValor = quebrarBytesValor(valor);
			byte[] bytesInstrucoes = new byte[] { instrucao.getCode(), bytesValor[0], bytesValor[1] };
			linhaInstrucao.setInstrucao(bytesInstrucoes);
		} else {
			linhaInstrucao.setInstrucao(new byte[] { instrucao.getCode() });
		}
	}

	private byte[] quebrarBytesValor(short valor) {
		byte byte1 = (byte) (valor & 0x00FF);
		byte byte2 = (byte) (valor & 0xFF00);
		return new byte[] { byte1, byte2 };
	}

	private short getValorParametrosMOV(String primeiroParametro, String segundoParametro) throws CompiladorException {
		Matcher matcher = PATTERN_NUMEROS.matcher(primeiroParametro);
		if (!matcher.find()) {
			matcher = PATTERN_NUMEROS.matcher(segundoParametro);
		}
		if (!matcher.find()) {
			throw new CompiladorException("Erro inesperado ao pegar o valor dos parâmetros do MOV");
		}

		String valor = matcher.group();
		return Short.valueOf(valor);
	}

	private void adicionarParametroEnumMOV(StringBuilder enumSB, String parametro) {
		StringBuilder parametroSB = new StringBuilder();
		if (parametro.matches("[a-zA-Z]+")) { // Inicia com letras
			parametroSB.append(parametro);
		} else if (parametro.matches("\\{\\d*\\}")) { // É do tipo {1234}
			parametroSB.append("LITERAL");
		} else if (parametro.matches("\\[\\d*\\]")) { // É do tipo [1234]
			parametroSB.append("MEM");
		} else if (parametro.matches("\\[(BX|BP|Bp|Bx|bP|bX|bp|bx)\\s*\\+\\s*\\d*\\]")) { // É do tipo [BX/BP + 4]
			String registrador = parametro.contains("BP") ? "BP" : "BX";
			parametroSB.append("MEM_");
			parametroSB.append(registrador);
			parametroSB.append("_P");
		} else if (parametro.matches("\\[(BX|BP|Bp|Bx|bP|bX|bp|bx)\\s*\\-\\s*\\d*\\]")) { // É do tipo [BX/BP - 4]
			String registrador = parametro.contains("BP") ? "BP" : "BX";
			parametroSB.append("MEM_");
			parametroSB.append(registrador);
			parametroSB.append("_S");
		}

		enumSB.append(parametroSB);
	}

	private void validarExistenciaVirgula(LinhaInstrucao linhaInstrucao, String linha) throws CaractereEsperadoCompiladorException {
		if (!temVirgula(linha)) {
			throw new CaractereEsperadoCompiladorException(linhaInstrucao.getIdxLinha(), ",");
		}
	}

	private String getSegundoParametro(LinhaInstrucao linhaInstrucao, String linha) throws CompiladorException {
		validarExistenciaVirgula(linhaInstrucao, linha);
		int idxVirgula = linha.indexOf(",");
		return linha.substring(idxVirgula + 1).trim();
	}

	private String getPrimeiroParametro(String linha) {
		int idxVirgula = linha.indexOf(",");
		if (-1 == idxVirgula) {
			idxVirgula = linha.length();
		}

		int idxInstrucao = linha.indexOf(" ");
		if (idxVirgula < idxInstrucao || -1 == idxInstrucao) {
			idxInstrucao = 0;
		}
		return linha.substring(idxInstrucao, idxVirgula); // TODO VALIDAR
	}

	private String removerComando(String linha, String comando) {
		return linha.substring(comando.length()).trim(); // TODO VALIDAR
	}

	private String getComandoLinha(String linha) {
		if (-1 != linha.indexOf(' ')) {
			String comando = linha.substring(0, linha.indexOf(' '));
			if (Utils.isEmpty(comando)) {
				comando = linha;
			}
			return comando.toUpperCase().trim();
		}
		return linha;
	}

	private void validarTamanhoInstrucoes(List<LinhaInstrucao> linhasInstrucao) throws SemEspacoMemoriaCompiladorException {
		int totalBytes = getTotalBytesInstrucoes(linhasInstrucao);
		if (totalBytes > Short.MAX_VALUE) {
			throw new SemEspacoMemoriaCompiladorException(totalBytes);
		}
	}

	private byte[] converterLinhasInstrucaoParaArray(List<LinhaInstrucao> linhasInstrucao) {
		int totalBytes = getTotalBytesInstrucoes(linhasInstrucao);
		AtomicInteger idx = new AtomicInteger(0);
		byte[] bytes = new byte[totalBytes];
		linhasInstrucao.stream() //
				.map(linhaInstrucao -> linhaInstrucao.getInstrucao()) //
				.forEach(bytesInstrucao -> {
					for (byte byteInstrucao : bytesInstrucao) {
						bytes[idx.getAndIncrement()] = byteInstrucao;
					}
				});
		return bytes;
	}

	private int getTotalBytesInstrucoes(List<LinhaInstrucao> linhasInstrucao) {
		return linhasInstrucao.parallelStream() //
				.map(linha -> linha.getInstrucao().length) //
				.reduce(Integer::sum) //
				.orElse(0);
	}

	private List<LinhaInstrucao> getLinhasInstrucao(List<String> linhasCodigo) {
		List<LinhaInstrucao> linhasInstrucao = new ArrayList<>();

		for (int idx = 0; idx < linhasCodigo.size(); idx++) {
			linhasInstrucao.add(new LinhaInstrucao(linhasCodigo.get(idx), idx));
		}

		return linhasInstrucao;
	}

	private List<String> getLinhasCodigo(String codigo) {
		return Arrays.stream(codigo.split("\\n")) //
				.parallel()//
				.map(String::trim) //
				.map(String::toUpperCase) //
				.collect(Collectors.toList());
	}

}
