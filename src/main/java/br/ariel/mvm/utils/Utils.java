package br.ariel.mvm.utils;

public class Utils {

	public static boolean isEmpty(String str) {
		return null == str || str.isEmpty();
	}

	public static boolean isNotNumber(String str) {
		return !isNumber(str);
	}

	public static boolean isNumber(String str) {
		if (isEmpty(str)) {
			return false;
		}
		return !str.matches("[^\\d+.?\\d*]") && str.matches("\\d+.?\\d*");
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 * Rotaciona os bits do parametro <code>high</code> 8 posicoes a esquerda e aplica a operacoes
	 * <code>or</code> encima do parametro <code>low</code>. O resultado disso e' uma concatenacao de bits.<br>
	 * Exemplo:<br>
	 * [0000 1111] [0000 0001] ||| Resultado: 0000 1111 0000 0001
	 *
	 * @param high
	 * @param low
	 * @return
	 */
	public static short concatenarBytes(byte high, byte low) {
		return (short) ((high << 8) | (0xFF & low));
	}

	public static byte[] quebrarBytes(short valor) {
		byte[] bytes = new byte[2];

		bytes[0] = (byte) (valor & 0x00FF);
		bytes[1] = (byte) (((short) 0xFF00 & valor) >> 8);

		return bytes;
	}
}
