import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

class User {
    private String userId;
    private String pin;

    public User(String userId, String pin) {
        this.userId = userId;
        this.pin = pin;
    }

    public String getUserId() {
        return userId;
    }

    public String getPin() {
        return pin;
    }
}

class Account {
    private double balance;
    private List<String> transactions;

    public Account() {
        this.balance = 0.0;
        this.transactions = new ArrayList<>();
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        transactions.add("Deposited: $" + amount);
    }

    public void withdraw(double amount) throws Exception {
        if (amount > balance) {
            throw new Exception("Insufficient funds");
        }
        balance -= amount;
        transactions.add("Withdrew: $" + amount);
    }

    public void transfer(Account targetAccount, double amount) throws Exception {
        if (amount > balance) {
            throw new Exception("Insufficient funds");
        }
        balance -= amount;
        targetAccount.balance += amount;
        transactions.add("Transferred: $" + amount + " to " + targetAccount);
        targetAccount.transactions.add("Received: $" + amount + " from " + this);
    }

    public List<String> getTransactions() {
        return transactions;
    }
}

class ATM {
    private User user;
    private Account account;

    public ATM(User user, Account account) {
        this.user = user;
        this.account = account;
    }

    public boolean authenticate(String userId, String pin) {
        return user.getUserId().equals(userId) && user.getPin().equals(pin);
    }

    public void showMenu() {
        System.out.println("\n1. Transaction History");
        System.out.println("2. Withdraw");
        System.out.println("3. Deposit");
        System.out.println("4. Transfer");
        System.out.println("5. Quit");
    }

    public void transactionHistory() {
        List<String> transactions = account.getTransactions();
        if (transactions.isEmpty()) {
            System.out.println("No transactions yet.");
        } else {
            for (String transaction : transactions) {
                System.out.println(transaction);
            }
        }
    }

    public void withdraw(double amount) {
        try {
            account.withdraw(amount);
            System.out.println("Withdrawal successful. New balance: $" + account.getBalance());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void deposit(double amount) {
        account.deposit(amount);
        System.out.println("Deposit successful. New balance: $" + account.getBalance());
    }

    public void transfer(Account targetAccount, double amount) {
        try {
            account.transfer(targetAccount, amount);
            System.out.println("Transfer successful. New balance: $" + account.getBalance());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

class Bank {
    private HashMap<String, User> users;
    private HashMap<String, Account> accounts;

    public Bank() {
        users = new HashMap<>();
        accounts = new HashMap<>();
    }

    public void addUser(String userId, String pin) {
        User user = new User(userId, pin);
        Account account = new Account();
        users.put(userId, user);
        accounts.put(userId, account);
    }

    public Account getAccount(String userId) {
        return accounts.get(userId);
    }

    public User getUser(String userId) {
        return users.get(userId);
    }
}

public class ATMInterface {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Bank bank = new Bank();
        bank.addUser("user1", "1234");
        bank.addUser("user2", "5678");

        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine();

        User user = bank.getUser(userId);
        Account account = bank.getAccount(userId);

        if (user == null || !new ATM(user, account).authenticate(userId, pin)) {
            System.out.println("Authentication failed");
            return;
        }

        ATM atm = new ATM(user, account);
        while (true) {
            atm.showMenu();
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();
            if (choice.equals("1")) {
                atm.transactionHistory();
            } else if (choice.equals("2")) {
                System.out.print("Enter amount to withdraw: ");
                double amount = Double.parseDouble(scanner.nextLine());
                atm.withdraw(amount);
            } else if (choice.equals("3")) {
                System.out.print("Enter amount to deposit: ");
                double amount = Double.parseDouble(scanner.nextLine());
                atm.deposit(amount);
            } else if (choice.equals("4")) {
                System.out.print("Enter target user ID: ");
                String targetUserId = scanner.nextLine();
                Account targetAccount = bank.getAccount(targetUserId);
                if (targetAccount == null) {
                    System.out.println("Target account not found");
                    continue;
                }
                System.out.print("Enter amount to transfer: ");
                double amount = Double.parseDouble(scanner.nextLine());
                atm.transfer(targetAccount, amount);
            } else if (choice.equals("5")) {
                System.out.println("Goodbye!");
                break;
            } else {
                System.out.println("Invalid choice");
            }
        }
        scanner.close();
    }
}
