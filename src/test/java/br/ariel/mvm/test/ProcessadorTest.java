package br.ariel.mvm.test;

import org.junit.Assert;
import org.junit.Test;

import br.ariel.mvm.exception.MVMException;
import br.ariel.mvm.model.InstrucaoProcessador;

public class ProcessadorTest extends MVMBaseTest {

	@Test
	public void testeMovAxBx() throws MVMException, InterruptedException {
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0x14);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.MOV_BX_AX);
		adicionarInstrucao(InstrucaoProcessador.INIT_AX);
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_BX);
		adicionarInstrucao(InstrucaoProcessador.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals(20, processador.getAx());
		Assert.assertEquals(20, processador.getBx());
		Assert.assertEquals(0, processador.getCx());
		Assert.assertEquals(0, processador.getBp());
		Assert.assertEquals(0, processador.getSp());
	}

	@Test
	public void testeMovAxCx() throws MVMException, InterruptedException {
		adicionarInstrucao(InstrucaoProcessador.INIT_AX);
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0x14);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.MOV_CX_AX);
		adicionarInstrucao(InstrucaoProcessador.INIT_AX);
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_CX);
		adicionarInstrucao(InstrucaoProcessador.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals(20, processador.getAx());
		Assert.assertEquals(0, processador.getBx());
		Assert.assertEquals(20, processador.getCx());
		Assert.assertEquals(0, processador.getBp());
		Assert.assertEquals(0, processador.getSp());
	}

	@Test
	public void testePopAx() throws MVMException, InterruptedException {
		adicionarInstrucao(InstrucaoProcessador.INIT_AX);
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0x15);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.MOV_SP_AX);
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0x34);
		adicionarInstrucao(0x12);
		adicionarInstrucao(InstrucaoProcessador.PUSH_AX);
		adicionarInstrucao(InstrucaoProcessador.INIT_AX);
		adicionarInstrucao(InstrucaoProcessador.POP_AX);
		adicionarInstrucao(InstrucaoProcessador.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x1234, processador.getAx());
		Assert.assertEquals(0, processador.getBx());
		Assert.assertEquals(0, processador.getCx());
		Assert.assertEquals(0, processador.getBp());
		Assert.assertEquals(21, processador.getSp());
	}

	@Test
	public void testPushAx() throws MVMException, InterruptedException {
		adicionarInstrucao(InstrucaoProcessador.INIT_AX);
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0x15);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.MOV_SP_AX);
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0x0A);
		adicionarInstrucao(0xF0);
		adicionarInstrucao(InstrucaoProcessador.PUSH_AX);
		adicionarInstrucao(InstrucaoProcessador.HALT);

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
		adicionarInstrucao(InstrucaoProcessador.INIT_AX);
		adicionarInstrucao(InstrucaoProcessador.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals(0, processador.getAx());
		Assert.assertEquals(0, processador.getBx());
		Assert.assertEquals(0, processador.getCx());
		Assert.assertEquals(0, processador.getBp());
		Assert.assertEquals(0, processador.getSp());
	}

	@Test
	public void testInitAx2() throws MVMException, InterruptedException {
		adicionarInstrucao(InstrucaoProcessador.INIT_AX);
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0x0A);
		adicionarInstrucao(0xF0);
		adicionarInstrucao(InstrucaoProcessador.INIT_AX);
		adicionarInstrucao(InstrucaoProcessador.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals(0, processador.getAx());
		Assert.assertEquals(0, processador.getBx());
		Assert.assertEquals(0, processador.getCx());
		Assert.assertEquals(0, processador.getBp());
		Assert.assertEquals(0, processador.getSp());
	}

	@Test
	public void testMovBxAx() throws MVMException, InterruptedException {
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0x14);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.MOV_BX_AX);
		adicionarInstrucao(InstrucaoProcessador.INIT_AX);
		adicionarInstrucao(InstrucaoProcessador.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals(0, processador.getAx());
		Assert.assertEquals(20, processador.getBx());
		Assert.assertEquals(0, processador.getCx());
		Assert.assertEquals(0, processador.getBp());
		Assert.assertEquals(0, processador.getSp());
	}

	@Test
	public void testMovCxAx() throws MVMException, InterruptedException {
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0x14);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.MOV_CX_AX);
		adicionarInstrucao(InstrucaoProcessador.INIT_AX);
		adicionarInstrucao(InstrucaoProcessador.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals(0, processador.getAx());
		Assert.assertEquals(0, processador.getBx());
		Assert.assertEquals(20, processador.getCx());
		Assert.assertEquals(0, processador.getBp());
		Assert.assertEquals(0, processador.getSp());
	}

	@Test
	public void testMovAxMem() throws MVMException, InterruptedException {
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_MEM);
		adicionarInstrucao(0x18);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.HALT);

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
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0x01);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.MOV_BX_AX);
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_MEM_BX_P);
		adicionarInstrucao(0x18);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.HALT);

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
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0x01);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.MOV_BP_AX);
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_MEM_BP_S);
		adicionarInstrucao(0x18);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.HALT);

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
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0x01);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.MOV_BP_AX);
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_MEM_BP_P);
		adicionarInstrucao(0x18);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.HALT);

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
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0x52);
		adicionarInstrucao(0xAE);
		adicionarInstrucao(InstrucaoProcessador.MOV_MEM_AX);
		adicionarInstrucao(0x18);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.HALT);

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
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0xE5);
		adicionarInstrucao(0x01);
		adicionarInstrucao(InstrucaoProcessador.MOV_BX_AX);
		adicionarInstrucao(InstrucaoProcessador.MOV_MEM_BX_P_AX);
		adicionarInstrucao(0x18);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.HALT);

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
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0xE5);
		adicionarInstrucao(0x12);
		adicionarInstrucao(InstrucaoProcessador.MOV_SP_AX);
		adicionarInstrucao(InstrucaoProcessador.MOV_BP_SP);
		adicionarInstrucao(InstrucaoProcessador.INIT_AX);
		adicionarInstrucao(InstrucaoProcessador.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0, processador.getAx());
		Assert.assertEquals((short) 0, processador.getBx());
		Assert.assertEquals((short) 0, processador.getCx());
		Assert.assertEquals((short) 0x12E5, processador.getBp());
		Assert.assertEquals((short) 0x12E5, processador.getSp());
	}

	@Test
	public void testMovSpBp() throws MVMException, InterruptedException {
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0xE5);
		adicionarInstrucao(0x12);
		adicionarInstrucao(InstrucaoProcessador.MOV_BP_AX);
		adicionarInstrucao(InstrucaoProcessador.MOV_SP_BP);
		adicionarInstrucao(InstrucaoProcessador.INIT_AX);
		adicionarInstrucao(InstrucaoProcessador.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0, processador.getAx());
		Assert.assertEquals((short) 0, processador.getBx());
		Assert.assertEquals((short) 0, processador.getCx());
		Assert.assertEquals((short) 0x12E5, processador.getBp());
		Assert.assertEquals((short) 0x12E5, processador.getSp());
	}

	@Test
	public void testAddAxBx() throws MVMException, InterruptedException {
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0xE5);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.MOV_BX_AX);
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0x00);
		adicionarInstrucao(0x12);
		adicionarInstrucao(InstrucaoProcessador.ADD_AX_BX);
		adicionarInstrucao(InstrucaoProcessador.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x12E5, processador.getAx());
		Assert.assertEquals((short) 0x00E5, processador.getBx());
		Assert.assertEquals((short) 0, processador.getCx());
		Assert.assertEquals((short) 0, processador.getBp());
		Assert.assertEquals((short) 0, processador.getSp());
	}

	@Test
	public void testAddAxCx() throws MVMException, InterruptedException {
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0xE5);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.MOV_CX_AX);
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0x00);
		adicionarInstrucao(0x12);
		adicionarInstrucao(InstrucaoProcessador.ADD_AX_CX);
		adicionarInstrucao(InstrucaoProcessador.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x12E5, processador.getAx());
		Assert.assertEquals((short) 0, processador.getBx());
		Assert.assertEquals((short) 0x00E5, processador.getCx());
		Assert.assertEquals((short) 0, processador.getBp());
		Assert.assertEquals((short) 0, processador.getSp());
	}

	@Test
	public void testAddBxCx() throws MVMException, InterruptedException {
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0xE5);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.MOV_CX_AX);
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0x00);
		adicionarInstrucao(0x12);
		adicionarInstrucao(InstrucaoProcessador.MOV_BX_AX);
		adicionarInstrucao(InstrucaoProcessador.ADD_BX_CX);
		adicionarInstrucao(InstrucaoProcessador.INIT_AX);
		adicionarInstrucao(InstrucaoProcessador.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0, processador.getAx());
		Assert.assertEquals((short) 0x12E5, processador.getBx());
		Assert.assertEquals((short) 0x00E5, processador.getCx());
		Assert.assertEquals((short) 0, processador.getBp());
		Assert.assertEquals((short) 0, processador.getSp());
	}

	@Test
	public void testSubAxBx() throws MVMException, InterruptedException {
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0xE5);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.MOV_BX_AX);
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0x00);
		adicionarInstrucao(0x12);
		adicionarInstrucao(InstrucaoProcessador.SUB_AX_BX);
		adicionarInstrucao(InstrucaoProcessador.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x111B, processador.getAx());
		Assert.assertEquals((short) 0x00E5, processador.getBx());
		Assert.assertEquals((short) 0, processador.getCx());
		Assert.assertEquals((short) 0, processador.getBp());
		Assert.assertEquals((short) 0, processador.getSp());
	}

	@Test
	public void testSubAxCx() throws MVMException, InterruptedException {
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0xE5);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.MOV_CX_AX);
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0x00);
		adicionarInstrucao(0x12);
		adicionarInstrucao(InstrucaoProcessador.SUB_AX_CX);
		adicionarInstrucao(InstrucaoProcessador.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x111B, processador.getAx());
		Assert.assertEquals((short) 0, processador.getBx());
		Assert.assertEquals((short) 0x00E5, processador.getCx());
		Assert.assertEquals((short) 0, processador.getBp());
		Assert.assertEquals((short) 0, processador.getSp());
	}

	@Test
	public void testSubBxCx() throws MVMException, InterruptedException {
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0xE5);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.MOV_CX_AX);
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0x00);
		adicionarInstrucao(0x12);
		adicionarInstrucao(InstrucaoProcessador.MOV_BX_AX);
		adicionarInstrucao(InstrucaoProcessador.SUB_BX_CX);
		adicionarInstrucao(InstrucaoProcessador.INIT_AX);
		adicionarInstrucao(InstrucaoProcessador.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0, processador.getAx());
		Assert.assertEquals((short) 0x111B, processador.getBx());
		Assert.assertEquals((short) 0x00E5, processador.getCx());
		Assert.assertEquals((short) 0, processador.getBp());
		Assert.assertEquals((short) 0, processador.getSp());
	}

	@Test
	public void testIncAx() throws MVMException, InterruptedException {
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0xE5);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.INC_AX);
		adicionarInstrucao(InstrucaoProcessador.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x00E6, processador.getAx());
		Assert.assertEquals((short) 0, processador.getBx());
		Assert.assertEquals((short) 0, processador.getCx());
		Assert.assertEquals((short) 0, processador.getBp());
		Assert.assertEquals((short) 0, processador.getSp());
	}

	@Test
	public void testIncBx() throws MVMException, InterruptedException {
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0xE5);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.MOV_BX_AX);
		adicionarInstrucao(InstrucaoProcessador.INC_BX);
		adicionarInstrucao(InstrucaoProcessador.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x00E5, processador.getAx());
		Assert.assertEquals((short) 0x00E6, processador.getBx());
		Assert.assertEquals((short) 0, processador.getCx());
		Assert.assertEquals((short) 0, processador.getBp());
		Assert.assertEquals((short) 0, processador.getSp());
	}

	@Test
	public void testIncCx() throws MVMException, InterruptedException {
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0xE5);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.MOV_CX_AX);
		adicionarInstrucao(InstrucaoProcessador.INC_CX);
		adicionarInstrucao(InstrucaoProcessador.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x00E5, processador.getAx());
		Assert.assertEquals((short) 0, processador.getBx());
		Assert.assertEquals((short) 0x00E6, processador.getCx());
		Assert.assertEquals((short) 0, processador.getBp());
		Assert.assertEquals((short) 0, processador.getSp());
	}

	@Test
	public void testDecAx() throws MVMException, InterruptedException {
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0xE5);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.DEC_AX);
		adicionarInstrucao(InstrucaoProcessador.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x00E4, processador.getAx());
		Assert.assertEquals((short) 0, processador.getBx());
		Assert.assertEquals((short) 0, processador.getCx());
		Assert.assertEquals((short) 0, processador.getBp());
		Assert.assertEquals((short) 0, processador.getSp());
	}

	@Test
	public void testDecBx() throws MVMException, InterruptedException {
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0xE5);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.MOV_BX_AX);
		adicionarInstrucao(InstrucaoProcessador.DEC_BX);
		adicionarInstrucao(InstrucaoProcessador.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x00E5, processador.getAx());
		Assert.assertEquals((short) 0x00E4, processador.getBx());
		Assert.assertEquals((short) 0, processador.getCx());
		Assert.assertEquals((short) 0, processador.getBp());
		Assert.assertEquals((short) 0, processador.getSp());
	}

	@Test
	public void testDecCx() throws MVMException, InterruptedException {
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0xE5);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.MOV_CX_AX);
		adicionarInstrucao(InstrucaoProcessador.DEC_CX);
		adicionarInstrucao(InstrucaoProcessador.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x00E5, processador.getAx());
		Assert.assertEquals((short) 0, processador.getBx());
		Assert.assertEquals((short) 0x00E4, processador.getCx());
		Assert.assertEquals((short) 0, processador.getBp());
		Assert.assertEquals((short) 0, processador.getSp());
	}

	@Test
	public void testIncBp() throws MVMException, InterruptedException {
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0xE5);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.MOV_BP_AX);
		adicionarInstrucao(InstrucaoProcessador.INC_BP);
		adicionarInstrucao(InstrucaoProcessador.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x00E5, processador.getAx());
		Assert.assertEquals((short) 0, processador.getBx());
		Assert.assertEquals((short) 0, processador.getCx());
		Assert.assertEquals((short) 0x00E6, processador.getBp());
		Assert.assertEquals((short) 0, processador.getSp());
	}

	@Test
	public void testDecBp() throws MVMException, InterruptedException {
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0xE5);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.MOV_BP_AX);
		adicionarInstrucao(InstrucaoProcessador.DEC_BP);
		adicionarInstrucao(InstrucaoProcessador.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x00E5, processador.getAx());
		Assert.assertEquals((short) 0, processador.getBx());
		Assert.assertEquals((short) 0, processador.getCx());
		Assert.assertEquals((short) 0x00E4, processador.getBp());
		Assert.assertEquals((short) 0, processador.getSp());
	}

	@Test
	public void testTestAx0() throws MVMException, InterruptedException {
		adicionarInstrucao(InstrucaoProcessador.INIT_AX);
		adicionarInstrucao(InstrucaoProcessador.TEST_AX_0);
		adicionarInstrucao(0x0A);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.INC_AX);
		adicionarInstrucao(InstrucaoProcessador.INC_AX);
		adicionarInstrucao(InstrucaoProcessador.INC_AX);
		adicionarInstrucao(InstrucaoProcessador.INC_AX);
		adicionarInstrucao(InstrucaoProcessador.INC_AX);
		adicionarInstrucao(InstrucaoProcessador.INC_AX);
		adicionarInstrucao(InstrucaoProcessador.HALT);

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
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0x01);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.TEST_AX_0);
		adicionarInstrucao(0x0A);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.INC_AX);
		adicionarInstrucao(InstrucaoProcessador.INC_AX);
		adicionarInstrucao(InstrucaoProcessador.INC_AX);
		adicionarInstrucao(InstrucaoProcessador.INC_AX);
		adicionarInstrucao(InstrucaoProcessador.INC_AX);
		adicionarInstrucao(InstrucaoProcessador.INC_AX);
		adicionarInstrucao(InstrucaoProcessador.HALT);

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
		adicionarInstrucao(InstrucaoProcessador.INIT_AX);
		adicionarInstrucao(InstrucaoProcessador.JMP);
		adicionarInstrucao(0x0A);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.INC_AX);
		adicionarInstrucao(InstrucaoProcessador.INC_AX);
		adicionarInstrucao(InstrucaoProcessador.INC_AX);
		adicionarInstrucao(InstrucaoProcessador.INC_AX);
		adicionarInstrucao(InstrucaoProcessador.INC_AX);
		adicionarInstrucao(InstrucaoProcessador.INC_AX);
		adicionarInstrucao(InstrucaoProcessador.HALT);

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
		adicionarInstrucao(InstrucaoProcessador.INIT_AX);
		adicionarInstrucao(InstrucaoProcessador.JMP);
		adicionarInstrucao(0x08);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.INC_AX);
		adicionarInstrucao(InstrucaoProcessador.INC_AX);
		adicionarInstrucao(InstrucaoProcessador.INC_AX);
		adicionarInstrucao(InstrucaoProcessador.INC_AX);
		adicionarInstrucao(InstrucaoProcessador.INC_AX);
		adicionarInstrucao(InstrucaoProcessador.INC_AX);
		adicionarInstrucao(InstrucaoProcessador.HALT);

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
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0xFF);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.MOV_SP_AX);
		adicionarInstrucao(InstrucaoProcessador.CALL);
		adicionarInstrucao(0x08);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.INC_AX);
		adicionarInstrucao(InstrucaoProcessador.HALT);

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
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0xFF);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.MOV_SP_AX);
		adicionarInstrucao(InstrucaoProcessador.CALL);
		adicionarInstrucao(0x09);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.INC_BX);
		adicionarInstrucao(InstrucaoProcessador.HALT);
		adicionarInstrucao(InstrucaoProcessador.INC_AX);
		adicionarInstrucao(InstrucaoProcessador.RET);

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
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0xFF);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.MOV_SP_AX);
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0x09);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.MOV_BX_AX);
		adicionarInstrucao(InstrucaoProcessador.PUSH_BX);
		adicionarInstrucao(InstrucaoProcessador.HALT);

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
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0xFF);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.MOV_SP_AX);
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0x09);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.MOV_CX_AX);
		adicionarInstrucao(InstrucaoProcessador.PUSH_CX);
		adicionarInstrucao(InstrucaoProcessador.HALT);

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
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0xFF);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.MOV_SP_AX);
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0x09);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.MOV_BP_AX);
		adicionarInstrucao(InstrucaoProcessador.PUSH_BP);
		adicionarInstrucao(InstrucaoProcessador.HALT);

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
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0xFF);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.MOV_SP_AX);
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0x09);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.MOV_BP_AX);
		adicionarInstrucao(InstrucaoProcessador.PUSH_BP);
		adicionarInstrucao(InstrucaoProcessador.POP_BP);
		adicionarInstrucao(InstrucaoProcessador.HALT);

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
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0xFF);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.MOV_SP_AX);
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0x09);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.MOV_BX_AX);
		adicionarInstrucao(InstrucaoProcessador.PUSH_BX);
		adicionarInstrucao(InstrucaoProcessador.POP_BX);
		adicionarInstrucao(InstrucaoProcessador.HALT);

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
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0xFF);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.MOV_SP_AX);
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0x09);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.MOV_CX_AX);
		adicionarInstrucao(InstrucaoProcessador.PUSH_CX);
		adicionarInstrucao(InstrucaoProcessador.POP_CX);
		adicionarInstrucao(InstrucaoProcessador.HALT);

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
		adicionarInstrucao(InstrucaoProcessador.NOP);
		adicionarInstrucao(InstrucaoProcessador.HALT);

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
		adicionarInstrucao(InstrucaoProcessador.HALT);
		adicionarInstrucao(InstrucaoProcessador.INC_AX);
		adicionarInstrucao(InstrucaoProcessador.INC_BX);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0, processador.getAx());
		Assert.assertEquals((short) 0, processador.getBx());
		Assert.assertEquals((short) 0, processador.getCx());
		Assert.assertEquals((short) 0, processador.getBp());
		Assert.assertEquals((short) 0, processador.getSp());
		Assert.assertEquals((short) 0x0001, processador.getIp());
	}

	@Test
	public void testDecSp() throws MVMException, InterruptedException {
		adicionarInstrucao(InstrucaoProcessador.INIT_AX);
		adicionarInstrucao(InstrucaoProcessador.INC_AX);
		adicionarInstrucao(InstrucaoProcessador.INC_AX);
		adicionarInstrucao(InstrucaoProcessador.INC_AX);
		adicionarInstrucao(InstrucaoProcessador.MOV_SP_AX);
		adicionarInstrucao(InstrucaoProcessador.DEC_SP);
		adicionarInstrucao(InstrucaoProcessador.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x0003, processador.getAx());
		Assert.assertEquals((short) 0, processador.getBx());
		Assert.assertEquals((short) 0, processador.getCx());
		Assert.assertEquals((short) 0, processador.getBp());
		Assert.assertEquals((short) 0x0002, processador.getSp());
		Assert.assertEquals((short) 0x0007, processador.getIp());
	}

	@Test
	public void testIncSp() throws MVMException, InterruptedException {
		adicionarInstrucao(InstrucaoProcessador.INIT_AX);
		adicionarInstrucao(InstrucaoProcessador.INC_AX);
		adicionarInstrucao(InstrucaoProcessador.INC_AX);
		adicionarInstrucao(InstrucaoProcessador.INC_AX);
		adicionarInstrucao(InstrucaoProcessador.MOV_SP_AX);
		adicionarInstrucao(InstrucaoProcessador.INC_SP);
		adicionarInstrucao(InstrucaoProcessador.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x0003, processador.getAx());
		Assert.assertEquals((short) 0, processador.getBx());
		Assert.assertEquals((short) 0, processador.getCx());
		Assert.assertEquals((short) 0, processador.getBp());
		Assert.assertEquals((short) 0x0004, processador.getSp());
		Assert.assertEquals((short) 0x0007, processador.getIp());
	}

	@Test
	public void testAxLiteral() throws MVMException, InterruptedException {
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0xFF);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x00FF, processador.getAx());
		Assert.assertEquals((short) 0, processador.getBx());
		Assert.assertEquals((short) 0, processador.getCx());
		Assert.assertEquals((short) 0, processador.getBp());
		Assert.assertEquals((short) 0x0000, processador.getSp());
		Assert.assertEquals((short) 0x0004, processador.getIp());
	}

	@Test
	public void testMemBpSAx() throws MVMException, InterruptedException {
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0xFF);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.MOV_BP_AX);
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0xFF);
		adicionarInstrucao(0x02);
		adicionarInstrucao(InstrucaoProcessador.MOV_MEM_BP_S_AX);
		adicionarInstrucao(0x01);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x02FF, processador.getAx());
		Assert.assertEquals((short) 0, processador.getBx());
		Assert.assertEquals((short) 0, processador.getCx());
		Assert.assertEquals((short) 0x00FF, processador.getBp());
		Assert.assertEquals((short) 0x0000, processador.getSp());
		Assert.assertEquals((short) 0x000B, processador.getIp());

		Assert.assertEquals((byte) 0xFF, memoria.getData((short) 0x00FE));
		Assert.assertEquals((byte) 0x02, memoria.getData((short) 0x00FF));
	}

	@Test
	public void testMemBpPAx() throws MVMException, InterruptedException {
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0xFF);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.MOV_BP_AX);
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0xFF);
		adicionarInstrucao(0x02);
		adicionarInstrucao(InstrucaoProcessador.MOV_MEM_BP_P_AX);
		adicionarInstrucao(0x01);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x02FF, processador.getAx());
		Assert.assertEquals((short) 0, processador.getBx());
		Assert.assertEquals((short) 0, processador.getCx());
		Assert.assertEquals((short) 0x00FF, processador.getBp());
		Assert.assertEquals((short) 0x0000, processador.getSp());
		Assert.assertEquals((short) 0x000B, processador.getIp());

		Assert.assertEquals((byte) 0xFF, memoria.getData((short) 0x0100));
		Assert.assertEquals((byte) 0x02, memoria.getData((short) 0x0101));
	}

	@Test
	public void testTestAxBx1() throws MVMException, InterruptedException {
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0xFF);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.MOV_BX_AX);
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0xFF);
		adicionarInstrucao(0x02);
		adicionarInstrucao(InstrucaoProcessador.TEST_AX_BX);
		adicionarInstrucao(0x0D);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.INC_CX);
		adicionarInstrucao(InstrucaoProcessador.INC_CX);
		adicionarInstrucao(InstrucaoProcessador.INC_CX);
		adicionarInstrucao(InstrucaoProcessador.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x02FF, processador.getAx());
		Assert.assertEquals((short) 0x00FF, processador.getBx());
		Assert.assertEquals((short) 0x0003, processador.getCx());
		Assert.assertEquals((short) 0x0000, processador.getBp());
		Assert.assertEquals((short) 0x0000, processador.getSp());
		Assert.assertEquals((short) 0x000E, processador.getIp());
	}

	@Test
	public void testTestAxBx2() throws MVMException, InterruptedException {
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(0xFF);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.MOV_BX_AX);
		adicionarInstrucao(InstrucaoProcessador.TEST_AX_BX);
		adicionarInstrucao(0x0A);
		adicionarInstrucao(0x00);
		adicionarInstrucao(InstrucaoProcessador.INC_CX);
		adicionarInstrucao(InstrucaoProcessador.INC_CX);
		adicionarInstrucao(InstrucaoProcessador.INC_CX);
		adicionarInstrucao(InstrucaoProcessador.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x00FF, processador.getAx());
		Assert.assertEquals((short) 0x00FF, processador.getBx());
		Assert.assertEquals((short) 0x0000, processador.getCx());
		Assert.assertEquals((short) 0x0000, processador.getBp());
		Assert.assertEquals((short) 0x0000, processador.getSp());
		Assert.assertEquals((short) 0x000B, processador.getIp());
	}

	@Test
	public void testMovAxSp() throws MVMException, InterruptedException {
		adicionarInstrucao(InstrucaoProcessador.INC_SP);
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_SP);
		adicionarInstrucao(InstrucaoProcessador.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x0001, processador.getAx());
		Assert.assertEquals((short) 0x0000, processador.getBx());
		Assert.assertEquals((short) 0x0000, processador.getCx());
		Assert.assertEquals((short) 0x0000, processador.getBp());
		Assert.assertEquals((short) 0x0001, processador.getSp());
		Assert.assertEquals((short) 0x0003, processador.getIp());
	}

	@Test
	public void testMovSpAx() throws MVMException, InterruptedException {
		adicionarInstrucao(InstrucaoProcessador.INC_AX);
		adicionarInstrucao(InstrucaoProcessador.MOV_SP_AX);
		adicionarInstrucao(InstrucaoProcessador.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x0001, processador.getAx());
		Assert.assertEquals((short) 0x0000, processador.getBx());
		Assert.assertEquals((short) 0x0000, processador.getCx());
		Assert.assertEquals((short) 0x0000, processador.getBp());
		Assert.assertEquals((short) 0x0001, processador.getSp());
		Assert.assertEquals((short) 0x0003, processador.getIp());
	}

	@Test
	public void testMovAxBp() throws MVMException, InterruptedException {
		adicionarInstrucao(InstrucaoProcessador.INC_BP);
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_BP);
		adicionarInstrucao(InstrucaoProcessador.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x0001, processador.getAx());
		Assert.assertEquals((short) 0x0000, processador.getBx());
		Assert.assertEquals((short) 0x0000, processador.getCx());
		Assert.assertEquals((short) 0x0001, processador.getBp());
		Assert.assertEquals((short) 0x0000, processador.getSp());
		Assert.assertEquals((short) 0x0003, processador.getIp());
	}

	@Test
	public void testMovBpAx() throws MVMException, InterruptedException {
		adicionarInstrucao(InstrucaoProcessador.INC_AX);
		adicionarInstrucao(InstrucaoProcessador.MOV_BP_AX);
		adicionarInstrucao(InstrucaoProcessador.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x0001, processador.getAx());
		Assert.assertEquals((short) 0x0000, processador.getBx());
		Assert.assertEquals((short) 0x0000, processador.getCx());
		Assert.assertEquals((short) 0x0001, processador.getBp());
		Assert.assertEquals((short) 0x0000, processador.getSp());
		Assert.assertEquals((short) 0x0003, processador.getIp());
	}

	@Test
	public void testInt() throws MVMException, InterruptedException {
		adicionarInstrucao(InstrucaoProcessador.JMP);
		adicionarInstrucao(7);
		adicionarInstrucao(0);
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(20);
		adicionarInstrucao(0);
		adicionarInstrucao(InstrucaoProcessador.HALT);
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(50);
		adicionarInstrucao(0);
		adicionarInstrucao(InstrucaoProcessador.MOV_BP_AX);
		adicionarInstrucao(InstrucaoProcessador.MOV_SP_AX);
		adicionarInstrucao(InstrucaoProcessador.INT);
		adicionarInstrucao(3);
		adicionarInstrucao(0);
		adicionarInstrucao(InstrucaoProcessador.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x0014, processador.getAx());
		Assert.assertEquals((short) 0x0000, processador.getBx());
		Assert.assertEquals((short) 0x0000, processador.getCx());
		Assert.assertEquals((short) 0x0032, processador.getBp());
		Assert.assertEquals((short) 0x0028, processador.getSp());
		Assert.assertEquals((short) 0x0007, processador.getIp());
	}

	@Test
	public void testIret() throws MVMException, InterruptedException {
		adicionarInstrucao(InstrucaoProcessador.JMP);
		adicionarInstrucao(7);
		adicionarInstrucao(0);
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(20);
		adicionarInstrucao(0);
		adicionarInstrucao(InstrucaoProcessador.IRET);
		adicionarInstrucao(InstrucaoProcessador.MOV_AX_LITERAL);
		adicionarInstrucao(50);
		adicionarInstrucao(0);
		adicionarInstrucao(InstrucaoProcessador.MOV_BP_AX);
		adicionarInstrucao(InstrucaoProcessador.MOV_SP_AX);
		adicionarInstrucao(InstrucaoProcessador.INT);
		adicionarInstrucao(3);
		adicionarInstrucao(0);
		adicionarInstrucao(InstrucaoProcessador.INC_AX);
		adicionarInstrucao(InstrucaoProcessador.HALT);

		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x0033, processador.getAx());
		Assert.assertEquals((short) 0x0000, processador.getBx());
		Assert.assertEquals((short) 0x0000, processador.getCx());
		Assert.assertEquals((short) 0x0032, processador.getBp());
		Assert.assertEquals((short) 0x0032, processador.getSp());
		Assert.assertEquals((short) 0x0011, processador.getIp());
	}

	// IN_AX((byte) 0b00011101), //
	// OUT_AX((byte) 0b00011110), //
}
