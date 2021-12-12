import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class MyController implements Initializable {
    /* Scene 1 components */
    @FXML public AnchorPane firstSceneRoot;
    @FXML public TextField portNumberField;
    @FXML public TextField ipAddressField;

    /* Scene 2 components */
    @FXML public TextField amountField;
    @FXML public ListView<String> mainListView;
    @FXML public Button playerButton;
    @FXML public Button tieButton;
    @FXML public Button bankerButton;
    @FXML public Button quitButton;
    @FXML public Label winningsLabel;
    @FXML public Label playerCard1;
    @FXML public Label playerCard2;
    @FXML public Label playerCard3;
    @FXML public Label playerTotal;
    @FXML public Label bankerCard1;
    @FXML public Label bankerCard2;
    @FXML public Label bankerCard3;
    @FXML public Label bankerTotal;
    @FXML public Button playAgainButton;

    Client clientConnection;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    public void connectClientMethod() throws IOException {
        String ip = ipAddressField.getText();
        int port = Integer.parseInt(portNumberField.getText());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/SecondSceneClient.fxml"));
        Parent root2 = loader.load(); // load view into parent
        root2.setStyle("-fx-font-family: 'serif'");
        MyController myctr = loader.getController(); // get controller created by FXMLLoader
        myctr.initClientConnection(ip, port);
        firstSceneRoot.getScene().setRoot(root2); // update scene graph
    }

    public void initClientConnection(String ip, int port) {
        clientConnection = new Client(data->{
            Platform.runLater(()->{
                BaccaratInfo info = (BaccaratInfo)data;
                mainListView.getItems().add(0, info.message);
                if (info.wantsToPlay) {
                    playerCard3.setText("****");
                    bankerCard3.setText("****");
                    // Display 2 cards per player and banker
                    playerCard1.setText(info.playerHand.get(0).value.toString()+" "+info.playerHand.get(0).suite);
                    playerCard2.setText(info.playerHand.get(1).value.toString()+" "+info.playerHand.get(1).suite);
                    bankerCard1.setText(info.bankerHand.get(0).value.toString()+" "+info.bankerHand.get(0).suite);
                    bankerCard2.setText(info.bankerHand.get(1).value.toString()+" "+info.bankerHand.get(1).suite);

                    Pause1(info);
                }
            });
        }, ip, port);
        clientConnection.start();
    }

    public void setPlayerMethod() throws IOException {
        BaccaratInfo info = new BaccaratInfo("Player",Double.parseDouble(amountField.getText()));
        disableButtons();
        clientConnection.send(info);
    }

    public void setTieMethod() {
        BaccaratInfo info = new BaccaratInfo("Tie",Double.parseDouble(amountField.getText()));
        disableButtons();
        clientConnection.send(info);
    }
    public void setBankerMethod() {
        BaccaratInfo info = new BaccaratInfo("Banker",Double.parseDouble(amountField.getText()));
        disableButtons();
        clientConnection.send(info);
    }

    public void quitMethod() {
        Platform.exit();
        System.exit(0);
    }

    public String getTotalWinnings(double payout, double oldWinnings) {
        String result = "";

        double net = (payout+oldWinnings);
        result = Double.toString(net);

        return result;
    }

    // Pause1 initiates after initial banker and player hands are shown
    // on finish: shows the game results OR pause again to show more info
    public void Pause1(BaccaratInfo info) {
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(actionEvent -> {
            // check if natural win, post message and update current winnings
            if (info.naturalWin) {
                mainListView.getItems().add(0, info.whoWon);
                mainListView.getItems().add(0, info.result);
                playerTotal.setText(info.playerTotal.toString());
                bankerTotal.setText(info.bankerTotal.toString());
                winningsLabel.setText(getTotalWinnings(info.payout, Double.parseDouble(winningsLabel.getText())));
                playAgainButton.setDisable(false);
            } else { // pause for few more seconds.
                mainListView.getItems().add(0, "No Natural win...");
                Pause2(info);
            }
        });
        pause.play();
    }

    // Pause2 initiates after when there isn't a natural win
    // on finish: check if player has 3rd hand, display, pause
    // else display banker card, pause.
    public void Pause2(BaccaratInfo info) {
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(actionEvent -> {
            // check if player has 3rd hand, display
            if (info.playerHand.get(2).value!=0) {
                mainListView.getItems().add(0, "Player Gets Extra Card!");
                playerCard3.setText(info.playerHand.get(2).value+" "+info.playerHand.get(2).suite);
                Pause3(info);
            } else if (info.bankerHand.get(2).value!=0) {
                mainListView.getItems().add(0 ,"Banker Gets Extra Card!");
                bankerCard3.setText(info.bankerHand.get(2).value+" "+info.bankerHand.get(2).suite);
                Pause4(info);
            }
            // check if banker doesn't have extra hand, display game results
            // Pause3
        });
        pause.play();
    }

    // Pause3 initiates after when player has 3rd hand and has been displayed
    // on finish: check if banker has extra hand
    public void Pause3(BaccaratInfo info) {
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(actionEvent -> {
            // check if player has 3rd hand, display
            if (info.bankerHand.get(2).value!=0) {
                mainListView.getItems().add(0,"Banker Gets Extra Card!");
                bankerCard3.setText(info.bankerHand.get(2).value+" "+info.bankerHand.get(2).suite);
                Pause4(info);
            } else {
                playerTotal.setText(info.playerTotal.toString());
                bankerTotal.setText(info.bankerTotal.toString());
                mainListView.getItems().add(0, info.whoWon);
                mainListView.getItems().add(0, info.result);
                winningsLabel.setText(getTotalWinnings(info.payout, Double.parseDouble(winningsLabel.getText())));
                playAgainButton.setDisable(false);
            }
        });
        pause.play();
    }

    // Pause4 initiates after when banker has 3rd hand, and has been displayed
    // on finish: display final results
    public void Pause4(BaccaratInfo info) {
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(actionEvent -> {
            // report results of the game
            playerTotal.setText(info.playerTotal.toString());
            bankerTotal.setText(info.bankerTotal.toString());
            mainListView.getItems().add(0, info.whoWon);
            mainListView.getItems().add(0, info.result);
            winningsLabel.setText(getTotalWinnings(info.payout, Double.parseDouble(winningsLabel.getText())));
            playAgainButton.setDisable(false);
        });
        pause.play();
    }

    public void playAgainMethod() {
        playAgainButton.setDisable(true);
        enableButtons();
    }

    public void disableButtons() {
        tieButton.setDisable(true);
        playerButton.setDisable(true);
        bankerButton.setDisable(true);
    }

    public void enableButtons() {
        tieButton.setDisable(false);
        playerButton.setDisable(false);
        bankerButton.setDisable(false);
    }
}