import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.event.ActionEvent;

/**
 * Project 4 main driver.
 * 
 * This is the main class of an app that presents a simulation of the model
 * created by economist Thomas Schelling to help explain self-segregation.
 * 
 * @author Nicholas Harasty
 * @version 2017.04.20
 */
public class SchellingSim extends Application
{
    private SimGrid simGrid;
    private int round;
    private Text roundText;
    private Text satisfiedText;
    
    /**
     * Presents the simulation window with buttons to run and reset the
     * simulation.
     * 
     * @param primaryStage the main application window
     */
    public void start(Stage primaryStage)
    {   
        
        //creates step button executing one round of agent movement
        Button stepButton = new Button("Step");
        stepButton.setOnAction(this::handleStep);
        
        //creates run botton to perform simulation until all agents satisfied
        Button runButton = new Button("Run");
        runButton.setOnAction(this::handleRun);
        
        //creates reset button 
        Button resetButton = new Button("Reset");
        resetButton.setOnAction(this::handleReset);
        
        FlowPane buttons = new FlowPane(stepButton, runButton, resetButton);
        buttons.setAlignment(Pos.TOP_CENTER);
        buttons.setPadding(new Insets(20, 0, 0, 0));
        buttons.setHgap(20);
        
        
        simGrid = new SimGrid(30, 30);
        round = 0;
        roundText = new Text();
        satisfiedText = new Text();
        updateRoundData();
        
        FlowPane roundData = new FlowPane(roundText, satisfiedText);
        roundData.setAlignment(Pos.TOP_CENTER);
        roundData.setHgap(20);
        
        VBox root = new VBox(buttons, roundData, simGrid);
        root.setSpacing(20);
        
        Scene scene = new Scene(root, 550, 600);

        primaryStage.setTitle("Schelling Self-Segregation Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Include the following line to avoid a bug in BlueJ
        primaryStage.setOnCloseRequest(e -> System.exit(0));        
    }
    
    
    /**
     * Handles the user pressing the Step button by performing one step of
     * the simulation and updating the display.
     * @param event the event representing the button being pushed
     */
    public void handleStep(ActionEvent event)
    {   
        if (simGrid.getSatisfiedPercent() < 100)
        {
            simGrid.performSimulationStep();
            round++;
            updateRoundData();
        }
    }

    /**
     * Handles the user pressing the Run button by repeatedly performing steps
     * of the simulation until all agents are satisfied.
     * @param event the event representing the button being pushed
     */
    public void handleRun(ActionEvent event)
    {   
        while (simGrid.getSatisfiedPercent() < 100)
        {
            simGrid.performSimulationStep();
            round++;
            updateRoundData();
        }
    }
    
    /**
     * Resets the simulation to an initial random state.
     * @param event the event representing the button being pushed
     */
    public void handleReset(ActionEvent event)
    {   
        simGrid.resetGrid();
        round = 0;
        updateRoundData();
    }
    
    /**
     * Updates the round and satisfaction data displayed in the simulation
     * window.
     */
    private void updateRoundData()
    {
        roundText.setText("Round: " + round);
        satisfiedText.setText("Satisfied: " +
            String.format("%.1f %%", simGrid.getSatisfiedPercent()));
    }
    
    /**
     * Launches the JavaFX application.
     * @param args unused command-line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }
}