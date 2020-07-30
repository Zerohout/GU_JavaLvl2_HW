package lesson5.joinVersion;

import static lesson5.Helper.*;

public class MultiThreadArrayFiller implements Runnable {
    private int copyArrayIndex;
    private int threadNumber;

    public MultiThreadArrayFiller(int copyStartIndex, int threadNumber) {
        this.copyArrayIndex = copyStartIndex;
        this.threadNumber = threadNumber;
    }

    @Override
    public void run() {
        System.out.printf("Начало работы %d потока%n", threadNumber);
        var array = new float[ARR_HALF_SIZE];
        System.arraycopy(arr, copyArrayIndex, array, 0, ARR_HALF_SIZE);

        for (var i = 0; i < array.length; i++) {
            array[i] = (float) (array[i] * Math.sin(0.2f + (i + copyArrayIndex) / 5) * Math.cos(0.2f + (i + copyArrayIndex) / 5) * Math.cos(0.4f + (i + copyArrayIndex) / 2));
        }
        System.arraycopy(array, 0, arr, copyArrayIndex, ARR_HALF_SIZE);
        System.out.printf("Окончание работы %d потока%n", threadNumber);
    }
}
