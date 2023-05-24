package utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ClientRequest;
import util.ResponseCode;
import util.ServerResponse;
import util.User;

import java.io.*;


public class Requester{
    private Logger logger = LoggerFactory.getLogger("Requester");
    private ClientRequest userRequest;
    private HandleRequest handleRequest;
    private ServerResponse responseToUser;

    public Requester(ClientRequest userRequest, HandleRequest handleRequest) {
        this.userRequest = userRequest;
        this.handleRequest = handleRequest;
    }

    public void handleRequest(ObjectOutputStream objectOutputStream) {
        try {

            responseToUser = handleRequest.compute(userRequest);
            logger.debug("Request '" + userRequest.getCommandName() + "' is completed.");

            objectOutputStream.writeObject(responseToUser);
            logger.debug("Response on request '" + userRequest.getCommandName() + "' is send.");
            objectOutputStream.flush();


        }catch (IOException e) {
            throw new RuntimeException(e);
        }

    }



}
