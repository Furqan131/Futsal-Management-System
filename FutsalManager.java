import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FutsalManager {
    int size;
    FutsalGround[] array; 
    ArrayList<Booking> bookings; 
    public BookingQueue pendingRequests; 
    Algorithms algo;
    
    final String COURTS_FILE = "futsal_courts.csv";
    final String BOOKINGS_FILE = "confirmed_bookings.csv";
    final String REQUESTS_FILE = "requests.csv";

    public FutsalManager() {
        algo = new Algorithms();
        size = 0;
        array = new FutsalGround[0];
        bookings = new ArrayList<>();
        pendingRequests = new BookingQueue(); 
        
        loadCourts();
        loadBookings();
        loadRequests();
    }

    public void refreshData() {
        bookings.clear();
        loadBookings();
        pendingRequests = new BookingQueue();
        loadRequests();
    }

    // --- ANALYTICS ---
    public int calculateTotalRevenue() {
        int total = 0;
        for (Booking b : bookings) {
            int index = algo.binarySearchById(b.getCourtId(), array);
            if (index != -1) {
                total += array[index].price;
            }
        }
        return total;
    }

    public String getMostPopularCourt() {
        if (bookings.isEmpty()) return "N/A";
        HashMap<String, Integer> counts = new HashMap<>();
        for (Booking b : bookings) {
            counts.put(b.getCourtName(), counts.getOrDefault(b.getCourtName(), 0) + 1);
        }
        String popular = "";
        int max = -1;
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                popular = entry.getKey();
            }
        }
        return popular;
    }

    public ArrayList<Booking> searchBookings(String query) {
        ArrayList<Booking> result = new ArrayList<>();
        String q = query.toLowerCase().trim();
        for (Booking b : bookings) {
            if (b.getCustomerName().toLowerCase().contains(q) || 
                b.getCourtName().toLowerCase().contains(q) ||
                b.getDate().contains(q)) {
                result.add(b);
            }
        }
        return result;
    }

    // --- COURT MANAGEMENT ---
    public boolean addCourtGUI(String id, String name, String loc, int price) {
        for (int i = 0; i < size; i++) {
            if (array[i].id.equalsIgnoreCase(id)) return false; 
        }
        addToArray(new FutsalGround(id, name, loc, price));
        saveCourts();
        return true;
    }

    public boolean removeCourtGUI(String id) {
        int index = algo.binarySearchById(id, array);
        if (index == -1) return false;
        FutsalGround[] temp = new FutsalGround[size - 1];
        for (int i = 0, j = 0; i < size; i++) {
            if (i != index) temp[j++] = array[i];
        }
        array = temp;
        size--;
        saveCourts();
        return true;
    }
    
    private void addToArray(FutsalGround ground) {
        FutsalGround[] temp = new FutsalGround[size + 1];
        System.arraycopy(array, 0, temp, 0, size);
        temp[size] = ground;
        array = temp;
        size++;
    }

    // --- CRITICAL FIX: BOOKING AVAILABILITY ---
    // This method checks ONLY the specific slot. It ignores any global 'isBooked' flags.
    public boolean isSlotBooked(String courtId, String date, String timeSlot) {
        if(bookings == null) return false;
        
        String checkDate = date.trim();
        String checkTime = timeSlot.trim();
        
        for (Booking b : bookings) {
            // Compare ID, Date, and Time exactly
            if (b.getCourtId().equalsIgnoreCase(courtId) && 
                b.getDate().trim().equalsIgnoreCase(checkDate) && 
                b.getTimeSlot().trim().equalsIgnoreCase(checkTime)) {
                return true; 
            }
        }
        return false;
    }

    public void confirmBooking(String courtId, String userName, String userEmail, String date, String timeSlot) {
        int index = algo.binarySearchById(courtId, array);
        if (index != -1) {
            String courtName = array[index].name;
            // Create a new booking record for this specific slot
            Booking newBooking = new Booking(courtId, courtName, userEmail, userName, date, timeSlot);
            bookings.add(newBooking);
            saveBookings();
        }
    }
    
    public boolean cancelBooking(String courtId, String date, String timeSlot) {
        Booking toRemove = null;
        for(Booking b : bookings) {
             if (b.getCourtId().equalsIgnoreCase(courtId) && 
                 b.getDate().trim().equals(date.trim()) && 
                 b.getTimeSlot().trim().equals(timeSlot.trim())) {
                toRemove = b;
                break;
            }
        }
        if(toRemove != null) {
            bookings.remove(toRemove);
            saveBookings();
            return true;
        }
        return false;
    }
    
    // --- SORTING ---
    public void sortBookingsByCustomer() {
        int n = bookings.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (bookings.get(j).getCustomerName().compareToIgnoreCase(bookings.get(j + 1).getCustomerName()) > 0) {
                    Booking temp = bookings.get(j);
                    bookings.set(j, bookings.get(j + 1));
                    bookings.set(j + 1, temp);
                }
            }
        }
    }

    public void sortBookingsByCourt() {
        int n = bookings.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (bookings.get(j).getCourtName().compareToIgnoreCase(bookings.get(j + 1).getCourtName()) > 0) {
                    Booking temp = bookings.get(j);
                    bookings.set(j, bookings.get(j + 1));
                    bookings.set(j + 1, temp);
                }
            }
        }
    }
    
    public ArrayList<Booking> getBookingsForUser(String email) {
        ArrayList<Booking> userBookings = new ArrayList<>();
        for (Booking b : bookings) {
            if (b.getCustomerEmail().equalsIgnoreCase(email)) {
                userBookings.add(b);
            }
        }
        return userBookings;
    }

    public void addRequestToQueue(String courtId, User user, String fullDateTime) {
        int index = algo.binarySearchById(courtId, array);
        if (index != -1) {
            pendingRequests.enqueue(array[index], user, fullDateTime);
            saveRequests();
        }
    }

    public void sortByPrice() { algo.sortByPrice(array, size); saveCourts(); }
    public void sortByID() { algo.sortById(array, size); saveCourts(); }
    public void sortByName() { algo.sortByName(array, size); saveCourts(); }

    // --- FILE I/O ---
    private void saveCourts() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(COURTS_FILE))) {
            for (FutsalGround g : array) {
                if(g == null) continue;
                // Only saving core court info, NO booking status here
                bw.write(g.id + "," + g.name + "," + g.price + "," + g.location);
                bw.newLine();
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void loadCourts() {
        File f = new File(COURTS_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4) {
                    // Load court, always default to not booked. Booking status comes from 'bookings' list.
                    addToArray(new FutsalGround(data[0], data[1], data[3], Integer.parseInt(data[2])));
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void saveBookings() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(BOOKINGS_FILE))) {
            for (Booking b : bookings) {
                bw.write(b.toString());
                bw.newLine();
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void loadBookings() {
        File f = new File(BOOKINGS_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    bookings.add(new Booking(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]));
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void saveRequests() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(REQUESTS_FILE))) {
            BookingRequest current = pendingRequests.peek();
            while (current != null) {
                String line = current.court.id + "," + current.user.getName() + "," + current.user.getEmail() + "," + current.requestedTime;
                bw.write(line);
                bw.newLine();
                current = current.next;
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void loadRequests() {
        File f = new File(REQUESTS_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    int idx = algo.binarySearchById(parts[0], array);
                    if (idx != -1) {
                        User u = new User(parts[1], parts[2], "N/A");
                        pendingRequests.enqueue(array[idx], u, parts[3]);
                    }
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
    }
}