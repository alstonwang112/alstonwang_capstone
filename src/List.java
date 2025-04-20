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

    public List(String listName, String listDescription, LocalDateTime listCreated, ArrayList<Task> listOfTasks) {
        this.listName = listName;
        this.listDescription = listDescription;
        this.listCreated = listCreated;
        this.listOfTasks = listOfTasks;
    }

    List(List L) {
        System.out.println("Copy constructor called");
        listName = L.listName;
        listDescription = L.listDescription;
        listCreated = L.listCreated;
        listOfTasks = L.listOfTasks;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public void setListDescription(String listDescription) {
        this.listDescription = listDescription;
    }

    public void setListOfTasks(ArrayList<Task> L) {
        this.listOfTasks = L;
    }

    public String getListName() {
        return this.listName; // Returns the name of the list.
    }

    public String getListDescription() {
        return this.listDescription; // Returns the list description..
    }

    public LocalDateTime getListCreated() {
        return this.listCreated; // Returns when the list was created.
    }

    public ArrayList<Task> getListOfTasks() {
        return this.listOfTasks; // Returns when the list of tasks.
    }

    public static List createList(String listName, String listDescription, ArrayList<Task> listOfTasks) {
        LocalDateTime listCreated = LocalDateTime.now();
        return new List(listName, listDescription, listCreated, listOfTasks);
    } // Creates a new task using an explicit constructor.

    public static int searchList(String listName, ArrayList<List> listCollection) {
        int size = listCollection.size();

        for (int i = 0; i < size; i++) {
            if (listName.equals(listCollection.get(i).getListName()))
            { return i; }
        }

        return -1;
    } // Used to search for the list the user desires.

    public static List editList(List L) {
        System.out.println("    What would you like to edit?");
        System.out.println("        (1) Edit list name.");
        System.out.println("        (2) Edit list description.");
        System.out.println("        (3) Remove task from list.");
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
                System.out.print("    Enter the new list name: ");
                String listName = scnr.nextLine();
                L.setListName(listName);
                break;
            }
            case 2: {
                System.out.print("    Enter the new list description: ");
                String listDescription = scnr.nextLine();
                L.setListDescription(listDescription);
                break;
            }
            case 3: {
                System.out.print("    Enter the name of the task you'd like to remove: ");
                String taskName = scnr.nextLine();

                int size = L.getListOfTasks().size();
                int index = -1;

                for (int i = 0; i < size; i++) {
                    if (taskName.equals(L.getListOfTasks().get(i).getTaskName())) {
                        index = i;
                        break;
                    }
                }

                if (index == -1)
                { System.out.println("    Task does not exist."); }
                else
                { L.getListOfTasks().remove(index); }
                break;
            }
        }

        System.out.print("    List edited. Continue editing? (Y/N): ");
        if (Main.continueOrNot(scnr) == 'Y' || Main.continueOrNot(scnr) == 'y')
        { return editList(L); } // Recursive call if the user wants to keep editing.
        else
        { return L; }
    } // Used to make edits to a list.

    public static void openList(List L) {
        System.out.println("List Name: " + L.getListName());
        System.out.println("List Description: " + L.getListName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("List Created On: " + L.getListCreated().format(formatter));
        System.out.println("Tasks: -------------------------------------------------------------------------------------");

        for (Task T : L.getListOfTasks()) {
            Task.openTask(T);
            System.out.println("-");
        }

        System.out.println("--------------------------------------------------------------------------------------------");
    } // Prints out the details of a task.
}