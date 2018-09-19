import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class PhilosopherProblem {

    public static class Philosopher implements Runnable {

        private final List<Semaphore> semaphores;
        private int id;

        public Philosopher(List<Semaphore> semaphores, int id) {
            this.semaphores = semaphores;
            this.id = id;
        }

        @Override
        public void run() {
            while (true) {
                thinking();
                if (id % 2 == 0) {
                    takeRightFork();
                    takeLeftFork();
                } else {
                    takeLeftFork();
                    takeRightFork();
                }
                eating();
                if (id % 2 == 0) {
                    putRightFork();
                    putLeftFork();
                } else {
                    putLeftFork();
                    putRightFork();
                }
            }
        }

        private void takeRightFork() {
            int idOfRight = (id + 1) % semaphores.size();
            try {
                semaphores.get(idOfRight).acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void takeLeftFork() {
            try {
                semaphores.get(id).acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void putRightFork() {
            int idOfRight = (id + 1) % semaphores.size();
            semaphores.get(idOfRight).release();
        }

        private void putLeftFork() {
            semaphores.get(id).release();
        }


        private void thinking() {
            System.out.printf("%d philosopher is thinking....\n", id);
        }

        private void eating() {
            System.out.printf("%d philosopher is eating....\n", id);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("%d philosopher is ok....\n", id);
        }
    }


    public static void main(String[] args) {
        List<Thread> threads = new ArrayList<>();
        List<Semaphore> semaphores = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            semaphores.add(new Semaphore(1));
            threads.add(new Thread(new Philosopher(semaphores, i)));
        }

        for (Thread t : threads) {
            t.start();
        }
    }
}