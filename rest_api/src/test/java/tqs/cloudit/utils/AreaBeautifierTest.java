package tqs.cloudit.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class AreaBeautifierTest {

    @Test
    public void beautify() {
        assertEquals("Area 1 Test", AreaBeautifier.beautify("area 1 test"));
        assertEquals("Area 1aaa Test", AreaBeautifier.beautify("area 1AaA test"));
        assertEquals("Database Systems", AreaBeautifier.beautify("DaTaBaSe sYsTeMs"));
        assertEquals("Area Area", AreaBeautifier.beautify("area      area"));
    }
}