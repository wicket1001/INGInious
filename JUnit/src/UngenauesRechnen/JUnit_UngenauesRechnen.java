package UngenauesRechnen;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Damjan on 26.11.2018.
 */
public class JUnit_UngenauesRechnen {

    @Test
    public void basicTest() {
        InexactNumber a = new InexactNumber(300, 5);
        InexactNumber b = new InexactNumber(200, 3);
        assertEquals("500.0±8.0", a.add(b).toString());
        assertEquals("100.0±8.0", a.sub(b).toString());
        assertEquals("60000.0±1900.0", a.mult(b).toString());
        assertEquals("1.5±0.0475", a.div(b).toString());
    }

    @Test
    public void whiskyTest() {
        double volumeBottle = 0.7D;
        double percentBottle = 0;
        double volumeGlass = 0.02D;
        double percentGlass = 7;
        double toleranceBottle = volumeBottle * (percentBottle / 100);
        double toleranceGlass = volumeGlass * (percentGlass / 100);
        InexactNumber inexactBottle = new InexactNumber(volumeBottle, toleranceBottle);
        InexactNumber inexactGlass = new InexactNumber(volumeGlass, toleranceGlass);
        String inexactBottleGetX = String.format("%.4f", inexactBottle.getX());
        String inexactBottleGetDx = String.format("%.4f", inexactBottle.getDx());
        String inexactGlasGetX = String.format("%.4f", inexactGlass.getX());
        String inexactGlasGetDx = String.format("%.4f", inexactGlass.getDx());
        assertEquals("0,7000", inexactBottleGetX);
        assertEquals("0,0000", inexactBottleGetDx);
        assertEquals("0,0200", inexactGlasGetX);
        assertEquals("0,0014", inexactGlasGetDx);
        InexactNumber ausschank = inexactBottle.div(inexactGlass);
        assertEquals("35.0±2.45", ausschank.toString());


        double anzahlMin = ausschank.getMin();
        double anzahlMax = ausschank.getMax();
        double restMin = (anzahlMin % 1) * (inexactGlass.getMin());
        double restMax = (anzahlMax % 1) * (inexactGlass.getMax());
        assertEquals(32.0, Math.floor(anzahlMin));
        assertEquals(0.010229999999999946, restMin);
        assertEquals(37.0, Math.floor(anzahlMax));
        assertEquals(0.009630000000000062, restMax);


        double min = ausschank.getMin() % 1;
        double max = ausschank.getMax() % 1;
        min = Math.round(min * 100) / 100D;
        max = Math.round(max * 100) / 100D;
        assertEquals(0.02, inexactGlass.getX());
        assertEquals(0.0014000000000000002, inexactGlass.getDx());
        assertEquals("35.0±2.45", ausschank.toString());
        assertEquals(32.55, ausschank.getMin());
        assertEquals(0.55, min);
        assertEquals(0.01023, (min * inexactGlass.getMin()));
        assertEquals(0.011770000000000003, (min * inexactGlass.getMax()));
        assertEquals(37.45, ausschank.getMax());
        assertEquals(0.45, max);
        assertEquals(0.009630000000000001, (max * inexactGlass.getMax()));
        assertEquals(0.008369999999999999, (max * inexactGlass.getMin()));
    }

    @Test
    public void wohnungTest(){
        double [] [] information = {
                {1.349, 1.808},
                {4.892, 3.240},
                {4.185, 3.168},
                {4.395, 2.678},
                {2.124, 2.673}


        };
        double deviation = 0.002;
        double originalM2 = 52.3;
        double pricePerM2 = 8.061;
        double priceGes = 463.9;
        assertEquals(52.3, originalM2);
        assertEquals(8.061, pricePerM2);
        assertEquals(463.9, priceGes);
        assertEquals(2.0, (deviation * 1000));


        InexactNumber flat = UngenauRechnenTest.getFlat(information, deviation);

        assertEquals(48.994414, flat.getX());
        assertEquals(0.061023999999999995, flat.getDx());
        assertEquals(48.933389999999996, flat.getMin());
        assertEquals(49.055438, flat.getMax());

        assertEquals(57.548691229376004, (priceGes / pricePerM2));

        assertEquals(9.456633132729493,((new InexactNumber(priceGes).div(flat)).getMin()));
        assertEquals(395.43588571799995,(flat.mult(new InexactNumber(pricePerM2)).getMax()));

        double inaccuracy = UngenauRechnenTest.getInaccuracy(information, originalM2) * 1000;
        assertEquals(108.33723401985947, inaccuracy);
    }
}
