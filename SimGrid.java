import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Random;

/**
 * Part of the solution to Project 4.
 * 
 * Represents the grid of agents in the Schelling Simulation.
 * 
 * @author lewis63 and Nicholas Harasty (harastyn)
 * @version 2017.04.20
 */
public class SimGrid extends Pane
{
    private Agent[][] grid;
    private int numCells;
    private double satisfiedPercent;

    // satisfaction threshold of 30%
    private final static double THRESHOLD = 0.3;  

    /**
     * Creates and initializes an agent grid of the specified size.
     * 
     * @param rows the number of rows in the simulation grid
     * @param cols the number of cols in the simulation grid
     */
    public SimGrid(int rows, int cols)
    {

        numCells = rows * cols;
        grid = new Agent[rows][cols];
        initializeGrid();

        // set initial satisfaction percentage
        ArrayList<GridLocation> unsatisfied = findUnsatisfiedAgents();
        satisfiedPercent = (numCells - unsatisfied.size()) / (double) numCells
            * 100;
    }

    /**
     * Fills the grid with an initial, random set of agents and vacant spaces.
     * Approximately 10% of the grid locations are left vacant. The rest are
     * evenly distributed between red and blue agents.
     */
    public void initializeGrid()
    {

        //  TO DO:  Create an Agent for every cell in the grid. Each agent
        //  will be either white, red, or blue. Give a 10% chance of it being
        //  a white (vacant) cell. If it's not going to be vacant, give a
        //  50/50 chance that it will be red or blue. Also, add the square
        //  for each agent to the pane (this object) so that it will be
        //  displayed.

        for (int i = 0; i < grid.length; i++)
        {
            for (int j = 0; j < grid[i].length; j++)
            {
                // random num to determine vacant square
                Random randNum = new Random();
                int vacantChance = randNum.nextInt(10) + 1;
                //random num to determine either red or blue agent
                int agentNum = randNum.nextInt(2) + 1;

                if (vacantChance < 2)
                {
                    Agent agentVacant = new 
                        Agent(i, j, Color.WHITE);                    
                    grid[i][j] = agentVacant;
                    //this.grid[i][j].add(grid);
                    this.getChildren().add(agentVacant.getSquare());
                }

                else if (agentNum < 2)
                {
                    Agent agentBlue = new Agent(i, j, Color.BLUE);
                    grid[i][j] = agentBlue;
                    this.getChildren().add(agentBlue.getSquare());
                }
                else
                {
                    Agent agentRed = new Agent(i, j, Color.RED);
                    grid[i][j] = agentRed;
                    this.getChildren().add(agentRed.getSquare());
                }

            }
        }

    }

    /**
     * Gets the current percentage of satisfied agents in the grid.
     * 
     * @return the percentage of satisfied agents in the simulation
     */
    public double getSatisfiedPercent()
    {
        return satisfiedPercent;
    }

    /**
     * Performs one step of the simulation by finding the location of all
     * unsatisfied agents, then moving each one to a randomly chosen vacant
     * location.
     * 
     * @return the number of unsatisfied agents found
     */
    public int performSimulationStep()
    {
        ArrayList<GridLocation> unsatisfied = findUnsatisfiedAgents();

        //  TO DO:  For each unsatisfied agent location, find a vacant
        //  location and switch the colors of the agents. That is, make
        //  the vacant agent blue or red as appropriate and make the
        //  unsatisfied agent white (don't bother actually moving the Agent
        //  objects).

        // update satisfaction percentage

        for (GridLocation location : unsatisfied)
        {
            GridLocation vacantLocation = findVacantLocation();
            Agent unhappy = grid[location.getRow()][location.getCol()];
            Agent vacant = grid[vacantLocation.getRow()]
                [vacantLocation.getCol()];

            vacant.setColor(unhappy.getColor());
            unhappy.setColor(Color.WHITE);

        }
        satisfiedPercent = (numCells - unsatisfied.size()) / (double) numCells
            * 100;

        return unsatisfied.size();
    }

    /**
     * Creates a list of all grid locations that contain an unsatisfied agent.
     * 
     * @return a list of the locations of all currently unsatisfied agents
     */
    private ArrayList<GridLocation> findUnsatisfiedAgents()
    {
        ArrayList<GridLocation> unsatisfied = new ArrayList<GridLocation>();

        //  TO DO: Examine each agent in the grid and, if it is not vacant and
        //  if it is not satisfied, add that agent's grid location to the
        //  unsatisfied list.

        for (int i = 0; i < grid.length; i++)
        {
            for (int j = 0; j < grid[i].length; j++)
            {
                if (!grid[i][j].isVacant() && !agentIsSatisfied(i, j))
                {
                    GridLocation location = new GridLocation(i, j);
                    unsatisfied.add(location);
                }
            }
        }

        return unsatisfied;
    }

