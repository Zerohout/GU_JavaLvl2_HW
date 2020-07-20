package lesson2;

class MyArraySizeException extends Exception {
    MyArraySizeException(String message) {
        super("Error array size. Need Array[4][4]. " + message);
    }
}
