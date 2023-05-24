package commands;

import exceptions.WrongAmountOfElementsException;
import util.User;
import utility.CollectionManager;
import utility.Console;
import utility.ResponseOutputer;

import java.time.LocalDate;

/**
 * Command 'info'. Saves the collection to a file.
 */

/*
TODO change smth
 */
public class InfoCommand extends AbstractCommand {
    private LocalDate creationDate;
    private CollectionManager collectionManager;

    public InfoCommand(CollectionManager collectionManager) {
        super("info", "shows information about the commands");
        this.collectionManager = collectionManager;
        creationDate = LocalDate.now();
    }

    /**
     * Execute of 'info' command.
     */

    @Override
    public void execute(String argument, Object object, User user) {
        try {
            if (!argument.isEmpty() || object != null) throw new WrongAmountOfElementsException();

            ResponseOutputer.appendln(
                    "Info about collection:"
                            + "  Creation Date:" + creationDate + " \n"
                            + "  Number of elements:" + collectionManager.collectionSize()
            );
            //сделать функцию/метод который бы печатал строку и сохранял бы её
        } catch (WrongAmountOfElementsException exception) {
            Console.println("Used: '" + getName() + "'");
        }
    }

}

