/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vecima.eassimulatorcli.scte18;

import com.vecima.eassimulatorcli.App;
import static com.vecima.eassimulatorcli.App.printSummary;
import com.vecima.eassimulatorcli.color.ConsoleColors;
import com.vecima.eassimulatorcli.crc32.Crc32Mpeg;
import com.vecima.eassimulatorcli.util.StringProcessor;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zayeed
 */
public class Scte18Encoder {                                                   //length:
    //private static String scte18Header;
    private String tableID = "1101 1000";                                       //8 bits 0xd8
    private String sectionSyntaxIndicator = "1";                                //1
    private final String zero = "0";                                            //1
    private final String reserved_ = "11";                                      //2
    private String sectionLength = "0000 1111 1100";                            //12  0xFC
    private String tableIdExtension = "0000 0000 0000 0000";                    //16
    private final String reserved_2 = "11";                                     //2
    private String sequenceNumber = "10000";                                    //5 01011
    private String currentNextIndicator = "1";                                  //1
    private String sectionNumber = "0000 0000";                                 //8
    private String lastSectionNumber = "0000 0000";                             //8
    private String protocolVersion = "0000 0000";                               //8
    private String easEventId = "0010 1000 1000 0110";                          //16
    private String easOriginatorCode = "0100 0101 0100 0001 0101 0011";         //24
    private String easEventCodeLength = "0000 0011";                            //8
    private String easEventCode = "0101 0010 0101 0111 0101 0100";              //var
    private String natureOfActivationTextLength = "0001 1100";                  //8 ->28 bytes
    private String natureOfActivationText = "0000 0001 0110 0101 0110 1110 0110 0111 0000 0001 0000 0000 0000 0000 0001 0100 0101 0010 0100 0101 0101 0001 0101 0101 0100 1001 0101 0010 0100 0101 0100 0100 0010 0000 0101 0111 0100 0101 0100 0101 0100 1011 0100 1100 0101 1001 0010 0000 0101 0100 0100 0101 0101 0011 0101 0100";                 //var
    private String alertMessageTimeRemaining = "0111 1000";                     //8 the time the message will be played for
    private String eventStartTime = "0000 0000 0000 0000 0000 0000 0000 0000";  //32
    private String eventDuration = "0000 0000 0000 1111";                       //16
    private final String reserved_3 = "1111 1111 1111";                         //12
    private String alertPriority = "1111";                                      //4
    private String detailsOobSourceId = "0000 0000 0000 0000";                  //16 could be 1EE1 7905
    private final String reserved_4 = "1111 11";                                //6
    private String detailsMajorChannelNumber = "0000 0000 00";                  //10
    private final String reserved_5 = "1111 11";                                //6
    private String detailsMinorChannelNumber = "0000 0000 00";                  //10
    private String audioOobSourceId = "0000 0000 0000 0000";                    //16 could be 1EE1 7905
    private String alertTextLength = "0000 0000 1011 0010";                     //16  178 bytes 
    private String alertText;                                                   //var (178 bytes) 01656E67010000AA + text(170 bytes) (56-20)
    
    private String locationCodeCount = "0000 0001";                             //8
    //for(i = 0; i < locationCodeCount; i++ ){
        private List<String> stateCode = new ArrayList<>();                     //8
        private List<String> countySubdivision = new ArrayList<>();             //4
        private final String reserved_6 = "11";                                 //2
        private List<String> countyCode = new ArrayList<>();             //10
    //}
    private String exceptionCount = "0000 0000";                                //8
    //for (i = 0; i < exceptionCount; i++){
        private String inBandReference;                                         //1
        private final String reserved_7 = "1111 111";                           //7
        //if(inBandReference){
            private final String reserved_8 = "1111 11";                        //6
            private List<String> exceptionMajorChannelNumber = new ArrayList<>(); //10
            private final String reserved_9 = "1111 11";                        //6
            private List<String> exceptionMinorChannelNumber = new ArrayList<>(); //10
        //}
        //else{
            private final String reserved_10= "1111 1111 1111 1111";            //16
            private List<String> exceptionOobSourceId = new ArrayList<>();      //16
        //}
    //}
    private final String reserved_11 = "111111";                                //6
    private String descriptorsLength = "00 0000 0000";                          //10
    //for(i = 0; i < N; i++){   
        private String descriptor="";                                           //var
    //}
    private String crc_32;
    
