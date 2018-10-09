package br.ariel.mvm.montador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import br.ariel.mvm.exception.CaractereEsperadoMontadorException;
import br.ariel.mvm.exception.MontadorException;
import br.ariel.mvm.exception.MultipleMontadorExceptions;
import br.ariel.mvm.exception.SemEspacoMemoriaMontadorException;
import br.ariel.mvm.model.InstrucaoProcessador;
import br.ariel.mvm.utils.Utils;

public class MontadorMVM {

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
	private static final String ORG = "ORG";

	private static final int INDICE_MAXIMO = Short.MAX_VALUE - 1;

	public byte[] montar(String codigo, boolean gerarTabelaRealocacao) throws MontadorException {
		List<String> linhasCodigo = getLinhasCodigo(codigo);
		List<LinhaInstrucao> linhasInstrucao = getLinhasInstrucao(linhasCodigo);
		processarLinhasInstrucao(linhasInstrucao);
		definirIndiceBytes(linhasInstrucao);
		processarLabels(linhasInstrucao);

		int tamanhoTabelaRealocacao = getTamanhoTabelaRealocacao(linhasInstrucao, gerarTabelaRealocacao);
		int totalBytes = getTotalBytesInstrucoes(linhasInstrucao, tamanhoTabelaRealocacao);
		validarTamanhoInstrucoes(totalBytes);

		return converterLinhasInstrucaoParaArray(linhasInstrucao, totalBytes, tamanhoTabelaRealocacao);
	}

	private void processarLinhasInstrucao(List<LinhaInstrucao> linhasInstrucao) throws MontadorException {
		MultipleMontadorExceptions exceptions = new MultipleMontadorExceptions();
		linhasInstrucao.parallelStream().forEach(linhaInstrucao -> {
			try {
				processarLinhaInstrucao(linhaInstrucao);
			} catch (MontadorException e) {
				synchronized (exceptions) {
					exceptions.addSuppressed(e);
				}
			}
		});

		if (exceptions.getSuppressed().length > 0) {
			throw exceptions;
		}
	}

	private void definirIndiceBytes(List<LinhaInstrucao> linhasInstrucao) {
		int idx = 0;
		for (LinhaInstrucao instrucao : linhasInstrucao) {
			instrucao.setIdxByte(idx);
			if (instrucao.isEhOrg()) {
				idx = instrucao.getIdxOrg();
			} else if (!instrucao.isEhLabel()) {
				idx += instrucao.getInstrucao().length;
			}
		}
	}

	private void processarLabels(List<LinhaInstrucao> linhasInstrucao) throws MontadorException {
		Map<String, Integer> indicePorLabel = linhasInstrucao.parallelStream() //
				.filter(LinhaInstrucao::isEhLabel) //
				.collect(Collectors.toMap(LinhaInstrucao::getLabel, LinhaInstrucao::getIdxByte));

		List<LinhaInstrucao> linhasInstrucaoComLabel = linhasInstrucao.parallelStream() //
				.filter(linha -> !linha.isEhLabel() && !linha.getLabel().isEmpty()) //
				.collect(Collectors.toList());

		for (LinhaInstrucao linha : linhasInstrucaoComLabel) {
			byte instrucao = linha.getInstrucao()[0];
			Integer indexLabel = indicePorLabel.get(linha.getLabel());
			if (null == indexLabel) {
				throw new MontadorException("Label [" + linha.getLabel() + "] não declarada!");
			}
			byte[] instrucaoCompleta = getBytesInstrucaoComIndex(instrucao, indexLabel);
			linha.setInstrucao(instrucaoCompleta);
		}
	}

