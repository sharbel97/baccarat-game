import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Read file fxml and draw interface.
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/firstSceneClient.fxml")));

        primaryStage.setTitle("Client Application");
        Scene s1 = new Scene(root, 600,500);
        s1.getStylesheets().add("/styles/styles.css");
        s1.getRoot().setStyle("-fx-font-family: 'serif'");
        primaryStage.setScene(s1);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}