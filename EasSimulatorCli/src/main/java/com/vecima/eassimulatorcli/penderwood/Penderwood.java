/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vecima.eassimulatorcli.penderwood;

/**
 *
 * @author zayeed
 */

import com.vecima.eassimulatorcli.App;
import com.vecima.eassimulatorcli.color.ConsoleColors;
import com.vecima.eassimulatorcli.scte18.Scte18Builder;
import java.text.ParseException;

public class Penderwood {
    public static boolean oobPid = false;
    private static final int EAS_HEADER_SIZE = 184;
    private static final int P0 = 0x475FFB10;
    private static final int P1 = 0x471FFB10;
    private static final int oobP0 = 0x475FFC10;
    private static final int oobP1 = 0x471FFC10;
    //eas header: 510
    //packet0 = (0-366)/2 = 183 + p0.length()/2 = 188 bytes
    //packet1 = (366-510)/2 = 72
    //188 - 72 = 116
    //116 - p1.length()/2 = 112 (need 112 bytes padding)
    //hence 112x2 = 224 of 0xf;
    
    public static String[] scte18Initializer() throws ParseException{
        Scte18Builder builder = new Scte18Builder();
        
        String easHeader = builder.buildHeader();
        
        //int headerBytes = Scte18HeaderBuilder.scte18Header.length()/2;
        int headerBytes = easHeader.length()/2;
        int numOfPackets = 0;
        
        numOfPackets += (int) Math.ceil((float) headerBytes/EAS_HEADER_SIZE);
        //System.out.println("numOfPackets: "+numOfPackets);
        
        easHeader += easHeaderPadding(numOfPackets, headerBytes);
        
        String[] packets = new String[numOfPackets];
        int index = 0;
        
        if (!oobPid) {
            for (int i = 0; i < numOfPackets; i++) {
                packets[i] = Integer.toHexString((i == 0) ? P0 : P1 + i).toUpperCase() + easHeader.substring(index, index + EAS_HEADER_SIZE * 2);
                index += EAS_HEADER_SIZE * 2;
            }
        }
        else{
            for (int i = 0; i < numOfPackets; i++) {
                packets[i] = Integer.toHexString((i == 0) ? oobP0 : oobP1 + i).toUpperCase() + easHeader.substring(index, index + EAS_HEADER_SIZE * 2);
                index += EAS_HEADER_SIZE * 2;
            }
        }
 
        return packets;   
    }

    public static String easHeaderPadding(int numOfPackets, int headerBytes){
        int padMore = numOfPackets*EAS_HEADER_SIZE - headerBytes;
        if(!App.tunrOffAllLog)
            System.out.println(ConsoleColors.GREEN_BRIGHT+"Padded : "+ConsoleColors.RESET+padMore+" bytes");
        String padding = "";
        for(int i = 0; i < padMore; i++){
            padding +="FF";
        }
        return padding;
    }
}
