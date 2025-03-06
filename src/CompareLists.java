import java.util.ArrayList;

public class CompareLists {
    public int compare(List L1, List L2, int sortBy) {
        int compareName = L1.getListName().compareTo(L2.getListName());
        int compareDescription = L1.getListDescription().compareTo(L2.getListDescription());
        int compareCreationDate = L1.getListCreated().compareTo(L2.getListCreated());

        switch (sortBy) {
            case 1: {
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
            case 2: {
                if (compareDescription != 0) {
                    return compareDescription;
                } else {
                    if (compareName != 0) {
                        return compareName;
                    } else {
                        return compareCreationDate;
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
                        return compareDescription;
                    }
                }
            }
        }
        return 0;
    }

    public ArrayList<List> Sort(ArrayList<List> listCollection, int sortBy, int sortOrder) {
        int n = listCollection.size();

        if (sortOrder == 1) {
            for (int i = 0; i < n - 1; i++) {
                for (int j = 0; j < n - 1 - i; j++) {
                    List L1 = listCollection.get(j);
                    List L2 = listCollection.get(j + 1);
                    if (compare(L1, L2, sortBy) > 0) { // Only swaps if the first element is greater than the second.
                        Swap(listCollection, L1, L2, j);
                    }
                }
            }
        } else {
            for (int i = 0; i < n - 1; i++) {
                for (int j = 0; j < n - 1 - i; j++) {
                    List L1 = listCollection.get(j);
                    List L2 = listCollection.get(j + 1);
                    if (compare(L1, L2, sortBy) < 0) { // Only swaps if the first element is greater than the second.
                        Swap(listCollection, L1, L2, j);
                    }
                }
            }
        }

        return listCollection;
    }

    public void Swap(ArrayList<List> listCollection, List L1, List L2, int i) {
        listCollection.set(i, L2);
        listCollection.set(i + 1, L1);
    }
}