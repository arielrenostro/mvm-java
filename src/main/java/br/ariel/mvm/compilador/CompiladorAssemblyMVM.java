package br.ariel.mvm.compilador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import br.ariel.mvm.exception.CaractereEsperadoCompiladorException;
import br.ariel.mvm.exception.CompiladorException;
import br.ariel.mvm.exception.SemEspacoMemoriaCompiladorException;
import br.ariel.mvm.utils.Utils;

public class CompiladorAssemblyMVM {

	private static final String SP = "SP";
	private static final String BP = "BP";
	private static final String CX = "CX";
	private static final String BX = "BX";
	private static final String AX = "AX";

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
		if (comando.toUpperCase().startsWith("MOV")) {
			processarComandoMOV(linhaInstrucao, linha, comando);
		}

		// TODO IMPLEMENTAR
	}

	private void processarComandoMOV(LinhaInstrucao linhaInstrucao, String linha, String comando) throws CompiladorException {
		linha = removerComando(linha, comando);
		linha = linha.toUpperCase();

		if (-1 == linha.indexOf(',')) {
			throw new CaractereEsperadoCompiladorException(linhaInstrucao.getIdxLinha(), ",");
		}

		String primeiroParametro = getPrimeiroParametroMOV(linhaInstrucao, linha);
		String segundoParametro = getSegundoParametroMOV(linhaInstrucao, linha);

		switch (primeiroParametro) {
		case AX:
			// TODO CONTINUAR
		}
	}

	private String getSegundoParametroMOV(LinhaInstrucao linhaInstrucao, String linha) throws CompiladorException {
		linha = linha.substring(linha.indexOf(",")).trim(); // TODO VALIDAR
		try {
			return getPrimeiroParametroMOV(linhaInstrucao, linha);
		} catch (CompiladorException e) {
			if (linha.startsWith("{")) {
				int idxFechaChaves = linha.indexOf("}");
				if (-1 == idxFechaChaves) {
					throw new CaractereEsperadoCompiladorException(linhaInstrucao.getIdxLinha(), "}");
				}
				String parametro = linha.substring(1, idxFechaChaves);
				if (Utils.isEmpty(parametro) || Utils.isNotNumber(parametro)) {
					throw new CompiladorException("Esperado um número na linha [" + linhaInstrucao.getIdxLinha() + "]");
				}
				return parametro;
			}
			throw e;
		}
	}

	private String getPrimeiroParametroMOV(LinhaInstrucao linhaInstrucao, String linha) throws CompiladorException {
		if (linha.startsWith(AX)) {
			return AX;
		} else if (linha.startsWith(BX)) {
			return BX;
		} else if (linha.startsWith(CX)) {
			return CX;
		} else if (linha.startsWith(BP)) {
			return BP;
		} else if (linha.startsWith(SP)) {
			return SP;
		} else if (linha.startsWith("[")) {
			int idxFechaColchete = linha.indexOf("]");
			if (-1 == idxFechaColchete) {
				throw new CaractereEsperadoCompiladorException(linhaInstrucao.getIdxLinha(), "]");
			}
			String parametro = linha.substring(1, idxFechaColchete);
			if (Utils.isEmpty(parametro) || Utils.isNotNumber(parametro)) {
				throw new CompiladorException("Esperado um número na linha [" + linhaInstrucao.getIdxLinha() + "]");
			}
			return parametro;
		} else {
			throw new CompiladorException("Esperado um registrador ou indice de memória na linha [" + linhaInstrucao.getIdxLinha() + "]");
		}
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
