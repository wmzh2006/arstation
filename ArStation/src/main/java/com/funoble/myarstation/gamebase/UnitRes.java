package com.funoble.myarstation.gamebase;


import java.io.*;

import com.funoble.myarstation.common.Tools;

public class UnitRes extends BaseRes{
	public final static int TYPE_NPC = 0;
	public final static int TYPE_ROLE = 1;
	public final static int TYPE_PLAYER = 2;
	
	public static final int Enemy = 0;
	public static final int Ally = 1;
	
	public final static int[] TYPES = 
	{
		TYPE_NPC, TYPE_ROLE, TYPE_PLAYER
	};
	public final static int[] CAMP = 
	{
		Enemy, Ally
	};
	
	public final static String[] TYPE_DESCS = 
	{
		"NPC", "角色", "玩家", 
	};
	
	public final static String[] CAMP_DESCS = 
	{
		"敌人", "同盟", 
	};
	
	public int unitType = 1;
	public int enemyType = 2;
	public String name;
	public int hp = 2;
	public int mp = 3;
	public int level = 4;
	public int iCampType = Ally;
	public boolean iFlip = false;
	
	private AutoMoveMode amm;
	private AutoMoveMode amm2;
	
	
	/**
	 * 释放自己
	 */
	public void releaseSelf() {
		if(amm2 != null) {
			amm2.releaseSelf();
			amm2 = null;
		}
		if(amm != null) {
			amm.releaseSelf();
			amm = null;
		}
		super.releaseSelf();
		//debug
		Tools.println("UnitRes释放！");
	}
	
	
	public UnitRes(UnitRes res) {
		iType = RES_UNIT;
		iID = res.iID;
		unitType = res.unitType;
		enemyType = res.enemyType;
		name = res.name;
		hp = res.hp;
		mp = res.mp;
		level = res.level;
		iCampType = res.iCampType;
		amm = res.getAMM();
		amm2 = res.getAMM2();
		iFlip = res.iFlip;
	}
	public UnitRes() {
		iType = RES_UNIT;
		iID = 0;
		unitType = 0;
		enemyType = 0;
		name = "";
		hp = 0;
		mp = 0;
		level = 0;
		iCampType = 0;
		amm = new AutoMoveMode();
		amm2 = new AutoMoveMode();
		iFlip = false;
	}
	
	public AutoMoveMode getAMM() {
		return amm.getCopy();
	}
	
	public void setAMM(AutoMoveMode amm) {
		this.amm = amm.getCopy();
	}
	
	public AutoMoveMode getAMM2() {
		return amm2.getCopy();
	}
	
	public void setAMM2(AutoMoveMode amm2) {
		this.amm2 = amm2.getCopy();
	}
	
	public void intputStream(DataInputStream dis)throws Exception
	{
		iID = dis.readInt();
		unitType = dis.readInt();
		iCampType = dis.readInt();
		name = dis.readUTF();
		enemyType = dis.readInt();
		hp = dis.readInt();
		mp = dis.readInt();
		level = dis.readInt();
		amm.load(dis);
		amm2.load(dis);
		iFlip = dis.readByte() == 1 ? true : false;
//		System.out.println("UnitRes intput = "+hp+"|"+mp);
	}
}