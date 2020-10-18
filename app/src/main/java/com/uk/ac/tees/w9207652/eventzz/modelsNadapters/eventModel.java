package com.uk.ac.tees.w9207652.eventzz.modelsNadapters;

public class eventModel {
    private String EID,EName,ESponsors,EDesc,EPrice,EVenue,EDate,EPicUrl,COID;

    public eventModel() {
    }

    public eventModel(String EID, String EName, String ESponsors, String EDesc, String EPrice, String EVenue, String EDate, String EPicUrl, String COID) {
        this.EID = EID;
        this.EName = EName;
        this.ESponsors = ESponsors;
        this.EDesc = EDesc;
        this.EPrice = EPrice;
        this.EVenue = EVenue;
        this.EDate = EDate;
        this.EPicUrl = EPicUrl;
        this.COID = COID;
    }

    public String getEID() {
        return EID;
    }

    public void setEID(String EID) {
        this.EID = EID;
    }

    public String getEName() {
        return EName;
    }

    public void setEName(String EName) {
        this.EName = EName;
    }

    public String getESponsors() {
        return ESponsors;
    }

    public void setESponsors(String ESponsors) {
        this.ESponsors = ESponsors;
    }

    public String getEDesc() {
        return EDesc;
    }

    public void setEDesc(String EDesc) {
        this.EDesc = EDesc;
    }

    public String getEPrice() {
        return EPrice;
    }

    public void setEPrice(String EPrice) {
        this.EPrice = EPrice;
    }

    public String getEVenue() {
        return EVenue;
    }

    public void setEVenue(String EVenue) {
        this.EVenue = EVenue;
    }

    public String getEDate() {
        return EDate;
    }

    public void setEDate(String EDate) {
        this.EDate = EDate;
    }

    public String getEPicUrl() {
        return EPicUrl;
    }

    public void setEPicUrl(String EPicUrl) {
        this.EPicUrl = EPicUrl;
    }

    public String getCOID() {
        return COID;
    }

    public void setCOID(String COID) {
        this.COID = COID;
    }
}
