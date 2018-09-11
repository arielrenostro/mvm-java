package br.ariel.mvm.utils;

public class Utils {

	public static boolean isEmpty(String str) {
		return null == str || str.isEmpty();
	}

	public static boolean isNotNumber(String str) {
		if (isEmpty(str)) {
			return true;
		}
		return false; // TODO IMPLEMENTAR
	}
}
