/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vecima.eassimulatorcli.property;

/**
 *
 * @author zayeed
 */
//import com.vecima.eassimulatorcli.scte18.Scte18HeaderBuilder;
import com.vecima.eassimulatorcli.color.ConsoleColors;
import com.vecima.eassimulatorcli.util.Validator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.pcap4j.core.PcapNativeException;

public class PropertyManager implements Serializable {

    public static String fileName = "config.properties";

    public static final String alertID = "alertID";
    public static final String alertText = "alertText";
    public static final String srcIP = "srcIP";
    public static final String srcPort = "srcPort";
    public static final String srcMacAddress = "srcMacAddress";
    public static final String dstIP = "dstIP";
    public static final String dstPort = "dstPort";
    public static final String multicastTtl = "multicastTtl";
    public static final String alertMessageDuplicateCount = "alertMessageDuplicateCount";
    public static final String additionalStartDelay = "additionalStartDelay";
    public static final String alertMessageTimeRemaining = "alertMessageTimeRemaining";
    public static final String eventStartTime = "eventStartTime";
    public static final String eventDuration = "eventDuration";
    public static final String alertPriority = "alertPriority";
    public static final String isInBandEnabled = "isInBandEnabled";
    public static final String detailsMajorChannelNumber = "detailsMajorChannelNumber";
    public static final String detailsMinorChannelNumber = "detailsMinorChannelNumber";
    public static final String detailsVideoOobId = "detailsVideoOobId";
    public static final String detailsAudioOobId = "detailsAudioOobId";
    public static final String exceptionChannelList = "exceptionChannelList";
    public static final String inBandExceptionList = "inBandExceptionList";
    public static final String oobExceptionList = "oobExceptionList";
    public static final String easAlertCode = "easAlertCode";
    public static final String locations = "locations";
    public static final String outOfBandPID = "outOfBandPID";
    
    public PropertyManager(String fileName) {
        PropertyManager.fileName = fileName;
    }
    
    public PropertyManager(){}
    
    Properties prop = new Properties();
    OutputStream content = null;

