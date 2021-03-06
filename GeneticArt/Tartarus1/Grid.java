package Tartarus1;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.awt.image.BufferedImage;
import java.util.*;
import java.io.*;

public class Grid {

    public ArrayList<Integer> colors;
    public ArrayList<Color> discreteColors;
    private int imageDimension;

    //functions and terminals
    public static final int INC = 0;
    public static final int ABS = 1;
    public static final int ADD = 2;
    public static final int SUBTR = 3;
    public static final int MULT = 4;
    public static final int DIV = 5;
    public static final int MAX = 6;
    public static final int AVG2 = 7;
    public static final int AVG3 = 8;
    public static final int IF = 9;
    public static final int X = 10;
    public static final int Y = 11;
    public static final int NORTH = 12;
    public static final int WEST = 13;
    public static final int NORTH_WEST = 14;
    public static final int RANDOM = 15;
    public static final int ZERO = 16;
    public static final int ONE = 17;
    public static final int TWO = 18;
    public static final int THREE = 19;
    public static final int FOUR = 20;
    public static final int FIVE = 21;
    public static final int SIX = 22;
    public static final int SEVEN = 23;
    public static final int EIGHT = 24;
    public static final int NINE = 25;
    public static final int TEN = 26;
    public static final int ELEVEN = 27;
    public static final int SIN = 28;
    public static final int COS = 29;

    public enum DiscreteColor {
        RED, ORANGE, YELLOW, LIME, GREEN, SEA_GREEN, LIGHT_BLUE, MEDIUM_BLUE, BLUE, PURPLE, MAGENTA, PINK
    }

    public enum EvalTypes {
        HUMAN, GRADIENT, STDEV, RAINBOW, SQUARES, X, INVERSE_X, SIM_NEIGHBOR, RANDO, WEIGHTED, BLUE_GREEN, SYMMETRY
    }

    public static final int numColors = DiscreteColor.values().length;

    public Grid(int imageDimension) {
        this.imageDimension = imageDimension;
        colors = new ArrayList<>();
        discreteColors = new ArrayList<>();
        discreteColors.add(Color.rgb(255, 0, 0));
        discreteColors.add(Color.rgb(255, 127, 0));
        discreteColors.add(Color.rgb(255, 255, 0));
        discreteColors.add(Color.rgb(127, 255, 0));
        discreteColors.add(Color.rgb(0, 255, 0));
        discreteColors.add(Color.rgb(0, 255, 127));
        discreteColors.add(Color.rgb(0, 255, 255));
        discreteColors.add(Color.rgb(0, 127, 255));
        discreteColors.add(Color.rgb(0, 0, 255));
        discreteColors.add(Color.rgb(127, 0, 255));
        discreteColors.add(Color.rgb(255, 0, 255));
        discreteColors.add(Color.rgb(255, 0, 127));
    }

    public void colorPixel(int color) {
        colorPixel(color, null);
    }

    public void colorPixel(int color, BufferedWriter out) {
        colors.add(color);
        if(out != null) updateFile(out, color);
    }

    private int weightedEval() {
        int fitness = imageDimension * imageDimension;
        int grads = 0;
        int equal = 0;
        for (int i = 0; i < imageDimension * imageDimension; i++) {
            if (isGradient(i)) {
                fitness--;
                grads++;
            } else if ((i > 0 && ((int) colors.get(i)) == ((int) colors.get(i - 1))) ||
                    (i < imageDimension * imageDimension - 1 && ((int) colors.get(i)) == ((int) colors.get(i + 1))) //||
                    //(i >= imageDimension && ((int)colors.get(i)) == ((int)colors.get(i - imageDimension))) ||
                    /*(i < imageDimension * imageDimension - imageDimension - 1 && ((int)colors.get(i)) == ((int)colors.get(i + imageDimension)))*/) {
                fitness--;
                equal++;
            }
        }
        if(Math.abs(grads - equal) > 200) fitness = imageDimension * imageDimension;
        if(calcStdDev() < 0.5) fitness = imageDimension * imageDimension;
        return fitness;
    }

    private double calcStdDev() {
        double mean = 0.0;
        for(int i = 0; i < imageDimension * imageDimension; i++) {
            mean += colors.get(i);
        }
        if(mean != 0.0) mean /= (imageDimension * imageDimension);
        double stdev = 0;
        for(int i = 0; i < imageDimension * imageDimension; i++) {
            stdev += Math.pow(colors.get(i) - mean, 2.0);
        }
        stdev /= numColors;
        stdev = Math.sqrt(stdev);
        return stdev;
    }

