public class Todo implements Comparable<Todo> {

    private String title;
    private String taskDescription;
    private Boolean isDone;
    private TodoPriority priority;

    public Todo(String title, String taskDescription) {
        this.title = title;
        this.taskDescription = taskDescription;
    }

    public String getTitle() {
        return title;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public Boolean getIsDone() {
        return isDone;
    }

    public TodoPriority getPriority() {
        return priority;
    }

    public void setDone(Boolean done) {
        isDone = done;
    }

    @Override
    public int compareTo(Todo anotherTodo) {
        if (this.getIsDone() && !anotherTodo.getIsDone()) return -1;
        if (!this.getIsDone() && anotherTodo.getIsDone()) return 1;
        if (this.priority.equals(anotherTodo.priority)) return 0;
        if (this.priority.equals(TodoPriority.HIGH) && !anotherTodo.priority.equals(TodoPriority.HIGH)) return 1;
        if (this.priority.equals(TodoPriority.MEDIUM) && anotherTodo.priority.equals(TodoPriority.LOW)) return 1;
        return -1;
    }

    @Override
    public String toString() {
        return "title " + this.getTitle() + " taskDescription " + this.getTaskDescription();
    }

    private enum TodoPriority {
        HIGH,
        MEDIUM,
        LOW
    }

}
