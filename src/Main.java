import java.io.*;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to Alston Wang's task management application.");
        char continueOrNot; // Used to check if the user wants to continue.
        ArrayList<Task> taskCollection = new ArrayList<>();
        ArrayList<List> listCollection = new ArrayList<>();
        int taskNum;
        int listNum;
        
        do {
            System.out.println("What would you like to do?");
            printOptions();
            System.out.print("Enter a number (1 - 10): ");

            Scanner scnr = new Scanner(System.in);
            int option = 0;

            do {
                try {
                    option = scnr.nextInt();
                    if (option < 1 || option > 10) {
                        System.out.print("Invalid. Try again: ");
                        scnr.nextLine();
                    }
                } catch (InputMismatchException e) {
                    System.out.print("Invalid. Try again: ");
                    scnr.nextLine();
                }
            } while (option < 1 || option > 10); // Will continue until valid input is given.

            scnr.nextLine();

            switch (option) {
                case 1: {
                    System.out.print("(1) Would you like to create (C) a new task or duplicate (D) an existing one? ");
                    String response;
                    char choice;

                    do {
                        response = scnr.nextLine();
                        choice = response.charAt(0);
                    } while((choice != 'C' && choice != 'c' && choice != 'D' && choice != 'd') || response.length() != 1);

                    if (Character.toLowerCase(choice) == 'c') {
                        System.out.print("    Enter the name of task you would like to create: ");
                        String taskName = scnr.nextLine();

                        System.out.print("    Enter a brief description of your task: ");
                        String taskDescription = scnr.nextLine();

                        System.out.print("    Enter a numeric deadline for your task (year, month, day, hour, minute): ");
                        String str = scnr.nextLine();

                        LocalDateTime taskDeadline;

                        do {
                            taskDeadline = checkLocalDateValidity(str);
                            if (taskDeadline == null)
                            { str = scnr.nextLine(); }
                        } while (taskDeadline == null);

                        LinkedList commentThread = new LinkedList(new Node("", null));
                        taskCollection.add(createTask(taskName, taskDescription, taskDeadline, commentThread));
                        System.out.print("    Task created. ");
                    } else {
                        System.out.print("    Enter the name of task you would like to duplicate: ");
                        String taskName = scnr.nextLine();

                        taskNum = searchTask(taskName, taskCollection);

                        if (taskNum == -1)
                        { System.out.print("    Task does not exist. "); }
                        else {
                            String taskDescription = taskCollection.get(taskNum).getTaskDescription();
                            LocalDateTime taskDeadline = taskCollection.get(taskNum).getTaskDeadline();
                            LocalDateTime taskCreated = taskCollection.get(taskNum).getTaskCreated();
                            LinkedList commentThread = new LinkedList(new Node("", null));
                            taskCollection.add(new Task(taskName, taskDescription, taskCreated, taskDeadline, commentThread));
                            System.out.print("    Task duplicated. ");
                        }
                    }
                    break;
                }
                case 2: {
                    System.out.print("(2) Enter the name of the task you would like to edit: ");
                    String taskName = scnr.nextLine();

                    taskNum = searchTask(taskName, taskCollection);

                    if (taskNum == -1)
                    { System.out.print("    Task does not exist. "); }
                    else {
                        taskCollection.set(taskNum, editTask(taskCollection.get(taskNum)));
                        System.out.print("    Task edited. ");
                    }

                    break;
                }
                case 3: {
                    System.out.print("(3) Enter the name of the task you would like to open: ");
                    String taskName = scnr.nextLine();

                    taskNum = searchTask(taskName, taskCollection);

                    if (taskNum == -1)
                    { System.out.print("    Task does not exist. "); }
                    else {
                        openTask(taskCollection.get(taskNum));
                        System.out.print("    Task opened. ");
                    }
                    break;
                }
                case 4: {
                    System.out.print("(4) Would you like to create (C) a new list or duplicate (D) an existing one? ");
                    String response;
                    char choice;

                    do {
                        response = scnr.nextLine();
                        choice = response.charAt(0);
                    } while((choice != 'C' && choice != 'c' && choice != 'D' && choice != 'd') || response.length() != 1);

                    if (Character.toLowerCase(choice) == 'c') {
                        System.out.print("    Enter the name of the list you would like to create: ");
                        String listName = scnr.nextLine();

                        System.out.print("    Enter a brief description of your list: ");
                        String listDescription = scnr.nextLine();

                        ArrayList<Task> list = new ArrayList<>();

                        System.out.print("    Enter the name of the task you would like to add to this list: ");
                        String taskName = scnr.nextLine();

                        taskNum = searchTask(taskName, taskCollection);
                        Task T;

                        if (taskNum != -1)
                        { T = taskCollection.get(taskNum); }
                        else {
                            System.out.print("    Enter a brief description of your task: ");
                            String taskDescription = scnr.nextLine();

                            System.out.print("    Enter a numeric deadline for your task (year, month, day, hour, minute): ");
                            String str = scnr.nextLine();

                            LocalDateTime taskDeadline;

                            do {
                                taskDeadline = checkLocalDateValidity(str);
                                if (taskDeadline == null)
                                { str = scnr.nextLine(); }
                            } while (taskDeadline == null);

                            LinkedList commentThread = new LinkedList(new Node("", null));
                            T = createTask(taskName, taskDescription, taskDeadline, commentThread);
                            taskCollection.add(T);
                        }

                        list.add(T);
                        listCollection.add(createList(listName, listDescription, list));
                        System.out.print("    List created. ");
                    } else {
                        System.out.print("    Enter the name of list you would like to duplicate: ");
                        String listName = scnr.nextLine();

                        listNum = searchList(listName, listCollection);

                        if (listNum == -1)
                        { System.out.print("    List does not exist. "); }
                        else {
                            String listDescription = listCollection.get(listNum).getListDescription();
                            LocalDateTime listCreated = listCollection.get(listNum).getListCreated();
                            ArrayList<Task> listOfTasks = listCollection.get(listNum).getListOfTasks();
                            listCollection.add(new List(listName, listDescription, listCreated, listOfTasks));
                            System.out.print("    List duplicated. ");
                        }
                    }
                    break;
                }
                case 5: {
                    System.out.print("(5) Enter the name of the list you would like to edit: ");
                    String listName = scnr.nextLine();

                    listNum = searchList(listName, listCollection);

                    if (listNum == -1)
                    { System.out.print("    List does not exist. "); }
                    else {
                        listCollection.set(listNum, editList(listCollection.get(listNum)));
                        System.out.print("    List edited. ");
                    }

                    break;
                }
                case 6: {
                    System.out.print("(6) Enter the name of the list you would like to open: ");
                    String listName = scnr.nextLine();

                    listNum = searchList(listName, listCollection);

                    if (listNum == -1)
                    { System.out.print("    List does not exist. "); }
                    else {
                        openList(listCollection.get(listNum));
                        System.out.print("    List opened. ");
                    }
                    break;
                }
                case 7: {
                    System.out.print("(7) Enter the name of the file you would like to open: ");

                    boolean readError; // Used to know when to break out of the do-while loop.
                    FileInputStream readMe = null;

                    do {
                        scnr.nextLine();
                        String nameOfFile = scnr.nextLine();
                        try {
                            readMe = new FileInputStream(nameOfFile);
                            readError = false;
                        } catch (FileNotFoundException e) {
                            System.out.print("    File does not exist. Try again: ");
                            readError = true;
                        }
                    } while (readError);

                    readFile(readMe);
                    System.out.print("    File read. ");
                    break;
                }
                case 8: {
                    System.out.print("(8) Enter the name of the file you would like to write to: ");

                    boolean writeError; // Used to know when to break out of the do-while loop.
                    FileOutputStream writeMe = null;

                    do {
                        scnr.nextLine();
                        String nameOfFile = scnr.nextLine();
                        try {
                            writeMe = new FileOutputStream(nameOfFile);
                            writeError = false;
                        } catch (FileNotFoundException e) {
                            System.out.print("    File does not exist. Try again: ");
                            writeError = true;
                        }
                    } while (writeError);

                    PrintWriter fileWriter = new PrintWriter(writeMe, true);
                    writeFile(fileWriter, writeMe, scnr);

                    System.out.print("    File written to. ");
                    break;
                }
                case 9: {
                    System.out.println("(9): ");
                    int sortItem = sortOptions();
                    int sortBy = sortBy(sortItem);
                    int sortOrder = sortOrder();

                    CompareTasks taskComparator = new CompareTasks();
                    CompareLists listComparator = new CompareLists();

                    switch (sortItem) {
                        case 1: {
                            taskCollection = taskComparator.Sort(taskCollection, sortBy, sortOrder);
                            System.out.print("    All tasks sorted. ");
                            break;
                        }
                        case 2: {
                            System.out.print("    Enter the name of the list you would like to sort: ");
                            String listName = scnr.nextLine();

                            listNum = searchList(listName, listCollection);

                            if (listNum == -1)
                            { System.out.print("    List does not exist. "); }
                            else {
                                ArrayList<Task> listOfTasks = listCollection.get(listNum).getListOfTasks();
                                taskComparator.Sort(listOfTasks, sortBy, sortOrder);
                                System.out.print("    List sorted. ");
                            }
                            break;
                        }
                        case 3: {
                            listCollection = listComparator.Sort(listCollection, sortBy, sortOrder);
                            System.out.print("    All lists sorted. ");
                            break;
                        }
                    }
                    break;
                }
                case 10: {
                    System.out.print("(10) Would you like to add (A) a comment or read (R) the comment thread? ");
                    String response;
                    char choice;

                    do {
                        response = scnr.nextLine();
                        choice = response.charAt(0);
                    } while((choice != 'A' && choice != 'a' && choice != 'R' && choice != 'r') || response.length() != 1);

                    if (Character.toLowerCase(choice) == 'a') {
                        System.out.print("    Enter the name of the task you would like to add a comment to: ");
                        String taskName = scnr.nextLine();

                        taskNum = searchTask(taskName, taskCollection);

                        if (taskNum != -1)
                        { System.out.print("    Task does not exist. "); }
                        else {
                            do {
                                Task T = taskCollection.get(taskNum);
                                System.out.print("    Write your comment here: ");
                                String comment = scnr.nextLine();
                                Node head = T.getCommentThread().head;

                                if (head == null) {
                                    T.setHead(comment);
                                } else {
                                    Node n = new Node(comment, null);
                                    Node curr = head;
                                    Node prev = null;

                                    while (curr != null) {
                                        if (curr.next == null) {
                                            prev = curr;
                                        }
                                        curr = curr.next;
                                    }

                                    prev.next = n;
                                }
                                System.out.print("    Comment added. Add more (Y/N)?");
                                response = scnr.nextLine();
                                choice = response.charAt(0);
                            } while((choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n') || response.length() != 1);
                        }
                    } else {
                        System.out.print("    Enter the name of the task you would like to read the comments of: ");
                        String taskName = scnr.nextLine();

                        taskNum = searchTask(taskName, taskCollection);

                        if (taskNum != -1)
                        { System.out.print("    Task does not exist. "); }
                        else {
                            Task T = taskCollection.get(taskNum);
                            System.out.println("    Reading Comments: ---------------------------------------------------------------------------------");
                            Node head = T.getCommentThread().head;

                            if (head == null) {
                                System.out.print("    No comments available. ");
                            } else {
                                Node curr = head;

                                while (curr != null) {
                                    System.out.println(curr.comment);
                                    curr = curr.next;
                                }
                            }
                        }
                        System.out.println("-------------------------------------------------------------------------------------------------------");
                    }
                    break;
                }
            }

            System.out.println("Continue with another action? (Y/N): ");
            continueOrNot = continueOrNot(scnr);
        } while (continueOrNot == 'Y' || continueOrNot == 'y');

        System.out.println("Application closed.");
        System.exit(1);
    }

    public static void printOptions() {
        System.out.println("    (1) Create/duplicate task.");
        System.out.println("    (2) Edit task.");
        System.out.println("    (3) Open task.");
        System.out.println("    (4) Create/duplicate list.");
        System.out.println("    (5) Edit list.");
        System.out.println("    (6) Open list.");
        System.out.println("    (7) Open text file.");
        System.out.println("    (8) Write to text file.");
        System.out.println("    (9) Sort.");
        System.out.println("    (10) Add/read comment(s).");
    } // Prints a list of possible actions.

    public static LocalDateTime checkLocalDateValidity(String str) {
        String[] date = str.split(",");

        int year = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]);
        int day = Integer.parseInt(date[2]);
        int hour = Integer.parseInt(date[3]);
        int minute = Integer.parseInt(date[4]);
        // FIXME: What happens when a string doesn't have 5 numbers?

        try
        { return LocalDateTime.of(year, month, day, hour, minute); }
        catch (DateTimeException e) {
            System.out.print("    Invalid. Try again: ");
            return null;
        }
    } // Checks to see if the date and time provided is valid.

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
                    taskDeadline = checkLocalDateValidity(str);
                    if (taskDeadline == null)
                    { str = scnr.nextLine(); }
                } while (taskDeadline == null);

                T.setTaskDeadline(taskDeadline);
                break;
            }
        }

        System.out.print("    Task edited. Continue editing? (Y/N): ");
        if (continueOrNot(scnr) == 'Y' || continueOrNot(scnr) == 'y')
        { return editTask(T); } // Recursive call if the user wants to keep editing.
        else
        { return T; }
    } // Used to make edits to a task.

    public static void openTask(Task T) {
        System.out.println("Task Name: " + T.getTaskName());
        System.out.println("Task Description: " + T.getTaskDescription());
        System.out.println("Task Created On: " + T.getTaskCreated());
        System.out.println("Task Deadline: " + T.getTaskDeadline());
    } // Prints out details of a task.

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
        if (continueOrNot(scnr) == 'Y' || continueOrNot(scnr) == 'y')
        { return editList(L); } // Recursive call if the user wants to keep editing.
        else
        { return L; }
    } // Used to make edits to a list.

    public static void openList(List L) {
        System.out.println("List Name: " + L.getListName());
        System.out.println("List Description: " + L.getListName());
        System.out.println("List Created On: " + L.getListCreated());
        System.out.println("Tasks: -------------------------------------------------------------------------------------");

        for (Task T : L.getListOfTasks()) {
            openTask(T);
            System.out.println("-");
        }

        System.out.println("--------------------------------------------------------------------------------------------");
    } // Prints out the details of a task.

    public static void readFile(FileInputStream readMe) {
        Scanner fileReader = new Scanner(readMe);

        while (fileReader.hasNextLine()) {
            String data = fileReader.nextLine();
            System.out.println(data);
        } // Reads the file line-by-line.

        fileReader.close();
    } // Used to read a file.

    public static void writeFile(PrintWriter fileWriter, FileOutputStream writeMe, Scanner scnr) {
        System.out.print("    Add the text you would like to write to " + writeMe + ": ");
        scnr.nextLine();
        String text = scnr.nextLine();
        fileWriter.println(text); // Writes to the file provided.

        System.out.print("    File written to. Continue writing? (Y/N): ");
        if (continueOrNot(scnr) == 'Y' || continueOrNot(scnr) == 'y')
        { writeFile(fileWriter, writeMe, scnr); } // Recursive call if the user wants to keep writing.
        else {
            fileWriter.flush();
            fileWriter.close();
        }
    } // Used to write to a file.

    public static int sortOptions() {
        System.out.println("What would you like to sort?");
        System.out.println("        (1) Sort all tasks.");
        System.out.println("        (2) Sort one list.");
        System.out.println("        (3) Sort all lists.");
        System.out.print("    Enter a number (1 - 3): ");

        Scanner scnr = new Scanner(System.in);
        int option = 0;

        do {
            try {
                option = scnr.nextInt();
                if (option < 1 || option > 3) {
                    System.out.print("Invalid. Try again: ");
                    scnr.nextLine();
                }
            } catch (InputMismatchException e) {
                System.out.print("Invalid. Try again: ");
                scnr.nextLine();
            }
        } while (option < 1 || option > 3); // Will continue until valid input is given.

        return option;
    } // Tells the program what the user wants to sort.

    public static int sortBy(int sortItem) {
        System.out.println("    What would you like to sort by?");
        System.out.println("        (1) Sort by name.");
        System.out.println("        (2) Sort by description.");
        System.out.println("        (3) Sort by creation date.");

        Scanner scnr = new Scanner(System.in);
        int option = 0;

        if (sortItem == 1) {
            System.out.println("        (4) Sort by due date.");
            System.out.print("    Enter a number (1 - 4): ");

            do {
                try {
                    option = scnr.nextInt();
                    if (option < 1 || option > 4) {
                        System.out.print("Invalid. Try again: ");
                        scnr.nextLine();
                    }
                } catch (InputMismatchException e) {
                    System.out.print("Invalid. Try again: ");
                    scnr.nextLine();
                }
            } while (option < 1 || option > 4);

            return option;
        } else {
            System.out.print("    Enter a number (1 - 3): ");

            do {
                try {
                    option = scnr.nextInt();
                    if (option < 1 || option > 3) {
                        System.out.print("Invalid. Try again: ");
                        scnr.nextLine();
                    }
                } catch (InputMismatchException e) {
                    System.out.print("Invalid. Try again: ");
                    scnr.nextLine();
                }
            } while (option < 1 || option > 3);

            return option;
        }
    } // Tells the program what the user wants to sort by.

    public static int sortOrder() {
        System.out.println("    In what order?");
        System.out.println("        (1) Ascending.");
        System.out.println("        (2) Descending.");
        System.out.print("    Enter a number (1 - 2): ");

        Scanner scnr = new Scanner(System.in);
        int option = 0;

        do {
            try {
                option = scnr.nextInt();
                if (option < 1 || option > 2) {
                    System.out.print("Invalid. Try again: ");
                    scnr.nextLine();
                }
            } catch (InputMismatchException e) {
                System.out.print("Invalid. Try again: ");
                scnr.nextLine();
            }
        } while (option < 1 || option > 2); // Will continue until valid input is given.

        return option;
    } // Tells the program what order the user wants to sort by.

    public static char continueOrNot(Scanner scnr) {
        String continueOrNot = scnr.nextLine();
        char more = continueOrNot.charAt(0);

        if ((more != 'Y' && more != 'y' && more != 'N' && more != 'n') || continueOrNot.length() != 1) {
            System.out.print("    Invalid: Try again: ");
            return continueOrNot(scnr);
        } else
        { return more; }
    } // Method to check if the user wants to continue a certain action.
}