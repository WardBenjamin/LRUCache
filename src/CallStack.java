public class CallStack<T, U> {
    private class Node<T, U> {
        public Node<T, U> _next;
        public Node<T, U> _previous;
        public T _key;
        public U _value;
    }
}
