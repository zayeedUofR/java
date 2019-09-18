/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unittest;

import org.junit.Assert;
import org.junit.Test;
import com.vecima.eassimulatorcli.penderwood.Penderwood;
import java.text.ParseException;

/**
 *
 * @author zayeed
 */
public class TestPenderwood {
    @Test
    public void test_scte18Initializer() throws ParseException{
        String[] packets = Penderwood.scte18Initializer();
        Assert.assertTrue(packets.length == 3);
        Assert.assertTrue(packets[0].length()/2 == 188);
        Assert.assertTrue(packets[1].length()/2 == 188);
        Assert.assertTrue(packets[2].length()/2 == 188);
    }
    
    @Test
    public void test_easHeaderPadding(){
        int numberOfPackets = 3;
        String eas_header = "00D8B1AC0000E100000027DD454153035257541C01656E67010000145245515549524544205745454B4C5920544553543C49CF66C1000FFFFF0000FC84FC020000012D01656E67010000AA566563696D61204E6574776F726B732048415320495353554544204120544F524E41444F205741524E494E4720464F522054484520464F4C4C4F57494E4720434F554E544945532F41524541533A2050696E616C2C20415A3B204D617269636F70612C20415A3B20416C70696E652C2043413B20556E69746564205374617465733B2043616C69666F726E69613B2042757474652C2043413B20416C61736B613B2044656E616C692C20414B3B2044656C61776172653B204D6173736163687573657474733B20466C6F726964613B20436F6C6F7261646F3B2041542031323A303720504D204F4E204170722030332C20323031392045464645435449564520554E54494C2031323A323220504D2E204D4553534147452046524F4D20566563696D612E200C040C15040C0D060C03000C00060C00060C07020C00020C440A0C00190C000C0C00080C0004FFFC74FC02FFFC73FC01FFFC4FFC01FFFC74FC02FC00248D19EC";
        int headerBytes = eas_header.length()/2;
        String result = Penderwood.easHeaderPadding(numberOfPackets, headerBytes);
        Assert.assertTrue(result.length()/2 == 120);
    }
}
