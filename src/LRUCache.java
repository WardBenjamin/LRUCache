import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * An implementation of <tt>Cache</tt> that uses a least-recently-used (LRU)
 * eviction policy.
 */
public class LRUCache<T, U> implements Cache<T, U> {

	private DataProvider _dataProvider;
	private HashMap<T, U> _cache;
	private CallStack<T> _callStack;

	private int _cacheLength;
	private int _numMiss;



	/**
	 * @param provider the data provider to consult for a cache miss
	 * @param capacity the exact number of (key,value) pairs to store in the cache
	 */
	public LRUCache (DataProvider<T, U> provider, int capacity) {
		_dataProvider = provider;
		_cacheLength = capacity;

        _cache = new HashMap<>();
        _callStack = new CallStack<>(capacity);
    }

	/**
	 * Returns the value associated with the specified key.
	 * @param key the key
	 * @return the value associated with the key
	 */
	public U get (T key) {
		U value = _cache.get(key);

		/*
        Checks to see if the value is in the cache, and if it isn't it calls it from the data
        provider and adds it to the cache
        */
        if (value == null) {
			_numMiss++;

			value = (U) _dataProvider.get(key);

			_cache.put(key, value);

			if(_cache.size() > _cacheLength) {
				T keyToDelete = _callStack.addAndEvict(key);

				_cache.remove(keyToDelete);
			}
			else {
				_callStack.addCall(key);
			}
		}
		else {
			_callStack.addCall(key);
		}
		return value;
	}

	/**
	 * Returns the number of cache misses since the object's instantiation.
	 * @return the number of cache misses since the object's instantiation.
	 */
	public int getNumMisses () {
		return _numMiss;
	}

	/**
	 * Returns the length of the cache
	 * @return the length of the cache
	 */
	public int getCacheLength() {
		return _cache.size();
	}

	/**
	 * Prints the cache
	 */
	public void printCache() {
        Iterator iter = (Iterator) _cache.entrySet().iterator();

        while(iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();

            System.out.print("Key: ");
            System.out.println(entry.getKey());
        }
    }

	private class CallStack<T> {
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

}

