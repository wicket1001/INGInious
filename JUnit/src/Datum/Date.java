package Datum;

/**
 * The worst date class, why should I program it Java does that better.
 *
 * year The year.
 *
 */
class Date {
    private int year;
    private int month;
    private int day;

    private static final String [] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private static final int [] lastDayInMonths = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private static final String [] weekNames = {"Sunday", "Monday", "Thursday", "Wednesday", "Tuesday", "Friday", "Saturday"};

    /**
     * The default constructor.
     *
     * Year = 1900.
     * Month = 0. // January
     * Day = 1.
     */
    Date () {
        this (1, 1, 1900);
    }

    /**
     * The constructor with parameters.
     *
     * @param month The month of the date.
     * @param day The day of the date.
     * @param year The year of the date.
     */
    Date(int month, int day, int year) {
        setDate(month, day, year);
    }

    /**
     * Set the date to a spezific date. First it is checked if the date is possible.
     *
     * @param month The month of the date.
     * @param day The day of the date.
     * @param year The year of the date.
     */
    void setDate (int month, int day, int year) {
        this.year = year;
        month --;
        if (month < 0 || month >= 12) {
            throw new IllegalArgumentException ("The month is not correct!");
        } else {
            this.month = month;
        }
        if (day < 1 || day > lastDayInMonth()) {
            throw new IllegalArgumentException ("The day is not correct!");
        } else {
            this.day = day;
        }
    }

    @Override
    public String toString() {
        return "[ " + monthNames[month] + " " + day + ", " + year + " ]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Date date = (Date) o;

        if (year != date.year) return false;
        if (month != date.month) return false;
        return day == date.day;
    }

    @Override
    public int hashCode() {
        int result = year;
        result = 31 * result + month;
        result = 31 * result + day;
        return result;
    }

    /**
     * Find out if the year is a leap year.
     *
     * @return The status of the year.
     */
    boolean isLeapYear () {
        return  (year % 400 == 0 || year % 4 == 0 && !(year % 100 == 0));
    }

    /**
     * Find out the last day in the month.
     *
     * @return The last day in month.
     */
    int lastDayInMonth() {
        if (month == 1) {
            if (isLeapYear()) {
                return 29;
            } else {
                return 28;
            }
        } else {
            return lastDayInMonths [month];
        }
    }

    /**
     * Increases the day by one.
     */
    void inc () {
        inc(1);
    }

    /**
     * Increases the day by days.
     * It is possible that the month or the year increases.
     *
     * @param days The number of days to increase.
     */
    void inc (int days) {
        while (true) {
            if (days == 0) {
                break;
            }
            if (days + day > lastDayInMonth()) {
                days = days - lastDayInMonth();
                if (month >= 11) {
                    month = 0;
                    year ++;
                } else {
                    month ++;
                }
            } else {
                day += days;
                days = 0;
            }
        }
    }

    /**
     * Gets the day of the year beginning with January 1.
     *
     * @return The day of the year.
     */
    int dayOfYear () {
        int dOY = 0;
        int m = month;
        month--;
        for (; month >= 0; month--) {
            dOY += lastDayInMonth();
        }
        month = m;
        return dOY + day;
    }

    /**
     * Gets the day of the week.
     * 0 = "Sunday"
     * 1 = "Monday"
     * 2 = "Thursday"
     * 3 = "Wednesday"
     * 4 = "Tuesday"
     * 5 = "Friday"
     * 6 = "Saturday"
     *
     * @return The number of the day of the week.
     */
    int dayOfWeek () {
        int magic = 3; // mi
        Date defaultDate = new Date();
        for (; this.equals(defaultDate); magic++) {
            defaultDate.inc();
        }
        return magic % 7;
    }

    /**
     * Gets the Name of the day of the week. Look dayOfWeek for further description.
     *
     * @param day The number of the day of the week.
     * @return The name of the day of the week.
     */
    String getNameofWeek (int day) {
        return weekNames[day % 7];
    }
}
