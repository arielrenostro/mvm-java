package br.ariel.mvm.test;

import org.junit.Assert;
import org.junit.Test;

import br.ariel.mvm.exception.MVMException;
import br.ariel.mvm.model.Instrucao;

public class ProcessadorTest extends MVMBaseTest {

	@Test
	public void testeMovAxBx() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0x14);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.MOV_BX_AX);
		adicionarInstrucao(Instrucao.INIT_AX);
		adicionarInstrucao(Instrucao.MOV_AX_BX);
		adicionarInstrucao(Instrucao.HALT);

		processadorController.processar(processador, memoria, monitor);

		Assert.assertEquals(20, processador.getAx());
		Assert.assertEquals(20, processador.getBx());
		Assert.assertEquals(0, processador.getCx());
		Assert.assertEquals(0, processador.getBp());
		Assert.assertEquals(0, processador.getSp());
	}

	@Test
	public void testeMovAxCx() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.INIT_AX);
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0x14);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.MOV_CX_AX);
		adicionarInstrucao(Instrucao.INIT_AX);
		adicionarInstrucao(Instrucao.MOV_AX_CX);
		adicionarInstrucao(Instrucao.HALT);

		processadorController.processar(processador, memoria, monitor);

		Assert.assertEquals(20, processador.getAx());
		Assert.assertEquals(0, processador.getBx());
		Assert.assertEquals(20, processador.getCx());
		Assert.assertEquals(0, processador.getBp());
		Assert.assertEquals(0, processador.getSp());
	}

	@Test
	public void testePopAx() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.INIT_AX);
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0x15);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.MOV_SP_AX);
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0x34);
		adicionarInstrucao(0x12);
		adicionarInstrucao(Instrucao.PUSH_AX);
		adicionarInstrucao(Instrucao.INIT_AX);
		adicionarInstrucao(Instrucao.POP_AX);
		adicionarInstrucao(Instrucao.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x1234, processador.getAx());
		Assert.assertEquals(0, processador.getBx());
		Assert.assertEquals(0, processador.getCx());
		Assert.assertEquals(0, processador.getBp());
		Assert.assertEquals(21, processador.getSp());
	}

	@Test
	public void testPushAx() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.INIT_AX);
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0x15);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.MOV_SP_AX);
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0x0A);
		adicionarInstrucao(0xF0);
		adicionarInstrucao(Instrucao.PUSH_AX);
		adicionarInstrucao(Instrucao.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((byte) 0x0A, memoria.getData((short) (510)));
		Assert.assertEquals((byte) 0xF0, memoria.getData((short) (511)));
		Assert.assertEquals((short) 509, processador.getSp());
		Assert.assertEquals((short) 0xF00A, processador.getAx());
		Assert.assertEquals(0, processador.getBx());
		Assert.assertEquals(0, processador.getCx());
		Assert.assertEquals(0, processador.getBp());
		Assert.assertEquals(21, processador.getSp());
	}

	@Test
	public void testInitAx() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.INIT_AX);
		adicionarInstrucao(Instrucao.HALT);

		processadorController.processar(processador, memoria, monitor);

		Assert.assertEquals(0, processador.getAx());
		Assert.assertEquals(0, processador.getBx());
		Assert.assertEquals(0, processador.getCx());
		Assert.assertEquals(0, processador.getBp());
		Assert.assertEquals(0, processador.getSp());
	}

	@Test
	public void testInitAx2() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.INIT_AX);
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0x0A);
		adicionarInstrucao(0xF0);
		adicionarInstrucao(Instrucao.INIT_AX);
		adicionarInstrucao(Instrucao.HALT);

		processadorController.processar(processador, memoria, monitor);

		Assert.assertEquals(0, processador.getAx());
		Assert.assertEquals(0, processador.getBx());
		Assert.assertEquals(0, processador.getCx());
		Assert.assertEquals(0, processador.getBp());
		Assert.assertEquals(0, processador.getSp());
	}

	@Test
	public void testMovBxAx() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0x14);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.MOV_BX_AX);
		adicionarInstrucao(Instrucao.INIT_AX);
		adicionarInstrucao(Instrucao.HALT);

		processadorController.processar(processador, memoria, monitor);

		Assert.assertEquals(0, processador.getAx());
		Assert.assertEquals(20, processador.getBx());
		Assert.assertEquals(0, processador.getCx());
		Assert.assertEquals(0, processador.getBp());
		Assert.assertEquals(0, processador.getSp());
	}

	@Test
	public void testMovCxAx() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0x14);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.MOV_CX_AX);
		adicionarInstrucao(Instrucao.INIT_AX);
		adicionarInstrucao(Instrucao.HALT);

		processadorController.processar(processador, memoria, monitor);

		Assert.assertEquals(0, processador.getAx());
		Assert.assertEquals(0, processador.getBx());
		Assert.assertEquals(20, processador.getCx());
		Assert.assertEquals(0, processador.getBp());
		Assert.assertEquals(0, processador.getSp());
	}

	@Test
	public void testMovAxMem() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.MOV_AX_MEM);
		adicionarInstrucao(0x18);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.HALT);

		memoria.setData((short) 0x18, (byte) 0x10);
		memoria.setData((short) 0x19, (byte) 0x11);

		processadorController.processar(processador, memoria, monitor);

		Assert.assertEquals(0x1110, processador.getAx());
		Assert.assertEquals(0, processador.getBx());
		Assert.assertEquals(0, processador.getCx());
		Assert.assertEquals(0, processador.getBp());
		Assert.assertEquals(0, processador.getSp());
	}

	@Test
	public void testMovAxMemBxP() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0x01);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.MOV_BX_AX);
		adicionarInstrucao(Instrucao.MOV_AX_MEM_BX_P);
		adicionarInstrucao(0x18);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.HALT);

		memoria.setData((short) 0x19, (byte) 0x10);
		memoria.setData((short) 0x1A, (byte) 0x11);

		processadorController.processar(processador, memoria, monitor);

		Assert.assertEquals(0x1110, processador.getAx());
		Assert.assertEquals(1, processador.getBx());
		Assert.assertEquals(0, processador.getCx());
		Assert.assertEquals(0, processador.getBp());
		Assert.assertEquals(0, processador.getSp());
	}

	@Test
	public void testMovAxMemBpS() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0x01);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.MOV_BP_AX);
		adicionarInstrucao(Instrucao.MOV_AX_MEM_BP_S);
		adicionarInstrucao(0x18);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.HALT);

		memoria.setData((short) 0x17, (byte) 0x10);
		memoria.setData((short) 0x18, (byte) 0x11);

		processadorController.processar(processador, memoria, monitor);

		Assert.assertEquals(0x1110, processador.getAx());
		Assert.assertEquals(0, processador.getBx());
		Assert.assertEquals(0, processador.getCx());
		Assert.assertEquals(1, processador.getBp());
		Assert.assertEquals(0, processador.getSp());
	}

	@Test
	public void testMovAxMemBpP() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0x01);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.MOV_BP_AX);
		adicionarInstrucao(Instrucao.MOV_AX_MEM_BP_P);
		adicionarInstrucao(0x18);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.HALT);

		memoria.setData((short) 0x19, (byte) 0x10);
		memoria.setData((short) 0x1A, (byte) 0x11);

		processadorController.processar(processador, memoria, monitor);

		Assert.assertEquals(0x1110, processador.getAx());
		Assert.assertEquals(0, processador.getBx());
		Assert.assertEquals(0, processador.getCx());
		Assert.assertEquals(1, processador.getBp());
		Assert.assertEquals(0, processador.getSp());
	}

	@Test
	public void testMovMemAx() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0x52);
		adicionarInstrucao(0xAE);
		adicionarInstrucao(Instrucao.MOV_MEM_AX);
		adicionarInstrucao(0x18);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.HALT);

		processadorController.processar(processador, memoria, monitor);

		Assert.assertEquals((short) 0xAE52, processador.getAx());
		Assert.assertEquals(0, processador.getBx());
		Assert.assertEquals(0, processador.getCx());
		Assert.assertEquals(0, processador.getBp());
		Assert.assertEquals(0, processador.getSp());

		Assert.assertEquals((byte) 0x52, memoria.getData((short) 0x18));
		Assert.assertEquals((byte) 0xAE, memoria.getData((short) 0x19));
	}

	// MOV_MEM_BX_P_AX((byte) 0b00001010), //
	//
	// MOV_BP_SP((byte) 0b00001011), //
	// MOV_SP_BP((byte) 0b00001100), //
	//
	// ADD_AX_BX((byte) 0b00001101), //
	// ADD_AX_CX((byte) 0b00001110), //
	// ADD_BX_CX((byte) 0b00001111), //
	//
	// SUB_AX_BX((byte) 0b00010000), //
	// SUB_AX_CX((byte) 0b00010001), //
	// SUB_BX_CX((byte) 0b00010010), //
	//
	// INC_AX((byte) 0b00010011), //
	// INC_BX((byte) 0b00010100), //
	// INC_CX((byte) 0b00010101), //
	//
	// DEC_AX((byte) 0b00010110), //
	// DEC_BX((byte) 0b00010111), //
	// DEC_CX((byte) 0b00011000), //
	//
	// TEST_AX_0((byte) 0b00011001), //
	// JMP((byte) 0b00011010), //
	// CALL((byte) 0b00011011), //
	// RET((byte) 0b00011100), //
	//
	// IN_AX((byte) 0b00011101), //
	// OUT_AX((byte) 0b00011110), //
	//
	// PUSH_BX((byte) 0b00100000), //
	// PUSH_CX((byte) 0b00100001), //
	// PUSH_BP((byte) 0b00100010), //
	// POP_BP((byte) 0b00100011), //
	// POP_CX((byte) 0b00100100), //
	// POP_BX((byte) 0b00100101), //
	//
	// NOP((byte) 0b00100111), //
	// HALT((byte) 0b00101000), //
	// DEC_SP((byte) 0b00101001), //
	//
	// MOV_MEN_BP_S_AX((byte) 0b00101010), //
	// MOV_MEN_BP_P_AX((byte) 0b00101011), //
	// MOV_AX_LITERAL((byte) 0b00101100), //
	// TEST_AX_BX((byte) 0b00101101), //
	// INC_SP((byte) 0b00101110), //
	//
	// MOV_AX_SP((byte) 0b00101111), //
	// MOV_SP_AX((byte) 0b00110000), //
	// MOV_AX_BP((byte) 0b00110001), //
	// MOV_BP_AX((byte) 0b00110010), //
	//
	// IRET((byte) 0b00110011), //
	// INT((byte) 0b00110100), //
	//
	// INC_BP((byte) 0b11111110), //
	// DEC_BP((byte) 0b11111111);

}
