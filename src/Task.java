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
    private Queue commentThread; // Holds a thread of comments.
    private String taskCompletion; // Checks if the task is complete (C) or pending (P).

    public Task(String taskName, String taskDescription, LocalDateTime taskCreated, LocalDateTime taskDeadline, Queue commentThread, String taskCompletion) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskCreated = taskCreated;
        this.taskDeadline = taskDeadline;
        this.commentThread = commentThread;
        this.taskCompletion = taskCompletion;
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

    public void setTaskCompletion(String taskCompletion) {
        this.taskCompletion = taskCompletion;
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

    public Queue getCommentThread() {
        return this.commentThread; // Returns the comment thread.
    }

    public String getTaskCompletion() {
        return this.taskCompletion;
    }

    public static Task createTask(String taskName, String taskDescription, LocalDateTime taskDeadline, Queue Q) {
        LocalDateTime taskCreated = LocalDateTime.now();
        return new Task(taskName, taskDescription, taskCreated, taskDeadline, Q, "P");
    } // Creates a new task using an explicit constructor.

    public static int searchTask(String taskName, ArrayList<Task> taskCollection) {
        int size = taskCollection.size();

        for (int i = 0; i < size; i++) {
            if (taskName.equals(taskCollection.get(i).getTaskName()))
            { return i; }
        }

        return -1;
    } // Used to search for the task the user desires.

    public static Task editTask(ArrayList<Task> taskCollection, Task T) {
        System.out.println("    What would you like to edit?");
        System.out.println("        (1) Edit task name.");
        System.out.println("        (2) Edit task description.");
        System.out.println("        (3) Edit task deadline.");
        System.out.println("        (4) Mark task as complete.");
        System.out.println("        (5) Delete task.");
        System.out.print("    Enter a number (1 - 5): ");

        Scanner scnr = new Scanner(System.in);
        int action = 0;

        do {
            try {
                action = scnr.nextInt();
                if (action < 1 || action > 5) {
                    System.out.print("Invalid. Try again: ");
                    scnr.nextLine();
                }
            } catch (InputMismatchException e) {
                System.out.print("Invalid. Try again: ");
                scnr.nextLine();
            }
        } while (action < 1 || action > 5);

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
                    taskDeadline = Main.checkLocalDateValidity(str.replaceAll("\\s", ""));
                    if (taskDeadline == null)
                    { str = scnr.nextLine(); }
                } while (taskDeadline == null);

                T.setTaskDeadline(taskDeadline);
                break;
            }
            case 4: {
                T.setTaskCompletion("C");
                break;
            }
            case 5: {
                taskCollection.remove(T);
                break;
            }
        }

        System.out.print("    Task edited. Continue editing? (Y/N): ");

        char continueOrNot = Main.continueOrNot(scnr);

        if (continueOrNot == 'Y' || continueOrNot == 'y')
        { return editTask(taskCollection, T); } // Recursive call if the user wants to keep editing.
        else
        { return T; }
    } // Used to make edits to a task.

    public static void openTask(Task T) {
        System.out.println("Task Name: " + T.getTaskName());
        System.out.println("Task Description: " + T.getTaskDescription());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("Task Created On: " + T.getTaskCreated().format(formatter));
        System.out.println("Task Deadline: " + T.getTaskDeadline().format(formatter));
    } // Prints out details of a task.
}