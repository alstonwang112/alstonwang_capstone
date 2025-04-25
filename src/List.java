import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class List {
    private String listName; // What the list will be called in the directory.
    private String listDescription; // What the description of the list is.
    private LocalDateTime listCreated; // Time and date the list was created.
    private ArrayList<Task> listOfTasks; // Tasks contained within the list.
    private String listCompletion; // Checks if the list is complete (C) or pending (P).

    public List(String listName, String listDescription, LocalDateTime listCreated, ArrayList<Task> listOfTasks, String listCompletion) {
        this.listName = listName;
        this.listDescription = listDescription;
        this.listCreated = listCreated;
        this.listOfTasks = listOfTasks;
        this.listCompletion = listCompletion;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public void setListDescription(String listDescription) {
        this.listDescription = listDescription;
    }

    public void setListCompletion(String listCompletion) {
        this.listCompletion = listCompletion;
    }

    public String getListName() {
        return this.listName; // Returns the name of the list.
    }

    public String getListDescription() {
        return this.listDescription; // Returns the list description.
    }

    public LocalDateTime getListCreated() {
        return this.listCreated; // Returns when the list was created.
    }

    public ArrayList<Task> getListOfTasks() {
        return this.listOfTasks; // Returns when the list of tasks.
    }

    public String getListCompletion() {
        return this.listCompletion;
    }


    public static List createList(String listName, String listDescription, ArrayList<Task> listOfTasks) {
        LocalDateTime listCreated = LocalDateTime.now();
        return new List(listName, listDescription, listCreated, listOfTasks, "P");
    } // Creates a new task using an explicit constructor.

    public static int searchList(String listName, ArrayList<List> listCollection) {
        int size = listCollection.size();

        for (int i = 0; i < size; i++) {
            if (listName.equals(listCollection.get(i).getListName()))
            { return i; }
        }

        return -1;
    } // Used to search for the list the user desires.

    public static List editList(ArrayList<List> listCollection, List L, ArrayList<Task> taskCollection) {
        System.out.println("    What would you like to edit?");
        System.out.println("        (1) Edit list name.");
        System.out.println("        (2) Edit list description.");
        System.out.println("        (3) Add task to list.");
        System.out.println("        (4) Remove task from list.");
        System.out.println("        (5) Delete list.");
        System.out.print("    Enter a number (1 - 5): ");

        Scanner scnr = new Scanner(System.in);
        int action = 0;

        do {
            try {
                action = scnr.nextInt();
                if (action < 1 || action > 5) {
                    System.out.print("    Invalid. Try again: ");
                    scnr.nextLine();
                }
            } catch (InputMismatchException e) {
                System.out.print("    Invalid. Try again: ");
                scnr.nextLine();
            }
        } while (action < 1 || action > 5);

        scnr.nextLine();

        switch (action) {
            case 1: {
                System.out.print("    Enter the new list name: ");
                String listName = scnr.nextLine();
                if (listName.equals("QUIT")) { System.exit(1); }
                else { L.setListName(listName); }
                break;
            }
            case 2: {
                System.out.print("    Enter the new list description: ");
                String listDescription = scnr.nextLine();
                if (listDescription.equals("QUIT")) { System.exit(1); }
                else { L.setListDescription(listDescription); }
                break;
            }
            case 3: {
                System.out.print("    Enter the name of the task you'd like to add: ");
                String taskName = scnr.nextLine();
                if (taskName.equals("QUIT")) { System.exit(1); }
                else {
                    Task T;
                    int taskNum = Task.searchTask(taskName, taskCollection);

                    if (taskNum == -1) {
                        System.out.print("    Task does not exist. A new task will be created. Enter a brief description of your task: ");
                        String taskDescription = scnr.nextLine();
                        if (taskDescription.equals("QUIT")) { System.exit(1); }
                        else {
                            System.out.print("    Enter a numeric deadline for your task (year, month, day, hour, minute): ");
                            String str = scnr.nextLine();
                            if (str.equals("QUIT")) { System.exit(1); }
                            else {
                                System.out.print("    Prioritize task? (Y/N): ");
                                char prioritizeOrNot = Main.continueOrNot(scnr);

                                boolean prioritize;

                                prioritize = (prioritizeOrNot == 'Y') || (prioritizeOrNot == 'y');

                                LocalDateTime taskDeadline;

                                do {
                                    taskDeadline = Main.checkLocalDateValidity(str.replaceAll("\\s", ""));
                                    if (taskDeadline == null) {
                                        str = scnr.nextLine();
                                        if (str.equals("QUIT")) { System.exit(1); }
                                    }
                                } while (taskDeadline == null);

                                Queue commentThread = new Queue(new Node("", null));

                                T = Task.createTask(taskName, taskDescription, taskDeadline, commentThread, prioritize);
                                L.getListOfTasks().add(T);
                            }
                        }
                    } else {
                        T = taskCollection.get(taskNum);
                        L.getListOfTasks().add(T);
                    }
                }
                break;
            }
            case 4: {
                System.out.print("    Enter the name of the task you'd like to remove: ");
                String taskName = scnr.nextLine();
                if (taskName.equals("QUIT")) { System.exit(1); }
                else {
                    int size = L.getListOfTasks().size();
                    int index = -1;

                    for (int i = 0; i < size; i++) {
                        if (taskName.equals(L.getListOfTasks().get(i).getTaskName())) {
                            index = i;
                            break;
                        }
                    }

                    if (index == -1) {
                        System.out.println("    Task does not exist.");
                    } else {
                        L.getListOfTasks().remove(index);
                    }
                }
                break;
            }
            case 5: {
                listCollection.remove(L);
                return null;
            }
        }

        System.out.print("    List edited. Continue editing? (Y/N): ");
        char continueOrNot = Main.continueOrNot(scnr);

        if (continueOrNot == 'Y' || continueOrNot == 'y')
        { return editList(listCollection, L, taskCollection); } // Recursive call if the user wants to keep editing.
        else
        { return L; }
    } // Used to make edits to a list.

    public static void openList(List L) {
        System.out.println("List Name: " + L.getListName());
        System.out.println("List Description: " + L.getListDescription());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("List Created On: " + L.getListCreated().format(formatter));
        System.out.println("Tasks: -------------------------------------------------------------------------------------");

        for (Task T : L.getListOfTasks()) {
            if (T.getTaskPrioritization()) {
                Task.openTask(T);
                System.out.println("-");
            }
        } // Prints all the prioritized tasks first.

        for (Task T : L.getListOfTasks()) {
            if (!(T.getTaskPrioritization())) {
                Task.openTask(T);
                System.out.println("-");
            }
        } // Prints the remainder of the tasks without double printing.

        System.out.println("--------------------------------------------------------------------------------------------");
    } // Prints out the details of a task.

    public static void checkTasksWithinList(List L) {
        for (Task T : L.getListOfTasks()) {
            if (T.getTaskCompletion().equals("P")) { return; }
        }

        L.setListCompletion("C");
    }
}