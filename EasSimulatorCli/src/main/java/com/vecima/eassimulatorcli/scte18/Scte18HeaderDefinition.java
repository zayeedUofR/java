/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vecima.eassimulatorcli.scte18;

/**
 *
 * @author zayeed
 */

public class Scte18HeaderDefinition {
    public static final int tableIDSize = 8;                                    //8 bits
    public static final int sectionSyntaxIndicatorSize = 1;                     //1
    public static final int zeroSize = 1 ;                                      //1
    public static final int reserved_Size = 2;                                  //2
    public static final int sectionLengthSize = 12;                             //12
    public static final int tableIdExtensionSize = 16;                          //16
    public static final int reserved_2Size = 2;                                 //2
    public static final int sequenceNumberSize = 5;                             //5
    public static final int currentNextIndicatorSize= 1;                        //1
    public static final int sectionNumberSize = 8;                              //8
    public static final int lastSectionNumberSize = 8;                          //8
    public static final int protocolVersionSize = 8;                            //8
    public static final int easEventIdSize = 16;                                //16
    public static final int easOriginatorCodeSize = 24;                         //24
    public static final int easEventCodeLengthSize = 8;                         //8
    public static final int easEventCodeSizeVar = calculateEasEventCodeSize();     //var
    public static final int natureOfActivationTextLengthSize = 8;               //8 -> e.g 28 bytes
    public static final int natureOfActivationTextSizeVar = calculateNatureOfActivationTextSize();                 //var
    public static final int alertMessageTimeRemainingSize = 8;                  //8
    public static final int eventStartTimeSize = 32;                            //32
    public static final int eventDurationSize = 16;                             //16
    public static final int reserved_3Size = 12;                                //12
    public static final int alertPrioritySize = 4;                              //4
    public static final int detailsOobSourceIdSize = 16;                        //16
    public static final int reserved_4Size = 6;                                 //6
    public static final int detailsMajorChannelNumberSize = 10;                 //10
    public static final int reserved_5Size = 6;                                 //6
    public static final int detailsMinorChannelNumberSize = 10;                 //10
    public static final int audioOobSourceIdSize = 16;                          //16
    public static final int alertTextLengthSize = 16;                           //16
    public static final int alertTextSizeVar = calculateAlertTextSize();           //var
    public static final int locationCodeCountSize = 8;                          //8
    //for(i = 0; i < locationCodeCount; i++ ){
        public static final int stateCodeSize = 8;                              //8
        public static final int countySubdivisionSize = 4;                      //4
        public static final int reserved_6Size = 2;                             //2
        public static final int countyCodeSize = 10;                            //10
    //}
    public static final int exceptionCountSize = 8;                             //8
    //for (i = 0; i < exceptionCount; i++){
        public static final int inBandReferenceSize = 1;                        //1
        public static final int reserved_7Size = 7;                             //7
        //if(inBandReference){
            public static final int reserved_8Size = 6;                         //6
            public static final int exceptionMajorChannelNumberSize = 10;       //10
            public static final int reserved_9Size = 6;                         //6
            public static final int exceptionMinorChannelNumberSize = 10;       //10
        //}
        //else{
            public static final int reserved_10Size = 16;                       //16
            public static final int exceptionOobSourceIdSize = 16;              //16
        //}
    //}
    public static final int reserved_11Size = 6;                                //6
    public static final int descriptorsLengthSize = 10;                         //10
    //for(i = 0; i < N; i++){   
        public static final int descriptorSizeVar = calculateDescriptorSize();     //var
    //}
    public static final int crc_32Size = 32;                                    //32
    
    
    public Scte18HeaderDefinition(){}
    // Calculate the vairable lengths based on input.
    private static int calculateEasEventCodeSize(){
        return 0;
    }
    
    private static int calculateNatureOfActivationTextSize(){
        return 0;
    }
    
    private static int calculateAlertTextSize(){
        return 0;
    }
    private static int calculateDescriptorSize(){
        return 0;
    }
}

