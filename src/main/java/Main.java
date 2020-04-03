import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.TreeMap;

public class Main {

    public static void main(String[] args) {

        TreeMap<String, Todo> todosMap = new TreeMap<>();

        String[] file = new String[]{args.length > 0 ? args[0] : "todo-list.json"};
        String filename = file[0];
        System.out.println();
        System.out.println("Введите help для помощи");
        try {
            todosMap = TodoIO.readFromFile(filename);
        } catch (NullPointerException np) {
            System.out.println("Выбранный файл не должен содержать null, все поля должны быть заполнены");
        } catch (AccessDeniedException permDenied) {
            System.out.println("Доступ к файлу запрещен");
        } catch (FileNotFoundException notFound) {
            System.out.println("Данный файл не найден");
        } catch (IOException ioEx) {
            System.out.println("Не удается прочитать список дел из выбранного файла (IOException)");
        } catch (JsonParseException parseEx) {
            System.out.println("Ошибка чтения JSON, проверьте правильность формата данных");
        }

        TodoManager.showHelp();

        do {
            try {
                String[] userCommand = Util.parseUserInput();
                String commandItself = userCommand[0];
                String commandArg = "";
                if (userCommand.length > 1) commandArg = userCommand[1];
                switch (commandItself.toLowerCase()) {
                    case "add_todo":
                        TodoManager.addTodo(todosMap,filename,commandArg);
                    break;
                    case "list":
                        TodoManager.listAllTodos(todosMap);
                    break;
                }
            } catch (IOException ioEx) {
                System.out.println("IOException");
            } catch (JsonSyntaxException jsonEx) {
                System.out.println("Ошибка синтаксиса JSON");
            }
        } while (true);
    }

}

