package kei.arkarmin.chatapp.server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);

            System.out.println("Server started. Waiting for client connection.");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected : " + clientSocket);

                ClientHandler clientThread = new ClientHandler(clientSocket, clients);

                clients.add(clientThread);

                new Thread(clientThread).start();
            }
        } catch (IOException e) {
            System.err.println("An error occurred while creating the socket server : " + e.getMessage());
            System.exit(1);
        }
    }
}

class ClientHandler implements Runnable {

    private Socket socket;
    private List<ClientHandler> clients;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket socket, List<ClientHandler> clients) {
        this.socket = socket;
        this.clients = clients;

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.err.println("An error occurred while creating the client handler : " + e.getMessage());
            System.exit(1);
        }
    }

    public void run() {
        try {
            String message;

            while ((message = in.readLine()) != null) {
                System.out.println("Received message : " + message);

                for (ClientHandler client : clients) {
                    if (client != this) {
                        client.out.println(message);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}