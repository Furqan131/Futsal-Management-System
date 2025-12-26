import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class CustomerDashboard extends JFrame {
    private User currentUser;
    private FutsalManager manager;
    
    // UI State
    private String selectedDate;
    private String selectedSlot;
    
    private JPanel gridPanel;
    private JPanel historyContainer;
    private JPanel timeSlotPanel;
    private JPanel dateScrollPanel;
    
    private CardLayout tabLayout;
    private JPanel contentPanel;
    private JButton btnFindCourt;
    private JButton btnMyBookings;
    
    // Sorting
    private JComboBox<String> sortBox;

    public CustomerDashboard(User user) {
        this.currentUser = user;
        this.manager = new FutsalManager();
        
        // Defaults
        this.selectedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        this.selectedSlot = null;
        
        setTitle("Futsal Management System - " + user.getName());
        setSize(1350, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- HEADER ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(StyleUtils.THEME_GREEN_MAIN);
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(new EmptyBorder(0, 30, 0, 30));
        
        JLabel brand = new JLabel("FUTSAL PRO");
        brand.setFont(new Font("Segoe UI", Font.BOLD, 24));
        brand.setForeground(Color.WHITE);
        
        // Custom Navigation Buttons
        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        navBar.setOpaque(false);
        
        btnFindCourt = createHeaderNavButton("Find a Court", true); 
        btnMyBookings = createHeaderNavButton("My Bookings", false);
        
        navBar.add(btnFindCourt);
        navBar.add(btnMyBookings);
        
        JPanel userArea = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 20));
        userArea.setOpaque(false);
        JLabel uName = new JLabel("Hello, " + user.getName());
        uName.setForeground(Color.WHITE);
        uName.setFont(StyleUtils.FONT_BODY_BOLD);
        
        JButton logout = StyleUtils.createOutlineButton("Sign Out");
        logout.setForeground(Color.WHITE);
        logout.setBorder(new LineBorder(Color.WHITE, 1));
        logout.setBackground(StyleUtils.THEME_GREEN_MAIN);
        logout.setPreferredSize(new Dimension(100, 35));
        
        userArea.add(uName);
        userArea.add(logout);
        
        header.add(brand, BorderLayout.WEST);
        header.add(navBar, BorderLayout.CENTER);
        header.add(userArea, BorderLayout.EAST);
        
        add(header, BorderLayout.NORTH);

        // --- MAIN CONTENT ---
        tabLayout = new CardLayout();
        contentPanel = new JPanel(tabLayout);
        contentPanel.setBackground(StyleUtils.BACKGROUND_MAIN);
        
        contentPanel.add(createBookingTab(), "BOOKING");
        contentPanel.add(createHistoryTab(), "HISTORY");
        
        add(contentPanel, BorderLayout.CENTER);
        
        // --- EVENT LISTENERS ---
        btnFindCourt.addActionListener(e -> {
            switchTab("BOOKING");
            setActiveButton(btnFindCourt);
        });
        
        btnMyBookings.addActionListener(e -> {
            switchTab("HISTORY");
            setActiveButton(btnMyBookings);
            refreshHistory();
        });
        
        logout.addActionListener(e -> {
            if(StyleUtils.showConfirm(this, "Sign out now?", "Logout")) {
                new LoginFrame(); dispose();
            }
        });

        setVisible(true);
    }
    
    private void switchTab(String name) {
        tabLayout.show(contentPanel, name);
    }
    
    private void setActiveButton(JButton active) {
        styleHeaderNavButton(btnFindCourt, false);
        styleHeaderNavButton(btnMyBookings, false);
        styleHeaderNavButton(active, true);
        btnFindCourt.repaint();
        btnMyBookings.repaint();
    }
    
    private JButton createHeaderNavButton(String text, boolean isActive) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean active = getForeground().equals(StyleUtils.THEME_GREEN_MAIN);
                if (active) g2.setColor(Color.WHITE);
                else if (getModel().isRollover()) g2.setColor(new Color(255, 255, 255, 50));
                else g2.setColor(new Color(0,0,0,0)); 
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        styleHeaderNavButton(btn, isActive);
        return btn;
    }
    
    private void styleHeaderNavButton(JButton btn, boolean isActive) {
        btn.setFont(StyleUtils.FONT_BODY_BOLD);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 40));
        if (isActive) btn.setForeground(StyleUtils.THEME_GREEN_MAIN);
        else btn.setForeground(Color.WHITE); 
    }
    
    private JPanel createBookingTab() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(StyleUtils.BACKGROUND_MAIN);
        
        // --- LEFT SIDEBAR (FILTERS) ---
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setPreferredSize(new Dimension(350, 0)); 
        filterPanel.setBorder(BorderFactory.createMatteBorder(0,0,0,1, new Color(220,220,220)));
        
        // 1. Search Bar & Sort
        JPanel searchBox = new JPanel(new BorderLayout());
        searchBox.setBackground(Color.WHITE);
        searchBox.setBorder(new EmptyBorder(25, 20, 15, 20));
        searchBox.setMaximumSize(new Dimension(350, 140)); // Increased height
        searchBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel sLbl = new JLabel("SEARCH & SORT");
        sLbl.setFont(StyleUtils.FONT_BODY_BOLD);
        sLbl.setForeground(StyleUtils.THEME_GREEN_DARK);
        
        JTextField searchField = new JTextField();
        searchField.setFont(StyleUtils.FONT_BODY);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(StyleUtils.THEME_GREEN_MAIN, 1, true),
            new EmptyBorder(8, 10, 8, 10)
        ));
        
        // Sort Combo
        String[] sortOptions = {"Sort by: Default (ID)", "Price: Low to High", "Name: A-Z"};
        sortBox = new JComboBox<>(sortOptions);
        styleCombo(sortBox);
        sortBox.addActionListener(e -> {
            int idx = sortBox.getSelectedIndex();
            if (idx == 0) manager.sortByID();
            else if (idx == 1) manager.sortByPrice();
            else if (idx == 2) manager.sortByName();
            refreshGrid(searchField.getText());
        });
        
        JPanel inputPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.add(searchField);
        inputPanel.add(sortBox);
        
        searchBox.add(sLbl, BorderLayout.NORTH);
        searchBox.add(Box.createVerticalStrut(8), BorderLayout.CENTER);
        searchBox.add(inputPanel, BorderLayout.SOUTH);
        filterPanel.add(searchBox);
        
        // 2. Date Picker
        JPanel dateContainer = new JPanel(new BorderLayout());
        dateContainer.setBackground(Color.WHITE);
        dateContainer.setBorder(new EmptyBorder(10, 20, 10, 20));
        dateContainer.setMaximumSize(new Dimension(350, 160)); 
        dateContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel dLbl = new JLabel("SELECT DATE");
        dLbl.setFont(StyleUtils.FONT_BODY_BOLD);
        dLbl.setForeground(StyleUtils.THEME_GREEN_DARK);
        dateContainer.add(dLbl, BorderLayout.NORTH);
        
        dateScrollPanel = new JPanel(new GridLayout(1, 0, 5, 5));
        dateScrollPanel.setBackground(Color.WHITE);
        
        JScrollPane ds = new JScrollPane(dateScrollPanel);
        ds.setBorder(null);
        ds.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        ds.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        ds.setPreferredSize(new Dimension(300, 85)); 
        
        ds.addMouseWheelListener(e -> {
            JScrollBar bar = ds.getHorizontalScrollBar();
            if (bar != null) bar.setValue(bar.getValue() + (e.getWheelRotation() * 15));
        });
        
        dateContainer.add(ds, BorderLayout.CENTER);
        filterPanel.add(dateContainer);

        filterPanel.add(Box.createVerticalStrut(50)); 

        // 3. Time Slot Grid
        JPanel slotContainer = new JPanel();
        slotContainer.setLayout(new BoxLayout(slotContainer, BoxLayout.Y_AXIS));
        slotContainer.setBackground(Color.WHITE);
        slotContainer.setBorder(new EmptyBorder(10, 20, 20, 20));
        slotContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel tLbl = new JLabel("SELECT TIME SLOT (24H)");
        tLbl.setFont(StyleUtils.FONT_BODY_BOLD);
        tLbl.setForeground(StyleUtils.THEME_GREEN_DARK);
        tLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        slotContainer.add(tLbl);
        
        slotContainer.add(Box.createVerticalStrut(10));
        
        timeSlotPanel = new JPanel(new GridLayout(0, 2, 8, 8)); 
        timeSlotPanel.setBackground(Color.WHITE);
        timeSlotPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JScrollPane slotScroll = new JScrollPane(timeSlotPanel);
        slotScroll.setBorder(null);
        slotScroll.getVerticalScrollBar().setUnitIncrement(16);
        slotScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        slotScroll.getVerticalScrollBar().setUnitIncrement(16);

        slotScroll.setPreferredSize(new Dimension(300, 300));
        slotScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        slotContainer.add(slotScroll);
        filterPanel.add(slotContainer);
        
        p.add(filterPanel, BorderLayout.WEST);
        
        // --- RIGHT SIDE (GRID) ---
        gridPanel = new JPanel(new GridLayout(0, 3, 20, 20)); 
        gridPanel.setBackground(StyleUtils.BACKGROUND_MAIN);
        gridPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        JScrollPane scroll = new JScrollPane(gridPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        p.add(scroll, BorderLayout.CENTER);
        
        // --- INITIALIZE ---
        generateDateButtons();
        generateTimeButtons();

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { refreshGrid(searchField.getText()); }
            public void removeUpdate(DocumentEvent e) { refreshGrid(searchField.getText()); }
            public void changedUpdate(DocumentEvent e) { refreshGrid(searchField.getText()); }
        });

        refreshGrid("");
        return p;
    }
    
    private void generateDateButtons() {
        dateScrollPanel.removeAll();
        ButtonGroup dateGroup = new ButtonGroup();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate today = LocalDate.now();
        
        for(int i=0; i<14; i++) {
            LocalDate d = today.plusDays(i);
            String dStr = d.format(fmt);
            String dayName = d.getDayOfWeek().name().substring(0,3);
            String dayNum = String.valueOf(d.getDayOfMonth());
            
            JToggleButton btn = new JToggleButton("<html><center>" + dayName + "<br><b>" + dayNum + "</b></center></html>") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    if (isSelected()) {
                        g2.setColor(StyleUtils.THEME_GREEN_MAIN);
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                    } else {
                        g2.setColor(Color.WHITE);
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                        g2.setColor(Color.LIGHT_GRAY);
                        g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
                    }
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            
            btn.setFont(StyleUtils.FONT_SMALL);
            btn.setPreferredSize(new Dimension(60, 50));
            btn.setFocusPainted(false);
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
            
            btn.addChangeListener(e -> {
                if(btn.isSelected()) btn.setForeground(Color.WHITE);
                else btn.setForeground(Color.BLACK);
            });
            
            if(i==0) btn.setSelected(true);
            
            btn.addActionListener(e -> {
                selectedDate = dStr;
                refreshGrid(""); 
            });
            dateGroup.add(btn);
            dateScrollPanel.add(btn);
        }
        dateScrollPanel.revalidate(); 
        dateScrollPanel.repaint();
    }
    
    private void generateTimeButtons() {
        timeSlotPanel.removeAll();
        ButtonGroup slotGroup = new ButtonGroup();
        String[] slots = {
            "12:00 AM - 01:00 AM", "01:00 AM - 02:00 AM", "02:00 AM - 03:00 AM", "03:00 AM - 04:00 AM",
            "04:00 AM - 05:00 AM", "05:00 AM - 06:00 AM", "06:00 AM - 07:00 AM", "07:00 AM - 08:00 AM",
            "08:00 AM - 09:00 AM", "09:00 AM - 10:00 AM", "10:00 AM - 11:00 AM", "11:00 AM - 12:00 PM",
            "12:00 PM - 01:00 PM", "01:00 PM - 02:00 PM", "02:00 PM - 03:00 PM", "03:00 PM - 04:00 PM",
            "04:00 PM - 05:00 PM", "05:00 PM - 06:00 PM", "06:00 PM - 07:00 PM", "07:00 PM - 08:00 PM",
            "08:00 PM - 09:00 PM", "09:00 PM - 10:00 PM", "10:00 PM - 11:00 PM", "11:00 PM - 12:00 AM"
        };
        
        for (String slot : slots) {
            String simpleLabel = slot.split("-")[0].trim();
            JToggleButton btn = new JToggleButton(simpleLabel) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    if (isSelected()) g2.setColor(StyleUtils.THEME_ACCENT_GOLD);
                    else g2.setColor(new Color(245, 245, 245));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            
            btn.setFont(StyleUtils.FONT_SMALL);
            btn.setFocusPainted(false);
            btn.setPreferredSize(new Dimension(130, 40));
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
            
            btn.addChangeListener(e -> {
                if(btn.isSelected()) {
                    btn.setForeground(Color.BLACK);
                    btn.setFont(StyleUtils.FONT_BODY_BOLD);
                } else {
                    btn.setForeground(Color.BLACK);
                    btn.setFont(StyleUtils.FONT_SMALL);
                }
            });
            
            btn.addActionListener(e -> {
                selectedSlot = slot;
                refreshGrid("");
            });
            slotGroup.add(btn);
            timeSlotPanel.add(btn);
        }
        if(timeSlotPanel.getComponentCount() > 0) ((JToggleButton)timeSlotPanel.getComponent(0)).doClick();
    }
    
    private void refreshGrid(String query) {
        if(manager == null) return;
        manager.refreshData();
        gridPanel.removeAll();
        String q = query.toLowerCase();
        
        for(FutsalGround g : manager.array) {
            if(g==null) continue;
            if(!q.isEmpty() && !g.name.toLowerCase().contains(q) && !g.location.toLowerCase().contains(q)) continue;
            
            boolean booked = false;
            if(selectedSlot != null) booked = manager.isSlotBooked(g.id, selectedDate, selectedSlot);
            
            JPanel card = StyleUtils.createCardPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBorder(new EmptyBorder(15,15,15,15));
            
            JLabel nm = new JLabel(g.name); nm.setFont(StyleUtils.FONT_HEADER_MD); nm.setAlignmentX(0.5f);
            JLabel lc = new JLabel(g.location); lc.setFont(StyleUtils.FONT_BODY); lc.setForeground(Color.GRAY); lc.setAlignmentX(0.5f);
            
            JPanel priceTag = new JPanel();
            priceTag.setBackground(StyleUtils.THEME_GREEN_LIGHT);
            JLabel pr = new JLabel("Rs " + g.price + "/hr"); 
            pr.setFont(StyleUtils.FONT_BODY_BOLD); pr.setForeground(Color.WHITE);
            priceTag.add(pr); priceTag.setMaximumSize(new Dimension(100, 30));
            
            JButton act;
            if(booked) {
                act = new JButton("UNAVAILABLE");
                act.setBackground(new Color(255, 230, 230));
                act.setForeground(StyleUtils.RED_ERROR);
                act.setFont(StyleUtils.FONT_BODY_BOLD);
                act.setBorder(new EmptyBorder(10, 20, 10, 20));
                act.setEnabled(false);
            } else {
                act = StyleUtils.createModernButton("Request Booking");
                act.addActionListener(e -> {
                    if(selectedSlot == null) { StyleUtils.showNotification(this, "Select time first", "Info", false); return; }
                    String reqStr = selectedDate + " | " + selectedSlot;
                    String msg = "<html>Confirm Request for:<br><b>" + g.name + "</b><br>On: " + selectedDate + "<br>At: " + selectedSlot + "</html>";
                    if(StyleUtils.showConfirm(this, msg, "Confirm Request")) {
                        manager.addRequestToQueue(g.id, currentUser, reqStr);
                        StyleUtils.showNotification(this, "Request Sent!", "Success", true);
                    }
                });
            }
            act.setAlignmentX(0.5f);
            
            card.add(nm); card.add(Box.createVerticalStrut(5));
            card.add(lc); card.add(Box.createVerticalStrut(10));
            card.add(priceTag); card.add(Box.createVerticalStrut(15));
            card.add(act);
            gridPanel.add(card);
        }
        gridPanel.revalidate(); gridPanel.repaint();
    }
    
    private JPanel createHistoryTab() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(StyleUtils.BACKGROUND_MAIN);
        
        historyContainer = new JPanel();
        historyContainer.setLayout(new BoxLayout(historyContainer, BoxLayout.Y_AXIS));
        historyContainer.setBackground(StyleUtils.BACKGROUND_MAIN);
        historyContainer.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JScrollPane sp = new JScrollPane(historyContainer);
        sp.setBorder(null);
        p.add(sp, BorderLayout.CENTER);
        return p;
    }
    
    private void refreshHistory() {
        if(manager == null) return;
        manager.refreshData();
        historyContainer.removeAll();
        ArrayList<Booking> confirmed = manager.getBookingsForUser(currentUser.getEmail());
        for(Booking b : confirmed) {
            historyContainer.add(createHistoryCard(b.getCourtName(), b.getDate() + " " + b.getTimeSlot(), "CONFIRMED", true));
            historyContainer.add(Box.createVerticalStrut(15));
        }
        BookingRequest req = manager.pendingRequests.peek();
        while(req != null) {
            if(req.user.getEmail().equalsIgnoreCase(currentUser.getEmail())) {
                historyContainer.add(createHistoryCard(req.court.name, req.requestedTime, "PENDING APPROVAL", false));
                historyContainer.add(Box.createVerticalStrut(15));
            }
            req = req.next;
        }
        historyContainer.revalidate(); historyContainer.repaint();
    }
    
    private JPanel createHistoryCard(String court, String time, String status, boolean isDone) {
        JPanel p = StyleUtils.createCardPanel();
        p.setLayout(new BorderLayout());
        p.setMaximumSize(new Dimension(2000, 100));
        p.setBorder(new EmptyBorder(20,25,20,25));
        
        JPanel left = new JPanel(new GridLayout(2,1));
        left.setOpaque(false);
        JLabel l1 = new JLabel(court); l1.setFont(StyleUtils.FONT_HEADER_MD);
        JLabel l2 = new JLabel(time); l2.setFont(StyleUtils.FONT_BODY); l2.setForeground(Color.GRAY);
        left.add(l1); left.add(l2);
        
        JLabel st = new JLabel(status);
        st.setFont(StyleUtils.FONT_BODY_BOLD);
        st.setForeground(isDone ? StyleUtils.THEME_GREEN_MAIN : StyleUtils.THEME_ACCENT_GOLD);
        st.setBorder(new LineBorder(isDone ? StyleUtils.THEME_GREEN_MAIN : StyleUtils.THEME_ACCENT_GOLD, 2, true));
        st.setBorder(BorderFactory.createCompoundBorder(st.getBorder(), new EmptyBorder(5, 10, 5, 10)));
        
        p.add(left, BorderLayout.WEST);
        p.add(st, BorderLayout.EAST);
        return p;
    }
    
    private void styleCombo(JComboBox box) {
        box.setBackground(Color.WHITE);
        box.setFont(StyleUtils.FONT_BODY);
        box.setPreferredSize(new Dimension(160, 40));
        box.setBorder(BorderFactory.createLineBorder(StyleUtils.THEME_GREEN_MAIN, 1));
    }
}