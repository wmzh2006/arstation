package com.funoble.myarstation.gamebase;

import java.io.*;

import com.funoble.myarstation.common.Tools;

class AutoMoveModeCell {
	public int dir;
	public int speed;
	public int tick;
	public int bullet;
	public int fireLogic;
	
	public AutoMoveModeCell() {
		dir = 0;
		speed = 0;
		tick = 0;
		bullet = 0;
		fireLogic = 0;
	}
	
	public AutoMoveModeCell getCopy() {
		AutoMoveModeCell result = new AutoMoveModeCell();
		result.copyFrom(this);
		return result;
	}
	
	public void copyFrom(AutoMoveModeCell enemyLogic) {
		this.dir = enemyLogic.dir;
		this.speed = enemyLogic.speed;
		this.tick = enemyLogic.tick;
		this.bullet = enemyLogic.bullet;
		this.fireLogic = enemyLogic.fireLogic;
	}
	
	
	public void load(DataInputStream in) throws Exception {
		dir = in.readInt();
		speed = in.readInt();
		tick = in.readInt();
		bullet = in.readInt();
		fireLogic = in.readInt();
	}
}




public class AutoMoveMode {
	public AutoMoveModeCell[] ammcs;
	
	/**
	 * 释放自己
	 */
	public void releaseSelf() {
		if(ammcs != null) {
			for(int i=0; i<ammcs.length; i++) {
				ammcs[i] = null;
			}
			ammcs = null;
		}
		//debug
		Tools.println("AutoMoveMode释放！");
	}
	
	public void AutoMoveMode() {
		ammcs = null;
	}
	
	public AutoMoveMode getCopy() {
		AutoMoveMode result = new AutoMoveMode();
		result.copyFrom(this);
		return result;
	}
	
	public void copyFrom(AutoMoveMode amm) {
		if(amm.ammcs == null) {
			this.ammcs = null;
		}
		else {
			this.ammcs = new AutoMoveModeCell[amm.ammcs.length];
			for(int i = 0; i < amm.ammcs.length; ++i) {
				this.ammcs[i] = amm.ammcs[i].getCopy();
			}
		}
	}
	
	public void load(DataInputStream in) throws Exception {
		ammcs = null;
		int length = in.readInt();
		if(length > 0) {
			ammcs = new AutoMoveModeCell[length];
			for(int i = 0; i < length; ++i) {
				ammcs[i] = new AutoMoveModeCell();
				ammcs[i].load(in);
			}
		}
	}
}

