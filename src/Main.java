import java.io.*;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to Alston Wang's task management application.");
        System.out.println("Enter QUIT to terminate the program at any time.");
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
        } catch (FileNotFoundException e1) { } // If not, no worries, we will create it later.

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
                    String response = scnr.nextLine();
                    if (response.equals("QUIT")) { System.exit(1); }
                    else {
                        char choice = response.charAt(0);

                        while ((choice != 'C' && choice != 'c' && choice != 'D' && choice != 'd') || response.length() != 1) {
                            System.out.print("    Invalid. Try again: ");
                            response = scnr.nextLine();
                            if (response.equals("QUIT")) { System.exit(1); }
                            choice = response.charAt(0);
                        }

                        if (Character.toLowerCase(choice) == 'c') {
                            System.out.print("    Enter the name of task you would like to create: ");
                            String taskName = scnr.nextLine();
                            if (taskName.equals("QUIT")) { System.exit(1); }
                            else {
                                System.out.print("    Enter a brief description of your task: ");
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
                                            taskDeadline = checkLocalDateValidity(str.replaceAll("\\s", ""));
                                            if (taskDeadline == null) {
                                                str = scnr.nextLine();
                                                if (str.equals("QUIT")) { System.exit(1); }
                                            }
                                        } while (taskDeadline == null);

                                        Queue commentThread = new Queue(new Node("", null));
                                        taskCollection.add(Task.createTask(taskName, taskDescription, taskDeadline, commentThread, prioritize));
                                        System.out.print("    Task created. ");
                                    }
                                }
                            }
                        } else {
                            System.out.print("    Enter the name of task you would like to duplicate: ");
                            String taskName = scnr.nextLine();
                            if (taskName.equals("QUIT")) { System.exit(1); }

                            taskNum = Task.searchTask(taskName, taskCollection);

                            if (taskNum == -1) {
                                System.out.print("    Task does not exist. ");
                            } else {
                                System.out.print("    Enter the name of this copy: ");
                                taskName = scnr.nextLine();
                                String taskDescription = taskCollection.get(taskNum).getTaskDescription();
                                LocalDateTime taskDeadline = taskCollection.get(taskNum).getTaskDeadline();
                                LocalDateTime taskCreated = taskCollection.get(taskNum).getTaskCreated();
                                Queue commentThread = new Queue(new Node("", null));
                                boolean prioritize = taskCollection.get(taskNum).getTaskPrioritization();
                                taskCollection.add(new Task(taskName, taskDescription, taskCreated, taskDeadline, commentThread, "P", prioritize));
                                System.out.print("    Task duplicated. ");
                            }
                        }
                    }
                    break;
                }
                case 2: {
                    System.out.print("(2) Enter the name of the task you would like to edit: ");
                    String taskName = scnr.nextLine();
                    if (taskName.equals("QUIT")) { System.exit(1); }

                    taskNum = Task.searchTask(taskName, taskCollection);

                    if (taskNum == -1)
                    { System.out.print("    Task does not exist. "); }
                    else {
                        Task T = Task.editTask(taskCollection, taskCollection.get(taskNum), listCollection);
                        if (T == null) { System.out.print("    Task deleted. "); }
                        else {
                            taskCollection.set(taskNum, Task.editTask(taskCollection, taskCollection.get(taskNum), listCollection));
                            System.out.print("    Task edited. ");
                        }
                    }

                    break;
                }
                case 3: {
                    System.out.print("(3) Enter the name of the task you would like to open: ");
                    String taskName = scnr.nextLine();
                    if (taskName.equals("QUIT")) { System.exit(1); }

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
                        if (response.equals("QUIT")) { System.exit(1); }
                        choice = response.charAt(0);
                    } while((choice != 'C' && choice != 'c' && choice != 'D' && choice != 'd') || response.length() != 1);

                    if (Character.toLowerCase(choice) == 'c') {
                        System.out.print("    Enter the name of the list you would like to create: ");
                        String listName = scnr.nextLine();
                        if (listName.equals("QUIT")) { System.exit(1); }
                        else {
                            System.out.print("    Enter a brief description of your list: ");
                            String listDescription = scnr.nextLine();
                            if (listDescription.equals("QUIT")) { System.exit(1); }
                            else {
                                ArrayList<Task> list = new ArrayList<>();

                                System.out.print("    Enter the name of the task you would like to add to this list: ");
                                String taskName = scnr.nextLine();
                                if (taskName.equals("QUIT")) { System.exit(1); }
                                else {
                                    taskNum = Task.searchTask(taskName, taskCollection);
                                    Task T;

                                    if (taskNum != -1) {
                                        T = taskCollection.get(taskNum);
                                        list.add(T);
                                        listCollection.add(List.createList(listName, listDescription, list));
                                        System.out.print("    List created. ");
                                    }
                                    else {
                                        System.out.print("    Enter a brief description of your task: ");
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
                                                    taskDeadline = checkLocalDateValidity(str.replaceAll("\\s", ""));
                                                    if (taskDeadline == null) {
                                                        str = scnr.nextLine();
                                                        if (str.equals("QUIT")) { System.exit(1); }
                                                    }
                                                } while (taskDeadline == null);

                                                Queue commentThread = new Queue(new Node("", null));
                                                T = Task.createTask(taskName, taskDescription, taskDeadline, commentThread, prioritize);
                                                taskCollection.add(T);
                                                list.add(T);
                                                listCollection.add(List.createList(listName, listDescription, list));
                                                System.out.print("    List created. ");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        System.out.print("    Enter the name of list you would like to duplicate: ");
                        String listName = scnr.nextLine();
                        if (listName.equals("QUIT")) { System.exit(1); }
                        else {
                            listNum = List.searchList(listName, listCollection);

                            if (listNum == -1) {
                                System.out.print("    List does not exist. ");
                            } else {
                                System.out.print("    Enter the name of this copy: ");
                                listName = scnr.nextLine();
                                String listDescription = listCollection.get(listNum).getListDescription();
                                LocalDateTime listCreated = listCollection.get(listNum).getListCreated();
                                ArrayList<Task> listOfTasks = listCollection.get(listNum).getListOfTasks();
                                listCollection.add(new List(listName, listDescription, listCreated, listOfTasks, "P"));
                                System.out.print("    List duplicated. ");
                            }
                        }
                    }
                    break;
                }
                case 5: {
                    System.out.print("(5) Enter the name of the list you would like to edit: ");
                    String listName = scnr.nextLine();
                    if (listName.equals("QUIT")) { System.exit(1); }
                    else {
                        listNum = List.searchList(listName, listCollection);

                        if (listNum == -1) {
                            System.out.print("    List does not exist. ");
                        } else {
                            List L = List.editList(listCollection, listCollection.get(listNum), taskCollection);
                            if (L == null) { System.out.print("    List deleted. "); }
                            else {
                                listCollection.set(listNum, List.editList(listCollection, listCollection.get(listNum), taskCollection));
                                System.out.print("    List edited. ");
                            }
                        }
                    }
                    break;
                }
                case 6: {
                    System.out.print("(6) Enter the name of the list you would like to open: ");
                    String listName = scnr.nextLine();
                    if (listName.equals("QUIT")) { System.exit(1); }
                    else {
                        listNum = List.searchList(listName, listCollection);

                        if (listNum == -1) {
                            System.out.print("    List does not exist. ");
                        } else {
                            List.openList(listCollection.get(listNum));
                            System.out.print("    List opened. ");
                        }
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
                            if (listName.equals("QUIT")) { System.exit(1); }
                            else {
                                listNum = List.searchList(listName, listCollection);

                                if (listNum == -1) {
                                    System.out.print("    List does not exist. ");
                                } else {
                                    ArrayList<Task> listOfTasks = listCollection.get(listNum).getListOfTasks();
                                    taskComparator.Sort(listOfTasks, sortBy, sortOrder);
                                    System.out.print("    List sorted. ");
                                }
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
                    System.out.print("(8) ");
                    int commentOption = commentOptions();
                    boolean exists = false;
                    String taskName;
                    Task T = null;

                    System.out.print("    Enter the name of the task you want to act on: ");

                    do {
                        taskName = scnr.nextLine();
                        if (taskName.equals("QUIT")) { System.exit(1); }
                        else {
                            taskNum = Task.searchTask(taskName, taskCollection);
                            if (taskNum == -1) {
                                System.out.print("    Task does not exist. Try again: ");
                            } else {
                                T = taskCollection.get(taskNum);
                                exists = true;
                            }
                        }
                    } while(!exists);

                    String taskDescription = T.getTaskDescription();
                    LocalDateTime taskCreated = T.getTaskCreated();
                    LocalDateTime taskDeadline = T.getTaskDeadline();

                    String comment;
                    Queue Q = T.getCommentThread();
                    Queue copy = null;

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
                                copy = t.getCommentThread();
                                break search;
                            }
                        }
                    }

                    switch (commentOption) {
                        case 1: {
                            System.out.print("    Write your comment: ");
                            comment = scnr.nextLine();
                            if (comment.equals("QUIT")) { System.exit(1); }
                            else {
                                if (Q.isEmpty()) {
                                    Q.head = new Node(comment, null);
                                    if (copy != null) { copy.head = new Node(comment, null); }
                                } else {
                                    Q.add(comment);
                                    if (copy != null) {
                                        copy.add(comment);
                                    }
                                }
                            }
                            break;
                        }
                        case 2: {
                            System.out.print("    Write the comment you'd like to edit: ");
                            comment = scnr.nextLine();
                            if (comment.equals("QUIT")) { System.exit(1); }
                            else {
                                Node curr = Q.head;
                                Node currCopy;
                                if (copy != null) {
                                    currCopy = copy.head;
                                } else {
                                    currCopy = null;
                                }

                                if (copy == null) {
                                    while ((curr != null)) {
                                        if (curr.comment.equals(comment)) {
                                            break;
                                        } else {
                                            curr = curr.next;
                                        }
                                    }

                                    if (curr == null) {
                                        System.out.println("    This comment doesn't exist. Add a new comment instead: ");
                                        comment = scnr.nextLine();
                                        if (comment.equals("QUIT")) { System.exit(1); }
                                        else {
                                            if (Q.isEmpty()) {
                                                Q.head = new Node(comment, null);
                                            } else {
                                                Q.add(comment);
                                            }
                                        }
                                        break;
                                    } else {
                                        System.out.print("    Write the comment you'd like to replace it with: ");
                                        comment = scnr.nextLine();
                                        if (comment.equals("QUIT")) { System.exit(1); }
                                        else { curr.comment = comment; }
                                        break;
                                    }
                                } else {
                                    while ((curr != null) && (currCopy != null)) {
                                        if (curr.comment.equals(comment) && currCopy.comment.equals(comment)) {
                                            break;
                                        } else {
                                            curr = curr.next;
                                            currCopy = currCopy.next;
                                        }
                                    }

                                    if ((curr == null) && (currCopy == null)) {
                                        System.out.println("    This comment doesn't exist. Add a new comment instead: ");
                                        comment = scnr.nextLine();
                                        if (comment.equals("QUIT")) { System.exit(1); }
                                        else {
                                            if (Q.isEmpty() && copy.isEmpty()) {
                                                Q.head = new Node(comment, null);
                                                copy.head = new Node(comment, null);
                                            } else {
                                                Q.add(comment);
                                                copy.add(comment);
                                            }
                                        }
                                        break;
                                    } else {
                                        System.out.print("    Write the comment you'd like to replace it with: ");
                                        comment = scnr.nextLine();
                                        if (comment.equals("QUIT")) { System.exit(1); }
                                        else {
                                            curr.comment = comment;
                                            currCopy.comment = comment;
                                        }
                                        break;
                                    }
                                }
                            }
                            break;
                        }
                        case 3: {
                            if (Q.isEmpty()) {
                                System.out.print("No comments available. ");
                            } else {
                                String s = Q.remove();
                                if (copy != null) { copy.remove(); }
                                if (s.isEmpty()) {
                                    s = Q.remove();
                                    if (copy != null) { copy.remove(); }
                                }
                                System.out.println("\"" + s + "\" successfully deleted.");
                            }
                            break;
                        }
                        case 4: {
                            System.out.println("--- Reading Comments: ---------------------------------------------------------------------------------");

                            if (Q.isEmpty()) { System.out.println("    No comments available. "); } else {
                                String s = Q.remove();
                                if (s.isEmpty()) {
                                    s = Q.remove(); // Remove again to get the next, non-empty String.
                                }
                                System.out.println(s);

                                Node head = new Node(s, null);
                                Queue storage = new Queue(head);

                                while (!Q.isEmpty()) {
                                    String print = Q.remove();
                                    System.out.println(print);
                                    storage.add(print);
                                }

                                Q.head = new Node(storage.remove(), null);

                                while (!storage.isEmpty()) {
                                    Q.add(storage.remove());
                                }
                            }

                            System.out.println("-------------------------------------------------------------------------------------------------------");
                            break;
                        }
                    }
                    break;
                }
                case 9: {
                    System.out.println("(9)");
                    Generic.check(taskCollection);
                    break;
                }
                case 10: {
                    System.out.println("(10)");
                    Generic.check(listCollection);
                    break;
                }
            }

            listCollection.removeIf(L -> L.getListOfTasks().isEmpty());

            System.out.print("Continue with another action? (Y/N): ");
            continueOrNot = continueOrNot(scnr);
        } while (continueOrNot == 'Y' || continueOrNot == 'y');

        try {
            createFile = new FileOutputStream("capstone.txt");
            fileWriter = new PrintWriter(createFile, false);
        } // If "capstone.txt" exists, we will write to it.
        catch (FileNotFoundException e2) { System.out.println("Saved."); } // If not, no worries, we will create it.

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
        System.out.println("    (8) Add, edit, remove, or read comment(s).");
        System.out.println("    (9) Check pending tasks.");
        System.out.println("    (10) Check pending lists.");
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
                LocalDateTime taskDeadline = LocalDateTime.parse(external_data[4]);
                String taskCompletion = external_data[5];
                boolean taskPrioritization = Boolean.parseBoolean(external_data[6]);

                int length = external_data.length;
                Queue commentThread;

                if (length > 7) {
                    String s1 = external_data[7];
                    Node n1 = new Node(s1, null);
                    commentThread = new Queue(n1);
                    for (int i = 8; i < length; i++) {
                        String s2 = external_data[i];
                        commentThread.add(s2);
                    }
                } else { commentThread = new Queue(null); }

                Task T = new Task(taskName, taskDescription, taskCreated, taskDeadline, commentThread, taskCompletion, taskPrioritization);
                taskCollection.add(T);
            } else if (external_data[0].equals("L")) {
                String listName = external_data[1];
                String listDescription = external_data[2];
                LocalDateTime listCreated = LocalDateTime.parse(external_data[3]);
                String listCompletion = external_data[4];
                ArrayList<Task> listOfTasks = new ArrayList<>();

                while (fileReader.hasNextLine()) {
                    String[] internal_data = fileReader.nextLine().split("~~");
                    if (internal_data[0].equals("L")) {
                        break;
                    }
                    String taskName = internal_data[1];
                    String taskDescription = internal_data[2];
                    LocalDateTime taskCreated = LocalDateTime.parse(internal_data[3]);
                    LocalDateTime taskDeadline = LocalDateTime.parse(internal_data[4]);
                    String taskCompletion = internal_data[5];
                    boolean taskPrioritization = Boolean.parseBoolean(internal_data[6]);

                    int length = internal_data.length;
                    Queue commentThread;

                    if (length > 7) {
                        String s1 = internal_data[7];
                        Node n1 = new Node(s1, null);
                        commentThread = new Queue(n1);
                        for (int i = 8; i < length; i++) {
                            String s2 = internal_data[i];
                            commentThread.add(s2);
                        }
                    } else { commentThread = new Queue(null); }

                    Task T = new Task(taskName, taskDescription, taskCreated, taskDeadline, commentThread, taskCompletion, taskPrioritization);
                    listOfTasks.add(T);
                }

                List L = new List(listName, listDescription, listCreated, listOfTasks, listCompletion);
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
            String taskCompletion = T.getTaskCompletion();
            String taskPrioritization = String.valueOf(T.getTaskPrioritization());
            fileWriter.write("T~~" + taskName + "~~" + taskDescription + "~~" + taskCreated + "~~" + taskDeadline + "~~" + taskCompletion + "~~" + taskPrioritization);

            Queue Q = T.getCommentThread();
            while (!Q.isEmpty()) {
                String comment = Q.remove();
                fileWriter.write("~~" + comment);
            }

            fileWriter.write("\n");
        }

        for (List L : listCollection) {
            String listName = L.getListName();
            String listDescription = L.getListDescription();
            String listCreated = L.getListCreated().toString();
            String listCompletion = L.getListCompletion();
            fileWriter.write("L~~" + listName + "~~" + listDescription + "~~" + listCreated + "~~" + listCompletion + "\n");

            for (Task T : L.getListOfTasks()) {
                String taskName = T.getTaskName();
                String taskDescription = T.getTaskDescription();
                String taskCreated = T.getTaskCreated().toString();
                String taskDeadline = T.getTaskCreated().toString();
                String taskCompletion = T.getTaskCompletion();
                String taskPrioritization = String.valueOf(T.getTaskPrioritization());
                fileWriter.write("T~~" + taskName + "~~" + taskDescription + "~~" + taskCreated + "~~" + taskDeadline + "~~" + taskCompletion + "~~" + taskPrioritization);

                Queue Q = T.getCommentThread();
                while (!Q.isEmpty()) {
                    String comment = Q.remove();
                    fileWriter.write("~~" + comment);
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
                    System.out.print("    Invalid. Try again: ");
                    scnr.nextLine();
                }
            } catch (InputMismatchException e) {
                System.out.print("    Invalid. Try again: ");
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
                        System.out.print("    Invalid. Try again: ");
                        scnr.nextLine();
                    }
                } catch (InputMismatchException e) {
                    System.out.print("    Invalid. Try again: ");
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
                        System.out.print("    Invalid. Try again: ");
                        scnr.nextLine();
                    }
                } catch (InputMismatchException e) {
                    System.out.print("    Invalid. Try again: ");
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
                    System.out.print("    Invalid. Try again: ");
                    scnr.nextLine();
                }
            } catch (InputMismatchException e) {
                System.out.print("    Invalid. Try again: ");
                scnr.nextLine();
            }
        } while (option < 1 || option > 2); // Will continue until valid input is given.

        return option;
    }

    public static int commentOptions() {
        System.out.println("What would you like to do?");
        System.out.println("        (1) Add new comment.");
        System.out.println("        (2) Edit existing comment.");
        System.out.println("        (3) Remove earliest comment.");
        System.out.println("        (4) Read all comments.");
        System.out.print("    Enter a number (1 - 4): ");

        Scanner scnr = new Scanner(System.in);
        int option = 0;

        do {
            try {
                option = scnr.nextInt();
                if (option < 1 || option > 4) {
                    System.out.print("    Invalid. Try again: ");
                    scnr.nextLine();
                }
            } catch (InputMismatchException e) {
                System.out.print("    Invalid. Try again: ");
                scnr.nextLine();
            }
        } while (option < 1 || option > 4); // Will continue until valid input is given.

        return option;
    }

    public static char continueOrNot(Scanner scnr) {
        String continueOrNot = scnr.nextLine();
        if (continueOrNot.equals("QUIT")) {
            System.exit(1);
        }
        else {
            char more = continueOrNot.charAt(0);

            if ((more != 'Y' && more != 'y' && more != 'N' && more != 'n') || continueOrNot.length() != 1) {
                System.out.print("    Invalid: Try again: ");
                return continueOrNot(scnr);
            } else
            { return more; }
        }
        return 0; // This will never be reached because of the System.exit(1). This is to avoid compile-time error.
    }
}