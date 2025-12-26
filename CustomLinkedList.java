public class CustomLinkedList {
    Node head;

    public CustomLinkedList() {
        this.head = null;
    }

    public void add(FutsalGround data) {
        Node newNode = new Node(data);
        if (head == null) {
            head = newNode;
        } else {
            newNode.next = head;
            head = newNode;
        }
    }

    public boolean remove(String id) {
        if (head == null) return false;

        if (head.data.id.equalsIgnoreCase(id)) {
            head = head.next;
            return true;
        }

        Node current = head;
        while (current.next != null) {
            if (current.next.data.id.equalsIgnoreCase(id)) {
                current.next = current.next.next;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public boolean display() {
        if (head == null) {
            System.out.println("NO BOOKED COURTS CURRENTLY.");
            return false;
        }
        System.out.println("--- BOOKED COURTS (LATEST FIRST) ---");
        Node current = head;
        while (current != null) {
            System.out.println(current.data);
            current = current.next;
        }
        return true;
    }
}