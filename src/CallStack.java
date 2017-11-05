import java.util.HashMap;

public class CallStack<T> {
    private class Node {
        Node _next;
        Node _previous;
        T _key;

        Node(Node next, Node previous, T key) {
            _next = next;
            _previous = previous;
            _key = key;
        }
    }

    private int _capacity;
    private Node _leastRecentlyUsed;
    private Node _mostRecentlyUsed;
    private HashMap<T, Node> _queue;

    CallStack(int capacity) {
        _capacity = capacity;

        _queue = new HashMap<>();
        _leastRecentlyUsed = null;
        _mostRecentlyUsed = null;
    }

    public void addCall(T key) {
        if (_queue.size() == 0) {
            Node newCall = new Node(null, null, key);

            _leastRecentlyUsed = newCall;
            _queue.put(key, newCall);
        }
        else if (_queue.size() < _capacity) {
            Node newCall = new Node(null, _mostRecentlyUsed, key);

            _mostRecentlyUsed._next = newCall;
            _mostRecentlyUsed = newCall;

            _queue.put(key, newCall);
        }
        else {
            if(key == _mostRecentlyUsed._key) {
                assert true;
            }
            else if (key == _leastRecentlyUsed._key) {
                Node node = _queue.get(key);
                Node newLRU = node._next;

                node._previous = _mostRecentlyUsed;
                node._next = null;
                _mostRecentlyUsed = node;

                newLRU._previous = null;
                _leastRecentlyUsed = newLRU;
            }
            else {
                Node node = _queue.get(key);

                Node previous = node._previous;
                Node next = node._next;

                node._previous = _mostRecentlyUsed;
                node._next = null;

                next._previous = previous;
                previous._next = next;

                _mostRecentlyUsed = node;
            }
        }
    }

    public T addAndEvict(T key) {
        T keyToDelete = _leastRecentlyUsed._key;

        _leastRecentlyUsed = _leastRecentlyUsed._next;
        addCall(key);

        return keyToDelete;
    }
}
