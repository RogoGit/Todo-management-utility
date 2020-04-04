import com.google.gson.Gson;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

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
       if (todosMap.isEmpty()) {
            System.out.println("Список дел пуст, пока нет ни одного дела");
       } else {
           TreeMap<String, Todo> sortedTodosMap = Util.sortTodosMap(todosMap);
           sortedTodosMap.forEach((key, value) -> System.out.println(value.toString()));
       }
    }

    public static void listByCompletion(TreeMap<String,Todo> todosMap, String[] commandArgs) {
       if (!(commandArgs[0].equals("true") || commandArgs[0].equals("false"))) {
           System.out.println("Неправильный аргумент команды (должно быть true или false)");
           return;
       }
       boolean isCompleted = Boolean.parseBoolean(commandArgs[0]);
       TreeMap<String,Todo> filteredMap = todosMap.entrySet()
               .stream()
               .filter(map -> map.getValue().getIsDone() == isCompleted)
               .collect(Collectors.toMap(Map.Entry::getKey,
                       Map.Entry::getValue,
                       (v1, v2) -> { throw new IllegalStateException(); },
                       TreeMap::new));
       if (filteredMap.isEmpty()) {
            System.out.println("Нет ни одного дела с выбранным параметром");
        } else {
            TreeMap<String,Todo> sortedTodos = Util.sortTodosMap(filteredMap);
            sortedTodos.forEach((key, value) -> System.out.println(value.toString()));
       }
    }

    public static void listByPriority(TreeMap<String,Todo> todosMap, String[] commandArgs) {
       try {
           Todo.TodoPriority priority = Todo.TodoPriority.valueOf(commandArgs[0].toUpperCase());
           TreeMap<String, Todo> filteredMap = todosMap.entrySet()
                   .stream()
                   .filter(map -> map.getValue().getPriority().equals(priority))
                   .collect(Collectors.toMap(Map.Entry::getKey,
                           Map.Entry::getValue,
                           (v1, v2) -> {
                               throw new IllegalStateException();
                           },
                           TreeMap::new));
           if (filteredMap.isEmpty()) {
               System.out.println("Нет ни одного дела с выбранным параметром");
           } else {
               TreeMap<String, Todo> sortedTodos = Util.sortTodosMap(filteredMap);
               sortedTodos.forEach((key, value) -> System.out.println(value.toString()));
           }
       } catch (IllegalArgumentException ex) {
           System.out.println("Неправильный аргумент команды (должно быть LOW, MEDIUM или HIGH)");
       }
    }

    public static void removeTodo(TreeMap<String,Todo> todosMap, String[] commandArgs) {
       String title = commandArgs[0];
       if (todosMap.containsKey(title)) {
           todosMap.entrySet().removeIf(entry -> entry.getKey().equals(title));
           System.out.println("Дело \"" + title + "\" успешно удалено");
       } else {
           System.out.println("Нет дела с заданным заголовком");
       }
    }

    public static void removeAll(TreeMap<String,Todo> todosMap) {
       todosMap.clear();
       System.out.println("Список дел очищен");
    }

    public static void markDone(TreeMap<String,Todo> todosMap, String[] commandArgs) {
        String title = commandArgs[0];
        if (todosMap.containsKey(title)) {
            todosMap.get(title).setDone(true);
            System.out.println("Установлен статус \"сделано\"");
        } else {
            System.out.println("Нет дела с заданным заголовком");
        }
    }

    public static void markUndone(TreeMap<String,Todo> todosMap, String[] commandArgs) {
        String title = commandArgs[0];
        if (todosMap.containsKey(title)) {
            todosMap.get(title).setDone(false);
            System.out.println("Установлен статус \"не сделано\"");
        } else {
            System.out.println("Нет дела с заданным заголовком");
        }
    }

    public static void setPriority(TreeMap<String,Todo> todosMap, String[] commandArgs) {
       try {
           String title = commandArgs[0];
           Todo.TodoPriority priority = Todo.TodoPriority.valueOf(commandArgs[1].toUpperCase().trim());
           if (todosMap.containsKey(title)) {
               todosMap.get(title).setPriority(priority);
               System.out.println("Приоритет изменен");
           } else {
               System.out.println("Нет дела с заданным заголовком");
           }
       } catch (IllegalArgumentException ex) {
           System.out.println("Неправильный второй аргумент команды (должно быть LOW, MEDIUM или HIGH)");
       }
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
