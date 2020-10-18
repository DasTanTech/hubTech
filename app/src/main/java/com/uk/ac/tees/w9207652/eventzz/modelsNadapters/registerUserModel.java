package com.uk.ac.tees.w9207652.eventzz.modelsNadapters;

public class registerUserModel {

    private String EID,UNAME,UEMAIL,ENAME,EDATE;

    public registerUserModel(String EID, String UNAME, String UEMAIL, String ENAME, String EDATE) {
        this.EID = EID;
        this.UNAME = UNAME;
        this.UEMAIL = UEMAIL;
        this.ENAME = ENAME;
        this.EDATE = EDATE;
    }

    public registerUserModel() {
    }

    public String getEID() {
        return EID;
    }

    public void setEID(String EID) {
        this.EID = EID;
    }

    public String getUNAME() {
        return UNAME;
    }

    public void setUNAME(String UNAME) {
        this.UNAME = UNAME;
    }

    public String getUEMAIL() {
        return UEMAIL;
    }

    public void setUEMAIL(String UEMAIL) {
        this.UEMAIL = UEMAIL;
    }

    public String getENAME() {
        return ENAME;
    }

    public void setENAME(String ENAME) {
        this.ENAME = ENAME;
    }

    public String getEDATE() {
        return EDATE;
    }

    public void setEDATE(String EDATE) {
        this.EDATE = EDATE;
    }
}