    /**
     * Determines if the agent at the specified location is satisfied. First
     * gets a list of all valid, non-vacant neighbors, then counts the number
     * of those neighbors that are the same type. An agent is satisfied with
     * its current location if the ratio of similar agents is greater that
     * a set threshold.
     * 
     * @return true if the agent is satisfied with its current location
     */
    private boolean agentIsSatisfied(int i, int j)
    {   

        ArrayList<Agent> neighbors = getNeighbors(i, j);

        int sameCount = 0;

        //  TO DO:  Count how many of the neighbors have the same color
        //  as the agent in question.
        //up and left

        for (Agent neighbor : neighbors)
        {
            if (neighbor.getColor().equals(grid[i][j].getColor()))
            {
                sameCount++;
            }
        }
        return  ((double) sameCount / neighbors.size() > THRESHOLD);

    }

    /**
     * Gets a list of agents that are neighbors (adjacent) to the specified
     * grid location. Checks each potential location individually, making sure
     * that each is valid (on the grid) and not vacant.
     * 
     * @return a list of agents that are adjacent to the specified location
     */
    private ArrayList<Agent> getNeighbors(int i, int j)
    {
        ArrayList<Agent> neighbors = new ArrayList<Agent>();

        // check up and left
        if (validLocation(i - 1, j - 1) && !grid[i - 1][j - 1].isVacant())
            neighbors.add(grid[i - 1][j - 1]);

        //check up
        if (validLocation(i - 1, j) && !grid[i - 1][j].isVacant())
            neighbors.add(grid[i - 1][j]);

        //check up and right
        if (validLocation(i - 1, j + 1) && !grid[i - 1][j + 1].isVacant())
            neighbors.add(grid[i - 1][j + 1]);    

        //check left
        if (validLocation(i , j - 1) && !grid[i][j - 1].isVacant())
            neighbors.add(grid[i][j - 1]);

        //check down and left
        if (validLocation(i + 1, j - 1) && !grid[i + 1][j - 1].isVacant())
            neighbors.add(grid[i + 1][j - 1]);    

        //check down
        if (validLocation(i + 1, j) && !grid[i + 1][j].isVacant())
            neighbors.add(grid[i + 1][j]);

        //check down and right
        if (validLocation(i + 1, j + 1) && !grid[i + 1][j + 1].isVacant())
            neighbors.add(grid[i + 1][j + 1]);

        //check right
        if (validLocation(i , j + 1) && !grid[i][j + 1].isVacant())
            neighbors.add(grid[i][j + 1]);    

        //  TO DO: Check all of the other potential neighbors, one at a time.
        return neighbors;
    }

    /**
     * Determines if the specified grid location is valid.
     * 
     * @return true if the specified location is a valid grid cell
     */
    private boolean validLocation(int i, int j)
    {
        //  TO DO:  Determine if the values specified for i and j are
        //  valid locations in the grid.
        return ((i < grid.length && i >= 0) && (j >= 0 && j < grid[i].length)); 
    }

    /**
     * Finds a vacant location in the simulation grid. Keeps checking cell
     * locations at random until a vacant one is found.
     * 
     * @return the grid location of a vacant cell
     */
    private GridLocation findVacantLocation()
    {
        //  TO DO:  Randomly pick a valid row and column and keep picking
        //  until a vacant one is found and then return it as a GridLocation
        //  object.
        Random randNum = new Random();
        int i = randNum.nextInt(grid.length);
        int j = randNum.nextInt(grid[i].length);
        while (!grid[i][j].isVacant())
        {
            i = randNum.nextInt(grid.length);
            j = randNum.nextInt(grid[i].length);
        }
        GridLocation vacantSpot = new GridLocation(i, j);
        return vacantSpot;
    }

    /**
     * Resets the simulation grid by clearing the pane of agent squares and
     * reinitializing the grid.
     */
    public void resetGrid()
    {
        getChildren().clear();
        initializeGrid();

        // set initial satisfaction percentage
        ArrayList<GridLocation> unsatisfied = findUnsatisfiedAgents();
        satisfiedPercent = (numCells - unsatisfied.size()) / (double) numCells
            * 100;
    }

}
