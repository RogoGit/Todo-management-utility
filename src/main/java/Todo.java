public class Todo {

    private String title;
    private String taskDescription;

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

    @Override
    public String toString() {
        return "title " + this.getTitle() + " taskDescription " + this.getTaskDescription();
    }
}
