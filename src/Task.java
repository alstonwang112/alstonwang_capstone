import java.time.LocalDateTime;

public class Task{
    private String taskName; // What the task will be called in the directory.
    private String task; // What the actual task is.
    private LocalDateTime taskCreated; // Time and date the task was created.
    private LocalDateTime taskDeadline; // Time and date the task needs to be completed by.

    public Task() {
        this.taskName = "";
        this.task = "";
        // this.taskCreated = ;
        // this.deadline = ;
        // Unsure of how to implement LocalDateTime, will work on it later.
    }

    public Task(String a, String b, LocalDateTime c, LocalDateTime d) {
        this.taskName = a;
        this.task = b;
        // this.taskCreated = c;
        // this.deadline = d;
        // Unsure of how to implement LocalDateTime, will work on it later.
    }

    public void setTaskName(String input) {
        this.taskName = input; // Set the task name in the directory.
    }

    public void setTask(String input) {
        this.task = input; // Set the actual task itself.
    }

    public void setTaskDeadline(LocalDateTime input) {
        this.taskDeadline = input; // Set the deadline for the task. I don't think the created variable needs a method?
    }

    public String getTaskName() {
        return this.taskName; // Returns the name of the task.
    }

    public String getTask() {
        return this.task; // Returns the task.
    }

    public LocalDateTime getCreationDate() {
        return this.taskCreated; // Returns when the task was created.
    }

    public LocalDateTime getTaskDeadline() {
        return this.taskDeadline; // Returns when the task is due.
    }
}