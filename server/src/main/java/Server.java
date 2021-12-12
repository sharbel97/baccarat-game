import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;

public class Server {
    int count = 0;
    int clientNumber = 1;
    int port;
    ArrayList<ClientThread> clients = new ArrayList<>();
    TheServer server;

    private final Consumer<Serializable> callback;

    Server(Consumer<Serializable> call, int port) {
        callback = call;
        server = new TheServer();
        server.start();
        this.port = port;
    }

    public class TheServer extends Thread {
        public void run() {
            try (ServerSocket mysocket = new ServerSocket(port);){

                while(true) {
                    ClientThread c = new ClientThread(mysocket.accept(), clientNumber);
                    callback.accept(new ServerUpdates("client has connected to server: " + "client #"+clientNumber, ++count));
                    clients.add(c);
                    c.start();
                    clientNumber++;
                }
            } catch (Exception e) {
                callback.accept(new ServerUpdates("Server socket did not launch, try different port number", count));
            }
        }
    }

    class ClientThread extends Thread{
        public boolean firstGame = false;
        Socket connection;
        int clientNum;
        ObjectInputStream in;
        ObjectOutputStream out;

        ClientThread(Socket s, int count) {
            this.connection = s;
            this.clientNum = count;
        }

        public void updateClients(BaccaratInfo info) {
            for (ClientThread t : clients) {
                try {
                    t.out.writeObject(info);
                } catch (Exception e) {
                    // some message
                }
            }
        }

        public boolean isNegative(Double d) {
            return Double.compare(d, 0.0) < 0;
        }

        public void codeCompressor(BaccaratInfo data) {
            if (Objects.equals(data.bankerTotal, data.playerTotal)) {
                if (Objects.equals(data.betOn, "Tie")) {
                    data.result = "Client #" + count + " has bet " + data.bet + " on " + data.betOn + ", and it is a Tie.";
                    callback.accept(new ServerUpdates(data.result, count));
                    data.whoWon = "It was a Tie.";
                    data.result = "Congrats, you bet Tie so you have won!";
                } else {
                    data.result = "Client #" + count + " has bet " + data.bet + " on " + data.betOn + ", and it is a Tie.";
                    callback.accept(new ServerUpdates(data.result, count));
                    data.whoWon = "It was a Tie.";
                    data.result = "Sorry, you bet " + data.betOn + " so you have lost!";
                }
            } else if (data.bankerTotal > data.playerTotal) {
                if (Objects.equals(data.betOn, "Banker")) {
                    data.result = "Client #" + count + " has bet " + data.bet + " on " + data.betOn + ", and the Banker wins.";
                    callback.accept(new ServerUpdates(data.result, count));
                    data.whoWon = "The Banker won.";
                    data.result = "Congrats, you bet " + data.betOn + " so you have won!";
                } else {
                    data.result = "Client #" + count + " has bet " + data.bet + " on " + data.betOn + ", and the Banker wins.";
                    callback.accept(new ServerUpdates(data.result, count));
                    data.whoWon = "The Banker won.";
                    data.result = "Sorry, you bet " + data.betOn + " so you have lost!";
                }
            } else {
                if (Objects.equals(data.betOn, "Player")) {
                    data.result = "Client #" + count + " has bet " + data.bet + " on " + data.betOn + ", and the Player wins.";
                    callback.accept(new ServerUpdates(data.result, count));
                    data.whoWon = "The Player won.";
                    data.result = "Congrats, you bet " + data.betOn + " so you have won!";
                } else {
                    data.result = "Client #" + count + " has bet " + data.bet + " on " + data.betOn + ", and the Player wins.";
                    callback.accept(new ServerUpdates(data.result, count));
                    data.whoWon = "The Player won.";
                    data.result = "Sorry, you bet " + data.betOn + " so you have lost!";
                }
            }
        }

        public void run() {
            try {
                out = new ObjectOutputStream(connection.getOutputStream());
                in = new ObjectInputStream(connection.getInputStream());
                connection.setTcpNoDelay(true);
            } catch(Exception e) {
                System.out.println("Streams not open");
            }
            BaccaratInfo info = new BaccaratInfo("New client on server: Client#"+clientNum);
            info.wantsToPlay = false;
            info.clientNum = clientNum;
            updateClients(info);
            System.out.println("New client on server: Client#" +clientNum);

            while (true) {
                try {
                    // check boolean, iuf message is true add additional message
                    System.out.println("Waiting for client make bets");
                    BaccaratInfo data = (BaccaratInfo) in.readObject();
                    BaccaratGame myBaccaratGame = new BaccaratGame(data.betOn, data.bet);

                    BaccaratGameLogic myBaccaratGameLogic = new BaccaratGameLogic();
                    myBaccaratGameLogic.playGame(data, myBaccaratGame);

                    data.bankerTotal = myBaccaratGame.theGame.handTotal(data.bankerHand);
                    data.playerTotal = myBaccaratGame.theGame.handTotal(data.playerHand);

                    if (!firstGame) {
                        firstGame = true;
                    } else {
                        callback.accept(new ServerUpdates("Client #" + clientNum + " wants to play another game!", count) );
                    }
                    codeCompressor(data);

                    if (data.playerHand.size() == 2) {
                        data.playerHand.add(new Card(0,0));
                    }
                    if (data.bankerHand.size() == 2) {
                        data.bankerHand.add(new Card(0,0));
                    }

                    data.wantsToPlay = true;
                    data.clientNum = clientNum;
                    out.writeObject(data);

                } catch (Exception e) {

                    callback.accept(new ServerUpdates("Client #"+this.clientNum+" has disconnected!", --count));
                    BaccaratInfo data = new BaccaratInfo("Client#"+this.clientNum+" has left the server!");
                    updateClients(data);
                    clients.remove(this);
                    break;
                }
            }
        }
    }
}
