package br.ariel.mvm.controller;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import br.ariel.mvm.exception.InstrucaoInvalidaException;
import br.ariel.mvm.exception.MemoriaSemTamanhoException;
import br.ariel.mvm.exception.PosicaoMemoriaInvalida;
import br.ariel.mvm.exception.SemMemoriaException;
import br.ariel.mvm.exception.SemProcessadorException;
import br.ariel.mvm.model.Instrucao;
import br.ariel.mvm.model.Memoria;
import br.ariel.mvm.model.Processador;

/**
 * @author ariel
 */
public class ProcessadorController {

	public void processar(Processador processador, Memoria memoria) throws SemProcessadorException, SemMemoriaException, MemoriaSemTamanhoException, PosicaoMemoriaInvalida {
		validarProcessar(processador, memoria);

		Map<Byte, Instrucao> instrucoes = carregarInstrucoes();
		Instrucao instrucao = null;

		while (instrucao != Instrucao.HALT) {
			instrucao = proximaInstrucao(processador, memoria, instrucoes);
			executarInstrucao(processador, memoria, instrucao);
		}
	}

	private Map<Byte, Instrucao> carregarInstrucoes() {
		return Stream.of(Instrucao.values()).collect(Collectors.toMap(Instrucao::getCode, Function.identity()));
	}

