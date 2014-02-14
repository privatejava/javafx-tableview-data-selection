/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tableviewdataselection;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Narayan G. Maharjan <me@ngopal.com.np>
 */
public class TableViewDataSelection extends Application {
    @Override
    public void start(Stage primaryStage) {
        Pane p = null;
        try {
            p = FXMLLoader.load(getClass().getResource("TableViewDataFXML.fxml"));
        } catch (IOException ex) {
            p = new BorderPane();
            Label l = new Label("Error on FXML loading:" + ex.getMessage());
            p.getChildren().add(l);
            Logger.getLogger(TableViewDataSelection.class.getName()).log(Level.SEVERE, null, ex);
        }


        Scene scene = new Scene(p);

        primaryStage.setTitle("TableView Data Selection Demo !");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX
     * application. main() serves only as fallback in case the
     * application can not be launched through deployment artifacts,
     * e.g., in IDEs with limited FX support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
