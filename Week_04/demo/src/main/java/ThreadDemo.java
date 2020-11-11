/*
 * 思考有多少种方式，在 main 函数启动一个新线程，运行一个方法，拿到这个方法的返回值后，退出主线程？
 *  */

import sun.awt.windows.ThemeReader;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

public class ThreadDemo {

    public static void main(String[] args) {
        threadDemo1();
        threadDemo2();
        threadDemo3();
        threadDemo4();
        threadDemo5();
        threadDemo6();
        threadDemo7();
        threadDemo8();
        threadDemo9();
        threadDemo10();


    }

    public static int threadDemo1() {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        Future<Integer> result = executorService.submit(() -> sum());
        try {
            return result.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
        return 0;
    }

    public static int threadDemo2() {
        FutureTask<Integer> futureTask = new FutureTask<Integer>(() -> sum());
        try {
            new Thread(futureTask).start();
            return (Integer) futureTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static int threadDemo3() {
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> sum());
        try {
            return completableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static int threadDemo4() {
        AtomicInteger atomicInteger = new AtomicInteger();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        new Thread(() -> {
            atomicInteger.set(sum());
            countDownLatch.countDown();
        }).start();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return atomicInteger.get();
    }

    public static int threadDemo5() {
        Object object = new Object();
        AtomicInteger atomicInteger = new AtomicInteger();
        Thread thread = new Thread(() -> {
            synchronized (object) {
                atomicInteger.set(sum());
                object.notify();
            }
        });
        thread.start();
        synchronized (object) {
            if (atomicInteger.get() == 0) {
                try {
                    object.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return atomicInteger.get();
        }
    }

    public static int threadDemo6() {
        AtomicInteger atomicInteger = new AtomicInteger();
        new Thread(() -> {
            atomicInteger.set(sum());
        }).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return atomicInteger.get();
    }

    public static int threadDemo7() {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        AtomicInteger atomicInteger = new AtomicInteger();
        Thread thread = new Thread(() -> {
            lock.lock();
            try {
                atomicInteger.set(sum());
            } catch (Exception ignored) {
            } finally {
                condition.signal();
                lock.unlock();
            }
        });
        thread.start();
        lock.lock();
        if (atomicInteger.get() == 0) {
            try {
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
        return atomicInteger.get();
    }

    public static int threadDemo8() {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        AtomicInteger atomicInteger = new AtomicInteger();
        Thread thread = new Thread(() -> {
            try {
                atomicInteger.set(sum());
                cyclicBarrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        return atomicInteger.get();
    }

    public static int threadDemo9() {
        Semaphore semaphore = new Semaphore(0);
        AtomicInteger atomicInteger = new AtomicInteger();
        Thread thread = new Thread(() -> {
            try {
                atomicInteger.set(sum());
                semaphore.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return atomicInteger.get();
    }

    public static int threadDemo10() {
        final boolean[] flag = {false};
        class CalcTask implements Runnable {
            private volatile int value;

            @Override
            public void run() {
                value = sum();
                flag[0] = true;
            }

            public int getValue() {
                return value;
            }
        }
        CalcTask task = new CalcTask();
        new Thread(task).start();
        while (!flag[0]) {
            Thread.yield();
        }
        return task.getValue();
    }

    private static int sum() {
        System.out.println("Thread Name ----->" + Thread.currentThread().getName());

        return fibo(36);
    }

    private static int fibo(int a) {
        if (a < 2)
            return 1;
        int i = 1, j = 1;
        while (a-- > 1) {
            int sum = i + j;
            j = i;
            i = sum;
        }
        return i;
    }
}