	private void executarInstrucao(Processador processador, Memoria memoria, Instrucao instrucao) throws PosicaoMemoriaInvalida {
		if (Instrucao.INIT_AX.equals(instrucao)) {
			processador.setAx((short) 0);

		} else if (Instrucao.MOV_AX_BX.equals(instrucao)) {
			processador.setAx(processador.getBx());

		} else if (Instrucao.MOV_AX_CX.equals(instrucao)) {
			processador.setAx(processador.getCx());

		} else if (Instrucao.MOV_BX_AX.equals(instrucao)) {
			processador.setBx(processador.getAx());

		} else if (Instrucao.MOV_CX_AX.equals(instrucao)) {
			processador.setCx(processador.getAx());

		} else if (Instrucao.MOV_AX_MEM.equals(instrucao)) {
			short idx = memoria.getData(processador.incIp());
			processador.setAx(memoria.getData(idx));

		} else if (Instrucao.MOV_AX_MEM_BX_P.equals(instrucao)) {
			short idx = memoria.getData(processador.incIp());
			idx += processador.getBx();
			processador.setAx(memoria.getData(idx));

		} else if (Instrucao.MOV_AX_MEM_BP_S.equals(instrucao)) {
			short idx = memoria.getData(processador.incIp());
			idx -= processador.getBp();
			processador.setAx(memoria.getData(idx));

		} else if (Instrucao.MOV_AX_MEM_BP_P.equals(instrucao)) {
			short idx = memoria.getData(processador.incIp());
			idx += processador.getBp();
			processador.setAx(memoria.getData(idx));

		} else if (Instrucao.MOV_MEM_AX.equals(instrucao)) {
			short idx = memoria.getData(processador.incIp());
			memoria.setData(idx, processador.getAl());
			memoria.setData(++idx, processador.getAh());

		} else if (Instrucao.MOV_MEM_BX_P_AX.equals(instrucao)) {
			short idx = memoria.getData(processador.incIp());
			idx += processador.getBx();
			memoria.setData(idx, processador.getAl());
			memoria.setData(++idx, processador.getAh());

		} else if (Instrucao.MOV_BP_SP.equals(instrucao)) {
			processador.setBp(processador.getSp());

		} else if (Instrucao.MOV_SP_BP.equals(instrucao)) {
			processador.setSp(processador.getBp());

		} else if (Instrucao.ADD_AX_BX.equals(instrucao)) {
			short ax = (short) (processador.getAx() + processador.getBx());
			processador.setAx(ax);

		} else if (Instrucao.ADD_AX_CX.equals(instrucao)) {
			short ax = (short) (processador.getAx() + processador.getCx());
			processador.setAx(ax);

		} else if (Instrucao.ADD_BX_CX.equals(instrucao)) {
			short bx = (short) (processador.getBx() + processador.getCx());
			processador.setBx(bx);

		} else if (Instrucao.SUB_AX_BX.equals(instrucao)) {
			short ax = (short) (processador.getAx() - processador.getBx());
			processador.setAx(ax);

		} else if (Instrucao.SUB_AX_CX.equals(instrucao)) {
			short ax = (short) (processador.getAx() - processador.getCx());
			processador.setAx(ax);

		} else if (Instrucao.SUB_BX_CX.equals(instrucao)) {
			short bx = (short) (processador.getBx() - processador.getCx());
			processador.setBx(bx);

		} else if (Instrucao.INC_AX.equals(instrucao)) {
			short ax = (short) (processador.getAx() + 1);
			processador.setAx(ax);

		} else if (Instrucao.INC_BX.equals(instrucao)) {
			short bx = (short) (processador.getBx() + 1);
			processador.setBx(bx);

		} else if (Instrucao.INC_CX.equals(instrucao)) {
			short cx = (short) (processador.getCx() + 1);
			processador.setCx(cx);

		} else if (Instrucao.DEC_AX.equals(instrucao)) {
			short ax = (short) (processador.getAx() - 1);
			processador.setAx(ax);

		} else if (Instrucao.DEC_BX.equals(instrucao)) {
			short bx = (short) (processador.getBx() - 1);
			processador.setBx(bx);

		} else if (Instrucao.DEC_CX.equals(instrucao)) {
			short cx = (short) (processador.getCx() - 1);
			processador.setCx(cx);

		} else if (Instrucao.TEST_AX_0.equals(instrucao)) {
			if (processador.getAx() == 0) {
				executarInstrucao(processador, memoria, Instrucao.JMP);
			} else {
				short ip = (short) (processador.getIp() + 2);
				processador.setIp(ip);
			}

		} else if (Instrucao.JMP.equals(instrucao)) {
			byte jmpPosicao1 = memoria.getData(processador.incIp());
			byte jmpPosicao2 = memoria.getData(processador.incIp());
			// Rotaciona os bits 8 posições a esquerda e aplica um or encima da próxima posição.
			// [0000 1111] [0000 0001] ||| Resultado: 0000 1111 0000 0001
			short jmpIdx = concatenarBytes(jmpPosicao1, jmpPosicao2);

			processador.setIp(jmpIdx);
		} else if (Instrucao.CALL.equals(instrucao)) {
			short idxCall = processador.incIp();

			short idxProximoIp = processador.incIp();
			short idxSp = processador.getSp();
			memoria.setData(idxSp--, (byte) (idxProximoIp & 0x00FF));
			memoria.setData(idxSp--, (byte) (idxProximoIp & 0xFF00));

			processador.setSp(idxSp);
			processador.setIp(idxCall);

		} else if (Instrucao.RET.equals(instrucao)) {
			short idxSp = processador.getSp();
			byte jmpPosicao1 = memoria.getData(++idxSp);
			byte jmpPosicao2 = memoria.getData(++idxSp);
			short idxIp = concatenarBytes(jmpPosicao1, jmpPosicao2);

			processador.setIp(idxIp);
			processador.setSp(idxSp);

		} else if (Instrucao.IN_AX.equals(instrucao)) {
			// TODO Validar se deve congelar esperando uma entrada do teclado.

		} else if (Instrucao.OUT_AX.equals(instrucao)) {
			// TODO Criar o monitor
			byte caractere = memoria.getData(processador.incIp());
			// monitor.append(caractere);

		} else if (Instrucao.PUSH_AX.equals(instrucao)) {
			short sp = processador.getSp();
			memoria.setData(sp--, processador.getAl());
			memoria.setData(sp--, processador.getAh());
			processador.setSp(sp);

		} else if (Instrucao.PUSH_BX.equals(instrucao)) {
			short sp = processador.getSp();
			memoria.setData(sp--, processador.getBl());
			memoria.setData(sp--, processador.getBh());
			processador.setSp(sp);

		} else if (Instrucao.PUSH_CX.equals(instrucao)) {
			short sp = processador.getSp();
			memoria.setData(sp--, processador.getCl());
			memoria.setData(sp--, processador.getCh());
			processador.setSp(sp);

		} else if (Instrucao.PUSH_BP.equals(instrucao)) {
			short sp = processador.getSp();
			memoria.setData(sp--, processador.getBpl());
			memoria.setData(sp--, processador.getBph());
			processador.setSp(sp);

		} else if (Instrucao.POP_BP.equals(instrucao)) {
			short sp = processador.getSp();
			byte data1 = memoria.getData(++sp);
			byte data2 = memoria.getData(++sp);
			short bp = concatenarBytes(data1, data2);

			processador.setBp(bp);
			processador.setSp(sp);

		} else if (Instrucao.POP_CX.equals(instrucao)) {
			short sp = processador.getSp();
			byte data1 = memoria.getData(++sp);
			byte data2 = memoria.getData(++sp);
			short cx = concatenarBytes(data1, data2);

			processador.setCx(cx);
			processador.setSp(sp);

		} else if (Instrucao.POP_BX.equals(instrucao)) {
			short sp = processador.getSp();
			byte data1 = memoria.getData(++sp);
			byte data2 = memoria.getData(++sp);
			short bx = concatenarBytes(data1, data2);

			processador.setBx(bx);
			processador.setSp(sp);

		} else if (Instrucao.POP_AX.equals(instrucao)) {
			short sp = processador.getSp();
			byte data1 = memoria.getData(++sp);
			byte data2 = memoria.getData(++sp);
			short ax = concatenarBytes(data1, data2);

			processador.setAx(ax);
			processador.setSp(sp);

		} else if (Instrucao.NOP.equals(instrucao)) {
			Thread.sleep(1);

		} else if (Instrucao.HALT.equals(instrucao)) {
			// Sai no while

		} else if (Instrucao.DEC_SP.equals(instrucao)) {
			processador.setSp((short) (processador.getSp() - 1)); // TODO DEC 1 ou 2?

		} else if (Instrucao.MOV_MEN_BP_S_AX.equals(instrucao)) {
		} else if (Instrucao.MOV_MEN_BP_P_AX.equals(instrucao)) {
		} else if (Instrucao.MOV_AX_LITERAL.equals(instrucao)) {
		} else if (Instrucao.TEST_AX_BX.equals(instrucao)) {
		} else if (Instrucao.INC_SP.equals(instrucao)) {

		} else if (Instrucao.MOV_AX_SP.equals(instrucao)) {
		} else if (Instrucao.MOV_SP_AX.equals(instrucao)) {
		} else if (Instrucao.MOV_AX_BP.equals(instrucao)) {
		} else if (Instrucao.MOV_BP_AX.equals(instrucao)) {

		} else if (Instrucao.IRET.equals(instrucao)) {
		} else if (Instrucao.INT.equals(instrucao)) {

		} else if (Instrucao.BP_P.equals(instrucao)) {
		} else if (Instrucao.BP_S.equals(instrucao)) {
		} else {
			throw new InstrucaoInvalidaException();
		}
	}

	private short concatenarBytes(byte byte1, byte byte2) {
		return (short) ((byte1 << 8) | byte2);
	}

	private Instrucao proximaInstrucao(Processador processador, Memoria memoria, Map<Byte, Instrucao> instrucoes) throws PosicaoMemoriaInvalida {
		short ip = processador.incIp();

		byte data = memoria.getData(ip);
		return instrucoes.get(data);
	}

	private void validarProcessar(Processador processador, Memoria memoria) throws SemProcessadorException, SemMemoriaException, MemoriaSemTamanhoException {
		if (null == processador) {
			throw new SemProcessadorException();
		}

	}
}
