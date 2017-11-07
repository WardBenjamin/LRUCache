import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;

public class LRUCacheTester {

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
    Cache<Integer, Integer> _testCache;

    public void initServer() {
        _server = new DataSource<Integer,Integer>();
        for(int i = 0; i <99; i++)
            _server._data.put(i,((int)i * 69 - 420) ^2);
    }

    @Before
    public void init(){
        initServer();
        _testCache = new LRUCache<Integer, Integer>(_server,5);
    }

    @Test
    public void testCacheSize (){
        _testCache.get(1);
        _testCache.get(2);
        _testCache.get(3);
        _testCache.get(4);
        _testCache.get(5);

        _testCache.get(1);
        assertEquals(_testCache.getNumMisses(), 5);

        _testCache.get(6);
        assertEquals(_testCache.getNumMisses(), 6);

        _testCache.get(2);
        assertEquals(_testCache.getNumMisses(), 7);

        _testCache.get(1);
        assertEquals(_testCache.getNumMisses(), 7);
    }

    @Test
    public void testCacheCorrectness(){
        assertEquals(_testCache.get(1),_server.get(1));
        assertEquals(_testCache.get(1),_server.get(1));
        assertEquals(_testCache.get(6),_server.get(6));
    }
}
