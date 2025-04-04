import java.time.LocalDateTime;
import java.util.ArrayList;

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
}