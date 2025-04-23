public class Queue {
    Node head;

    public Queue(Node head) {
        this.head = head;
    }

    public void add(String s) {
        Node curr = head;
        while (curr.next != null) {
            curr = curr.next;
        }
        curr.next = new Node(s, null);
    }

    public String remove() {
        String s = head.comment;
        head = head.next;
        return s;
    }

    public void peek() {
        System.out.println(head.comment);
    }

    public boolean isEmpty() {
        return head == null;
    }
}