	private synchronized void processarLinhaInstrucao(LinhaInstrucao linhaInstrucao) throws MontadorException {
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
			processarComandoDEC(linhaInstrucao, linha, comando);// TODO
			break;
		case JMP:
			processarComandoJMP(linhaInstrucao, linha, comando);// TODO
			break;
		case TEST:
			processarComandoTEST(linhaInstrucao, linha, comando);// TODO
			break;
		case CALL:
			processarComandoCALL(linhaInstrucao, linha, comando);// TODO
			break;
		case RET:
			processarComandoRET(linhaInstrucao, linha, comando);// TODO
			break;
		case IN:
			processarComandoIN(linhaInstrucao, linha, comando);// TODO
			break;
		case OUT:
			processarComandoOUT(linhaInstrucao, linha, comando);// TODO
			break;
		case PUSH:
			processarComandoPUSH(linhaInstrucao, linha, comando);// TODO
			break;
		case POP:
			processarComandoPOP(linhaInstrucao, linha, comando);// TODO
			break;
		case NOP:
			processarComandoNOP(linhaInstrucao, linha, comando);// TODO
			break;
		case HALT:
			processarComandoHALT(linhaInstrucao, linha, comando);// TODO
			break;
		case IRET:
			processarComandoIRET(linhaInstrucao, linha, comando);// TODO
			break;
		case INT:
			processarComandoINT(linhaInstrucao, linha, comando);// TODO
			break;
		case ORG:
			processarComandoORG(linhaInstrucao, linha, comando);// TODO
			break;
		default:
			processarNaoInstrucao(linhaInstrucao, linha, comando);// TODO
		}
	}

	private void processarNaoInstrucao(LinhaInstrucao linhaInstrucao, String linha, String comando) throws MontadorException {
		if (!ehComentario(comando)) {
			if (ehNumero(comando)) {
				processarNumero(linhaInstrucao, linha, comando);
			} else {
				processarLabel(linhaInstrucao, linha, comando);
			}
		}
	}

	private boolean ehComentario(String comando) {
		return comando.startsWith("//");
	}

	private void processarNumero(LinhaInstrucao linhaInstrucao, String linha, String comando) throws MontadorException {
		if (Utils.isNotEmpty(linha)) {
			throw new MontadorException("Literal inválido na linha [" + (linhaInstrucao.getIdxLinha() + 1) + "]");
		}
		comando = comando.substring(1, comando.length() - 1);
		int literal = Integer.valueOf(comando);
		if (literal <= 128 && literal >= -128) {
			linhaInstrucao.setInstrucao(new byte[] { (byte) literal });
		} else {
			byte[] quebrarBytesValor = quebrarBytesValor((short) literal);
			linhaInstrucao.setInstrucao(quebrarBytesValor);
		}
	}

	private boolean ehNumero(String comando) {
		return comando.matches("\\{\\d*\\}");
	}

	private void processarComandoORG(LinhaInstrucao linhaInstrucao, String linha, String comando) throws MontadorException {
		linha = linha.toUpperCase().trim();

		String primeiroParametro = getPrimeiroParametro(linha);
		linha = linha.substring(primeiroParametro.length());
		if (Utils.isNotNumber(primeiroParametro) || Utils.isNotEmpty(linha)) {
			throw new MontadorException("ORG inválido na linha [" + (linhaInstrucao.getIdxLinha() + 1) + "]");
		}

		int idxOrg = Integer.valueOf(primeiroParametro);

		if (INDICE_MAXIMO < idxOrg) {
			throw new MontadorException("ORG inválido na linha [" + (linhaInstrucao.getIdxLinha() + 1) + "]. Índice máximo [" + INDICE_MAXIMO + "] e índice informado [" + idxOrg + "]");
		}

		linhaInstrucao.setEhOrg(true);
		linhaInstrucao.setIdxOrg(idxOrg);
	}

	private void processarComandoCALL(LinhaInstrucao linhaInstrucao, String linha, String comando) throws MontadorException {
		if (Utils.isEmpty(linha) || Utils.isNotEmpty(linha)) {
			throw new MontadorException("CALL inválido na linha [" + (linhaInstrucao.getIdxLinha() + 1) + "]");
		}

		byte[] bytes = getBytesInstrucaoComIndex(InstrucaoProcessador.CALL, 0);
		linhaInstrucao.setInstrucao(bytes);
		linhaInstrucao.setLabel(linha);
	}

	private void processarComandoJMP(LinhaInstrucao linhaInstrucao, String linha, String comando) throws MontadorException {
		if (Utils.isEmpty(linha)) {
			throw new MontadorException("JMP inválido na linha [" + (linhaInstrucao.getIdxLinha() + 1) + "]");
		}

		byte[] bytes = getBytesInstrucaoComIndex(InstrucaoProcessador.JMP, 0);
		linhaInstrucao.setInstrucao(bytes);
		linhaInstrucao.setLabel(linha);
	}

	private void processarComandoINT(LinhaInstrucao linhaInstrucao, String linha, String comando) throws MontadorException {
		linha = linha.toUpperCase().trim();

		String primeiroParametro = getPrimeiroParametro(linha);
		linha = linha.substring(primeiroParametro.length());
		if (Utils.isEmpty(primeiroParametro) || Utils.isNotEmpty(linha) || Utils.isNotNumber(primeiroParametro)) {
			throw new MontadorException("INT inválido na linha [" + (linhaInstrucao.getIdxLinha() + 1) + "]");
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
		bytes[2] = (byte) (((short) 0xFF00 & index) >> 8);
		return bytes;
	}

	private void processarComandoIRET(LinhaInstrucao linhaInstrucao, String linha, String comando) throws MontadorException {
		if (!linha.trim().isEmpty()) {
			throw new MontadorException("IRET inválido na linha [" + (linhaInstrucao.getIdxLinha() + 1) + "]");
		}

		linhaInstrucao.setInstrucao(new byte[] { InstrucaoProcessador.IRET.getCode() });
	}

	private void processarComandoHALT(LinhaInstrucao linhaInstrucao, String linha, String comando) throws MontadorException {
		if (!linha.trim().isEmpty()) {
			throw new MontadorException("HALT inválido na linha [" + (linhaInstrucao.getIdxLinha() + 1) + "]");
		}

		linhaInstrucao.setInstrucao(new byte[] { InstrucaoProcessador.HALT.getCode() });
	}

	private void processarComandoNOP(LinhaInstrucao linhaInstrucao, String linha, String comando) throws MontadorException {
		if (!linha.trim().isEmpty()) {
			throw new MontadorException("NOP inválido na linha [" + (linhaInstrucao.getIdxLinha() + 1) + "]");
		}

		linhaInstrucao.setInstrucao(new byte[] { InstrucaoProcessador.NOP.getCode() });
	}

	private void processarComandoPOP(LinhaInstrucao linhaInstrucao, String linha, String comando) throws MontadorException {
		linha = linha.toUpperCase().trim();

		String primeiroParametro = getPrimeiroParametro(linha);
		linha = linha.substring(primeiroParametro.length());
		if (Utils.isEmpty(primeiroParametro) || Utils.isNotEmpty(linha)) {
			throw new MontadorException("POP inválido na linha [" + (linhaInstrucao.getIdxLinha() + 1) + "]");
		}

		StringBuilder enumSB = new StringBuilder();
		enumSB.append("POP_");
		enumSB.append(primeiroParametro);

		InstrucaoProcessador instrucao = getEnumPorStringBuilder(enumSB);
		if (null == instrucao) {
			throw new MontadorException("POP inválido na linha [" + (linhaInstrucao.getIdxLinha() + 1) + "]");
		}

		linhaInstrucao.setInstrucao(new byte[] { instrucao.getCode() });
	}

	private void processarComandoPUSH(LinhaInstrucao linhaInstrucao, String linha, String comando) throws MontadorException {
		linha = linha.toUpperCase().trim();

		String primeiroParametro = getPrimeiroParametro(linha);
		linha = linha.substring(primeiroParametro.length());
		if (Utils.isEmpty(primeiroParametro) || Utils.isNotEmpty(linha)) {
			throw new MontadorException("PUSH inválido na linha [" + (linhaInstrucao.getIdxLinha() + 1) + "]");
		}

		StringBuilder enumSB = new StringBuilder();
		enumSB.append("PUSH_");
		enumSB.append(primeiroParametro);

		InstrucaoProcessador instrucao = getEnumPorStringBuilder(enumSB);
		if (null == instrucao) {
			throw new MontadorException("PUSH inválido na linha [" + (linhaInstrucao.getIdxLinha() + 1) + "]");
		}

		linhaInstrucao.setInstrucao(new byte[] { instrucao.getCode() });
	}

	private void processarComandoOUT(LinhaInstrucao linhaInstrucao, String linha, String comando) throws MontadorException {
		linha = linha.toUpperCase().trim();

		String primeiroParametro = getPrimeiroParametro(linha);
		linha = linha.substring(primeiroParametro.length());
		if (Utils.isEmpty(primeiroParametro) || Utils.isNotEmpty(linha)) {
			throw new MontadorException("OUT inválido na linha [" + (linhaInstrucao.getIdxLinha() + 1) + "]");
		}

		StringBuilder enumSB = new StringBuilder();
		enumSB.append("OUT_");
		enumSB.append(primeiroParametro);

		InstrucaoProcessador instrucao = getEnumPorStringBuilder(enumSB);
		if (null == instrucao) {
			throw new MontadorException("OUT inválido na linha [" + (linhaInstrucao.getIdxLinha() + 1) + "]");
		}

		linhaInstrucao.setInstrucao(new byte[] { instrucao.getCode() });
	}

	private void processarComandoIN(LinhaInstrucao linhaInstrucao, String linha, String comando) throws MontadorException {
		linha = linha.toUpperCase().trim();

		String primeiroParametro = getPrimeiroParametro(linha);
		linha = linha.substring(primeiroParametro.length());
		if (Utils.isEmpty(primeiroParametro) || Utils.isNotEmpty(linha)) {
			throw new MontadorException("IN inválido na linha [" + (linhaInstrucao.getIdxLinha() + 1) + "]");
		}

		StringBuilder enumSB = new StringBuilder();
		enumSB.append("IN_");
		enumSB.append(primeiroParametro);

		InstrucaoProcessador instrucao = getEnumPorStringBuilder(enumSB);
		if (null == instrucao) {
			throw new MontadorException("IN inválido na linha [" + (linhaInstrucao.getIdxLinha() + 1) + "]");
		}

		linhaInstrucao.setInstrucao(new byte[] { instrucao.getCode() });
	}

	private void processarComandoRET(LinhaInstrucao linhaInstrucao, String linha, String comando) throws MontadorException {
		if (!linha.trim().isEmpty()) {
			throw new MontadorException("RET inválido na linha [" + (linhaInstrucao.getIdxLinha() + 1) + "]");
		}

		linhaInstrucao.setInstrucao(new byte[] { InstrucaoProcessador.RET.getCode() });
	}

	private void processarComandoTEST(LinhaInstrucao linhaInstrucao, String linha, String comando) throws MontadorException {
		linha = linha.toUpperCase();

		String primeiroParametro = getPrimeiroParametro(linha);
		if (Utils.isEmpty(primeiroParametro)) {
			throw new MontadorException("TEST inválido na linha [" + (linhaInstrucao.getIdxLinha() + 1) + "]");
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
			throw new MontadorException("TEST inválido na linha [" + (linhaInstrucao.getIdxLinha() + 1) + "]");
		}

		linhaInstrucao.setInstrucao(new byte[] { instrucao.getCode() });
	}

	private boolean temVirgula(String linha) {
		return -1 != linha.indexOf(',');
	}

	private void processarComandoDEC(LinhaInstrucao linhaInstrucao, String linha, String comando) throws MontadorException {
		linha = linha.toUpperCase();

		String primeiroParametro = getPrimeiroParametro(linha);
		linha = linha.substring(primeiroParametro.length());
		if (Utils.isEmpty(primeiroParametro) || Utils.isNotEmpty(linha)) {
			throw new MontadorException("DEC inválido na linha [" + (linhaInstrucao.getIdxLinha() + 1) + "]");
		}

		StringBuilder enumSB = new StringBuilder();
		enumSB.append("DEC_");
		enumSB.append(primeiroParametro);

		InstrucaoProcessador instrucao = getEnumPorStringBuilder(enumSB);
		if (null == instrucao) {
			throw new MontadorException("DEC inválido na linha [" + (linhaInstrucao.getIdxLinha() + 1) + "]");
		}

		linhaInstrucao.setInstrucao(new byte[] { instrucao.getCode() });
	}

	private void processarComandoINC(LinhaInstrucao linhaInstrucao, String linha, String comando) throws MontadorException {
		linha = linha.toUpperCase();

		String primeiroParametro = getPrimeiroParametro(linha);
		linha = linha.substring(primeiroParametro.length());
		if (Utils.isEmpty(primeiroParametro) || Utils.isNotEmpty(linha)) {
			throw new MontadorException("INC inválido na linha [" + (linhaInstrucao.getIdxLinha() + 1) + "]");
		}

		StringBuilder enumSB = new StringBuilder();
		enumSB.append("INC_");
		enumSB.append(primeiroParametro);

		InstrucaoProcessador instrucao = getEnumPorStringBuilder(enumSB);
		if (null == instrucao) {
			throw new MontadorException("INC inválido na linha [" + (linhaInstrucao.getIdxLinha() + 1) + "]");
		}

		linhaInstrucao.setInstrucao(new byte[] { instrucao.getCode() });
		linhaInstrucao.setInstrucaoProcessador(instrucao);
	}

	private void processarLabel(LinhaInstrucao linhaInstrucao, String linha, String comando) throws MontadorException {
		if (Utils.isNotEmpty(linha)) {
			throw new MontadorException("Label ou comando inválido na linha [" + (linhaInstrucao.getIdxLinha() + 1) + "]");
		}

		if (!comando.endsWith(":")) {
			throw new MontadorException("Esperado ':' no final da label da linha [" + (linhaInstrucao.getIdxLinha() + 1) + "]");
		}

		comando = comando.substring(0, comando.length() - 1);

		linhaInstrucao.setLabel(comando);
		linhaInstrucao.setEhLabel(true);
	}

	private void processarComandoSUB(LinhaInstrucao linhaInstrucao, String linha, String comando) throws MontadorException {
		linha = linha.toUpperCase();

		String primeiroParametro = getPrimeiroParametro(linha);
		String segundoParametro = getSegundoParametro(linhaInstrucao, linha);

		if (Utils.isEmpty(primeiroParametro) || Utils.isEmpty(segundoParametro)) {
			throw new MontadorException("SUB inválido na linha [" + (linhaInstrucao.getIdxLinha() + 1) + "]");
		}

		StringBuilder enumSB = new StringBuilder();
		enumSB.append("SUB_");
		enumSB.append(primeiroParametro);
		enumSB.append("_");
		enumSB.append(segundoParametro);

		InstrucaoProcessador instrucao = getEnumPorStringBuilder(enumSB);
		if (null == instrucao) {
			throw new MontadorException("SUB inválido na linha [" + (linhaInstrucao.getIdxLinha() + 1) + "]");
		}

		linhaInstrucao.setInstrucao(new byte[] { instrucao.getCode() });
		linhaInstrucao.setInstrucaoProcessador(instrucao);s
	}

	private void processarComandoADD(LinhaInstrucao linhaInstrucao, String linha, String comando) throws MontadorException {
		linha = linha.toUpperCase();

		String primeiroParametro = getPrimeiroParametro(linha);
		String segundoParametro = getSegundoParametro(linhaInstrucao, linha);

		if (Utils.isEmpty(primeiroParametro) || Utils.isEmpty(segundoParametro)) {
			throw new MontadorException("ADD inválido na linha [" + (linhaInstrucao.getIdxLinha() + 1) + "]");
		}

		StringBuilder enumSB = new StringBuilder();
		enumSB.append("ADD_");
		enumSB.append(primeiroParametro);
		enumSB.append("_");
		enumSB.append(segundoParametro);

		InstrucaoProcessador instrucao = getEnumPorStringBuilder(enumSB);
		if (null == instrucao) {
			throw new MontadorException("ADD inválido na linha [" + (linhaInstrucao.getIdxLinha() + 1) + "]");
		}

		linhaInstrucao.setInstrucao(new byte[] { instrucao.getCode() });
		linhaInstrucao.setInstrucaoProcessador(instrucao);
	}

	private void processarComandoINIT(LinhaInstrucao linhaInstrucao, String linha, String comando) throws MontadorException {
		linha = linha.toUpperCase();

		if (!"AX".equalsIgnoreCase(linha)) {
			throw new MontadorException("Esperado um INIT AX na linha [" + (linhaInstrucao.getIdxLinha() + 1) + "]");
		}

		linhaInstrucao.setInstrucao(new byte[] { InstrucaoProcessador.INIT_AX.getCode() });
		linhaInstrucao.setInstrucaoProcessador(InstrucaoProcessador.INIT_AX);
	}

	private void processarComandoMOV(LinhaInstrucao linhaInstrucao, String linha, String comando) throws MontadorException {
		linha = linha.toUpperCase();

		String primeiroParametro = getPrimeiroParametro(linha);
		String segundoParametro = getSegundoParametro(linhaInstrucao, linha);

		if (Utils.isEmpty(primeiroParametro) || Utils.isEmpty(segundoParametro)) {
			throw new MontadorException("MOV inválido na linha [" + (linhaInstrucao.getIdxLinha() + 1) + "]");
		}

		StringBuilder enumSB = new StringBuilder();
		enumSB.append("MOV_");
		adicionarParametroEnumMOV(enumSB, primeiroParametro);
		enumSB.append("_");
		adicionarParametroEnumMOV(enumSB, segundoParametro);

		InstrucaoProcessador instrucao = getEnumPorStringBuilder(enumSB);
		if (null == instrucao) {
			throw new MontadorException("MOV inválido na linha [" + (linhaInstrucao.getIdxLinha() + 1) + "]");
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

	private void definirInstrucaoMOV(LinhaInstrucao linhaInstrucao, InstrucaoProcessador instrucao, String primeiroParametro, String segundoParametro) throws MontadorException {
		if (instrucao.name().contains("MEM") || instrucao.name().contains("LITERAL")) {
			short valor = getValorParametrosMOV(primeiroParametro, segundoParametro);
			byte[] bytesValor = quebrarBytesValor(valor);
			byte[] bytesInstrucoes = new byte[] { instrucao.getCode(), bytesValor[0], bytesValor[1] };
			linhaInstrucao.setInstrucao(bytesInstrucoes);
		} else {
			linhaInstrucao.setInstrucao(new byte[] { instrucao.getCode() });
		}

		linhaInstrucao.setInstrucaoProcessador(instrucao);
	}

	private byte[] quebrarBytesValor(short valor) {
		byte byte1 = (byte) (valor & 0x00FF);
		byte byte2 = (byte) (((short) 0xFF00 & valor) >> 8);
		return new byte[] { byte1, byte2 };
	}

	private short getValorParametrosMOV(String primeiroParametro, String segundoParametro) throws MontadorException {
		Matcher matcher = PATTERN_NUMEROS.matcher(primeiroParametro);
		if (!matcher.find()) {
			matcher = PATTERN_NUMEROS.matcher(segundoParametro);
		}
		if (!matcher.find()) {
			throw new MontadorException("Erro inesperado ao pegar o valor dos parâmetros do MOV");
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

	private void validarExistenciaVirgula(LinhaInstrucao linhaInstrucao, String linha) throws CaractereEsperadoMontadorException {
		if (!temVirgula(linha)) {
			throw new CaractereEsperadoMontadorException(linhaInstrucao.getIdxLinha(), ",");
		}
	}

	private String getSegundoParametro(LinhaInstrucao linhaInstrucao, String linha) throws MontadorException {
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
		return linha.substring(idxInstrucao, idxVirgula);
	}

	private String removerComando(String linha, String comando) {
		return linha.substring(comando.length()).trim();
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

	private void validarTamanhoInstrucoes(int totalBytes) throws SemEspacoMemoriaMontadorException {
		if (INDICE_MAXIMO < totalBytes) {
			throw new SemEspacoMemoriaMontadorException(totalBytes);
		}
	}

	private byte[] converterLinhasInstrucaoParaArray(List<LinhaInstrucao> linhasInstrucao, int totalBytes, int tamanhoTabelaRealocacao) {
		int idx = 0;
		byte[] bytes = new byte[totalBytes];

		popularTabelaRealocacao(bytes, tamanhoTabelaRealocacao, linhasInstrucao);

		for (LinhaInstrucao linha : linhasInstrucao) {
			if (linha.isEhOrg()) {
				idx = linha.getIdxOrg();
			} else {
				for (byte byteInstrucao : linha.getInstrucao()) {
					bytes[(idx++) + tamanhoTabelaRealocacao] = byteInstrucao;
				}
			}
		}
		return bytes;
	}

	private void popularTabelaRealocacao(byte[] bytes, int tamanhoTabelaRealocacao, List<LinhaInstrucao> linhasInstrucao) {
		popularTamanhoTabelaRealocacao(bytes, tamanhoTabelaRealocacao);

		int idxTabela = 2;
		int idx = 0;
		for (LinhaInstrucao instrucao : linhasInstrucao) {
			if (instrucao.isEhOrg()) {
				idx = instrucao.getIdxOrg();
			} else if (null != instrucao.getInstrucao()) {
				idx += instrucao.getInstrucao().length;
			}

			if (isInstrucaoNecessitaRealocacao(instrucao)) {
				byte[] bytesIndiceRealocacao = Utils.quebrarBytes((short) (idx - 2));
				bytes[idxTabela++] = bytesIndiceRealocacao[0];
				bytes[idxTabela++] = bytesIndiceRealocacao[1];
			}
		}
	}

	private void popularTamanhoTabelaRealocacao(byte[] bytes, int tamanhoTabelaRealocacao) {
		byte[] tamanhoTabelaBytes = Utils.quebrarBytes((short) tamanhoTabelaRealocacao);
		bytes[0] = tamanhoTabelaBytes[0];
		bytes[1] = tamanhoTabelaBytes[1];
	}

	private int getTamanhoTabelaRealocacao(List<LinhaInstrucao> linhasInstrucao, boolean gerarTabelaRealocacao) {
		int tamanho = 0;
		if (gerarTabelaRealocacao) {
			for (LinhaInstrucao instrucao : linhasInstrucao) {
				if (isInstrucaoNecessitaRealocacao(instrucao)) {
					tamanho++;
				}
			}
		}
		return tamanho;
	}

	private boolean isInstrucaoNecessitaRealocacao(LinhaInstrucao instrucao) {
		InstrucaoProcessador instrucaoProcessador = instrucao.getInstrucaoProcessador();
		if (null != instrucaoProcessador) {
			switch (instrucaoProcessador) {
			case CALL:
			case JMP:
			case MOV_MEM_AX:
			case MOV_MEM_BP_P_AX:
			case MOV_MEM_BP_S_AX:
			case MOV_MEM_BX_P_AX:
			case MOV_AX_MEM:
			case MOV_AX_MEM_BP_P:
			case MOV_AX_MEM_BP_S:
			case MOV_AX_MEM_BX_P:
				return true;
			default:
			}
		}
		return false;
	}

	private int getTotalBytesInstrucoes(List<LinhaInstrucao> linhasInstrucao, int tamanhoTabelaRealocacao) {
		AtomicInteger indice = new AtomicInteger(0);
		AtomicInteger maiorIndice = new AtomicInteger(0);
		for (LinhaInstrucao linha : linhasInstrucao) {
			if (linha.isEhOrg()) {
				indice.set(linha.getIdxOrg());
			} else {
				indice.addAndGet(linha.getInstrucao().length);
			}
			if (indice.get() > maiorIndice.get()) {
				maiorIndice.set(indice.get());
			}
		}
		return maiorIndice.get() + tamanhoTabelaRealocacao;
	}

	private List<LinhaInstrucao> getLinhasInstrucao(List<String> linhasCodigo) {
		List<LinhaInstrucao> linhasInstrucao = new ArrayList<>();

		for (int idx = 0; idx < linhasCodigo.size(); idx++) {
			String linha = linhasCodigo.get(idx);
			if (!linha.isEmpty()) {
				linhasInstrucao.add(new LinhaInstrucao(linha, idx));
			}
		}

		return linhasInstrucao;
	}

	private List<String> getLinhasCodigo(String codigo) {
		return Arrays.stream(codigo.split("\\n")) //
				.parallel()//
				.map(String::trim) //
				.collect(Collectors.toList());
	}

}
