package com.funoble.myarstation.resmanager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import com.funoble.myarstation.common.Tools;
import com.funoble.myarstation.game.MyArStation;
import com.funoble.myarstation.gamebase.CBuilding;
import com.funoble.myarstation.gamebase.Map;
import com.funoble.myarstation.gamebase.Project;
import com.funoble.myarstation.gamebase.Scene;
import com.funoble.myarstation.gamebase.Sprite;
import com.funoble.myarstation.gamelogic.SpriteButton;
import com.funoble.myarstation.utils.ActivityUtil;
import com.funoble.myarstation.utils.TPoint;
import com.funoble.myarstation.view.GameEventID;

public class CPakManager {
	public static Project iUIPak = null;
	public static Project iFacePak = null;
	public static Project iDeskPak = null;
//	public static Project iYanHuaPak = null;
	public static Project[] iItemPak = null;
	public static Project iWeiSePak = null;
	public static Project iPiKongPak = null;
	public static Project iZhaiPak = null;
	public static Project iActivityInfPak = null;
	public static Project iLightningPak = null;
	public static Project iCarPak = null;
	public static Project iSkillIconPak = null;
	public static Project iSelectRoom = null;
	public static Project iMyHomePak = null;
	public static Project iInfoPak = null;
	public static Project iLoadingPak = null;
	public static Project iBulletinPak = null;
	private static HashMap<String, Project> SynProjects = null;
	
	public static int MAX_ITEM_SPRITE_TYPE = 2; //道具动画的类型数
	
	public static Project loadUIPak() {
		if(iUIPak == null) {
			iUIPak = Project.loadProject(ActivityUtil.PATH_SCREEN+"login_map.pak", false);
		}
		return iUIPak;
	}
	
	public static Project loadBulletinPak() {
		if(iBulletinPak == null) {
			iBulletinPak = Project.loadProject(ActivityUtil.PATH_SCREEN+"bulletin_map.pak", false);
		}
		return iBulletinPak;
	}
	
	public static Project loadSelectRoomPak() {
		if(iSelectRoom == null) {
			iSelectRoom = Project.loadProject(ActivityUtil.PATH_SCREEN+"selectroom_map.pak", false);
		}
		return iSelectRoom;
	}
	
	public static void ReleaseUIPak() {
//		iUIPak.releaseSelf();
		iUIPak = null;
	}
	
	public static Project loadLoading() {
		if(iLoadingPak == null) {
			iLoadingPak = Project.loadProject(ActivityUtil.PATH_SCREEN+"loading_map.pak", false);
		}
		return iLoadingPak;
	}
	
	public static Project loadDeskPak() {
		if(iDeskPak == null) {
			iDeskPak = Project.loadProject(ActivityUtil.PATH_SCREEN+"desk_map.pak", false);
		}
		return iDeskPak;
	}
		
	public static Project loadFacePak() {
		if(iFacePak == null) {
			iFacePak = Project.loadProject(ActivityUtil.PATH_SCREEN+"face_map.pak", false);
		}
		return iFacePak;
	}
	
//	public static Project loadYanHuaPak() {
//		if(iYanHuaPak == null) {
//			iYanHuaPak = Project.loadProject(ActivityUtil.PATH_SCREEN+"yanhua_map.pak", false);
//		}
//		return iYanHuaPak;
//	}
	
	public static Project loadItemPak(int aID) {
		if(iItemPak == null) {
			iItemPak = new Project[MAX_ITEM_SPRITE_TYPE];
			for(int i=0; i<MAX_ITEM_SPRITE_TYPE; i++) {
				iItemPak[i] = null;
			}
		}
		if(iItemPak[aID] == null) {
			iItemPak[aID] = Project.loadProject(ActivityUtil.PATH_SCREEN+"item" + aID + "_map.pak", false);
		}
		return iItemPak[aID];
	}
	
	public static Project loadWeiSePak() {
		if(iWeiSePak == null) {
			iWeiSePak = Project.loadProject(ActivityUtil.PATH_SCREEN+"anim01_map.pak", false);
		}
		return iWeiSePak;
	}
	
