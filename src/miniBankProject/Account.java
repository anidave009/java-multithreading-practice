package miniBankProject;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Random;

// ─────────────────────────────────────────
// 1. ACCOUNT CLASS
// ─────────────────────────────────────────
class Account {
    private final String name;
    private int balance;
    final ReentrantLock lock = new ReentrantLock(); // each account has its own lock

    public Account(String name, int initialBalance) {
        this.name = name;
        this.balance = initialBalance;
    }

    // deduct money — only called when lock is held
    public void debit(int amount) {
        balance -= amount;
    }

    // add money — only called when lock is held
    public void credit(int amount) {
        balance += amount;
    }

    public int getBalance() { return balance; }
    public String getName()  { return name; }

    @Override
    public String toString() {
        return name + "(₹" + balance + ")";
    }
}
