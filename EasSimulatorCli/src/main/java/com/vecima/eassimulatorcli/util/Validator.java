/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vecima.eassimulatorcli.util;


import com.vecima.eassimulatorcli.App;
import static com.vecima.eassimulatorcli.App.ifaceAddr;
import com.vecima.eassimulatorcli.color.ConsoleColors;
import com.vecima.eassimulatorcli.scte18.Scte18AlertCodes;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.Pcaps;
/**
 *
 * @author zayeed
 */
public class Validator {
    public static  String osName = System.getProperty("os.name");
    public static  String osArch = System.getProperty("os.arch");
    public static  String javaHome = System.getProperty("java.home");
    public static boolean writeLogs = false;
    
    public static boolean isValidIPv4(String ipv4Address) {
        String regex = 
                "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
		"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
		"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
		"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        return Pattern.matches(regex, ipv4Address);
    }
    
    public static boolean isValidPort(String portNo){
        int port;
        if(isDigitsOnly(portNo)){
            port = Integer.parseInt(portNo, 10);
            return port > 0 && port < 65536;
        }
        else
            return false;
        
    }
    
    public static boolean isDigitsOnly(String digits){
        String regExp = "\\d+";
        return digits.matches(regExp);
    }
    
    public static boolean isValidAlertCode(String alertCode){
        boolean flag = false;
        String[][] codes = Scte18AlertCodes.getAlertCodes();
        if(alertCode.length() != 3)
            return false;
        else
            for (int i = 0; i < codes.length; i++){
                if(alertCode.equals(codes[i][0])){
                    flag = true;
                    break;
                }
            }
        
        return flag;
    }
    
    public static boolean isValidAreaCode(String areaCodes){
        boolean flag = false;
        List<String> locationList = new ArrayList<>();
        locationList.addAll(Arrays.asList(areaCodes.split(" ")));
        for(String loc: locationList){
            if(loc.length() != 6){
                System.out.println(ConsoleColors.RED+"Invalid Code: "+loc+ConsoleColors.RESET);
                return false;
            }
                
            for (int i = 0; i < Locations.getAREAS().length; i++){
                if(loc.equals(Locations.getAREAS()[i][0])){
                    flag = true;
                    break;
                }
                else if(i == Locations.getAREAS().length -1){
                    //ErrorMessages.setInvalidAreaCode(loc);
                    System.out.println(ConsoleColors.RED+"Invalid Code: "+loc+ConsoleColors.RESET);
                    flag = false;
                }
            }
        }
        
        
        return flag;
    }
    
    public static void checkEnv(){
        String command = "getcap "+javaHome+"/bin/java";
        String result;
        if(osName.contains("Linux"))
        {
            result= executeCommand(command);

            if(!result.contains("cap_net_admin,cap_net_raw+eip")){
                System.out.println("Allow Java to capture packet on available interface(s).\n Run the command appeared below:\n sudo setcap cap_net_raw,cap_net_admin=eip " +javaHome+"/bin/java");
                writeLogs = true;
                System.out.println(ConsoleColors.RED_BRIGHT + "Fatal error: Network operations not permitted!"+ConsoleColors.RESET);
                System.exit(0);
            }
        }
        //System.out.println(result);
        //System.out.println("Arch: "+System.getProperty("os.arch"));
        //System.out.println("Os: "+System.getProperty("os.name"));
        //System.out.println("Os: "+System.getProperty("java.home"));
    }
    
    private static String executeCommand(String command) {

        StringBuilder output = new StringBuilder();

        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader
                    = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

        } catch (IOException | InterruptedException e) {
        }

        return output.toString();

    }
    
    public static boolean isBoolean(String str){
        return str.equals("true") || str.equals("false");
        
    }

    public static boolean isValid(String property) {
        return property.equals("enabled") || property.equals("disabled"); 
    }

    public static boolean isInRange(String property, int start, int end) {
        int val = Integer.parseInt(property, 10);
        return val >= start && val <= end;
    }
    
    public static boolean isValidMajorMinorCombination(String property) {
        String[] channel_pairs = property.split(" ");
        for (String pair:channel_pairs){
            if(pair.indexOf('-') == -1){
                return false;
            }
            else if (pair.indexOf('-') == 0){
                return false;
            }
            else if (pair.indexOf('-') == pair.length() - 1){
                return false;
            }
            
            else{
                String[] values = pair.split("-");
                for (String value : values) {
                    int result = Integer.parseInt(value);
                    if (result > 255 || result < 0) {
                        return false;
                    }
                }
            }
        }
        
        
        return true;
    }
    
    public static boolean isInterfaceSelectable(String srcIP) throws PcapNativeException{
        boolean iface;
        
        // select network interface
        try {
            App.ifaceAddr = InetAddress.getByName(srcIP);
        } catch (IOException e) {
            System.out.println(e);
            return false;
        }
        App.nif = Pcaps.getDevByAddress(ifaceAddr);
        iface = App.nif != null;
        
        /*if (nif == null) {
            iface= false;
        } else {
            iface = true;
        }*/
        return iface;
    }
    
    public static boolean isValidMac(String mac) {
        String pattern = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(mac);
        return m.find();
    }
}
