import java.time.LocalDateTime;

public class Task{
    private String taskName; // What the task will be called in the directory.
    private String taskDescription; // What the actual task is.
    private LocalDateTime taskCreated; // Time and date the task was created.
    private LocalDateTime taskDeadline; // Time and date the task needs to be completed by.

    public Task(String taskName, String taskDescription, LocalDateTime taskCreated, LocalDateTime taskDeadline) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskCreated = taskCreated;
        this.taskDeadline = taskDeadline;
    }

    Task(Task T) {
        System.out.println("Copy constructor called");
        taskName = T.taskName;
        taskDescription = T.taskDescription;
        taskCreated = T.taskCreated;
        taskDeadline = T.taskDeadline;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public void setTaskDeadline(LocalDateTime taskDeadline) {
        this.taskDeadline = taskDeadline;
    }

    public String getTaskName() {
        return this.taskName; // Returns the name of the task.
    }

    public String getTaskDescription() {
        return this.taskDescription; // Returns the task.
    }

    public LocalDateTime getTaskCreated() {
        return this.taskCreated; // Returns when the task was created.
    }

    public LocalDateTime getTaskDeadline() {
        return this.taskDeadline; // Returns when the task is due.
    }

    @Override
    public String toString() {
        return "(" + taskName + ", " + taskDescription + ", " + taskCreated + ", " + taskDeadline + ")";
    }
}