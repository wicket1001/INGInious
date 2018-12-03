package Datum;

/**
 * Created by Damjan Lazic on 19.11.2018.
 */

import org.junit.jupiter.api.Test;

import java.util.function.BooleanSupplier;

import static Datum.DateTest.getDate;
import static org.junit.jupiter.api.Assertions.*;

public class JUnit_DateTest {

    Date d1 = new Date();
    Date d2 = getDate(12, 27, 2015); // December 27, 2015
    Date d3 = getDate(0, 99, 8045);
    Date d4 = getDate(2, 29, 2012);

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
        d3.setDate(2, 28, 2012); // same as Date() - try use to use
        assertEquals(new Date(2, 28, 2012), d3);
        d3.inc();
        assertEquals(new Date(2, 29, 2012), d3);
        d2.inc(7);
        assertEquals(new Date(1, 3, 2016), d2);
        assertEquals(new Date(1, 1, 1900), d1);
        d1.inc(42124);
        assertEquals(new Date(5, 2, 2015), d1);
        //assertEquals(true, d4.equals(d3));
    }
    @Test
    public void specialCases(){
        assertThrows(IllegalArgumentException.class, () -> new Date(0, 2, 2000));
        assertThrows(IllegalArgumentException.class, () -> new Date(13, 2, 2000));
        assertThrows(IllegalArgumentException.class, () -> new Date(2, 0, 2000));
        assertThrows(IllegalArgumentException.class, () -> new Date(32, 3, 2000));
        assertThrows(IllegalArgumentException.class, () -> new Date(2, 29, 1900));
        new Date(2, 29, 2000);
    }

    @Test
    public void Bonus(){
        assertEquals(60, d4.dayOfYear());
        assertEquals(3, d4.dayOfWeek());
        Date today = getDate(11, 16, 2016);
        assertEquals(321, today.dayOfYear());
        assertEquals("Wednesday", today.getNameofWeek(today.dayOfWeek()));
    }

}
