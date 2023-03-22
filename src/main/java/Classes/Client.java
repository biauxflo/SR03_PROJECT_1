package Classes;

import java.net.Socket;
import java.util.UUID;

public class Client {

    private final String id;
    private final Socket socket;
    private String pseudo;

    public Client(Socket socket,String pseudo) {
        this.id = UUID.randomUUID().toString();
        this.socket = socket;
        this.pseudo = "";
    }

    public String getId() {
        return id;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPseudo() {
        return pseudo;
    }


    public String toString(){
        return ("Id du client : "+getId());
    }
}