    public Scte18Encoder(){
        
    }
    
    public String scte18Encode(){
        String exceptionChannelCount="";
        String locationInfo = "";
        //System.out.println("alertText: "+alertText.length()); //178 bytes 1424 bits
        String easHeader = 
                //tableID+
                //sectionSyntaxIndicator+
                //zero+
                //reserved_+
                //sectionLength+    //calculated?
                tableIdExtension+
                reserved_2+
                sequenceNumber+
                currentNextIndicator+
                sectionNumber+
                lastSectionNumber+
                protocolVersion+
                easEventId+
                easOriginatorCode+
                easEventCodeLength+
                easEventCode+
                natureOfActivationTextLength+
                natureOfActivationText+
                alertMessageTimeRemaining+
                eventStartTime+
                eventDuration+
                reserved_3+
                alertPriority+
                detailsOobSourceId+
                reserved_4+
                detailsMajorChannelNumber+
                reserved_5+
                detailsMinorChannelNumber+
                audioOobSourceId+
                alertTextLength+
                alertText+
    
//           01 04 0C 15 00 FC 00
//remaining: 0000 0001 0000 0100 0000 1100 0001 0101 0000 0000 1111 1100 0000 0000

                locationCodeCount;
        for(int i = 0; i < Integer.parseInt(locationCodeCount.replace(" ", ""), 2); i++){
             locationInfo += stateCode.get(i) + countySubdivision.get(i) + reserved_6 + countyCode.get(i);
         }
        //System.out.println(ConsoleColors.PURPLE+"Locations: "+locationInfo);
         //System.out.println("locationInfo: "+locationInfo.replace(" ", ""));
        easHeader += locationInfo + exceptionCount;
        //System.out.println("Exception counT: "+exceptionCount);
        for (int i = 0; i < Integer.parseInt(exceptionCount.replace(" ", ""), 2); i++) {
            if (Integer.parseInt(getInBandReference(), 2) > 0) {
                inBandReference = "1";
                exceptionChannelCount += inBandReference + reserved_7 + reserved_8 + exceptionMajorChannelNumber.get(i) + reserved_9 + exceptionMinorChannelNumber.get(i);
            } else {
                inBandReference = "0";
                exceptionChannelCount += inBandReference + reserved_7 + reserved_10 + exceptionOobSourceId.get(i);
            }
        }
        
        //System.out.println("exceptionChannelCount: "+exceptionChannelCount.replace(" ", ""));
        easHeader += exceptionChannelCount + 
                reserved_11+
                descriptorsLength;
        //System.out.println("lenth: "+easHeader.length()/8);
        for(int i = 0; i < Integer.parseInt(descriptorsLength.replace(" ", ""), 2); i++){
            descriptor += "0000 0000";
        }
        // add descriptor with eas header when completed //
        
        //count the section length and add it to easHeader;
        int sectLen = easHeader.replace(" ", "").length()/8 + 4; //4 bytes for CRC_32
        //System.out.println(ConsoleColors.GREEN_BRIGHT+"Calculated Section Length: "+ConsoleColors.RESET+sectLen);
        sectionLength = StringProcessor.bitStuffer(Integer.toBinaryString(sectLen), Scte18HeaderDefinition.sectionLengthSize);
        //System.out.println("sectionLen: "+sectionLength);
        easHeader = tableID+
                sectionSyntaxIndicator+
                zero+
                reserved_+
                sectionLength+ 
                easHeader;
        
        easHeader = easHeader.replace(" ", "");                                 //strip off all spaces 
        easHeader = StringProcessor.binToHex(easHeader);
        
        //calculate CRC32
        crc_32 = Crc32Mpeg.magicCrc32Mpeg(easHeader);
        
        easHeader = easHeader+crc_32;
        if (!App.tunrOffAllLog) {
            System.out.println("\n" + ConsoleColors.YELLOW + "**************************Generated EAS Payload Info***************************" + ConsoleColors.RESET);
            if (printSummary) {
                printPacketSummary();
            }
            System.out.println(ConsoleColors.GREEN_BRIGHT + "CRC_32" + ConsoleColors.RESET + " = " + crc_32);
            System.out.println(ConsoleColors.GREEN_BRIGHT + "EAS Header" + ConsoleColors.RESET + " = " + easHeader);
            System.out.println(ConsoleColors.GREEN_BRIGHT + "Length" + ConsoleColors.RESET + " = " + easHeader.length() / 2 + " bytes. " + ConsoleColors.GREEN_BRIGHT + "Packets: " + ConsoleColors.RESET + (int) Math.ceil((float) (easHeader.length() / 2) / 184));
            System.out.println("***************************************************************\n");
        }
        easHeader = "00"+easHeader;
        
        return easHeader;
    }


