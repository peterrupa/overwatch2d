package com.overwatch2d.game;

/**
 * Created by Pauweey on 12/7/2016.
 */
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ChatClient{
    static String username = "";

    public static String repeat(String s, int n) {
        return new String(new char[n]).replace("\0", s);
    }

    public static void main(String [] args){
        try {
            String serverName = args[0]; //get IP address of server from first param
            int port = Integer.parseInt(args[1]); //get port from second param


         /* Open a ClientSocket and connect to ServerSocket */
            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);
            System.out.println("Just connected to " + client.getRemoteSocketAddress());

            Thread sendMessage = new Thread(){
                public void run(){
                    try{
                        System.out.print("Enter username: ");
                        username = new Scanner(System.in).nextLine();
                        // username = Character.toString((char) System.in.read());

                        while(true) {
                            System.out.print(username + ": ");
                            String message = new Scanner(System.in).nextLine(); //get message

						/* Send data to the ServerSocket */
                            OutputStream outToServer = client.getOutputStream();
                            DataOutputStream out = new DataOutputStream(outToServer);
                            out.writeUTF(username + ": " + message);
                            out.flush();
                        }
                    } catch(Exception e) {
                        System.exit(1);
                    }

                }
            };


            Thread receiveMessage = new Thread(){
                public void run(){
                    try{
					/* Receive data from the ServerSocket */
                        InputStream inFromServer = client.getInputStream();
                        DataInputStream in = new DataInputStream(inFromServer);

                        while(true) {
                            String message = in.readUTF();

                            if(message != "") {
                                System.out.print(repeat("\b", username.length() + ": ".length()));
                                System.out.println(message);
                                System.out.print(username + ": ");
                            }
                        }
                    } catch(Exception e) {
                        System.exit(1);
                    }
                }
            };

            //start threads
            sendMessage.start();
            receiveMessage.start();

        } catch(IOException e){
            //e.printStackTrace();
            System.out.println("Cannot find Server");
        } catch(ArrayIndexOutOfBoundsException e){
            System.out.println("Usage: java GreetingClient <server ip> <port no.>");
        }
    }
}

