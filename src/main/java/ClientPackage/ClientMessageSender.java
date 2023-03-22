package ClientPackage;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientMessageSender extends Thread {
    private Socket socket;
    private boolean running=true;
    public  void forceInterupt(){
       System.exit(1);
    }

    public ClientMessageSender(Socket sock) {
        this.socket = sock;
    }

    public void run() {

        try {
            // Récupérer le flux de sortie pour envoyer les données au serveur

            DataOutputStream dataOut = new DataOutputStream(this.socket.getOutputStream());
            // Récupérer le pseudo de l'utilisateur
            Scanner sc = new Scanner(System.in);
            System.out.println("Bienvenue dans l'application de chat \nEntrez votre pseudo:");
            String pseudo = sc.nextLine();

            // Envoyer le pseudo au serveur
            dataOut.writeUTF(pseudo);

            // Créer un objet ClientMessageInterceptor pour intercepter les messages entrants du serveur
            ClientMessageInterceptor interceptor = new ClientMessageInterceptor(this.socket,this);
            interceptor.start();

            // Envoyer les messages entrés par l'utilisateur au serveur
            String message="";
            boolean check = false;

            do {
                message = sc.nextLine();
                dataOut.writeUTF(message);
            } while (!message.equals("exit"));

        } catch (IOException ex) {
            System.err.println("Erreur lors de l'envoi de message au serveur : " + ex.getMessage());
            interrupt();
        }
    }
}