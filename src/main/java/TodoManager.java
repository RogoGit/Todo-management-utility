import com.google.gson.Gson;

import java.io.*;
import java.util.*;

public class TodoManager {

   public static void addTodo(HashMap<String,Todo> todosMap, String filename, String commandArg) throws IOException {
        Gson gson = new Gson();
        Todo todo = gson.fromJson(commandArg, Todo.class);
        if (todo.getTitle() != null && todo.getTaskDescription()!= null) {
            if (todosMap.containsKey(todo.getTitle())) {
                System.out.println("Дело с таким заголовком уже существует. Перезаписать его? (введите yes, если да, иначе - отмена  )");
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String choice = br.readLine();
                if (choice.trim().toLowerCase().equals("yes")) {
                    todosMap.put(todo.getTitle(), todo);
                    System.out.println("Добавлено");
                } else {
                    System.out.println("Не перезаписано");
                }
            } else {
                todosMap.put(todo.getTitle(), todo);
                System.out.println("Добавлено");
            }
        } else {
            System.out.println("Не удалось добавить, все поля должны быть заполнены");
        }
        try {
            TodoIO.writeToFile(todosMap, filename);
        } catch (IOException x) {
            System.out.println("Не удается сохранить изменения в файл (IOException)");
        }
    }

}
