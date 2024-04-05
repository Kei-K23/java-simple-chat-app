package kei.arkarmin.chatapp.client;

import java.io.*;
import java.net.*;

public class ChatClient {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private BufferedReader inputConsole;

    public ChatClient(String addr, int port) {
        try {
            socket = new Socket(addr, port);

            System.out.println("Connected to chat server : " + socket);

            inputConsole = new BufferedReader(new InputStreamReader(System.in));
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String line = "";
            while (!line.equals("exit")) {
                line = inputConsole.readLine();
                out.println(line);
                System.out.println(in.readLine());
            }

            socket.close();
            inputConsole.close();
            out.close();
        } catch (UnknownHostException e) {
            System.err.println("Unknown host : " + e.getMessage());
        } catch (IOException i) {
            System.err.println("An error occurred : " + i.getMessage());
        }
    }

    public static void main(String[] args) {
        ChatClient chatClient = new ChatClient("localhost", 5555);
    }
}
