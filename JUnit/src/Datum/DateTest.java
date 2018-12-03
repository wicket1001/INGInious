package Datum;

public class DateTest {

	public static void main(String[] args) {

		// Do NOT use any of the java time/date functions!
		// Start with all lines commented: 
		//     mark lines and "add block comment": ctrl-/ aka ctrl-shift-7
		// Loop: 
		//     Uncomment one line and make it work
		// IntelliJ is your friend: "quick fix"

		Date d1 = new Date(); // defaults to January 1, 1900
		// Output is in "English" -- d1 is January 1, 1900
		// Hint: static final String[] monthNames = ...
		System.out.println("d1 is " + d1 + " [ January 1, 1900 ]"); // always: [ correct solution ]
		
		System.out.println("d1.isLeapYear() is " + d1.isLeapYear() + " [ false ]");
		System.out.println("d1.lastDayInMonth() is " + d1.lastDayInMonth() + " [ 31 ]");

		// Mind the order of the parameters!
		Date d2 = getDate(12, 27, 2015); // December 27, 2015
		System.out.println("\nd2 is " + d2 + " [ December 27, 2015 ]");
        Date d3 = getDate(0, 99, 8045);
        System.out.println("d3 is " + d3 + " [ January 1, 1900 ]");

		d3.setDate(2, 28, 2012); // same as Date() - try use to use
									// Refactor/Extract Method
		System.out.println("\nd3 is " + d3 + " [ February 28, 2012 ]");
		System.out.println("d3.lastDayInMonth() is " + d3.lastDayInMonth() + " [ 29 ]");

		d3.inc(); // increment to next day (changes d3)
		System.out.println("d3.inc() is " + d3 + " (leap year allows 29th)" + " [ February 29, 2012 ]");

		d2.inc(7); // increment 7 days (changes d2)
		System.out.println("\nd2.inc(7) is " + d2 + " [ January 3, 2016 ]");

		System.out.println("\nd1 = " + d1 + " [ January 1, 1900 ]");
		d1.inc(42124);
		System.out.println("   + 42124 days is " + d1 + " [ May 2, 2015 ]"); // how to check the
														// result?

		Date d4 = getDate(2, 29, 2012);
		if (d4.equals(d3)) {
			System.out.println("d4 equals d3" + " [ OK ]");
		} else {
			System.out.println("d4 equals not d3"  + " [ FAIL ]");
		}

		// Special cases
		Date s1 = getDate(0, 2, 2000); // bad
		System.out.println("\ns1 = " + s1 + " [ January 1, 1900 ]");
		Date s2 = getDate(13, 2, 2000); // bad
		System.out.println("s2 = " + s2 + " [ January 1, 1900 ]");
		Date s3 = getDate(2, 0, 2000); // bad
		System.out.println("s3 = " + s3 + " [ January 1, 1900 ]");
		Date s4 = getDate(32, 3, 2000); // bad
		System.out.println("s4 = " + s4 + " [ January 1, 1900 ]");
		Date s5 = getDate(2, 29, 1900); // bad
		System.out.println("s5 = " + s5 + " [ January 1, 1900 ]");
		Date s6 = getDate(2, 29, 2000); // ok
		System.out.println("s6 = " + s6 + " [ February 29, 2000 ]");
		// Bonus:
		System.out.println("d4 - Day of year = " + d4.dayOfYear() + " [ 60 ]"); //  count from  January 1
		System.out.println("d4 - Day of week = " + d4.dayOfWeek() + " [ 3 ]"); // 0-Sunday, 1-Monday,...,6-Saturday
				// Hint: calculate (magicNumber + days since January 1, 1900) % 7
		
		Date today = getDate(11, 16, 2016);
		System.out.println("\ntoday = " + today);
		System.out.println("today - Day of year = " + today.dayOfYear());
		System.out.println("today - Day of week = " + today.getNameofWeek(today.dayOfWeek()));
		/**/
	}

    /**
     * Catches the exceptions of the Date class.
     *
     * @param month The month of the date.
     * @param day The day of the date.
     * @param year The year of the date.
     * @return The Date that can be default.
     */
	protected static Date getDate(int month, int day, int year) {
        try {
            return new Date(month, day, year); // invalid date -> January 1, 1900
        } catch (IllegalArgumentException e) {
            System.out.println(e.toString());
            return new Date();
        }
    }
}