	public static Project loadPiKongPak() {
		if(iPiKongPak == null) {
			iPiKongPak = Project.loadProject(ActivityUtil.PATH_SCREEN+"anim02_map.pak", false);
		}
		return iPiKongPak;
	}
	
	public static Project loadZhaiPak() {
		if(iZhaiPak == null) {
			iZhaiPak = Project.loadProject(ActivityUtil.PATH_SCREEN+"anim03_map.pak", false);
		}
		return iZhaiPak;
	}
	
	public static Project loadActivityInfPak() {
		if(iActivityInfPak == null) {
			iActivityInfPak = Project.loadProject(ActivityUtil.PATH_SCREEN+"anim04_map.pak", false);
		}
		return iActivityInfPak;
	}
	
	public static Project loadLightningPak() {
		if(iLightningPak == null) {
			iLightningPak = Project.loadProject(ActivityUtil.PATH_SCREEN+"anim05_map.pak", false);
		}
		return iLightningPak;
	}
	
	public static Project loadCarPak() {
		if(iCarPak == null) {
			iCarPak = Project.loadProject(ActivityUtil.PATH_SCREEN+"car_map.pak", false);
		}
		return iCarPak;
	}
	
	public static Project loadSkillIconPak() {
		if(iSkillIconPak == null) {
			iSkillIconPak = Project.loadProject(ActivityUtil.PATH_SCREEN+"anim06_map.pak", false);
		}
		return iSkillIconPak;
	}
	
	public static Project loadMyHomePak() {
		if(iMyHomePak == null) {
			iMyHomePak = Project.loadProject(ActivityUtil.PATH_SCREEN+"myhome_map.pak", false);
		}
		return iMyHomePak;
	}
	
	public static void ReleaseMyHomePak() {
//		iUIPak.releaseSelf();
		iMyHomePak = null;
	}
	
	public static Project loadInfoPak() {
		if(iInfoPak == null) {
			iInfoPak = Project.loadProject(ActivityUtil.PATH_SCREEN+"info_map.pak", false);
		}
		return iInfoPak;
	}
	
	public static void ReleaseInfoPak() {
//		iUIPak.releaseSelf();
		iInfoPak = null;
	}
	
	public static Project loadSynProject(String aProjectName) {
		if(aProjectName == null) throw new NullPointerException("aProjectName");
		Project project = null;
		if(SynProjects == null) {
			SynProjects = new HashMap<String, Project>();
			Tools.println("ctreate SynProjects!");
		}
		project = SynProjects.get(aProjectName);
		if(project == null) {
			project = MyArStation.iHttpDownloadManager.getProject(aProjectName);
			Tools.println("Download SynProjects!");
			if(project != null) {
				SynProjects.put(aProjectName, project);
				Tools.println("Add SynProjects!");
			}
		}
		return project;
	}
	
	public static void ReleaseSynProject() {
		if(SynProjects != null) {
			Tools.println("ReleaseSynProject size " + SynProjects.size());
			Iterator<String> iterator = (Iterator<String>) SynProjects.keySet().iterator();
			while(iterator.hasNext()) {
				Project project = SynProjects.get(iterator.next());
				Tools.println("ReleaseSynProject " + iterator.next());
				project = null;
			}
			SynProjects.clear();
			Tools.println("ReleaseSynProject size " + SynProjects.size());
		}
	}
	
	public static void ReleaseSynProjectByKey(String name) {
		if(name == null) throw new NullPointerException();
		if(SynProjects != null) {
			Tools.println("ReleaseSynProjectByKey size " + SynProjects.size());
			Iterator<String> iterator = (Iterator<String>) SynProjects.keySet().iterator();
			while(iterator.hasNext()) {
				String key = iterator.next();
				if(key != null && name.equals(key)) {
					SynProjects.remove(key);
					Tools.println("ReleaseSynProjectByKey remove " + key);
					break;
				}
			}
			Tools.println("ReleaseSynProjectByKey size " + SynProjects.size());
		}
	}
}
