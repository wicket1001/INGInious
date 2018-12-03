package UngenauesRechnen;


public class UngenauRechnenTest {
    public static void main(String[] args) {
        test(new InexactNumber(300, 5), new InexactNumber(200, 3));
        test(new InexactNumber(10, 1), new InexactNumber(5, 2));
        whisky(0.7D, 0, 0.02D, 7);
        double [] [] information = {
                {1.349, 1.808},
                {4.892, 3.240},
                {4.185, 3.168},
                {4.395, 2.678},
                {2.124, 2.673}


        };
        double deviation = 0.002; // 2 mm Abweichung bei Bosch Laser Messer
        // wohnung(information, deviation, 52.3, 8.061, 463.9);  // Quadratmeter Org: 52,3 ; Quadratmeterpreis: 8,061 ; Miete: 463,90
    }

    private static void test (InexactNumber a, InexactNumber b) {
        System.out.println(a.add(b).toString());
        System.out.println(a.sub(b).toString());
        System.out.println(a.mult(b).toString());
        System.out.println(a.div(b).toString());
        System.out.println();
    }

    private static void whisky (double volumeBottle, double percentBottle, double volumeGlass, double percentGlass) {
        double toleranceBottle = volumeBottle * (percentBottle / 100);
        double toleranceGlass = volumeGlass * (percentGlass / 100);
        InexactNumber inexactBottle = new InexactNumber(volumeBottle, toleranceBottle);
        InexactNumber inexactGlass = new InexactNumber(volumeGlass, toleranceGlass);

        System.out.printf("Bottle Volume: (%.4f\u00B1%.4f) Liter.\n", inexactBottle.getX(), inexactBottle.getDx());
        System.out.printf("Glass Volume : (%.4f\u00B1%.4f) Liter.\n", inexactGlass.getX(), inexactGlass.getDx());
        InexactNumber ausschank = inexactBottle.div(inexactGlass);
        System.out.println("She fills: (" + ausschank.toString() + ") glasses.");
        double anzahlMin = ausschank.getMin();
        double anzahlMax = ausschank.getMax();
        double restMin = (anzahlMin % 1) * (inexactGlass.getMin()); // TODO
        double restMax = (anzahlMax % 1) * (inexactGlass.getMax()); // TODO
        System.out.println();
        System.out.println("1) She fills at least " + Math.floor(anzahlMin) + " glasses, then the residual is " + restMin + " Liter."); // 0,xxx20
        System.out.println("2) She fills at most  " + Math.floor(ausschank.getMax()) + " glasses, then the residual is " + restMax + " Liter."); // 0,xxx80
        System.out.println();

        double min = ausschank.getMin() % 1;
        double max = ausschank.getMax() % 1;
        min = Math.round(min * 100) / 100D;
        max = Math.round(max * 100) / 100D;

        System.out.println("Generell: Gläser: " + inexactGlass.toString() + " ; Ausschank: " + ausschank.toString());
        System.out.println("Min: Gläser: " + ausschank.getMin() + " ; Rest in der Flasche: " + min + " ; Rest Inhalt: " + (min * inexactGlass.getMin()) + " ; das andere: " + (min * inexactGlass.getMax()));
        System.out.println("Max: Gläser: " + ausschank.getMax() + " ; Rest in der Flasche: " + max + " ; Rest Inhalt: " + (max * inexactGlass.getMax()) + " ; das andere: " + (max * inexactGlass.getMin()));
        System.out.println();

    }

    private static void wohnung (double [] [] information, double deviation, double originalM2, double pricePerM2, double priceGes) {
        System.out.println("According landlord the flat has " + originalM2 + "m², the rent amounts " + pricePerM2 + " Euro/m².");
        System.out.println("According landlord then over-all rent results to " + priceGes + " Euro.");
        System.out.println("The measurement device has ±" + (deviation * 1000) + "mm deviation.");
        System.out.println();

        InexactNumber flat = getFlat(information, deviation);
        System.out.print("1) The flat size is according to the laser measurement device: ");
        System.out.println("(" + flat.toString() + ")m².");
        System.out.println("    Meaning she has at least " + flat.getMin() + "m² and at the maximum " + flat.getMax() + "m².");
        System.out.println();

        System.out.print("2) According to the landlord and the price per square meter, ");
        System.out.println("the flat has to be at least " + (priceGes / pricePerM2) + "m² big.");
        System.out.println();

        System.out.print("3a) Aus der vom Vermieter angegebenen Gesamtmiete und der gemessenen ");
        System.out.println("Wohnungsgröße ergibt sich ein minimaler Quadratmeterpreis von " + ((new InexactNumber(priceGes).div(flat)).getMin()) + " Euro.");
        System.out.print("3b) Aus der vom Vermieter angegebenen Miete/m² und der gemessenen Wohnungsgröße ");
        System.out.println("ergibt sich eine maximale Gesamtmiete von "+ (flat.mult(new InexactNumber(pricePerM2)).getMax()) + " Euro.");
        System.out.println();

        System.out.print("4) Die Messungenauigkeit müsste mindestens x80,x578xxxxxxxxmm betragen, ");
        System.out.println("damit die angegebene Miete gerechtfertigt ist.");
        System.out.println();

        double inaccuracy = getInaccuracy(information, originalM2) * 1000;
        System.out.println("Inaccuracy: " + inaccuracy);
    }

    private static double getInaccuracy(double [] [] information, double originalM2) { // TODO
        double inaccuracy = 1;
        double tolerance = 0.000001;
        double erg = originalM2 - getFlat(information, inaccuracy).getMax();
        while (Math.abs(erg) >= tolerance) {
                if (erg < tolerance) {
                    inaccuracy /= 10;
                } else if (erg > tolerance) {
                    inaccuracy *= 2;
                }
                erg = originalM2 - (getFlat(information, inaccuracy)).getMax();
            }
        return inaccuracy;
    }

    private static InexactNumber getFlat (double [] [] information, double deviation) {
        InexactNumber flat = new InexactNumber();
        for (double room []: information) {
            flat = flat.add(getRoom(room [0], room [1], deviation));
        }
        return flat;
    }

    private static InexactNumber getRoom (double a, double b, double tolerance) {
        return new InexactNumber(a, tolerance).mult(new InexactNumber(b, tolerance));
    }
}
