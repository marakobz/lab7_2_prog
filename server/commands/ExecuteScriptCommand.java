package commands;

import exceptions.NoSuchCommandException;

import util.User;
import utility.Console;
import utility.ResponseOutputer;
import utility.UserIO;


/**
 * Command 'execute_script'. Saves the collection to a file.
 */
public class ExecuteScriptCommand extends AbstractCommand{
    private UserIO userIO;
    public ExecuteScriptCommand(UserIO userIO){
        super("execute_script", "read and execute the script from the specified file");
        this.userIO = userIO;
    }

    /**
     * Execute of 'execute_script' command.
     */

    @Override
    public void execute(String argument, Object object, User user) throws NoSuchCommandException {
        if (argument.isEmpty() || object != null) {
            Console.println("Used: '" + getName() + "'");
            throw new NoSuchCommandException();
        }
        ResponseOutputer.appendln("Reading the given script ...");
        ResponseOutputer.appendln(userIO.startReadScript(argument));

    }
}

