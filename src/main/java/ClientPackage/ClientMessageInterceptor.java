package ClientPackage;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientMessageInterceptor extends Thread {

    private Socket socket;
    private ClientMessageSender sender;

    public ClientMessageInterceptor(Socket socket,ClientMessageSender sender) {
        this.socket = socket;
        this.sender=sender;
    }

    @Override
    public void run() {
        try {
            // Récupérer le flux entrant de données depuis le serveur
            DataInputStream dataIn = new DataInputStream(socket.getInputStream());
            String message = "";

            do {
                // Lire le message envoyé par le serveur
                message = dataIn.readUTF();
                if(!"exit".equals(message)){
                    System.out.println(message);
                }

            } while (!message.equals("exit"));
            dataIn.close();
        } catch (IOException ex) {
            System.err.println("Erreur lors de la lecture des messages du serveur : " + ex.getMessage());
            System.err.println("Conexion perdue avec le serveur (déconexion imprévue du serveur)");
            sender.forceInterupt();
            try{
                this.socket.close();
            }catch (IOException e){
                System.err.println("Erreur lors de la fermeture du socket ");
            }

        } finally {
            //Avertissement de déconexion du serveur
                System.out.println("Déconnexion du serveur de chat");
                interrupt();
        }
    }
}