package lesson2;

class MyArrayDataException extends Exception {
    MyArrayDataException(String message) {
        super(String.format("Error when trying to convert text to a number. %s",message));
    }
}
