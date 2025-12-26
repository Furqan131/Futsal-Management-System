public class FutsalGround {
    String id;
    String name;
    String location;
    int price;
    boolean isBooked;
    String startTime;
    String endTime;
    
    // Updated to include Email as requested for the Admin Dashboard
    String customerName;
    String customerPhone;
    String customerEmail;

    public FutsalGround(String id, String name, String location, int price) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.price = price;
        this.isBooked = false;
        this.startTime = null;
        this.endTime = null;
        this.customerName = null;
        this.customerPhone = null;
        this.customerEmail = null;
    }

    @Override
    public String toString() {
        return "ID: " + id + 
               " | Name: " + name + 
               " | Loc: " + location + 
               " | Price: Rs " + price + 
               " | Booked: " + (isBooked ? "YES" : "NO") + 
               (isBooked ? (" | Time: " + startTime + " - " + endTime + " | Customer: " + customerName + " (" + customerPhone + ")") : "");
    }
}