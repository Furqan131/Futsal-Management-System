import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class StyleUtils {
    public static final Color THEME_GREEN_DARK = new Color(0, 80, 40);
    public static final Color THEME_GREEN_MAIN = new Color(0, 120, 60);
    public static final Color THEME_GREEN_LIGHT = new Color(40, 160, 90);
    public static final Color THEME_ACCENT_GOLD = new Color(255, 193, 7);
    
    public static final Color BACKGROUND_MAIN = new Color(242, 246, 242); 
    public static final Color BACKGROUND_LIGHT = new Color(248, 252, 248); 

    public static final Color TEXT_DARK = new Color(40, 40, 40);
    public static final Color RED_ERROR = new Color(220, 53, 69);
    public static final Color BLUE_INFO = new Color(23, 162, 184);

    public static final Font FONT_HEADER_LG = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font FONT_HEADER_MD = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_BODY_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);

    public static JButton createModernButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(THEME_GREEN_DARK);
                } else if (getModel().isRollover()) {
                    g2.setColor(THEME_GREEN_LIGHT);
                } else {
                    g2.setColor(THEME_GREEN_MAIN);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(Color.WHITE);
        btn.setFont(FONT_BODY_BOLD);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 25, 10, 25));
        return btn;
    }
    
    public static JButton createOutlineButton(String text) {
        JButton btn = new JButton(text);
        btn.setForeground(THEME_GREEN_MAIN);
        btn.setBackground(Color.WHITE);
        btn.setFont(FONT_BODY_BOLD);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(THEME_GREEN_MAIN, 2, true),
            new EmptyBorder(8, 20, 8, 20)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static JPanel createCardPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(2, 2, getWidth()-4, getHeight()-4, 15, 15);
                g2.setColor(new Color(0,0,0,20)); 
                g2.drawRoundRect(2, 2, getWidth()-4, getHeight()-4, 15, 15);
                g2.dispose();
            }
        };
    }

    public static void styleTable(JTable table) {
        table.setRowHeight(40);
        table.setFont(FONT_BODY);
        table.setSelectionBackground(new Color(220, 255, 230));
        table.setSelectionForeground(TEXT_DARK);
        table.setGridColor(new Color(230, 230, 230));
        table.setShowVerticalLines(false);
        
        JTableHeader header = table.getTableHeader();
        header.setBackground(THEME_GREEN_MAIN);
        header.setForeground(Color.WHITE);
        header.setFont(FONT_BODY_BOLD);
        header.setPreferredSize(new Dimension(0, 45));
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
    }

    public static void showNotification(Component parent, String msg, String title, boolean isSuccess) {
        JDialog dialog = new JDialog(getFrame(parent), "", true);
        dialog.setUndecorated(true);
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createLineBorder(isSuccess ? THEME_GREEN_MAIN : RED_ERROR, 2));
        p.setBackground(Color.WHITE);
        
        JPanel head = new JPanel(new FlowLayout(FlowLayout.LEFT));
        head.setBackground(isSuccess ? THEME_GREEN_MAIN : RED_ERROR);
        JLabel tLbl = new JLabel(title);
        tLbl.setForeground(Color.WHITE);
        tLbl.setFont(FONT_BODY_BOLD);
        head.add(tLbl);
        
        JLabel mLbl = new JLabel("<html><center>" + msg + "</center></html>", SwingConstants.CENTER);
        mLbl.setFont(FONT_BODY);
        mLbl.setBorder(new EmptyBorder(20,30,20,30));
        
        JButton ok = createModernButton("OK");
        ok.addActionListener(e -> dialog.dispose());
        JPanel foot = new JPanel();
        foot.setBackground(Color.WHITE);
        foot.setBorder(new EmptyBorder(0,0,15,0));
        foot.add(ok);
        
        p.add(head, BorderLayout.NORTH);
        p.add(mLbl, BorderLayout.CENTER);
        p.add(foot, BorderLayout.SOUTH);
        
        dialog.add(p);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
    
    public static boolean showConfirm(Component parent, String msg, String title) {
        JDialog dialog = new JDialog(getFrame(parent), "", true);
        dialog.setUndecorated(true);
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createLineBorder(THEME_GREEN_MAIN, 2));
        p.setBackground(Color.WHITE);
        
        JPanel head = new JPanel(new FlowLayout(FlowLayout.LEFT));
        head.setBackground(THEME_GREEN_MAIN);
        JLabel tLbl = new JLabel(title);
        tLbl.setForeground(Color.WHITE);
        tLbl.setFont(FONT_BODY_BOLD);
        head.add(tLbl);
        
        JLabel mLbl = new JLabel("<html><center>" + msg + "</center></html>", SwingConstants.CENTER);
        mLbl.setFont(FONT_BODY);
        mLbl.setBorder(new EmptyBorder(20,30,20,30));
        
        JButton yes = createModernButton("Yes");
        JButton no = createOutlineButton("No/Cancel");
        
        final boolean[] res = {false};
        yes.addActionListener(e -> { res[0]=true; dialog.dispose(); });
        no.addActionListener(e -> { res[0]=false; dialog.dispose(); });
        
        JPanel foot = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        foot.setBackground(Color.WHITE);
        foot.add(yes);
        foot.add(no);
        
        p.add(head, BorderLayout.NORTH);
        p.add(mLbl, BorderLayout.CENTER);
        p.add(foot, BorderLayout.SOUTH);
        
        dialog.add(p);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
        return res[0];
    }
    
    public static String showInput(Component parent, String msg, String title) {
        JDialog dialog = new JDialog(getFrame(parent), "", true);
        dialog.setUndecorated(true);
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createLineBorder(THEME_GREEN_MAIN, 2));
        p.setBackground(Color.WHITE);
        
        JPanel head = new JPanel(new FlowLayout(FlowLayout.LEFT));
        head.setBackground(THEME_GREEN_MAIN);
        JLabel tLbl = new JLabel(title);
        tLbl.setForeground(Color.WHITE);
        tLbl.setFont(FONT_BODY_BOLD);
        head.add(tLbl);
        
        JPanel body = new JPanel(new GridLayout(2,1));
        body.setBackground(Color.WHITE);
        body.setBorder(new EmptyBorder(20,30,10,30));
        JLabel mLbl = new JLabel(msg);
        mLbl.setFont(FONT_BODY);
        JTextField tf = new JTextField();
        tf.setFont(FONT_BODY);
        body.add(mLbl);
        body.add(tf);
        
        JButton ok = createModernButton("OK");
        JButton cancel = createOutlineButton("Cancel");
        
        final String[] res = {null};
        ok.addActionListener(e -> { res[0]=tf.getText(); dialog.dispose(); });
        cancel.addActionListener(e -> dialog.dispose());
        
        JPanel foot = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        foot.setBackground(Color.WHITE);
        foot.add(ok);
        foot.add(cancel);
        
        p.add(head, BorderLayout.NORTH);
        p.add(body, BorderLayout.CENTER);
        p.add(foot, BorderLayout.SOUTH);
        
        dialog.add(p);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
        return res[0];
    }

    private static Frame getFrame(Component p) {
        if(p == null) return null;
        if(p instanceof Frame) return (Frame)p;
        return getFrame(p.getParent());
    }
}