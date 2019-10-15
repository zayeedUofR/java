/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vecima.eassimulatorcli.util;

import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author zayeed
 */
public class StringProcessor {

    public static String asciiToHex(String ascii, int alertTextLength) { //verified: ok
        int lengthdifference;
        if (ascii.length() > alertTextLength) {
            ascii = ascii.substring(0, alertTextLength);
        } else if (ascii.length() < alertTextLength) {
            //System.out.println("Text is shorter by: "+(alertTextLength - ascii.length()));
            lengthdifference = alertTextLength - ascii.length();
            for (int i = 0; i < lengthdifference; i++) {
                ascii += "x"; //make the text length equal to textLength defined with padding x to the end
            }
        }
        
        char[] ch = ascii.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : ch) {
            String hexByte = String.format("%H", c);
            stringBuilder.append(hexByte);
        }
        return stringBuilder.toString();
    }

    public static String hexToAscii(String hexBytes) { //verified: ok
        StringBuilder stringBuilder = new StringBuilder("");
        for (int i = 0; i < hexBytes.length(); i += 2) {
            String str = hexBytes.substring(i, i + 2);
            stringBuilder.append((char) Integer.parseInt(str, 16));
        }
        return stringBuilder.toString();
    }

    public static String binToHex(String binaryString) {                        //verified: yes
        int index = 0;
        //String bin = "0000000101100101011011100110011100000001000000000000000010101010010101100110010101100011011010010110110101100001001000000100111001100101011101000111011101101111011100100110101101110011001000000100100001000001010100110010000001001001010100110101001101010101010001010100010000100000010000010010000001010010010001010101000101010101010010010101001001000101010001000010000001010111010001010100010101001011010011000101100100100000010101000100010101010011010101000010000001000110010011110101001000100000010101000100100001000101001000000100011001001111010011000100110001001111010101110100100101001110010001110010000001000011010011110101010101001110010101000100100101000101010100110010111101000001010100100100010101000001010100110011101000100000010100000110100101101110011000010110110000101100001000000100000101011010001110110010000001000001010101000010000000000001111000000011000100110010001110100011000100110011001000000101000001001101001000000100111101001110";
        //String bin_2 = "010100110100010101010000001000000011000100110111001011000010000000110010001100000011000100111000001000000100010101000110010001100100010101000011010101000100100101010110010001010010000001010101010011100101010001001001010011000010000000110001001100100011101000110010001110000010000001010000010011010010111000100000010011010100010101010011010100110100000101000111010001010010000001000110010100100100111101001101001000000101011001100101011000110110100101101101011000010010111000100000";
        
        if (! binaryString.matches("^[01]+$")) {
            System.out.println("Invalid binary string!");
            return "-1";
        }
        
        if (binaryString.length() % 4 == 0) {
            String[] bin = new String[binaryString.length() / 4];
            for (int i = 0; i < binaryString.length() / 4; i++) {
                bin[i] = "";
                for (int j = index; j < index + 4; j++) {
                    bin[i] += binaryString.charAt(j);
                }
                index += 4;
            }
            String[] result = binaryToHexArray(bin);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < result.length; i++) {
                stringBuilder.append(result[i].toUpperCase());
            }

            return stringBuilder.toString();
        } else {
            System.out.println("Binary String length is not correct!");
            return "-1";
        }
    }

    public static String[] binaryToHexArray(String[] bin) {
        String[] result = new String[bin.length];
        for (int i = 0; i < bin.length; i++) {
            result[i] = Integer.toHexString(Integer.parseInt(bin[i], 2));
        }
        //return Integer.toHexString(Integer.parseInt(bin[0], 2));
        return result;
    }

    public static String hexToBin(String hexStr) {

        StringBuilder sb = new StringBuilder();
        String temp;
        for (int i = 0; i < hexStr.length(); i++) { //date: 24 sept 2018
            temp = Integer.toBinaryString(Integer.parseInt(hexStr.substring(i, i + 1), 16));
            sb.append(bitStuffer(temp, 4));
        }
        return sb.toString();

    }

    public static String bitStuffer(String toBePadded, int len) {

        String padString = "";
        String padded = "";
        for (int i = 0; i < len; i++) {
            padString = padString + '0'; //pad one byte/bit with leading zeros
        }

        int strLen = toBePadded.length();
        if (strLen < len) {
            padded = (padString.substring(0, padString.length() - toBePadded.length())) + toBePadded;
            return padded;
        } else if(strLen > len){
            return toBePadded.substring(0, len);
        }
        else
            return toBePadded;
    }
    
    public static byte[] toByteArray(String hexString) {
        //System.out.println("Byte Array: "+DatatypeConverter.parseHexBinary(s));
        return DatatypeConverter.parseHexBinary(hexString);
    }
    
    public static boolean isBinary(String str){
        return str.matches("[01]+");
    }
    
    public static String multicast_ip_to_mac(String ip_address){
        String mac="01:00:5E:";
        String[] octets = ip_address.split("\\.");
        
        int second_octet = Integer.parseInt(octets[1]);
        String second_oct_binary = bitStuffer(Integer.toBinaryString(second_octet), 8);
        String second_octet_hex = bitStuffer(binToHex("0"+second_oct_binary.substring(1)), 2);

        int third_octet = Integer.parseInt(octets[2], 10);
        int fourth_octet = Integer.parseInt(octets[3], 10);

        String third_octet_hex = bitStuffer(Integer.toHexString(third_octet), 2);
        String fourth_octet_hex = bitStuffer(Integer.toHexString(fourth_octet), 2);

        return mac + second_octet_hex + ":" + third_octet_hex + ":" + fourth_octet_hex;
    }
}

