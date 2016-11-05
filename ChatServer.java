import java.net.*;
import java.io.*;
import java.util.LinkedList;

public class ChatServer extends Thread{
   private ServerSocket serverSocket;
   private LinkedList<Socket> listOfClients = new LinkedList<Socket>();
   
   public ChatServer(int port) throws IOException{
	  serverSocket = new ServerSocket(port);
      System.out.println("Chat server started.");
	  System.out.println("Listening on port " + serverSocket.getLocalPort() + ".");
   }

   public void run(){
      boolean connected = true;
      
	  while(connected){
         try{ 
            /* Start accepting data from the ServerSocket */
			Socket server = serverSocket.accept();
            System.out.println("Connected: " + server.getRemoteSocketAddress());
			listOfClients.add(server);
            
			Thread messageHandler = new Thread(){
				public void run(){
                    while(true){
                        try {
                            /* Read data from the ClientSocket */
                            DataInputStream in = new DataInputStream(server.getInputStream());
                            String message = in.readUTF();
                            
                            for(Socket client:listOfClients){
                                if(client == server) continue; //skip itself
                                DataOutputStream out = new DataOutputStream(client.getOutputStream());
                                /* Send data to the ClientSocket */
                                out.writeUTF(message);
                                out.flush();
                            }
                        } catch(Exception e) {
                            listOfClients.remove(server);
                        }
                    }
				}			
			};

			messageHandler.start();           
         } catch(IOException e) {
            System.out.println("Usage: java ChatServer <port no.>");
            break;
         }
      } 
   }

   public static void main(String [] args){
      try{
         int port = Integer.parseInt(args[0]);
         Thread t = new ChatServer(port);
         t.start();
      } catch(IOException e){
         System.out.println("Usage: java ChatServer <port no.>");
      } catch(ArrayIndexOutOfBoundsException e) {
         System.out.println("Usage: java ChatServer <port no.> ");
      }
   }
}