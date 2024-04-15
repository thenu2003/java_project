import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GanttChart extends JPanel {
    private List<ExpenseCategory> expenseCategories;
    private String chartTitle;
    public GanttChart(String chartTitle) {
        this.chartTitle = chartTitle;
        expenseCategories = new ArrayList<>();
        setBackground(new Color(240, 240, 240));
    }
    public void setExpenseCategories(List<ExpenseCategory> expenseCategories) {
        this.expenseCategories = expenseCategories;
        repaint();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int taskHeight = 30;
        int barGap = 20;
        int topMargin = 50;
        int leftMargin = 100;
        int chartWidth = getWidth() - leftMargin * 2;
        Font titleFont = new Font(Font.SANS_SERIF, Font.BOLD, 18);
        g2d.setFont(titleFont);
        FontMetrics titleMetrics = g2d.getFontMetrics(titleFont);
        int titleWidth = titleMetrics.stringWidth(chartTitle);
        g2d.drawString(chartTitle, (getWidth() - titleWidth) / 2, topMargin / 2);
        Font categoryFont = new Font(Font.SANS_SERIF, Font.BOLD, 14);
        g2d.setFont(categoryFont);
        FontMetrics categoryMetrics = g2d.getFontMetrics(categoryFont);
        for (int i = 0; i < expenseCategories.size(); i++) {
            ExpenseCategory category = expenseCategories.get(i);
            int y = topMargin + i * (taskHeight + barGap);
            int taskWidth = (int) (chartWidth * category.getAmount() / category.getMaxAmount());
            g2d.setColor(category.getColor());
            g2d.fillRect(leftMargin, y, taskWidth, taskHeight);
            int textWidth = categoryMetrics.stringWidth(category.getName());
            int textX = leftMargin - textWidth - 10;
            int textY = y + taskHeight / 2 + categoryMetrics.getHeight() / 2 - categoryMetrics.getDescent();
            g2d.setColor(Color.BLACK);
            g2d.drawString(category.getName(), textX, textY);
            g2d.drawString(String.format("$%.2f", category.getAmount()), leftMargin + taskWidth + 10, y + taskHeight / 2);
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Gantt Chart");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            List<ExpenseCategory> expenseCategories = getExpenseCategoriesFromUser();
            GanttChart ganttChart = new GanttChart("Expenses of the Day");
            ganttChart.setExpenseCategories(expenseCategories);
            frame.add(ganttChart, BorderLayout.CENTER);

            frame.setSize(800, 600);
            frame.setVisible(true);
        });
    }
    private static List<ExpenseCategory> getExpenseCategoriesFromUser() {
        List<ExpenseCategory> categories = new ArrayList<>();
        JPanel panel = new JPanel(new GridLayout(0, 2));
        JTextField amountFieldFood = new JTextField();
        JTextField amountFieldTransportation = new JTextField();
        JTextField amountFieldEntertainment = new JTextField();
        JTextField amountFieldUtilities = new JTextField();
        JTextField amountFieldOthers = new JTextField();
        panel.add(new JLabel("Amount for Food:"));
        panel.add(amountFieldFood);
        panel.add(new JLabel("Amount for Transportation:"));
        panel.add(amountFieldTransportation);
        panel.add(new JLabel("Amount for Entertainment:"));
        panel.add(amountFieldEntertainment);
        panel.add(new JLabel("Amount for Utilities:"));
        panel.add(amountFieldUtilities);
        panel.add(new JLabel("Amount for Others:"));
        panel.add(amountFieldOthers);
        int result = JOptionPane.showConfirmDialog(null, panel, "Enter Expense Amounts for Today", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                double amountFood = Double.parseDouble(amountFieldFood.getText().trim());
                double amountTransportation = Double.parseDouble(amountFieldTransportation.getText().trim());
                double amountEntertainment = Double.parseDouble(amountFieldEntertainment.getText().trim());
                double amountUtilities = Double.parseDouble(amountFieldUtilities.getText().trim());
                double amountOthers = Double.parseDouble(amountFieldOthers.getText().trim());

                categories.add(new ExpenseCategory("Food", amountFood));
                categories.add(new ExpenseCategory("Transport", amountTransportation));
                categories.add(new ExpenseCategory("Grocery", amountEntertainment));
                categories.add(new ExpenseCategory("Utilities", amountUtilities));
                categories.add(new ExpenseCategory("Others", amountOthers));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input for amount.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        return categories;
    }
}

class ExpenseCategory {
    private String name;
    private double amount;
    private static final double MAX_AMOUNT = 1000; 
    private Color color;
    public ExpenseCategory(String name, double amount) {
        this.name = name;
        this.amount = amount;
        switch (name.toLowerCase()) {
            case "food":
                color = Color.decode("#097969");
                break;
            case "transportation":
                color = Color.decode("#50C878");
                break;
            case "entertainment":
                color = Color.decode("#2AAA8A");
                break;
            case "utilities":
                color = Color.decode("#478778");
                break;
            case "others":
                color = Color.decode("#E64A19");
                break;
            default:
                color = Color.decode("#ececa3");
                break;
        }
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public double getMaxAmount() {
        return MAX_AMOUNT;
    }

    public Color getColor() {
        return color;
    }
}
