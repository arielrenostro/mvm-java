package br.ariel.mvm.model;

/**
 * @author ariel
 */
public class Processador {

	private short ax = (short) 0x0000;
	private short bx = (short) 0x0000;
	private short cx = (short) 0x0000;

	private short ip = (short) 0x0000;
	private short bp = (short) 0x0000;
	private short sp = (short) 0x0000;

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
		return (byte) ((short) 0x00FF & ax);
	}

	public byte getAh() {
		return (byte) (((short) 0xFF00 & ax) >> 8);
	}

	public byte getBl() {
		return (byte) ((short) 0x00FF & bx);
	}

	public byte getBh() {
		return (byte) (((short) 0xFF00 & bx) >> 8);
	}

	public byte getCl() {
		return (byte) ((short) 0x00FF & cx);
	}

	public byte getCh() {
		return (byte) (((short) 0xFF00 & cx) >> 8);
	}

	public byte getBpl() {
		return (byte) ((short) 0x00FF & bp);
	}

	public byte getBph() {
		return (byte) (((short) 0xFF00 & bp) >> 8);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" ax [");
		sb.append(ax);
		sb.append("] bx [");
		sb.append(bx);
		sb.append("] cx [");
		sb.append(cx);
		sb.append("] bp [");
		sb.append(bp);
		sb.append("] sp [");
		sb.append(sp);
		sb.append("] ip [");
		sb.append(ip);
		sb.append("]");
		return sb.toString();
	}
}
