/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unittest;

import org.junit.Assert;
import org.junit.Test;
import com.vecima.eassimulatorcli.scte18.Scte18Builder;
import com.vecima.eassimulatorcli.scte18.Scte18HeaderDefinition;
import com.vecima.eassimulatorcli.util.StringProcessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

/**
 *
 * @author zayeed
 */
public class TestScte18Builder {

    @Test
    public void test_processIntegerInput() {
        Scte18Builder builder = new Scte18Builder();
        String detailsVideoOobId = "7905";
        String result = builder.processIntegerInput(detailsVideoOobId, Scte18HeaderDefinition.detailsOobSourceIdSize);
        Assert.assertTrue(result.length() == Scte18HeaderDefinition.detailsOobSourceIdSize);
        Assert.assertTrue(StringProcessor.isBinary(result));
    }

    @Test
    public void test_processAlertText() {
        Scte18Builder builder = new Scte18Builder();
        String alert_text = "Vecima Networks HAS ISSUED A TORNADO WARNING FOR THE FOLLOWING COUNTIES/AREAS: Pinal, AZ; Maricopa, AZ;";
        String result = builder.processAlertText(alert_text);
        Assert.assertTrue(StringProcessor.isBinary(result));
        final String hex = StringProcessor.binToHex(result);
        String ascii = StringProcessor.hexToAscii(hex.substring("01656E67010000AA".length()));
        Assert.assertTrue(ascii.equals(alert_text));

    }

    @Test
    public void tes_exceptionChannelListParserInBand() {
        String inband_exceptions = "116-2 115-1 79-1 116-2";
        List<String[]> inBandExpChannels = Scte18Builder.exceptionChannelListParserInBand(inband_exceptions);
        final List<String> exceptionMajorChannelNumber = new ArrayList<>();
        final List<String> exceptionMinorChannelNumber = new ArrayList<>();

        inBandExpChannels.forEach((channels) -> {
            for (int i = 0; i < channels.length - 1; i++) {
                exceptionMajorChannelNumber.add(channels[i]);
                exceptionMinorChannelNumber.add(channels[i + 1]);
            }
        });
        
        List<String> expected_majors = Arrays.asList("116", "115", "79", "116");
        List<String> expected_minors = Arrays.asList("2", "1", "1", "2");

        Assert.assertThat(exceptionMajorChannelNumber, is(expected_majors));
        Assert.assertThat(exceptionMajorChannelNumber, is(not(expected_minors)));
        
        Assert.assertThat(exceptionMinorChannelNumber, is(expected_minors));
        Assert.assertThat(exceptionMinorChannelNumber, is(not(expected_majors)));
    }
    
    @Test
    public void test_exceptionChannelListParserOob(){
        Scte18Builder builder = new Scte18Builder();
        String oobExceptionList = "2012 2059 2076 2079 5022 5023 5024 5042";
        List<String> oobChannels = new ArrayList<>();
        oobChannels = builder.exceptionChannelListParserOob(oobExceptionList);
        List<String> expected_channels = Arrays.asList("2012", "2059", "2076", "2079", "5022", "5023", "5024", "5042");
        Assert.assertThat(oobChannels, is(expected_channels));
        
    }
    
    @Test
    public void test_parseLocationInfo(){
        Scte18Builder builder = new Scte18Builder();
        String locations = "004021 004013 006003 000000 006000 006007 002000 002068 010000 025000 012000 008000";
        int location_size = builder.parseLocationInfo(locations);
        Assert.assertTrue(location_size == 12);
    }
}
