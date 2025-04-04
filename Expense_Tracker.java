import java.io.*;
import java.util.*;
public class Expense_Tracker{
    private static final String FILE_NAME = "expenses.txt";
    private static List<Expense> expenses = new ArrayList();
    public static void main(String[] args) {
        loadExpenses(); // Load existing expenses from the file
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            saveExpenses();
            System.out.println("\n********* Personal Expense Tracker *********");
            System.out.println("1. Add Expense");
            System.out.println("2. View Expenses");
            System.out.println("3. Edit Expense");
            System.out.println("4. Delete Expense");
            System.out.println("5. Calculate Total Expenses");
            System.out.println("6. View Summary Report");
            System.out.println("7. Exit");
            System.out.print("Enter your choice:- ");
            
            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input! Please Enter a number:-");
                scanner.next(); 
            }
            choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    addExpense(scanner);
                    break;
                case 2:
                    view(scanner);
                    break;
                case 3:
                    editExpense(scanner);
                    break;
                case 4:
                    deleteExpense(scanner);
                    break;
                case 5: 
                    calculateTotal();
                    break;
                case 6:
                    viewSummaryReport(scanner);
                    break;
                case 7:
                {
                    saveExpenses(); // Save data before exiting
                    System.out.println("Saved Data Successfully From File.");
                    break;
                }
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 7);
        scanner.close();
    }
    
    private static void addExpense(Scanner scanner) {
        System.out.print("Enter date (YYYY-MM-DD):- ");
        String date = scanner.nextLine();
        if(date_checker(date)){
            System.out.print("Enter amount (Rs.):- ");
            double amount;
            while (!scanner.hasNextDouble()) {
                System.out.println("Invalid input! Please Enter a valid amount.");
                scanner.next(); 
            }
            amount = scanner.nextDouble();
            scanner.nextLine(); 

            System.out.print("Enter category:- ");
            String category = scanner.nextLine();

            System.out.print("Enter payment mode:- ");
            String paymentMode = scanner.nextLine();

            System.out.print("Enter description:- ");
            String description = scanner.nextLine();

            Expense expense = new Expense(date, amount, category, paymentMode, description);
            expenses.add(expense);
            System.out.println("Expense added successfully!");
            Collections.sort(expenses, new Comparator<Expense>() {
                public int compare(Expense o1, Expense o2) {
                    return o1.getDate().compareTo(o2.getDate());
                }
            });

        }
    }

    private static void view(Scanner scanner) {
        System.out.println("\n---- CHOOSE ONE OF THE VIEW OPTION ----");
        System.out.println("1. View All Expenses");
        System.out.println("2. View Expenses By Specific Category");
        System.out.println("3. View Expenses By Specific Date");
        System.out.println("4. View Highest Expense");
        System.out.println("5. View All Expenses Sorted By Amount");
        System.out.print("Enter your choice:- ");
        int choice;
        try{
            choice = scanner.nextInt();
            scanner.nextLine(); 
            switch (choice) {
                case 1:
                    viewExpenses();
                    break;
                case 2:
                    filterByCategory(scanner);
                    break;
                case 3: 
                    viewExpensesByDate(scanner);
                    break;
                case 4:
                    viewHighestExpense();
                    break;
                case 5:
                    sortExpensesByAmount(scanner);
                    break;
                default:
                    System.out.println("Invalid Choice");
            }
        }
        catch(InputMismatchException e){
            System.out.println("Invalid Input");
        } 
    }
    private static void viewExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }
        System.out.println("\n=================================== Expense List ================================== ");
        System.out.println("S. Date\t\t\tAmount\t\t\tCategory\tPayment Mode\tDescription");
        for (int i = 0; i < expenses.size(); i++) {
            System.out.println((i + 1) + ". " + expenses.get(i));
        }
    }
    private static void filterByCategory(Scanner scanner) {
        System.out.print("Enter category to filter: ");
        String category = scanner.nextLine();
        System.out.println("\n=== Expenses in Category: " + category + " ===");
        boolean found = false;
        System.out.println("Date\t\t\tAmount\t\t\tCategory\tPayment Mode\tDescription");
        for (Expense e : expenses) {
            if (e.getCategory().equalsIgnoreCase(category)) {
                System.out.println(e);
                found = true;
                
            }
        }
        if (!found) {
            System.out.println("No expenses found in this category.");
        }
    }
    private static void viewExpensesByDate(Scanner scanner) {
        System.out.print("Enter date (YYYY-MM-DD) to view expenses: ");
        String date = scanner.nextLine();
        System.out.println("Date\t\t\tAmount\t\t\tCategory\tPayment Mode\tDescription");
        if (date_checker(date)){
            boolean found = false;
            for (Expense expense : expenses) {
                if (expense.getDate().equals(date)) {
                    System.out.println(expense);
                    found = true;
                }
            }
            if (!found) {
                System.out.println("No expenses found for this date.");
            }
        }
    }
    private static void viewHighestExpense() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses to display.");
        } else {
            System.out.println("                  Date\t\t\tAmount\t\t\tCategory\tPayment Mode\tDescription");
            Expense highest = Collections.max(expenses, Comparator.comparingDouble(exp -> exp.getAmount()));
            System.out.println("Highest expense:- " + highest);
        }
    }
    private static void sortExpensesByAmount(Scanner scanner) {
        Collections.sort(expenses, Comparator.comparingDouble(o1 -> o1.getAmount()));
        System.out.println("Expenses sorted by amount.");
        viewExpenses();
        Collections.sort(expenses, new Comparator<Expense>() {
            public int compare(Expense o1, Expense o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
    }
    private static void calculateTotal() {
        double total = 0;
        for (Expense expense : expenses) {
            total += expense.getAmount();
        }
        System.out.println("Total Expenses:- Rs." + total);
    }
    private static void editExpense(Scanner scanner) {
        viewExpenses();
        try{
            System.out.print("Enter the number of the expense to edit: ");
            int index = scanner.nextInt() - 1;
            scanner.nextLine(); 
            if (index < 0 || index >= expenses.size()) {
                System.out.println("Invalid selection!");
                return;
            }
            Expense selectedExpense = expenses.get(index);
            System.out.print("Enter new date (YYYY-MM-DD) existing (" + selectedExpense.getDate() + "): ");
            String newDate = scanner.nextLine();
            if(date_checker(newDate)){
                System.out.print("Enter new amount or press Enter to keep existing (Rs." + selectedExpense.getAmount() + "): ");
                String amountInput = scanner.nextLine();
                System.out.print("Enter new category or press Enter to keep existing (" + selectedExpense.getCategory() + "): ");
                String newCategory = scanner.nextLine();
                System.out.print("Enter new payment mode or press Enter to keep existing (" + selectedExpense.getPaymentMode() + "): ");
                String newPaymentMode = scanner.nextLine();
                System.out.print("Enter new description or press Enter to keep existing (" + selectedExpense.getDescription() + "): ");
                String newDescription = scanner.nextLine();

                if (!newDate.isEmpty()) selectedExpense.setDate(newDate);
                if (!amountInput.isEmpty()) selectedExpense.setAmount(Double.parseDouble(amountInput));
                if (!newCategory.isEmpty()) selectedExpense.setCategory(newCategory);
                if (!newPaymentMode.isEmpty()) selectedExpense.setPaymentMode(newPaymentMode);
                if (!newDescription.isEmpty()) selectedExpense.setDescription(newDescription);
                System.out.println("Expense updated successfully!");
            }
        }
        catch(InputMismatchException e){
            System.out.println("Error In Edit Expense Occur");
        }
    }

    private static void deleteExpense(Scanner scanner) {
        viewExpenses();
        try{
            System.out.print("Enter the number of the expense to delete: ");
            int index = scanner.nextInt() - 1;
            scanner.nextLine(); 
            if (index < 0 || index >= expenses.size()) {
                System.out.println("Invalid selection!");
                return;
            }
            expenses.remove(index);
            System.out.println("Expense deleted successfully!");
        }
        catch(InputMismatchException e){
            System.out.println("Error In Deleting Expense");
        }
    }
    private static boolean date_checker(String date){
        if (date.length()<10 || date.length()>10){
            System.out.println("Incorrect Date Format");
            return false;
        }
        else if (date.charAt(4)!='-'|| date.charAt(7)!='-'){
            System.out.println("Incorrect Date Format:");
            return false;
        }
        String[] parts = date.split("-");
        int year,month,day;
        try{
        year = Integer.parseInt(parts[0]);
        month = Integer.parseInt(parts[1]);
        day = Integer.parseInt(parts[2]);
        if(month==2){
            if(year%4==0){
                if(day>0 && day<30)
                    return true;
                else{
                    System.out.println("Incorrect Date");
                    return false;
                }
            }
            if(day>0 && day<29)
                return true;
            else{
                System.out.println("Incorrect Date");
                return false;
            }
        }
        if( month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12 ){
            if(day>0 && day<32)
                return true;
            else{
                System.out.println("Incorrect Date");
                return false;
            }
        }
        if( month == 4 || month == 6 || month == 9 || month == 11  ){
            if(day>0 && day<31)
                return true;
            else{
                System.out.println("Incorrect Date");
                return false;
            }
        }
        return false;
        }
        catch(NumberFormatException e){
            System.out.println("Invalid Date");
            return false;
        }
        
    }
    private static void viewSummaryReport(Scanner scanner) {
        System.out.println("\n=== Summary Report ===");
        System.out.println("1. Monthly Summary");
        System.out.println("2. Yearly Summary");
        System.out.print("Enter your choice: ");
        
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input! Please enter a number.");
            scanner.next(); 
        }
        int choice = scanner.nextInt();
        scanner.nextLine(); 

        switch (choice) {
            case 1 -> viewMonthlySummary();
            case 2 -> viewYearlySummary();
            default -> System.out.println("Invalid choice! Please try again.");
        }
    }

    private static void viewMonthlySummary() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }

        System.out.println("\n========= Monthly Summary =========");
        String currentMonth = "";
        double totalAmount = 0.0;

        for (Expense e : expenses) {
            String month = e.getDate().substring(0, 7); // YYYY-MM
            if (!month.equals(currentMonth)) {
                if (!currentMonth.isEmpty()) {
                    System.out.println(currentMonth + ": Rs." + totalAmount);
                }
                currentMonth = month;
                totalAmount = 0.0;
            }
            totalAmount += e.getAmount();
        }

        if (!currentMonth.isEmpty()) {
            System.out.println(currentMonth + ": Rs." + totalAmount);
        }
    }

    private static void viewYearlySummary() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }

        System.out.println("\n========= Yearly Summary =========");
        String currentYear = "";
        double totalAmount = 0.0;

        for (Expense e : expenses) {
            String year = e.getDate().substring(0, 4); // YYYY
            if (!year.equals(currentYear)) {
                if (!currentYear.isEmpty()) {
                    System.out.println(currentYear + ": Rs." + totalAmount);
                }
                currentYear = year;
                totalAmount = 0.0;
            }
            totalAmount += e.getAmount();
        }

        if (!currentYear.isEmpty()) {
            System.out.println(currentYear + ": Rs." + totalAmount);
        }
    }
    
    private static void saveExpenses() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Expense e : expenses) {
                writer.write(e.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving expenses: " + e.getMessage());
        }
    }

    private static void loadExpenses() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    Expense expense = Expense.fromFileString(line);
                    expenses.add(expense);
                } catch (Exception e) {
                    System.out.println("Skipping invalid data: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading expenses: " + e.getMessage());
        }
    }
}
class Expense {
    private String date;
    private double amount;
    private String category;
    private String paymentMode;
    private String description;

    public Expense(String date, double amount, String category, String paymentMode, String description) {
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.paymentMode = paymentMode;
        this.description = description;
    }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    @Override
    public String toString() {
        return date + "\t\tRS." + amount + "\t\t" + category + "\t\t" + paymentMode + "\t\t" + description;
    }
    public String toFileString() {
        return date + "," + amount + "," + category + "," + paymentMode + "," + description;
    }
    public static Expense fromFileString(String fileString) {
        String[] parts = fileString.split(",");
        return new Expense(parts[0], Double.parseDouble(parts[1]), parts[2], parts[3], parts[4]);
    }
}
