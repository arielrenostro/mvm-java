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

		mvmController.iniciar(processador, memoria, monitor);

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

		mvmController.iniciar(processador, memoria, monitor);

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

		Assert.assertEquals((byte) 0x0A, memoria.getData((short) (0x14)));
		Assert.assertEquals((byte) 0xF0, memoria.getData((short) (0x15)));
		Assert.assertEquals((short) 0x0013, processador.getSp());
		Assert.assertEquals((short) 0xF00A, processador.getAx());
		Assert.assertEquals(0, processador.getBx());
		Assert.assertEquals(0, processador.getCx());
		Assert.assertEquals(0, processador.getBp());
	}

	@Test
	public void testInitAx() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.INIT_AX);
		adicionarInstrucao(Instrucao.HALT);

		mvmController.iniciar(processador, memoria, monitor);

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

		mvmController.iniciar(processador, memoria, monitor);

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

		mvmController.iniciar(processador, memoria, monitor);

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

		mvmController.iniciar(processador, memoria, monitor);

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

		mvmController.iniciar(processador, memoria, monitor);

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

		mvmController.iniciar(processador, memoria, monitor);

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

		mvmController.iniciar(processador, memoria, monitor);

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

		mvmController.iniciar(processador, memoria, monitor);

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

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0xAE52, processador.getAx());
		Assert.assertEquals(0, processador.getBx());
		Assert.assertEquals(0, processador.getCx());
		Assert.assertEquals(0, processador.getBp());
		Assert.assertEquals(0, processador.getSp());

		Assert.assertEquals((byte) 0x52, memoria.getData((short) 0x18));
		Assert.assertEquals((byte) 0xAE, memoria.getData((short) 0x19));
	}

	@Test
	public void testMovMemBxPAx() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0xE5);
		adicionarInstrucao(0x01);
		adicionarInstrucao(Instrucao.MOV_BX_AX);
		adicionarInstrucao(Instrucao.MOV_MEM_BX_P_AX);
		adicionarInstrucao(0x18);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x01E5, processador.getAx());
		Assert.assertEquals((short) 0x01E5, processador.getBx());
		Assert.assertEquals(0, processador.getCx());
		Assert.assertEquals(0, processador.getBp());
		Assert.assertEquals(0, processador.getSp());

		Assert.assertEquals((byte) 0xE5, memoria.getData((short) 509));
		Assert.assertEquals((byte) 0x01, memoria.getData((short) 510));
	}

	@Test
	public void testMovBpSp() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0xE5);
		adicionarInstrucao(0x12);
		adicionarInstrucao(Instrucao.MOV_SP_AX);
		adicionarInstrucao(Instrucao.MOV_BP_SP);
		adicionarInstrucao(Instrucao.INIT_AX);
		adicionarInstrucao(Instrucao.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0, processador.getAx());
		Assert.assertEquals((short) 0, processador.getBx());
		Assert.assertEquals((short) 0, processador.getCx());
		Assert.assertEquals((short) 0x12E5, processador.getBp());
		Assert.assertEquals((short) 0x12E5, processador.getSp());
	}

	@Test
	public void testMovSpBp() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0xE5);
		adicionarInstrucao(0x12);
		adicionarInstrucao(Instrucao.MOV_BP_AX);
		adicionarInstrucao(Instrucao.MOV_SP_BP);
		adicionarInstrucao(Instrucao.INIT_AX);
		adicionarInstrucao(Instrucao.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0, processador.getAx());
		Assert.assertEquals((short) 0, processador.getBx());
		Assert.assertEquals((short) 0, processador.getCx());
		Assert.assertEquals((short) 0x12E5, processador.getBp());
		Assert.assertEquals((short) 0x12E5, processador.getSp());
	}

	@Test
	public void testAddAxBx() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0xE5);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.MOV_BX_AX);
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0x00);
		adicionarInstrucao(0x12);
		adicionarInstrucao(Instrucao.ADD_AX_BX);
		adicionarInstrucao(Instrucao.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x12E5, processador.getAx());
		Assert.assertEquals((short) 0x00E5, processador.getBx());
		Assert.assertEquals((short) 0, processador.getCx());
		Assert.assertEquals((short) 0, processador.getBp());
		Assert.assertEquals((short) 0, processador.getSp());
	}

	@Test
	public void testAddAxCx() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0xE5);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.MOV_CX_AX);
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0x00);
		adicionarInstrucao(0x12);
		adicionarInstrucao(Instrucao.ADD_AX_CX);
		adicionarInstrucao(Instrucao.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x12E5, processador.getAx());
		Assert.assertEquals((short) 0, processador.getBx());
		Assert.assertEquals((short) 0x00E5, processador.getCx());
		Assert.assertEquals((short) 0, processador.getBp());
		Assert.assertEquals((short) 0, processador.getSp());
	}

	@Test
	public void testAddBxCx() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0xE5);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.MOV_CX_AX);
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0x00);
		adicionarInstrucao(0x12);
		adicionarInstrucao(Instrucao.MOV_BX_AX);
		adicionarInstrucao(Instrucao.ADD_BX_CX);
		adicionarInstrucao(Instrucao.INIT_AX);
		adicionarInstrucao(Instrucao.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0, processador.getAx());
		Assert.assertEquals((short) 0x12E5, processador.getBx());
		Assert.assertEquals((short) 0x00E5, processador.getCx());
		Assert.assertEquals((short) 0, processador.getBp());
		Assert.assertEquals((short) 0, processador.getSp());
	}

	@Test
	public void testSubAxBx() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0xE5);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.MOV_BX_AX);
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0x00);
		adicionarInstrucao(0x12);
		adicionarInstrucao(Instrucao.SUB_AX_BX);
		adicionarInstrucao(Instrucao.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x111B, processador.getAx());
		Assert.assertEquals((short) 0x00E5, processador.getBx());
		Assert.assertEquals((short) 0, processador.getCx());
		Assert.assertEquals((short) 0, processador.getBp());
		Assert.assertEquals((short) 0, processador.getSp());
	}

	@Test
	public void testSubAxCx() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0xE5);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.MOV_CX_AX);
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0x00);
		adicionarInstrucao(0x12);
		adicionarInstrucao(Instrucao.SUB_AX_CX);
		adicionarInstrucao(Instrucao.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x111B, processador.getAx());
		Assert.assertEquals((short) 0, processador.getBx());
		Assert.assertEquals((short) 0x00E5, processador.getCx());
		Assert.assertEquals((short) 0, processador.getBp());
		Assert.assertEquals((short) 0, processador.getSp());
	}

	@Test
	public void testSubBxCx() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0xE5);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.MOV_CX_AX);
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0x00);
		adicionarInstrucao(0x12);
		adicionarInstrucao(Instrucao.MOV_BX_AX);
		adicionarInstrucao(Instrucao.SUB_BX_CX);
		adicionarInstrucao(Instrucao.INIT_AX);
		adicionarInstrucao(Instrucao.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0, processador.getAx());
		Assert.assertEquals((short) 0x111B, processador.getBx());
		Assert.assertEquals((short) 0x00E5, processador.getCx());
		Assert.assertEquals((short) 0, processador.getBp());
		Assert.assertEquals((short) 0, processador.getSp());
	}

	@Test
	public void testIncAx() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0xE5);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.INC_AX);
		adicionarInstrucao(Instrucao.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x00E6, processador.getAx());
		Assert.assertEquals((short) 0, processador.getBx());
		Assert.assertEquals((short) 0, processador.getCx());
		Assert.assertEquals((short) 0, processador.getBp());
		Assert.assertEquals((short) 0, processador.getSp());
	}

	@Test
	public void testIncBx() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0xE5);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.MOV_BX_AX);
		adicionarInstrucao(Instrucao.INC_BX);
		adicionarInstrucao(Instrucao.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x00E5, processador.getAx());
		Assert.assertEquals((short) 0x00E6, processador.getBx());
		Assert.assertEquals((short) 0, processador.getCx());
		Assert.assertEquals((short) 0, processador.getBp());
		Assert.assertEquals((short) 0, processador.getSp());
	}

	@Test
	public void testIncCx() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0xE5);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.MOV_CX_AX);
		adicionarInstrucao(Instrucao.INC_CX);
		adicionarInstrucao(Instrucao.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x00E5, processador.getAx());
		Assert.assertEquals((short) 0, processador.getBx());
		Assert.assertEquals((short) 0x00E6, processador.getCx());
		Assert.assertEquals((short) 0, processador.getBp());
		Assert.assertEquals((short) 0, processador.getSp());
	}

	@Test
	public void testDecAx() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0xE5);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.DEC_AX);
		adicionarInstrucao(Instrucao.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x00E4, processador.getAx());
		Assert.assertEquals((short) 0, processador.getBx());
		Assert.assertEquals((short) 0, processador.getCx());
		Assert.assertEquals((short) 0, processador.getBp());
		Assert.assertEquals((short) 0, processador.getSp());
	}

	@Test
	public void testDecBx() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0xE5);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.MOV_BX_AX);
		adicionarInstrucao(Instrucao.DEC_BX);
		adicionarInstrucao(Instrucao.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x00E5, processador.getAx());
		Assert.assertEquals((short) 0x00E4, processador.getBx());
		Assert.assertEquals((short) 0, processador.getCx());
		Assert.assertEquals((short) 0, processador.getBp());
		Assert.assertEquals((short) 0, processador.getSp());
	}

	@Test
	public void testDecCx() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0xE5);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.MOV_CX_AX);
		adicionarInstrucao(Instrucao.DEC_CX);
		adicionarInstrucao(Instrucao.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x00E5, processador.getAx());
		Assert.assertEquals((short) 0, processador.getBx());
		Assert.assertEquals((short) 0x00E4, processador.getCx());
		Assert.assertEquals((short) 0, processador.getBp());
		Assert.assertEquals((short) 0, processador.getSp());
	}

	@Test
	public void testIncBp() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0xE5);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.MOV_BP_AX);
		adicionarInstrucao(Instrucao.INC_BP);
		adicionarInstrucao(Instrucao.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x00E5, processador.getAx());
		Assert.assertEquals((short) 0, processador.getBx());
		Assert.assertEquals((short) 0, processador.getCx());
		Assert.assertEquals((short) 0x00E6, processador.getBp());
		Assert.assertEquals((short) 0, processador.getSp());
	}

	@Test
	public void testDecBp() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0xE5);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.MOV_BP_AX);
		adicionarInstrucao(Instrucao.DEC_BP);
		adicionarInstrucao(Instrucao.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x00E5, processador.getAx());
		Assert.assertEquals((short) 0, processador.getBx());
		Assert.assertEquals((short) 0, processador.getCx());
		Assert.assertEquals((short) 0x00E4, processador.getBp());
		Assert.assertEquals((short) 0, processador.getSp());
	}

	@Test
	public void testTestAx0() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.INIT_AX);
		adicionarInstrucao(Instrucao.TEST_AX_0);
		adicionarInstrucao(0x0A);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.INC_AX);
		adicionarInstrucao(Instrucao.INC_AX);
		adicionarInstrucao(Instrucao.INC_AX);
		adicionarInstrucao(Instrucao.INC_AX);
		adicionarInstrucao(Instrucao.INC_AX);
		adicionarInstrucao(Instrucao.INC_AX);
		adicionarInstrucao(Instrucao.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0, processador.getAx());
		Assert.assertEquals((short) 0, processador.getBx());
		Assert.assertEquals((short) 0, processador.getCx());
		Assert.assertEquals((short) 0, processador.getBp());
		Assert.assertEquals((short) 0, processador.getSp());
		Assert.assertEquals((short) 0x0B, processador.getIp());
	}

	@Test
	public void testTestAx0_2() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0x01);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.TEST_AX_0);
		adicionarInstrucao(0x0A);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.INC_AX);
		adicionarInstrucao(Instrucao.INC_AX);
		adicionarInstrucao(Instrucao.INC_AX);
		adicionarInstrucao(Instrucao.INC_AX);
		adicionarInstrucao(Instrucao.INC_AX);
		adicionarInstrucao(Instrucao.INC_AX);
		adicionarInstrucao(Instrucao.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x0007, processador.getAx());
		Assert.assertEquals((short) 0, processador.getBx());
		Assert.assertEquals((short) 0, processador.getCx());
		Assert.assertEquals((short) 0, processador.getBp());
		Assert.assertEquals((short) 0, processador.getSp());
		Assert.assertEquals((short) 0x000D, processador.getIp());
	}

	@Test
	public void testJmp() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.INIT_AX);
		adicionarInstrucao(Instrucao.JMP);
		adicionarInstrucao(0x0A);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.INC_AX);
		adicionarInstrucao(Instrucao.INC_AX);
		adicionarInstrucao(Instrucao.INC_AX);
		adicionarInstrucao(Instrucao.INC_AX);
		adicionarInstrucao(Instrucao.INC_AX);
		adicionarInstrucao(Instrucao.INC_AX);
		adicionarInstrucao(Instrucao.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0, processador.getAx());
		Assert.assertEquals((short) 0, processador.getBx());
		Assert.assertEquals((short) 0, processador.getCx());
		Assert.assertEquals((short) 0, processador.getBp());
		Assert.assertEquals((short) 0, processador.getSp());
		Assert.assertEquals((short) 0x000B, processador.getIp());
	}

	@Test
	public void testJmp_2() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.INIT_AX);
		adicionarInstrucao(Instrucao.JMP);
		adicionarInstrucao(0x08);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.INC_AX);
		adicionarInstrucao(Instrucao.INC_AX);
		adicionarInstrucao(Instrucao.INC_AX);
		adicionarInstrucao(Instrucao.INC_AX);
		adicionarInstrucao(Instrucao.INC_AX);
		adicionarInstrucao(Instrucao.INC_AX);
		adicionarInstrucao(Instrucao.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x0002, processador.getAx());
		Assert.assertEquals((short) 0, processador.getBx());
		Assert.assertEquals((short) 0, processador.getCx());
		Assert.assertEquals((short) 0, processador.getBp());
		Assert.assertEquals((short) 0, processador.getSp());
		Assert.assertEquals((short) 0x000B, processador.getIp());
	}

	@Test
	public void testCall() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0xFF);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.MOV_SP_AX);
		adicionarInstrucao(Instrucao.CALL);
		adicionarInstrucao(0x08);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.INC_AX);
		adicionarInstrucao(Instrucao.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x00FF, processador.getAx());
		Assert.assertEquals((short) 0, processador.getBx());
		Assert.assertEquals((short) 0, processador.getCx());
		Assert.assertEquals((short) 0, processador.getBp());
		Assert.assertEquals((short) 0x00FD, processador.getSp());
		Assert.assertEquals((short) 0x0009, processador.getIp());
	}

	@Test
	public void testRet() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0xFF);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.MOV_SP_AX);
		adicionarInstrucao(Instrucao.CALL);
		adicionarInstrucao(0x09);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.INC_BX);
		adicionarInstrucao(Instrucao.HALT);
		adicionarInstrucao(Instrucao.INC_AX);
		adicionarInstrucao(Instrucao.RET);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x0100, processador.getAx());
		Assert.assertEquals((short) 0x0001, processador.getBx());
		Assert.assertEquals((short) 0, processador.getCx());
		Assert.assertEquals((short) 0, processador.getBp());
		Assert.assertEquals((short) 0x00FF, processador.getSp());
		Assert.assertEquals((short) 0x0009, processador.getIp());
	}

	@Test
	public void testPushBx() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0xFF);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.MOV_SP_AX);
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0x09);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.MOV_BX_AX);
		adicionarInstrucao(Instrucao.PUSH_BX);
		adicionarInstrucao(Instrucao.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x0009, processador.getAx());
		Assert.assertEquals((short) 0x0009, processador.getBx());
		Assert.assertEquals((short) 0, processador.getCx());
		Assert.assertEquals((short) 0, processador.getBp());
		Assert.assertEquals((short) 0x00FD, processador.getSp());
		Assert.assertEquals((short) 0x000A, processador.getIp());

		Assert.assertEquals((byte) 0x09, memoria.getData((short) 0x00FE));
		Assert.assertEquals((byte) 0x00, memoria.getData((short) 0x00FF));
	}

	@Test
	public void testPushCx() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0xFF);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.MOV_SP_AX);
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0x09);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.MOV_CX_AX);
		adicionarInstrucao(Instrucao.PUSH_CX);
		adicionarInstrucao(Instrucao.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x0009, processador.getAx());
		Assert.assertEquals((short) 0, processador.getBx());
		Assert.assertEquals((short) 0x0009, processador.getCx());
		Assert.assertEquals((short) 0, processador.getBp());
		Assert.assertEquals((short) 0x00FD, processador.getSp());
		Assert.assertEquals((short) 0x000A, processador.getIp());

		Assert.assertEquals((byte) 0x09, memoria.getData((short) 0x00FE));
		Assert.assertEquals((byte) 0x00, memoria.getData((short) 0x00FF));
	}

	@Test
	public void testPushBp() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0xFF);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.MOV_SP_AX);
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0x09);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.MOV_BP_AX);
		adicionarInstrucao(Instrucao.PUSH_BP);
		adicionarInstrucao(Instrucao.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x0009, processador.getAx());
		Assert.assertEquals((short) 0, processador.getBx());
		Assert.assertEquals((short) 0, processador.getCx());
		Assert.assertEquals((short) 0x0009, processador.getBp());
		Assert.assertEquals((short) 0x00FD, processador.getSp());
		Assert.assertEquals((short) 0x000A, processador.getIp());

		Assert.assertEquals((byte) 0x09, memoria.getData((short) 0x00FE));
		Assert.assertEquals((byte) 0x00, memoria.getData((short) 0x00FF));
	}

	@Test
	public void testPopBp() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0xFF);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.MOV_SP_AX);
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0x09);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.MOV_BP_AX);
		adicionarInstrucao(Instrucao.PUSH_BP);
		adicionarInstrucao(Instrucao.POP_BP);
		adicionarInstrucao(Instrucao.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x0009, processador.getAx());
		Assert.assertEquals((short) 0, processador.getBx());
		Assert.assertEquals((short) 0, processador.getCx());
		Assert.assertEquals((short) 0x0009, processador.getBp());
		Assert.assertEquals((short) 0x00FF, processador.getSp());
		Assert.assertEquals((short) 0x000B, processador.getIp());

		Assert.assertEquals((byte) 0x09, memoria.getData((short) 0x00FE));
		Assert.assertEquals((byte) 0x00, memoria.getData((short) 0x00FF));
	}

	@Test
	public void testPopBx() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0xFF);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.MOV_SP_AX);
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0x09);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.MOV_BX_AX);
		adicionarInstrucao(Instrucao.PUSH_BX);
		adicionarInstrucao(Instrucao.POP_BX);
		adicionarInstrucao(Instrucao.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x0009, processador.getAx());
		Assert.assertEquals((short) 0x0009, processador.getBx());
		Assert.assertEquals((short) 0, processador.getCx());
		Assert.assertEquals((short) 0, processador.getBp());
		Assert.assertEquals((short) 0x00FF, processador.getSp());
		Assert.assertEquals((short) 0x000B, processador.getIp());

		Assert.assertEquals((byte) 0x09, memoria.getData((short) 0x00FE));
		Assert.assertEquals((byte) 0x00, memoria.getData((short) 0x00FF));
	}

	@Test
	public void testPopCx() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0xFF);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.MOV_SP_AX);
		adicionarInstrucao(Instrucao.MOV_AX_LITERAL);
		adicionarInstrucao(0x09);
		adicionarInstrucao(0x00);
		adicionarInstrucao(Instrucao.MOV_CX_AX);
		adicionarInstrucao(Instrucao.PUSH_CX);
		adicionarInstrucao(Instrucao.POP_CX);
		adicionarInstrucao(Instrucao.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x0009, processador.getAx());
		Assert.assertEquals((short) 0, processador.getBx());
		Assert.assertEquals((short) 0x0009, processador.getCx());
		Assert.assertEquals((short) 0, processador.getBp());
		Assert.assertEquals((short) 0x00FF, processador.getSp());
		Assert.assertEquals((short) 0x000B, processador.getIp());

		Assert.assertEquals((byte) 0x09, memoria.getData((short) 0x00FE));
		Assert.assertEquals((byte) 0x00, memoria.getData((short) 0x00FF));
	}

	@Test
	public void testNop() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.NOP);
		adicionarInstrucao(Instrucao.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0, processador.getAx());
		Assert.assertEquals((short) 0, processador.getBx());
		Assert.assertEquals((short) 0, processador.getCx());
		Assert.assertEquals((short) 0, processador.getBp());
		Assert.assertEquals((short) 0, processador.getSp());
		Assert.assertEquals((short) 0x0002, processador.getIp());
	}

	@Test
	public void testHalt() throws MVMException, InterruptedException {
		adicionarInstrucao(Instrucao.HALT);
		adicionarInstrucao(Instrucao.INC_AX);
		adicionarInstrucao(Instrucao.INC_BX);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0, processador.getAx());
		Assert.assertEquals((short) 0, processador.getBx());
		Assert.assertEquals((short) 0, processador.getCx());
		Assert.assertEquals((short) 0, processador.getBp());
		Assert.assertEquals((short) 0, processador.getSp());
		Assert.assertEquals((short) 0x0001, processador.getIp());
	}

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
	// IN_AX((byte) 0b00011101), //
	// OUT_AX((byte) 0b00011110), //
}
