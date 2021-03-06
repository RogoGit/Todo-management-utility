package com.todo_management;

import com.google.gson.Gson;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Commands for todo list management
 */

public class TodoManager {

    /**
     * Adding new todo in JSON format
     * @param todosMap - map of all todos from file (Key - title of Todo, Value - Todo object itself)
     * @param commandArgs - arguments for this command - new Todo object  in json format
     * @throws IOException - exception while reading user input
     */
   public static void addTodoJson(TreeMap<String,Todo> todosMap, String[] commandArgs) throws IOException {
        Gson gson = new Gson();
        Todo todo = gson.fromJson(commandArgs[0], Todo.class);
        if (todo.getIsDone() == null) todo.setDone(false);
        if (todo.getTitle() != null && todo.getTaskDescription()!= null && todo.getPriority()!= null) {
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
            System.out.println("Убедитесь, что все поля укащаны правильно, приоритет задан как LOW/MEDIUM/HIGH");
        }
    }

    /**
     * Adding new todo without JSON
     * @param todosMap - map of all todos from file (Key - title of Todo, Value - Todo object itself)
     * @throws IOException - exception while reading user input
     */
    public static void addTodo(TreeMap<String,Todo> todosMap) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Введите заголовок нового дела");
        String title = br.readLine();
        if (todosMap.containsKey(title)) {
            System.out.println("Дело с таким заголовком уже существует. Перезаписать его? (введите yes, если да, иначе - отмена  )");
            String choice = br.readLine();
            if (!choice.trim().toLowerCase().equals("yes")) {
                System.out.println("Отмена добавления нового дела");
                return;
            }
        }
        System.out.println("Введите описание");
        String description = br.readLine();
        System.out.println("Выберите приоритет (LOW/MEDIUM/HIGH)");
        String priority;
        do {
            priority = br.readLine();
            if (!(priority.toUpperCase().equals("LOW") || priority.toUpperCase().equals("MEDIUM")
                    || priority.toUpperCase().equals("HIGH"))) {
                System.out.println("Неправильно задан приоритет (допустимо LOW, MEDIUM или HIGH)");
                System.out.println("Выберите приоритет");
            } else {
                break;
            }
        }
        while (true);
        todosMap.put(title, new Todo(title, description, false, Todo.TodoPriority.valueOf(priority.toUpperCase())));
        System.out.println("Добавлено");
    }

    /**
     * Saving user todos to file
     * @param todosMap - map of all todos from file (Key - title of Todo, Value - Todo object itself)
     * @param filename - name of file, where todos are stored
     */
    public static void save(TreeMap<String,Todo> todosMap, String filename) {
        try {
            TodoIO.writeToFile(todosMap, filename);
            System.out.println("Список дел успешно сохранен");
        } catch (IOException x) {
            System.out.println("Не удается сохранить изменения в файл (IOException)");
        }
    }

    /**
     * Printing list of all user todos
     * @param todosMap - map of all todos from file (Key - title of Todo, Value - Todo object itself)
     */
    public static void listAllTodos(TreeMap<String,Todo> todosMap) {
       if (todosMap.isEmpty()) {
            System.out.println("Список дел пуст, пока нет ни одного дела");
       } else {
           TreeMap<String, Todo> sortedTodosMap = Util.sortTodosMap(todosMap);
           sortedTodosMap.forEach((key, value) -> System.out.println(value.toString()));
       }
    }

    /**
     * Printing list of todos filtered by isDone field
     * @param todosMap - map of all todos from file (Key - title of Todo, Value - Todo object itself)
     * @param commandArgs - boolean value of isDone (true/false), which list is filtered by
     * @return filtered map - used for tests
     */
    public static TreeMap<String, Todo> listByCompletion(TreeMap<String,Todo> todosMap, String[] commandArgs) {
       if (!(commandArgs[0].equals("true") || commandArgs[0].equals("false"))) {
           System.out.println("Неправильный аргумент команды (должно быть true или false)");
           return null;
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
            return null;
        } else {
            TreeMap<String,Todo> sortedFilteredTodos = Util.sortTodosMap(filteredMap);
            sortedFilteredTodos.forEach((key, value) -> System.out.println(value.toString()));
            return sortedFilteredTodos;
       }
    }

    /**
     * Printing list of todos filtered by priority field
     * @param todosMap - map of all todos from file (Key - title of Todo, Value - Todo object itself)
     * @param commandArgs - TodoPriority enum value, which list is filtered by
     * @return filtered map - used for tests
     */
    public static TreeMap<String, Todo> listByPriority(TreeMap<String,Todo> todosMap, String[] commandArgs) {
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
               System.out.println("Не найдено ни одного дела с выбранным параметром");
               return null;
           } else {
               TreeMap<String, Todo> sortedTodos = Util.sortTodosMap(filteredMap);
               sortedTodos.forEach((key, value) -> System.out.println(value.toString()));
               return sortedTodos;
           }
       } catch (IllegalArgumentException ex) {
           System.out.println("Неправильный аргумент команды (должно быть LOW, MEDIUM или HIGH)");
           return null;
       }
    }

    /**
     * Printing list of todos filtered by both priority and isDone fields
     * @param todosMap - map of all todos from file (Key - title of Todo, Value - Todo object itself)
     * @param commandArgs - boolean value of isDone and TodoPriority enum value, which list is filtered by
     * @return filtered map - used for tests
     */
    public static TreeMap<String, Todo> listBy(TreeMap<String,Todo> todosMap, String[] commandArgs) {
        if (!(commandArgs[0].equals("true") || commandArgs[0].equals("false"))) {
            System.out.println("Неправильный первый аргумент команды (должно быть true или false)");
            return null;
        }
        boolean isCompleted = Boolean.parseBoolean(commandArgs[0]);
        try {
            Todo.TodoPriority priority = Todo.TodoPriority.valueOf(commandArgs[1].toUpperCase());
            TreeMap<String, Todo> filteredMap = todosMap.entrySet()
                    .stream()
                    .filter(map -> map.getValue().getPriority().equals(priority))
                    .filter(map -> map.getValue().getIsDone() == isCompleted)
                    .collect(Collectors.toMap(Map.Entry::getKey,
                            Map.Entry::getValue,
                            (v1, v2) -> {
                                throw new IllegalStateException();
                            },
                            TreeMap::new));
            if (filteredMap.isEmpty()) {
                System.out.println("Нет ни одного дела с выбранными параметрами");
                return null;
            } else {
                TreeMap<String, Todo> sortedTodos = Util.sortTodosMap(filteredMap);
                sortedTodos.forEach((key, value) -> System.out.println(value.toString()));
                return sortedTodos;
            }
        } catch (IllegalArgumentException ex) {
            System.out.println("Неправильный второй аргумент команды (должно быть LOW, MEDIUM или HIGH)");
            return null;
        }
    }

    /**
     * Removing todo by its title
     * @param todosMap - map of all todos from file (Key - title of Todo, Value - Todo object itself)
     * @param commandArgs - title of todo, which should be deleted
     */
    public static void removeTodo(TreeMap<String,Todo> todosMap, String[] commandArgs) {
       String title = commandArgs[0];
       if (todosMap.containsKey(title)) {
           todosMap.entrySet().removeIf(entry -> entry.getKey().equals(title));
           System.out.println("Дело \"" + title + "\" успешно удалено");
       } else {
           System.out.println("Нет дела с заданным заголовком");
       }
    }

    /**
     * Removing all todos
     * @param todosMap - map of all todos from file (Key - title of Todo, Value - Todo object itself)
     */
    public static void removeAll(TreeMap<String,Todo> todosMap) {
       todosMap.clear();
       System.out.println("Список дел очищен");
    }

    /**
     * Mark todo as done by its title (setting isDone field as true)
     * @param todosMap - map of all todos from file (Key - title of Todo, Value - Todo object itself)
     * @param commandArgs -  title of todo
     */
    public static void markDone(TreeMap<String,Todo> todosMap, String[] commandArgs) {
        String title = commandArgs[0];
        if (todosMap.containsKey(title)) {
            todosMap.get(title).setDone(true);
            System.out.println("Установлен статус \"сделано\"");
        } else {
            System.out.println("Нет дела с заданным заголовком");
        }
    }

    /**
     * Mark todo as not done by its title (setting isDone field as false)
     * @param todosMap - map of all todos from file (Key - title of Todo, Value - Todo object itself)
     * @param commandArgs -  title of todo
     */
    public static void markUndone(TreeMap<String,Todo> todosMap, String[] commandArgs) {
        String title = commandArgs[0];
        if (todosMap.containsKey(title)) {
            todosMap.get(title).setDone(false);
            System.out.println("Установлен статус \"не сделано\"");
        } else {
            System.out.println("Нет дела с заданным заголовком");
        }
    }

    /**
     * Setting todo priority
     * @param todosMap - map of all todos from file (Key - title of Todo, Value - Todo object itself)
     * @param commandArgs -  title of todo and priority to be set
     */
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
       System.out.println("Используемый JSON формат дела:");
       System.out.println("{\"title\": \"Заголовок задания\", ");
       System.out.println("\"taskDescription\": \"Описание дела\", ");
       System.out.println("\"isDone\": false, ");
       System.out.println("\"priority\": \"HIGH\"}\n");

       System.out.println("title - уникальный заголовок каждого дела, у каждого дела он должен быть свой");
       System.out.println("taskDescription - описание дела");
       System.out.println("isDone - показывает, выполнено дело или нет (true - если да, false - если нет). Может принимать только эти значения");
       System.out.println("priority - приоритет дела (LOW - низкий, MEDIUM - средний, HIGH - выоокий). Может принимать только эти значения\n");

       System.out.println("При отображении списка дел дела сортируются в следующем порядке:");
       System.out.println("- по полю isDone - сначала идут невыполненные задания, потом выполненные ");
       System.out.println("- по приоритету - при одинаковом значении isDone сначала отбражаются дела с высоким приоритетом," +
               "потом со средним, потом с низким.\n");

       System.out.println("Поддерживаются следующие команды:");
       System.out.println("help - Инструкция к использованию программы, список возможных команд");
       System.out.println("list - Показать список всех дел");
       System.out.println("add_todo - Добавить новое дело");
       System.out.println("json_add_todo {todo} - Добавить новое дело в формате JSON (поддерживается многострочный ввод)");
       System.out.println("list_by_completion <true/false> - В зависиости от аргумента команды - показать список всех завершенных (true) или незавершенных (false) дел");
       System.out.println("list_by_priority <low/medium/high> - Показать список всех дел с указанным приоритетом");
       System.out.println("list_by <true/false> <low/medium/high> - Показать список всех дел согласно заданному фильтру " +
               "(первый аргумент - выполнено дело или нет, второй - приоритет)");
       System.out.println("remove_todo \"title\"- Удалить дело по его заголовку");
       System.out.println("remove_all - Удалить все дела безвозвратно");
       System.out.println("mark_done \"title\" - Обозначить дело как выполненное");
       System.out.println("mark_undone \"title\" - Обозначить дело как невыполненное");
       System.out.println("set_priority \"title\" low/medium/high - Поставить заданный приоритет выбранному делу");
       System.out.println("save - Сохранить список дел (сохранение также будет произведено автоматически при выходе из программы)");
       System.out.println("exit - Выход из программы");
    }

}
