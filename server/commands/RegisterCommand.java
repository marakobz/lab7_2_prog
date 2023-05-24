package commands;

import exceptions.DatabaseHandlingException;
import exceptions.UserAlreadyExistException;
import exceptions.WrongAmountOfElementsException;
import util.User;
import utility.Console;
import utility.DatabaseUserManager;
import utility.ResponseOutputer;

public class RegisterCommand extends AbstractCommand{
    private DatabaseUserManager databaseUserManager;

    public RegisterCommand(DatabaseUserManager databaseUserManager) {
        super("register",  "внутренняя команда");
        this.databaseUserManager = databaseUserManager;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute(String stringArgument, Object objectArgument, User user) {
        try {
            if (!stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            if (databaseUserManager.insertUser(user)) {
                ResponseOutputer.appendln("User " +
                        user.getUsername() + " is registered.");
            }
            else throw new UserAlreadyExistException();
        } catch (WrongAmountOfElementsException exception) {
            Console.printerror("Использование: эммм...эээ.это внутренняя команда...");
        } catch (ClassCastException exception) {
            Console.printerror("Переданный клиентом объект неверен!");
        } catch (DatabaseHandlingException exception) {
            Console.printerror(exception.getMessage());
            Console.printerror("Произошла ошибка при обращении к базе данных!");
        } catch (UserAlreadyExistException exception) {
            Console.printerror("Пользователь " + user.getUsername() + " уже существует!");
        }
    }
}
