/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vecima.eassimulatorcli;

import com.vecima.eassimulatorcli.color.ConsoleColors;
import static com.vecima.eassimulatorcli.options.OptionManager.executeOptions;
import com.vecima.eassimulatorcli.util.StringProcessor;
import com.vecima.eassimulatorcli.packets.PacketBuilder;
import com.vecima.eassimulatorcli.penderwood.Penderwood;
import com.vecima.eassimulatorcli.property.PropertyManager;
import com.vecima.eassimulatorcli.util.ErrorMessages;
import com.vecima.eassimulatorcli.util.Validator;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Calendar;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;
import org.pcap4j.packet.Packet;
import org.pcap4j.util.NifSelector;

/**
 *
 * @author zayeed
 */
public class App {

    public static int alertID = 10384; //default
    public static int alertMessageDuplicateCount = 2;
    private static boolean[] validateProperties;
    public static boolean printSummary = false;
    public static boolean tunrOffAllLog = false;
    
    private static final int READ_TIMEOUT = 50;
    private static final int SNAPLEN = 65536;
    public static PcapNetworkInterface nif = null;
    public static InetAddress ifaceAddr = null;

    public static PcapNetworkInterface getNetworkDevices() {
        PcapNetworkInterface device = null;
        // Pcap4j comes with a convenient method for listing and choosing a network interface from the terminal
        try {
            // List the network devices available with a prompt
            device = new NifSelector().selectNetworkInterface();
        } catch (IOException e) {
        }
        if (device == null) {
            return null;
        }
        return device;
    }

    public static void main(String[] args) throws Exception {
        //process command line arguments
        executeOptions(args);
        
        //validate properties
        validateProperties = PropertyManager.validateProperties();
        for (int i = 0; i < validateProperties.length; i++) {
            if (!validateProperties[i]) {
                System.out.println(ConsoleColors.RED_BRIGHT + "Fatal error: " + ErrorMessages.getMSG()[i]+ " [ErrorCode: "+StringProcessor.bitStuffer(i+"", 4)+"]");
                System.out.print(ConsoleColors.RESET);
                System.exit(0);
            }
        }
        //checks System environments
        Validator.checkEnv();
        
        //initialize parameters
        String[] packets = Penderwood.scte18Initializer();
        if (!tunrOffAllLog) {
            for (int p = 0; p < packets.length; p++) {
                System.out.println(ConsoleColors.GREEN_BRIGHT + "Packet[" + p + "]: " + ConsoleColors.RESET + packets[p]);
                System.out.println();
            }
        }

        
        PacketBuilder pb = new PacketBuilder();
        Packet[] pkts = new Packet[packets.length];

        for (int i = 0; i < packets.length; i++) {
            pkts[i] = pb.getwholePacket(StringProcessor.toByteArray(packets[i]));
        }
        
        //network interface already selected during validation.
        PcapHandle sendHandle = nif.openLive(SNAPLEN, PromiscuousMode.PROMISCUOUS, READ_TIMEOUT);
        
        //send packets
        for (int i = 0; i < alertMessageDuplicateCount; i++) {
            try {
                for (Packet pkt : pkts) {
                    sendHandle.sendPacket(pkt); //send Prepared Packets
                }
            } catch (PcapNativeException | NotOpenException e) {
            }
        }
        Calendar calendar = Calendar.getInstance();
        System.out.println("\n"+ConsoleColors.GREEN_BRIGHT+"Cable Emergency Alert sent on: "+ calendar.getTime()+" with Eas event ID: "+alertID+ConsoleColors.RESET+"\n");
    }

}

//Linux capture permission:
//getcap /usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java
//setcap cap_net_raw,cap_net_admin=eip /usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java


//System.out.println(nif.getName() + "(" + nif.getDescription() + ")");
//System.out.println(""+packet.toString());
//ExecutorService pool = Executors.newSingleThreadExecutor();