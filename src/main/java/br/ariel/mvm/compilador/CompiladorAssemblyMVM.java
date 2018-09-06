package br.ariel.mvm.compilador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CompiladorAssemblyMVM {

	public byte[] compilar(String codigo) {
		validarCodigo(codigo);
		List<String> linhasCodigo = getLinhasCodigo(codigo);
		List<LinhaInstrucao> linhasInstrucao = getLinhasInstrucao(linhasCodigo);
		processarLinhasInstrucao(linhasInstrucao); // VALIDAR O TAMANHO MAXIMO DA MEMORIA
		return converterLinhasInstrucaoParaArray(linhasInstrucao);
	}

	private byte[] converterLinhasInstrucaoParaArray(List<LinhaInstrucao> linhasInstrucao) {
		return linhasInstrucao.parallelStream() //
				.flatMap(linhaInstrucao -> Arrays.stream(linhaInstrucao.getInstrucao())) //
				.toArray();
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
