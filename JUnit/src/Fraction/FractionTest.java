/**
 * Diese Klasse dient zum Testen der Bruch-Klasse (Fraction)
 */

package Fraction;

public class FractionTest {
	public static void main(String[] args) {
        System.out.println(new Fraction()); // = 0 / 1 = 0,
        System.out.println(new Fraction(17)); // = 17 / 1 = 17,
        System.out.println(new Fraction(20, -12)); // = −5 / 3

        System.out.println("1/2 + 1/6 = 2/3 = " + new Fraction(1,2).add(new Fraction(1,6))); // 1 / 2 + 1 / 6 = 2 / 3
        System.out.println("1/2 - 1/6 = 1/3 = " + new Fraction(1,2).sub(new Fraction(1,6))); // 1 / 2 − 1 / 6 = 1 / 3
        System.out.println("6/5 * 10/8 = 3/2 = " + new Fraction(6,5).mult(new Fraction(10,8))); // 6 / 5 · 10 / 8 = 3 / 2
        System.out.println("16/3 : 4/9 = 12 = " + new Fraction(16,3).div(new Fraction(4,9))); // 16 / 3 : 4 / 9 = 12
        System.out.println("(1/2 + 1/3 ) · −6/5 = −1 = " + (new Fraction(1,2).add(new Fraction(1,3)).mult(new Fraction(-6,5)))); // ( 1 / 2 + 1 / 3 ) · − 6 / 5 = − 1
        System.out.println("23/14 % 3/7 = 5/14 = " + new Fraction(23,14).modulo(new Fraction(3,7))); // 23 / 14 % 3 / 7 = 5 / 14
        // , da ( 23 / 14 ) % ( 3 / 7 ) = ( ( 23 · 7 ) / ( 14 · 7 ) ) % ( ( 3 · 14 ) / ( 14 · 7 ) ) =
        // ( 161 % 42 ) / ( 14 · 7 ) = 35 / ( 14 · 7 ) = ( 5 · 7 ) / ( 14 · 7 ) = 514

        System.out.println("1/8 = 0.125 = " + new Fraction(1,8).toDouble());

        //System.out.println("1,625 = 13/8 = " +  new Fraction(1.625)); // 1,625 = 13 / 8,
        System.out.println("0,033333333 = 1/30 = " + new Fraction(0.033333333));
        System.out.println("0,333333333 = 1/3 = " + new Fraction(0.333333333));
        System.out.println("0,066666666 = 2/30 = " + new Fraction(0.066666666));
        System.out.println("1,433333333 = 43/30 = " + new Fraction(1.433333333));
        System.out.println("0,5078125 = 65 / 128, = " + new Fraction(0.5078125));
        System.out.println("0,9463043212890625 = 62017 / 65536, = " + new Fraction(0.9463043212890625));
        //System.out.println("3,1415926 = 6588397 / 2097152 ≈ 3 . 141592502593994 = " + new Fraction(3.1415926));
	}
}
