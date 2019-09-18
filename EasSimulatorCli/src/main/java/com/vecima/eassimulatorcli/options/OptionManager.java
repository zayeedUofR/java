/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vecima.eassimulatorcli.options;

import com.vecima.eassimulatorcli.App;
import com.vecima.eassimulatorcli.color.ConsoleColors;
import com.vecima.eassimulatorcli.property.PropertyManager;
import com.vecima.eassimulatorcli.scte18.Scte18AlertCodes;
import com.vecima.eassimulatorcli.scte18.Scte18Decoder;
import com.vecima.eassimulatorcli.util.Locations;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;

/**
 *
 * @author zayeed
 */
public class OptionManager {
    private static final String readConfigFile = "-f";
    private static final String version = "-v";
    private static final String help = "--help";
    private static final String printSummary = "-s";
    private static final String parseScte18 = "-p";
    private static final String alertIdOverride = "-i";
    private static final String about = "-a";
    private static final String locations = "-l";
    private static final String scte18AlertCodes = "-c";
    private static final String writeConfigFile = "-g";
    private static final String noLogs = "-x";
    private static boolean flag = false;
    
    public static void executeOptions(String[] args) throws PcapNativeException, NotOpenException{   //-f config.prop  args.length = 2
        if(args.length == 0){
            System.out.println(ConsoleColors.YELLOW+"No suitable options given, Try --help"+ConsoleColors.RESET);
            System.exit(0);
        }
        for(int i = 0; i < args.length; i++){
            if(args[i].equals(readConfigFile) && args.length > (i+1)){
                if(!args[i+1].endsWith(".properties")){
                    System.out.println(ConsoleColors.YELLOW+"Bad parameter for option -f, invalid config file. Try --help"+ConsoleColors.RESET);
                    System.exit(0);
                }
                else{
                    PropertyManager.fileName = args[i+1];
                    flag = true;
                }
                
            //else if(Arrays.asList(args).contains(readConfigFile)){
            }
            else if(args[i].equals(readConfigFile) && args.length <= (i+1)){
                System.out.println(ConsoleColors.YELLOW+"No properties file given. Exiting..."+ConsoleColors.RESET);
                System.exit(0);
            }
            if(args[i].equals(printSummary)){
                App.printSummary = true;
                flag = true;
            }
            
            if(args[i].equals(noLogs)){
                App.tunrOffAllLog = true;
                flag = true;
            }
            
            if(args[i].equals(scte18AlertCodes) && args.length ==1){
                System.out.println(ConsoleColors.BLUE_BOLD+"SCTE18 Alert Codes: ");
                //String[][] ac = Scte18AlertCodes.getAlertCodes();
                for (int c = 0; c < Scte18AlertCodes.getAlertCodes().length; c++){
                    System.out.println("    "+ConsoleColors.GREEN_BOLD+Scte18AlertCodes.getAlertCodes()[c][0]+ConsoleColors.RESET+": "+ConsoleColors.YELLOW+Scte18AlertCodes.getAlertCodes()[c][1]+ConsoleColors.RESET);
                }
                System.exit(0);
            }
            
            if(args[i].equals(locations) && args.length ==1){
                System.out.println(ConsoleColors.BLUE_BOLD+"Available Location Codes: ");
                for (int c = 0; c < Locations.getAREAS().length; c++){
                    System.out.println("    "+ConsoleColors.GREEN_BOLD+Locations.getAREAS()[c][0]+ConsoleColors.RESET+": "+ConsoleColors.YELLOW+Locations.getAREAS()[c][1]+ConsoleColors.RESET);
                }
                System.exit(0);
            }
            
            if(args[i].equals(parseScte18) && args.length > (i+1)){
                if(args[i+1].endsWith(".pcap") || args[i+1].endsWith(".pcapng")){
                    Scte18Decoder.pcapFile = args[i+1];
                    Scte18Decoder.packetAnalyser();
                    System.exit(0);
                }
                else{
                    System.out.println(ConsoleColors.YELLOW+"No Pcap file Provided. Try --help"+ConsoleColors.RESET);
                    System.exit(0);
                }
            }
            else if(args[i].equals(parseScte18) && args.length <= (i+1)){
                System.out.println(ConsoleColors.YELLOW+"No Pcap file given. Exiting..."+ConsoleColors.RESET);
                System.exit(0);
            }
            
            
            if(args[i].equals(alertIdOverride) && args.length > (i+1))
                App.alertID = Integer.parseInt(args[i+1]);
            if(args[i].equals(help) && args.length == 1){
                System.out.println("Usage: java -jar EasSimulatorCli-vX.X.jar [options] [args]");
                System.out.println("Where options include: ");
                System.out.println("                -f:         read config file [usage: -f [fileName]]");
                System.out.println("                -s:         print Eas Payload Summary upon sending");
                System.out.println("                -x:         turn off all packet information on console");
                System.out.println("                -p:         decode SCTE18 pcap/pcapng [usage: -p [filename]]");
                System.out.println("                -l:         list all available locations");
                System.out.println("                -c:         list all available SCTE18 Alert Codes");
                System.out.println("                -g:         write sample config file");
                System.out.println("            --help:         shows help");
                System.out.println("                -v:         version");
                System.out.println("                -a:         about");
                System.exit(0);
            }
            if(args[i].equals(about) && args.length == 1){
                System.out.println("\n   **********************************************************************************************");
                System.out.println("   |EASSimulatorCli v2.3                                                                           |\n   |Developed by: Zayeed Chowdhury                                                               |\n   |Acknowledgements: Curtis Petersen, Lloyd Telmo, Trevor Hamm, Alexander Thompson, Blair Smith.|\n   |Copyright &copy; 2018 Vecima Networks.                                                       |");
                System.out.println("    **********************************************************************************************\n");
                System.exit(0);
            }
            if(args[i].equals(version) && args.length == 1){
                System.out.println(ConsoleColors.GREEN_BRIGHT+"EASSimulatorCli version 2.3, Released April 2019"+ConsoleColors.RESET);
                System.exit(0);
            }
            
            if(args[i].equals(writeConfigFile) && args.length == 1){
                PropertyManager pf = new PropertyManager();
                pf.writeProperties();
                System.out.println(ConsoleColors.GREEN_BRIGHT+"A sample config_sample.properties file written in current directory."+ConsoleColors.RESET);
                System.exit(0);
            }
            
            if(args[i].equals(printSummary) && args.length == 1){
                System.out.println(ConsoleColors.YELLOW+"Bad option, -s is conjugal with -f. Try --help"+ConsoleColors.RESET);
                System.exit(0);
            }
            if(args[i].equals(noLogs) && args.length == 1){
                System.out.println(ConsoleColors.YELLOW+"Bad option, -x is conjugal with -f. Try --help"+ConsoleColors.RESET);
                System.exit(0);
            }
            
            else if(!flag){
                System.out.println(ConsoleColors.YELLOW+"Bad option "+args[i]+" or wrong usage. Try --help."+ConsoleColors.RESET);
                System.exit(0);
            }
            
             
        }
    }
    
    
}
