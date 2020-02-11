package qconlondon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    private static final String HOST_MACHINE = "127.0.0.1";

    public static void main(String args[]) {
        String host = HOST_MACHINE;
        int port = Server.PORT_NUMBER;
        new Client(host, port);
    }

    public Client(String host, int port) {
        try {
            System.out.println("Connecting to host " + host + " on port " + port + ".");

            Socket echoSocket = null;
            PrintWriter out = null;
            BufferedReader in = null;

            try {
                echoSocket = new Socket(host, 8081);
                out = new PrintWriter(echoSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            } catch (UnknownHostException e) {
                System.err.println("Unknown host: " + host);
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Unable to get streams from server");
                System.exit(1);
            }

            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                System.out.print("client: ");
                String userInput = stdIn.readLine();
                if ("q".equals(userInput)) {
                    break;
                }
                out.println(userInput);
                // System.out.println("server: " + in.readLine());
            }

            out.close();
            in.close();
            stdIn.close();
            echoSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}