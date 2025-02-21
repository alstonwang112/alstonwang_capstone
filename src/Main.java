import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to Alston Wang's task management application.");
        char keepGoing;
        
        do {
            System.out.println("What would you like to do?");
            printOptions();
            System.out.print("Enter a number 1 - 8: ");

            Scanner scnr = new Scanner(System.in);
            int option = 0;

            do {
                try {
                    option = scnr.nextInt();
                    if (option < 0 || option > 8) {
                        System.out.print("Invalid. Try again: ");
                    }
                } catch (InputMismatchException e) {
                    System.out.print("Invalid. Try again: ");
                    option = scnr.nextInt();
                }
            } while (option < 0 || option > 8);

            switch (option) {
                case 1: {
                    System.out.print("Enter the task you would like to create: ");
                    String taskToCreate = scnr.nextLine();

                    break;
                }
                case 2:
                    System.out.print("Enter the name of the task you would like to edit: ");
                    String taskToEdit = scnr.nextLine();

                    break;
                case 3:
                    System.out.print("Enter the name of the task you would like to open: ");
                    String taskToOpen = scnr.nextLine();
                    break;
                case 4:
                    System.out.print("Enter the name of the list you would like to create: ");
                    String listToCreate = scnr.nextLine();
                    break;
                case 5:
                    System.out.print("Enter the name of the list you would like to edit: ");
                    String listToEdit = scnr.nextLine();
                    break;
                case 6:
                    System.out.print("Enter the name of the list you would like to open: ");
                    String listToOpen = scnr.nextLine();
                    break;
                case 7:
                    System.out.print("Enter the name of the file you would like to open: ");
                    FileInputStream toBeRead = null;
                    String readName = scnr.nextLine();
                    toBeRead = findReadableFile(toBeRead, readName, scnr);
                    readFile(toBeRead);
                    break;
                case 8:
                    System.out.print("Enter the name of the file you would like to write to: ");
                    String writtenName = scnr.nextLine();
                    FileOutputStream toBeWrittenTo = findWritableFile(writtenName, scnr);
                    PrintWriter fileWriter = new PrintWriter(toBeWrittenTo);

                    System.out.print("Add the text you would like to write to your file: ");
                    String text = scnr.nextLine();
                    fileWriter.println(text);
                    System.out.print("Write more? (Y/N)");
                    String moreString;
                    char moreChar;
                    do {
                        moreString = scnr.nextLine();
                        moreChar = moreString.charAt(0);
                    } while ((moreString.length() != 1) || ((moreChar != 'Y') && (moreChar != 'y') && (moreChar != 'N') && (moreChar != 'n')));
                    writeFile(fileWriter, text, moreChar, scnr);

                    fileWriter.flush();
                    fileWriter.close();
                    break;
            }

            do {
                System.out.print("Would you like to do anything else? (Y/N) ");
                keepGoing = scnr.next().charAt(0);
            } while (keepGoing != 'N' && keepGoing != 'n' && keepGoing != 'Y' && keepGoing != 'y');
        } while (keepGoing == 'Y' || keepGoing == 'y');

        System.out.println("Task manager closed.");
    }

    public static void printOptions() {
        System.out.println("    (1) Create task.");
        System.out.println("    (2) Edit task.");
        System.out.println("    (3) Open task.");
        System.out.println("    (4) Create list.");
        System.out.println("    (5) Edit list.");
        System.out.println("    (6) Open list.");
        System.out.println("    (7) Open the text file.");
        System.out.println("    (8) Write to the text file.");
    }

    public static FileInputStream findReadableFile(FileInputStream toBeRead, String readName, Scanner scnr) {
        try {
            toBeRead = new FileInputStream(readName);
            return toBeRead;
        } catch (FileNotFoundException e) {
            System.out.print("File does not exist. Try again or press 0 to quit: ");
            readName = scnr.nextLine();
            return findReadableFile(toBeRead, readName, scnr);
        }
    }

    public static void readFile(FileInputStream toBeRead) {
        Scanner fileReader = new Scanner(toBeRead);
        while (fileReader.hasNextLine()) {
            String data = fileReader.nextLine();
            System.out.println(data);
        }
        fileReader.close();
    }

    public static FileOutputStream findWritableFile(String writtenName, Scanner scnr) {
        try {
            FileOutputStream toBeWrittenTo = new FileOutputStream(writtenName);
            return toBeWrittenTo;
        } catch (FileNotFoundException e) {
            System.out.print("File does not exist. Try again or press 0 to quit: ");
            writtenName = scnr.nextLine();
            return findWritableFile(writtenName, scnr);
        }
    }

    public static void writeFile(PrintWriter fileWriter, String text, char moreChar, Scanner scnr) {
        if (moreChar == 'Y' || moreChar == 'y') {
            fileWriter.println(text);
            System.out.print("Write more? (Y/N)");
            String moreString;
            do {
                moreString = scnr.nextLine();
                moreChar = moreString.charAt(0);
            } while ((moreString.length() != 1) || ((moreChar != 'Y') && (moreChar != 'y') && (moreChar != 'N') && (moreChar != 'n')));
            writeFile(fileWriter, text, moreChar, scnr);
        }
    }
}