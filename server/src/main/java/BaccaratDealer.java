import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/*
generateDeck will generate a new standard 52 card deck where
each card is an instance of the Card class in the ArrayList<Card> deck.
dealHand will deal two cards and return them in an ArrayList<Card>.
drawOne will deal a single card and return it.
shuffleDeck will create a new deck of 52 cards and “shuffle”;
randomize the cards in that ArrayList<Card>.
deckSize will just return how many cards are in this.deck at any given time.
*/

public class BaccaratDealer {
    public Random randomGenerator = new Random();
    public ArrayList<Card> deck = new ArrayList<>();

    BaccaratDealer() {

    }

    public void generateDeck() {
        deck.clear();
        for (int i = 1; i < 14; ++i) {
            for (int j = 0; j < 4; ++j) {
                deck.add(new Card(j, i));
            }
        }
    };

    public ArrayList<Card> dealHand() {
        int index = randomGenerator.nextInt(deck.size());
        ArrayList<Card> returnList = new ArrayList<>();
        returnList.add(deck.get(index));
        deck.remove(index);
        int index2 = randomGenerator.nextInt(deck.size());
        returnList.add(deck.get(index2));
        deck.remove(index2);
        return returnList;
    };

    public Card drawOne() {
        int index = randomGenerator.nextInt(deck.size());
        Card temp = deck.get(index);
        deck.remove(index);
        return temp;
    };

    public void shuffleDeck() {
        generateDeck();
        Collections.shuffle(this.deck); // ez money lul
    };

    public Integer deckSize() {
        return this.deck.size();
    };

}
