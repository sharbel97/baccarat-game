import java.io.Serializable;

public class Card implements Serializable {
    public String suite;
    public Integer value;

    public Card() {

    }

    public Card(Integer x, Integer y) { // card constructor
        if (x == 0) {
            this.suite = "clubs";
        } else if (x == 1) {
            this.suite = "spades";
        } else if (x == 2) {
            this.suite = "hearts";
        } else if (x == 3) {
            this.suite = "diamonds";
        }
        this.value = y;
    }

}
