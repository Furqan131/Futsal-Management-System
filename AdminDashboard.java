import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder; 
import javax.swing.table.DefaultTableModel;

public class AdminDashboard extends JFrame {
    private FutsalManager manager;
    private JPanel centerPanel;
    private CardLayout centerLayout;
    
    // UI Elements
    private DefaultTableModel tableModel;
    private DefaultTableModel bookingTableModel;
    private JTable bookingTable;
    private JPanel queueContainer;
    private JPanel cancellationContainer; // NEW
    
    private JLabel lblTotalRevenue;
    private JLabel lblTotalBookings;
    private JLabel lblPopularCourt;
    
    // Sidebar Buttons
    private JButton btnHome, btnCourts, btnQueue, btnBookings, btnCancelMode;

    public AdminDashboard() {
        manager = new FutsalManager();
        
        setTitle("Admin Console - Futsal Management System");
        setSize(1450, 950);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- SIDEBAR (Modern & Fixed) ---
        JPanel sidebar = new JPanel(new GridBagLayout()); 
        sidebar.setBackground(StyleUtils.THEME_GREEN_DARK);
        sidebar.setPreferredSize(new Dimension(320, 0)); 
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        
        // Logo
        gbc.gridy = 0;
        gbc.insets = new Insets(40, 20, 50, 20);
        JLabel logo = new JLabel("<html><center>ADMIN<br>DASHBOARD</center></html>", SwingConstants.CENTER);
        logo.setFont(StyleUtils.FONT_HEADER_MD);
        logo.setForeground(Color.WHITE);
        sidebar.add(logo, gbc);
        
        // Navigation Buttons
        gbc.insets = new Insets(0, 25, 15, 25); 
        
        btnHome = createNavButton("Analytics Home", "HOME");
        gbc.gridy = 1; sidebar.add(btnHome, gbc);
        
        btnCourts = createNavButton("Manage Courts", "COURTS");
        gbc.gridy = 2; sidebar.add(btnCourts, gbc);
        
        btnQueue = createNavButton("Pending Requests", "QUEUE");
        gbc.gridy = 3; sidebar.add(btnQueue, gbc);
        
        btnBookings = createNavButton("All Bookings", "BOOKINGS");
        gbc.gridy = 4; sidebar.add(btnBookings, gbc);
        
        // NEW: Dedicated Cancel Booking Sidebar Item
        btnCancelMode = createNavButton("Cancel Bookings", "CANCEL_MODE");
        // Style it slightly differently to indicate it's an action-heavy page? 
        // For consistency, keep it same style but maybe red hover?
        // Let's keep consistent style for now.
        gbc.gridy = 5; sidebar.add(btnCancelMode, gbc);
        
        // Spacer
        gbc.gridy = 6;
        gbc.weighty = 1.0;
        sidebar.add(Box.createVerticalGlue(), gbc);
        
        // Logout
        gbc.gridy = 7;
        gbc.weighty = 0;
        gbc.insets = new Insets(0, 25, 40, 25);
        JButton logout = StyleUtils.createModernButton("Logout");
        logout.setBackground(StyleUtils.RED_ERROR);
        sidebar.add(logout, gbc);

        // --- CENTER ---
        centerLayout = new CardLayout();
        centerPanel = new JPanel(centerLayout);
        centerPanel.setBackground(StyleUtils.BACKGROUND_MAIN); 
        
        centerPanel.add(createHomePanel(), "HOME");
        centerPanel.add(createCourtsPanel(), "COURTS");
        centerPanel.add(createQueuePanel(), "QUEUE");
        centerPanel.add(createBookingsPanel(), "BOOKINGS");
        centerPanel.add(createCancellationPanel(), "CANCEL_MODE"); // NEW Panel

        add(sidebar, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        
        // Actions
        logout.addActionListener(e -> {
            if(StyleUtils.showConfirm(this, "Logout?", "Confirm")) { new LoginFrame(); dispose(); }
        });

        // Set default active
        setActiveButton(btnHome);
        refreshAnalytics();
        
        setVisible(true);
    }
    
    // --- NAV BUTTON HELPER ---
    private void setActiveButton(JButton active) {
        // Reset all
        resetNavStyle(btnHome);
        resetNavStyle(btnCourts);
        resetNavStyle(btnQueue);
        resetNavStyle(btnBookings);
        resetNavStyle(btnCancelMode);
        
        // Set active style (Solid White, Green Text)
        active.putClientProperty("isActive", true);
        active.repaint();
    }
    
    private void resetNavStyle(JButton btn) {
        btn.putClientProperty("isActive", false);
        btn.repaint();
    }

    private JButton createNavButton(String text, String cardName) {
        JButton b = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                boolean active = Boolean.TRUE.equals(getClientProperty("isActive"));
                
                if (active) {
                    g2.setColor(Color.WHITE);
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 255, 255, 50));
                } else {
                    g2.setColor(new Color(255, 255, 255, 20)); 
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
                
                // Text color update based on state
                if(active) setForeground(StyleUtils.THEME_GREEN_MAIN);
                else setForeground(Color.WHITE);
            }
        };
        
        b.setForeground(Color.WHITE);
        b.setFont(StyleUtils.FONT_BODY_BOLD);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setOpaque(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(0, 55)); 
        
        b.addActionListener(e -> {
            centerLayout.show(centerPanel, cardName);
            setActiveButton(b);
            
            if(cardName.equals("HOME")) refreshAnalytics();
            if(cardName.equals("QUEUE")) refreshQueue();
            if(cardName.equals("BOOKINGS")) refreshBookingsTable();
            if(cardName.equals("COURTS")) refreshCourtsTable();
            if(cardName.equals("CANCEL_MODE")) refreshCancellationPanel();
        });
        return b;
    }
    
    // --- PANEL 0: HOME ---
    private JPanel createHomePanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(StyleUtils.BACKGROUND_MAIN);
        p.setBorder(new EmptyBorder(40,40,40,40));
        
        JLabel title = new JLabel("Analytics Overview");
        title.setFont(StyleUtils.FONT_HEADER_LG);
        title.setForeground(StyleUtils.THEME_GREEN_MAIN);
        p.add(title, BorderLayout.NORTH);
        
        JPanel cards = new JPanel(new GridLayout(1, 3, 30, 0));
        cards.setOpaque(false);
        cards.setBorder(new EmptyBorder(40, 0, 0, 0));
        
        lblTotalRevenue = new JLabel("0");
        lblTotalBookings = new JLabel("0");
        lblPopularCourt = new JLabel("-");
        
        cards.add(createStatCard("Total Revenue", lblTotalRevenue, new Color(0, 120, 60)));
        cards.add(createStatCard("Total Bookings", lblTotalBookings, new Color(23, 162, 184)));
        cards.add(createStatCard("Most Popular", lblPopularCourt, new Color(255, 193, 7)));
        
        JPanel content = new JPanel(new BorderLayout());
        content.setOpaque(false);
        content.add(cards, BorderLayout.NORTH);
        p.add(content, BorderLayout.CENTER);
        return p;
    }
    
    private JPanel createStatCard(String title, JLabel valueLbl, Color color) {
        JPanel card = StyleUtils.createCardPanel();
        card.setLayout(new GridLayout(2, 1));
        card.setBorder(new EmptyBorder(25,25,25,25));
        
        JLabel t = new JLabel(title);
        t.setFont(StyleUtils.FONT_BODY_BOLD);
        t.setForeground(Color.GRAY);
        
        valueLbl.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valueLbl.setForeground(color);
        
        card.add(t); card.add(valueLbl);
        return card;
    }
    
    private void refreshAnalytics() {
        int rev = manager.calculateTotalRevenue();
        int count = manager.bookings.size();
        String pop = manager.getMostPopularCourt();
        lblTotalRevenue.setText("Rs " + rev);
        lblTotalBookings.setText(String.valueOf(count));
        lblPopularCourt.setText(pop);
    }

    // --- PANEL 1: COURTS ---
    private JPanel createCourtsPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(StyleUtils.BACKGROUND_MAIN);
        p.setBorder(new EmptyBorder(40, 40, 40, 40));
        
        JPanel head = new JPanel(new BorderLayout());
        head.setOpaque(false);
        JLabel title = new JLabel("Manage Courts");
        title.setFont(StyleUtils.FONT_HEADER_LG);
        title.setForeground(StyleUtils.THEME_GREEN_MAIN);
        
        JPanel acts = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        acts.setOpaque(false);
        JButton addBtn = StyleUtils.createModernButton("+ New Court");
        JButton delBtn = StyleUtils.createOutlineButton("Delete Court");
        acts.add(addBtn); acts.add(delBtn);
        
        head.add(title, BorderLayout.WEST); head.add(acts, BorderLayout.EAST);
        p.add(head, BorderLayout.NORTH);
        
        String[] cols = {"Court ID", "Name", "Location", "Price / Hr"};
        tableModel = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        JTable table = new JTable(tableModel);
        StyleUtils.styleTable(table);
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(Color.WHITE);
        p.add(sp, BorderLayout.CENTER);
        
        refreshCourtsTable();
        
        addBtn.addActionListener(e -> showAddDialog());
        delBtn.addActionListener(e -> {
            String id = StyleUtils.showInput(this, "Enter ID to remove:", "Remove");
            if(id != null && !id.isEmpty()) {
                if(manager.removeCourtGUI(id)) { refreshCourtsTable(); StyleUtils.showNotification(this, "Removed", "Success", true); } 
                else { StyleUtils.showNotification(this, "ID not found", "Error", false); }
            }
        });
        return p;
    }
    
    private void showAddDialog() {
        JDialog d = new JDialog(this, "", true);
        d.setUndecorated(true);
        JPanel p = new JPanel(new GridLayout(5, 1, 10, 10));
        p.setBorder(BorderFactory.createCompoundBorder(new LineBorder(StyleUtils.THEME_GREEN_MAIN, 2), new EmptyBorder(20,20,20,20)));
        p.setBackground(Color.WHITE);
        
        JTextField idF = new JTextField(); idF.setBorder(BorderFactory.createTitledBorder("ID"));
        JTextField nmF = new JTextField(); nmF.setBorder(BorderFactory.createTitledBorder("Name"));
        JTextField lcF = new JTextField(); lcF.setBorder(BorderFactory.createTitledBorder("Location"));
        JTextField prF = new JTextField(); prF.setBorder(BorderFactory.createTitledBorder("Price"));
        
        JPanel btns = new JPanel(new FlowLayout());
        btns.setBackground(Color.WHITE);
        JButton sv = StyleUtils.createModernButton("Save");
        JButton cn = StyleUtils.createOutlineButton("Cancel");
        btns.add(sv); btns.add(cn);
        
        p.add(idF); p.add(nmF); p.add(lcF); p.add(prF); p.add(btns);
        
        sv.addActionListener(e -> {
            try {
                if(manager.addCourtGUI(idF.getText(), nmF.getText(), lcF.getText(), Integer.parseInt(prF.getText()))){
                    refreshCourtsTable(); d.dispose();
                } else { StyleUtils.showNotification(d, "ID exists", "Error", false); }
            } catch(Exception ex) { StyleUtils.showNotification(d, "Invalid Price", "Error", false); }
        });
        cn.addActionListener(e -> d.dispose());
        d.add(p); d.setSize(300, 350); d.setLocationRelativeTo(this); d.setVisible(true);
    }
    
    private void refreshCourtsTable() {
        tableModel.setRowCount(0);
        for(FutsalGround g : manager.array) {
            if(g!=null) tableModel.addRow(new Object[]{g.id, g.name, g.location, g.price});
        }
    }

    // --- PANEL 2: QUEUE ---
    private JPanel createQueuePanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(StyleUtils.BACKGROUND_MAIN);
        p.setBorder(new EmptyBorder(40, 40, 40, 40));
        
        JLabel title = new JLabel("Pending Requests");
        title.setFont(StyleUtils.FONT_HEADER_LG);
        title.setForeground(StyleUtils.THEME_GREEN_MAIN);
        p.add(title, BorderLayout.NORTH);
        
        queueContainer = new JPanel();
        queueContainer.setLayout(new BoxLayout(queueContainer, BoxLayout.Y_AXIS)); 
        queueContainer.setBackground(StyleUtils.BACKGROUND_MAIN);
        
        JScrollPane sp = new JScrollPane(queueContainer);
        sp.setBorder(null);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        p.add(sp, BorderLayout.CENTER);
        
        return p;
    }
    
    private void refreshQueue() {
        queueContainer.removeAll();
        BookingRequest req = manager.pendingRequests.peek();
        
        if(req == null) {
            JLabel l = new JLabel("No Pending Requests");
            l.setFont(StyleUtils.FONT_HEADER_MD);
            l.setForeground(Color.GRAY);
            l.setAlignmentX(Component.CENTER_ALIGNMENT);
            queueContainer.add(Box.createVerticalGlue()); queueContainer.add(l); queueContainer.add(Box.createVerticalGlue());
        } else {
            BookingRequest current = req;
            while(current != null) {
                JPanel card = createQueueCard(current);
                queueContainer.add(card);
                queueContainer.add(Box.createVerticalStrut(20));
                current = current.next;
            }
        }
        queueContainer.revalidate(); queueContainer.repaint();
    }
    
    private JPanel createQueueCard(BookingRequest req) {
        JPanel card = StyleUtils.createCardPanel();
        card.setMaximumSize(new Dimension(1000, 120));
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(20,25,20,25));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel info = new JPanel(new GridLayout(2, 1));
        info.setOpaque(false);
        JLabel l1 = new JLabel(req.user.getName() + " requests " + req.court.name);
        l1.setFont(StyleUtils.FONT_HEADER_MD);
        JLabel l2 = new JLabel(req.requestedTime + " | " + req.user.getEmail());
        l2.setFont(StyleUtils.FONT_BODY); l2.setForeground(Color.GRAY);
        info.add(l1); info.add(l2);
        
        JPanel acts = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        acts.setOpaque(false);
        JButton app = StyleUtils.createModernButton("Approve");
        JButton rej = StyleUtils.createOutlineButton("Reject");
        acts.add(app); acts.add(rej);
        
        card.add(info, BorderLayout.CENTER);
        card.add(acts, BorderLayout.EAST);
        
        if(req != manager.pendingRequests.peek()) {
            app.setEnabled(false); rej.setEnabled(false);
            app.setToolTipText("Process FIFO");
        } else {
            app.addActionListener(e -> {
                manager.pendingRequests.dequeue();
                manager.saveRequests();
                String[] split = req.requestedTime.split("\\|");
                manager.confirmBooking(req.court.id, req.user.getName(), req.user.getEmail(), split[0].trim(), split[1].trim());
                StyleUtils.showNotification(this, "Approved", "Success", true);
                refreshQueue(); refreshAnalytics();
            });
            rej.addActionListener(e -> {
                manager.pendingRequests.dequeue();
                manager.saveRequests();
                StyleUtils.showNotification(this, "Rejected", "Info", false);
                refreshQueue();
            });
        }
        return card;
    }

    // --- PANEL 3: BOOKINGS ---
    private JPanel createBookingsPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(StyleUtils.BACKGROUND_MAIN);
        p.setBorder(new EmptyBorder(40, 40, 40, 40));
        
        JPanel topBox = new JPanel(new BorderLayout());
        topBox.setOpaque(false);
        
        JLabel title = new JLabel("All Bookings");
        title.setFont(StyleUtils.FONT_HEADER_LG);
        title.setForeground(StyleUtils.THEME_GREEN_MAIN);
        
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        toolbar.setOpaque(false);
        
        JTextField searchF = new JTextField(15);
        searchF.setFont(StyleUtils.FONT_BODY);
        searchF.setBorder(BorderFactory.createCompoundBorder(new LineBorder(StyleUtils.THEME_GREEN_MAIN, 1, true), new EmptyBorder(5, 10, 5, 10)));
        
        JButton searchBtn = StyleUtils.createOutlineButton("Search");
        JButton btnSortName = StyleUtils.createOutlineButton("Sort Name");
        JButton btnSortCourt = StyleUtils.createOutlineButton("Sort Court");
        
        toolbar.add(new JLabel("Search: ")); toolbar.add(searchF); toolbar.add(searchBtn);
        toolbar.add(Box.createHorizontalStrut(15));
        toolbar.add(btnSortName); toolbar.add(btnSortCourt);
        
        topBox.add(title, BorderLayout.WEST); topBox.add(toolbar, BorderLayout.EAST);
        p.add(topBox, BorderLayout.NORTH);
        
        String[] cols = {"Court ID", "Court Name", "Customer", "Date", "Time Slot"};
        bookingTableModel = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        bookingTable = new JTable(bookingTableModel);
        StyleUtils.styleTable(bookingTable);
        
        JScrollPane sp = new JScrollPane(bookingTable);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(Color.WHITE);
        p.add(sp, BorderLayout.CENTER);
        
        btnSortName.addActionListener(e -> { manager.sortBookingsByCustomer(); refreshBookingsTable(); });
        btnSortCourt.addActionListener(e -> { manager.sortBookingsByCourt(); refreshBookingsTable(); });
        searchBtn.addActionListener(e -> {
            String q = searchF.getText();
            if(q.isEmpty()) refreshBookingsTable();
            else {
                ArrayList<Booking> res = manager.searchBookings(q);
                bookingTableModel.setRowCount(0);
                for(Booking b : res) bookingTableModel.addRow(new Object[]{b.getCourtId(), b.getCourtName(), b.getCustomerName(), b.getDate(), b.getTimeSlot()});
            }
        });
        return p;
    }
    
    // --- NEW PANEL: CANCELLATION MODE ---
    private JPanel createCancellationPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(StyleUtils.BACKGROUND_MAIN);
        p.setBorder(new EmptyBorder(40, 40, 40, 40));
        
        JLabel title = new JLabel("Cancellation Mode");
        title.setFont(StyleUtils.FONT_HEADER_LG);
        title.setForeground(StyleUtils.RED_ERROR); // Red title to indicate danger/action
        p.add(title, BorderLayout.NORTH);
        
        cancellationContainer = new JPanel();
        cancellationContainer.setLayout(new BoxLayout(cancellationContainer, BoxLayout.Y_AXIS));
        cancellationContainer.setBackground(StyleUtils.BACKGROUND_MAIN);
        
        JScrollPane sp = new JScrollPane(cancellationContainer);
        sp.setBorder(null);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        p.add(sp, BorderLayout.CENTER);
        
        return p;
    }
    
    private void refreshCancellationPanel() {
        cancellationContainer.removeAll();
        
        if(manager.bookings.isEmpty()) {
            JLabel l = new JLabel("No Active Bookings to Cancel");
            l.setFont(StyleUtils.FONT_HEADER_MD);
            l.setForeground(Color.GRAY);
            l.setAlignmentX(Component.CENTER_ALIGNMENT);
            cancellationContainer.add(Box.createVerticalGlue()); cancellationContainer.add(l); cancellationContainer.add(Box.createVerticalGlue());
        } else {
            for(Booking b : manager.bookings) {
                JPanel card = StyleUtils.createCardPanel();
                card.setMaximumSize(new Dimension(1000, 100));
                card.setLayout(new BorderLayout());
                card.setBorder(new EmptyBorder(20,25,20,25));
                card.setAlignmentX(Component.CENTER_ALIGNMENT);
                
                JPanel info = new JPanel(new GridLayout(2, 1));
                info.setOpaque(false);
                JLabel l1 = new JLabel(b.getCourtName() + " - " + b.getCustomerName());
                l1.setFont(StyleUtils.FONT_HEADER_MD);
                JLabel l2 = new JLabel(b.getDate() + " at " + b.getTimeSlot());
                l2.setFont(StyleUtils.FONT_BODY); l2.setForeground(Color.GRAY);
                info.add(l1); info.add(l2);
                
                JButton cancelBtn = StyleUtils.createModernButton("Cancel Booking");
                cancelBtn.setBackground(StyleUtils.RED_ERROR);
                cancelBtn.addActionListener(e -> {
                    if(StyleUtils.showConfirm(this, "Permanently cancel this booking?", "Confirm Cancel")) {
                        manager.cancelBooking(b.getCourtId(), b.getDate(), b.getTimeSlot());
                        StyleUtils.showNotification(this, "Booking Cancelled", "Success", true);
                        refreshCancellationPanel(); // Refresh list
                        refreshAnalytics();
                    }
                });
                
                card.add(info, BorderLayout.CENTER);
                card.add(cancelBtn, BorderLayout.EAST);
                
                cancellationContainer.add(card);
                cancellationContainer.add(Box.createVerticalStrut(15));
            }
        }
        cancellationContainer.revalidate(); cancellationContainer.repaint();
    }
    
    private void refreshBookingsTable() {
        bookingTableModel.setRowCount(0);
        for(Booking b : manager.bookings) {
            bookingTableModel.addRow(new Object[]{b.getCourtId(), b.getCourtName(), b.getCustomerName(), b.getDate(), b.getTimeSlot()});
        }
    }
}