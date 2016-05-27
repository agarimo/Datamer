package datamer;

import datamer.ctrl.rutines.CveTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Agarimo
 */
public class Datamer extends Application {

    @Override
    public void init() {
        Var.initVar();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Datamer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        Var.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/datamer/view/Win.fxml"));

        Image icon = new Image(getClass().getResourceAsStream("/datamer/resources/DeathStar.png"));
        Var.stage.getIcons().add(icon);
        Var.stage.setTitle("DataFest 1.2");

        Scene scene = new Scene(root);
//        scene.getStylesheets().setAll(getClass().getResource("/datamer/resources/modena.css").toExternalForm());
        Var.stage.setScene(scene);
        Var.stage.setMaximized(true);
        Var.stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public static void test() {
        Var.initVar();
        CveTask ct = new CveTask();

        ct.run();
    }
}
