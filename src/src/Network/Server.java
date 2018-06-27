package Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ServerSocket serverSocket;
    private ObjectInputStream objectInputStream;
    private ExecutorService pool;
    private OnMessageReceivedListener onMessageReceivedListener;
    private void initServerSocket (int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    private void listenForNewClients (OnNewClientConnection onNewClientConnection)
    {
        NewClientsListener newClientsListener = new NewClientsListener(serverSocket,onNewClientConnection);
        newClientsListener.start();
    }

    public void initServer (int port,OnMessageReceivedListener onMessageReceivedListener)
    {
        this.onMessageReceivedListener = onMessageReceivedListener;
        pool = Executors.newFixedThreadPool(10);
        try {
            initServerSocket(port);
            listenForNewClients(new OnNewClientConnection() {
                @Override
                public void onNewClientConnection(Socket client) {
                    ServerThread serverThread = new ServerThread(serverSocket, client,onMessageReceivedListener);
                    pool.execute(serverThread);
                    System.out.println("connected");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static class NewClientsListener extends Thread {

        ServerSocket serverSocket;
        OnNewClientConnection onNewClientConnection;

        public NewClientsListener(ServerSocket serverSocket, OnNewClientConnection onNewClientConnection) {
            this.serverSocket = serverSocket;
            this.onNewClientConnection = onNewClientConnection;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    onNewClientConnection.onNewClientConnection(serverSocket.accept());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class ServerThread implements Runnable {
        private Socket clientSocket;
        private ObjectInputStream objectInputStream;
        private OnMessageReceivedListener onMessageReceivedListener;

        public ServerThread(ServerSocket serverSocket, Socket clientSocket,
                            OnMessageReceivedListener onMessageReceivedListener) {
            this.onMessageReceivedListener = onMessageReceivedListener;
            this.clientSocket = clientSocket;
            try {
               objectInputStream = (new ObjectInputStream(clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Object o = objectInputStream.readObject();
                    onMessageReceivedListener.onMessageReceived(o,clientSocket.getInetAddress().toString());
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public interface OnNewClientConnection {
        public void onNewClientConnection (Socket client);
    }

    public interface OnMessageReceivedListener {
        public void onMessageReceived (Object o,String ip);
    }
}
