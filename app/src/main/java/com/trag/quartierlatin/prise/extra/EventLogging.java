package com.trag.quartierlatin.prise.extra;

/**
 * Created by PNattawut on 18-Dec-16.
 */

public class EventLogging {
    private int eByUserId;
    private int eId;
    private String lByName;
    private int logType;
    private String logDate;
    //11 = Going to the seat with..
    //12 = Quiting from the event with..
    //13 = .. Already at seat.
    //50 = .. is Ready to Receive the Award.
    //51 = .. is Unready to Receive the Award.
    //52 = .. is Absent << May not use.
    //53 = .. is Already Received the Award << May not use.
    //54 = .. is Already Quited the event
    //55 =

    //args
    private String vargs1;
    private int iargs1;

    public int getLogType() {
        return logType;
    }

    public void setLogType(int logType) {
        this.logType = logType;
    }

    public String getlByName() {
        return lByName;
    }

    public void setlByName(String lByName) {
        this.lByName = lByName;
    }

    public String getVargs1() {
        return vargs1;
    }

    public void setVargs1(String vargs1) {
        this.vargs1 = vargs1;
    }

    public int getIargs1() {
        return iargs1;
    }

    public void setIargs1(int iargs1) {
        this.iargs1 = iargs1;
    }

    public String getLogDate() {
        return logDate;
    }

    public void setLogDate(String logDate) {
        this.logDate = logDate;
    }

    @Override
    public String toString() {
        return "EventLogging{" +
                "eByUserId=" + eByUserId +
                ", eId=" + eId +
                ", lByName='" + lByName + '\'' +
                ", logType=" + logType +
                ", logDate='" + logDate + '\'' +
                ", vargs1='" + vargs1 + '\'' +
                ", iargs1=" + iargs1 +
                '}';
    }
}
