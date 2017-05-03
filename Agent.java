import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

/**
 * Part of the solution to Project 4.
 * 
 * Represents a simulation agent using a square of a particular color.
 * A white agent represents an unoccupied grid cell.
 * 
 * @author Nicholas Harasty
 * @version 2017.04.20
 */
public class Agent
{
    private Rectangle square;

    /**
     * Sets the agent's display location (based on the specified row and
     * column) as well as the agent's color.
     * 
     * @param row the grid row for this agent
     * @param col the grid column for this agent
     * @param color the color of this agent
     */
    public Agent(int row, int col, Color color)
    {
        square = new Rectangle(col * 15 + 50, row * 15, 15, 15);
        square.setStroke(Color.BLACK);
        square.setFill(color);
    }

    /**
     * Gets the rectangle (square) representing this agent.
     * @return the square representing this agent 
     */
    public Rectangle getSquare()
    {
        return square;
    }
    
    /**
     * Gets the color of this agent.
     * @return the color of this agent 
     */
    public Color getColor()
    {
        return (Color)square.getFill();
    }
    
    /**
     * Sets the color of this agent.
     * 
     * @param color the new color for this agent
     */
    public void setColor(Color color)
    {
        square.setFill(color);
    }
    
    /**
     * Determines if this agent represents a vacant (white) location.
     * @return true if location is vacant and false otherwise
     */
    public boolean isVacant()
    {
        return square.getFill().equals(Color.WHITE);
    }
}
