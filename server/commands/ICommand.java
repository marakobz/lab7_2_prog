package commands;

import util.User;

/**
 * Interface for all commands.
 */
public interface ICommand{

    String getName();
    void execute(String commandArguments, Object objectArgument, User user);

}