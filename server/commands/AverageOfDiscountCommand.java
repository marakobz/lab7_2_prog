package commands;

import exceptions.WrongAmountOfElementsException;
import models.Ticket;
import util.User;
import utility.CollectionManager;
import utility.Console;
import utility.ResponseOutputer;

import java.util.Iterator;

/**
 * Command 'average_of_discount'. Saves the collection to a file.
 */
public class AverageOfDiscountCommand extends AbstractCommand {
    CollectionManager collectionManager;

    public AverageOfDiscountCommand(CollectionManager collectionManager) {
        super("average_of_discount", "print the average value of the discount field for all items in the collection");
        this.collectionManager = collectionManager;
    }

    /**
     * Execute of 'average_of_discount' command.
     */


    @Override
    public void execute(String argument, Object object, User user){
        try {
            if (!argument.isEmpty()) throw new WrongAmountOfElementsException();
            Iterator<Ticket> disc = collectionManager.getCollection().iterator();
            int sum = 0;
            int count = 0;
            while (disc.hasNext()) {
                Ticket ticket = disc.next();
                sum += ticket.getDiscount();
                count += 1;
            }
            ResponseOutputer.appendln("The average \"discount\" is: " + sum / count);
        } catch (WrongAmountOfElementsException e) {
            Console.println("Used: '" + getName() + "'");
        }
    }
}
