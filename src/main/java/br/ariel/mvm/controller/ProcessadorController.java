package br.ariel.mvm.controller;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import br.ariel.mvm.exception.InstrucaoInvalidaException;
import br.ariel.mvm.exception.MVMException;
import br.ariel.mvm.exception.PosicaoMemoriaInvalida;
import br.ariel.mvm.model.Instrucao;
import br.ariel.mvm.model.Memoria;
import br.ariel.mvm.model.Monitor;
import br.ariel.mvm.model.Processador;

/**
 * @author ariel
 */
public class ProcessadorController {

	public void processar(Processador processador, Memoria memoria, Monitor monitor) throws MVMException, InterruptedException {
		Map<Byte, Instrucao> instrucoes = carregarInstrucoes();
		Instrucao instrucao = null;

		while (instrucao != Instrucao.HALT) {
			instrucao = proximaInstrucao(processador, memoria, instrucoes);
			executarInstrucao(processador, memoria, monitor, instrucao);
		}
	}

	private Map<Byte, Instrucao> carregarInstrucoes() {
		return Stream.of(Instrucao.values()).collect(Collectors.toMap(Instrucao::getCode, Function.identity()));
	}

	private void executarInstrucao(Processador processador, Memoria memoria, Monitor monitor, Instrucao instrucao) throws MVMException, InterruptedException {
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
			byte low = memoria.getData(processador.incIp());
			byte high = memoria.getData(processador.incIp());
			short idx = concatenarBytes(high, low);

			low = memoria.getData(idx++);
			high = memoria.getData(idx);

			short ax = concatenarBytes(high, low);
			processador.setAx(ax);

		} else if (Instrucao.MOV_AX_MEM_BX_P.equals(instrucao)) {
			byte low = memoria.getData(processador.incIp());
			byte high = memoria.getData(processador.incIp());
			short idx = concatenarBytes(high, low);
			idx += processador.getBx();

			low = memoria.getData(idx++);
			high = memoria.getData(idx);

			short ax = concatenarBytes(high, low);
			processador.setAx(ax);

		} else if (Instrucao.MOV_AX_MEM_BP_S.equals(instrucao)) {
			byte low = memoria.getData(processador.incIp());
			byte high = memoria.getData(processador.incIp());
			short idx = concatenarBytes(high, low);
			idx -= processador.getBp();

			low = memoria.getData(idx++);
			high = memoria.getData(idx);

			short ax = concatenarBytes(high, low);
			processador.setAx(ax);

		} else if (Instrucao.MOV_AX_MEM_BP_P.equals(instrucao)) {
			byte low = memoria.getData(processador.incIp());
			byte high = memoria.getData(processador.incIp());
			short idx = concatenarBytes(high, low);
			idx += processador.getBp();

			low = memoria.getData(idx++);
			high = memoria.getData(idx);

			short ax = concatenarBytes(high, low);
			processador.setAx(ax);

		} else if (Instrucao.MOV_MEM_AX.equals(instrucao)) {
			byte low = memoria.getData(processador.incIp());
			byte high = memoria.getData(processador.incIp());
			short idx = concatenarBytes(high, low);
			memoria.setData(idx++, processador.getAl());
			memoria.setData(idx, processador.getAh());

		} else if (Instrucao.MOV_MEM_BX_P_AX.equals(instrucao)) {
			byte low = memoria.getData(processador.incIp());
			byte high = memoria.getData(processador.incIp());
			short idx = concatenarBytes(high, low);
			idx += processador.getBx();
			memoria.setData(idx++, processador.getAl());
			memoria.setData(idx, processador.getAh());

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
				executarInstrucao(processador, memoria, monitor, Instrucao.JMP);
			} else {
				short ip = (short) (processador.getIp() + 2);
				processador.setIp(ip);
			}

		} else if (Instrucao.JMP.equals(instrucao)) {
			byte jmpPosicaoLow = memoria.getData(processador.incIp());
			byte jmpPosicaoHigh = memoria.getData(processador.incIp());
			short jmpIdx = concatenarBytes(jmpPosicaoHigh, jmpPosicaoLow);

			processador.setIp(jmpIdx);
		} else if (Instrucao.CALL.equals(instrucao)) {
			byte jmpPosicaoLow = memoria.getData(processador.incIp());
			byte jmpPosicaoHigh = memoria.getData(processador.incIp());
			short idxCall = concatenarBytes(jmpPosicaoHigh, jmpPosicaoLow);

			short idxProximoIp = processador.incIp();
			short idxSp = processador.getSp();
			memoria.setData(idxSp--, (byte) (idxProximoIp & 0xFF00));
			memoria.setData(idxSp--, (byte) (idxProximoIp & 0x00FF));

			processador.setSp(idxSp);
			processador.setIp(idxCall);

		} else if (Instrucao.RET.equals(instrucao)) {
			short idxSp = processador.getSp();
			byte jmpPosicaoLow = memoria.getData(++idxSp);
			byte jmpPosicaoHigh = memoria.getData(++idxSp);
			short idxIp = concatenarBytes(jmpPosicaoHigh, jmpPosicaoLow);

			processador.setIp(idxIp);
			processador.setSp(idxSp);

		} else if (Instrucao.IN_AX.equals(instrucao)) {
			// TODO Validar se deve congelar esperando uma entrada do teclado.

		} else if (Instrucao.OUT_AX.equals(instrucao)) {
			byte caractere = memoria.getData(processador.incIp());
			monitor.append(caractere);

		} else if (Instrucao.PUSH_AX.equals(instrucao)) {
			short sp = processador.getSp();
			memoria.setData(sp--, processador.getAh());
			memoria.setData(sp--, processador.getAl());
			processador.setSp(sp);

		} else if (Instrucao.PUSH_BX.equals(instrucao)) {
			short sp = processador.getSp();
			memoria.setData(sp--, processador.getBh());
			memoria.setData(sp--, processador.getBl());
			processador.setSp(sp);

		} else if (Instrucao.PUSH_CX.equals(instrucao)) {
			short sp = processador.getSp();
			memoria.setData(sp--, processador.getCh());
			memoria.setData(sp--, processador.getCl());
			processador.setSp(sp);

		} else if (Instrucao.PUSH_BP.equals(instrucao)) {
			short sp = processador.getSp();
			memoria.setData(sp--, processador.getBph());
			memoria.setData(sp--, processador.getBpl());
			processador.setSp(sp);

		} else if (Instrucao.POP_BP.equals(instrucao)) {
			short sp = processador.getSp();
			byte low = memoria.getData(++sp);
			byte high = memoria.getData(++sp);
			short bp = concatenarBytes(high, low);

			processador.setBp(bp);
			processador.setSp(sp);

		} else if (Instrucao.POP_CX.equals(instrucao)) {
			short sp = processador.getSp();
			byte low = memoria.getData(++sp);
			byte high = memoria.getData(++sp);
			short cx = concatenarBytes(high, low);

			processador.setCx(cx);
			processador.setSp(sp);

		} else if (Instrucao.POP_BX.equals(instrucao)) {
			short sp = processador.getSp();
			byte low = memoria.getData(++sp);
			byte high = memoria.getData(++sp);
			short bx = concatenarBytes(high, low);

			processador.setBx(bx);
			processador.setSp(sp);

		} else if (Instrucao.POP_AX.equals(instrucao)) {
			short sp = processador.getSp();
			byte low = memoria.getData(++sp);
			byte high = memoria.getData(++sp);
			short ax = concatenarBytes(high, low);

			processador.setAx(ax);
			processador.setSp(sp);

		} else if (Instrucao.NOP.equals(instrucao)) {
			Thread.sleep(1);

		} else if (Instrucao.HALT.equals(instrucao)) {
			// Espera sair

		} else if (Instrucao.DEC_SP.equals(instrucao)) {
			processador.setSp((short) (processador.getSp() - 2));

		} else if (Instrucao.MOV_MEM_BP_S_AX.equals(instrucao)) {
			byte low = memoria.getData(processador.incIp());
			byte high = memoria.getData(processador.incIp());
			short idx = concatenarBytes(high, low);
			idx = (short) (processador.getBp() - idx);
			memoria.setData(idx++, processador.getAl());
			memoria.setData(idx, processador.getAh());

		} else if (Instrucao.MOV_MEM_BP_P_AX.equals(instrucao)) {
			byte low = memoria.getData(processador.incIp());
			byte high = memoria.getData(processador.incIp());
			short idx = concatenarBytes(high, low);
			idx = (short) (processador.getBp() + idx);
			memoria.setData(idx++, processador.getAl());
			memoria.setData(idx, processador.getAh());

		} else if (Instrucao.MOV_AX_LITERAL.equals(instrucao)) {
			byte low = memoria.getData(processador.incIp());
			byte high = memoria.getData(processador.incIp());
			short ax = concatenarBytes(high, low);
			processador.setAx(ax);

		} else if (Instrucao.TEST_AX_BX.equals(instrucao)) {
			if (processador.getBx() == processador.getAx()) {
				executarInstrucao(processador, memoria, monitor, Instrucao.JMP);
			} else {
				short ip = (short) (processador.getIp() + 2);
				processador.setIp(ip);
			}

		} else if (Instrucao.INC_SP.equals(instrucao)) {
			processador.setSp((short) (processador.getSp() + 2));

		} else if (Instrucao.MOV_AX_SP.equals(instrucao)) {
			processador.setAx(processador.getSp());

		} else if (Instrucao.MOV_SP_AX.equals(instrucao)) {
			processador.setSp(processador.getAx());

		} else if (Instrucao.MOV_AX_BP.equals(instrucao)) {
			processador.setAx(processador.getBp());

		} else if (Instrucao.MOV_BP_AX.equals(instrucao)) {
			processador.setBp(processador.getAx());

		} else if (Instrucao.IRET.equals(instrucao)) {
			// //"pop cx"
			// //"pop bx"
			// //"pop ax"
			// //"pop bp"
			// //"ret"

			executarInstrucao(processador, memoria, monitor, Instrucao.POP_CX);
			executarInstrucao(processador, memoria, monitor, Instrucao.POP_BX);
			executarInstrucao(processador, memoria, monitor, Instrucao.POP_AX);
			executarInstrucao(processador, memoria, monitor, Instrucao.POP_BP);
			executarInstrucao(processador, memoria, monitor, Instrucao.RET);

		} else if (Instrucao.INT.equals(instrucao)) {
			// //"push ip"
			// //"push bp"
			// //"push ax"
			// //"push bx"
			// //"push cx"
			// //"int"
			short idxInt = processador.incIp();

			short idxProximoIp = processador.incIp();
			short idxSp = processador.getSp();
			memoria.setData(idxSp--, (byte) (idxProximoIp & 0x00FF));
			memoria.setData(idxSp--, (byte) (idxProximoIp & 0xFF00));

			processador.setSp(idxSp);
			processador.setIp(idxInt);
			executarInstrucao(processador, memoria, monitor, Instrucao.PUSH_BP);
			executarInstrucao(processador, memoria, monitor, Instrucao.PUSH_AX);
			executarInstrucao(processador, memoria, monitor, Instrucao.PUSH_BX);
			executarInstrucao(processador, memoria, monitor, Instrucao.PUSH_CX);

		} else if (Instrucao.INC_BP.equals(instrucao)) {
			processador.setBp((short) (processador.getBp() + 1));

		} else if (Instrucao.DEC_BP.equals(instrucao)) {
			processador.setBp((short) (processador.getBp() - 1));

		}
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
	private short concatenarBytes(byte high, byte low) {
		return (short) ((high << 8) | (0xFF & low));
	}

	private Instrucao proximaInstrucao(Processador processador, Memoria memoria, Map<Byte, Instrucao> instrucoes) throws PosicaoMemoriaInvalida, InstrucaoInvalidaException {
		short ip = processador.incIp();
		byte data = memoria.getData(ip);
		Instrucao instrucao = instrucoes.get(data);
		if (null == instrucao) {
			throw new InstrucaoInvalidaException(data);
		}
		return instrucao;
	}

}
