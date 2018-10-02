package br.ariel.mvm.main;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import br.ariel.mvm.model.InstrucaoProcessador;

public class Ajuda {

	public static void main(String[] args) {
		Map<Byte, InstrucaoProcessador> collect = Stream.of(InstrucaoProcessador.values()).collect(Collectors.toMap(InstrucaoProcessador::getCode, Function.identity()));

		System.out.println(collect.get((byte) 26)); // _mem.m[0 + enderecoDeCarga] = 26;
		System.out.println(collect.get((byte) 10)); // _mem.m[1 + enderecoDeCarga] = 10;
		System.out.println(collect.get((byte) 204)); // _mem.m[2 + enderecoDeCarga] = 204;
		System.out.println(collect.get((byte) 219)); // _mem.m[3 + enderecoDeCarga] = 219;
		System.out.println(collect.get((byte) 234)); // _mem.m[4 + enderecoDeCarga] = 234;
		System.out.println(collect.get((byte) 249)); // _mem.m[5 + enderecoDeCarga] = 249;
		System.out.println(collect.get((byte) 44)); // _mem.m[10 + enderecoDeCarga] = 44;
		System.out.println(collect.get((byte) 19)); // _mem.m[11 + enderecoDeCarga] = 19;
		System.out.println(collect.get((byte) 9)); // _mem.m[12 + enderecoDeCarga] = 9;
		System.out.println(collect.get((byte) 0)); // _mem.m[13 + enderecoDeCarga] = 0;
		System.out.println(collect.get((byte) 5)); // _mem.m[14 + enderecoDeCarga] = 5;
		System.out.println(collect.get((byte) 5)); // _mem.m[15 + enderecoDeCarga] = 5;
		System.out.println(collect.get((byte) 48)); // _mem.m[16 + enderecoDeCarga] = 48;
		System.out.println(collect.get((byte) 39)); // _mem.m[17 + enderecoDeCarga] = 39;
		System.out.println(collect.get((byte) 51)); // _mem.m[18 + enderecoDeCarga] = 51;
		System.out.println(collect.get((byte) 0)); // _mem.m[19 + enderecoDeCarga] = 0;
		System.out.println(collect.get((byte) 4)); // _mem.m[20 + enderecoDeCarga] = 4;
		System.out.println(collect.get((byte) 5)); // _mem.m[21 + enderecoDeCarga] = 5;
		System.out.println(collect.get((byte) 6)); // _mem.m[22 + enderecoDeCarga] = 6;
		System.out.println(collect.get((byte) 3)); // _mem.m[23 + enderecoDeCarga] = 3;
		System.out.println(collect.get((byte) 44)); // _mem.m[24 + enderecoDeCarga] = 44;
		System.out.println(collect.get((byte) 0)); // _mem.m[25 + enderecoDeCarga] = 0;
		System.out.println(collect.get((byte) 45)); // _mem.m[26 + enderecoDeCarga] = 45;
		System.out.println(collect.get((byte) 30)); // _mem.m[27 + enderecoDeCarga] = 30;
		System.out.println(collect.get((byte) 26)); // _mem.m[28 + enderecoDeCarga] = 26;
		System.out.println(collect.get((byte) 39)); // _mem.m[29 + enderecoDeCarga] = 39;
		System.out.println(collect.get((byte) 4)); // _mem.m[30 + enderecoDeCarga] = 4;
		System.out.println(collect.get((byte) 5)); // _mem.m[31 + enderecoDeCarga] = 5;
		System.out.println(collect.get((byte) 2)); // _mem.m[32 + enderecoDeCarga] = 2;
		System.out.println(collect.get((byte) 48)); // _mem.m[33 + enderecoDeCarga] = 48;
		System.out.println(collect.get((byte) 21)); // _mem.m[34 + enderecoDeCarga] = 21;
		System.out.println(collect.get((byte) 2)); // _mem.m[35 + enderecoDeCarga] = 2;
		System.out.println(collect.get((byte) 9)); // _mem.m[36 + enderecoDeCarga] = 9;
		System.out.println(collect.get((byte) 6)); // _mem.m[37 + enderecoDeCarga] = 6;
		System.out.println(collect.get((byte) 51)); // _mem.m[38 + enderecoDeCarga] = 51;
		System.out.println(collect.get((byte) 44)); // _mem.m[39 + enderecoDeCarga] = 44;
		System.out.println(collect.get((byte) 1)); // _mem.m[40 + enderecoDeCarga] = 1;
		System.out.println(collect.get((byte) 45)); // _mem.m[41 + enderecoDeCarga] = 45;
		System.out.println(collect.get((byte) 45)); // _mem.m[42 + enderecoDeCarga] = 45;
		System.out.println(collect.get((byte) 26)); // _mem.m[43 + enderecoDeCarga] = 26;
		System.out.println(collect.get((byte) 54)); // _mem.m[44 + enderecoDeCarga] = 54;
		System.out.println(collect.get((byte) 4)); // _mem.m[45 + enderecoDeCarga] = 4;
		System.out.println(collect.get((byte) 5)); // _mem.m[46 + enderecoDeCarga] = 5;
		System.out.println(collect.get((byte) 3)); // _mem.m[47 + enderecoDeCarga] = 3;
		System.out.println(collect.get((byte) 48)); // _mem.m[48 + enderecoDeCarga] = 48;
		System.out.println(collect.get((byte) 21)); // _mem.m[49 + enderecoDeCarga] = 21;
		System.out.println(collect.get((byte) 2)); // _mem.m[50 + enderecoDeCarga] = 2;
		System.out.println(collect.get((byte) 9)); // _mem.m[51 + enderecoDeCarga] = 9;
		System.out.println(collect.get((byte) 6)); // _mem.m[52 + enderecoDeCarga] = 6;
		System.out.println(collect.get((byte) 51)); // _mem.m[53 + enderecoDeCarga] = 51;
		System.out.println(collect.get((byte) 0)); // _mem.m[54 + enderecoDeCarga] = 0;
		System.out.println(collect.get((byte) 9)); // _mem.m[55 + enderecoDeCarga] = 9;
		System.out.println(collect.get((byte) 6)); // _mem.m[56 + enderecoDeCarga] = 6;
		System.out.println(collect.get((byte) 5)); // _mem.m[57 + enderecoDeCarga] = 5;
		System.out.println(collect.get((byte) 4)); // _mem.m[58 + enderecoDeCarga] = 4;
		System.out.println(collect.get((byte) 48)); // _mem.m[59 + enderecoDeCarga] = 48;
		System.out.println(collect.get((byte) 51)); // _mem.m[60 + enderecoDeCarga] = 51;
		System.out.println(collect.get((byte) 26)); // _mem.m[70 + enderecoDeCarga] = 26;
		System.out.println(collect.get((byte) 70)); // _mem.m[71 + enderecoDeCarga] = 70;
		System.out.println(collect.get((byte) 26)); // _mem.m[74 + enderecoDeCarga] = 26;
		System.out.println(collect.get((byte) 74)); // _mem.m[75 + enderecoDeCarga] = 74;
		System.out.println(collect.get((byte) 26)); // _mem.m[78 + enderecoDeCarga] = 26;
		System.out.println(collect.get((byte) 78)); // _mem.m[79 + enderecoDeCarga] = 78;
		System.out.println(collect.get((byte) 70)); // _mem.m[209 + enderecoDeCarga] = 70;
		System.out.println(collect.get((byte) 74)); // _mem.m[224 + enderecoDeCarga] = 74;
		System.out.println(collect.get((byte) 78)); // _mem.m[239 + enderecoDeCarga] = 78;
		System.out.println(collect.get((byte) 19)); // _mem.m[254 + enderecoDeCarga] = 19;
	}
}
