import java.io.Serializable;

public class ServerUpdates implements Serializable {
    String message;
    int count;

    ServerUpdates(String s, int i) {
        message = s;
        count = i;
    }
}
