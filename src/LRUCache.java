import java.util.HashMap;

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
}