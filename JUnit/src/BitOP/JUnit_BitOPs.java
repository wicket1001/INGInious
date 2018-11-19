package BitOP;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Created by Lazic Damjan on 15.10.2018.
 */



public class JUnit_BitOPs {
    IPv4Bibliothek test = new IPv4Bibliothek();


    @Test
    public void to32BitIpStringTest(){
        assertEquals(0xc0a80001, test.to32BitIp("192.168.0.1"));
        assertEquals(0x64000001, test.to32BitIp("100.0.0.1"));
        assertEquals(0x4A7D2B63, test.to32BitIp("74.125.43.99"));
        assertEquals(0x0302017B, test.to32BitIp("3.2.1.123"));
        assertEquals(0xFFFEFDFC, test.to32BitIp("255.254.253.252"));
    }

    @Test
    public void to32BitIpArrayTest(){
        assertEquals(0xc0a80001, test.to32BitIp(new int[]{192, 168, 0, 1}));
        assertEquals(0x64000001, test.to32BitIp(new int[]{100, 0, 0, 1}));
        assertEquals(0x4A7D2B63, test.to32BitIp(new int[]{74, 125, 43, 99}));
        assertEquals(0x0302017B, test.to32BitIp(new int[]{3, 2, 1, 123}));
        assertEquals(0xFFFEFDFC, test.to32BitIp(new int[]{255, 254, 253, 252}));
    }

    @Test
    public void getSuffixTest(){
        assertEquals(16, test.getSuffix("192.168.0.1/16"));
        assertEquals(12, test.getSuffix("192.168.0.1/12"));
        assertEquals(6, test.getSuffix("192.168.0.1/6"));
        assertEquals(24, test.getSuffix("192.168.0.1/24"));
        assertEquals(30, test.getSuffix("192.168.0.1/30"));
    }

    @Test
    public void getNetmaskTest(){

    }
}
