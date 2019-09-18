/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vecima.eassimulatorcli.scte18;

import com.vecima.eassimulatorcli.App;
import com.vecima.eassimulatorcli.color.ConsoleColors;
import com.vecima.eassimulatorcli.datetime.DateTime;
import com.vecima.eassimulatorcli.packets.PacketBuilder;
import com.vecima.eassimulatorcli.penderwood.Penderwood;
import com.vecima.eassimulatorcli.property.PropertyManager;
import static com.vecima.eassimulatorcli.property.PropertyManager.fileName;
import com.vecima.eassimulatorcli.util.Locations;
import com.vecima.eassimulatorcli.util.StringProcessor;
import static com.vecima.eassimulatorcli.util.StringProcessor.asciiToHex;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author zayeed
 */
public class Scte18Builder {

    private boolean exceptionChannelList;
    //private boolean mixExceptionList = false;
    private String easEventId;
    private String alertText;
    private String alertTextLength = "0000 0000 1011 1101";                     //16  178 bytes 1011 0010 ->178
    private String alertMessageTimeRemaining;
    private String eventStartTime = "0";
    private String eventDuration;
    private String priority;
    private String inBandRef;
    private String detailsMajorChannelNumber;
    private String detailsMinorChannelNumber;
    private final List<String> exceptionMajorChannelNumber = new ArrayList<>();
    private final List<String> exceptionMinorChannelNumber = new ArrayList<>();
    private String exceptionCount = "00000000";
    private String audioOobSourceId;
    private String detailsOobSourceId;
    private final List<String> exceptionOobSourceId = new ArrayList<>();
    //locations
    private String locationCodeCount = "00000001";
    private final List<String> stateCode = new ArrayList<>();
    private final List<String> countySubdivision = new ArrayList<>();
    private final List<String> countyCode = new ArrayList<>();
    private List<String> locations = new ArrayList<>();
    public static String startDelay;
    
    public Scte18Builder() {
    }

    public String buildHeader() throws ParseException {
        //initializeEthernetParameters();
        initializeParameters();
        Scte18Encoder en = new Scte18Encoder();

        en.setEasEventId(easEventId)
                .setAlertTextLength(alertTextLength)
                .setAlertText(alertText)
                .setAlertMessageTimeRemaining(alertMessageTimeRemaining)
                .setEventStartTime(eventStartTime)
                .setEventDuration(eventDuration)
                .setAlertPriority(priority)
                .setInBandReference(inBandRef)
                .setLocationCodeCount(locationCodeCount)
                .setStateCode(stateCode)
                .setCountySubdivision(countySubdivision)
                .setCountyCode(countyCode);

        if (en.getInBandReference().equals("1")) {
            en.setDetailsMajorChannelNumber(detailsMajorChannelNumber)
                    .setDetailsMinorChannelNumber(detailsMinorChannelNumber)
                    .setDetailsOobSourceId(detailsOobSourceId);
            if (exceptionChannelList) {
                en.setExceptionMajorChannelNumber(exceptionMajorChannelNumber)
                        .setExceptionMinorChannelNumber(exceptionMinorChannelNumber)
                        .setExceptionOobSourceId(exceptionOobSourceId)
                        .setExceptionCount(exceptionCount);
            }

        } else if (en.getInBandReference().equals("0")) {
            en.setAudioOobSourceId(audioOobSourceId)
                    .setDetailsOobSourceId(detailsOobSourceId);
            if (exceptionChannelList) {
                en.setExceptionOobSourceId(exceptionOobSourceId)
                        .setExceptionCount(exceptionCount);
            }
        }

        return en.scte18Encode();

    }

