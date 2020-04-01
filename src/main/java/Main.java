import com.google.gson.JsonParseException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        ArrayList<Todo> todosList = new ArrayList<>();

        String[] filename = new String[]{args.length > 0 ? args[0] : "todo-list.json"};
        System.out.println();
        System.out.println("Введите help для помощи");
        try {
            todosList = TodoManager.readFromFile(filename[0]);
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

        System.out.println(todosList);

    }

}
