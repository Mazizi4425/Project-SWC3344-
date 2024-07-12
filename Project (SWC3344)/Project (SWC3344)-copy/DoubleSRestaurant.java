import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.*;

public class DoubleSRestaurant extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JLabel countLabel;
    private int customerCount = 0;
    private Queue<String> customerQueue = new LinkedList<>();
    private Queue<String> counter1Queue = new LinkedList<>();
    private Queue<String> counter2Queue = new LinkedList<>();
    private Queue<String> counter3Queue = new LinkedList<>();
    private StringBuilder counter1Receipts = new StringBuilder();
    private StringBuilder counter2Receipts = new StringBuilder();
    private StringBuilder counter3Receipts = new StringBuilder();
    private DefaultListModel<String> counter1ListModel = new DefaultListModel<>();
    private DefaultListModel<String> counter2ListModel = new DefaultListModel<>();
    private DefaultListModel<String> counter3ListModel = new DefaultListModel<>();
    private JTextField searchTextField;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
                public void run() {
                    try {
                        DoubleSRestaurant frame = new DoubleSRestaurant();
                        frame.setVisible(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
    }

    /**
     * Create the frame.
     */
    public DoubleSRestaurant() {
        setResizable(false);
        setTitle("DoubleSRestaurant");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1231, 760);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        countLabel = new JLabel("Count : 0");
        countLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        countLabel.setHorizontalAlignment(SwingConstants.CENTER);
        countLabel.setBounds(575, 10, 100, 13);
        contentPane.add(countLabel);

        JButton showDataButton = new JButton("Pending Customer");
        showDataButton.setBackground(new Color(211, 211, 211)); // Light gray
        showDataButton.setFont(new Font("Dubai", Font.BOLD, 15));
        showDataButton.setBounds(165, 45, 176, 25);
        showDataButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new ShowDataFrame(customerQueue).setVisible(true);
                }
            });
        contentPane.add(showDataButton);

        JButton loadOrdersFromFile = new JButton("Load Orders from File");
        loadOrdersFromFile.setBackground(new Color(176, 224, 230)); // Powder blue
        loadOrdersFromFile.setFont(new Font("Dubai", Font.BOLD, 15));
        loadOrdersFromFile.setBounds(375, 45, 176, 25);
        loadOrdersFromFile.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    loadOrdersFromFile();
                }
            });
        contentPane.add(loadOrdersFromFile);

        JButton addNewCustomerButton = new JButton("Add New Customer");
        addNewCustomerButton.setBackground(new Color(240, 230, 140)); // Khaki
        addNewCustomerButton.setFont(new Font("Dubai", Font.BOLD, 15));
        addNewCustomerButton.setBounds(575, 45, 176, 25);
        addNewCustomerButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    addNewCustomerManually();
                }
            });
        contentPane.add(addNewCustomerButton);

        JButton removeCustomerButton = new JButton("Remove Customer");
        removeCustomerButton.setBackground(new Color(255, 192, 203)); // Pink
        removeCustomerButton.setFont(new Font("Dubai", Font.BOLD, 15));
        removeCustomerButton.setBounds(778, 45, 176, 25);
        removeCustomerButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    removeCustomerById();
                }
            });
        contentPane.add(removeCustomerButton);

        JButton nextQueueButton = new JButton("Next Queue");
        nextQueueButton.setBackground(new Color(255, 250, 205)); // Lemon chiffon
        nextQueueButton.setFont(new Font("Dubai", Font.BOLD, 15));
        nextQueueButton.setBounds(982, 45, 121, 25);
        nextQueueButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    nextQueue();
                }
            });
        contentPane.add(nextQueueButton);

        createCounterButtons(84, 568, counter1Queue, "Counter 1", counter1Receipts);
        createCounterButtons(486, 568, counter2Queue, "Counter 2", counter2Receipts);
        createCounterButtons(910, 568, counter3Queue, "Counter 3", counter3Receipts);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 118, 386, 427);
        contentPane.add(scrollPane);

        JList<String> counter1List = new JList<>(counter1ListModel);
        counter1List.setFont(new Font("Segoe UI Black", Font.BOLD, 13));
        scrollPane.setViewportView(counter1List);

        JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setBounds(422, 118, 372, 427);
        contentPane.add(scrollPane1);

        JList<String> counter2List = new JList<>(counter2ListModel);
        counter2List.setFont(new Font("Segoe UI", Font.BOLD, 13));
        scrollPane1.setViewportView(counter2List);

        JScrollPane scrollPane2 = new JScrollPane();
        scrollPane2.setBounds(819, 118, 372, 427);
        contentPane.add(scrollPane2);

        JList<String> counter3List = new JList<>(counter3ListModel);
        counter3List.setFont(new Font("Segoe UI", Font.BOLD, 13));
        scrollPane2.setViewportView(counter3List);

        JButton allRecordButton = new JButton("All Records");
        allRecordButton.setBackground(new Color(255, 235, 205)); // Blanched almond
        allRecordButton.setFont(new Font("Segoe UI Black", Font.BOLD, 15));
        allRecordButton.setBounds(476, 681, 133, 40);
        allRecordButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new RecordFrame(counter1Receipts, counter2Receipts, counter3Receipts).setVisible(true);
                }
            });
        contentPane.add(allRecordButton);

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Dubai", Font.BOLD, 15));
        exitButton.setBackground(new Color(135, 206, 250)); // Light sky blue
        exitButton.setBounds(635, 683, 116, 40);
        exitButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int option = JOptionPane.showConfirmDialog(DoubleSRestaurant.this,
                            "Are you sure you want to exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    }
                }
            });
        contentPane.add(exitButton);

        JLabel counter1Label = new JLabel("Counter 1");
        counter1Label.setFont(new Font("Nirmala UI", Font.BOLD, 15));
        counter1Label.setHorizontalAlignment(SwingConstants.CENTER);
        counter1Label.setBounds(172, 95, 100, 13);
        contentPane.add(counter1Label);

        JLabel counter2Label = new JLabel("Counter 2");
        counter2Label.setHorizontalAlignment(SwingConstants.CENTER);
        counter2Label.setFont(new Font("Nirmala UI", Font.BOLD, 15));
        counter2Label.setBounds(560, 95, 100, 13);
        contentPane.add(counter2Label);

        JLabel counter3Label = new JLabel("Counter 3");
        counter3Label.setHorizontalAlignment(SwingConstants.CENTER);
        counter3Label.setFont(new Font("Nirmala UI", Font.BOLD, 14));
        counter3Label.setBounds(968, 95, 100, 13);
        contentPane.add(counter3Label);

        searchTextField = new JTextField();
        searchTextField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        searchTextField.setBounds(850, 10, 150, 25);
        contentPane.add(searchTextField);
        searchTextField.setColumns(10);

        JButton searchCustomerButton = new JButton("Search");
        searchCustomerButton.setFont(new Font("Dubai", Font.BOLD, 15));
        searchCustomerButton.setBackground(new Color(221, 160, 221)); // Plum
        searchCustomerButton.setBounds(1010, 10, 85, 25);
        searchCustomerButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    searchCustomer();
                }
            });
        contentPane.add(searchCustomerButton);
    }

    private void createCounterButtons(int x, int y, Queue<String> queue, String counterName, StringBuilder receipts ) {
        JButton receiptButton = new JButton("Receipt");
        receiptButton.setBackground(new Color(240, 248, 255)); // Alice blue
        receiptButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        receiptButton.setBounds(x, y, 85, 25);
        receiptButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    processPayment(queue, counterName, receipts);
                    updateListModels();
                }
            });
        contentPane.add(receiptButton);

        JButton recordButton = new JButton("Record");
        recordButton.setBackground(new Color(240, 248, 255)); // Alice blue
        recordButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        recordButton.setBounds(x + 171, y, 85, 25);
        recordButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    showReceipt(receipts, counterName);
                }
            });
        contentPane.add(recordButton);
    }

    private void loadOrdersFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Admin\\Desktop\\customerList.txt"))) {
            String line;
            while ((line = br.readLine()) != null && customerQueue.size() < 100) {
                customerQueue.add(line);
            }
            customerCount = customerQueue.size();
            countLabel.setText("Count : " + customerCount);
            JOptionPane.showMessageDialog(this, "100 New Customers added successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addNewCustomerManually() {
        String custId = JOptionPane.showInputDialog(this, "Enter Customer ID:");
        String custName = JOptionPane.showInputDialog(this, "Enter Customer Name:");
        String tableNumber = JOptionPane.showInputDialog(this, "Enter Table Number:");
        String orderId = JOptionPane.showInputDialog(this, "Enter Order ID:");
        String itemName = JOptionPane.showInputDialog(this, "Enter Item Name:");
        String itemPrice = JOptionPane.showInputDialog(this, "Enter Item Price:");
        String quantity = JOptionPane.showInputDialog(this, "Enter Quantity:");
        String orderTime = JOptionPane.showInputDialog(this, "Enter Order Time:");

        String customerData = String.format("%s, %s, %s, %s, %s, %s, %s, %s",
                custId, custName, tableNumber, orderId, itemName, itemPrice, quantity, orderTime);

        customerQueue.add(customerData);
        customerCount++;
        countLabel.setText("Count : " + customerCount);
        JOptionPane.showMessageDialog(this, "New Customer added successfully:\n");
    }

    private void removeCustomerById() {
        String customerIdToRemove = JOptionPane.showInputDialog(this, "Enter Customer ID to remove:");
        boolean removed = false;

        // Check in customerQueue
        removed = removeFromQueue(customerQueue, customerIdToRemove);

        // Check in counter1Queue
        if (!removed) {
            removed = removeFromQueue(counter1Queue, customerIdToRemove);
        }

        // Check in counter2Queue
        if (!removed) {
            removed = removeFromQueue(counter2Queue, customerIdToRemove);
        }

        // Check in counter3Queue
        if (!removed) {
            removed = removeFromQueue(counter3Queue, customerIdToRemove);
        }

        if (removed) {
            JOptionPane.showMessageDialog(this, "Customer with ID " + customerIdToRemove + " removed successfully.");
            customerCount--;
            countLabel.setText("Count : " + customerCount);
            updateListModels();
        } else {
            JOptionPane.showMessageDialog(this, "No customer found with ID " + customerIdToRemove);
        }
    }

    private boolean removeFromQueue(Queue<String> queue, String customerIdToRemove) {
        boolean removed = false;
        Queue<String> tempQueue = new LinkedList<>();

        while (!queue.isEmpty()) {
            String customer = queue.poll();
            if (customer.split(",")[0].trim().equals(customerIdToRemove)) {
                removed = true;
            } else {
                tempQueue.add(customer);
            }
        }

        while (!tempQueue.isEmpty()) {
            queue.add(tempQueue.poll());
        }

        return removed;
    }

    private void nextQueue() {
        // Separate customers into the three queues
        while (!customerQueue.isEmpty() && (counter1Queue.size() < 5 || counter2Queue.size() < 5 || counter3Queue.size() < 5)) {
            String customer = customerQueue.poll();
            if (customer != null) {
                try {
                    int quantity = Integer.parseInt(customer.split(",")[6].trim()); // Adjusted index to match the quantity field

                    if (quantity <= 5 && counter1Queue.size() < 5) {
                        counter1Queue.add(customer);
                    } else if (quantity <= 5 && counter2Queue.size() < 5) {
                        counter2Queue.add(customer);
                    } else if (quantity > 5 && counter3Queue.size() < 5) {
                        counter3Queue.add(customer);
                    } else {
                        customerQueue.add(customer);
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error parsing quantity for customer: " + customer);
                }
            }
        }
        updateListModels();
        updateQueueCounts();
    }

    private void processPayment(Queue<String> queue, String counterName, StringBuilder receipts) {
        if (!queue.isEmpty()) {
            String customer = queue.poll();
            // Process the payment for the customer

            receipts.append("Successful Payment from").append(customer.split(",")[1]).append("\n\n");
            JOptionPane.showMessageDialog(this, "Processing payment for " + customer.split(",")[1] + " at " + counterName);
            JOptionPane.showMessageDialog(null, "===================================\n"
                + "                           RECEIPT             \n"
                + "===================================\n"
                + "Customer ID                        : " + customer.split(",")[0] + "\n" 
                + "Customer Name                  :" + customer.split(",")[1] + "\n"
                + "Table Number                     :" + customer.split(",")[2] + "\n"
                + "Order ID                               :" + customer.split(",")[3] + "\n"
                + "Food/Beverages Ordered :" + customer.split(",")[4] + "\n"
                + "Price per Unit                      : RM" + customer.split(",")[5] + "\n"
                + "Quantity                                :" + customer.split(",")[6] + "\n"
                + "Order Time                           :" + customer.split(",")[7] + "\n"
                + "Total Paid                             : RM" + customer.split(",")[8] + "\n\n"
                + "----------           Paid at " + counterName + "           ----------");

        } else {
            JOptionPane.showMessageDialog(this, counterName + " is empty.");
        }
    }

    private void showReceipt(StringBuilder receipts, String counterName) {
        if (receipts.length() > 0) {
            JOptionPane.showMessageDialog(this, receipts.toString());
        } else {
            JOptionPane.showMessageDialog(this, counterName + " has no receipts.");
        }
    }

    private void updateQueueCounts() {
        // Update the counters based on current queue sizes
        countLabel.setText("Count : " + customerQueue.size());
    }

    private void updateListModels() {
        counter1ListModel.clear();
        counter2ListModel.clear();
        counter3ListModel.clear();

        for (String customer : counter1Queue) {
            counter1ListModel.addElement(customer);
        }

        for (String customer : counter2Queue) {
            counter2ListModel.addElement(customer);
        }

        for (String customer : counter3Queue) {
            counter3ListModel.addElement(customer);
        }
    }

    private void searchCustomer() {
        String searchText = searchTextField.getText().trim();
        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a customer ID to search.");
            return;
        }

        StringBuilder result = new StringBuilder();
        boolean found = false;

        // Search in all queues
        found = searchInQueue(customerQueue, searchText, result);
        if (!found) found = searchInQueue(counter1Queue, searchText, result);
        if (!found) found = searchInQueue(counter2Queue, searchText, result);
        if (!found) found = searchInQueue(counter3Queue, searchText, result);

        if (found) {
            JOptionPane.showMessageDialog(this, "Search Results:\n" + result.toString());
        } else {
            JOptionPane.showMessageDialog(this, "No customer found with the ID: " + searchText);
        }
    }

    private boolean searchInQueue(Queue<String> queue, String searchText, StringBuilder result) {
        for (String customer : queue) {
            if (customer.split(",")[0].trim().equals(searchText)) { // Assuming ID is the first field
                result.append(customer).append("\n");
                return true;
            }
        }
        return false;
    }
}