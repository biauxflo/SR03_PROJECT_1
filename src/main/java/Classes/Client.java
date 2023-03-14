package Classes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

public class Client {

    private final String id;
    private final Socket socket;
    private String psuedo;

    public Client(Socket socket,String psuedo) {
        this.id = UUID.randomUUID().toString();
        this.socket = socket;
        this.psuedo = "";
    }

    public String getId() {
        return id;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setPsuedo(String psuedo) {
        this.psuedo = psuedo;
    }

    public String getPsuedo() {
        return psuedo;
    }


    public String toString(){
        return ("Id du client : "+getId());
    }
}
