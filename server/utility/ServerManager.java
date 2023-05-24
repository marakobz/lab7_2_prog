package utility;

import ch.qos.logback.core.net.server.Client;
import commands.ClearCommand;
import exceptions.ClosingSocketException;
import exceptions.ConnectionErrorException;
import exceptions.OpeningServerSocketException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ClientRequest;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Runs the server.
 */
public class ServerManager {
    private int port;
    private ServerSocket serverSocket;
    private Logger logger = LoggerFactory.getLogger("ServerManager");
    private boolean isStopped;
    private final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(4);
    private final ReentrantLock reentrantLock;
    private HandleRequest handleRequest;

    public ServerManager(int port, HandleRequest handleRequest) {
        this.port = port;
        this.reentrantLock = new ReentrantLock();
        this.handleRequest = handleRequest;
    }

    /**
     * Begins server operation.
     */
    public void run() {
        try {
            openServerSocket();
            while (!isStopped()) {
                try {
                    acquireConnection();
                    if (isStopped()) throw new ConnectionErrorException();
                    Socket clientSocket = connectToClient();
                    fixedThreadPool.submit(new ConnectionHandler(this, clientSocket, handleRequest));
                    logger.info("Connected with " + clientSocket.getRemoteSocketAddress() + " client");
                } catch (ConnectionErrorException exception) {
                    if (!isStopped()) {
                        Console.printerror("Mistake occurred while connecting to client");
                        logger.error("Mistake occurred while connecting to client");
                    } else break;
                }
            }
            fixedThreadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            logger.info("Work of server is ended.");
        } catch (OpeningServerSocketException exception) {
            Console.printerror("Server cannot be started");
            logger.error("Server cannot be started");
        } catch (InterruptedException e) {
            Console.printerror("Mistake occurred while disconnecting with clients");
        }
    }

    /**
     * Acquire connection.
     */
    public void acquireConnection() {
        reentrantLock.tryLock();
        logger.info("Permission for a new connection has been received.");
    }

    /**
     * Release connection.
     */
    public void releaseConnection() {
        reentrantLock.lock();
        logger.info("A connection break has been registered.");
    }

    /**
     * Finishes server operation.
     */
    public synchronized void stop() {
        try {
            logger.debug("Ending work of server...");
            if (serverSocket == null) throw new ClosingSocketException();
            isStopped = true;
            fixedThreadPool.shutdown();
            serverSocket.close();
            logger.debug("Ending work with connected clients...");
            logger.debug("Work of server is ended.");
        } catch (ClosingSocketException exception) {
            Console.printerror("Incapable to end the work of server which is not run");
            logger.error("Incapable to end the work of server which is not run");
        } catch (IOException exception) {
            Console.printerror("Mistake occurred while ending the work of server");
            Console.println("Ending work with connected clients...");
            logger.error("Mistake occurred while ending the server");
        }
    }

    /**
     * Checked stops of server.
     *
     * @return Status of server stop.
     */
    private synchronized boolean isStopped() {
        return isStopped;
    }

    /**
     * Open server socket.
     */
    private void openServerSocket() throws OpeningServerSocketException {
        try {
            logger.debug("Running the server...");
            serverSocket = new ServerSocket(port);
            logger.info("Server is run.");
        } catch (IllegalArgumentException exception) {
            logger.error("Port '" + port + "' is beyond possible values");
            throw new OpeningServerSocketException();
        } catch (IOException exception) {
            logger.error("Mistake occurred while trying to use port '" + port + "'");
            throw new OpeningServerSocketException();
        }
    }

    /**
     * Connecting to client.
     */
    private Socket connectToClient() throws ConnectionErrorException {
        try {
            logger.info("Listening to the port '" + port + "'...");
            Socket clientSocket = serverSocket.accept();
            logger.info("Connection with client is set");
            return clientSocket;
        } catch (IOException exception) {
            throw new ConnectionErrorException();
        }
    }


}
