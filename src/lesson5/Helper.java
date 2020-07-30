package lesson5;

public class Helper {
    private final static int ARR_SIZE = 10000000;
    public final static int ARR_HALF_SIZE = ARR_SIZE / 2;
    public static int threadCount = 0;
    public static int arrayNumber = 0;
    public static float[] arr;

    public static void separate() {
        System.out.println("\n================================================================");
    }

    public static void simpleArrayFiller() {
        prepareToWorkingWithArray();
        long startWorkingTime = System.currentTimeMillis();

        for (var i = 0; i < ARR_SIZE; i++) {
            arr[i] = (float) (arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
        printTotalInfo(startWorkingTime);
        separate();
    }

    public static void prepareToWorkingWithArray() {
        System.out.printf("Начало работы с %d-м массивом%n", ++arrayNumber);

        arr = new float[ARR_SIZE];
        for (var i = 0; i < ARR_SIZE; i++) {
            arr[i] = 1;
        }
    }

    public static void printTotalInfo(long startWorkingTime) {
        System.out.printf("Проверка массива: arr[544]=%f,arr[77_289]=%f,arr[928_367]=%f,arr[6_732_456]=%f,arr[7_967_933]=%f,arr[9_999_999]=%f%n"
                , arr[544], arr[77_289], arr[928_367], arr[6_732_456], arr[7_967_933], arr[9_999_999]);
        System.out.printf("Работа со %d-м массивом завершена. Время работы метода: %sмс%n", arrayNumber, System.currentTimeMillis() - startWorkingTime);
    }
}
