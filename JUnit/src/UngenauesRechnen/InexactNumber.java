package UngenauesRechnen;

class InexactNumber {
    private double x = 0;
    private double dx = 0;

    InexactNumber () {  }

    InexactNumber (double x) {
        this.x = x;
    }

    InexactNumber (double x, double dx) {
        this.x = x;
        this.dx = dx;
    }

    public double getX() {
        return x;
    }

    public double getDx() {
        return dx;
    }

    public double getMax() {
        return x + dx;
    }

    public double getMin() {
        return x - dx;
    }

    @Override
    public String toString() {
        return x + "\u00B1" + dx;// return String.format("%.4f", x) + "\u00B1" + String.format("%.4f", dx);
    }

    InexactNumber add (InexactNumber other) {
        return new InexactNumber(x + other.x, dx + other.dx); // (x1±dx1)+(x2±dx2)=(x1+x2)±(dx1+dx2)
    }

    InexactNumber sub (InexactNumber other) {
        return new InexactNumber(x - other.x, dx + other.dx); // (x1±dx1)−(x2±dx2)=(x1−x2)±(dx1+dx2)
    }

    InexactNumber mult (InexactNumber other) {
        return new InexactNumber(x * other.x, Math.abs(x * other.dx) + Math.abs(other.x * dx)); // (x1±dx1)·(x2±dx2)=(x1·x2)±(|x1·dx2|+|x2·dx1|)
    }

    InexactNumber div (InexactNumber other) {
        return new InexactNumber(x / other.x, this.mult(other).dx / (other.x * other.x)); // (x1±dx1) / (x2±dx2) = (x1/x2) ± (|x1·dx2|+|x2·dx1|) / (x2^2)
    }

}
