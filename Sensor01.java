package VerteilteSysteme;

import java.net.*;
import java.io.*;

public class Sensor01 {
    
    private static void sendMsg(String msg) throws IOException {
        try (Socket s = new Socket("localhost", 1337)) {
            System.out.println("Connected to server");
            PrintWriter networkOut = new PrintWriter(s.getOutputStream(), true);
            networkOut.println(msg);
            System.out.println("Message sent: " + msg);
        } catch (IOException e) {
            System.err.println("Failed to connect or send message: " + e.getMessage());
            throw e;
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.print("Sensor Started");
        double x = 20;
        String theLine = x + "°C";
        while (x < 100) {
        	 Thread.sleep(10000);
        	sendMsg(theLine);
            x = x + (Math.random() - 0.5) * 5;
            theLine = Math.round(x) + "°C";
        }
    }
}
