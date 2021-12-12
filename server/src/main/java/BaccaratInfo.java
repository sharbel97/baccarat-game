import java.io.Serializable;
import java.util.ArrayList;
// Object class which will send info between Server and Client

public class BaccaratInfo implements Serializable {

    long serialVersionUIS;
    double bet;
    String betOn;
    public Double totalWinnings;
    ArrayList<Card> playerHand = new ArrayList<>();
    ArrayList<Card> bankerHand = new ArrayList<>();
    Integer playerTotal;
    Integer bankerTotal;
    Double payout;
    String result;
    String whoWon;
    String message;
    boolean wantsToPlay;
    boolean naturalWin;
    int clientNum;

    BaccaratInfo(String betOn, double bet) {
        clientNum = 0;
        playerTotal = 0;
        bankerTotal = 0;
        payout = 0.0;
        result = "";
        whoWon = "";
        message = "";
        wantsToPlay = false;
        totalWinnings = 0.0;
        this.betOn = betOn;
        this.bet = bet;
        naturalWin = false;
    }

    BaccaratInfo(String message) {
        clientNum = 0;
        playerTotal = 0;
        bankerTotal = 0;
        payout = 0.0;
        result = "";
        whoWon = "";
        wantsToPlay = false;
        totalWinnings = 0.0;
        betOn = "";
        bet = 0.0;
        this.message = message;
        naturalWin = false;
    }

}
