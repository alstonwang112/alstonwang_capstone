import java.util.ArrayList;

public class Generic {
    public static <Generic> void check(ArrayList<Generic> arr) {
        int pending = 0;
        int completed = 0;

        for (Generic G : arr) {
            String status;

            if (G instanceof Task) { status = ((Task) G).getTaskCompletion(); }
            else {
                List.checkTasksWithinList(((List) G));
                status = ((List) G).getListCompletion();
            }

            if (status.equals("P")) { pending++; }
            else { completed++; }
        }

        Generic G = arr.getFirst();

        if (G instanceof Task) {
            System.out.println(" " + pending + " tasks pending.");
            System.out.println("    " + completed + " tasks completed.");
        } else {
            System.out.println(pending + " lists pending.");
            System.out.println("    " + completed + " lists completed.");
        }
    }
}