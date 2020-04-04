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

    public void setPriority(TodoPriority todoPriority) {
        priority = todoPriority;
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
        String priorStatus;
        if (this.getPriority().equals(TodoPriority.LOW)) {
            priorStatus = "НИЗКИЙ";
        } else if (this.getPriority().equals(TodoPriority.MEDIUM)) {
            priorStatus = "СРЕДНИЙ";
        } else {
            priorStatus = "ВЫСОКИЙ";
        }
        String isDoneStatus = this.getIsDone() ? "сделано" : "не сделано";
        return "--------------------------------\n" +
                 this.getTitle() + " - " +  isDoneStatus + "\n" +
                "Описание: " + this.getTaskDescription() + "\n" +
                "Приоритет: " + priorStatus + "\n";
    }

    public enum TodoPriority {
        HIGH,
        MEDIUM,
        LOW
    }

}
