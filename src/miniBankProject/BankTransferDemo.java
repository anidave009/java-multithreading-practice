package miniBankProject;

// ─────────────────────────────────────────
// 3. MAIN — threads simulate transfers
// ─────────────────────────────────────────
public class BankTransferDemo {

    public static void main(String[] args) throws InterruptedException {

        // Create two accounts
        Account accountA = new Account("Alice", 1000);
        Account accountB = new Account("Bob",   1000);

        Bank bank = new Bank();

        System.out.println("=== Initial Balances ===");
        System.out.println(accountA);
        System.out.println(accountB);
        System.out.println("========================\n");

        // Thread 1: Alice → Bob (₹300)
        // Thread 2: Bob → Alice (₹200)
        // These are in OPPOSITE directions — classic deadlock scenario without tryLock
        Thread t1 = new Thread(() -> {
            bank.transferWithRetry(accountA, accountB, 300);
        }, "Thread-A→B");

        Thread t2 = new Thread(() -> {
            bank.transferWithRetry(accountB, accountA, 200);
        }, "Thread-B→A");

        t1.start();
        t2.start();

        // Wait for both threads to finish
        t1.join();
        t2.join();

        // Final balances
        // Alice: 1000 - 300 + 200 = 900
        // Bob:   1000 + 300 - 200 = 1100
        System.out.println("\n=== Final Balances ===");
        System.out.println(accountA);
        System.out.println(accountB);
        System.out.println("Expected → Alice: ₹900 | Bob: ₹1100");
    }
}
