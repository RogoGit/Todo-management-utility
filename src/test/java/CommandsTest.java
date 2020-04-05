import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import todo_management.Todo;
import todo_management.TodoManager;
import todo_management.Util;

import java.util.TreeMap;

public class CommandsTest {

    private Todo todo1;
    private Todo todo2;
    private Todo todo3;
    private Todo todo4;
    private Todo todo5;
    private TreeMap<String, Todo> actualMap;

    /**
     * Creating map of todos before the tests
     */
    @Before
    public void createTodoList()  {
        actualMap = new TreeMap<>();
        todo1 = new Todo("title 1", "This is todo 1", false, Todo.TodoPriority.LOW);
        actualMap.put(todo1.getTitle(),todo1);
        todo2 = new Todo("title 2", "This is todo 2", true, Todo.TodoPriority.HIGH);
        actualMap.put(todo2.getTitle(),todo2);
        todo3 = new Todo("title 3", "This is todo 3", false, Todo.TodoPriority.MEDIUM);
        actualMap.put(todo3.getTitle(),todo3);
        todo4 = new Todo("title 4", "This is todo 4", true, Todo.TodoPriority.LOW);
        actualMap.put(todo4.getTitle(),todo4);
        todo5 = new Todo("title 5", "This is todo 5", false, Todo.TodoPriority.HIGH);
        actualMap.put(todo5.getTitle(),todo5);
    }

    /**
     * Clearing map after each test
     */
    @After
    public void clearTodosMap() {
        actualMap.clear();
    }

    /**
     * Testing markDone - if todo_management.Todo object isDone field really became true
     */
    @Test
    public void testMarkDone() {
        String[] commandArgs = {"title 1"};
        TreeMap<String, Todo> expectedMap = actualMap;

        TodoManager.markDone(expectedMap,commandArgs);

        Assert.assertEquals("Меняем статус дела на законченное", expectedMap.get(commandArgs[0]).getIsDone(), true);
    }

    /**
     * Testing markUndone - if todo_management.Todo object isDone field really became false
     */
    @Test
    public void testMarkUndone() {
        String[] commandArgs = {"title 2"};
        TreeMap<String, Todo> expectedMap = actualMap;

        TodoManager.markUndone(expectedMap,commandArgs);

        Assert.assertEquals("Меняем статус дела на незаконченное", expectedMap.get(commandArgs[0]).getIsDone(), false);
    }

    /**
     * Testing setPriority - if todo_management.Todo priority changed to given enum value
     */
    @Test
    public void testSetPriority() {
        String[] commandArgs = {"title 3", "HIGH"};
        TreeMap<String, Todo> expectedMap = actualMap;

        TodoManager.setPriority(expectedMap,commandArgs);

        Assert.assertEquals("Менянм приоритет на высокий", expectedMap.get(commandArgs[0]).getPriority(), Todo.TodoPriority.HIGH);
    }

    /**
     * Testing removeTodo - if todo_management.Todo removed from map
     */
    @Test
    public void testRemoveTodo() {
        String[] commandArgs = {"title 4"};
        TreeMap<String, Todo> expectedMap = actualMap;

        TodoManager.removeTodo(expectedMap,commandArgs);
        actualMap.remove(commandArgs[0]);

        Assert.assertEquals("Удаляем дело title 4", expectedMap, actualMap);
    }

    /**
     * Testing removeAll - if all todo_management.Todo objects removed
     */
    @Test
    public void testRemoveAll() {
        TreeMap<String, Todo> expectedMap = actualMap;

        TodoManager.removeAll(expectedMap);
        actualMap.clear();

        Assert.assertEquals("Удаляем все дела", expectedMap, actualMap);
    }

    /**
     * Testing list sorting - if sorting is in correct order
     */
    @Test
    public void testTodoListSort() {
        TreeMap<String, Todo> expectedMap = actualMap;
        actualMap.clear();
        // putting in actual map in correct order
        actualMap.put(todo5.getTitle(),todo5);
        actualMap.put(todo3.getTitle(),todo3);
        actualMap.put(todo1.getTitle(),todo1);
        actualMap.put(todo2.getTitle(),todo2);
        actualMap.put(todo4.getTitle(),todo4);

        Util.sortTodosMap(expectedMap);

        Assert.assertEquals("Проверяем порядок дел", expectedMap, actualMap);
    }

    /**
     * Testing listByCompletion - listing only todos with given completion status
     */
    @Test
    public void testListByCompletion() {
        String[] commandArgs = {"true"};
        TreeMap<String, Todo> expectedMap = actualMap;
        actualMap.clear();
        // putting in actual map only true values
        actualMap.put(todo2.getTitle(),todo2);
        actualMap.put(todo4.getTitle(),todo4);

        TreeMap<String, Todo> expectedFilteredMap = TodoManager.listByCompletion(expectedMap, commandArgs);

        Assert.assertEquals("Проверяем, что в списке только выполненные дела", expectedFilteredMap, actualMap);
    }

    /**
     * Testing listByPriority - listing only todos with given priority
     */
    @Test
    public void testListByPriority() {
        String[] commandArgs = {"HIGH"};
        TreeMap<String, Todo> expectedMap = actualMap;
        actualMap.clear();
        // putting in actual map only high priority values
        actualMap.put(todo2.getTitle(),todo2);
        actualMap.put(todo5.getTitle(),todo5);

        TreeMap<String, Todo> expectedFilteredMap = TodoManager.listByPriority(expectedMap, commandArgs);

        Assert.assertEquals("Проверяем, что в списке только дела с высоким приоритетом", expectedFilteredMap, actualMap);
    }

    /**
     * Testing listByCompletion - listing only todos with given filter
     */
    @Test
    public void testListBy() {
        String[] commandArgs = {"false", "LOW"};
        TreeMap<String, Todo> expectedMap = actualMap;
        actualMap.clear();
        // putting in actual map only false and low values
        actualMap.put(todo1.getTitle(),todo1);

        TreeMap<String, Todo> expectedFilteredMap = TodoManager.listBy(expectedMap, commandArgs);

        Assert.assertEquals("Проверяем, что в списке только невыполненные дела с низким приоритетом", expectedFilteredMap, actualMap);
    }

}
