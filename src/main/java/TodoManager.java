import com.google.gson.Gson;

import java.io.*;
import java.util.*;

public class TodoManager {

   public static void addTodo(TreeMap<String,Todo> todosMap, String filename, String commandArg) throws IOException {
        Gson gson = new Gson();
        Todo todo = gson.fromJson(commandArg, Todo.class);
        if (todo.getIsDone() == null) todo.setDone(false);
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
            System.out.println("Не удалось добавить, необходимо заполнить поля title, taskDescription, priority");
        }
        try {
            TodoIO.writeToFile(todosMap, filename);
        } catch (IOException x) {
            System.out.println("Не удается сохранить изменения в файл (IOException)");
        }
    }

    public static void listAllTodos(TreeMap<String,Todo> todosMap) {
        TreeMap<String, Todo> sortedTodosMap = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String key1, String key2) {
                int returned = (-1)*todosMap.get(key1).compareTo(todosMap.get(key2));
                if (returned == 0 && !key1.equals(key2))
                    returned = 1;
                return returned;
            }
        });
        sortedTodosMap.putAll(todosMap);
        sortedTodosMap.forEach((key,value) -> System.out.println(value.toString()));
    }

}
