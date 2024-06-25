package VerteilteSysteme;

import java.io.*;
import java.net.*;
import java.util.*;

public class Broker {
    private static List<Socket> subscribedClients = new ArrayList<>();
    
    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(1337)) 
        {	
        	System.out.println("Server started on port 1337");
            while (true) {
                Socket clientSocket = server.accept();
                new Thread(new Clientthread(clientSocket)).start();
            }
        } 
        catch (IOException e) 
        {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    static class Clientthread implements Runnable 
    {
        private Socket clientSocket;

        public Clientthread(Socket socket) 
        {
            this.clientSocket = socket;
        }

        public void run() 
        {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                String message;
                while ((message = br.readLine()) != null) {
                    if ("SUBSCRIBE".equalsIgnoreCase(message)) {
                        subscribedClients.add(clientSocket);
                        System.out.println("Client subscribed to temperature updates.");
                    } else if ("UNSUBSCRIBE".equalsIgnoreCase(message)) {
                        subscribedClients.remove(clientSocket);
                        System.out.println("Client unsubscribed from temperature updates.");
                    } else {
                        broadcastMessage(message, clientSocket);
                    }
                }
            } catch (IOException e) {
                System.err.println("Connection error: " + e.getMessage());
            }
        }

        private void broadcastMessage(String message, Socket senderSocket) 
        {
            for (Socket client : subscribedClients) {
                if (client != senderSocket) {
                    try {
                        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                        out.println(message);
                    } catch (IOException e) {
                        System.err.println("Error broadcasting message to client");
                    }
                }
            }
        }
    }
}



