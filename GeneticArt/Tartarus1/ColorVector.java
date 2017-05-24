package Tartarus1;

public class ColorVector {

    public double[] rgbColor;

    public ColorVector(double r, double g, double b) {
        rgbColor = new double[3];
        rgbColor[0] = r;
        rgbColor[1] = g;
        rgbColor[2] = b;
    }

    public static ColorVector greater(ColorVector c1, ColorVector c2) {
        double c1Sum = 0;
        double c2Sum = 0;
        for(int dude = 0; dude < 3; dude++) {
            c1Sum += c1.rgbColor[dude];
            c2Sum += c2.rgbColor[dude];
        }
        if(c1Sum > c2Sum) return c1;
        return c2;
    }

    public double getRed() {
        return rgbColor[0];
    }

    public double getGreen() {
        return rgbColor[1];
    }

    public double getBlue() {
        return rgbColor[2];
    }

    public void add(int i) {
        for(int dude = 0; dude < 3; dude++) {
            rgbColor[dude] += i;
        }
    }

    public void abs() {
        for(int dude = 0; dude < 3; dude++) {
            rgbColor[dude] = Math.abs(rgbColor[dude]);
        }
    }

    public void addColor(ColorVector c) {
        for(int dude = 0; dude < 3; dude++) {
            rgbColor[dude] += c.rgbColor[dude];
        }
    }

    public void subtrColor(ColorVector c) {
        for(int dude = 0; dude < 3; dude++) {
            rgbColor[dude] -= c.rgbColor[dude];
        }
    }

    public void multColor(ColorVector c) {
        for(int dude = 0; dude < 3; dude++) {
            rgbColor[dude] *= c.rgbColor[dude];
        }
    }

    public void divColor(ColorVector c) {
        double temp;
        for(int dude = 0; dude < 3; dude++) {
            temp = c.rgbColor[dude];
            if (temp != 0) {
                rgbColor[dude] /= temp;
            }
        }
    }

    public void avgColor(ColorVector c) {
        for(int dude = 0; dude < 3; dude++) {
            rgbColor[dude] = (rgbColor[dude] + c.rgbColor[dude]) / 2;
        }
    }

    public void avgColor(ColorVector c1, ColorVector c2) {
        for(int dude = 0; dude < 3; dude++) {
            rgbColor[dude] = (rgbColor[dude] + c1.rgbColor[dude] + c2.rgbColor[dude]) / 3;
        }
    }

    public double avg() {
        double avg = 0;
        for(int dude = 0; dude < 3; dude++) {
            avg += rgbColor[dude];
        }
        avg /= 3;
        return avg;
    }

    public void sin() {
        for(int dude = 0; dude < 3; dude++) {
            rgbColor[dude] = Math.sin(rgbColor[dude]);
        }
    }

    public void cos() {
        for(int dude = 0; dude < 3; dude++) {
            rgbColor[dude] = Math.cos(rgbColor[dude]);
        }
    }

    public void tan() {
        for(int dude = 0; dude < 3; dude++) {
            rgbColor[dude] = Math.tan(rgbColor[dude]);
        }
    }

    public void print() {
        System.out.println(getRed() + ", " + getGreen() + ", " + getBlue());
    }
}