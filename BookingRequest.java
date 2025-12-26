// This is the Data Node for our Queue
public class BookingRequest {
    FutsalGround court;
    User user;
    String requestedTime;
    BookingRequest next; // Pointer for the Queue LinkedList

    public BookingRequest(FutsalGround court, User user, String requestedTime) {
        this.court = court;
        this.user = user;
        this.requestedTime = requestedTime;
        this.next = null;
    }
}