    public void writeProperties() {
        try {
            content = new FileOutputStream("config_sample.properties");
            // set the properties value
            prop.setProperty(this.alertID, "10390");
            //prop.setProperty(this.alertText, "Vecima Networks HAS ISSUED A REQUIRED WEEKLY TEST FOR THE FOLLOWING COUNTIES/AREAS: Pinal, AZ; AT 12:13 PM ON SEP 17, 2018 EFFECTIVE UNTIL 12:28 PM. MESSAGE FROM Vecima. ");
            prop.setProperty(this.srcIP, "192.168.216.56");
            prop.setProperty(this.srcPort, "32769");
            prop.setProperty(this.srcMacAddress, "00:13:3b:10:31:5b");
            prop.setProperty(this.dstIP, "239.5.5.53");
            prop.setProperty(this.dstPort, "10000");
            prop.setProperty(this.multicastTtl, "20");
            prop.setProperty(this.alertMessageDuplicateCount, "2");
            prop.setProperty(this.eventDuration, "15");
            prop.setProperty(this.additionalStartDelay, "0");
            prop.setProperty(this.alertMessageTimeRemaining, "120");
            prop.setProperty(this.eventStartTime, "0");
            prop.setProperty(this.alertPriority, "15"); //highest(15)
            prop.setProperty(this.isInBandEnabled, "true");
            prop.setProperty(this.detailsMajorChannelNumber, "0");
            prop.setProperty(this.detailsMinorChannelNumber, "0");
            prop.setProperty(this.detailsVideoOobId, "7905");
            prop.setProperty(this.detailsAudioOobId, "7905");
            prop.setProperty(this.exceptionChannelList, "enabled");
            prop.setProperty(this.inBandExceptionList, "128-1 130-2 133-1");
            prop.setProperty(this.oobExceptionList, "7903 7723 1290 1299");
            prop.setProperty(this.easAlertCode, "RWT");
            prop.setProperty(this.locations, "004021 004013 000000 006000 006007 006003");
            prop.setProperty(this.outOfBandPID, "disabled");
            
            // save properties 
            prop.store(content, null);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (content != null) {
                try {
                    content.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void readProperties() {
        Properties prop1 = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream(fileName);
            //load the file
            prop1.load(input);
            System.out.println("Read Property-alertText: " + prop1.getProperty("alertText"));

        } catch (IOException ex) {
            System.out.println("Error Reading Property file: Some fields might be missing or incorrect!");

        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }

    }

    public static boolean[] validateProperties() throws PcapNativeException {

        Properties prop = new Properties();
        InputStream input = null;
        boolean[] result = new boolean[26];
        File file = new File(fileName);
        if(!file.exists()){
            System.out.println(ConsoleColors.RED+"Error: "+fileName+": No such file, Execution stopped!"+ConsoleColors.RESET);
            System.exit(0);
        }
            
        try {
            input = new FileInputStream(fileName);
            //load the file
            prop.load(input);
            result[0] = Validator.isValidIPv4(prop.getProperty(srcIP));
            result[1] = Validator.isValidPort(prop.getProperty(srcPort));
            result[2] = Validator.isValidIPv4(prop.getProperty(dstIP));
            result[3] = Validator.isValidPort(prop.getProperty(dstPort));
            result[4] = Validator.isDigitsOnly(prop.getProperty(alertID));
            result[5] = Validator.isDigitsOnly(prop.getProperty(alertMessageDuplicateCount));
            result[6] = Validator.isDigitsOnly(prop.getProperty(additionalStartDelay));
            result[7] = Validator.isDigitsOnly(prop.getProperty(alertMessageTimeRemaining)) && Validator.isInRange(prop.getProperty(alertMessageTimeRemaining), 0, 255);
            result[8] = Validator.isDigitsOnly(prop.getProperty(alertPriority)) && Validator.isInRange(prop.getProperty(alertPriority), 0, 15);
            result[9] = Validator.isDigitsOnly(prop.getProperty(detailsMajorChannelNumber)) && Validator.isInRange(prop.getProperty(detailsMajorChannelNumber), 0, 1023);
            result[10] = Validator.isDigitsOnly(prop.getProperty(detailsMinorChannelNumber)) && Validator.isInRange(prop.getProperty(detailsMinorChannelNumber), 0, 1023);
            result[11] = Validator.isDigitsOnly(prop.getProperty(detailsAudioOobId)) && Validator.isInRange(prop.getProperty(detailsAudioOobId), 0, 65535);
            result[12] = Validator.isDigitsOnly(prop.getProperty(detailsVideoOobId)) && Validator.isInRange(prop.getProperty(detailsVideoOobId), 0, 65535);
            result[13] = Validator.isInterfaceSelectable(prop.getProperty(srcIP));
            result[14] = Validator.isValidAlertCode(prop.getProperty(easAlertCode).replace(" ", ""));
            result[15] = Validator.isValidAreaCode(prop.getProperty(locations));
            result[16] = Validator.isDigitsOnly(prop.getProperty(inBandExceptionList).replace(" ", "").replace("-",""));
            result[17] = Validator.isDigitsOnly(prop.getProperty(oobExceptionList).replace(" ", "")); 
            result[18] = Validator.isBoolean(prop.getProperty(isInBandEnabled));
            result[19] = Validator.isValid(prop.getProperty(exceptionChannelList));
            result[20] = Validator.isValidMajorMinorCombination(prop.getProperty(inBandExceptionList));
            result[21] = Validator.isValid(prop.getProperty(outOfBandPID));
            result[22] = Validator.isDigitsOnly(prop.getProperty(multicastTtl)) && Validator.isInRange(prop.getProperty(multicastTtl), 0, 200);
            result[23] = Validator.isDigitsOnly(prop.getProperty(eventDuration)) && Validator.isInRange(prop.getProperty(eventDuration), 0, 65535);
            result[24] = Validator.isDigitsOnly(prop.getProperty(eventStartTime));
            result[25] = Validator.isValidMac(prop.getProperty(srcMacAddress));

        } catch (IOException ex) {
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }

        return result;
    }
/*
    public static List<String[]> exceptionChannelListParser(boolean inBand, String chList) {
        List<String> temp = new ArrayList<>();
        List<String[]> inBandChannels = new ArrayList<>();
        List<String[]> ooBChannels = new ArrayList<>();

        if (inBand) {
            temp.addAll(Arrays.asList(chList.split(" ")));

            for (int i = 0; i < temp.size(); i++) {
                inBandChannels.add(temp.get(i).split("-"));
            }
            return inBandChannels;
        } else {
            temp.addAll(Arrays.asList(chList.split(" ")));
            for (int i = 0; i < temp.size(); i++) {
                ooBChannels.add(temp.get(i).split(""));
            }
            return ooBChannels;
        }
    }
*/
}
