package datamer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    }

    @Override
    public void start(Stage stage) throws Exception {
        Var.stage = stage;

        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResourceAsStream("/datamer/view/Win.fxml"));

        Image icon = new Image(getClass().getResourceAsStream("/datamer/resources/DeathStar.png"));
        Var.stage.getIcons().add(icon);
        Var.stage.setTitle("DataFest 1.3.1");

        Scene scene = new Scene((Parent) root);
        Var.stage.setScene(scene);
        Var.stage.setMinHeight(700);
        Var.stage.setMinWidth(1200);
//        Var.stage.setMaximized(true);
        Var.stage.show();

        stage.setOnCloseRequest((WindowEvent event) -> {
            event.consume();
            stage.setIconified(true);
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
