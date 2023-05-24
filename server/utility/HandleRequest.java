package utility;

import commands.AbstractCommand;
import commands.ICommand;
import exceptions.NoSuchCommandException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ClientRequest;
import util.ResponseCode;
import util.ServerResponse;
import util.User;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PipedReader;
import java.util.concurrent.locks.ReentrantLock;

public class HandleRequest extends ReentrantLock {
    private CommandManager commandManager;
    private ServerResponse response;

    public HandleRequest(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    protected ServerResponse compute(ClientRequest request) {
        User hashedUser = new User(
                request.getUser().getUsername(),
                PasswordHasher.hashPassword(request.getUser().getPassword())
        );
        executeCommand(request.getCommandName(), request.getCommandArguments(), request.getObjectArgument(), hashedUser);
        //System.out.println(data);
        return new ServerResponse(ResponseOutputer.getAndClear(), ResponseCode.SUCCESS);
    }


    /**
     * Executes a command from a request.
     *
     * @param command               Name of command.
     * @param commandObjectArgument Object argument for command.
     * @return
     */
    private synchronized ServerResponse executeCommand(String command, String commandStringArgument,
                                                       Object commandObjectArgument, User user) {
        if (commandManager.getCommands().containsKey(command)) {
            AbstractCommand abstractCommand = commandManager.getCommands().get(command);
            try {
                abstractCommand.execute(commandStringArgument, commandObjectArgument, user);
            } catch (NoSuchCommandException e) {

                response = new ServerResponse("Unknown command detected: " + command, ResponseCode.ERROR);

            }
        } else {
            response = new ServerResponse("Unknown command detected: " + command, ResponseCode.ERROR);

        }
        return response;
    }
}