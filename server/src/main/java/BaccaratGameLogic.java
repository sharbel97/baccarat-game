import java.util.ArrayList;
import java.util.Objects;

/*
The method whoWon will evaluate two hands at the end of the game and
return a string depending on the winner: “Player”, “Banker”, “Draw”.
The method handTotal will take a hand and return how many points that hand is worth.
The methods evaluateBankerDraw and evaluatePlayerDraw will return true if either one
should be dealt a third card, otherwise return false.
 */

public class BaccaratGameLogic {

    public String whoWon(ArrayList<Card> player, ArrayList<Card> banker) {
        int playerTotal = handTotal(player);
        int bankerTotal = handTotal(banker);

        if (playerTotal == bankerTotal) {
            return "Tie";
        } else if (playerTotal > bankerTotal) {
            return "Player";
        } else {
            return "Banker";
        }
    }

    public Integer handTotal(ArrayList<Card> hand) {
        int total = 0;
        for (Card card : hand) {
            if (card.value < 10) {
                total += card.value;
            }
        }

        while (total >= 10) {
            total -= 10;
        }

        return total;
    }

    public Boolean evaluateBankerDraw(ArrayList<Card> hand, Card playerCard) {
        Integer total = handTotal(hand);
        if (total > 6) {
            return false;
        }

        if (total < 3) {
            return true;
        } else {
            if (playerCard == null) {
                return total <= 5;
            } else if (playerCard.value == 0 || playerCard.value == 10 || playerCard.value == 11 || playerCard.value == 12 || playerCard.value == 13) {
                return total <= 3;
            } else if (playerCard.value == 1) {
                return total <= 3;
            } else if (playerCard.value == 2) {
                return total <= 4;
            } else if (playerCard.value == 3) {
                return total <= 4;
            } else if (playerCard.value == 4) {
                return total <= 5;
            } else if (playerCard.value == 5) {
                return total <= 5;
            } else if (playerCard.value == 6) {
                return true;
            } else if (playerCard.value == 7) {
                return true;
            } else if (playerCard.value == 8) {
                return total <= 3;
            } else if (playerCard.value == 9) {
                return total <= 3;
            }

        }

        return handTotal(hand) < 7;
    }

    public Boolean evaluatePlayerDraw(ArrayList<Card> hand) {
        return handTotal(hand) < 6;
    }

    Double checkNaturalWin(Integer handTotalPlayer, Integer handTotalBanker, BaccaratInfo myBaccaratInfo) {

        if (handTotalBanker > 7 && handTotalPlayer > 7) { // Natural tie-off
            if (handTotalBanker.equals(handTotalPlayer)) {
                if (Objects.equals(myBaccaratInfo.betOn, "Tie")) {
                    return myBaccaratInfo.bet * 8.0;
                } else {
                    return myBaccaratInfo.bet * -1.0;
                }
            } else if (handTotalBanker > handTotalPlayer) {
                if (Objects.equals(myBaccaratInfo.betOn, "Banker")) {
                    return myBaccaratInfo.bet;
                } else {
                    return myBaccaratInfo.bet * -1.0;
                }
            } else {
                if (Objects.equals(myBaccaratInfo.betOn, "Player")) {
                    return myBaccaratInfo.bet;
                } else {
                    return myBaccaratInfo.bet * -1.0;
                }
            }

        } else if (handTotalBanker > 7) { // Banker natural win
            if (Objects.equals(myBaccaratInfo.betOn, "Banker")) {
                return myBaccaratInfo.bet;
            } else {
                return myBaccaratInfo.bet * -1.0;
            }
        } else if (handTotalPlayer > 7) { // Player natural win
            if (Objects.equals(myBaccaratInfo.betOn, "Player")) {
                return myBaccaratInfo.bet;
            } else {
                return myBaccaratInfo.bet * -1.0;
            }
        }

        return 0.0;
    }

    void playGame(BaccaratInfo myBaccaratInfo, BaccaratGame myBaccaratGame) {
        Double payout = 0.0;
        BaccaratGameLogic myGameLogic = new BaccaratGameLogic();
        myBaccaratInfo.playerHand = myBaccaratGame.playerHand;
        myBaccaratInfo.bankerHand = myBaccaratGame.bankerHand;

        Integer handTotalPlayer = myGameLogic.handTotal(myBaccaratGame.playerHand);
        Integer handTotalBanker = myGameLogic.handTotal(myBaccaratGame.bankerHand);

        if (handTotalBanker > 7 || handTotalPlayer > 7) {
            payout = checkNaturalWin(handTotalPlayer, handTotalBanker, myBaccaratInfo);
            myBaccaratInfo.payout = payout;
            myBaccaratInfo.naturalWin = true;
            return;
        }

        if (handTotalPlayer <= 6) {
            myBaccaratInfo.playerHand.add(myBaccaratGame.theDealer.drawOne());
            myBaccaratGame.playerHand = myBaccaratInfo.playerHand;
            if (myGameLogic.evaluateBankerDraw(myBaccaratInfo.bankerHand, myBaccaratInfo.playerHand.get(2))) {
                myBaccaratInfo.bankerHand.add(myBaccaratGame.theDealer.drawOne());
                myBaccaratGame.bankerHand = myBaccaratInfo.bankerHand;
            }
            payout = myBaccaratGame.evaluateWinnings();
            myBaccaratInfo.payout = payout;
            return;
        }

        if (myGameLogic.evaluateBankerDraw(myBaccaratInfo.bankerHand, null)) {
            myBaccaratInfo.bankerHand.add(myBaccaratGame.theDealer.drawOne());
            myBaccaratGame.bankerHand = myBaccaratInfo.bankerHand;
            payout = myBaccaratGame.evaluateWinnings();
            myBaccaratInfo.payout = payout;
        }
    }
}





