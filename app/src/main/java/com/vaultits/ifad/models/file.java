package com.vaultits.ifad.models;

public class file {

    private String mID;
    private String mTitle;
    private String mAuthor;
    private String Date;
    private String mPath;
    private String mAppointmentID;
    private String mStatus;
    private String mUri;

    public String getmUri() {
        return mUri;
    }

    public void setmUri(String mUri) {
        this.mUri = mUri;
    }

    public String getmStatus() {
        return mStatus;
    }
    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getmAppointmentID() {
        return mAppointmentID;
    }

    public void setmAppointmentID(String mAppointmentID) {
        this.mAppointmentID = mAppointmentID;
    }

    public String getmID() {
        return mID;
    }

    public void setmID(String mID) {
        this.mID = mID;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public void setmAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getmPath() {
        return mPath;
    }

    public void setmPath(String mPath) {
        this.mPath = mPath;
    }
}
