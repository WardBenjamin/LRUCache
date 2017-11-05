import java.util.HashMap;

public class CallStack<T, U> {
    private class Node<T, U> {
        public Node<T, U> _next;
        public Node<T, U> _previous;
        public T _key;
        public U _value;
    }

    private int _capacity;
    private Node _leastRecentlyUsed;
    private Node _mostRecentlyUsed;
    private HashMap<T, Node<T, U>> _queue;

    CallStack(int capacity) {
        _capacity = capacity;

        _queue = new HashMap<>();
    }
}
