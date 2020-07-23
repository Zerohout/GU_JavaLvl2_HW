package lesson3;

import java.util.*;

public class Lesson3 {

    public void start() {
        demoTextArray();
        demoAddressBook();
    }

    private void demoTextArray() {
        String[] textArray = Helper.createTextList();
        ArrayList<String> textArrList = new ArrayList<>(Arrays.asList(textArray));
        ArrayList<String> uniqueValues = getUniqueValues(textArrList);

        System.out.println(String.format("Вся коллекция слов целиком:%nTotal size:%d %s%n",
                textArrList.size(), textArrList));
        System.out.println(String.format("Первый вариант получения списка уникальных значений (с помощью HashSet):%nArray size: %d %s",
                uniqueValues.size(), uniqueValues));
        calculateArrayWords(textArrList);
    }

    private ArrayList<String> getUniqueValues(List<String> array) {
        return new ArrayList<>(new HashSet<>(array));
    }

    private void calculateArrayWords(ArrayList<String> arrayList) {
        var operableList = new ArrayList<>(arrayList);
        var wordsCollection = new HashMap<String, Integer>();
        var uniqueArrayList = new ArrayList<String>();

        for (var i = 0; i < operableList.size(); i++) {
            var word = operableList.get(i);
            var lastObjIndex = operableList.lastIndexOf(word);
            var wordsCount = wordsCollection.containsKey(word) ? wordsCollection.get(word) + 1 : 1;
            if (lastObjIndex != i) {
                operableList.remove(lastObjIndex);
                i--;
            } else {
                uniqueArrayList.add(word);
            }
            wordsCollection.put(word, wordsCount);
        }

        System.out.println(String.format("Второй способ получения уникальных значений (в цикле):%nArray size: %d %s%n",
                uniqueArrayList.size(), uniqueArrayList));
        System.out.printf("Подсчет слов в списке:\n%s\n\n", wordsCollection);
    }

    private void demoAddressBook(){
        var addressBook = Helper.createAddressBook();
        System.out.println("Список телефонных номеров:");
        addressBook.printNumbers("Man");
        addressBook.printNumbers("Woman");
        addressBook.printNumbers("Human");
        addressBook.printNumbers("NotHuman");
        addressBook.printNumbers("Batman");
        addressBook.printNumbers("Superman");
    }
}
