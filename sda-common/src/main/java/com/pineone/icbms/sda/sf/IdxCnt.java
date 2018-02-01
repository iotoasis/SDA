package com.pineone.icbms.sda.sf;

/**
 *   개수값을 가지는 클래스
 */
public class IdxCnt {
	int idx;
	int cnt;

	public int getCnt() {
		return cnt;
	}

	public void setCnt(int cnt) {
		this.cnt = cnt;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	@Override
	public String toString() {
		return "IdxCnt [idx=" + idx + ", cnt=" + cnt + "]";
	}
	
	
}
