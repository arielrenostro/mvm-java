package br.ariel.mvm.compilador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import br.ariel.mvm.exception.CaractereEsperadoCompiladorException;
import br.ariel.mvm.exception.CompiladorException;
import br.ariel.mvm.exception.SemEspacoMemoriaCompiladorException;
import br.ariel.mvm.model.InstrucaoProcessador;
import br.ariel.mvm.utils.Utils;

public class CompiladorAssemblyMVM {

	private static final Pattern PATTERN_NUMEROS = Pattern.compile("\\d+");

	public byte[] compilar(String codigo) throws SemEspacoMemoriaCompiladorException {
		validarCodigo(codigo);
		List<String> linhasCodigo = getLinhasCodigo(codigo);
		List<LinhaInstrucao> linhasInstrucao = getLinhasInstrucao(linhasCodigo);
		processarLinhasInstrucao(linhasInstrucao);
		return converterLinhasInstrucaoParaArray(linhasInstrucao);
	}

	private void processarLinhasInstrucao(List<LinhaInstrucao> linhasInstrucao) throws SemEspacoMemoriaCompiladorException {
		linhasInstrucao.parallelStream().forEach(linhaInstrucao -> processarLinhaInstrucao(linhaInstrucao));

		processarLabels(linhasInstrucao); // TODO IMPLEMENTAR

		validarTamanhoInstrucoes(linhasInstrucao);
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
		if (comando.equalsIgnoreCase("MOV")) {
			processarComandoMOV(linhaInstrucao, linha, comando);
		}

		// TODO IMPLEMENTAR
	}

	private void processarComandoMOV(LinhaInstrucao linhaInstrucao, String linha, String comando) throws CompiladorException {
		linha = removerComando(linha, comando);
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

		InstrucaoProcessador instrucao = InstrucaoProcessador.valueOf(enumSB.toString());
		if (null == instrucao) {
			throw new CompiladorException("MOV inválido na linha [" + linhaInstrucao.getIdxLinha() + "]");
		}

		definirInstrucaoMOV(linhaInstrucao, instrucao, primeiroParametro, segundoParametro);
	}

	private void definirInstrucaoMOV(LinhaInstrucao linhaInstrucao, InstrucaoProcessador instrucao, String primeiroParametro, String segundoParametro) throws CompiladorException {
		if (instrucao.name().contains("MEM")) {
			short valor = getValorParametrosMOV(primeiroParametro, segundoParametro);
			byte[] bytesValor = quebrarBytesValor(valor);
		}
	}

	private byte[] quebrarBytesValor(short valor) { // TODO ARIEL -> CONTINUAR
		return;
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
		if (-1 == linha.indexOf(',')) {
			throw new CaractereEsperadoCompiladorException(linhaInstrucao.getIdxLinha(), ",");
		}
	}

	private String getSegundoParametro(LinhaInstrucao linhaInstrucao, String linha) throws CompiladorException {
		validarExistenciaVirgula(linhaInstrucao, linha);
		int idxVirgula = linha.indexOf(",");
		return linha.substring(idxVirgula).trim();
	}

	private String getPrimeiroParametro(String linha) throws CompiladorException {
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
		String comando = linha.substring(0, linha.indexOf(' '));
		if (Utils.isEmpty(comando)) {
			comando = linha;
		}
		return comando;
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

	private void validarCodigo(String codigo) {
		// TODO IMPLEMENTAR
	}
}
