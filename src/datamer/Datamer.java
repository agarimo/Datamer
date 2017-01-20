package datamer;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Agarimo
 */
public class Datamer extends Application {

    @Override
    public void init() {
        Var.initVar();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Datamer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void start(Stage stage) throws Exception {

        if (Var.isUpToDate) {
            Var.stage = stage;

            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(getClass().getResourceAsStream("/datamer/view/Win.fxml"));

            Image icon = new Image(getClass().getResourceAsStream("/datamer/resources/DeathStar.png"));
            Var.stage.getIcons().add(icon);
            Var.stage.setTitle("DataFest 1.4.1");

            Scene scene = new Scene((Parent) root);
//        scene.getStylesheets().setAll(getClass().getResource("/datamer/resources/materialDesign.css").toExternalForm());
            Var.stage.setScene(scene);

            Var.stage.setOnCloseRequest((WindowEvent event) -> {
                event.consume();
                stage.setIconified(true);
            });

            Var.stage.setMinHeight(700);
            Var.stage.setMinWidth(1200);
            Var.stage.setMaximized(true);
            Var.stage.show();

        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("INFORMACIÓN");
            alert.setHeaderText("EXISTE UNA ACTUALIZACIÓN");
            alert.setContentText("Instale la última versión del programa.");

            alert.showAndWait();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
