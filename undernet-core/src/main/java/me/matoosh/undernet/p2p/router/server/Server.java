package me.matoosh.undernet.p2p.router.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Server part of the router.
 *
 * Created by Mateusz Rębacz on 30.01.2017.
 */

public class Server {
    /**
     * Port used by the server.
     */
    public int port;
    /**
     * Server socket of the server.
     */
    public ServerSocket serverSocket;
    /**
     * Whether the server is running.
     */
    public boolean running = false;
    /**
     * Whether the server is accpeting connections.
     */
    private boolean acceptingConnections = false;
    /**
     * List of the active connections.
     */
    public ArrayList<Connection> connections = new ArrayList<Connection>();

    /**
     * Creates a server instance using a specified port.
     * @param port
     */
    public Server(int port) {
        this.port = port;
    }

    /**
     * Starts the server.
     * @throws Exception
     */
    public void start() throws Exception {
        //The server loop.
        Thread connectionAssignmentThread = new Thread(new Runnable() {
            @Override
            public void run() {
                running = true;
                acceptingConnections = true;

                try {
                    serverSocket = new ServerSocket(42069);


                    while(running) {
                        //If no new connections are awaiting, continue the loop.
                        if(!acceptingConnections) continue;

                        //Set the pending connection flag to false.
                        acceptingConnections = false;

                        //Listening for the incoming connection and accepting it on a separate thread.
                        Thread t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    connections.add(new Connection(Server.this, Thread.currentThread()));
                                } catch (Exception e) {
                                    Logger.getGlobal().info("Connection error: " + e.toString());
                                }
                            }
                        });

                        t.start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    //Server stopped.
                    running = false;
                }
            }
        });
        connectionAssignmentThread.start();
    }

    /**
     * Stops the server.
     */
    public void stop() {
        //Stopping the server loop.
        running = false;

        //Interrupting all the connections.
        for (Connection c:
             connections) {
            c.drop();
        }
    }

    //Events

    /**
     * Called when a connection has been established.
     * @param c
     */
    public void onConnectionEstablished(Connection c) {
        System.out.println("New connection established with " + c.node);
        //Accepting new connections.
        acceptingConnections = true;
    }
}