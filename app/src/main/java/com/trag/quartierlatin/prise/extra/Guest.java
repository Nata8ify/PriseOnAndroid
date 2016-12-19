/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.trag.quartierlatin.prise.extra;

/**
 *
 * @author QuartierLatin
 */
public class Guest {

    private int guestNo;
    private String seatRow;
    private String seatNo; // As String
    private int awardNo;
    private String award;
    private String guestName;
    private String corp;
    private String position;
    private int status;
    private int eventId;
    private int userId;

    public int getGuestNo() {
        return guestNo;
    }

    public void setGuestNo(int guestNo) {
        this.guestNo = guestNo;
    }

    public String getSeatRow() {
        return seatRow;
    }

    public void setSeatRow(String seatRow) {
        this.seatRow = seatRow;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public int getAwardNo() {
        return awardNo;
    }

    public void setAwardNo(int awardNo) {
        this.awardNo = awardNo;
    }

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getCorp() {
        return corp;
    }

    public void setCorp(String corp) {
        this.corp = corp;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    
    @Override
    public String toString() {
        return "Guest{" + "guestNo=" + guestNo + ", seatRow=" + seatRow + ", seatNo=" + seatNo + ", awardNo=" + awardNo + ", award=" + award + ", guestName=" + guestName + ", corp=" + corp + ", position=" + position + ", status=" + status + ", eventId=" + eventId + ", userId=" + userId + '}';
    }

    
    
}
