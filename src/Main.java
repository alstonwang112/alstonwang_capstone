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
        int taskNum; // Used to retrieve the task we want when editing or opening.
        int listNum; // Used to retrieve the list we want when editing or opening.
        
        do {
            System.out.println("What would you like to do?");
            printOptions();
            System.out.print("Enter a number (1 - 9): ");

            Scanner scnr = new Scanner(System.in);
            int option = checkNumberValidity(1, 10, scnr);

            switch (option) {
                case 1: {
                    System.out.print("(1) Enter the name of task you would like to create: ");
                    String taskName = scnr.nextLine();

                    System.out.print("    Enter a brief description of your task: ");
                    String taskDescription = scnr.nextLine();

                    System.out.print("    Enter a numeric deadline for your task (year, month, day, hour, minute): ");
                    String str = scnr.nextLine();
                    LocalDateTime taskDeadline = checkLocalDateValidity(str, scnr);

                    taskCollection.add(createTask(taskName, taskDescription, taskDeadline));
                    System.out.print("    Task created. ");
                    break;
                }
                case 2: {
                    System.out.print("(2) Enter the name of the task you would like to edit: ");
                    String editTask = scnr.nextLine();

                    taskNum = searchTask(editTask, taskCollection, scnr);
                    taskCollection.set(taskNum, editTask(taskCollection, taskCollection.get(taskNum), scnr));
                    break;
                }
                case 3: {
                    System.out.print("(3) Enter the name of the task you would like to open: ");
                    String openTask = scnr.nextLine();

                    taskNum = searchTask(openTask, taskCollection, scnr);
                    openTask(taskCollection.get(taskNum), scnr);
                    System.out.print("    Task opened. ");
                    break;
                }
                case 4: {
                    System.out.print("(4) Enter the name of the list you would like to create: ");
                    String listName = scnr.nextLine();

                    System.out.print("    Enter a brief description of your list: ");
                    String listDescription = scnr.nextLine();

                    ArrayList<Task> listOfTasks = new ArrayList<>();

                    System.out.print("    Enter the name of the task you would like to add to this list: ");
                    String taskName = scnr.nextLine();
                    // Check if this task exists.
                    // If not, create a new task.
                    // Add task to the ArrayList.
                    // Use the ArrayList to create a List object.

                    break;
                }
                case 5: {
                    System.out.print("(5) Enter the name of the list you would like to edit: ");
                    String editList = scnr.nextLine();
                    break;
                }
                case 6: {
                    System.out.print("(6) Enter the name of the list you would like to open: ");
                    String openList = scnr.nextLine();
                    break;
                }
                case 7: {
                    System.out.print("(7) Enter the name of the file you would like to open: ");

                    boolean readError; // Used to know when to break out of the do-while loop.
                    FileInputStream readMe = null;

                    do {
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
                    break;
                }
                case 9: {
                    System.out.println("(9): ");
                    int sortItem = sortOptions(scnr);
                    int sortBy = sortBy(scnr, sortItem);
                    int sortOrder = sortOrder(scnr);

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
                            String listToBeSorted = scnr.nextLine();

                            listNum = searchList(listToBeSorted, listCollection, scnr);
                            List temp = listCollection.get(listNum);

                            ArrayList<Task> replacementListOfTasks = taskComparator.Sort(temp.getListOfTasks(), sortBy, sortOrder);
                            List replacement = new List(temp.getListName(), temp.getListDescription(), temp.getListCreated(), replacementListOfTasks);

                            listCollection.set(listNum, replacement);

                            System.out.print("    List sorted. ");
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
            }

            System.out.println("Continue with another task? (Y/N): ");
            continueOrNot = continueOrNot(scnr);
        } while (continueOrNot == 'Y' || continueOrNot == 'y');

        System.out.println("Application closed.");
        System.exit(1);
    }

    public static void printOptions() {
        System.out.println("    (1) Create task.");
        System.out.println("    (2) Edit task.");
        System.out.println("    (3) Open task.");
        System.out.println("    (4) Create list.");
        System.out.println("    (5) Edit list.");
        System.out.println("    (6) Open list.");
        System.out.println("    (7) Open text file.");
        System.out.println("    (8) Write to text file.");
        System.out.println("    (9) Sort.");
    } // Prints a list of possible actions.

    public static int checkNumberValidity(int lowerBound, int upperBound, Scanner scnr) {
        try {
            int num = scnr.nextInt();
            if (num < lowerBound || num > upperBound) {
                System.out.print("    Invalid. Try again: ");
                return checkNumberValidity(lowerBound, upperBound, scnr);
            } else {
                return num;
            }
        } catch (InputMismatchException e) {
            System.out.print("    Invalid. Try again: ");
            return checkNumberValidity(lowerBound, upperBound, scnr);
        }
    } // Method repeats until valid input is provided.

    public static LocalDateTime checkLocalDateValidity(String str, Scanner scnr) {
        String[] date = str.split(",");

        int year = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]);
        int day = Integer.parseInt(date[2]);
        int hour = Integer.parseInt(date[3]);
        int minute = Integer.parseInt(date[4]);

        try {
            return LocalDateTime.of(year, month, day, hour, minute);
        } catch (DateTimeException e) {
            System.out.print("    Invalid. Try again: ");
            str = scnr.nextLine();
            return checkLocalDateValidity(str, scnr);
        }
    } // Checks to see if the date and time provided is valid.

    public static Task createTask(String taskName, String taskDescription, LocalDateTime taskDeadline) {
        LocalDateTime taskCreated = LocalDateTime.now();
        return new Task(taskName, taskDescription, taskCreated, taskDeadline);
    } // Creates a new task using an explicit constructor.

    public static int searchTask(String taskName, ArrayList<Task> taskCollection, Scanner scnr) {
        int size = taskCollection.size();

        for (int i = 0; i < size; i++) {
            if (taskName.equals(taskCollection.get(i).getTaskName())) {
                return i;
            }
        }

        System.out.print("    Invalid. Try again: ");
        taskName = scnr.nextLine();

        return searchTask(taskName, taskCollection, scnr);
    } // Used to search for the task the user desires.

    public static Task editTask(ArrayList<Task> taskCollection, Task T, Scanner scnr) {
        System.out.println("    What would you like to edit?");
        System.out.println("        (1) Edit task name.");
        System.out.println("        (2) Edit task description.");
        System.out.println("        (3) Edit task deadline.");
        System.out.print("    Enter a number (1 - 3): ");

        int action = checkNumberValidity(1, 3, scnr);

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
                LocalDateTime taskDeadline = checkLocalDateValidity(str, scnr);
                T.setTaskDeadline(taskDeadline);
                break;
            }
        }

        System.out.print("    Task edited. Continue editing? (Y/N): ");
        if (continueOrNot(scnr) == 'Y' || continueOrNot(scnr) == 'y') {
            return editTask(taskCollection, T, scnr); // Recursive call if the user wants to keep editing.
        } else {
            return T;
        }
    } // Used to make edits to a task.

    public static void openTask(Task T, Scanner scnr) {
        System.out.println("Task Name: " + T.getTaskName());
        System.out.println("Task Description: " + T.getTaskDescription());
        System.out.println("Task Created On: " + T.getTaskCreated());
        System.out.println("Task Deadline: " + T.getTaskDeadline());
    } // Prints out details of a task.

    /*
    public static ArrayList<Task> addTaskToList() {

    }
    */

    public static int searchList(String listName, ArrayList<List> listCollection, Scanner scnr) {
        int size = listCollection.size();

        for (int i = 0; i < size; i++) {
            if (listName.equals(listCollection.get(i).getListName())) {
                return i;
            }
        }

        System.out.print("    Invalid. Try again: ");
        listName = scnr.nextLine();

        return searchList(listName, listCollection, scnr);
    } // Used to search for the list the user desires.

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
        String text = scnr.nextLine();
        fileWriter.println(text); // Writes to the file provided.

        System.out.print("    File written to. Continue writing? (Y/N): ");
        if (continueOrNot(scnr) == 'Y' || continueOrNot(scnr) == 'y') {
            writeFile(fileWriter, writeMe, scnr); // Recursive call if the user wants to keep writing.
        } else {
            fileWriter.flush();
            fileWriter.close();
        }
    } // Used to write to a file.

    public static int sortOptions(Scanner scnr) {
        System.out.println("What would you like to sort?");
        System.out.println("        (1) Sort all tasks.");
        System.out.println("        (2) Sort one list.");
        System.out.println("        (3) Sort all lists.");
        System.out.print("    Enter a number (1 - 3): ");

        return checkNumberValidity(1, 3, scnr);
    } // Tells the program what the user wants to sort.

    public static int sortBy(Scanner scnr, int sortItem) {
        System.out.println("    What would you like to sort by?");
        System.out.println("        (1) Sort by name.");
        System.out.println("        (2) Sort by description.");
        System.out.println("        (3) Sort by creation date.");

        if (sortItem == 1) {
            System.out.println("        (4) Sort by due date.");
            System.out.print("    Enter a number (1 - 4): ");
            return checkNumberValidity(1, 4, scnr);
        } else {
            System.out.print("    Enter a number (1 - 3): ");
            return checkNumberValidity(1, 3, scnr);
        }
    } // Tells the program what the user wants to sort by.

    public static int sortOrder(Scanner scnr) {
        System.out.println("    In what order?");
        System.out.println("        (1) Ascending.");
        System.out.println("        (2) Descending.");
        System.out.print("    Enter a number (1 - 2): ");

        return checkNumberValidity(1, 2, scnr);
    } // Tells the program what order the user wants to sort by.

    public static char continueOrNot(Scanner scnr) {
        String continueOrNot = scnr.nextLine();
        char more = continueOrNot.charAt(0);

        if ((more != 'Y' && more != 'y' && more != 'N' && more != 'n') || continueOrNot.length() != 1) {
            System.out.print("    Invalid: Try again: ");
            return continueOrNot(scnr);
        } else {
            return more;
        }
    } // Method to check if the user wants to continue a certain action.
}