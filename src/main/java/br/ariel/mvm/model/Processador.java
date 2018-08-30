package br.ariel.mvm.model;

/**
 * @author ariel
 */
public class Processador {

	private short ax = (short) 0x0000;
	private short bx = (short) 0x0000;
	private short cx = (short) 0x0000;

	private short ip = (short) 0x0000;
	private short bp = (short) 0xFFFF;
	private short sp = (short) 0xFFFF;

	public Processador() {
		super();
	}

	public short getAx() {
		return ax;
	}

	public void setAx(short ax) {
		this.ax = ax;
	}

	public short getBx() {
		return bx;
	}

	public void setBx(short bx) {
		this.bx = bx;
	}

	public short getCx() {
		return cx;
	}

	public void setCx(short cx) {
		this.cx = cx;
	}

	public short getBp() {
		return bp;
	}

	public void setBp(short bp) {
		this.bp = bp;
	}

	public short getIp() {
		return ip;
	}

	public void setIp(short ip) {
		this.ip = ip;
	}

	public short getSp() {
		return sp;
	}

	public void setSp(short sp) {
		this.sp = sp;
	}

	public short incIp() {
		return ip++;
	}

	public byte getAl() {
		return (byte) (0x00FF & ax);
	}

	public byte getAh() {
		return (byte) (0xFF00 & ax);
	}

	public byte getBl() {
		return (byte) (0x00FF & bx);
	}

	public byte getBh() {
		return (byte) (0xFF00 & bx);
	}

	public byte getCl() {
		return (byte) (0x00FF & cx);
	}

	public byte getCh() {
		return (byte) (0xFF00 & cx);
	}

	public byte getBpl() {
		return (byte) (0x00FF & bp);
	}

	public byte getBph() {
		return (byte) (0xFF00 & bp);
	}
}
