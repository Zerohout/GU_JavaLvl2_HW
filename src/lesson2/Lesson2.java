package lesson2;

public class Lesson2 {


    public void start() {
        Helper.calculateArray(createCorrectArray());
        Helper.calculateArray(createIncorrectArray1());
        Helper.calculateArray(createIncorrectArray2());
    }

    private String[][] createCorrectArray() {
        return new String[][]{
                {"1", "two", "3", "4"},
                {"f1ve", "6", "7", "8"},
                {"9", "ten", "11", "12"},
                {"13", "14", "fifteen", "16"}
        };
    }

    private String[][] createIncorrectArray1() {
        return new String[][]{
                {"1", "two", "3", "4", "pop"},
                {"f1ve", "6", "7", "8"},
                {"9", "ten", "11", "12", "pip", "pup"},
                {"13", "14", "fifteen", "16", "crack"}
        };
    }

    private String[][] createIncorrectArray2() {
        return new String[][]{
                {"1", "two", "3", "4", "pop"},
                {"f1ve", "6", "7", "8"},
                {"9", "ten", "11", "12", "pip", "pup"},
                {"13", "14", "fifteen", "16", "crack"},
                {"17", "18", "nineteen", "20", "boom"}
        };
    }
}
