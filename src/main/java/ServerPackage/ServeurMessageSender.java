package ServerPackage;

import Classes.Client;

import java.io.*;

public class ServeurMessageSender extends Thread{

    private Client client;
     private String origine;
    private String message;
    private int quoiFaire;

    public ServeurMessageSender(Client client, String message, int quoiFaire, String origine) {
        this.client= client;
        this.message = message;
        this.quoiFaire=quoiFaire;
        this.origine = origine;
    }

    public void run(){
        try{
            DataOutputStream dataOut = new DataOutputStream(client.getSocket().getOutputStream());
            switch (quoiFaire){
                case 0:
                    if(origine.equals(client.getPsuedo())){
                        dataOut.writeUTF(origine+" a rejuint la conversation ");
                        if(ServeurExec.getClients().size()==1){
                            dataOut.writeUTF("Vous etes atuellement le seul utilisateur connecté");
                        }else{
                            dataOut.writeUTF("Actuellement les utilisateurs connectés sont : ");
                            int i = 1;
                            for (Client cl : ServeurExec.getClients()){
                                if( !cl.getPsuedo().equals("") && !cl.getPsuedo().equals(client.getPsuedo())){
                                    dataOut.writeUTF(i +":"+cl.getPsuedo());
                                    i++;
                                }
                            }
                        }
                        dataOut.writeUTF("-----------------------------");
                    }

                    else{
                        dataOut.writeUTF(origine+" a rejuint la conversation");
                    }
                    break;
                case 1 :
                    dataOut.writeUTF(origine+" a dit : "+message);
                    break;
                case 2 :
                    if(origine.equals(client.getPsuedo())){
                        dataOut.writeUTF("exit");
                    }else{
                        dataOut.writeUTF(origine+" a quitté la conversation");
                    }
                    break;
            }
        }
        catch(IOException e){
            System.err.println("Exception rencontré au niveau du MessageSender: "+ e.toString());
            System.out.println("---Envoi de message au socket fermé par déconexion non prévue--- ");
        }


    }

}
