package br.ariel.mvm.test;

import java.io.IOException;

import br.ariel.mvm.controller.BiosController;
import br.ariel.mvm.exception.MVMException;
import br.ariel.mvm.exception.PosicaoMemoriaInvalidaException;
import br.ariel.mvm.model.InstrucaoProcessador;
import br.ariel.mvm.model.Memoria;

public class GeradorBIOS {

	private static final Memoria memoria = new Memoria((short) 512);
	private static short idx = 0;

	public static void main(String[] args) throws IOException, MVMException {
		adicionarInstrucao(InstrucaoProcessador.JMP); // 0
		adicionarInstrucao(0x13); // 1
		adicionarInstrucao(0x00); // 2
		adicionarInstrucao(0x00); // 3
		adicionarInstrucao(0x00); // 4
		adicionarInstrucao(0x00); // 5
		adicionarInstrucao(0x00); // 6
		adicionarInstrucao(0x00); // 7
		adicionarInstrucao(0x00); // 8
		adicionarInstrucao(0x00); // 9
		adicionarInstrucao(InstrucaoProcessador.INC_AX); // 10
		adicionarInstrucao(InstrucaoProcessador.MOV_MEM_AX); // 11
		adicionarInstrucao(0x02); // 12
		adicionarInstrucao(0x00); // 13
		adicionarInstrucao(InstrucaoProcessador.INC_AX); // 14
		adicionarInstrucao(InstrucaoProcessador.MOV_MEM_AX); // 15
		adicionarInstrucao(0x03); // 16
		adicionarInstrucao(0x00); // 17
		adicionarInstrucao(InstrucaoProcessador.RET); // 18
		adicionarInstrucao(InstrucaoProcessador.INIT_AX); // 19
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL); // 20
		adicionarInstrucao(0x09); // 21
		adicionarInstrucao(0x00); // 22
		adicionarInstrucao(InstrucaoProcessador.MOV_SP_AX); // 23
		adicionarInstrucao(InstrucaoProcessador.INIT_AX); // 24
		adicionarInstrucao(InstrucaoProcessador.CALL); // 25
		adicionarInstrucao(0x0A); // 26
		adicionarInstrucao(0x00); // 27
		adicionarInstrucao(InstrucaoProcessador.CALL); // 28
		adicionarInstrucao(0x0A); // 29
		adicionarInstrucao(0x00); // 30
		adicionarInstrucao(InstrucaoProcessador.HALT); // 31

		new BiosController().gerarArquivoBios("/home/ariel/Temp/bios.bin", memoria);
		new BiosController().carregarArquivoBios("/home/ariel/Temp/bios.bin");
	}

	private static void adicionarInstrucao(int b) throws PosicaoMemoriaInvalidaException {
		memoria.setData(idx++, (byte) b);
	}

	private static void adicionarInstrucao(InstrucaoProcessador instrucao) throws PosicaoMemoriaInvalidaException {
		adicionarInstrucao(instrucao.getCode());
	}

}