    private int humanEval() {
        Platform.runLater(() -> {
            WritableImage image = new WritableImage(imageDimension, imageDimension);
            PixelWriter p = image.getPixelWriter();
            for(int x = 0; x < imageDimension; x++) {
                for(int y = 0; y < imageDimension; y++) {
                    p.setColor(x, y, discreteColors.get(colors.get(imageDimension * y + x)));
                }
            }
            ImageView imageView = new ImageView(image);
            Main.rootPane.getChildren().clear();
            Main.rootPane.getChildren().add(imageView);
        });
        return getFitnessFromUser();
    }

    private boolean isGradient(int i) {
        int northIndex = -1;
        int northEastIndex = -1;
        int eastIndex = -1;
        int southEastIndex = -1;
        int southIndex = -1;
        int southWestIndex = -1;
        int westIndex = -1;
        int northWestIndex = -1;
        if(i >= imageDimension) {
            northIndex = i - imageDimension;
        }
        if(i <= imageDimension * imageDimension - imageDimension - 1) {
            southIndex = i + imageDimension;
        }
        if((i + 1) % imageDimension != 0) {
            eastIndex = i + 1;
        }
        if(i % imageDimension != 0) {
            westIndex = i - 1;
        }
        if(northIndex != -1 && eastIndex != -1) {
            northEastIndex = northIndex + 1;
        }
        if(northIndex != -1 && westIndex != -1) {
            northWestIndex = northIndex - 1;
        }
        if(southIndex != -1 && eastIndex != -1) {
            southEastIndex = southIndex + 1;
        }
        if(southIndex != -1 && westIndex != -1) {
            southWestIndex = southIndex - 1;
        }
        int cur = colors.get(i);
        int north = 1;
        if(northIndex != -1) north = Math.abs(colors.get(northIndex) - cur);
        int northEast = 1;
        if(northEastIndex != -1) northEast = Math.abs(colors.get(northEastIndex) - cur);
        int east = 1;
        if(eastIndex != -1) east = Math.abs(colors.get(eastIndex) - cur);
        int southEast = 1;
        if(southEastIndex != -1) southEast = Math.abs(colors.get(southEastIndex) - cur);
        int south = 1;
        if(southIndex != -1) south= Math.abs(colors.get(southIndex) - cur);
        int southWest = 1;
        if(southWestIndex != -1) southWest = Math.abs(colors.get(southWestIndex) - cur);
        int west = 1;
        if(westIndex != -1) west = Math.abs(colors.get(westIndex) - cur);
        int northWest = 1;
        if(northWestIndex != -1) northWest = Math.abs(colors.get(northWestIndex) - cur);
        return north >= 1 && north < 4 && northEast >= 1 && northEast < 4 && east >= 1 && east < 4 &&
                southEast >= 1 && southEast < 4 && south >= 1 && south < 4 && southWest >= 1 && southWest < 4 &&
                west >= 1 && west < 4 && northWest >= 1 && northWest < 4;
    }

    private int gradientEval() {
        int fitness = imageDimension * imageDimension;
        for(int i = 0; i < imageDimension * imageDimension; i++) {
            if(isGradient(i)) fitness--;
        }
        return fitness;
    }

    private int stdevEval() {
        double stdev = calcStdDev();
        return imageDimension * imageDimension - (int)Math.floor(stdev);
    }

    private int rainbowEval() {
        int fitness = imageDimension * imageDimension;
        for(int i = 0; i < imageDimension * imageDimension; i++) {
            if(colors.get(i) == i / imageDimension) fitness--;
        }
        return fitness;
    }

    private int squareEval() {
        int fitness = imageDimension * imageDimension;
        for(int i = 2; i < imageDimension * imageDimension; i++) {
            if(colors.get(i) == colors.get(i - 2) && colors.get(i) != colors.get(i - 1)) fitness--;
        }
        return fitness;
    }

    private int xEVal() {
        int fitness = imageDimension * imageDimension;
        for(int i = 0; i < imageDimension * imageDimension; i++) {
            if(colors.get(i) == (i % imageDimension) % numColors) fitness--;
        }
        return fitness;
    }

    private int inverseXEval() {
        int fitness = imageDimension * imageDimension;
        for(int i = 1; i < imageDimension * imageDimension; i++) {
            if(colors.get(i) < colors.get(i - 1)) fitness--;
        }
        return fitness;
    }

