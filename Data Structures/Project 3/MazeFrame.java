//WILLIAM KUREK
//PROJECT 3
//CS 445
import java.util.ArrayList;
import javax.swing.JFrame;

public class MazeFrame {

    public static void main(String[] args) throws InterruptedException {
        int width = 15;
        int height = 10;
        JFrame frame = new JFrame();
        Maze maze = new Maze(width, height);
        ArrayList<Pair<Integer, Integer>> solution = new ArrayList<Pair<Integer, Integer>>();
        MazeComponent mc = new MazeComponent(maze, solution);
        frame.setSize(800, 800);
        frame.setTitle("Maze");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(mc);
        frame.setVisible(true);

        solution.add(new Pair<Integer, Integer>(0, 0));
        Thread.sleep(1000);
        solveMaze(solution, mc, maze, 0, 0, 0 , 0);
        mc.repaint();
    }

    /**
     * Solve Maze: recursively solve the maze
     *
     * @param solution : The array list solution is needed so that every
     * recursive call, a new (or more) next position can be added or removed.
     * @param mc : This is the MazeComponent. We need that only for the purpose
     * of animation. We need to call mc.repaint() every time a new position is
     * added or removed. For example, : solution.add(...); mc.repaint();
     * Thread.sleep(sleepTime); : solution.remove(...); mc.repaint();
     * Thread.sleep(sleepTime); :
     * @param maze : The maze data structure to be solved.
     * @return a boolean value to previous call to tell the previous call
     * whether a solution is found.
     * @throws InterruptedException: We need this because of our
     * Thread.sleep(50);
     */
    public static boolean solveMaze(ArrayList<Pair<Integer, Integer>> solution, MazeComponent mc, Maze maze, int currentX, int currentY, int previousX, int previousY) throws InterruptedException {

        Pair <Integer, Integer> pair = new Pair<Integer, Integer>(currentX, currentY);

        if (currentX < 0 || currentY < 0 || currentY > maze.getWidth() - 1 || currentX > maze.getHeight() - 1) {
            return false;
        }

        if (currentX == previousX + 1) {
            if (maze.isNorthWall(currentX, currentY)) {
                return false;
            }
        }
        if (currentX == previousX - 1) {
            if (maze.isSouthWall(currentX, currentY)) {
                return false;
            }
        }
        if (currentY == previousY + 1) {
            if (maze.isWestWall(currentX, currentY)) {
                return false;
            }
        }
        if (currentY == previousY - 1) {
            if (maze.isEastWall(currentX, currentY)) {
                return false;
            }
        }

        if (solution.size() > 1) {
            for (Pair <Integer, Integer> search : solution) {
                if (search.toString().equals(pair.toString())) {
                    return false;
                }
            }
        }
        if (currentX == maze.getHeight() - 1 && currentY == maze.getWidth() - 1) {
            solution.add(pair);
            mc.repaint();
            Thread.sleep(50);
            return true;
        } else {
            solution.add(pair);
            mc.repaint();
            Thread.sleep(50);
        }
        if (solveMaze(solution, mc, maze, currentX - 1, currentY, currentX, currentY) || solveMaze(solution, mc, maze, currentX, currentY + 1, currentX, currentY) || solveMaze(solution, mc, maze, currentX + 1, currentY, currentX, currentY) || solveMaze(solution, mc, maze, currentX, currentY - 1, currentX, currentY)) { //N||E||S||W
            return true;
        } else {
            solution.remove(pair);
        }
        mc.repaint();
        Thread.sleep(50);
        return false;
    }
}
