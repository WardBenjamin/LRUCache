import java.util.HashMap;
import java.util.LinkedList;

/**
 * An implementation of <tt>Cache</tt> that uses a least-recently-used (LRU)
 * eviction policy.
 */
public class LRUCache<T, U> implements Cache<T, U> {
    DataProvider _dataProvider;
    LinkedList _frequencyMap;
    HashMap<T,node<T,U>> _cache;

    private int _cacheLength;
	public int _numMiss;
	private node<T,U> lastUsed;
	private node<T,U> firstUsed;



	private class node<T,U> {
		T key;
		U value;
		Node<T,U> previous;
		Node<T,U> next;
		node<T,U>(T key, U value, node<T,U> previous, node<T,U> next){
			this.key = key;
			this.value = value;
			this.previous = previous;
			this.next = next;
		}
	}

	/**
	 * @param provider the data provider to consult for a cache miss
	 * @param capacity the exact number of (key,value) pairs to store in the cache
	 */
	public LRUCache (DataProvider<T, U> provider, int capacity) {
	    _dataProvider = provider;
		_cache = new HashMap<T,U>();
		_cacheLength = capacity;
	}

	/**
	 * Returns the value associated with the specified key.
	 * @param key the key
	 * @return the value associated with the key
	 */
	public U get (T key) {
		U value = _cache.get(key);

		if(value == null){
			_numMiss++;
			value = _dataProvider.get(key);
			_cache.add(key, value);
		}
		else{

		}
		if(_cache.size() > _cacheLength){
			evict();
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

	private void evict () {

	}
}
