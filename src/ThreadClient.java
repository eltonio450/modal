import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

import Stockage.Machine;
import Stockage.Paquet;
import Utilitaires.Global;
import Utilitaires.Message;
import Utilitaires.Utilitaires;


public class ThreadClient implements Runnable { 

  public void run() { 
    try { 
      try (SocketChannel clientSocket = SocketChannel.open()) {
        Paquet aEnvoyer = new Paquet(1,Global.MYSELF) ;
        for(int i = 0 ; i< 5 ; i++) {
          aEnvoyer.otherHosts.add(i, Global.MYSELF);
        }
        Machine correspondant = new Machine("localhost",5656) ;
        //init connection
        InetSocketAddress local = new InetSocketAddress(0); 
        clientSocket.bind(local); 
        InetSocketAddress remote = new InetSocketAddress(correspondant.ipAdresse, correspondant.port); 
        clientSocket.connect(remote); 

        //ask to exchange
        ByteBuffer buffer = Utilitaires.stringToBuffer(Message.EXCHANGE) ;
        clientSocket.write(buffer) ;
        buffer.clear() ;
        System.out.println("Client attend r�ponse") ;
        clientSocket.read(buffer) ;
        System.out.println("Client a re�u r�ponse") ;
        buffer.flip() ;
        String s = Utilitaires.buffToString(buffer) ;
        System.out.println(s) ;
    
        if(!s.equals(Message.DEMANDE_ID)){
          System.out.println(false) ;
        }

        buffer = Utilitaires.stringToBuffer(aEnvoyer.idGlobal) ;
        clientSocket.write(buffer) ;
        System.out.println("Client a envoy�") ;
        buffer = ByteBuffer.allocate(100) ;
        buffer.clear() ;
        clientSocket.read(buffer) ;
        buffer.flip() ;
        System.out.println("client : " + new String(buffer.array(),StandardCharsets.UTF_16BE)) ;
        

        if (true){
          //exchange can begin : send its package
          aEnvoyer.envoyerPaquet(clientSocket);
          System.out.println("Client a envoy� Paquet") ;
          /*if(recoitPaquet(clientSocket)){
            aEnvoyer.removePaquet();
            System.out.println(true) ;
          }
          else {} */
        }

        else {
         System.out.println("Echec") ;
        }
      }
      catch(Exception e){

      }
    }
    catch (Exception e) {throw new RuntimeException(e); }
  } 


}
