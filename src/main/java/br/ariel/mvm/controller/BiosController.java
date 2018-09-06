package br.ariel.mvm.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import br.ariel.mvm.exception.MVMException;
import br.ariel.mvm.exception.PosicaoMemoriaInvalida;
import br.ariel.mvm.model.Memoria;

public class BiosController {

	public File gerarArquivoBios(String url, Memoria memoria) throws PosicaoMemoriaInvalida, IOException {
		byte[] mem = new MemoriaController().extrairMemoriaEmArray(memoria);
		return gerarArquivoBios(url, mem);
	}

	public byte[] carregarArquivoBios(String url) throws IOException, MVMException {
		Path path = Paths.get(url);
		if (!Files.exists(path)) {
			throw new MVMException("Arquivo não existe [" + url + "]");
		}

		File file = path.toFile();

		FileInputStream fis = new FileInputStream(file);
		long size = fis.getChannel().size();
		byte[] memoria = new byte[(int) size];
		fis.read(memoria);
		fis.close();

		return memoria;
	}

	public File gerarArquivoBios(String url, byte[] memoria) throws IOException {
		Path path = Paths.get(url);
		if (Files.exists(path)) {
			Files.delete(path);
		}

		Files.createFile(path);
		File file = path.toFile();

		FileOutputStream fos = new FileOutputStream(file);
		fos.write(memoria);
		fos.flush();
		fos.close();

		return file;
	}

}
