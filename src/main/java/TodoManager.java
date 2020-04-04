import com.google.gson.Gson;

import java.io.*;
import java.util.*;

public class TodoManager {

   public static void addTodo(TreeMap<String,Todo> todosMap, String[] commandArgs) throws IOException {
        Gson gson = new Gson();
        Todo todo = gson.fromJson(commandArgs[0], Todo.class);
        if (todo.getPriority() == null) {
            System.out.println("Не удалось добавить. Неправильно введен приоритет (LOW/MEDIUM/HIGH)");
            return;
        }
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
    }

    public static void save(TreeMap<String,Todo> todosMap, String filename) {
        try {
            TodoIO.writeToFile(todosMap, filename);
            System.out.println("Список дел успешно сохранен");
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

    public static void removeTodo(TreeMap<String,Todo> todosMap, String[] commandArgs) {
       String title = commandArgs[0];
       todosMap.entrySet().removeIf(entry -> entry.getKey().equals(title));
       System.out.println("Дело \"" + title + "\" успешно удалено");
    }

    public static void removeAll(TreeMap<String,Todo> todosMap) {
       todosMap.clear();
       System.out.println("Список дел очищен");
    }

    public static void showHelp() {
       System.out.println("help - Инструкция к использованию программы, список возможных команд");
       System.out.println("list - Показать список всех дел");
       System.out.println("add_todo {todo} - Добавить новое дело (задается в формате JSON, поддерживается многострочный ввод)");
       System.out.println("list_by_completion true/fasle - В зависиости от аргумента команды - показать список всех завершенных (true) или незавершенных (fasle) дел");
       System.out.println("list_by_priority low/medium/high - Показать список всех дел с указанным приоритетом");
       System.out.println("remove_todo \"title\"- Удалить дело по его заголовку");
       System.out.println("remove_all - Удалить все дела безвозвратно");
       System.out.println("mark_done \"title\" - Обозначить дело как выполненное");
       System.out.println("mark_undone \"title\" - Обозначить дело как невыполненное");
       System.out.println("set_priority \"title\" low/medium/high - Поставить заданный приоритет выбранному делу");
       System.out.println("save - Сохранить список дел (сохранение также будет произведено автоматически при выходе из программы)");
       System.out.println("exit - Выход из программы");
    }

}
