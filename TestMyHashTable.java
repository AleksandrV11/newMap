package map;

public class TestMyHashTable {
    public static void main(String[] args) {
        MyHashTable<Integer, String> myHashTable = new MyHashTable();
        myHashTable.put(null, "000");
        myHashTable.put(1, "11");
        myHashTable.put(2, "22");
        myHashTable.put(3, "33");
        myHashTable.put(4, "44");
        myHashTable.put(5, "55");
        myHashTable.put(6, "66");
        myHashTable.put(7, "77");
        System.out.println(myHashTable);
        myHashTable.put(7, "88");
        System.out.println(myHashTable.get(7));
        System.out.println(myHashTable);
        System.out.println(myHashTable.remove(7));
        System.out.println(myHashTable);


    }
}
