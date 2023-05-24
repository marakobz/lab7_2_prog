package commands;

import exceptions.NoSuchCommandException;
import exceptions.WrongAmountOfElementsException;
import util.User;
import utility.CollectionManager;
import utility.Console;
import utility.ResponseOutputer;

import java.io.IOException;


/**
 * Command 'exit'. Saves the collection to a file.
 */
public class ExitCommand extends AbstractCommand{
    private CollectionManager collectionManager;


    public ExitCommand(CollectionManager collectionManager){
        super("exit", "end the program (without saving to a file)");
        this.collectionManager = collectionManager;

    }

    /**
     * Execute of 'exit' command.
     */
    @Override
    public void execute(String argument, Object object, User user) throws NoSuchCommandException {
        try {
            if (!argument.isEmpty() || object != null) throw new WrongAmountOfElementsException();
            ResponseOutputer.appendln(collectionManager.exit());
        } catch (WrongAmountOfElementsException exception) {
            Console.println("Used: '" + getName() + "'");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
