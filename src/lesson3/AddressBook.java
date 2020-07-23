package lesson3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddressBook {
    private Map<String, ArrayList<Integer>> personsPhones = new HashMap<>();

    public void add(String name, Integer phone) {
        if (personsPhones.containsKey(name)) {
            personsPhones.get(name).add(phone);
            return;
        }
        var temp = new ArrayList<Integer>();
        temp.add(phone);
        personsPhones.put(name, temp);
    }

    public ArrayList<Integer> getPhoneNumber(String name) {return personsPhones.get(name);}

    public void printNumbers(String name) {
        var phones = getPhoneNumber(name);
        String message = phones != null
                ? String.format("At name \"%s\" we have %d phone numbers: %s", name, phones.size(), phones)
                : String.format("We don't have %s's phone numbers, sorry.%n", name);
        System.out.println(message);
    }
}