    private int simNeighborsEval() {
        int fitness = imageDimension * imageDimension;
        for(int x = 1; x < imageDimension - 1; x++) {
            for(int y = 1; y < imageDimension - 1; y++) {
                int cur = colors.get(x * y + x);
                int dif1 = colors.get(imageDimension * y + (x - 1)) - cur;
                int dif2 = colors.get(imageDimension * (y - 1) + (x - 1)) - cur;
                int dif3 = colors.get(imageDimension * (y + 1) + (x + 1)) - cur;
                int dif4 = colors.get(imageDimension * y + (x + 1)) - cur;
                if(dif1 > 0 && dif1 < 4 && dif2 > 0 && dif2 < 4 && dif3 > 0 && dif3 < 4 && dif4 > 0 && dif4 < 4) fitness--;
            }
        }
        return fitness;
    }

    private int randoEval() {
        int neighborsFit = simNeighborsEval();
        int gradFit = gradientEval();
        int squareFit = squareEval();
        int rainbowFit = rainbowEval();
        int avgFit = (neighborsFit + gradFit + squareFit + rainbowFit) / 4;
        return avgFit;
    }

    private int blueGreenEval() {
        int fitness = imageDimension * imageDimension * 3;
        for(int x = 0; x < imageDimension; x++) {
            for(int y = 0; y < imageDimension; y++) {
                if (colors.get(imageDimension * y + x) >= 4 &&
                        colors.get(imageDimension * y + x) <= 8) {
                    fitness--;
                }
            }
        }
        double stdev = calcStdDev();
        fitness = (int) Math.floor(fitness - stdev * 50);
        int gradFit = gradientEval();
        fitness = fitness - gradFit;

        return fitness;
    }

    private int symmetryEval() {
        int fitness = imageDimension * imageDimension;
        int xMirror;
        int yMirror;
        for(int x = 0; x < imageDimension; x++) {
            for (int y = 0; y < imageDimension; y++) {
                xMirror = imageDimension - x - 1;
                yMirror = imageDimension - y - 1;
                if (colors.get(imageDimension * y + x) == colors.get(imageDimension * yMirror + xMirror)) {
                    fitness--;
                }
            }
        }
        double standardDev = calcStdDev();
        if (standardDev < 2) {
            return imageDimension * imageDimension;
        }
        return fitness;
    }

    // determine the fitness of the current state of the grid. fitness is (maxScore+1) - score
    // where score is the number of sides of blocks that are touching a wall
    public int calcFitness(EvalTypes evalType) {
        switch (evalType) {
            case HUMAN:
                return humanEval();
            case GRADIENT:
                return gradientEval();
            case STDEV:
                return stdevEval();
            case RAINBOW:
                return rainbowEval();
            case SQUARES:
                return squareEval();
            case X:
                return xEVal();
            case INVERSE_X:
                return inverseXEval();
            case SIM_NEIGHBOR:
                return simNeighborsEval();
            case RANDO:
                return randoEval();
            case WEIGHTED:
                return weightedEval();
            case BLUE_GREEN:
                return blueGreenEval();
            case SYMMETRY:
                return symmetryEval();
        }
        return 0;
    }

    private int getFitnessFromUser() {
        int fitness;
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("Enter a score between 1 and 10 where 1 is the best");
        try {
            fitness = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("That's not an integer... Try again");
            return getFitnessFromUser();
        }
        if(fitness < 1 || fitness > 10) {
            System.out.println("That's not between 1 and 10... Try again");
            return getFitnessFromUser();
        }
        return fitness;
    }

    public void print() {
        print(System.out);
    }

    // print the current state of the grid, showing blocks and the dozer 
    // pointing in the correct direction
    public void print(PrintStream os) {
        for(int x = 0; x < imageDimension; x++) {
            for(int y = 0; y < imageDimension; y++) {
                os.print(colors.get(x * y + x) + " ");
            }
            os.println();
        }
    }

    private void updateFile(BufferedWriter out, int color) {
        try{
            out.write(color + " ");
        } catch (IOException e) {
            System.out.println("Error while writing to file in update method");
        }
    }

    public void outputFitness(BufferedWriter out, int gridFitness) {
        try{
            out.write("\n");
        } catch (IOException e) {
            System.out.println("Error while writing to file in fitness method");
        }
    }
}
