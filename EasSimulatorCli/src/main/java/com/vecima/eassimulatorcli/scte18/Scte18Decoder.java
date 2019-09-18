/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vecima.eassimulatorcli.scte18;

import com.vecima.eassimulatorcli.color.ConsoleColors;
import com.vecima.eassimulatorcli.util.StringProcessor;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.Packet;

/**
 *
 * @author zayeed
 */
public class Scte18Decoder {
            
    public static String pcapFile = "/home/zayeed/eas_testing_tool/eas_oob_monroe_36expch_041018.pcap";
/*  private final int IP_HEADER = 20;
    private final int UDP_HEADER = 8;
    private final int ETHERNET_HEADER = 14;
    private final int MPEG_TS = 188;
    private final int MPEG_HEADER = 4;
    private final int EAS_PAYLOAD = 184;
    private final int PACKET_SIZE = 230;
*/
    private static List<String> getpcapPacketList(String pcapFile) throws PcapNativeException, NotOpenException {
        File file = new File(pcapFile);
        if(!file.exists()){
            System.out.println(ConsoleColors.RED+"Error: "+pcapFile+": No such file, Execution stopped!"+ConsoleColors.RESET);
            System.exit(0);
        }
        PcapHandle handler = Pcaps.openOffline(pcapFile);
        Packet packet = null;
        List<String> packetList = new ArrayList<>();
        while ((packet = handler.getNextPacket()) != null) {
            packetList.add(javax.xml.bind.DatatypeConverter.printHexBinary(packet.getRawData()));
        }
        return packetList;
    }

