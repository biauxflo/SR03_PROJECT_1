package ServerPackage;

import Classes.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServeurMessageInterceptor extends Thread {

    private static final Logger LOGGER = Logger.getLogger(ServeurMessageInterceptor.class.getName());
    private static final int MAX_PSEUDO_ATTEMPTS = 4;

    private Client client;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public ServeurMessageInterceptor(Client client) {
        this.client = client;
    }

    public void run() {
        try{
            String message;
            DataOutputStream dataOut = new DataOutputStream(client.getSocket().getOutputStream());
            DataInputStream dataIn = new DataInputStream(client.getSocket().getInputStream());
            try {
                int pseudoAttempts = 0;
                String pseudo;
                do {
                    pseudoAttempts++;
                    pseudo = dataIn.readUTF();
                    if (ServeurExec.isPseudoTaken(pseudo)) {
                        dataOut.writeUTF("Ce pseudo est déjà pris, veuillez en choisir un autre.");
                    }
                } while (ServeurExec.isPseudoTaken(pseudo) && pseudoAttempts < MAX_PSEUDO_ATTEMPTS);

                if (!ServeurExec.getListClients().containsKey(client.getId())) {
                    LOGGER.log(Level.WARNING, "Client with ID {0} not found in client list.", client.getId());
                    return;
                }

                Client newClient = ServeurExec.getListClients().get(client.getId());
                newClient.setPseudo(pseudo);
                ServeurExec.addClient(newClient);
                this.setClient(newClient);
                ServeurExec.broadcast("", this.getClient(), 0);

                do {
                    message = dataIn.readUTF();
                    if (!message.equals("exit")) {
                        ServeurExec.broadcast(message, this.getClient(), 1);
                    }
                } while (!message.equals("exit"));
            } catch (IOException e) {
                System.err.println("Exception rencontrée au niveau du MessageInterceptor : "+e.getMessage());
                System.out.println("---Déconexion non prévue de l'utilisateur---");
            } finally {
                try {
                    ServeurExec.broadcast("",this.getClient(),2);
                    dataIn.close();
                    dataOut.close();
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Exception lors de la fermeture de la connexion : ", e);
                }finally {
                    ServeurExec.removeClient(client);
                }
            }

        }
        catch(IOException e){
            System.out.println("Exception rencontré au niveau du MessageInterceptor : "+ e.toString());
            ServeurExec.broadcast("",this.getClient(),2);
            ServeurExec.removeClient(client);
        }
    }


}