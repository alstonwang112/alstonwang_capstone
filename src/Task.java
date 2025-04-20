import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Task {
    private String taskName; // What the task will be called in the directory.
    private String taskDescription; // What the actual task is.
    private LocalDateTime taskCreated; // Time and date the task was created.
    private LocalDateTime taskDeadline; // Time and date the task needs to be completed by.
    private LinkedList commentThread; // Holds a thread of comments.

    public Task(String taskName, String taskDescription, LocalDateTime taskCreated, LocalDateTime taskDeadline, LinkedList commentThread) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskCreated = taskCreated;
        this.taskDeadline = taskDeadline;
        this.commentThread = commentThread;
    }

    Task(Task T) {
        System.out.println("Copy constructor called");
        taskName = T.taskName;
        taskDescription = T.taskDescription;
        taskCreated = T.taskCreated;
        taskDeadline = T.taskDeadline;
        commentThread = T.commentThread;
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

    public void setHead(String comment) {
        this.commentThread.head.comment = comment;
    }

    public String getTaskName() {
        return this.taskName; // Returns the name of the task.
    }

    public String getTaskDescription() {
        return this.taskDescription; // Returns the task description.
    }

    public LocalDateTime getTaskCreated() {
        return this.taskCreated; // Returns when the task was created.
    }

    public LocalDateTime getTaskDeadline() {
        return this.taskDeadline; // Returns when the task is due.
    }

    public LinkedList getCommentThread() {
        return this.commentThread; // Returns the comment thread.
    }

    public static Task createTask(String taskName, String taskDescription, LocalDateTime taskDeadline, LinkedList L) {
        LocalDateTime taskCreated = LocalDateTime.now();
        return new Task(taskName, taskDescription, taskCreated, taskDeadline, L);
    } // Creates a new task using an explicit constructor.

    public static int searchTask(String taskName, ArrayList<Task> taskCollection) {
        int size = taskCollection.size();

        for (int i = 0; i < size; i++) {
            if (taskName.equals(taskCollection.get(i).getTaskName()))
            { return i; }
        }

        return -1;
    } // Used to search for the task the user desires.

    public static Task editTask(Task T) {
        System.out.println("    What would you like to edit?");
        System.out.println("        (1) Edit task name.");
        System.out.println("        (2) Edit task description.");
        System.out.println("        (3) Edit task deadline.");
        System.out.print("    Enter a number (1 - 3): ");

        Scanner scnr = new Scanner(System.in);
        int action = 0;

        do {
            try {
                action = scnr.nextInt();
                if (action < 1 || action > 3) {
                    System.out.print("Invalid. Try again: ");
                    scnr.nextLine();
                }
            } catch (InputMismatchException e) {
                System.out.print("Invalid. Try again: ");
                scnr.nextLine();
            }
        } while (action < 1 || action > 3);

        scnr.nextLine();

        switch (action) {
            case 1: {
                System.out.print("    Enter the new task name: ");
                String taskName = scnr.nextLine();
                T.setTaskName(taskName);
                break;
            }
            case 2: {
                System.out.print("    Enter the new task description: ");
                String taskDescription = scnr.nextLine();
                T.setTaskDescription(taskDescription);
                break;
            }
            case 3: {
                System.out.print("    Enter the new task deadline (year, month, day, hour, minute): ");
                String str = scnr.nextLine();
                LocalDateTime taskDeadline;

                do {
                    taskDeadline = Main.checkLocalDateValidity(str);
                    if (taskDeadline == null)
                    { str = scnr.nextLine(); }
                } while (taskDeadline == null);

                T.setTaskDeadline(taskDeadline);
                break;
            }
        }

        System.out.print("    Task edited. Continue editing? (Y/N): ");
        if (Main.continueOrNot(scnr) == 'Y' || Main.continueOrNot(scnr) == 'y')
        { return editTask(T); } // Recursive call if the user wants to keep editing.
        else
        { return T; }
    } // Used to make edits to a task.

    public static void openTask(Task T) {
        System.out.println("Task Name: " + T.getTaskName());
        System.out.println("Task Description: " + T.getTaskDescription());
        System.out.println("Task Created On: " + T.getTaskCreated());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("Task Deadline: " + T.getTaskDeadline().format(formatter));
    } // Prints out details of a task.
}