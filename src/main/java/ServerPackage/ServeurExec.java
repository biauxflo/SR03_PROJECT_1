package ServerPackage;
import Classes.Client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServeurExec {

    // Map pour stocker les clients connectés
    private static final Map<String, Client> clients = new ConcurrentHashMap<>();

    // ThreadPool pour la gestion des threads
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    // Méthode pour récupérer les clients connectés
    public static Collection<Client> getClients(){
        return clients.values();
    }

    // Méthode pour récupérer la liste des clients connectés
    public static Map<String, Client> getListClients(){
        return clients;
    }
    //Méthode pour vérifier la présence d'un psuedo dans la liste des utilisateurs

    public static boolean isPseudoTaken(String pseudo){

        for (Client cl : ServeurExec.getClients()){
            if (cl.getPseudo().equals(pseudo)){
                return true;

            }
        }
        return false;
    }

    // Méthode pour ajouter un client à la liste des clients connectés
    public static void addClient (Client client ){
        clients.put(client.getId(),client);
    }

    // Méthode pour supprimer un client de la liste des clients connectés
    public static void removeClient(Client client) {
        try{

            clients.remove(client.getId());

            client.getSocket().close();
            System.out.println("Client déconnecté : " + client);
        }catch (IOException e){
            System.err.println("Exception rencontré lors de la fermeture du socket ");
        }

    }

    // Méthode pour envoyer un message à tous les clients connectés
    public static void broadcast(String message, Client sender,int action) {
        for (Client client : clients.values()) {
            // On vérifie que le client a un pseudonyme
            if (!client.getPseudo().equals("")) {
                ServeurMessageSender mesSender = new ServeurMessageSender(client, message, action ,sender.getPseudo());
                // On soumet la tâche à exécuter dans le ThreadPool
                mesSender.run();
            }
        }
    }

    public static void main(String[] args) {
        try (ServerSocket servSock = new ServerSocket(9000)) {
            System.out.println("Serveur démarré sur le port 9000.");
            while (true) {
                Socket newSock = servSock.accept();
                Client newClient = new Client(newSock,"");
                clients.put(newClient.getId(), newClient);
                System.out.println("Nouveau client connecté : " + newClient);
                // On soumet la tâche de gestion de message du client dans le ThreadPool
                executor.submit(new ServeurMessageInterceptor(newClient));
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de l'ouverture du serveur : " + e.getMessage());
        } finally {
            // On arrête le ThreadPool à la fin de l'exécution
            executor.shutdown();
        }
    }
}