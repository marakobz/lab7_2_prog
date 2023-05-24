package commands;

import exceptions.DatabaseHandlingException;
import exceptions.UserIsNotFoundException;
import exceptions.WrongAmountOfElementsException;
import util.User;
import utility.Console;
import utility.DatabaseUserManager;
import utility.ResponseOutputer;

public class LoginCommand extends AbstractCommand{
    private DatabaseUserManager databaseUserManager;

    public LoginCommand(DatabaseUserManager databaseUserManager) {
        super("login", "внутренняя команда");
        this.databaseUserManager = databaseUserManager;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute(String stringArgument, Object objectArgument, User user) {
        try {
            if (!stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            if (databaseUserManager.checkUserByUsernameAndPassword(user)){
                ResponseOutputer.appendln("User " +
                    user.getUsername() + " is authorized.");
            }
            else throw new UserIsNotFoundException();
        } catch (WrongAmountOfElementsException exception) {
            Console.printerror("Использование: эммм...эээ.это внутренняя команда...");
        } catch (ClassCastException exception) {
            Console.printerror("Переданный клиентом объект неверен!");
        } catch (DatabaseHandlingException exception) {
            Console.printerror("Произошла ошибка при обращении к базе данных!");
        } catch (UserIsNotFoundException exception) {
            Console.printerror("Неправильные имя пользователя или пароль!");
        }
    }
}
