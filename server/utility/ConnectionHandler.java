package utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ClientRequest;
import util.ServerResponse;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.concurrent.*;

public class ConnectionHandler implements Runnable{

    private Logger logger = LoggerFactory.getLogger("ConnectionHandler");
    private ServerManager server;
    private Socket clientSocket;
    private HandleRequest handleRequest;


    public ConnectionHandler(ServerManager server, Socket clientSocket, HandleRequest handleRequest){
        this.server = server;
        this.clientSocket = clientSocket;
        this.handleRequest = handleRequest;

    }
    /**
     * Main handling cycle.
     */
    @Override
    public void run() {
        ClientRequest userRequest = null;
        boolean stopFlag = false;
        try (ObjectInputStream clientReader = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream clientWriter = new ObjectOutputStream(clientSocket.getOutputStream())) {
            do {
                try {
                    userRequest = (ClientRequest) clientReader.readObject();

                    Requester requester = new Requester(userRequest, handleRequest);
                    Runnable task = () -> {
                        requester.handleRequest(clientWriter);

                    };

                    Thread thread = new Thread(task);
                    thread.start();
                    thread.join();


                } catch (IOException | ClassNotFoundException e) {
                    logger.error(e.getMessage());
                    throw new RuntimeException();
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                    throw new RuntimeException(e);
                }

                logger.debug("Active threads:" + Thread.activeCount());
            } while (!stopFlag);
        } catch (CancellationException exception) {
            logger.warn("A multithreading error occurred while processing the request");
        } catch (IOException exception) {
            logger.warn("Unexpected disconnection with client" + clientSocket.getInetAddress());
        }
    }


}
