import java.awt.*;
import javax.swing.*;

public class LoginFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel rightPanel;
    private UserManager userManager;

    public LoginFrame() {
        userManager = new UserManager();
        
        setTitle("Futsal Manager - Access");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));

        // --- LEFT PANEL (Visual) ---
        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Diagonal Gradient
                GradientPaint gp = new GradientPaint(0, 0, StyleUtils.THEME_GREEN_DARK, getWidth(), getHeight(), StyleUtils.THEME_GREEN_LIGHT);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                // Abstract Shapes
                g2.setColor(new Color(255,255,255,20));
                g2.fillOval(-50, -50, 300, 300);
                g2.fillOval(getWidth()-200, getHeight()/2, 400, 400);
                g2.setColor(new Color(255,255,255,40));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(50, 50, getWidth()-100, getHeight()-100, 30, 30);
            }
        };
        leftPanel.setLayout(new GridBagLayout());
        
        JPanel brandBox = new JPanel();
        brandBox.setOpaque(false);
        brandBox.setLayout(new BoxLayout(brandBox, BoxLayout.Y_AXIS));
        
        JLabel logoText = new JLabel("FUTSAL PRO");
        logoText.setFont(new Font("Segoe UI", Font.BOLD, 48));
        logoText.setForeground(Color.WHITE);
        logoText.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel slogan = new JLabel("Elite Court Management System");
        slogan.setFont(StyleUtils.FONT_HEADER_MD);
        slogan.setForeground(StyleUtils.THEME_ACCENT_GOLD);
        slogan.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        brandBox.add(logoText);
        brandBox.add(Box.createVerticalStrut(10));
        brandBox.add(slogan);
        leftPanel.add(brandBox);

        // --- RIGHT PANEL (Interactive) ---
        cardLayout = new CardLayout();
        rightPanel = new JPanel(cardLayout);
        rightPanel.setBackground(Color.WHITE);
        
        rightPanel.add(createLogin(), "LOGIN");
        rightPanel.add(createSignup(), "SIGNUP");

        add(leftPanel);
        add(rightPanel);
        setVisible(true);
    }

    private JPanel createLogin() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(10, 40, 10, 40);
        g.gridx=0; g.gridy=0;

        JLabel head = new JLabel("Welcome Back");
        head.setFont(StyleUtils.FONT_HEADER_LG);
        head.setForeground(StyleUtils.THEME_GREEN_MAIN);
        p.add(head, g);
        
        g.gridy++;
        JLabel sub = new JLabel("Login to continue booking");
        sub.setFont(StyleUtils.FONT_BODY);
        sub.setForeground(Color.GRAY);
        p.add(sub, g);

        g.gridy++; g.insets = new Insets(30, 40, 5, 40);
        p.add(new JLabel("Username / Email"), g);
        
        g.gridy++; g.insets = new Insets(0, 40, 15, 40);
        JTextField userF = new JTextField();
        userF.setFont(StyleUtils.FONT_BODY);
        userF.setPreferredSize(new Dimension(300, 40));
        p.add(userF, g);

        g.gridy++; g.insets = new Insets(0, 40, 5, 40);
        p.add(new JLabel("Password"), g);
        
        g.gridy++; g.insets = new Insets(0, 40, 30, 40);
        JPasswordField passF = new JPasswordField();
        passF.setPreferredSize(new Dimension(300, 40));
        p.add(passF, g);

        g.gridy++;
        JButton loginBtn = StyleUtils.createModernButton("LOGIN");
        p.add(loginBtn, g);

        g.gridy++; g.insets = new Insets(15, 40, 10, 40);
        JButton toSign = StyleUtils.createOutlineButton("Create New Account");
        toSign.setBorder(null); // Flat look
        p.add(toSign, g);

        // Logic
        loginBtn.addActionListener(e -> {
            String u = userF.getText().trim();
            String pwd = new String(passF.getPassword());
            if(u.equals("furqan") && pwd.equals("furqan123")) {
                new AdminDashboard(); dispose();
            } else {
                User usr = userManager.loginUser(u, pwd);
                if(usr!=null) {
                    new CustomerDashboard(usr); dispose();
                } else {
                    StyleUtils.showNotification(this, "Invalid credentials", "Login Error", false);
                }
            }
        });
        toSign.addActionListener(e -> cardLayout.show(rightPanel, "SIGNUP"));

        return p;
    }

    private JPanel createSignup() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(5, 40, 5, 40);
        g.gridx=0; g.gridy=0;

        JLabel head = new JLabel("New Account");
        head.setFont(StyleUtils.FONT_HEADER_LG);
        head.setForeground(StyleUtils.THEME_GREEN_MAIN);
        p.add(head, g);

        g.gridy++; g.insets = new Insets(20, 40, 5, 40);
        p.add(new JLabel("Full Name"), g);
        g.gridy++; g.insets = new Insets(0, 40, 10, 40);
        JTextField nameF = new JTextField(); nameF.setPreferredSize(new Dimension(300, 35));
        p.add(nameF, g);

        g.gridy++; g.insets = new Insets(0, 40, 5, 40);
        p.add(new JLabel("Email"), g);
        g.gridy++; g.insets = new Insets(0, 40, 10, 40);
        JTextField emailF = new JTextField(); emailF.setPreferredSize(new Dimension(300, 35));
        p.add(emailF, g);

        g.gridy++; g.insets = new Insets(0, 40, 5, 40);
        p.add(new JLabel("Password"), g);
        g.gridy++; g.insets = new Insets(0, 40, 10, 40);
        JPasswordField passF = new JPasswordField(); passF.setPreferredSize(new Dimension(300, 35));
        p.add(passF, g);

        g.gridy++; g.insets = new Insets(0, 40, 5, 40);
        p.add(new JLabel("Confirm Password"), g);
        g.gridy++; g.insets = new Insets(0, 40, 20, 40);
        JPasswordField cPassF = new JPasswordField(); cPassF.setPreferredSize(new Dimension(300, 35));
        p.add(cPassF, g);

        g.gridy++;
        JButton signBtn = StyleUtils.createModernButton("REGISTER");
        p.add(signBtn, g);

        g.gridy++; g.insets = new Insets(10, 40, 10, 40);
        JButton toLog = StyleUtils.createOutlineButton("Back to Login");
        toLog.setBorder(null);
        p.add(toLog, g);

        signBtn.addActionListener(e -> {
            String n = nameF.getText(), em = emailF.getText();
            String pw = new String(passF.getPassword()), cpw = new String(cPassF.getPassword());
            if(n.isEmpty()||em.isEmpty()||pw.isEmpty()) {
                StyleUtils.showNotification(this, "All fields required", "Error", false); return;
            }
            if(!pw.equals(cpw)) {
                StyleUtils.showNotification(this, "Passwords mismatch", "Error", false); return;
            }
            if(userManager.registerUser(n, em, pw)) {
                StyleUtils.showNotification(this, "Registration Success!", "Welcome", true);
                cardLayout.show(rightPanel, "LOGIN");
            } else {
                StyleUtils.showNotification(this, "Email exists", "Error", false);
            }
        });
        toLog.addActionListener(e -> cardLayout.show(rightPanel, "LOGIN"));

        return p;
    }
}