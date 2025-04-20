import java.util.ArrayList;

public class Stack <T>{
    T obj;

    Stack(T obj) {
        this.obj = obj;
    }

    public T getObject() {
        return this.obj;
    }

    public static <T> void push(ArrayList<T> array, T input) {
        array.add(input);
    }

    public static <T> T pop(ArrayList<T> array) {
        int n = array.size();
        return array.remove(n - 1);
    }

    public static <T> T peek(ArrayList<T> array) {
        int n = array.size();
        T t = array.remove(n - 1);
        array.add(t);
        return t;
    }

    public static <T> void print(ArrayList<T> array, int i) {
        System.out.println(array.get(i));
    }
}