package lesson5.joinVersion;

import static lesson5.Helper.*;

public class JoinThreadsVersion {
    public void start() {
        System.out.println("===Запуск версии с использованием ожидания процессов с помощью метода join..===");
        simpleArrayFiller();
        MultiThreadArrayFiller();
        arrayNumber = 0;
    }

    private void MultiThreadArrayFiller() {
        prepareToWorkingWithArray();
        long startWorkingTime = System.currentTimeMillis();

        try {
            startMultiTreadingWork();
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }

        printTotalInfo(startWorkingTime);
        threadCount = 0;
        separate();
    }

    private void startMultiTreadingWork() throws InterruptedException {
        var t1 = new Thread(new MultiThreadArrayFiller(0,++threadCount));
        var t2 = new Thread(new MultiThreadArrayFiller(ARR_HALF_SIZE,++threadCount));
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
}
