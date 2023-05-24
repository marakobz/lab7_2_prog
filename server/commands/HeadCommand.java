package commands;

import exceptions.NoSuchCommandException;
import util.User;
import utility.CollectionManager;
import utility.Console;
import utility.ResponseOutputer;

/**
 * Command 'head'. Saves the collection to a file.
 */
public class HeadCommand extends AbstractCommand {
    CollectionManager collectionManager;

    public HeadCommand(CollectionManager collectionManager) {
        super("head", " show first element of collection");
        this.collectionManager = collectionManager;
    }

    /**
     * Execute of 'head' command.
     */

    @Override
    public void execute(String argument, Object object, User user) throws NoSuchCommandException {
        if (!argument.isEmpty() || object != null) {
            throw new NoSuchCommandException();
        }
        if (collectionManager.getCollection().isEmpty()) {
            ResponseOutputer.appendln("Collection is empty");
        }else {
            ResponseOutputer.appendln(collectionManager.getFirst());
        }

    }
}
