import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

/**
 * Code to test an <tt>LRUCache</tt> implementation.
 */
public class CacheTest {
	private DataSource _server;
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
		this._server = new DataSource<Integer,Integer>();
		for(int i = 0; i <99; i++)
			_server._data.put(i,dataCreator(i));
	}

	public Integer dataCreator(int i) { return ((i * 69 - 420) ^2); }

	@Test
	public void testCacheWorksProperly () {
		initServer();
		LRUCache<Integer,Integer> testcache = new LRUCache<Integer, Integer>(_server,5);
		assertEquals(testcache.get(1),dataCreator(1));
		assertEquals(_server.log, "1");
		assertEquals(testcache.get(2),dataCreator(2));
		assertEquals(_server.log , "12");
		assertEquals(testcache.get(3),dataCreator(3));
		assertEquals(_server.log , "123");
		assertEquals(testcache.get(4),dataCreator(4));
		assertEquals(_server.log , "1234");
		assertEquals(testcache.get(5),dataCreator(5));
		assertEquals(_server.log , "12345");
		assertEquals(testcache.get(1),dataCreator(1));
		assertEquals(_server.log , "12345");
		assertEquals(testcache.get(6),dataCreator(6));
		assertEquals(_server.log , "123456");
		assertEquals(testcache.get(2),dataCreator(2));
		assertEquals(_server.log , "1234562");


	}
}
