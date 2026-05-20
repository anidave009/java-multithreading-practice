package miniBankProject;


import java.util.Random;
import java.util.concurrent.TimeUnit;

// ─────────────────────────────────────────
// 2. BANK CLASS — contains transfer logic
// ─────────────────────────────────────────
class Bank {
    private final Random random = new Random();

    public boolean transfer(Account from, Account to, int amount) {
        try {
            // Step 1: Try to lock the sender's account (wait up to 2 sec)
            if (from.lock.tryLock(2, TimeUnit.SECONDS)) {
                try {
                    System.out.println("  [" + Thread.currentThread().getName() + "] "
                            + "Locked " + from.getName() + ", trying to lock " + to.getName() + "...");

                    // Step 2: Try to lock the receiver's account (wait up to 2 sec)
                    if (to.lock.tryLock(2, TimeUnit.SECONDS)) {
                        try {
                            // Step 3: Both locks acquired — safe to transfer
                            System.out.println("  [" + Thread.currentThread().getName() + "] "
                                    + "Locked both. Transferring ₹" + amount
                                    + " from " + from.getName() + " to " + to.getName());

                            from.debit(amount);
                            to.credit(amount);

                            System.out.println("  [" + Thread.currentThread().getName() + "] "
                                    + "Done! " + from + " | " + to);
                            return true;

                        } finally {
                            to.lock.unlock(); // always release receiver's lock
                            System.out.println("  [" + Thread.currentThread().getName() + "] "
                                    + "Released lock on " + to.getName());
                        }
                    } else {
                        // Couldn't get receiver's lock in time
                        System.out.println("  [" + Thread.currentThread().getName() + "] "
                                + "Couldn't lock " + to.getName() + " — backing off...");
                    }

                } finally {
                    from.lock.unlock(); // always release sender's lock
                    System.out.println("  [" + Thread.currentThread().getName() + "] "
                            + "Released lock on " + from.getName());
                }

            } else {
                // Couldn't get sender's lock in time
                System.out.println("  [" + Thread.currentThread().getName() + "] "
                        + "Couldn't lock " + from.getName() + " — backing off...");
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return false; // transfer did not happen
    }


    // Retry transfer with random backoff to avoid livelock
    public void transferWithRetry(Account from, Account to, int amount) {
        int attempt = 1;

        while (true) {
            System.out.println("\n[" + Thread.currentThread().getName() + "] "
                    + "Attempt #" + attempt + ": ₹" + amount
                    + " from " + from.getName() + " → " + to.getName());

            boolean success = transfer(from, to, amount);

            if (success) {
                System.out.println("[" + Thread.currentThread().getName() + "] Transfer SUCCESS on attempt #" + attempt);
                return;
            }

            // Failed — wait a random time before retrying (prevents livelock)
            try {
                int waitMs = random.nextInt(100) + 50; // 50–150ms random wait
                System.out.println("[" + Thread.currentThread().getName() + "] Retrying in " + waitMs + "ms...");
                Thread.sleep(waitMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            attempt++;
        }
    }
}
