/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unittest;

import org.junit.Assert;
import org.junit.Test;
import com.vecima.eassimulatorcli.datetime.DateTime;
import java.text.ParseException;

/**
 *
 * @author zayeed
 */
public class TestDateTime {
    @Test
    public void test_formatDateTime() throws ParseException{
        String date_time = "Sun Oct 07 20:58:34 CST 2018";
        String result = DateTime.formatDateTime(date_time);
        String expected_result = "08:58 PM ON Oct 07, 2018";
        Assert.assertEquals(expected_result, result);
    }
    
    @Test
    public void test_add15mins() throws ParseException{
         String date_time = "Sun Oct 07 20:58:34 CST 2018";
         String result = DateTime.getTime(date_time);
         Assert.assertEquals("08:58 PM", result);
    }
    
    @Test
    public void test_caluculateEventStartTime() throws ParseException{
        int result = DateTime.caluculateEventStartTime();
        Assert.assertTrue(result > 0);
    }
}
