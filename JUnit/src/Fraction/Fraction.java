package Fraction;

import java.util.ArrayList;

/**
 * Die Bruch-Klasse bietet die vier Grundrechnungsarten und noch einige
 * Operatoren mehr zum Rechnen mit Bruechen.
 * 
 * @author DI Franz Breunig und DI August Hoerandl, HTL, Rennweg, November 2015
 */
public class Fraction {
	/** das Attribut (= Eigneschaft) Zaehler des Bruchs */
	private int numerator;   // Alle Attribute werden automatisch mit 0 initialisiert.
	
	/** das Attribut (= Eigneschaft)  Nenner des Bruchs */
	private int denominator; // Wird automatisch mit 0 initialisiert
	
	
	/**
	 * Der Standard-Konstruktor initialisiert den Bruch mit dem Wert 0/1 = Null.
	 * Hinweis: Falls ueberhaupt kein Konstruktor geschrieben wird, 
	 *          dann existiert dieser Konstruktor automatisch.
	 */
	public Fraction() {
		this.denominator = 1;
	}
	
	
	/**
	 * Der Konstruktor initialisiert den Bruch als ganze Zahl.
	 * 
	 * @param numerator Zaehler
	 */
	public Fraction (int numerator) {
		this.numerator   = numerator;
		this.denominator = 1;
	}
	
	
	/**
	 * Der Konstruktor initialisiert den Bruch mit dem Zaehler und Nenner.<br>
	 * Der Bruch wird dabei automatisch gekuerzt.
	 * 
	 * @param numerator   der Zaehler
	 * @param denominator der Nenner
	 * @throws ArithmeticException / by zero, falls der Nenner == 0 ist
	 */
	public Fraction (long numerator, long denominator) {
		set(numerator, denominator);
	}
	
	public Fraction (double f) {

		numerator = 0;
		denominator = 1;
		while ( f - (double)numerator/denominator >= 0.0000001) {
			denominator *= 2;
			numerator = (int) (f * denominator);
		}

		/*
		ArrayList<Integer> add = new ArrayList<>();
        while (true) {
            add.add((int)f);
            f = f - add.get(add.size() - 1);
            if ((int)(f * 10000) == (int)f) {
                break;
            }
            f = ((double)(1) / f);

		}
        Fraction frac = new Fraction(1, add.get(add.size() - 1));
        for (int i = 0; i < add.size() - 1; i++) {
            Fraction temp = new Fraction(add.get(add.size() - 2 - i));
            frac = new Fraction(1).div(frac);
            frac = temp.add(frac);
        }
        numerator = frac.numerator;
        denominator = frac.denominator;
        */
    }

	@Override
	public String toString() {
		if (denominator == 1) {
			return Integer.toString(numerator);
		}
		
		return numerator + "/" + denominator;
	}
	
	
	/**
	 * Liefert den Zaehler des Bruchs.
	 * 
	 * @return Zaehler ({@link #numerator})
	 * Hinweis: Eingabe des Links: #num + Auswahl von "{@link #numerator}"
	 * {@link #numerator}
	 */
	public int getNumerator() {
		return numerator;
	}
	
	
	/**
	 * Liefer den Nenner des Bruchs.
	 * 
	 * @return Nenner ({@link #denominator})
	 */
	public int getDenominator() {
		return denominator;
	}
	
	
	/**
	 * Setzt den Zaehler ({@link #numerator}) des Bruchs und kuerzt den Bruch.
	 * 
	 * @param numerator Zaehler
	 */
	public void setNumerator(int numerator) {
		set(numerator, denominator);
	}
	
	
	/**
	 * Setzt den Nenner ({@link #denominator}) des Bruchs und kuerzt den Bruch
	 * @param denominator Nenner
	 * @throws ArithmeticException / by zero, falls der Nenner == 0 ist
	 */
	public void setDenominator(int denominator) {
		set(numerator, denominator);
	}
	
	
	/**
	 * Setzt den Zaehler ({@link #numerator}) und Nenner ({@link #denominator}) des Bruchs und kuerzt den Bruch.<br>
	 * Tipp: eine ArithmeticException / by zero loest du am leichtesten aus mit: "numerator = numerator / denominator;"
	 * 
	 * @param numerator   Zaehler
	 * @param denominator Nenner
	 * @throws ArithmeticException / by zero, falls der Nenner == 0 ist
	 */
	public void set(long numerator, long denominator) {
		// TO Loese eine "ArithmeticException / by zero"-Exception aus, falls der Nenner == 0 ist
		if (denominator == 0) {
			throw new ArithmeticException("Du Depp hast durch null geteilt (Ich habe keine Ahnung von Rehtschreibung , Beistrichsetzung oder Grammatik))");
		}
		
	    long gcd = gcd(numerator, denominator);
	    
	    if (denominator > 0) {
	        this.numerator   = (int)(numerator     / gcd);
	        this.denominator = (int)(denominator   / gcd);
	    } else {
	        this.numerator   = - (int)(numerator   / gcd);
	        this.denominator = - (int)(denominator / gcd);
	    }
	}
	
	
	/**
	 * GCD = Greatest Common Devisor = groeszter gemeinsamer Teiler (ggT)
	 * 
	 * @param a erste Zahl
	 * @param b zweite Zahl
	 * @return der groeszte gemeinsame Teiler von a und b
	 */
	public static int gcd(long a, long b) {
	    // TO ergaenze den Code (sieh Euklidischer Algorithmus zum bestimmen
		//      des ggT: Pseudo-Code Wikipedia (Iterative Variante):
		//      https://de.wikipedia.org/wiki/Euklidischer_Algorithmus#Beschreibung_durch_Pseudocode_2
		// Beachte: Der Algorithmus funktioniert nur mit positiven Zahlen,
		//          du kannst eine Zahl positiv machen mit: "zahl = Math.abs(zahl);"

		/*
		if (b == 0) {
			return (int)a;
		} else {
			return gcd(b, a % b);
		}
		*/
		a = Math.abs(a);
		b = Math.abs(b);
		while (b != 0) {
			long h = a % b;
			a = b;
			b = h;
		}
		return (int)a;
	}

	
	/**
	 * Addition this + f
	 * 
	 * @param f der rechte Operand der Addition
	 * @return das Ergebnis der Addition
	 */
	public Fraction add(Fraction f) {
		return new Fraction(numerator * (long)f.denominator  +  f.numerator * (long)denominator,
				denominator * (long)f.denominator);
	}

	public Fraction sub (Fraction f) {
		//return new Fraction(numerator * (long)f.denominator  -  f.numerator * (long)denominator,
		//		denominator * (long)f.denominator);
        return new Fraction(numerator, denominator).add(f.minus());
	}

    public Fraction mult (Fraction f) {
        return new Fraction((long)numerator * f.numerator,
                denominator * (long)f.denominator);
    }

    public Fraction div (Fraction f) {
        return f.reciprocal().mult(this);
    }

    public Fraction reciprocal () {
        return new Fraction(denominator, numerator);
    }

    public Fraction minus () {
        return new Fraction (numerator * -1, denominator);
    }

    public Fraction modulo (Fraction f) {
        return new Fraction((numerator * (long)f.denominator)  %  (f.numerator * (long)denominator),
                denominator * (long)f.denominator);
    }

    public double toDouble () {
        return (double)numerator / (double)denominator;
    }
}