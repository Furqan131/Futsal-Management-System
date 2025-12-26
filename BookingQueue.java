// NEW DSA CONCEPT: Queue (First-In-First-Out)
// Used for managing pending booking requests
public class BookingQueue {
    private BookingRequest front;
    private BookingRequest rear;
    private int size;

    public BookingQueue() {
        this.front = null;
        this.rear = null;
        this.size = 0;
    }

    // Add a request to the end of the line
    public void enqueue(FutsalGround court, User user, String time) {
        BookingRequest newNode = new BookingRequest(court, user, time);
        if (rear == null) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    // Remove the request from the front (Admin approves/rejects)
    public BookingRequest dequeue() {
        if (front == null) return null;

        BookingRequest temp = front;
        front = front.next;

        if (front == null) {
            rear = null;
        }
        size--;
        return temp;
    }

    // See who is next without removing
    public BookingRequest peek() {
        return front;
    }

    public boolean isEmpty() {
        return front == null;
    }

    public int getSize() {
        return size;
    }
}