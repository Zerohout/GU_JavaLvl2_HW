package lesson5.syncVersion;

import static lesson5.Helper.*;

public class SynchronizedVersion {
    private Object monitor1 = new Object();
    private Object monitor2 = new Object();

    public void start() {
        System.out.println("===Запуск версии с синхронизацией объектов...===");
        simpleArrayFiller();
        MultiThreadArrayFiller();
        arrayNumber = 0;
    }

    private void MultiThreadArrayFiller() {
        prepareToWorkingWithArray();
        long startWorkingTime = System.currentTimeMillis();

        new Thread(() -> multiArrayFiller(0, 0)).start();
        new Thread(() -> multiArrayFiller(ARR_HALF_SIZE, 1)).start();

        synchronized (monitor1) {
            synchronized (monitor2) {
                printTotalInfo(startWorkingTime);
            }
        }
        threadCount = 0;
        separate();
    }

    private void multiArrayFiller(int copyStartIndex, int monitorNum) {
        synchronized (monitorNum == 0 ? monitor1 : monitor2) {
            var threadNumber = ++threadCount;
            System.out.printf("Начало работы %d потока%n", threadNumber);
            var array = new float[ARR_HALF_SIZE];
            System.arraycopy(arr, copyStartIndex, array, 0, ARR_HALF_SIZE);

            for (var i = 0; i < array.length; i++) {
                array[i] = (float) (array[i] * Math.sin(0.2f + (i + copyStartIndex) / 5) * Math.cos(0.2f + (i + copyStartIndex) / 5) * Math.cos(0.4f + (i + copyStartIndex) / 2));
            }

            System.arraycopy(array, 0, arr, copyStartIndex, ARR_HALF_SIZE);
            System.out.printf("Окончание работы %d потока%n", threadNumber);
        }
    }
}
