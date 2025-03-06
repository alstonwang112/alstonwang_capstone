import java.util.ArrayList;

public class CompareTasks {
    public int compare(Task T1, Task T2, int sortBy) {
        int compareName = T1.getTaskName().compareTo(T2.getTaskName());
        int compareDescription = T1.getTaskDescription().compareTo(T2.getTaskDescription());
        int compareCreationDate = T1.getTaskCreated().compareTo(T2.getTaskCreated());
        int compareDueDate = T1.getTaskDeadline().compareTo(T2.getTaskDeadline());

        switch (sortBy) {
            case 1: {
                if (compareName != 0) {
                    return compareName;
                } else {
                    if (compareDescription != 0) {
                        return compareDescription;
                    } else {
                        if (compareCreationDate != 0) {
                            return compareCreationDate;
                        } else {
                            return compareDueDate;
                        }
                    }
                }
            }
            case 2: {
                if (compareDescription != 0) {
                    return compareDescription;
                } else {
                    if (compareName != 0) {
                        return compareName;
                    } else {
                        if (compareCreationDate != 0) {
                            return compareCreationDate;
                        } else {
                            return compareDueDate;
                        }
                    }
                }
            }
            case 3: {
                if (compareCreationDate != 0) {
                    return compareCreationDate;
                } else {
                    if (compareName != 0) {
                        return compareName;
                    } else {
                        if (compareDescription != 0) {
                            return compareDescription;
                        } else {
                            return compareDueDate;
                        }
                    }
                }
            }
            case 4: {
                if (compareDueDate != 0) {
                    return compareDueDate;
                } else {
                    if (compareName != 0) {
                        return compareName;
                    } else {
                        if (compareDescription != 0) {
                            return compareDescription;
                        } else {
                            return compareCreationDate;
                        }
                    }
                }
            }
        }
        return 0;
    }

    public ArrayList<Task> Sort(ArrayList<Task> taskCollection, int sortBy, int sortOrder) {
        int n = taskCollection.size();

        if (sortOrder == 1) {
            for (int i = 0; i < n - 1; i++) {
                for (int j = 0; j < n - 1 - i; j++) {
                    Task T1 = taskCollection.get(j);
                    Task T2 = taskCollection.get(j + 1);
                    if (compare(T1, T2, sortBy) > 0) { // Only swaps if the first element is greater than the second.
                        Swap(taskCollection, T1, T2, j);
                    }
                }
            }
        } else {
            for (int i = 0; i < n - 1; i++) {
                for (int j = 0; j < n - 1 - i; j++) {
                    Task T1 = taskCollection.get(j);
                    Task T2 = taskCollection.get(j + 1);
                    if (compare(T1, T2, sortBy) < 0) { // Only swaps if the first element is greater than the second.
                        Swap(taskCollection, T1, T2, j);
                    }
                }
            }
        }

        return taskCollection;
    }

    public void Swap(ArrayList<Task> taskCollection, Task T1, Task T2, int j) {
        taskCollection.set(j, T2);
        taskCollection.set(j + 1, T1);
    }
}