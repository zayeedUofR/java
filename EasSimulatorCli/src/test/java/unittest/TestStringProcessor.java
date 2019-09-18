/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unittest;

import org.junit.Assert;
import org.junit.Test;
import com.vecima.eassimulatorcli.util.StringProcessor;

/**
 *
 * @author zayeed
 */
public class TestStringProcessor {

    @Test
    public void test_asciiToHex() {
        String ascii = "Vecima Networks has issued a Tornado Watch for the following areas: Pinal, AZ, Mericopa.";
        String expected_value = "566563696D61204E6574776F726B732068617320697373756564206120546F726E61646F20576174636820666F722074686520666F6C6C6F77696E672061726561733A2050696E616C2C20415A2C204D657269636F70612E787878787878787878787878";
        String hex = StringProcessor.asciiToHex(ascii, 100);
        Assert.assertEquals(expected_value, hex);
    }

    @Test
    public void test_hexToAscii() {
        String hex = "566563696D61204E6574776F726B732068617320697373756564206120546F726E61646F20576174636820666F722074686520666F6C6C6F77696E672061726561733A2050696E616C2C20415A2C204D657269636F70612E";
        String expected_value = "Vecima Networks has issued a Tornado Watch for the following areas: Pinal, AZ, Mericopa.";
        String ascii = StringProcessor.hexToAscii(hex);
        Assert.assertEquals(expected_value, ascii);
    }

    @Test
    public void test_binToHex() {
        String bin_1 = "0000000101100101011011100110011100000001000000000000000010101010010101100110010101100011011010010110110101100001001000000100111001100101011101000111011101101111011100100110101101110011001000000100100001000001010100110010000001001001010100110101001101010101010001010100010000100000010000010010000001010010010001010101000101010101010010010101001001000101010001000010000001010111010001010100010101001011010011000101100100100000010101000100010101010011010101000010000001000110010011110101001000100000010101000100100001000101001000000100011001001111010011000100110001001111010101110100100101001110010001110010000001000011010011110101010101001110010101000100100101000101010100110010111101000001010100100100010101000001010100110011101000100000010100000110100101101110011000010110110000101100001000000100000101011010001110110010000001000001010101000010000000000001111000000011000100110010001110100011000100110011001000000101000001001101001000000100111101001110";
        String bin_2 = "1011";
        String hex_1 = StringProcessor.binToHex(bin_1);
        String expected_hex_1 = "01656E67010000AA566563696D61204E6574776F726B7320484153204953535545442041205245515549524544205745454B4C59205445535420464F522054484520464F4C4C4F57494E4720434F554E544945532F41524541533A2050696E616C2C20415A3B2041542001E031323A313320504D204F4E";
        Assert.assertEquals(hex_1, expected_hex_1);
        String hex_2 = StringProcessor.binToHex(bin_2);
        Assert.assertEquals(hex_2, "B");
        
        String bin_3 = "10101";
        String hex_3 = StringProcessor.binToHex(bin_3);
        Assert.assertEquals(hex_3, "-1");
    }
    
    @Test
    public void test_binaryToHexArray(){
        String[] bin = {"1100", "0100", "0100", "1110", "1111", "1011", "0101", "0111", "0001", "0110", "1100", "0111"};
        String[] hex = StringProcessor.binaryToHexArray(bin);
        String[] expected_result = {"c", "4", "4", "e", "f", "b", "5", "7", "1", "6", "c", "7"};
        Assert.assertArrayEquals(expected_result, hex);
        
    }
    
    @Test
    public void test_toByteArray(){
        String hex = "01656E670100";
        byte[] bytes = StringProcessor.toByteArray(hex);
        byte[] expected_result = {1, 101, 110, 103, 1, 0};
        Assert.assertArrayEquals(bytes, expected_result);
 
    }

    @Test
    public void test_bitStuffer() {
        String digits = "100";
        String bcd = StringProcessor.bitStuffer(digits, 8);
        Assert.assertEquals(8, bcd.length());
    }

    @Test
    public void test_multicast_ip_to_mac() {
        String dstIP = "239.8.8.70";
        String mac = StringProcessor.multicast_ip_to_mac(dstIP);
        Assert.assertEquals("01:00:5E:08:08:46", mac);

        String dstIP_2 = "239.5.150.150";
        String mac_2 = StringProcessor.multicast_ip_to_mac(dstIP_2);
        Assert.assertEquals("01:00:5E:05:96:96", mac_2);
    }

    @Test
    public void test_isBinary() {
        String binary = "1010110100";
        Assert.assertTrue(StringProcessor.isBinary(binary));

        String not_binary = "41020150";
        Assert.assertFalse(StringProcessor.isBinary(not_binary));
    }

    @Test
    public void test_hexToBin() {
        String hex = "2";
        Assert.assertEquals("0010", StringProcessor.hexToBin(hex));

        String hex_2 = "AF";
        Assert.assertEquals("10101111", StringProcessor.hexToBin(hex_2));
    }
}
