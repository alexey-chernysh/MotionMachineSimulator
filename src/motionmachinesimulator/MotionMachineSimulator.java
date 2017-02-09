/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package motionmachinesimulator;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import motionmachinesimulator.Processor.ProcessorSettings;

/**
 *
 * @author alexey
 */
public class MotionMachineSimulator extends Application {
    
    @Override
    public void start(Stage primaryStage) {
/*
        Button startButton = new Button();
        startButton.setText("Start");
        startButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
            }
        });

        StackPane root = new StackPane();
        Scene scene = new Scene(root, 1300, 750);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
        */

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println(ProcessorSettings.gear_state);
        System.out.println(ProcessorSettings.step_state);
        System.out.println(ProcessorSettings.accuracy_state);
        launch(args);
    }
    
}
