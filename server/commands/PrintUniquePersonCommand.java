package commands;

import exceptions.CollectionIsEmptyException;
import models.Person;
import models.Ticket;
import util.User;
import utility.CollectionManager;
import utility.Console;
import utility.ResponseOutputer;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Command 'print_unique'. Saves the collection to a file.
 */

public class PrintUniquePersonCommand extends AbstractCommand {
    CollectionManager collectionManager;

    public PrintUniquePersonCommand(CollectionManager collectionManager) {
        super("print_unique_person", "output the unique values of the person field of all items in the collection");
        this.collectionManager = collectionManager;
    }

    /**
     * Execute of 'print_unique' command.
     */
    @Override
    public void execute(String argument, Object object, User user) {
        try {
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();
            List<Person> personSet = collectionManager.getCollection()
                    .stream()
                    .map(Ticket::getPerson)
                    .collect(Collectors.toList());
            for (Person person : personSet){
                ResponseOutputer.appendln(person);
            }
        }catch (CollectionIsEmptyException e){
            ResponseOutputer.appendln("Collection is empty");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Mistake occurred, try again");
        }
    }

}
