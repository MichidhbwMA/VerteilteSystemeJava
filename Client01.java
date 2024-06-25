package VerteilteSysteme;

import java.io.*;
import java.util.Scanner;
import java.net.*;

public class Client01 {

    public static void main(String[] args) throws Exception {
        // Start a thread to receive messages from the server
        new Thread(new MessageReceiver()).start();
    }
    
    static class MessageReceiver implements Runnable {
        public void run() {
            try (Socket s = new Socket("localhost", 1337);
                 BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                 PrintWriter networkOut = new PrintWriter(s.getOutputStream(), true);
                 Scanner scanner = new Scanner(System.in)) {
                    
                // Lies die Eingabe von der Konsole
                System.out.print("Willst du die Temperatur abonnieren? (ja/nein): ");
                String msg = scanner.nextLine();

                // Sende die Nachricht an den Server
                networkOut.println(msg.equals("ja") ? "SUBSCRIBE" : "UNSUBSCRIBE");
                System.out.println("Message sent: " + msg);

                // Empfange Nachrichten vom Server
                String message;
                while ((message = br.readLine()) != null) {
                    System.out.print("\nReceived message: " + message);
                }
            } catch (IOException e) {
                System.err.println("Failed to connect to server for receiving messages: " + e.getMessage());
            }
        }
    }
}


