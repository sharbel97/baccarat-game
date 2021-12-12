import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class MyController implements Initializable {
    /* Scene 1 components */
    @FXML public AnchorPane firstSceneRoot;
    @FXML public TextField portNumberField;

    /*Scene 2 components */
    @FXML public AnchorPane secondSceneRoot;
    @FXML public TextField numPlayersConnected;
    @FXML public ListView<String> mainListView;
    @FXML public Button turnOffServer;

    Server serverConnection;
    int count;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Auto generated method stub
    }

    public void destroyServer() {
        Platform.exit();
        System.exit(0);
    }

    public void connectServerMethod(ActionEvent e) throws IOException{
        // Get port number
        int port = Integer.parseInt(portNumberField.getText());
        // Get instance of loader class
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/SecondSceneServer.fxml"));
        Parent root2 = loader.load(); // load view into parent
        root2.setStyle("-fx-font-family: 'serif'");
        MyController myctr = loader.getController(); // get controller created by FXMLLoader
        myctr.initServerConnection(port);
        firstSceneRoot.getScene().setRoot(root2); // update scene graph
    }

    public void initServerConnection(int port) {
        serverConnection = new Server(data-> {
            Platform.runLater(()->{
                ServerUpdates updates = (ServerUpdates)data;
                mainListView.getItems().add(0, updates.message);
                numPlayersConnected.setText(Integer.toString(updates.count));
            });
        }, port);
    }
}