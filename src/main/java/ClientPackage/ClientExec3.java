package ClientPackage;

import java.net.Socket;
// Importer la classe IOException pour gérer les erreurs d'entrée/sortie
import java.io.IOException;

// Définition de la classe Client Exec3
public class ClientExec3 {
    public static void main(String[] args) {
        try {
            // Établir la connexion avec le serveur en créant un objet Socket qui se connecte à l'hôte "localhost" sur le port 9000
            Socket socket = new Socket("localhost", 9000);

            // Créer un objet ClientMessageSender pour envoyer les messages au serveur via la connexion réseau établie
            ClientMessageSender sender = new ClientMessageSender(socket);
            sender.run();

        } catch (IOException e) {
            // Gérer les erreurs de connexion en affichant le message d'erreur à la console d'erreur standard
            System.err.println("Erreur lors de la connexion au serveur : " + e.getMessage());
        }
    }
}