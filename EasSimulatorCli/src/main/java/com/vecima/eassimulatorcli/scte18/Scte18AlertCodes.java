/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vecima.eassimulatorcli.scte18;

import com.vecima.eassimulatorcli.datetime.DateTime;
import java.text.ParseException;
import java.util.Calendar;

/**
 *
 * @author zayeed
 *
 */
public class Scte18AlertCodes {

    private static final String[][] ALERT_CODES = new String[][]{
        {"RWT", "REQUIRED WEEKLY TEST", "1"},                                   // National codes 1
        {"RMT", "REQUIRED MONTHLY TEST", "1"},
        {"EAT", "Emergency Action Termination", "1"},
        {"EAN", "Emergency Action Notification", "1"},
        {"NIC", "National Information Center", "1"},
        {"NPT", "National Periodic Test", "1"},
        {"ADR", "Administrative Message", "0"},                                 //local codes 0
        {"AVW", "Avalanche Warning", "0"},
        {"AVA", "Avalanche Watch", "0"},
        {"BZW", "Blizzard Warning", "0"},
        {"CAE", "Child Abduction Emergency", "0"},
        {"CDW", "Civil Danger Warning", "0"},
        {"CEM", "Civil Emergency Message", "0"},
        {"CFW", "Coastal Flood Warning", "0"},
        {"CFA", "Coastal Flood Watch", "0"},
        {"DFW", "Dust Storm Warning", "0"},
        {"EQW", "Earthquake Warning", "0"},
        {"EVI", "Evacuation Immediate", "0"},
        {"FRW", "Fire Warning", "0"},
        {"FFW", "Flash Flood Warning", "0"},
        {"FFA", "Flash Flood Watch", "0"},
        {"FFS", "Flash Flood Statement", "0"},
        {"FLS", "Flood Statement", "0"},
        {"FLW", "Flood Warning", "0"},
        {"FLA", "Flood Watch", "0"},
        {"HMW", "Hazardous Materials Warning", "0"},
        {"HWW", "High Wind Warning", "0"},
        {"HWA", "High Wind Watch", "0"},
        {"HUW", "Hurricane Warning", "0"},
        {"HUA", "Hurricane Watch", "0"},
        {"HLS", "Hurricane Statement", "0"},
        {"LEW", "Law Enforcement Warning", "0"},
        {"LAE", "Local Area Emergency", "0"},
        {"NMN", "Network Message Notification", "0"},
        {"TOE", "911 Telephone Outage Emergency", "0"},
        {"NUW", "Nuclear Power Plant Warning", "0"},
        {"DMO", "Practice/Demo Warning", "0"},
        {"RHW", "Radiological Hazard Warning", "0"},
        {"SVR", "Severe Thunderstorm Warning", "0"},
        {"SVA", "Severe Thunderstorm Watch", "0"},
        {"SVS", "Severe Weather Statement", "0"},
        {"SMW", "Special Marine Warning", "0"},
        {"SPS", "Special Weather Statement", "0"},
        {"TOR", "Tornado Warning", "0"},
        {"TOA", "Tornado Watch", "0"},
        {"TRW", "Tropical Storm Warning", "0"},
        {"TRA", "Tropical Storm Watch", "0"},
        {"TSW", "Tsunami Warning", "0"},
        {"TSA", "Tsunami Watch", "0"},
        {"VOW", "Volcano Warning", "0"},
        {"WSW", "Winter Storm Warning", "0"},
        {"WSA", "Winter Storm Watch", "0"},};

    private String replaceAlertCode = getAlertCodes()[0][1];
    private String replaceLocations = "Pinal, AZ;";
    private String replaceAlertStartTime = "";
    private String replaceAlertEndTime = "";

    private String alertMsg;

    public Scte18AlertCodes() throws ParseException {
        //LocalDateTime now = LocalDateTime.now().plusMinutes(10);
        Calendar calendar = Calendar.getInstance();
        replaceAlertStartTime = DateTime.formatDateTime("" + calendar.getTime());
        // Add 15 minutes to the calendar time
        calendar.add(Calendar.MINUTE, 15);
        replaceAlertEndTime = DateTime.getTime(calendar.getTime() + "");
    }

    /**
     * @param replaceAlertCode the replaceAlertCode to set
     * @return
     */
    public Scte18AlertCodes setReplaceAlertCode(String replaceAlertCode) {
        for (int i = 0; i < ALERT_CODES.length; i++) {
            if (ALERT_CODES[i][0].equals(replaceAlertCode)) {
                this.replaceAlertCode = ALERT_CODES[i][1].toUpperCase();
            }
        }
        return this;
    }

    /**
     * @param replaceLocations the replaceLocations to set
     * @return
     */
    public Scte18AlertCodes setReplaceLocations(String replaceLocations) {
        this.replaceLocations = replaceLocations;
        return this;
    }

    /**
     * @param replaceAlertStartTime the replaceAlertStartTime to set
     * @return
     */
    public Scte18AlertCodes setReplaceAlertStartTime(String replaceAlertStartTime) {
        this.replaceAlertStartTime = replaceAlertStartTime;
        return this;
    }

    /**
     * @param replaceAlertEndTime the replaceAlertEndTime to set
     * @return
     */
    public Scte18AlertCodes setReplaceAlertEndTime(String replaceAlertEndTime) {
        this.replaceAlertEndTime = replaceAlertEndTime;
        return this;
    }

    /**
     * @return the alertMsg
     */
    public String getAlertMsg() {
        return alertMsg;
    }

    /**
     * @return the alertCodes
     */
    public static String[][] getAlertCodes() {
        return ALERT_CODES;
    }

    /**
     * @param alertMsg the alertMsg to set
     * @return
     */
    public Scte18AlertCodes setAlertMsg() {
        String article;
        //if(replaceAlertCode.charAt(0) == 'A')
        switch (replaceAlertCode.charAt(0)) {
            case 'A':
            case 'E':
            case 'I':
            case 'O':
            case 'U':
                /*vowel*/
                article = "AN";
                break;
            default:
                /*not a vowel*/
                article = "A";
                break;
        }

        this.alertMsg = "Vecima Networks HAS ISSUED " + article + " " + replaceAlertCode + " FOR THE FOLLOWING COUNTIES/AREAS: " + replaceLocations + "AT " + getReplaceAlertStartTime() + " EFFECTIVE UNTIL " + getReplaceAlertEndTime() + ". MESSAGE FROM Vecima. ";
        return this;
    }

    /**
     * @return the replaceAlertStartTime
     */
    public String getReplaceAlertStartTime() {
        return replaceAlertStartTime;
    }

    /**
     * @return the replaceAlertEndTime
     */
    public String getReplaceAlertEndTime() {
        return replaceAlertEndTime;
    }

}
