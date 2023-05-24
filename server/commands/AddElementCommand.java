package commands;

import exceptions.DatabaseHandlingException;
import exceptions.NoSuchCommandException;
import exceptions.WrongAmountOfElementsException;
import util.TicketRaw;
import util.User;
import utility.*;

import java.time.LocalDate;

/**
 * Command 'add_element'. Saves the collection to a file.
 */
public class AddElementCommand extends AbstractCommand{
    private CollectionManager collectionManager;
    private OrganizationAsker organizationAsker;
    private LocalDate creationDate;
    private DatabaseCollectionHandler databaseCollectionHandler;


    public AddElementCommand(CollectionManager collectionManager, OrganizationAsker organizationAsker, DatabaseCollectionHandler databaseCollectionHandler){
        super("add","add a new item to the collection");
        this.organizationAsker = organizationAsker;
        this.collectionManager = collectionManager;
        this.databaseCollectionHandler = databaseCollectionHandler;

    }

    /**
     * Execute of 'add_element' command.
     */
    @Override
    public void execute(String argument, Object object, User user) throws NoSuchCommandException {
        try {
            if (!argument.isEmpty() || object == null) throw new WrongAmountOfElementsException();
            TicketRaw ticketRaw = (TicketRaw) object;
            collectionManager.addToCollection(databaseCollectionHandler.insertTicket(ticketRaw, user)
            );
            ResponseOutputer.appendln("Ticket is created");
        } catch (WrongAmountOfElementsException e) {
            ResponseOutputer.appenderror("Used: " + getName());
        } catch (DatabaseHandlingException e) {
            Console.printerror(e.getMessage());
        }
    }
}
