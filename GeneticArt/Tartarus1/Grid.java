package Tartarus1;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.io.*;

public class Grid {

    public ArrayList<Integer> colors;
    private int imageDimension;

    //functions and terminals
    public final static int INC = 0;
    public final static int ADD = 1;
    public final static int MAX = 2;
    public final static int X = 3;
    public static final int Y = 4;
    public static final int NORTH = 5;
    public static final int WEST = 6;
    public static final int NORTH_WEST = 7;
    public static final int RANDOM = 8;


    public enum DiscreteColor {
        RED, ORANGE, YELLOW, LIME, GREEN, SEA_GREEN, LIGHT_BLUE, MEDIUM_BLUE, BLUE, PURPLE, MAGENTA, PINK
    }

    public static final int numColors = DiscreteColor.values().length;

    public Grid(int imageDimension) {
        this.imageDimension = imageDimension;
        colors = new ArrayList<>();
    }

    public void colorPixel(int color) {
        colorPixel(color, null);
    }

    public void colorPixel(int color, BufferedWriter out) {
        colors.add(color);
        //System.out.println(color);
        if(out != null) updateFile(out, color);
    }

    // determine the fitness of the current state of the grid. fitness is (maxScore+1) - score
    // where score is the number of sides of blocks that are touching a wall
    public int calcFitness() {
        BufferedWriter out;
        try {
            Path path = Paths.get("evaluate.txt");
            Files.deleteIfExists(path);
        } catch (IOException e) {
            System.out.println("Error deleting evaluate.txt");
            e.printStackTrace();
        }
        try{
            //sets it to append mode
            out = new BufferedWriter(new FileWriter("evaluate.txt", true));
            for(int color : colors) out.write(color + " ");
            out.flush();
            out.close();
        }catch(IOException exception){
            System.out.println("Error writing to evaluate.txt");
        }
        ProcessBuilder pb = new ProcessBuilder("./drawer", "<", "evaluate.txt");
        pb.directory(new File("../../"));
        File log = new File("log");
        pb.redirectErrorStream(true);
        pb.redirectOutput(ProcessBuilder.Redirect.appendTo(log));
        try {
            pb.start();
        } catch (IOException e) {
            System.out.println("Error starting process");
            e.printStackTrace();
        }
        return getFitnessFromUser();
    }

    private int getFitnessFromUser() {
        int fitness;
        Scanner scanner = new Scanner(System.in);
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
