package utility;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

import exceptions.DatabaseHandlingException;
import models.*;


/**
 * Operates the collection.
 */

public class CollectionManager {

    private NavigableSet<Ticket> collection;
    private DatabaseCollectionHandler databaseCollectionHandler;

    public CollectionManager(DatabaseCollectionHandler databaseCollectionHandler) throws IOException {
        this.databaseCollectionHandler = databaseCollectionHandler;
        loadCollection();
    }

    public NavigableSet<Ticket> getCollection(){
        return collection;
    }
    public void clearCollection() {
        collection.clear();
    }

    /**
     * @return Size of the collection.
     */
    public int collectionSize(){
        return collection.size();
    }

    /**
     * @return The first element of the collection or null if collection is empty.
     */
   public Ticket getFirst(){
        return collection.stream().findFirst().orElse(null);
    }


    /**
     * @return A ticket by his ID or null if ticket isn't found.
     */
    public Ticket getById(int id) {
        for (Ticket element : collection) {
            if (element.getId() == id) return element;
        }
        return null;
    }

    /**
     * @param ticket A ticket which value will be found.
     * @return A marine by his value or null if marine isn't found.
     */
    public Ticket getByValue(Ticket ticket){
        for (Ticket tickets : collection){
            if (tickets.equals(ticket)) return tickets;
        }
        return null;
    }

    /**
     * Removes greater.
     */
    public void removeGreater(Ticket ticketToCompare) {
        collection.removeIf(ticket -> ticket.compareTo(ticketToCompare) > 0);
    }

    /**
     * Adds a new ticket to collection.
     * @param element A ticket to add.
     */
    public void addToCollection(Ticket element) {
        collection.add(element);
    }

    /**
     * Removes a new ticket to collection.
     * @param element A marine to remove.
     */
    public void removeFromCollection(Ticket element) {
        collection.remove(element);
    }

    /**
     * Exits the program
     *
     * @return
     */
    public Object exit() throws IOException {
        Console.println("Work of program is ended");
        System.exit(0);

        return true;
    }

    /**
     * Loads the collection from file.
     */
    public void loadCollection() throws IOException {
        try{
            collection = databaseCollectionHandler.getCollection();
            Console.println("Collection is loaded");


        }catch (DatabaseHandlingException exception) {
            collection = new TreeSet<>();
            Console.printerror("Коллекция не может быть загружена!");
            //logger.error("Коллекция не может быть загружена!");
        }
    }
    /**
     * Counting group by its creation date
     *
     * @return
     */
    public Object groupCountingByCrDate(){
        HashMap<LocalDateTime, TreeSet<Ticket>> groupMap = new HashMap<>();
        for (Ticket i : collection){
            if (groupMap.get((i).getCreationDate()) == null){
                TreeSet<Ticket> x = new TreeSet<>();
                x.add(i);
                groupMap.put((i).getCreationDate(), x);
            } else groupMap.get((i).getCreationDate()).add(i);
        }
        for (Map.Entry<LocalDateTime, TreeSet<Ticket>> entry : groupMap.entrySet()){
            ResponseOutputer.appendln("Elements created in " + entry.getKey() + " :\n");
            entry.getValue().forEach(CollectionManager::display);
        }
        return true;
    }
    /**
     * Display the info about created ticket
     */
    public static void display(Ticket ticket) {
        ResponseOutputer.appendln("ID of ticket:" + ticket.getId());
        ResponseOutputer.appendln("Name of ticket:" + ticket.getName());
        ResponseOutputer.appendln("Creation date of ticket:" + ticket.getCreationDate());
        ResponseOutputer.appendln("Coordinates:" + ticket.getCoordinates());
        ResponseOutputer.appendln("Price:" + ticket.getPrice());
        ResponseOutputer.appendln("Discount:" + ticket.getDiscount());
        ResponseOutputer.appendln("Refund:" + ticket.getRefundable());
        ResponseOutputer.appendln("Type of ticket:" + ticket.getType());
        ResponseOutputer.appendln("Human's info:" + ticket.getPerson());
    }
    @Override
    public String toString() {
        if (collection.isEmpty()) return "There is nothing in collection";

        StringBuilder builder = new StringBuilder();
        for (Ticket ticket : collection) {
            builder.append(ticket);
            builder.append("\n");
        }
        return builder.toString();
    }
}
