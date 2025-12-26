import java.io.*;
import java.util.ArrayList;

public class UserManager {
    private final String FILE_NAME = "users.txt";
    private ArrayList<User> users;

    public UserManager() {
        users = new ArrayList<>();
        loadUsers();
    }

    public boolean registerUser(String name, String email, String password) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return false; 
            }
        }
        User newUser = new User(name, email, password);
        users.add(newUser);
        saveUsers();
        return true;
    }

    public User loginUser(String email, String password) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }
    
    // NEW METHOD: Get all users for Admin View
    public ArrayList<User> getAllUsers() {
        return users;
    }

    private void saveUsers() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (User u : users) {
                bw.write(u.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    private void loadUsers() {
        File f = new File(FILE_NAME);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    users.add(new User(parts[0], parts[1], parts[2]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }
}