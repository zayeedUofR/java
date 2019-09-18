/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unittest;

import org.junit.Assert;
import org.junit.Test;
import com.vecima.eassimulatorcli.util.Validator;

/**
 *
 * @author zayeed
 */
public class TestValidator {
    @Test
    public void test_isValidIPv4(){
        String ipv4 = "192.168.255.255";
        Assert.assertTrue(Validator.isValidIPv4(ipv4));
        ipv4 = "45.555.2.0";
         Assert.assertFalse(Validator.isValidIPv4(ipv4));
    }
    
    @Test
    public void test_isValidPort(){
        String port = "8403";
        Assert.assertTrue(Validator.isValidPort(port));
        port = "745222";
        Assert.assertFalse(Validator.isValidPort(port));
    }
    
    @Test
    public void test_isDigitsOnly(){
        Assert.assertTrue(Validator.isDigitsOnly("32211"));
        Assert.assertFalse(Validator.isDigitsOnly("5tr5"));
    }
    
    @Test
    public void test_isValidAlertCode(){
        Assert.assertTrue(Validator.isValidAlertCode("TOR"));
        Assert.assertTrue(Validator.isValidAlertCode("HMW"));
        Assert.assertFalse(Validator.isValidAlertCode("KKK"));
    }
    
    @Test
    public void test_isValidAreaCode(){
        Assert.assertTrue(Validator.isValidAreaCode("006003"));
        Assert.assertTrue(Validator.isValidAreaCode("010000"));
        Assert.assertFalse(Validator.isValidAreaCode("666666"));
    }
    
   
    @Test
    public void test_isBoolean(){
        Assert.assertTrue(Validator.isBoolean("true"));
        Assert.assertTrue(Validator.isBoolean("false"));
        Assert.assertFalse(Validator.isBoolean("String"));
        
    }
    
    @Test
    public void test_isInRange(){
        int x = 255;
        int y = 512;
        Assert.assertTrue(Validator.isInRange("400", x, y));
    }
    
    @Test
    public void test_isValidMajorMinorCombination(){
        String inBandExceptionList = "115-2 113-8 255-2 6-9";
        boolean result = Validator.isValidMajorMinorCombination(inBandExceptionList);
        Assert.assertTrue(result);
        
        inBandExceptionList = "116-2 115-1 79-1 116-";
        result = Validator.isValidMajorMinorCombination(inBandExceptionList);
        Assert.assertFalse(result);
        
        inBandExceptionList = "116-2 115-1 79-1 -";
        result = Validator.isValidMajorMinorCombination(inBandExceptionList);
        Assert.assertFalse(result);
        
        inBandExceptionList = "116-2 115-1 79-1 -2";
        result = Validator.isValidMajorMinorCombination(inBandExceptionList);
        Assert.assertFalse(result);
        
        inBandExceptionList = "116-2 115-1 79-1 2";
        result = Validator.isValidMajorMinorCombination(inBandExceptionList);
        Assert.assertFalse(result);
        
        inBandExceptionList = "116-2 115-1 79-1 116 117-1";
        result = Validator.isValidMajorMinorCombination(inBandExceptionList);
        Assert.assertFalse(result);
        
        inBandExceptionList = "116-2 115-1 79-1 116-9   117-1";
        result = Validator.isValidMajorMinorCombination(inBandExceptionList);
        Assert.assertFalse(result);
    }
    
    @Test
    public void test_isValidMac(){
        String mac = "94:c6:91:7f:3f:9c";
        Assert.assertTrue(Validator.isValidMac(mac));
        mac = "01:00:5E:05:96:96";
        Assert.assertTrue(Validator.isValidMac(mac));
        mac = "01:900:5E:05:96:93";
        Assert.assertFalse(Validator.isValidMac(mac));
        mac = "01:90:5E:05:96:93:";
        Assert.assertFalse(Validator.isValidMac(mac));
    }
}
