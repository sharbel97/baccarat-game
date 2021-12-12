import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

public class Client extends Thread {
    Socket socketClient;
    ObjectOutputStream out;
    ObjectInputStream in;

    String ipAddress;
    int port;

    private final Consumer<Serializable> callback;

    Client(Consumer<Serializable> call, String ip, int port){
        callback = call;
        ipAddress = ip;
        this.port = port;
    }

    public void run() {
        try {
            socketClient = new Socket(ipAddress, port);
            out = new ObjectOutputStream(socketClient.getOutputStream());
            in = new ObjectInputStream(socketClient.getInputStream());
            socketClient.setTcpNoDelay(true);
        } catch (Exception e) {
            // creating client socket error
        }

        while(true) {
            try {
                BaccaratInfo info = (BaccaratInfo) in.readObject();

                // accepting whatever updateClients() from server sent
                // callback currently only update the mainListView by taking in a string
                // How would we update the card labels, totals, score, winnings?
                // If we pass in BaccaratInfo object we must figure out how to parse
                // this object in MyController initClientConnection function.
                callback.accept(info);
            } catch (Exception e) {
                // creating a connection error
                try {
                    in.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    break;
                }
                break;
            }
        }
    }

    public void send(BaccaratInfo data) {
        try {
            out.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
