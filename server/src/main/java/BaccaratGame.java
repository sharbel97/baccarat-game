import java.util.ArrayList;
import java.util.Objects;

/*
evaluateWinnings will determine if the user won or lost their bet and return the amount won or
lost based on the value in currentBet
 */

public class BaccaratGame {
    public ArrayList<Card> playerHand;
    public ArrayList<Card> bankerHand;
    public BaccaratDealer theDealer = new BaccaratDealer();
    public BaccaratGameLogic theGame = new BaccaratGameLogic();
    public Double currentBet = 0.0;
    public Double totalWinnings = 0.0;
    String betOn = "";

    BaccaratGame(String x, Double y) {
        theDealer.generateDeck();
        playerHand = theDealer.dealHand();
        bankerHand = theDealer.dealHand();
        betOn = x;
        currentBet = y;
    }


    public Double evaluateWinnings() {
        String winner = theGame.whoWon(playerHand, bankerHand);
        Double betReturn = currentBet;
        if (Objects.equals(betOn, winner) && Objects.equals(winner, "Player")) {

        } else if (Objects.equals(betOn, winner) && Objects.equals(winner, "Banker")) {

        } else if (Objects.equals(betOn, winner) && Objects.equals(winner, "Tie")) {
            betReturn *= 8.0; // :O
        } else if (!Objects.equals(betOn, winner)) {
            totalWinnings -= currentBet; // ;-;
            betReturn = betReturn * -1.0;
        }

        return betReturn;
    };

}

