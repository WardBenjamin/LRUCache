import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

        public T getNextKey() {
            if(_next != null) {
                return _next._key;
            }
            else {
                return null;
            }
        }

        public T getPrevKey() {
            if(_previous != null) {
                return _previous._key;
            }
            else {
                return null;
            }
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
        final Node node = _queue.get(key);

        if (_queue.size() == 0) {
            Node newCall = new Node(null, null, key);

            _leastRecentlyUsed = newCall;
            _mostRecentlyUsed = newCall;
            _queue.put(key, newCall);
        }
        else if (_queue.size() < _capacity && (node == null)) {
            Node newCall = new Node(null, _mostRecentlyUsed, key);

            _mostRecentlyUsed._next = newCall;
            _mostRecentlyUsed = newCall;

            _queue.put(key, newCall);
        }
        else {
            if(key.equals(_mostRecentlyUsed._key)) {
                assert true;
            }
            else if (key.equals(_leastRecentlyUsed._key)) {
                Node newLRU = node._next;

                node._previous = _mostRecentlyUsed;
                _mostRecentlyUsed._next = node;
                node._next = null;

                _mostRecentlyUsed = node;

                newLRU._previous = null;
                _leastRecentlyUsed = newLRU;
            }
            else {
                Node previous = node._previous;
                Node next = node._next;

                node._previous = _mostRecentlyUsed;
                _mostRecentlyUsed._next = node;
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
        _leastRecentlyUsed._previous = null;

        _queue.remove(keyToDelete);

        addCall(key);

        return keyToDelete;
    }

    public HashMap<T, Node> getQueue() {
        return _queue;
    }

    public void printMap() {
        Iterator iter = (Iterator) _queue.entrySet().iterator();

        while(iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();

            System.out.print("Key: ");
            System.out.print(entry.getKey());
            System.out.print(" Prev: ");
            System.out.print(((Node) entry.getValue()).getPrevKey());
            System.out.print(" Next: ");
            System.out.println(((Node) entry.getValue()).getNextKey());
        }

        System.out.println("MRU: " + _mostRecentlyUsed._key);
        System.out.println("LRU: " + _leastRecentlyUsed._key);
    }

    public T getMRU() {
        return _mostRecentlyUsed._key;
    }

    public T getLRU () {
        return _leastRecentlyUsed._key;
    }
}
