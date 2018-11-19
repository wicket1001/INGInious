package Fraction;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Damjan Lazic on 12.11.2018.
 */
public class JUnit_Fraction {

    Fraction fraction1 = new Fraction();
    Fraction fraction2 = new Fraction();
    Fraction fraction3 = new Fraction();

        @Test
        public void testCreateFraction(){
            fraction1.setNumerator(0);
            assertEquals(0, fraction1.getNumerator());
            assertEquals(1, fraction1.getDenominator());
        }

    @Test
    public void testFractionOnlyNumerator(){
        fraction1.setNumerator(17);
        assertEquals(17, fraction1.getNumerator());
        assertEquals(1, fraction1.getDenominator());
    }

    @Test
    public void testReducedFraction(){
        fraction1.setNumerator(20);
        fraction1.setDenominator(-12);
        assertEquals(-5, -fraction1.getNumerator());
        assertEquals(3, fraction1.getDenominator());
    }

    @Test
    public void testFractionAdd(){
        fraction1.setNumerator(1);
        fraction1.setDenominator(2);
        fraction2.setNumerator(1);
        fraction2.setDenominator(6);
        assertEquals(2, (fraction1.add(fraction2)).getNumerator());
        assertEquals(3, (fraction1.add(fraction2)).getDenominator());
    }

    @Test
    public void testFractionSub(){
        fraction1.setNumerator(1);
        fraction1.setDenominator(2);
        fraction2.setNumerator(1);
        fraction2.setDenominator(6);
        assertEquals(1, (fraction1.sub(fraction2)).getNumerator());
        assertEquals(3, (fraction1.sub(fraction2)).getDenominator());
    }

    
    @Test
    public void testFractionMult(){
        fraction1.setNumerator(6);
        fraction1.setDenominator(5);
        fraction2.setNumerator(10);
        fraction2.setDenominator(8);
        assertEquals(3, (fraction1.mult(fraction2)).getNumerator());
        assertEquals(2, (fraction1.mult(fraction2)).getDenominator());
    }

    @Test
    public void testFractionDiv(){
        fraction1.setNumerator(16);
        fraction1.setDenominator(3);
        fraction2.setNumerator(4);
        fraction2.setDenominator(9);
        assertEquals(12, (fraction1.div(fraction2)).getNumerator());
        assertEquals(1, (fraction1.div(fraction2)).getDenominator());
    }

    @Test
    public void testFractionOne(){
        fraction1.setNumerator(1);
        fraction1.setDenominator(2);
        fraction2.setNumerator(1);
        fraction2.setDenominator(3);
        fraction3.setNumerator(-6);
        fraction3.setDenominator(5);
        assertEquals(-1, ((fraction1.add(fraction2)).mult(fraction3)).getNumerator());
        assertEquals(1, ((fraction1.add(fraction2)).mult(fraction3)).getDenominator());
    }

    @Test
    public void testFractionModulo(){
        fraction1.setNumerator(23);
        fraction1.setDenominator(14);
        fraction2.setNumerator(3);
        fraction2.setDenominator(7);
        assertEquals(5, (fraction1.modulo(fraction2)).getNumerator());
        assertEquals(14, (fraction1.modulo(fraction2)).getDenominator());
    }

    @Test
    public void testFractiontoDouble(){
        fraction1.setNumerator(1);
        fraction1.setDenominator(8);
        assertEquals(0.125, fraction1.toDouble());
    }

    @Test
    public void testFractions(){
        assertEquals(69905, new Fraction(0.033333333).getNumerator());
        assertEquals(2097152, new Fraction(0.033333333).getDenominator());
        assertEquals(1398101, new Fraction(0.33333333).getNumerator());
        assertEquals(4194304, new Fraction(0.33333333).getDenominator());
        assertEquals(69905, new Fraction(0.066666666).getNumerator());
        assertEquals(1048576, new Fraction(0.066666666).getDenominator());
        assertEquals(12023671, new Fraction(1.433333333).getNumerator());
        assertEquals(8388608, new Fraction(1.433333333).getDenominator());
        assertEquals(65, new Fraction(0.5078125).getNumerator());
        assertEquals(128, new Fraction(0.5078125).getDenominator());
        assertEquals(62017, new Fraction(0.9463043212890625).getNumerator());
        assertEquals(65536, new Fraction(0.9463043212890625).getDenominator());
    }
}
