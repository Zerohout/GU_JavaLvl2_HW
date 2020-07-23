package lesson3;

class Helper {
    static AddressBook createAddressBook(){
        var out = new AddressBook();
        out.add("Man",1110);
        out.add("Woman",1111);
        out.add("Human",1211);
        out.add("NotHuman",3111);
        out.add("Man",1411);
        out.add("NotHuman",1151);
        out.add("Woman",1116);
        out.add("Man",1171);
        out.add("Woman",1811);
        out.add("Batman",9111);
        return out;
    }

    static String[] createTextList() {
        return new String[]{
                "apple",
                "pineapple",
                "banana",
                "lemon",
                "carrot",
                "apple",
                "orange",
                "potato",
                "onion",
                "banana",
                "tomato",
                "garlic",
                "pineapple",
                "orange",
                "onion",
                "apple",
                "onion",
                "garlic",
                "orange",
                "onion"
        };
    }
}
