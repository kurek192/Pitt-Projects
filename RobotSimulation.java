
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;

public class RobotSimulation {

    static int height, width;
    static int harvest, simulations;
    static final int D_COST = 14; //diagonol cost for shortest path algorithm
    static final int VH_COST = 10;//vertical/horizontal cost for shortest path algorithm 
    static int startX, startY;//start y for shortest path algorithm
    static int endX, endY;//start x for shortest path algorithm
    static double harvestAverage, time;

    static Robot robot = new Robot();
    static ArrayList<Map> list;
    static ArrayList<Coordinate> cells = new ArrayList<Coordinate>();
    static Coordinate[][] theMap = new Coordinate[5][5];
    static double[] results;
    static PriorityQueue<Coordinate> open;
    static boolean closed[][];
    static Random rand = new Random();

    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("Invalid number of arguments!");
            System.exit(0);
        }

        String csvFile = args[0];
        intialize(csvFile);
        setSimulations();

        results = new double[simulations];

        System.out.print("\n");

        switch (args[1]) {
            case "1":
                System.out.println("SIMULATING ROBOT NAVIGATION STRATEGY 1 (" + simulations + " times):");
                System.out.println("Move toward the location where the material might appear (and reappear)\nwith the highest probability and ramain there for the rest of the time. ");
                for (int i = 0; i < simulations; i++) {
                    intialize(csvFile);
                    strategyOne();
                    results[i] = harvest;
                    harvestAverage += harvest;
                }
                break;
            case "2":
                System.out.println("SIMULATING ROBOT NAVIGATION STRATEGY 2 (" + simulations + " times):");
                System.out.println("Randomly choose a direction and continue until the robot bumps into a wall/obstacle. ");
                for (int i = 0; i < simulations; i++) {
                    intialize(csvFile);
                    strategyTwo();
                    results[i] = harvest;
                    harvestAverage += harvest;
                }
                break;

            case "3":
                System.out.println("SIMULATING ROBOT NAVIGATION STRATEGY 3 (" + simulations + " times):");
                System.out.println("Randomly choose a direction at every step. ");
                for (int i = 0; i < simulations; i++) {
                    intialize(csvFile);
                    strategyThree();
                    results[i] = harvest;
                    harvestAverage += harvest;
                }
                break;
            case "4":
                System.out.println("SIMULATING ROBOT NAVIGATION STRATEGY 4 (" + simulations + " times):");
                System.out.println("Always head toward a (randomly chosen) location where the material could appear. ");
                for (int i = 0; i < simulations; i++) {
                    intialize(csvFile);
                    strategyFour();
                    results[i] = harvest;
                    harvestAverage += harvest;
                }
                break;

            default:
                break;
        }

        Statistics stats = new Statistics(results);

        System.out.println("\nRESULTS: ");
        System.out.println("Average Harvest (" + simulations + " runs): " + (harvestAverage / simulations));
        System.out.println("Standard Deviation: " + stats.standardDeviation());
        System.out.println("Variance: " + stats.variance());

    }

    //set the number of desired simulations
    public static void setSimulations() {
        Scanner inScan = new Scanner(System.in);
        simulations = 0;
        boolean valid = false;
        while (valid == false) {
            try {
                System.out.println("How many times would you like to run the simulation?");
                simulations = inScan.nextInt();
                valid = true;

            } catch (Exception e) {
                System.out.println("Please enter a valid number!");
                inScan.nextLine();
            }
        }
    }

    //initialize and/or reset data structures for each run
    public static void intialize(String csvFile) {
        list = new ArrayList<Map>();
        cells = new ArrayList<Coordinate>();
        robot = new Robot();
        harvest = 0;
        height = 0;
        width = 0;
        list = generateMap(csvFile);
        time = (height * width) * .25;

    }

    //NAVIGATION STRATEGY 1
    //Utilizes the A star algorithm to determine the the shortest path to the location
    //with the highest probability.
    public static void strategyOne() {

        ArrayList<Map> temp = new ArrayList<>();
        for (Map m : list) {
            if (m.getValue().equals("#")) {
            } else if (m.getValue().equals("0")) {
            } else if (Double.parseDouble(m.getValue()) > 0 && Double.parseDouble(m.getValue()) < 1) {
                temp.add(m);
            }
        }

        Map maxProb = Collections.max(temp, new ProbabilityCompare());

        ArrayList<Map> blocked = new ArrayList<>();
        for (Map m : list) {
            if (m.getValue().equals("#")) {
                blocked.add(m);
            }
        }

        ArrayList<Coordinate> temp2 = new ArrayList<Coordinate>();
        temp2 = findShortestPath(height, width, maxProb.getSecond(), maxProb.getFirst(), 1, 1, blocked);
        int j = 0;

        for (int i = 0; i < temp2.size(); i++) {

            robot.setLocation(temp2.get(i).i, temp2.get(i).j);
            generateMaterial();
            String event = getEvent(robot.getX(), robot.getY());
            processEvent(event);
            j = i;

        }

        for (int i = j; i < time; i++) {

            generateMaterial();
            String event = getEvent(robot.getX(), robot.getY());
            processEvent(event);

        }

    }

    //NAVIGATION STRATEGY 2
    //utilizes a random int and a case and switch to determine the next random step
    public static void strategyTwo() {
        int n = rand.nextInt(8) + 1;

        for (int i = 0; i < time; i++) {

            generateMaterial();
            robot.setOld(robot.getX(), robot.getY());
            switch (n) {
                case 1:
                    robot.moveNW();
                    break;
                case 2:
                    robot.moveN();
                    break;
                case 3:
                    robot.moveNE();
                    break;
                case 4:
                    robot.moveE();
                    break;
                case 5:
                    robot.moveSE();
                    break;
                case 6:
                    robot.moveS();
                    break;
                case 7:
                    robot.moveSW();
                    break;
                case 8:
                    robot.moveW();
                    break;
            }

            String event = getEvent(robot.getX(), robot.getY());
            processEvent(event);

            if (event.equals("#")) {//if the next event is an obstacle, change direction
                n = rand.nextInt(8) + 1;
            }

        }

    }

    //NAVIGATION STRATEGY 3
    //utilizes a random int and case and switch to determine the next step
    public static void strategyThree() {

        for (int i = 0; i < time; i++) {
            generateMaterial();
            int n = rand.nextInt(8) + 1;
            robot.setOld(robot.getX(), robot.getY());
            switch (n) {
                case 1:
                    robot.moveNW();
                    break;
                case 2:
                    robot.moveN();
                    break;
                case 3:
                    robot.moveNE();
                    break;
                case 4:
                    robot.moveE();
                    break;
                case 5:
                    robot.moveSE();
                    break;
                case 6:
                    robot.moveS();
                    break;
                case 7:
                    robot.moveSW();
                    break;
                case 8:
                    robot.moveW();
                    break;
            }

            String event = getEvent(robot.getX(), robot.getY());
            processEvent(event);

        }

    }

    //NAVIGATION STRATEGY 4
    //utilizes the A star algorithm to find the shortest path between locations
    public static void strategyFour() {
        ArrayList<Map> temp = new ArrayList<>();
        for (Map m : list) {
            if (m.getValue().equals("#")) {
            } else if (m.getValue().equals("0")) {
            } else if (Double.parseDouble(m.getValue()) > 0 && Double.parseDouble(m.getValue()) < 1) {
                temp.add(m);
            }
        }

        ArrayList<Map> blocked = new ArrayList<>();
        for (Map m : list) {
            if (m.getValue().equals("#")) {
                blocked.add(m);
            }
        }
        int start = 1;
        int stop = 1;
        int i = 0;
        while (i < time) {

            int j = rand.nextInt(temp.size() - 1) + 0;
            ArrayList<Coordinate> temp2 = new ArrayList<Coordinate>();
            temp2 = findShortestPath(height, width, temp.get(j).getSecond(), temp.get(j).getFirst(), start, stop, blocked);
            stop = temp.get(j).getSecond();
            start = temp.get(j).getFirst();

            for (int k = 0; k < temp2.size(); k++) {

                if (temp2.get(k).i == temp.get(j).getFirst() && temp2.get(k).j == temp.get(j).getSecond()) {//if the location has been reached, break loop and determine next location
                    break;
                }
                robot.setLocation(temp2.get(k).j, temp2.get(k).i);
                generateMaterial();
                String event = getEvent(robot.getX(), robot.getY());
                processEvent(event);
                i++;
                if (i == time) {
                    break;

                }
            }

            temp2.clear();

        }

    }

    //determine what locations will generate material 
    //set and decrement rest values
    //determine active spaces
    public static void generateMaterial() {

        for (Map m : list) {

            if (m.getValue().equals("#")) {
            } else if ((m.getValue().equals("0")) || (Double.parseDouble(m.getValue()) > 0 && Double.parseDouble(m.getValue()) < 1)) {

                double material = Double.parseDouble(m.getValue());
                if (m.getRest()) {//if location is at reset

                    m.decrementRest();//if location is at rest, decrement
                    if (m.getRestTime() <= 0) {
                        m.setValue(m.getRestValue());
                        m.setRest(false, "null");
                    }
                }

                material = Double.parseDouble(m.getValue());
                if ((material > 0 && material < 1)) {//determine probability
                    material = material * 100;
                    int j = rand.nextInt(100) + 1;

                    if (j >= 1 && j <= material) {
                        m.setActive();//location will generate material
                    }
                }

            }
        }
    }

    //generate an ArrayList of locations from the csv input file
    public static ArrayList<Map> generateMap(String csvFile) {
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        ArrayList<String[]> matrix = new ArrayList<String[]>();
        ArrayList<Map> list = new ArrayList<Map>();

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] country = line.split(cvsSplitBy);
                matrix.add(country);

            }

        } catch (Exception e) {
            System.out.println("Invalid file!");
            System.exit(0);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }

        int j = 0;
        int i = 0;

        for (String[] result : matrix) {

            for (i = 0; i < result.length; i++) {
                Map temp = new Map(i, j, result[i]);
                list.add(temp);
            }
            j++;
        }

        width = i;
        height = j;
        return list;

    }

    //get the event at a given location
    public static String getEvent(int x, int y) {
        for (Map m : list) {
            if (m.getFirst() == robot.getX() && m.getSecond() == robot.getY()) {
                return m.getValue();
            }
        }

        return null;
    }

    //determine whether a material can be harvested
    public static void harvest(int x, int y) {

        for (Map m : list) {
            if ((m.getFirst() == robot.getX() && m.getSecond() == robot.getY()) && m.getStatus()) {//if material is active, harvest
                harvest++;
                m.setRest(true, m.getValue());//set to rest
                m.setInactive();//set to inactive
                m.setValue("0");
            }
        }
    }

    //process an event
    public static void processEvent(String event) {
        if (event.equals("#")) {//if wall, revert
            robot.setLocation(robot.getOldX(), robot.getOldY());
        } else if (event.equals("0")) {//if nothing, proceed
        } else if (Double.parseDouble(event) > 0 && Double.parseDouble(event) < 1) {//if harvest, potentially harvest
            harvest(robot.getX(), robot.getY());
        }

    }

    //x, y = map dimensions
    //startX, startY = start location's x and y coordinates
    //endY, endX = end location's x and y coordinates
    // blocked = array containing obstacle coordinates
    public static ArrayList<Coordinate> findShortestPath(int y, int x, int startY, int startX, int endY, int endX, ArrayList<Map> obstacles) {

        //reset the data structures
        theMap = new Coordinate[x][y];
        closed = new boolean[x][y];
        open = new PriorityQueue<>((Object o1, Object o2) -> {
            Coordinate c1 = (Coordinate) o1;
            Coordinate c2 = (Coordinate) o2;

            return c1.cost < c2.cost ? -1
                    : c1.cost > c2.cost ? 1 : 0;
        });

        setStart(startX, startY);//set start     
        setEnd(endY, endX);//Set end

        for (int i = 0; i < x; ++i) {
            for (int j = 0; j < y; ++j) {
                theMap[i][j] = new Coordinate(i, j);
                theMap[i][j].heuristic = Math.abs(i - endX) + Math.abs(j - endY);

            }

        }
        
        theMap[startX][startY].cost = 0;

        for (int i = 0; i < obstacles.size(); ++i) {
            setObstacle(obstacles.get(i).getFirst(), obstacles.get(i).getSecond()); //set obstacle location
        }

        findShortestPath();//A star algorithm

        if (closed[endX][endY]) {//determine the path
            Coordinate curr = theMap[endX][endY];
            cells.add(curr);
            while (curr.parent != null) {
                cells.add(curr.parent);
                curr = curr.parent;
            }

        } else {
        }

        return cells;
    }

    
    //A Star algorithm implementation to determine the shortest path with 8 possible directions
    public static void findShortestPath() {
       
        open.add(theMap[startX][startY]);//add start

        Coordinate curr;

        while (true) {
            curr = open.poll();
            if (curr == null) {
                break;
            }
            closed[curr.i][curr.j] = true;

            if (curr.equals(theMap[endX][endY])) {
                return;
            }

            Coordinate c;
            if (curr.i - 1 >= 0) {
                c = theMap[curr.i - 1][curr.j];
                checkCost(curr, c, curr.cost + VH_COST);

                if (curr.j - 1 >= 0) {
                    c = theMap[curr.i - 1][curr.j - 1];
                    checkCost(curr, c, curr.cost + D_COST);
                }

                if (curr.j + 1 < theMap[0].length) {
                    c = theMap[curr.i - 1][curr.j + 1];
                    checkCost(curr, c, curr.cost + D_COST);
                }
            }

            if (curr.j - 1 >= 0) {
                c = theMap[curr.i][curr.j - 1];
                checkCost(curr, c, curr.cost + VH_COST);
            }

            if (curr.j + 1 < theMap[0].length) {
                c = theMap[curr.i][curr.j + 1];
                checkCost(curr, c, curr.cost + VH_COST);
            }

            if (curr.i + 1 < theMap.length) {
                c = theMap[curr.i + 1][curr.j];
                checkCost(curr, c, curr.cost + VH_COST);

                if (curr.j - 1 >= 0) {
                    c = theMap[curr.i + 1][curr.j - 1];
                    checkCost(curr, c, curr.cost + D_COST);
                }

                if (curr.j + 1 < theMap[0].length) {
                    c = theMap[curr.i + 1][curr.j + 1];
                    checkCost(curr, c, curr.cost + D_COST);
                }
            }
        }
    }

    //set obstacles as null
    public static void setObstacle(int x, int y) {
        theMap[x][y] = null;
    }

    //set start location
    public static void setStart(int x, int y) {
        startX = x;
        startY = y;
    }

    //set end location
    public static void setEnd(int x, int y) {
        endX = x;
        endY = y;
    }

    //check cost for A star algorithm
    static void checkCost(Coordinate curr, Coordinate c, int cost) {
        if (c == null || closed[c.i][c.j]) {
            return;
        }
        int t_final_cost = c.heuristic + cost;

        boolean inOpen = open.contains(c);
        if (!inOpen || t_final_cost < c.cost) {
            c.cost = t_final_cost;
            c.parent = curr;
            if (!inOpen) {
                open.add(c);
            }
        }
    }

}
