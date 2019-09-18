/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unittest;

import org.junit.Assert;
import org.junit.Test;
import com.vecima.eassimulatorcli.crc32.Crc32Mpeg;

/**
 *
 * @author zayeed
 */
public class TestCrc32Mpeg {
    @Test
    public void test_magicCrc32Mpeg(){
        String eas_paylod = "d8b0fa0000df000000288a454153035257541c01656e67010000145245515549524544205745454b4c5920544553547848cd2a74000fffff0000fc00fc00000000b001656e67010000a8566563696d61204e6574776f726b7320484153204953535545442041205245515549524544205745454b4c59205445535420464f522054484520464f4c4c4f57494e4720434f554e544945532f41524541533a2050696e616c2c20415a3b20415420393a313520414d204f4e205345502031392c20323031382045464645435449564520554e54494c20393a333020414d2e204d4553534147452046524f4d20566563696d612e2001040c1500fc00";
        String expected_CRC = "D0EDA43A";
        String result = Crc32Mpeg.magicCrc32Mpeg(eas_paylod);
        Assert.assertEquals(expected_CRC, result);
        eas_paylod = "2a2ee3";
        expected_CRC = "06D21AA4";
        result = Crc32Mpeg.magicCrc32Mpeg(eas_paylod);
        Assert.assertEquals(expected_CRC, result); 
    }
}
