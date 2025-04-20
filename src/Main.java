import java.io.*;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to Alston Wang's task management application.");
        System.out.println("Press *** to terminate the program at any time.");
        char continueOrNot; // Used to check if the user wants to continue.
        ArrayList<Task> taskCollection = new ArrayList<>();
        ArrayList<List> listCollection = new ArrayList<>();
        int taskNum;
        int listNum;

        FileInputStream myFile;
        FileOutputStream createFile;
        PrintWriter fileWriter = null;

        try {
            myFile = new FileInputStream("capstone.txt");
            readFile(myFile, taskCollection, listCollection); // If "capstone.txt" exists, it will read it.
        } catch (FileNotFoundException e1) {
            try {
                createFile = new FileOutputStream("capstone.txt");
                fileWriter = new PrintWriter(createFile);
            } // If "capstone.txt" doesn't exist, it will create it.
            catch (FileNotFoundException e2) { System.out.println("Error."); } // Thus, this exception will never be reached.
        }

        do {
            System.out.println("What would you like to do?");
            printOptions();
            System.out.print("Enter a number (1 - 8): ");

            Scanner scnr = new Scanner(System.in);
            int option = 0;

            do {
                try {
                    option = scnr.nextInt();
                    if (option < 1 || option > 8) {
                        System.out.print("Invalid. Try again: ");
                        scnr.nextLine();
                    }
                } catch (InputMismatchException e) {
                    System.out.print("Invalid. Try again: ");
                    scnr.nextLine();
                }
            } while (option < 1 || option > 8); // Will continue until valid input is given.

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
                        taskCollection.add(Task.createTask(taskName, taskDescription, taskDeadline, commentThread));
                        System.out.print("    Task created. ");
                    } else {
                        System.out.print("    Enter the name of task you would like to duplicate: ");
                        String taskName = scnr.nextLine();

                        taskNum = Task.searchTask(taskName, taskCollection);

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

                    taskNum = Task.searchTask(taskName, taskCollection);

                    if (taskNum == -1)
                    { System.out.print("    Task does not exist. "); }
                    else {
                        taskCollection.set(taskNum, Task.editTask(taskCollection.get(taskNum)));
                        System.out.print("    Task edited. ");
                    }

                    break;
                }
                case 3: {
                    System.out.print("(3) Enter the name of the task you would like to open: ");
                    String taskName = scnr.nextLine();

                    taskNum = Task.searchTask(taskName, taskCollection);

                    if (taskNum == -1)
                    { System.out.print("    Task does not exist. "); }
                    else {
                        Task.openTask(taskCollection.get(taskNum));
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

                        taskNum = Task.searchTask(taskName, taskCollection);
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
                            T = Task.createTask(taskName, taskDescription, taskDeadline, commentThread);
                            taskCollection.add(T);
                        }

                        list.add(T);
                        listCollection.add(List.createList(listName, listDescription, list));
                        System.out.print("    List created. ");
                    } else {
                        System.out.print("    Enter the name of list you would like to duplicate: ");
                        String listName = scnr.nextLine();

                        listNum = List.searchList(listName, listCollection);

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

                    listNum = List.searchList(listName, listCollection);

                    if (listNum == -1)
                    { System.out.print("    List does not exist. "); }
                    else {
                        listCollection.set(listNum, List.editList(listCollection.get(listNum)));
                        System.out.print("    List edited. ");
                    }

                    break;
                }
                case 6: {
                    System.out.print("(6) Enter the name of the list you would like to open: ");
                    String listName = scnr.nextLine();

                    listNum = List.searchList(listName, listCollection);

                    if (listNum == -1)
                    { System.out.print("    List does not exist. "); }
                    else {
                        List.openList(listCollection.get(listNum));
                        System.out.print("    List opened. ");
                    }
                    break;
                }
                case 7: {
                    System.out.println("(7): ");
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

                            listNum = List.searchList(listName, listCollection);

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
                case 8: {
                    System.out.print("(8): ");
                    int comments = commentOptions();
                    boolean exists = false;
                    String taskName;
                    Task T = null;

                    do {
                        System.out.print("    Enter the name of the task you want to open the comments thread of: ");
                        taskName = scnr.nextLine();
                        taskNum = Task.searchTask(taskName, taskCollection);
                        if (taskNum == -1) {
                            System.out.print("    Task does not exist. ");
                        } else {
                            T = taskCollection.get(taskNum);
                            exists = true;
                        }
                    } while(!exists);

                    String comment;

                    switch (comments) {
                        case 1: {
                            System.out.print("    Write your comment: ");
                            comment = scnr.nextLine();
                            if (T.getCommentThread().isEmpty()) {
                                T.getCommentThread().head = new Node(comment, null);
                            } else {
                                Queue.add(comment);
                            }
                            break;
                        }
                        case 2: {
                            System.out.print("    Write the comment you'd like to edit: ");
                            comment = scnr.nextLine();

                            Node curr = T.getCommentThread().head;
                            while (curr != null) {
                                if (curr.comment.equals(comment)) {
                                    break;
                                } else {
                                    curr = curr.next;
                                }
                            }

                            if (curr == null) {
                                System.out.println("    This comment doesn't exist. Add a new comment instead: ");
                                comment = scnr.nextLine();
                                if (T.getCommentThread().isEmpty()) {
                                    T.getCommentThread().head = new Node(comment, null);
                                } else {
                                    Queue.add(comment);
                                }
                                break;
                            } else {
                                System.out.print("    Write the comment you'd like to replace it with: ");
                                curr.comment = scnr.nextLine();
                                break;
                            }
                        }
                        case 3: {
                            String deleted = Queue.remove();
                            break;
                        }
                        case 4: {

                            break;
                        }
                    }

                    if (Character.toLowerCase(choice) == 'a') {
                        System.out.print("    Enter the name of the task you would like to add a comment to: ");
                        String taskName = scnr.nextLine();

                        taskNum = Task.searchTask(taskName, taskCollection);

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

                        taskNum = Task.searchTask(taskName, taskCollection);

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

        writeFile(fileWriter, taskCollection, listCollection);

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
        System.out.println("    (7) Sort.");
        System.out.println("    (8) Add/read comment(s).");
    }

    public static LocalDateTime checkLocalDateValidity(String str) {
        String[] date = str.split(",");
        if (date.length != 5) {
            System.out.print("    Invalid. Try again: ");
            return null;
        } else {
            int year = Integer.parseInt(date[0]);
            int month = Integer.parseInt(date[1]);
            int day = Integer.parseInt(date[2]);
            int hour = Integer.parseInt(date[3]);
            int minute = Integer.parseInt(date[4]);
            try { return LocalDateTime.of(year, month, day, hour, minute); } catch (DateTimeException e) {
                System.out.print("    Invalid. Try again: ");
                return null;
            }
        }
    }

    public static void readFile(FileInputStream myFile, ArrayList<Task> taskCollection, ArrayList<List> listCollection) {
        Scanner fileReader = new Scanner(myFile);

        while (fileReader.hasNextLine()) {
            String[] external_data = fileReader.nextLine().split("~~");
            if (external_data[0].equals("T")) {
                String taskName = external_data[1];
                String taskDescription = external_data[2];
                LocalDateTime taskCreated = LocalDateTime.parse(external_data[3]);
                LocalDateTime taskDeadline = LocalDateTime.parse(external_data[3]);

                String s1 = external_data[4];
                Node n1 = new Node(s1, null);
                LinkedList commentThread = new LinkedList(n1);
                Node curr = n1;

                int length = external_data.length;
                if (length >= 6) {
                    for (int i = 5; i < length; i++) {
                        String s2 = external_data[i];
                        Node n2 = new Node(s2, null);
                        curr.next = n2;
                        curr = n2;
                    }
                }

                Task T = new Task(taskName, taskDescription, taskCreated, taskDeadline, commentThread);
                taskCollection.add(T);
            } else if (external_data[0].equals("L")) {
                String listName = external_data[1];
                String listDescription = external_data[2];
                LocalDateTime listCreated = LocalDateTime.parse(external_data[3]);
                ArrayList<Task> listOfTasks = new ArrayList<>();

                while (fileReader.hasNextLine()) {
                    String[] internal_data = fileReader.nextLine().split("~~");
                    if (internal_data[0].equals("L")) {
                        break;
                    }
                    String taskName = external_data[1];
                    String taskDescription = external_data[2];
                    LocalDateTime taskCreated = LocalDateTime.parse(external_data[3]);
                    LocalDateTime taskDeadline = LocalDateTime.parse(external_data[3]);

                    String s1 = external_data[4];
                    Node n1 = new Node(s1, null);
                    LinkedList commentThread = new LinkedList(n1);
                    Node curr = n1;

                    int length = external_data.length;
                    if (length >= 6) {
                        for (int i = 5; i < length; i++) {
                            String s2 = external_data[i];
                            Node n2 = new Node(s2, null);
                            curr.next = n2;
                            curr = n2;
                        }
                    }

                    Task T = new Task(taskName, taskDescription, taskCreated, taskDeadline, commentThread);
                    taskCollection.add(T);
                    listOfTasks.add(T);
                }

                List L = new List(listName, listDescription, listCreated, listOfTasks);
                listCollection.add(L);
            }
        }
        fileReader.close();
    }

    public static void writeFile(PrintWriter fileWriter, ArrayList<Task> taskCollection, ArrayList<List> listCollection) {
        for (Task T : taskCollection) {
            String taskName = T.getTaskName();
            String taskDescription = T.getTaskDescription();
            String taskCreated = T.getTaskCreated().toString();
            String taskDeadline = T.getTaskCreated().toString();
            fileWriter.write("T~~" + taskName + "~~" + taskDescription + "~~" + taskCreated + "~~" + taskDeadline + "~~");

            Node curr = T.getCommentThread().head;
            while (curr != null) {
                String comment = curr.comment;
                fileWriter.write("~~" + comment);
                curr = curr.next;
            }

            fileWriter.write("\n");
        }

        for (List L : listCollection) {
            String listName = L.getListName();
            String listDescription = L.getListDescription();
            String listCreated = L.getListCreated().toString();
            fileWriter.write("L~~" + listName + "~~" + listDescription + "~~" + listCreated + "\n");

            for (Task T : L.getListOfTasks()) {
                String taskName = T.getTaskName();
                String taskDescription = T.getTaskDescription();
                String taskCreated = T.getTaskCreated().toString();
                String taskDeadline = T.getTaskCreated().toString();
                fileWriter.write("T~~" + taskName + "~~" + taskDescription + "~~" + taskCreated + "~~" + taskDeadline + "~~");

                Node curr = T.getCommentThread().head;
                while (curr != null) {
                    String comment = curr.comment;
                    fileWriter.write("~~" + comment);
                    curr = curr.next;
                }

                fileWriter.write("\n");
            }
        }

        fileWriter.flush();
        fileWriter.close();
    }

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
    }

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
    }

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
    }

    public static int commentOptions() {
        System.out.println("What would you like to do?");
        System.out.println("        (1) Add new comment.");
        System.out.println("        (2) Edit existing comment.");
        System.out.println("        (3) Remove most recent comment.");
        System.out.println("        (4) Read all comments.");
        System.out.print("    Enter a number (1 - 4): ");

        Scanner scnr = new Scanner(System.in);
        int option = 0;

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
        } while (option < 1 || option > 4); // Will continue until valid input is given.

        return option;
    }

    public static char continueOrNot(Scanner scnr) {
        String continueOrNot = scnr.nextLine();
        char more = continueOrNot.charAt(0);

        if ((more != 'Y' && more != 'y' && more != 'N' && more != 'n') || continueOrNot.length() != 1) {
            System.out.print("    Invalid: Try again: ");
            return continueOrNot(scnr);
        } else
        { return more; }
    }
}