    public void initializeParameters() throws ParseException {
        Properties prop = new Properties();
        InputStream inputStream = null;
        List<String[]> inBandExpChannels = new ArrayList<>();
        List<String> oobChannels = new ArrayList<>();

        try {
            inputStream = new FileInputStream(fileName);
            //load the file
            prop.load(inputStream);

            if (prop.getProperty(PropertyManager.exceptionChannelList).equals("enabled")) {
                exceptionChannelList = true;
            } else {
                exceptionChannelList = false;
            }
            
            if (prop.getProperty(PropertyManager.outOfBandPID).equals("enabled")) {
                Penderwood.oobPid = true;
            }
            
            eventDuration = processIntegerInput(prop.getProperty(PropertyManager.eventDuration), Scte18HeaderDefinition.eventDurationSize);
            
            easEventId = processIntegerInput(prop.getProperty(PropertyManager.alertID), Scte18HeaderDefinition.easEventIdSize);
            App.alertID = Integer.parseInt(prop.getProperty(PropertyManager.alertID), 10);
            
            //locations
            this.locationCodeCount = StringProcessor.bitStuffer(Integer.toBinaryString(parseLocationInfo(prop.getProperty("locations"))), Scte18HeaderDefinition.locationCodeCountSize);
            String concatLocs="";
            for (String loc : locations) {
                for (int i = 0; i < Locations.getAREAS().length; i++) {
                    if (loc.equals(Locations.getAREAS()[i][0])) {
                        concatLocs += Locations.getAREAS()[i][1] + "; ";
                    }
                }
            }
            
            //System.out.println("Locations concatednated as: "+concatLocs);
            
            Scte18AlertCodes ac = new Scte18AlertCodes();
            ac.setReplaceAlertCode(prop.getProperty(PropertyManager.easAlertCode).replace(" ", ""))
                    .setReplaceLocations(concatLocs)
                    .setAlertMsg();

            alertText = processAlertText(ac.getAlertMsg());

            alertMessageTimeRemaining = processIntegerInput(prop.getProperty(PropertyManager.alertMessageTimeRemaining), Scte18HeaderDefinition.alertMessageTimeRemainingSize);
            startDelay = prop.getProperty(PropertyManager.eventStartTime);
            int event_time_sec = DateTime.caluculateEventStartTime() + Integer.parseInt(startDelay);
            eventStartTime = processIntegerInput(Integer.toString(event_time_sec), Scte18HeaderDefinition.eventStartTimeSize);
            
            priority = processIntegerInput(prop.getProperty(PropertyManager.alertPriority), Scte18HeaderDefinition.alertPrioritySize);

            if (prop.getProperty(PropertyManager.isInBandEnabled).equals("true")) {
                inBandRef = processIntegerInput("1", Scte18HeaderDefinition.inBandReferenceSize);
            } else if (prop.getProperty(PropertyManager.isInBandEnabled).equals("false")) {
                inBandRef = processIntegerInput("0", Scte18HeaderDefinition.inBandReferenceSize);
            }

            detailsMajorChannelNumber = processIntegerInput(prop.getProperty(PropertyManager.detailsMajorChannelNumber), Scte18HeaderDefinition.detailsMajorChannelNumberSize);
            detailsMinorChannelNumber = processIntegerInput(prop.getProperty(PropertyManager.detailsMinorChannelNumber), Scte18HeaderDefinition.detailsMinorChannelNumberSize);

            inBandExpChannels = exceptionChannelListParserInBand(prop.getProperty(PropertyManager.inBandExceptionList));

            for (String[] channels : inBandExpChannels) {
                for (int i = 0; i < channels.length - 1; i++) {
                    exceptionMajorChannelNumber.add(processIntegerInput(channels[i], Scte18HeaderDefinition.exceptionMajorChannelNumberSize));
                    exceptionMinorChannelNumber.add(processIntegerInput(channels[i + 1], Scte18HeaderDefinition.exceptionMinorChannelNumberSize));
                }
            }

            oobChannels = exceptionChannelListParserOob(prop.getProperty(PropertyManager.oobExceptionList));

            for (String channels : oobChannels) {
                exceptionOobSourceId.add(processIntegerInput(channels, Scte18HeaderDefinition.exceptionOobSourceIdSize));

            }

            if (exceptionMajorChannelNumber.size() == exceptionMinorChannelNumber.size() && inBandRef.equals("1")) {
                exceptionCount = processIntegerInput(exceptionMajorChannelNumber.size() + "", Scte18HeaderDefinition.exceptionCountSize);
                //System.out.println("inBandExceptionCount: " + exceptionCount);
            } else if (exceptionMajorChannelNumber.size() != exceptionMinorChannelNumber.size() && inBandRef.equals("1")) {
                System.out.println(ConsoleColors.RED + "Fatal error: Invalid Major and Minor Channel number combination. Check your config.properties file!");
                System.exit(0);
            } else if (inBandRef.equals("0")) {
                exceptionCount = processIntegerInput(Integer.toString(exceptionOobSourceId.size()), Scte18HeaderDefinition.exceptionCountSize);
                //System.out.println("oobExceptionCount: "+exceptionCount+"   [Decimal: "+Integer.parseInt(exceptionCount, 2)+"]");
            }
            audioOobSourceId = processIntegerInput(prop.getProperty(PropertyManager.detailsAudioOobId), Scte18HeaderDefinition.audioOobSourceIdSize);
            detailsOobSourceId = processIntegerInput(prop.getProperty(PropertyManager.detailsVideoOobId), Scte18HeaderDefinition.detailsOobSourceIdSize);

            PacketBuilder.srcIP = prop.getProperty(PropertyManager.srcIP);
            PacketBuilder.srcMacAddress = prop.getProperty(PropertyManager.srcMacAddress);
            PacketBuilder.dstIP = prop.getProperty(PropertyManager.dstIP);
            
            PacketBuilder.inputSrcPort = Integer.parseInt(prop.getProperty(PropertyManager.srcPort), 10);
            PacketBuilder.inputDstPort = Integer.parseInt(prop.getProperty(PropertyManager.dstPort), 10);
            PacketBuilder.inputTTL = Integer.parseInt(prop.getProperty(PropertyManager.multicastTtl), 10);

            App.alertMessageDuplicateCount = Integer.parseInt(prop.getProperty(PropertyManager.alertMessageDuplicateCount), 10);
            
            if(!App.tunrOffAllLog)
                System.out.println("\nGenerated alertText: " + ConsoleColors.GREEN_BRIGHT+ac.getAlertMsg()+ConsoleColors.RESET);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String processIntegerInput(String property, int size) {
        return StringProcessor.bitStuffer(Integer.toBinaryString(Integer.parseInt(property)), size);
    }

    public String processAlertText(String alertText) {
        String temp;
        temp = "01656E67010000AA" + asciiToHex(alertText, alertText.length()); // Integer.parseInt(this.alertTextLength.replace(" ", ""), 2)
        this.alertTextLength = StringProcessor.bitStuffer(Integer.toBinaryString(alertText.length() + 8), Scte18HeaderDefinition.alertTextLengthSize);
        return StringProcessor.hexToBin(temp);
    }

    public static List<String[]> exceptionChannelListParserInBand(String chList) {
        List<String> temp = new ArrayList<>();
        List<String[]> inBandChannels = new ArrayList<>();
        for (String ch : chList.split(" ")) {
            String[] chVal = ch.split("-");
            for (String x: chVal){
                if(Integer.parseInt(x, 10) >=0 && Integer.parseInt(x, 10) <= 1023)      //validating
                    continue;
                else{
                    System.out.println("\n"+ConsoleColors.RED+"Fatal Error: inBandExceptionList channel number "+x+" is not in desired range of 0 to 1023. Exiting..."+ConsoleColors.RESET);
                    System.exit(0);
                }
            }
            temp.add(ch);
        }
        for (int i = 0; i < temp.size(); i++) {
            inBandChannels.add(temp.get(i).split("-"));
        }
        
        return inBandChannels;

    }

    public List<String> exceptionChannelListParserOob(String chList) {
        List<String> ooBChannels = new ArrayList<>();
        for (String ch : chList.split(" ")) {
            if(Integer.parseInt(ch, 10) >= 0 && Integer.parseInt(ch, 10) <= 65535)    //validating
                ooBChannels.add(ch);
            else{
                System.out.println("\n"+ConsoleColors.RED+"Fatal Error: oobExceptionList channel number "+ch+" is not in desired range of 0 to 65535. Exiting..."+ConsoleColors.RESET);
                System.exit(0);
            }
        }
        return ooBChannels;
    }

    public int parseLocationInfo(String locations) {
        List<String> locationList = new ArrayList<>();
        locationList.addAll(Arrays.asList(locations.split(" ")));
        this.locations = locationList;

        String[] stateCode = new String[locationList.size()];
        String[] countySubdivision = new String[locationList.size()];
        String[] countyCode = new String[locationList.size()];

        for (int i = 0; i < locationList.size(); i++) {
            stateCode[i] = StringProcessor.bitStuffer(Integer.toBinaryString(Integer.parseInt(locationList.get(i).substring(0, 3))), Scte18HeaderDefinition.stateCodeSize);
            this.stateCode.add(stateCode[i]);

            countySubdivision[i] = StringProcessor.bitStuffer(Integer.toBinaryString(Integer.parseInt(locationList.get(i).substring(3, 4))), Scte18HeaderDefinition.countySubdivisionSize);
            this.countySubdivision.add(countySubdivision[i]);

            countyCode[i] = StringProcessor.bitStuffer(Integer.toBinaryString(Integer.parseInt(locationList.get(i).substring(4, 6))), Scte18HeaderDefinition.countyCodeSize);
            this.countyCode.add(countyCode[i]);
        }
        return locationList.size();
    }
}
