/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vecima.eassimulatorcli.util;

/**
 *
 * @author zayeed
 */
public class ErrorMessages {

    private static final String[] MSG = {                                       //Error Code:
        "Invalid Source IP!",                                                    //0
        "Invalid Source Port!",                                                  //1
        "Invalid Destination IP!",                                               //2
        "Invalid Destination Port!",                                             //3
        "alertId should only be numeric!",                                      //4
        "alertMessageDuplicateCount should only be numeric!",                   //5
        "additionalStartDelay should only be numeric!",                         //6
        "alertMessageTimeRemaining should only be numeric and between (0-255)!",            //7
        "alertPriority should only be numeric and either of (0, 3, 7, 11 or 15)!",                      //8
        "detailsMajorChannelNumber should only be numeric and between (0-1023)!",                       //9
        "detailsMinorChannelNumber should only be numeric and between (0-1023)!",                       //10
        "detailsAudioOobId should only be numeric and between (0-65535)!",                              //11
        "detailsVideoOobId should only be numeric and between (0-65535)!",                              //12
        "Could not select network interface. Check your configuration file and look for srcIP.",        //13
        "Invalid EAS alert Code!",                                               //14
        "Invalid Location Code!",                                               //15
        "Invalid character(s) in inBandExceptionList!",                          //16
        "Invalid character(s) in oobExceptionList!",                             //17
        "isInBandEnabled field can take either true or false",                  //18
        "exceptionChannelList can take either enabled or disabled",             //19 
        "Invalid Major-Minor channel combination in inBandExceptionList, or extra space between a pair (should be seperated by only one space)!",       //20
        "outOfBandPID can take either enabled or disabled",                     //21
        "multicastTtl can only be numeric and between (0-200)!",                //22
        "eventDuration can only be numeric and between (0-65535)!",             //23
        "eventStartTime can only be numeric!",                                  //24
    };


    /**
     * @return the MSG
     */
    public static String[] getMSG() {
        return MSG;
    }
}