    public static void packetAnalyser() throws PcapNativeException, NotOpenException{
        List<String> hexDump = getpcapPacketList(pcapFile);
        List<String> packets = new ArrayList<>();
        String easHeader="";
        String PID = "";
        //find how many different packet is there scte18_encode_java eas_oob_with_exception_java.pcap eas_oob_exception_200918 eas_inband_021018 /home/zayeed/eas_testing_tool/eas_oob_exception_200918.pcap
        //int uniquePackets = (int) hexDump.stream().distinct().count();
        if(hexDump.get(0).contains("475FFB")){
            PID = "0x1FFB";
            packets.add(hexDump.get(0));
        }
        else if(hexDump.get(0).contains("475FFC")){
            PID = "0x1FFC";
            packets.add(hexDump.get(0));
        }
        else{
            System.out.println(ConsoleColors.RED+"Error: Not a valid SCTE-18 standard pcap file. Exiting..."+ConsoleColors.RESET);
            System.exit(0);
        }
        
        
        int x = 1;
        while(!hexDump.get(x).contains("475FFB") && !hexDump.get(x).contains("475FFC")){
            if(hexDump.get(x).contains("471FFB") || hexDump.get(x).contains("471FFC"))
                packets.add(hexDump.get(x));
            x++;
            if(x > hexDump.size())
                break;
        }
        System.out.println("\n"+ConsoleColors.GREEN+"***********************************Parsing Packet***********************************"+ConsoleColors.RESET);
        System.out.println(ConsoleColors.BLUE_BOLD+"Pcap File: "+ConsoleColors.YELLOW+pcapFile+ConsoleColors.RESET);
        for (int i = 0; i < packets.size(); i++) {
            System.out.println(ConsoleColors.GREEN_BRIGHT+"Value of element " + i + ": "+ConsoleColors.RESET+ packets.get(i));
        }
        System.out.println(ConsoleColors.GREEN_BRIGHT+"Each Packet Size(): "+ConsoleColors.RESET+packets.get(0).length()/2 +" bytes");
        
        int easLen;
        
        switch (packets.get(0).length()) {
            case 244 * 2:
                for (int i = 0; i < packets.size(); i++) {
                    easLen = easHeader.length();
                    easHeader += (i == 0) ? packets.get(i).substring(94, packets.get(i).length() - 28) : packets.get(i).substring(92, packets.get(i).length() - 28);
                    System.out.println(ConsoleColors.GREEN_BRIGHT+"EasPayload[" + i + "]: " +ConsoleColors.RESET + ((i == 0)?easHeader:easHeader.substring(easLen)));
                }   break;
            case 230 * 2:
                for (int i = 0; i < packets.size(); i++) {
                    easLen = easHeader.length();
                    easHeader += (i == 0) ? packets.get(i).substring(94) : packets.get(i).substring(92);
                    System.out.println(ConsoleColors.GREEN_BRIGHT+"EasPayload[" + i + "]: " +ConsoleColors.RESET+((i == 0)?easHeader:easHeader.substring(easLen)));
                }   break;
            default:
                System.out.println(ConsoleColors.RED+"Pcap Packet Length Incorrect! Not able to parse the MPEG Packet. Exiting!" +ConsoleColors.RESET);
                System.exit(0);
        }
        
        int index = 0;    
        
        //find table_id
        int easTotalLen = easHeader.length()/2;
        System.out.println(ConsoleColors.GREEN_BRIGHT+"EAS Payload Length: "+ConsoleColors.RESET+easTotalLen+" bytes");
        //System.out.println(ConsoleColors.GREEN_BRIGHT+"EAS Header: "+ConsoleColors.RESET+easHeader);
        //System.out.println(ConsoleColors.GREEN_BRIGHT+"Eas Header Binary:"+ConsoleColors.RESET+StringProcessor.hexToBin(easHeader));
        
        easHeader = com.vecima.eassimulatorcli.util.StringProcessor.hexToBin(easHeader);
        Scte18HeaderDefinition shd = new Scte18HeaderDefinition();
        System.out.println("");
        System.out.println(ConsoleColors.YELLOW+"*********************SCTE18 cable_emergency_alert()*********************"+ConsoleColors.RESET);
        System.out.println(ConsoleColors.GREEN_BRIGHT+"table_ID" +ConsoleColors.RESET+" = "+easHeader.substring(index, index+shd.tableIDSize)+"                       [Decimal: "+(binToDec(easHeader.substring(index, index+shd.tableIDSize))+"]"));
        index += shd.tableIDSize;
        
        System.out.println(ConsoleColors.GREEN_BRIGHT+"sectionSyntaxIndicator" +ConsoleColors.RESET+" = "+easHeader.substring(index, index + shd.sectionSyntaxIndicatorSize));
        index += shd.sectionSyntaxIndicatorSize;
        
        System.out.println(ConsoleColors.GREEN_BRIGHT+"zero" +ConsoleColors.RESET+" = "+easHeader.substring(index, index + shd.zeroSize));
        index += shd.zeroSize;
        
        System.out.println(ConsoleColors.GREEN_BRIGHT+"reserved_" +ConsoleColors.RESET+" = "+easHeader.substring(index, index + shd.reserved_Size));
        index += shd.reserved_Size;
        
        System.out.println(ConsoleColors.GREEN_BRIGHT+"sectionLength" +ConsoleColors.RESET+" = "+easHeader.substring(index, index + shd.sectionLengthSize)+"              [Decimal: "+(binToDec(easHeader.substring(index, index+shd.sectionLengthSize))+"]"));
        int sectionLength = binToDec(easHeader.substring(index, index+shd.sectionLengthSize));
        index += shd.sectionLengthSize;
        
        System.out.println(ConsoleColors.GREEN_BRIGHT+"tableIdExtension" +ConsoleColors.RESET+" = "+easHeader.substring(index, index + shd.tableIdExtensionSize));
        index += shd.tableIdExtensionSize;
        
        System.out.println(ConsoleColors.GREEN_BRIGHT+"reserved_2" +ConsoleColors.RESET+" = "+easHeader.substring(index, index + shd.reserved_2Size));
        index += shd.reserved_2Size;
        
        System.out.println(ConsoleColors.GREEN_BRIGHT+"sequenceNumber" +ConsoleColors.RESET+" = "+easHeader.substring(index, index + shd.sequenceNumberSize)+"                    [Decimal: "+(binToDec(easHeader.substring(index, index+shd.sequenceNumberSize))+"]"));
        index += shd.sequenceNumberSize;
        
        System.out.println(ConsoleColors.GREEN_BRIGHT+"currentNextIndicator" +ConsoleColors.RESET+" = "+easHeader.substring(index, index + shd.currentNextIndicatorSize));
        index += shd.currentNextIndicatorSize;
        
        System.out.println(ConsoleColors.GREEN_BRIGHT+"sectionNumber" +ConsoleColors.RESET+" = "+easHeader.substring(index, index + shd.sectionNumberSize));
        index += shd.sectionNumberSize;
        
        System.out.println(ConsoleColors.GREEN_BRIGHT+"lastSectionNumber" +ConsoleColors.RESET+" = "+easHeader.substring(index, index + shd.lastSectionNumberSize));
        index += shd.lastSectionNumberSize;
        
        System.out.println(ConsoleColors.GREEN_BRIGHT+"protocolVersion" +ConsoleColors.RESET+" = "+easHeader.substring(index, index + shd.protocolVersionSize));
        index += shd.protocolVersionSize;
        
        System.out.println(ConsoleColors.GREEN_BRIGHT+"easEventId" +ConsoleColors.RESET+" = "+easHeader.substring(index, index + shd.easEventIdSize)+"             [Decimal: "+(binToDec(easHeader.substring(index, index+shd.easEventIdSize))+"]"));
        index += shd.easEventIdSize;
        
        System.out.println(ConsoleColors.GREEN_BRIGHT+"easOriginatorCode" +ConsoleColors.RESET+" = "+easHeader.substring(index, index + shd.easOriginatorCodeSize));
        index += shd.easOriginatorCodeSize;
        
        System.out.println(ConsoleColors.GREEN_BRIGHT+"easEventCodeLength" +ConsoleColors.RESET+" = "+easHeader.substring(index, index + shd.easEventCodeLengthSize));
        //index += shd.easEventCodeLengthSize;
        
        int varLen = 0;
        
        varLen = Integer.parseInt((easHeader.substring(index, index + shd.easEventCodeLengthSize)), 2);
        index += shd.easEventCodeLengthSize;
        
        //System.out.println("bytesNext: "+varLen);
        System.out.println(ConsoleColors.GREEN_BRIGHT+"easEventCode" +ConsoleColors.RESET+" = "+easHeader.substring(index, index+varLen*8));
        index += varLen*8;
        
        System.out.println(ConsoleColors.GREEN_BRIGHT+"natureOfActivationTextLength" +ConsoleColors.RESET+" = "+easHeader.substring(index, index+shd.natureOfActivationTextLengthSize));
        varLen = Integer.parseInt((easHeader.substring(index, index + shd.natureOfActivationTextLengthSize)), 2);
        index += shd.natureOfActivationTextLengthSize;
        
        System.out.println(ConsoleColors.GREEN_BRIGHT+"natureOfActivationText" +ConsoleColors.RESET+" = "+easHeader.substring(index, index+varLen*8));
        index += varLen*8;
        
        System.out.println(ConsoleColors.GREEN_BRIGHT+"alertMessageTimeRemaining" +ConsoleColors.RESET+" = "+easHeader.substring(index, index+shd.alertMessageTimeRemainingSize)+"      [Decimal: "+(binToDec(easHeader.substring(index, index+shd.alertMessageTimeRemainingSize))+"]"));
        index += shd.alertMessageTimeRemainingSize;
        
        System.out.println(ConsoleColors.GREEN_BRIGHT+"eventStartTime" +ConsoleColors.RESET+" = "+easHeader.substring(index, index+shd.eventStartTimeSize));
        index += shd.eventStartTimeSize;
        
        System.out.println(ConsoleColors.GREEN_BRIGHT+"eventDuration" +ConsoleColors.RESET+" = "+easHeader.substring(index, index+shd.eventDurationSize));
        index += shd.eventDurationSize;
        
        System.out.println(ConsoleColors.GREEN_BRIGHT+"reserved_3" +ConsoleColors.RESET+" = "+easHeader.substring(index, index+shd.reserved_3Size));
        index += shd.reserved_3Size;
        
        System.out.println(ConsoleColors.GREEN_BRIGHT+"alertPriority" +ConsoleColors.RESET+" = "+easHeader.substring(index, index+shd.alertPrioritySize)+"                      [Decimal: "+(binToDec(easHeader.substring(index, index+shd.alertPrioritySize))+"]"));
        index += shd.alertPrioritySize;
        
        System.out.println(ConsoleColors.GREEN_BRIGHT+"detailsOobSourceId" +ConsoleColors.RESET+" = "+easHeader.substring(index, index+shd.detailsOobSourceIdSize)+"     [Decimal: "+(binToDec(easHeader.substring(index, index+shd.detailsOobSourceIdSize))+"]"));
        index += shd.detailsOobSourceIdSize;
        
        System.out.println(ConsoleColors.GREEN_BRIGHT+"reserved_4" +ConsoleColors.RESET+" = "+easHeader.substring(index, index+shd.reserved_4Size));
        index += shd.reserved_4Size;
        
        System.out.println(ConsoleColors.GREEN_BRIGHT+"detailsMajorChannelNumber" +ConsoleColors.RESET+" = "+easHeader.substring(index, index+shd.detailsMajorChannelNumberSize)+"    [Decimal: "+(binToDec(easHeader.substring(index, index+shd.detailsMajorChannelNumberSize))+"]"));
        index += shd.detailsMajorChannelNumberSize;
        
        System.out.println(ConsoleColors.GREEN_BRIGHT+"reserved_5" +ConsoleColors.RESET+" = "+easHeader.substring(index, index+shd.reserved_5Size));
        index += shd.reserved_5Size;
        
        System.out.println(ConsoleColors.GREEN_BRIGHT+"detailsMinorChannelNumber" +ConsoleColors.RESET+" = "+easHeader.substring(index, index+shd.detailsMinorChannelNumberSize)+"    [Decimal: "+(binToDec(easHeader.substring(index, index+shd.detailsMinorChannelNumberSize))+"]"));
        index += shd.detailsMinorChannelNumberSize;
        
        System.out.println(ConsoleColors.GREEN_BRIGHT+"audioOobSourceId" +ConsoleColors.RESET+" = "+easHeader.substring(index, index+shd.audioOobSourceIdSize)+"       [Decimal: "+(binToDec(easHeader.substring(index, index+shd.audioOobSourceIdSize))+"]"));
        index += shd.audioOobSourceIdSize;
        
        System.out.println(ConsoleColors.GREEN_BRIGHT+"alertTextLength" +ConsoleColors.RESET+" = "+easHeader.substring(index, index+shd.alertTextLengthSize)+"        [Decimal: "+(binToDec(easHeader.substring(index, index+shd.alertTextLengthSize))+"]"));
        
        varLen = Integer.parseInt((easHeader.substring(index, index + shd.alertTextLengthSize)), 2);
        index += shd.alertTextLengthSize;
        
        System.out.println(ConsoleColors.GREEN_BRIGHT+"alertText" +ConsoleColors.RESET+" = "+easHeader.substring(index, index+varLen*8));
        System.out.println(ConsoleColors.GREEN_BRIGHT+"alertText ASCII:" +ConsoleColors.RESET+StringProcessor.hexToAscii(StringProcessor.binToHex(easHeader.substring(index, index+varLen*8).substring(64))));
        index += varLen*8;
        
        System.out.println("\n"+ConsoleColors.GREEN_BRIGHT+"locationCodeCount" +ConsoleColors.RESET+" = "+easHeader.substring(index, index+shd.locationCodeCountSize)+"              [Decimal: "+(binToDec(easHeader.substring(index, index+shd.locationCodeCountSize))+"]"));
        int iterate = Integer.parseInt((easHeader.substring(index, index + shd.locationCodeCountSize)), 2);
        index += shd.locationCodeCountSize;
       
        for(int i = 0; i < iterate; i++){
            System.out.println("***************************Location info["+i+"]***************************");
            System.out.println(ConsoleColors.GREEN_BRIGHT+"stateCode" +ConsoleColors.RESET+" = "+easHeader.substring(index, index+shd.stateCodeSize)+"                      [Decimal: "+(binToDec(easHeader.substring(index, index+shd.stateCodeSize))+"]"));
            index += shd.stateCodeSize;
            System.out.println(ConsoleColors.GREEN_BRIGHT+"countySubdivision" +ConsoleColors.RESET+" = "+easHeader.substring(index, index+shd.countySubdivisionSize)+"                  [Decimal: "+(binToDec(easHeader.substring(index, index+shd.countySubdivisionSize))+"]"));
            index += shd.countySubdivisionSize;
            System.out.println(ConsoleColors.GREEN_BRIGHT+"reserved_6" +ConsoleColors.RESET+" = "+easHeader.substring(index, index+shd.reserved_6Size));
            index += shd.reserved_6Size;
            System.out.println(ConsoleColors.GREEN_BRIGHT+"countyCode" +ConsoleColors.RESET+" = "+easHeader.substring(index, index+shd.countyCodeSize)+"                   [Decimal: "+(binToDec(easHeader.substring(index, index+shd.countyCodeSize))+"]"));
            index += shd.countyCodeSize;
            //System.out.println("***********************************************************************");
        }
        System.out.println("***********************"+ConsoleColors.GREEN_BRIGHT+"End of Locations [Total: "+iterate+"]"+ConsoleColors.RESET+"***********************");
        
        System.out.println("\n"+ConsoleColors.GREEN_BRIGHT+"exceptionCount" +ConsoleColors.RESET+" = "+easHeader.substring(index, index+shd.exceptionCountSize)+"                 [Decimal: "+(binToDec(easHeader.substring(index, index+shd.exceptionCountSize))+"]"));
        iterate = Integer.parseInt((easHeader.substring(index, index + shd.exceptionCountSize)), 2); 
        index += shd.exceptionCountSize;
        
        int inBandRef;
        for(int i = 0; i < iterate; i++){
            System.out.println("***************************Exception Channel["+i+"]***************************");
            System.out.println(ConsoleColors.GREEN_BRIGHT+"inBandReference" +ConsoleColors.RESET+" = "+easHeader.substring(index, index+shd.inBandReferenceSize));
            inBandRef = Integer.parseInt((easHeader.substring(index, index + shd.inBandReferenceSize)), 2);
            index += shd.inBandReferenceSize;
            
            System.out.println(ConsoleColors.GREEN_BRIGHT+"reserved_7" +ConsoleColors.RESET+" = "+easHeader.substring(index, index+shd.reserved_7Size));
            index += shd.reserved_7Size;
            
            if(inBandRef > 0){
                System.out.println(ConsoleColors.GREEN_BRIGHT+"reserved_8" +ConsoleColors.RESET+" = "+easHeader.substring(index, index+shd.reserved_8Size));
                index += shd.reserved_8Size;
                
                System.out.println(ConsoleColors.GREEN_BRIGHT+"exceptionMajorChannelNumber" +ConsoleColors.RESET+" = "+easHeader.substring(index, index+shd.exceptionMajorChannelNumberSize)+"   [Decimal: "+(binToDec(easHeader.substring(index, index+shd.exceptionMajorChannelNumberSize))+"]"));
                index += shd.exceptionMajorChannelNumberSize;
                
                System.out.println(ConsoleColors.GREEN_BRIGHT+"reserved_9" +ConsoleColors.RESET+" = "+easHeader.substring(index, index+shd.reserved_9Size));
                index += shd.reserved_9Size;
                
                System.out.println(ConsoleColors.GREEN_BRIGHT+"exceptionMinorChannelNumber" +ConsoleColors.RESET+" = "+easHeader.substring(index, index+shd.exceptionMinorChannelNumberSize)+"   [Decimal: "+(binToDec(easHeader.substring(index, index+shd.exceptionMinorChannelNumberSize))+"]"));
                index += shd.exceptionMinorChannelNumberSize;   
            }
            else{
                System.out.println(ConsoleColors.GREEN_BRIGHT+"reserved_10" +ConsoleColors.RESET+" = "+easHeader.substring(index, index+shd.reserved_10Size));
                index += shd.reserved_10Size; 
                
                System.out.println(ConsoleColors.GREEN_BRIGHT+"exceptionOobSourceId" +ConsoleColors.RESET+" = "+easHeader.substring(index, index+shd.exceptionOobSourceIdSize)+"   [Decimal: "+(binToDec(easHeader.substring(index, index+shd.exceptionOobSourceIdSize))+"]"));
                index += shd.exceptionOobSourceIdSize; 
            }
            
        }
        // "+ConsoleColors.GREEN_BRIGHT+"[Total: "+iterate+"]"+ConsoleColors.RESET+"
        System.out.println("*************************"+ConsoleColors.GREEN_BRIGHT+"End of Exception Channels [Total: "+iterate+"]"+ConsoleColors.RESET+"************************");
        
        System.out.println(ConsoleColors.GREEN_BRIGHT+"reserved_11" +ConsoleColors.RESET+" = "+easHeader.substring(index, index+shd.reserved_11Size));
        index += shd.reserved_11Size; 
        
        System.out.println(ConsoleColors.GREEN_BRIGHT+"descriptorsLength" +ConsoleColors.RESET+" = "+easHeader.substring(index, index+shd.descriptorsLengthSize));
        iterate = Integer.parseInt((easHeader.substring(index, index + shd.descriptorsLengthSize)), 2);
        index += shd.descriptorsLengthSize;
        
        for (int i = 0; i < iterate; i++){
            System.out.println(ConsoleColors.GREEN_BRIGHT+"descriptor" +ConsoleColors.RESET+" = "+easHeader.substring(index, index+iterate*8));
            index += iterate*8; 
        }
        
        System.out.println(ConsoleColors.GREEN_BRIGHT+"crc_32" +ConsoleColors.RESET+" = "+easHeader.substring(index, index+shd.crc_32Size));
        System.out.println(ConsoleColors.GREEN_BRIGHT+"Crc32 Hex: " +ConsoleColors.RESET+StringProcessor.binToHex(easHeader.substring(index, index+shd.crc_32Size)));
        index += shd.crc_32Size;
        System.out.println(ConsoleColors.GREEN_BRIGHT+"Padded: "+ConsoleColors.RESET+easHeader.substring(index).length()/8+" bytes");
        System.out.println(ConsoleColors.GREEN_BRIGHT+"Total Length" +ConsoleColors.RESET+" = "+sectionLength+" + "+easHeader.substring(index).length()/8+" + 3 = "+(sectionLength+easHeader.substring(index).length()/8 + 3)+" bytes as compared to"+ConsoleColors.GREEN_BRIGHT+" EAS Payload Length: "+ConsoleColors.RESET+easTotalLen+" bytes");
        System.out.println("Note: Padding bytes are not counted in <sectionLength>");
        System.out.println(ConsoleColors.GREEN_BRIGHT+"PID" +ConsoleColors.RESET+" = "+PID);
        System.out.println(ConsoleColors.YELLOW+"***********************Packet Parsing Finished Successfully!!************************"+ConsoleColors.RESET+"\n\n");
        // 3 bytes in total length are for table ID to section length (24 bits=3 bytes)
    }
    
    public static int binToDec(String binary){
        return Integer.parseInt(binary, 2);
    }
}
