import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Integer> arrayList = new MyArrayList<>();
        List<Integer> linkedList = new MyLinkedList<>();

        for (int i = 0; i < 30; i++) {
            arrayList.add(i);
            linkedList.add(i);
        }
        System.out.println(arrayList);
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        System.out.println(new MyArrayList<>(list));
        arrayList.remove(0);
        arrayList.addAll(list);
        System.out.println(arrayList.lastIndexOf(1));
        System.out.println(arrayList);
        System.out.println(linkedList);
        System.out.println(new MyLinkedList<>(list));
        linkedList.remove(0);
        linkedList.addAll(list);
        System.out.println(linkedList.lastIndexOf(1));
        System.out.println(linkedList);
        for (Integer integer : linkedList) {
            System.out.print(integer + ", ");
        }

    }
}
