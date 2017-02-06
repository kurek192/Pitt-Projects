//WILLIAM KUREK
//PROJECT 3
//CS 445
import java.util.Random;
// No other import statement is allowed

public final class Maze {

    private final int width;
    private final int height;
    private final boolean[][] north, east, south, west;

    /**
     * Constructor
     *
     * @param aWidth the number of chambers in each row
     * @param aHeight the number of chamber in each column
     */
    public Maze(int aWidth, int aHeight) {
        width = aWidth;
        height = aHeight;
        north = new boolean [aHeight][aWidth];
        east = new boolean [aHeight][aWidth];
        south = new boolean [aHeight][aWidth];
        west = new boolean [aHeight][aWidth];
        createBorder(aWidth, aHeight);
        
    }
    
    private void createBorder(int aWidth, int aHeight){ //CREATE BORDER OF MAZE
        {
            for (int r = 0; r < aHeight; r++) {
                for (int c = 0; c < aWidth; c++) {
                    north[r][c] = false;
                    east[r][c] = false;
                    south[r][c] = false;
                    west[r][c] = false;
                }
                int i = height - 1;
                while (i >= 0) {
                    if (i == 0) {
                        north [i][0] = true;
                        west [i][0] = true;                        
                        
                    } else {
                        west [i][0] = true;
                    }
                    i--;
                }
                i = height - 1;
                while (i >= 0) {
                    if (i == 0) {
                        north[i][width - 1] = true;
                        east[i][width - 1] = true;
                    } else {
                        east[i][width - 1] = true;
                    }
                    i--;
                }
                i = width - 1;
                while (i >= 0) {
                    if (i == width - 1) {
                        north [0][i] = true;
                        east [0][i] = true;
                    } else if (i == 0) {
                        north[0][i] = true;
                        west[0][i] = true;
                    } else {
                        north[0][i] = true;
                    }
                    i--;
                }
                i = width - 1;
                while (i >= 0) {
                    if (i == width - 1) {
                        east[height - 1][i] = true;
                        south[height - 1][i] = true;
                    } else if (i == 0) {
                        south[height - 1][i] = true;
                        west[height - 1][i] = true;
                    } else {
                        south[height - 1][i] = true;
                    }
                    i--;
                }
            }
            
            MazeRecurse(0, 0, height, width);
        }
    }

    private void MazeRecurse(int x1, int y1, int x2, int y2) { //RECURSIVELY CREATE FOUR NEW CHAMBERS
        
        int theWidth = x2 - x1;
        int theHeight = y2 - y1;
        if(theWidth < 2 || theHeight < 2){
            return;
        }
        
        Random random = new Random();
        int randx = generateRandomInt(x1 + 1, x2 - 1);
        int randy = generateRandomInt(y1 + 1, y2 - 1);
        
        int count = 0;
        int left = 0, right = 0, up = 0, down = 0;
        while (count < 3) {         //CHOOSE WALL TO LEAVE SOLID
            int tempRand = generateRandomInt(1, 4);
            if (tempRand == 1 && left != 1) {
                left = 1;
                count++;
            } else if (tempRand == 2 && right != 2) {
                right = 2;
                count++;
            } else if (tempRand == 3 && up != 3) {
                up = 3;
                count++;
            } else if (tempRand == 4 && down != 4) {
                down = 4;
                count++;
            }
        }
        
        addHorizontalWalls(randx, randy, y1, y2, left, right); //BUILD WALLS
        addVerticalWalls(randx, randy, x1, x2, up, down);

        MazeRecurse(x1, y1, randx, randy); //RECURSIVE CALLS
        MazeRecurse(randx, y1, x2, randy);
        MazeRecurse(x1, randy, randx, y2);
        MazeRecurse(randx, randy, x2, y2);
        //finalizeBorder();

    }

    private void addHorizontalWalls(int x, int y, int xMin, int xMax, int left, int right) { //ADD HORIZONTAL WALLS

        int rightDoor = generateRandomInt(y, xMax-1);
        int leftDoor = generateRandomInt(xMin, y-1);
        int theY = y;

        while(y != xMin-1){
            north[x][y] = true;
            south[x-1][y] = true;
            y--;
        }
        y = theY;
        while(y!= xMax){
            north[x][y] = true;
            south[x-1][y] = true;
            y++;
        }
 
        openWallHorizontal(rightDoor, x, right);
        openWallHorizontal(leftDoor, x, left);
        // }

    }

    private void addVerticalWalls(int x, int y, int yMin, int yMax, int up, int down) { //ADD VERTICAL WALLS
        
        int upDoor = generateRandomInt(yMin, x-1);
        int downDoor = generateRandomInt(x, yMax-1);
        
        int theX = x;
        
        while(x != yMin-1){
            west[x][y] = true;
            east[x][y-1] = true;
            x--;
        }
        x = theX;
        while(x != yMax){
            west[x][y] = true;
            east[x][y-1] = true;
            x++;
        }
        
        openWallVertical(upDoor, y, up);
        openWallVertical(downDoor, y, down);
        // }
    }

    private void openWallHorizontal(int x, int y, int door) { //OPEN SINGLE HORIZONTAL WALL
        if (door == 1) {
            north[y][x] = false;
            south[y-1][x] = false;
        }
        if (door == 2) {
            north[y][x] = false;
            south[y-1][x] = false;
        }       
    }
    
    private void openWallVertical(int y, int x, int door) { //OPEN SINGLE VERTICAL WALL
        if (door == 3) {
            west[y][x] = false;
            east[y][x-1] = false;
        }
        if (door == 4) {
            west[y][x] = false;
            east[y][x-1] = false;
        }
    }

    private static int generateRandomInt(int min, int max) { //RANDOM INT GENERATOR USING .RANDOM

        if (min > max) {
            return 0;
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    /**
     * getWidth
     *
     * @return the width of this maze
     */
    public int getWidth() { 
        return this.width;
    }

    /**
     * getHeight
     *
     * @return the height of this maze
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * isNorthWall
     *
     * @param row the row identifier of a chamber
     * @param column the column identifier of a chamber
     * @return true if the chamber at row row and column column contain a north
     * wall. Otherwise, return false
     */
    public boolean isNorthWall(int row, int column) {
        return north[row][column];
    }

    /**
     * isEastWall
     *
     * @param row the row identifier of a chamber
     * @param column the column identifier of a chamber
     * @return true if the chamber at row row and column column contain an east
     * wall. Otherwise, return false
     */
    public boolean isEastWall(int row, int column) {
        return east[row][column];
        
    }

    /**
     * isSouthWall
     *
     * @param row the row identifier of a chamber
     * @param column the column identifier of a chamber
     * @return true if the chamber at row row and column column contain a south
     * wall. Otherwise, return false
     */
    public boolean isSouthWall(int row, int column) {
        return south[row][column];
        
    }

    /**
     * isWestWall
     *
     * @param row the row identifier of a chamber
     * @param column the column identifier of a chamber
     * @return true if the chamber at row row and column column contain a west
     * wall. Otherwise, return false
     */
    public boolean isWestWall(int row, int column) {
        return west[row][column];
        
    }
}
