package br.ariel.mvm.model.bios;

import br.ariel.mvm.model.Instrucao;

public class BIOSMattos1 implements IBIOSMVM {

	@Override
	public byte[] getBios() {
		byte[] bios = new byte[9];

		bios[0] = Instrucao.INIT_AX.getCode();
		bios[1] = Instrucao.MOV_AX_LITERAL.getCode();
		bios[2] = 0x03;
		bios[3] = 0x00;
		bios[4] = Instrucao.INC_AX.getCode();
		bios[5] = Instrucao.MOV_MEM_AX.getCode();
		bios[6] = 0x02;
		bios[7] = 0x00;
		bios[8] = Instrucao.HALT.getCode();

		return bios;
	}

}
