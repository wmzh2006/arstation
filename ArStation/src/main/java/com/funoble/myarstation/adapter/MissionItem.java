package com.funoble.myarstation.adapter;

public class MissionItem {
    
	public int 	  missionType;
    public String picName;
    public String name;
    public String detail;
    public int    missionId;
    public int    missionState;
    
    public MissionItem() {
    	 this.picName  = "";
         this.name   = "";
         this.detail = "";
         this.missionId = 0;
         this.missionState = 0;
    }
    
    public MissionItem(int missionId, int missionState, String picName, String name, String detail) {
        this.picName  = picName;
        this.name   = name;
        this.detail = detail;
        this.missionId = missionId;
        this.missionState = missionState;
    }

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MissionItem [missionType=" + missionType + ", picName="
				+ picName + ", name=" + name + ", detail=" + detail
				+ ", missionId=" + missionId + ", missionState=" + missionState
				+ "]";
	}

	/* 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		 boolean flag = obj instanceof MissionItem;  
	        if(flag == false){  
	            return false;  
	        }  
	        MissionItem emp = (MissionItem)obj;  
	        if(this.missionId==emp.missionId && this.missionType == emp.missionType){  
	            return true;  
	        }else {  
	            return false;  
	        }  
	}
	
	
    
}
