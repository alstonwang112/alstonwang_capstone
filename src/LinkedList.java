public class LinkedList {
    Node head;

    LinkedList(Node n) {
        head = n;
    }

    public boolean isEmpty() {
        return head == null;
    }
}