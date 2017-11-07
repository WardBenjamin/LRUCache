import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

/**
 * Code to test an <tt>LRUCache</tt> implementation.
 */
public class CacheTest {
	private DataSource server;
	private class DataSource<T, U> implements DataProvider<T, U> {
		private HashMap<T, U> _data;
		public String log = "";
		public DataSource() {
			_data = new HashMap<T, U>();
		}

		public void insert(T key, U value) {
			_data.put(key, value);
		}

		public U get(T key) {
			log+= key.toString();
			return _data.get(key);
		}
	}


	public void initServer() {
		this.server = new DataSource<Integer,Integer>();
		for(int i = 0; i <99; i++)
			server._data.put(i,((int)i * 69 - 420) ^2);
	}

	@Test
	public void testCacheWorksProperly () {

	}
}
