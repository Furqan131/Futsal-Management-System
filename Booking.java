public class Booking {
    private String courtId;
    private String courtName;
    private String customerEmail;
    private String customerName;
    private String date;      // Format: dd-MM-yyyy
    private String timeSlot;  // Format: HH:mm - HH:mm

    public Booking(String courtId, String courtName, String customerEmail, String customerName, String date, String timeSlot) {
        this.courtId = courtId;
        this.courtName = courtName;
        this.customerEmail = customerEmail;
        this.customerName = customerName;
        this.date = date;
        this.timeSlot = timeSlot;
    }

    // Getters
    public String getCourtId() { return courtId; }
    public String getCourtName() { return courtName; }
    public String getCustomerEmail() { return customerEmail; }
    public String getCustomerName() { return customerName; }
    public String getDate() { return date; }
    public String getTimeSlot() { return timeSlot; }

    @Override
    public String toString() {
        return courtId + "," + courtName + "," + customerEmail + "," + customerName + "," + date + "," + timeSlot;
    }
    
    // Helper to check overlap
    public boolean matches(String id, String d, String t) {
        return this.courtId.equalsIgnoreCase(id) && this.date.equals(d) && this.timeSlot.equals(t);
    }
}