    /**
     * @param tableID the tableID to set
     * @return 
     */
    public Scte18Encoder setTableID(String tableID) {
        this.tableID = tableID;
        return this;
    }

    /**
     * @param sectionSyntaxIndicator the sectionSyntaxIndicator to set
     * @return 
     */
    public Scte18Encoder setSectionSyntaxIndicator(String sectionSyntaxIndicator) {
        this.sectionSyntaxIndicator = sectionSyntaxIndicator;
        return this;
    }

    

    /**
     * @param sectionLength the sectionLength to set
     * @return 
     */
    public Scte18Encoder setSectionLength(String sectionLength) {
        this.sectionLength = sectionLength;
        return this;
    }

    /**
     * @param tableIdExtension the tableIdExtension to set
     * @return 
     */
    public Scte18Encoder setTableIdExtension(String tableIdExtension) {
        this.tableIdExtension = tableIdExtension;
        return this;
    }

    

    /**
     * @param sequenceNumber the sequenceNumber to set
     * @return 
     */
    public Scte18Encoder setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
        return this;
    }

    /**
     * @param currentNextIndicator the currentNextIndicator to set
     * @return 
     */
    public Scte18Encoder setCurrentNextIndicator(String currentNextIndicator) {
        this.currentNextIndicator = currentNextIndicator;
        return this;
    }

    /**
     * @param sectionNumber the sectionNumber to set
     * @return 
     */
    public Scte18Encoder setSectionNumber(String sectionNumber) {
        this.sectionNumber = sectionNumber;
        return this;
    }

    /**
     * @param lastSectionNumber the lastSectionNumber to set
     * @return 
     */
    public Scte18Encoder setLastSectionNumber(String lastSectionNumber) {
        this.lastSectionNumber = lastSectionNumber;
        return this;
    }

    /**
     * @param protocolVersion the protocolVersion to set
     * @return 
     */
    public Scte18Encoder setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
        return this;
    }

    /**
     * @param easEventId the easEventId to set
     * @return 
     */
    public Scte18Encoder setEasEventId(String easEventId) {
        this.easEventId = easEventId;
        return this;
    }

    /**
     * @param easOriginatorCode the easOriginatorCode to set
     * @return 
     */
    public Scte18Encoder setEasOriginatorCode(String easOriginatorCode) {
        this.easOriginatorCode = easOriginatorCode;
        return this;
    }

    /**
     * @param easEventCodeLength the easEventCodeLength to set
     * @return 
     */
    public Scte18Encoder setEasEventCodeLength(String easEventCodeLength) {
        this.easEventCodeLength = easEventCodeLength;
        return this;
    }

    /**
     * @param easEventCode the easEventCode to set
     * @return 
     */
    public Scte18Encoder setEasEventCode(String easEventCode) {
        this.easEventCode = easEventCode;
        return this;
    }

    /**
     * @param natureOfActivationTextLength the natureOfActivationTextLength to set
     * @return 
     */
    public Scte18Encoder setNatureOfActivationTextLength(String natureOfActivationTextLength) {
        this.natureOfActivationTextLength = natureOfActivationTextLength;
        return this;
    }

    /**
     * @param natureOfActivationText the natureOfActivationText to set
     * @return 
     */
    public Scte18Encoder setNatureOfActivationText(String natureOfActivationText) {
        this.natureOfActivationText = natureOfActivationText;
        return this;
    }

    /**
     * @param alertMessageTimeRemaining the alertMessageTimeRemaining to set
     * @return 
     */
    public Scte18Encoder setAlertMessageTimeRemaining(String alertMessageTimeRemaining) {
        this.alertMessageTimeRemaining = alertMessageTimeRemaining;
        return this;
    }

    /**
     * @param eventStartTime the eventStartTime to set
     * @return 
     */
    public Scte18Encoder setEventStartTime(String eventStartTime) {
        this.eventStartTime = eventStartTime;
        return this;
    }

    /**
     * @param eventDuration the eventDuration to set
     * @return 
     */
    public Scte18Encoder setEventDuration(String eventDuration) {
        this.eventDuration = eventDuration;
        return this;
    }

    

    /**
     * @param alertPriority the alertPriority to set
     * @return 
     */
    public Scte18Encoder setAlertPriority(String alertPriority) {
        this.alertPriority = alertPriority;
        return this;
    }

    /**
     * @param detailsOobSourceId the detailsOobSourceId to set
     * @return 
     */
    public Scte18Encoder setDetailsOobSourceId(String detailsOobSourceId) {
        this.detailsOobSourceId = detailsOobSourceId;
        return this;
    }

    

    /**
     * @param detailsMajorChannelNumber the detailsMajorChannelNumber to set
     * @return 
     */
    public Scte18Encoder setDetailsMajorChannelNumber(String detailsMajorChannelNumber) {
        this.detailsMajorChannelNumber = detailsMajorChannelNumber;
        return this;
    }

    

    /**
     * @param detailsMinorChannelNumber the detailsMinorChannelNumber to set
     * @return 
     */
    public Scte18Encoder setDetailsMinorChannelNumber(String detailsMinorChannelNumber) {
        this.detailsMinorChannelNumber = detailsMinorChannelNumber;
        return this;
    }

    /**
     * @param audioOobSourceId the audioOobSourceId to set
     * @return 
     */
    public Scte18Encoder setAudioOobSourceId(String audioOobSourceId) {
        this.audioOobSourceId = audioOobSourceId;
        return this;
    }

    /**
     * @param alertTextLength the alertTextLength to set
     * @return 
     */
    public Scte18Encoder setAlertTextLength(String alertTextLength) {
        this.alertTextLength = alertTextLength;
        return this;
    }

    /**
     * @param alertText the alertText to set
     * @return 
     */
    public Scte18Encoder setAlertText(String alertText) {
        this.alertText = alertText;
        return this;
    }

    /**
     * @param locationCodeCount the locationCodeCount to set
     * @return 
     */
    public Scte18Encoder setLocationCodeCount(String locationCodeCount) {
        this.locationCodeCount = locationCodeCount;
        return this;
    }

    /**
     * @param stateCode the stateCode to set
     * @return 
     */
    public Scte18Encoder setStateCode(List<String> stateCode) {
        this.stateCode = stateCode;
        return this;
    }

    /**
     * @param countySubdivision the countySubdivision to set
     * @return 
     */
    public Scte18Encoder setCountySubdivision(List<String> countySubdivision) {
        this.countySubdivision = countySubdivision;
        return this;
    }

    

    /**
     * @param countyCode the countyCode to set
     * @return 
     */
    public Scte18Encoder setCountyCode(List<String> countyCode) {
        this.countyCode = countyCode;
        return this;
    }

    /**
     * @param exceptionCount the exceptionCount to set
     * @return 
     */
    public Scte18Encoder setExceptionCount(String exceptionCount) {
        this.exceptionCount = exceptionCount;
        return this;
    }

    /**
     * @param inBandReference the inBandReference to set
     * @return 
     */
    public Scte18Encoder setInBandReference(String inBandReference) {
        this.inBandReference = inBandReference;
        return this;
    }

    

    /**
     * @param exceptionMajorChannelNumber the exceptionMajorChannelNumber to set
     * @return 
     */
    public Scte18Encoder setExceptionMajorChannelNumber(List<String> exceptionMajorChannelNumber) {
        this.exceptionMajorChannelNumber = exceptionMajorChannelNumber;
        return this;
    }

   
    /**
     * @param exceptionMinorChannelNumber the exceptionMinorChannelNumber to set
     * @return 
     */
    public Scte18Encoder setExceptionMinorChannelNumber(List<String> exceptionMinorChannelNumber) {
        this.exceptionMinorChannelNumber = exceptionMinorChannelNumber;
        return this;
    }

    

    /**
     * @param exceptionOobSourceId the exceptionOobSourceId to set
     * @return 
     */
    public Scte18Encoder setExceptionOobSourceId(List<String> exceptionOobSourceId) {
        this.exceptionOobSourceId = exceptionOobSourceId;
        return this;
    }


    /**
     * @param descriptorsLength the descriptorsLength to set
     * @return 
     */
    public Scte18Encoder setDescriptorsLength(String descriptorsLength) {
        this.descriptorsLength = descriptorsLength;
        return this;
    }

    /**
     * @param descriptor the descriptor to set
     * @return 
     */
    public Scte18Encoder setDescriptor(String descriptor) {
        this.descriptor = descriptor;
        return this;
    }

    /**
     * @param crc_32 the crc_32 to set
     * @return 
     */
    public Scte18Encoder setCrc_32(String crc_32) {
        this.crc_32 = crc_32;
        return this;
    }

    /**
     * @return the inBandReference
     */
    public String getInBandReference() {
        return inBandReference;
    }
    
    
    public void printPacketSummary(){
        System.out.println(ConsoleColors.GREEN_BRIGHT+"tableID" +ConsoleColors.RESET+" = "+tableID.replace(" ",""));
        System.out.println(ConsoleColors.GREEN_BRIGHT+"sectionSyntaxIndicator" +ConsoleColors.RESET+" = "+sectionSyntaxIndicator.replace(" ",""));
        System.out.println(ConsoleColors.GREEN_BRIGHT+"zero" +ConsoleColors.RESET+" = "+zero.replace(" ",""));
        System.out.println(ConsoleColors.GREEN_BRIGHT+"reserved_" +ConsoleColors.RESET+" = "+reserved_.replace(" ",""));
        System.out.println(ConsoleColors.GREEN_BRIGHT+"sectionLength" +ConsoleColors.RESET+" = "+sectionLength.replace(" ","") + "                [Decimal: "+binToDec(sectionLength)+"]");
        System.out.println(ConsoleColors.GREEN_BRIGHT+"tableIdExtension" +ConsoleColors.RESET+" = "+tableIdExtension.replace(" ",""));
        System.out.println(ConsoleColors.GREEN_BRIGHT+"reserved_2" +ConsoleColors.RESET+" = "+reserved_2.replace(" ",""));
        System.out.println(ConsoleColors.GREEN_BRIGHT+"sequenceNumber" +ConsoleColors.RESET+" = "+sequenceNumber.replace(" ","")+ "                      [Decimal: "+binToDec(sequenceNumber)+"]");    
        System.out.println(ConsoleColors.GREEN_BRIGHT+"currentNextIndicator" +ConsoleColors.RESET+" = "+currentNextIndicator.replace(" ",""));
        System.out.println(ConsoleColors.GREEN_BRIGHT+"sectionNumber" +ConsoleColors.RESET+" = "+sectionNumber.replace(" ",""));
        System.out.println(ConsoleColors.GREEN_BRIGHT+"lastSectionNumber" +ConsoleColors.RESET+" = "+lastSectionNumber.replace(" ",""));
        System.out.println(ConsoleColors.GREEN_BRIGHT+"protocolVersion" +ConsoleColors.RESET+" = "+protocolVersion.replace(" ",""));
        System.out.println(ConsoleColors.GREEN_BRIGHT+"easEventId" +ConsoleColors.RESET+" = "+easEventId.replace(" ","")+ "               [Decimal: "+binToDec(easEventId)+"]"); 
        System.out.println(ConsoleColors.GREEN_BRIGHT+"easOriginatorCode" +ConsoleColors.RESET+" = "+easOriginatorCode.replace(" ",""));
        System.out.println(ConsoleColors.GREEN_BRIGHT+"easEventCodeLength" +ConsoleColors.RESET+" = "+easEventCodeLength.replace(" ",""));
        System.out.println(ConsoleColors.GREEN_BRIGHT+"easEventCode" +ConsoleColors.RESET+" = "+easEventCode.replace(" ",""));
        System.out.println(ConsoleColors.GREEN_BRIGHT+"natureOfActivationTextLength" +ConsoleColors.RESET+" = "+natureOfActivationTextLength.replace(" ",""));
        System.out.println(ConsoleColors.GREEN_BRIGHT+"natureOfActivationText" +ConsoleColors.RESET+" = "+natureOfActivationText.replace(" ",""));
        System.out.println(ConsoleColors.GREEN_BRIGHT+"alertMessageTimeRemaining" +ConsoleColors.RESET+" = "+alertMessageTimeRemaining.replace(" ","")+ "        [Decimal: "+binToDec(alertMessageTimeRemaining)+"]"); 
        System.out.println(ConsoleColors.GREEN_BRIGHT+"eventStartTime" +ConsoleColors.RESET+" = "+eventStartTime.replace(" ","")+ "            [Decimal: "+binToDec(eventStartTime)+" Start Time Delay: "+Scte18Builder.startDelay+" secs]");
        System.out.println(ConsoleColors.GREEN_BRIGHT+"eventDuration" +ConsoleColors.RESET+" = "+eventDuration.replace(" ","")+ "            [Decimal: "+binToDec(eventDuration)+"]");
        System.out.println(ConsoleColors.GREEN_BRIGHT+"reserved_3" +ConsoleColors.RESET+" = "+reserved_3.replace(" ",""));
        System.out.println(ConsoleColors.GREEN_BRIGHT+"alertPriority" +ConsoleColors.RESET+" = "+alertPriority.replace(" ","")+ "                        [Decimal: "+binToDec(alertPriority)+"]"); 
        System.out.println(ConsoleColors.GREEN_BRIGHT+"detailsOobSourceId" +ConsoleColors.RESET+" = "+detailsOobSourceId.replace(" ","")+ "       [Decimal: "+binToDec(detailsOobSourceId)+"]"); 
        System.out.println(ConsoleColors.GREEN_BRIGHT+"reserved_4" +ConsoleColors.RESET+" = "+reserved_4.replace(" ",""));
        System.out.println(ConsoleColors.GREEN_BRIGHT+"detailsMajorChannelNumber" +ConsoleColors.RESET+" = "+detailsMajorChannelNumber.replace(" ","")+ "      [Decimal: "+binToDec(detailsMajorChannelNumber)+"]"); 
        System.out.println(ConsoleColors.GREEN_BRIGHT+"reserved_5" +ConsoleColors.RESET+" = "+reserved_5.replace(" ",""));
        System.out.println(ConsoleColors.GREEN_BRIGHT+"detailsMinorChannelNumber" +ConsoleColors.RESET+" = "+detailsMinorChannelNumber.replace(" ","")+ "      [Decimal: "+binToDec(detailsMinorChannelNumber)+"]"); 
        System.out.println(ConsoleColors.GREEN_BRIGHT+"audioOobSourceId" +ConsoleColors.RESET+" = "+audioOobSourceId.replace(" ","")+ "         [Decimal: "+binToDec(audioOobSourceId)+"]"); 
        System.out.println(ConsoleColors.GREEN_BRIGHT+"alertTextLength" +ConsoleColors.RESET+" = "+alertTextLength.replace(" ","")+ "          [Decimal: "+binToDec(alertTextLength)+"]"); 
        System.out.println(ConsoleColors.GREEN_BRIGHT+"alertText" +ConsoleColors.RESET+" = "+alertText);
        System.out.println("\n"+ConsoleColors.GREEN_BRIGHT+"locationCodeCount" +ConsoleColors.RESET+" = "+locationCodeCount.replace(" ","")+ "                [Decimal: "+binToDec(locationCodeCount)+"]"); 
        
       // locationCodeCount;
        int count = 0;
        for(int i = 0; i < Integer.parseInt(locationCodeCount.replace(" ", ""), 2); i++){
            System.out.println("***********************Location["+i+"]***********************");
            System.out.println(ConsoleColors.GREEN_BRIGHT+"stateCode" +ConsoleColors.RESET+" = "+stateCode.get(i)+ "                        [Decimal: "+binToDec(stateCode.get(i))+"]");
            System.out.println(ConsoleColors.GREEN_BRIGHT+"countySubdivision" +ConsoleColors.RESET+" = "+countySubdivision.get(i)+ "                    [Decimal: "+binToDec(countySubdivision.get(i))+"]");
            System.out.println(ConsoleColors.GREEN_BRIGHT+"reserved_6" +ConsoleColors.RESET+" = "+reserved_6);
            System.out.println(ConsoleColors.GREEN_BRIGHT+"countyCode" +ConsoleColors.RESET+" = "+countyCode.get(i)+ "                     [Decimal: "+binToDec(countyCode.get(i))+"]");
            count++;
         }
        System.out.println("***************"+ConsoleColors.GREEN_BRIGHT+"End of Locations [Total: "+count+"]"+ConsoleColors.RESET+"***************");
        count = 0;
        System.out.println("\n"+ConsoleColors.GREEN_BRIGHT+"exceptionCount" +ConsoleColors.RESET+" = "+ exceptionCount.replace(" ","")+ "                   [Decimal: "+binToDec(exceptionCount)+"]");
        //System.out.println("Exception counT: "+exceptionCount);
        for (int i = 0; i < Integer.parseInt(exceptionCount.replace(" ", ""), 2); i++) {
            if (Integer.parseInt(getInBandReference(), 2) > 0) {
                System.out.println("*******************Exception Channel["+i+"]**********************");
                inBandReference = "1";
                System.out.println(ConsoleColors.GREEN_BRIGHT+"inBandReference" +ConsoleColors.RESET+" = "+inBandReference);
                System.out.println(ConsoleColors.GREEN_BRIGHT+"reserved_7" +ConsoleColors.RESET+" = "+reserved_7);
                System.out.println(ConsoleColors.GREEN_BRIGHT+"reserved_8" +ConsoleColors.RESET+" = "+reserved_8);
                System.out.println(ConsoleColors.GREEN_BRIGHT+"exceptionMajorChannelNumber" +ConsoleColors.RESET+" = "+exceptionMajorChannelNumber.get(i)+ "     [Decimal: "+binToDec(exceptionMajorChannelNumber.get(i))+"]");
                System.out.println(ConsoleColors.GREEN_BRIGHT+"reserved_9" +ConsoleColors.RESET+" = "+reserved_9);
                System.out.println(ConsoleColors.GREEN_BRIGHT+"exceptionMinorChannelNumber" +ConsoleColors.RESET+" = "+exceptionMinorChannelNumber.get(i)+ "     [Decimal: "+binToDec(exceptionMinorChannelNumber.get(i))+"]");
            } else {
                inBandReference = "0";
                System.out.println("*******************Exception Channel["+i+"]*******************");
                System.out.println(ConsoleColors.GREEN_BRIGHT+"inBandReference" +ConsoleColors.RESET+" = "+inBandReference);
                System.out.println(ConsoleColors.GREEN_BRIGHT+"reserved_7" +ConsoleColors.RESET+" = "+reserved_7);
                System.out.println(ConsoleColors.GREEN_BRIGHT+"reserved_10" +ConsoleColors.RESET+" = "+reserved_10);
                System.out.println(ConsoleColors.GREEN_BRIGHT+"exceptionOobSourceId" +ConsoleColors.RESET+" = "+exceptionOobSourceId.get(i)+ "     [Decimal: "+binToDec(exceptionOobSourceId.get(i))+"]");
            }
            count++;
        }
        System.out.println("*************"+ConsoleColors.GREEN_BRIGHT+"End of Exception Channels [Total: "+count+"]"+ConsoleColors.RESET+"*************");
        System.out.println("\n"+ConsoleColors.GREEN_BRIGHT+"reserved_11" +ConsoleColors.RESET+" = "+reserved_11);
        System.out.println(ConsoleColors.GREEN_BRIGHT+"descriptorsLength" +ConsoleColors.RESET+" = "+descriptorsLength);
        // descriptor is not added as the len is zero;
    }
    public static int binToDec(String binary){
        return Integer.parseInt(binary.replace(" ", ""), 2);
    }
}
