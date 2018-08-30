package br.ariel.mvm.model;

/**
 * @author ariel
 */
public enum Instrucao {

	INIT_AX((byte) 0b0000000), //
	MOV_AX_BX((byte) 0b00000001), //
	MOV_AX_CX((byte) 0b0000010), //
	MOV_BX_AX((byte) 0b0000011), //
	MOV_CX_AX((byte) 0b0000100), //

	MOV_AX_MEM((byte) 0b0000101), //
	MOV_AX_MEM_BX_P((byte) 0b00000110), //
	MOV_AX_MEM_BP_S((byte) 0b00000111), //
	MOV_AX_MEM_BP_P((byte) 0b00001000), //

	MOV_MEM_AX((byte) 0b00001001), //
	MOV_MEM_BX_P_AX((byte) 0b00001010), //

	MOV_BP_SP((byte) 0b00001011), //
	MOV_SP_BP((byte) 0b00001100), //

	ADD_AX_BX((byte) 0b00001101), //
	ADD_AX_CX((byte) 0b00001110), //
	ADD_BX_CX((byte) 0b00001111), //

	SUB_AX_BX((byte) 0b00010000), //
	SUB_AX_CX((byte) 0b00010001), //
	SUB_BX_CX((byte) 0b00010010), //

	INC_AX((byte) 0b00010011), //
	INC_BX((byte) 0b00010100), //
	INC_CX((byte) 0b00010101), //

	DEC_AX((byte) 0b00010110), //
	DEC_BX((byte) 0b00010111), //
	DEC_CX((byte) 0b00011000), //

	TEST_AX_0((byte) 0b00011001), //
	JMP((byte) 0b00011010), //
	CALL((byte) 0b00011011), //
	RET((byte) 0b00011100), //

	IN_AX((byte) 0b00011101), //
	OUT_AX((byte) 0b00011110), //

	PUSH_AX((byte) 0b00011111), //
	PUSH_BX((byte) 0b00100000), //
	PUSH_CX((byte) 0b00100001), //
	PUSH_BP((byte) 0b00100010), //
	POP_BP((byte) 0b00100011), //
	POP_CX((byte) 0b00100100), //
	POP_BX((byte) 0b00100101), //
	POP_AX((byte) 0b00100110), //

	NOP((byte) 0b00100111), //
	HALT((byte) 0b00101000), //
	DEC_SP((byte) 0b00101001), //

	MOV_MEN_BP_S_AX((byte) 0b00101010), //
	MOV_MEN_BP_P_AX((byte) 0b00101011), //
	MOV_AX_LITERAL((byte) 0b00101100), //
	TEST_AX_BX((byte) 0b00101101), //
	INC_SP((byte) 0b00101110), //

	MOV_AX_SP((byte) 0b00101111), //
	MOV_SP_AX((byte) 0b00110000), //
	MOV_AX_BP((byte) 0b00110001), //
	MOV_BP_AX((byte) 0b00110010), //

	IRET((byte) 0b00110011), //
	INT((byte) 0b00110100), //

	BP_P((byte) 0b11111110), //
	BP_S((byte) 0b11111111);

	private byte code;

	private Instrucao(byte code) {
		this.code = code;
	}

	public byte getCode() {
		return code;
	}

}
