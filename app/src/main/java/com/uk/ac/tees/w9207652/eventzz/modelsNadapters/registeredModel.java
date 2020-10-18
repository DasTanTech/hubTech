package com.uk.ac.tees.w9207652.eventzz.modelsNadapters;

public class registeredModel {
    private String EID,EName,ESponsors,EVenue,EDate;

    public registeredModel(String EID, String EName, String ESponsors, String EVenue, String EDate) {
        this.EID = EID;
        this.EName = EName;
        this.ESponsors = ESponsors;
        this.EVenue = EVenue;
        this.EDate = EDate;
    }

    public registeredModel() {
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
}
