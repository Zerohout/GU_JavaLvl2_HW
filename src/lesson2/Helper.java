package lesson2;

class Helper {
    private static int arrayCounter = 0;
    private static int operationErrorsCount = 0;
    private static final String separator = "\n***********************************************\n";

    static void calculateArray(String[][] arr) {
        checkArray(arr,4);

        System.out.printf("Start calculate array №%d.%n", arrayCounter);
        operationErrorsCount = 0;
        var sum = 0;
        for (var i = 0; i < arr.length; i++) {
            for (var j = 0; j < arr[i].length; j++) {
                sum += convertStringToInt(arr, i, j);
            }
        }

        System.out.printf("Calculate is over. Sum of values is equals %d. Errors during calculation - %d.%s", sum, operationErrorsCount, separator);
    }

    private static void checkArray(String[][] array, int neededSize) {
        System.out.printf("Start checking array №%d.%n", ++arrayCounter);
        var errorsCount = 0;
        var arraySize = checkTotalArraySize(array, neededSize);

        if (arraySize != neededSize) errorsCount++;

        errorsCount += checkCellArraySize(array, neededSize);

        System.out.printf("Checking is over. Array №%d - has %d errors.%s",
                arrayCounter, errorsCount, separator);
    }

    private static int checkTotalArraySize(String[][] array, int neededSize) {
        if (array.length != neededSize) {
            try {
                throw new MyArraySizeException(String.format("Array has size [%d]", array.length));
            } catch (MyArraySizeException e) {
                System.out.println(e.getMessage());
                return array.length;
            }
        }
        return neededSize;
    }

    private static int checkCellArraySize(String[][] array, int neededSize){
        var arraySize = array.length;
        var errorsCount = 0;
        for (var i = 0; i < arraySize; i++) {
            if (array[i].length != neededSize) {
                try {
                    throw new MyArraySizeException(String.format("The current array has an invalid size at index [%d] and is equal [%d]", i, array[i].length));
                }catch (MyArraySizeException e) {
                    System.out.println(e.getMessage());
                    errorsCount++;
                }
            }
        }
        return errorsCount;
    }

    private static int convertStringToInt(String[][] array, int firstIndex, int secondIndex) {
        var value = array[firstIndex][secondIndex];
        if (tryParse(value)) return Integer.parseInt(value);
        try {
            throw new MyArrayDataException(String.format("Error at Array[%d][%d] value is \"%s\".", firstIndex, secondIndex, value));
        } catch (MyArrayDataException e) {
            System.out.println(e.getMessage());
            operationErrorsCount++;
            return 0;
        }
    }

    private static boolean tryParse(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
