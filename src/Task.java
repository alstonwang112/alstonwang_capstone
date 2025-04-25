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
    private boolean taskPrioritization; // Checks if the task is prioritized (true) or not (false).

    public Task(String taskName, String taskDescription, LocalDateTime taskCreated, LocalDateTime taskDeadline, Queue commentThread, String taskCompletion, boolean taskPriorization) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskCreated = taskCreated;
        this.taskDeadline = taskDeadline;
        this.commentThread = commentThread;
        this.taskCompletion = taskCompletion;
        this.taskPrioritization = taskPriorization;
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

    public void setTaskPrioritization(boolean taskPrioritization) {
        this.taskPrioritization = taskPrioritization;
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

    public boolean getTaskPrioritization() {
        return this.taskPrioritization;
    }

    public static Task createTask(String taskName, String taskDescription, LocalDateTime taskDeadline, Queue commentThread, boolean taskPrioritization) {
        LocalDateTime taskCreated = LocalDateTime.now();
        return new Task(taskName, taskDescription, taskCreated, taskDeadline, commentThread, "P", taskPrioritization);
    } // Creates a new task using an explicit constructor.

    public static int searchTask(String taskName, ArrayList<Task> taskCollection) {
        int size = taskCollection.size();

        for (int i = 0; i < size; i++) {
            if (taskName.equals(taskCollection.get(i).getTaskName()))
            { return i; }
        }

        return -1;
    } // Used to search for the task the user desires.

    public static Task editTask(ArrayList<Task> taskCollection, Task T, ArrayList<List> listCollection) {
        System.out.println("    What would you like to edit?");
        System.out.println("        (1) Edit task name.");
        System.out.println("        (2) Edit task description.");
        System.out.println("        (3) Edit task deadline.");
        System.out.println("        (4) Edit task prioritization.");
        System.out.println("        (5) Mark task as complete.");
        System.out.println("        (6) Delete task.");
        System.out.print("    Enter a number (1 - 5): ");

        Scanner scnr = new Scanner(System.in);
        int action = 0;

        do {
            try {
                action = scnr.nextInt();
                if (action < 1 || action > 6) {
                    System.out.print("    Invalid. Try again: ");
                    scnr.nextLine();
                }
            } catch (InputMismatchException e) {
                System.out.print("    Invalid. Try again: ");
                scnr.nextLine();
            }
        } while (action < 1 || action > 6);

        scnr.nextLine();

        switch (action) {
            case 1: {
                System.out.print("    Enter the new task name: ");
                String taskName = scnr.nextLine();
                if (taskName.equals("QUIT")) { System.exit(1); }
                else {
                    T.setTaskName(taskName);

                    String taskDescription = T.getTaskDescription();
                    LocalDateTime taskCreated = T.getTaskCreated();
                    LocalDateTime taskDeadline = T.getTaskDeadline();

                    search:
                    for (List L : listCollection) {
                        for (Task t : L.getListOfTasks()) {
                            String tDescription = t.getTaskDescription();
                            LocalDateTime tCreated = t.getTaskCreated();
                            LocalDateTime tDeadline = t.getTaskDeadline();

                            int compareCreated = tCreated.compareTo(taskCreated);
                            int compareDeadline = tDeadline.compareTo(taskDeadline);

                            boolean compareDescription = tDescription.equals(taskDescription);

                            if ((compareCreated == 0) && (compareDeadline == 0) && compareDescription) {
                                t.setTaskName(taskName);
                                break search;
                            }
                        }
                    }
                }
                break;
            }
            case 2: {
                System.out.print("    Enter the new task description: ");
                String taskDescription = scnr.nextLine();
                if (taskDescription.equals("QUIT")) { System.exit(1); }
                else {
                    T.setTaskDescription(taskDescription);

                    String taskName = T.getTaskName();
                    LocalDateTime taskCreated = T.getTaskCreated();
                    LocalDateTime taskDeadline = T.getTaskDeadline();

                    search:
                    for (List L : listCollection) {
                        for (Task t : L.getListOfTasks()) {
                            String tName = t.getTaskName();
                            LocalDateTime tCreated = t.getTaskCreated();
                            LocalDateTime tDeadline = t.getTaskDeadline();

                            int compareCreated = tCreated.compareTo(taskCreated);
                            int compareDeadline = tDeadline.compareTo(taskDeadline);

                            boolean compareName = tName.equals(taskName);

                            if ((compareCreated == 0) && (compareDeadline == 0) && compareName) {
                                t.setTaskDescription(taskDescription);
                                break search;
                            }
                        }
                    }
                }
                break;
            }
            case 3: {
                System.out.print("    Enter the new task deadline (year, month, day, hour, minute): ");
                String str = scnr.nextLine();
                if (str.equals("QUIT")) { System.exit(1); }
                else {
                    LocalDateTime taskDeadline;
                    do {
                        taskDeadline = Main.checkLocalDateValidity(str.replaceAll("\\s", ""));
                        if (taskDeadline == null) {
                            str = scnr.nextLine();
                            if (str.equals("QUIT")) { System.exit(1); }
                        }
                    } while (taskDeadline == null);

                    T.setTaskDeadline(taskDeadline);

                    String taskName = T.getTaskName();
                    String taskDescription = T.getTaskDescription();
                    LocalDateTime taskCreated = T.getTaskCreated();

                    search:
                    for (List L : listCollection) {
                        for (Task t : L.getListOfTasks()) {
                            String tName = t.getTaskName();
                            String tDescription = t.getTaskDescription();
                            LocalDateTime tCreated = t.getTaskCreated();

                            int compareCreated = tCreated.compareTo(taskCreated);

                            boolean compareName = tName.equals(taskName);
                            boolean compareDescription = tDescription.equals(taskDescription);

                            if ((compareCreated == 0) && compareName && compareDescription) {
                                t.setTaskDeadline(taskDeadline);
                                break search;
                            }
                        }
                    }
                }
                break;
            }
            case 4: {
                System.out.print("    Prioritize task? (Y/N): ");
                char continueOrNot = Main.continueOrNot(scnr);
                T.setTaskPrioritization(continueOrNot == 'Y' || continueOrNot == 'y');

                String taskName = T.getTaskName();
                String taskDescription = T.getTaskDescription();
                LocalDateTime taskCreated = T.getTaskCreated();
                LocalDateTime taskDeadline = T.getTaskDeadline();

                search:
                for (List L : listCollection) {
                    for (Task t : L.getListOfTasks()) {
                        String tName = t.getTaskName();
                        String tDescription = t.getTaskDescription();
                        LocalDateTime tCreated = t.getTaskCreated();
                        LocalDateTime tDeadline = t.getTaskDeadline();

                        int compareCreated = tCreated.compareTo(taskCreated);
                        int compareDeadline = tDeadline.compareTo(taskDeadline);

                        boolean compareName = tName.equals(taskName);
                        boolean compareDescription = tDescription.equals(taskDescription);

                        if ((compareCreated == 0) && (compareDeadline == 0) && compareName && compareDescription) {
                            t.setTaskPrioritization(continueOrNot == 'Y' || continueOrNot == 'y');
                            break search;
                        }
                    }
                }
                break;
            }
            case 5: {
                T.setTaskCompletion("C");
                String taskName = T.getTaskName();
                String taskDescription = T.getTaskDescription();
                LocalDateTime taskCreated = T.getTaskCreated();
                LocalDateTime taskDeadline = T.getTaskDeadline();

                search:
                for (List L : listCollection) {
                    for (Task t : L.getListOfTasks()) {
                        String tName = t.getTaskName();
                        String tDescription = t.getTaskDescription();
                        LocalDateTime tCreated = t.getTaskCreated();
                        LocalDateTime tDeadline = t.getTaskDeadline();

                        int compareCreated = tCreated.compareTo(taskCreated);
                        int compareDeadline = tDeadline.compareTo(taskDeadline);

                        boolean compareName = tName.equals(taskName);
                        boolean compareDescription = tDescription.equals(taskDescription);

                        if ((compareCreated == 0) && (compareDeadline == 0) && compareName && compareDescription) {
                            t.setTaskCompletion("C");
                            break search;
                        }
                    }
                }
                break;
            }
            case 6: {
                taskCollection.remove(T);

                String taskName = T.getTaskName();
                String taskDescription = T.getTaskDescription();
                LocalDateTime taskCreated = T.getTaskCreated();
                LocalDateTime taskDeadline = T.getTaskDeadline();

                search:
                for (List L : listCollection) {
                    for (Task t : L.getListOfTasks()) {
                        String tName = t.getTaskName();
                        String tDescription = t.getTaskDescription();
                        LocalDateTime tCreated = t.getTaskCreated();
                        LocalDateTime tDeadline = t.getTaskDeadline();

                        int compareCreated = tCreated.compareTo(taskCreated);
                        int compareDeadline = tDeadline.compareTo(taskDeadline);

                        boolean compareName = tName.equals(taskName);
                        boolean compareDescription = tDescription.equals(taskDescription);

                        if ((compareCreated == 0) && (compareDeadline == 0) && compareName && compareDescription) {
                            L.getListOfTasks().remove(t);
                            break search;
                        }
                    }
                }
                return null;
            }
        }

        System.out.print("    Task edited. Continue editing? (Y/N): ");

        char continueOrNot = Main.continueOrNot(scnr);

        if (continueOrNot == 'Y' || continueOrNot == 'y')
        { return editTask(taskCollection, T, listCollection); } // Recursive call if the user wants to keep editing.
        else
        { return T; }
    } // Used to make edits to a task.

    public static void openTask(Task T) {
        System.out.print("Task Name: " + T.getTaskName());
        if (T.getTaskPrioritization()) { System.out.println(" (*)"); }
        else { System.out.println(); }
        System.out.println("Task Description: " + T.getTaskDescription());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("Task Created On: " + T.getTaskCreated().format(formatter));
        System.out.println("Task Deadline: " + T.getTaskDeadline().format(formatter));
        System.out.println("Task Status: " + T.getTaskCompletion());
    } // Prints out details of a task.
}