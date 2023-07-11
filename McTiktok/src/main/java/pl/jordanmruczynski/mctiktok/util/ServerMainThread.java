package pl.jordanmruczynski.mctiktok.util;

import org.bukkit.Bukkit;
import pl.jordanmruczynski.mctiktok.McTiktok;

import java.util.concurrent.CountDownLatch;

public class ServerMainThread {

    /**
     * This subclass is used to start an action on the main thread
     * and wait for it to complete, while the scheduler thread is
     * blocked. This can be useful if you want to retrieve values
     * from the main thread and calculate with them on your async
     * thread.
     *
     */
    public static class WaitForCompletion<T> {

        // this latch is released when the operation on the main
        // thread is allowed to start
        private CountDownLatch startSignal;

        // this latch is released when the operation on the
        // main thread has completed and the async thread may continue
        private CountDownLatch doneSignal;

        private Retrievable<T> retrievable;

        // the result returned by the retrieve-method of Retrievable
        private Object result;

        private WaitForCompletion(Retrievable<T> retrievable) {
            this.retrievable = retrievable;
            this.result = null;
            this.startSignal = new CountDownLatch(1);
            this.doneSignal = new CountDownLatch(1);
        }

        /**
         * Creates a new instance of the class and performs the operations
         * on the main thread
         *
         * @param retrievable The code calculating and returning the result.
         * @return The result of the calculation
         */
        public static <T> T result(Retrievable<T> retrievable) {
            WaitForCompletion<T> task = new WaitForCompletion<T>(retrievable);
            return (T) task.catchResult();
        }

        /**
         * Starts the operation on the main thread and blocks the async thread.
         *
         * @return The result from the calculation on the main thread.
         */
        private Object catchResult() {

            // start the operation on the main thread, which is still blocked
            // as startSignal has not been released yet.
            Bukkit.getScheduler().scheduleSyncDelayedTask(McTiktok.getPlugin(McTiktok.class), () -> {
                try {
                    // wait for the start signal to be fired
                    this.startSignal.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // retrieve the result from the operation and release
                // the latch showing that the operation has completed
                this.result = this.retrievable.retrieve();
                this.doneSignal.countDown();
            });

            // release start signal so that the operation on the main thread can begin
            this.startSignal.countDown();


            // wait for the operation on the main thread to complete
            // and then return the result
            try {
                this.doneSignal.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return this.result;
        }

    }

    /**
     * This subclass is used to start actions from your async scheduler
     * threads on the server main thread, while your async thread is not
     * blocked and continues as normal.
     *
     */
    public static class RunParallel {

        /**
         * Immediately executes the given task on the server main thread
         * using the synchronous bukkit scheduler executor.
         *
         * @param runnable The code to execute on the main thread
         */
        public static void run(Runnable runnable) {

            // if we are already on the main thread, directly execute the operation instead of creating a new scheduler.
            if (Bukkit.isPrimaryThread()) {
                runnable.run();
                return;
            }

            Bukkit.getScheduler().scheduleSyncDelayedTask(McTiktok.getPlugin(McTiktok.class), runnable);
        }

    }

}
