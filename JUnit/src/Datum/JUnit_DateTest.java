package Datum;

/**
 * Created by Damjan Lazic on 19.11.2018.
 */

import org.junit.jupiter.api.Test;

import static Datum.DateTest.getDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class JUnit_DateTest {

    Date d1 = new Date();
    Date d2 = getDate(12, 27, 2015); // December 27, 2015
    Date d3 = getDate(0, 99, 8045);
    @Test
    public void Test1(){
        assertEquals(new Date(1, 1, 1900), d1);
        assertFalse(d1.isLeapYear());
        assertEquals(31, d1.lastDayInMonth());
    }

    @Test
    public void Test2(){
        assertEquals(new Date(12, 27,2015), d2);
        assertEquals(new Date(1, 1,1900), d3);
    }

    d3.setDate(2, 28, 2012); // same as Date() - try use to use
}
