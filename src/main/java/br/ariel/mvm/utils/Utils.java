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

}
