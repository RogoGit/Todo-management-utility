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
                String[] commandArgs = new String[userCommand.length-1];
                System.arraycopy(userCommand,1,commandArgs,0,userCommand.length-1);
                switch (commandItself.toLowerCase().trim()) {
                    case "add_todo":
                        TodoManager.addTodo(todosMap, commandArgs);
                    break;
                    case "remove_todo":
                        TodoManager.removeTodo(todosMap, commandArgs);
                    break;
                    case "list":
                        TodoManager.listAllTodos(todosMap);
                    break;
                    case "remove_all":
                        TodoManager.removeAll(todosMap);
                    break;
                    case  "save":
                        TodoManager.save(todosMap, filename);
                    break;
                    case "help":
                        TodoManager.showHelp();
                    break;
                    case "exit":
                        System.out.println("До свидания");
                        TodoManager.save(todosMap, filename);
                        System.exit(0);
                    break;
                    default:
                        System.out.println("Не удалось распознать команду. Введите help для помощи");
                    break;
                }
            } catch (IOException ioEx) {
                System.out.println("IOException");
            } catch (JsonSyntaxException jsonEx) {
                System.out.println("Ошибка синтаксиса JSON. Проверьте правильность ввода");
            } catch (NullPointerException np) {
                //handling Ctrl-D
                System.out.println("До свидания");
                TodoManager.save(todosMap, filename);
                System.exit(0);
            }
        } while (true);
    }

}

