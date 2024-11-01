package map;

import java.util.Arrays;

public class MyHashTable<K, V> implements MyMap<K, V> {
    private int size = 16;
    Node[] values;

    public MyHashTable() {
        values = new Node[size];
    }

    public class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;
        private int hashcode;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    @Override
    public void put(K key, V value) {

        if (checkForRewrite()) {                       //достиг 0,75 розмера нужна перезапись
            rewrite();                          // перезапись
        } else {
            if (key == null) {       //треба записати під 0 інд
                Node<K, V> node = new Node<>(key, value);
                values[0] = node;
                return;
            }
            Node<K, V> node = new Node<>(key, value);
            Node nodeTemp = values[ind(key)];
            if (nodeTemp == null) { // бакет пуст присвоили ноду индексу
                values[ind(key)] = node;
                return;
            }
            if (nodeTemp != null) { //бакет не пуст
                while (nodeTemp != null) {
                    if (nodeTemp.key.hashCode() == node.key.hashCode() && nodeTemp.equals(node)) { //хеш та иклс однаковий Koliziy
                        node.next = nodeTemp.next;     //обьекти однак
                        nodeTemp = node;               //перезапись
                        values[ind(key)] = nodeTemp;
                        return;
                    }else if (nodeTemp.key.hashCode() == node.key.hashCode() && !nodeTemp.equals(node)){
                        values[ind(key)].next = node;//хешкод разн
                        return;
                    }
                    nodeTemp = nodeTemp.next;
                }
            }
        }
    }

    public String toString() {
        return "MyHashTable{values=" + Arrays.toString(values) + "}";
    }


    @Override
    public V get(K key) {
        if (key == null) {
            if (values[0] == null) {
                return null;
            } else {
                return (V) values[0].value;
            }
        } else if (ind(key) > values.length - 1) {
            return null;
        } else {
            Node<K, V> nodeTemp = values[ind(key)];
            while (nodeTemp != null) {
                if (nodeTemp.key == key) {
                    return nodeTemp.value;
                }
                nodeTemp = nodeTemp.next;
            }
        }
        return null;
    }

    @Override
    public V remove(K key) {
        if (key == null) {
            if (values[0] == null) {
                return null;
            } else {
                Node<K, V> nodeTemp = values[0];
                values[0] = null;
                return nodeTemp.value;
            }
        } else if (ind(key) > values.length - 1) {
            return null;
        } else {
            if (sizeBucket(ind(key)) < 1) {
                Node<K, V> nodeTemp = values[ind(key)];
                values[ind(key)] = null;
                return nodeTemp.value;
            }
            Node<K, V> nodeTemp = values[ind(key)];
            Node<K, V> node;
            while (nodeTemp != null) {
                node = nodeTemp;
                if (nodeTemp.key == key) {
                    node.next = nodeTemp.next;
                    values[ind(key)] = node;
                    return nodeTemp.value;
                }
                nodeTemp = nodeTemp.next;
            }
        }
        return null;
    }

    public int sizeBucket(int ind) {
        int count = 0;
        while (values[ind].next != null) {
            count++;
            values[ind] = values[ind].next;
        }
        return count;
    }

    public Node[] bucketNode(int ind) {             //повертає массив нод з бакета
        Node<K, V>[] temp = new Node[sizeBucket(ind)];
        int count = 0;
        while (values[ind] != null) {
            temp[count] = values[ind];
            values[ind] = values[ind].next;
            count++;
        }
        return null;
    }

    public int filledBuckets() {         //кількість заповнених бакетов
        int sum = 0;
        for (int i = 0; i < values.length; i++) {
            if (values[i] != null) {
                sum++;
            }
        }
        return sum;
    }

    public int ind(K key) {          //визначення индексу (це залишок при діленні хешкода на размер масива
        int ind = key.hashCode() % values.length;
        return key.hashCode() % values.length;
    }

    public boolean isEmpty() {              //якщо щось є то повертає true іначе false
        boolean proverka = false;
        Node[] values1 = values;
        for (Node node : values1) {
            if (node.value != null) {
                proverka = true;
                break;
            }
        }
        return proverka;
    }

    public boolean checkForRewrite() {    //якщо достіг то повертає тру потрібно перезаписувати
        if (filledBuckets() == values.length * 0.75) {
            return true;
        }
        return false;
    }

    public int increaseSize(int size) {    //збільшення розміру
        return size * 2;
    }

    public Node[] increasingArray() {
        int tempSize = increaseSize(size);   // збільшили розміру 2 рази
        Node[] temp = new Node[tempSize];
        for (int i = 0; i < values.length; i++) {
            temp[i] = values[i];
        }
        size = tempSize;
        return temp;
    }

    public void rewrite() {          //перезапісь масіва
        Node[] arrayIncreases = values;
        values = new Node[arrayIncreases.length * 2];
        for (int i = 0; i < arrayIncreases.length; i++) {
            if (arrayIncreases[i] == null) {
                continue;
            }
            Node<K, V> nodeTemp = arrayIncreases[i];
            while (nodeTemp.next != null) {
                put(nodeTemp.key, nodeTemp.value);
                nodeTemp = nodeTemp.next;
            }
            put(nodeTemp.key, nodeTemp.value);
        }
        if (checkForRewrite()) {
            rewrite();
        }
    }
}
