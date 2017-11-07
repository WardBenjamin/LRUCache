import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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

    @Test
    public void testCacheLength() {
        _testCache.get(1);
        _testCache.get(2);

        assertEquals(2, ((LRUCache) _testCache).getCacheLength());

        _testCache.get(2);
        assertEquals(2, ((LRUCache) _testCache).getCacheLength());

        _testCache.get(3);
        assertEquals(3, ((LRUCache) _testCache).getCacheLength());

        _testCache.get(4);
        _testCache.get(5);
        assertEquals(5, ((LRUCache) _testCache).getCacheLength());

        _testCache.get(6);
        assertEquals(5, ((LRUCache) _testCache).getCacheLength());
    }

    @Test
    public void testTimeComplexity() {
        final DataSource<Integer, Integer> miniServer = new DataSource<>();

        for (int i = 0; i < 42069; i++) {
            miniServer._data.put(i, i);
        }

        LRUCache<Integer, Integer> smallCache = new LRUCache(miniServer, 5);
        LRUCache<Integer, Integer> bigCache = new LRUCache(miniServer, 690);

        smallCache.get(5000);
        smallCache.get(5001);
        smallCache.get(5002);
        smallCache.get(5000);
        smallCache.get(5005);
        smallCache.get(5003);
        smallCache.get(5001);
        smallCache.get(5007);
        smallCache.get(5002);
        smallCache.get(5000);

        for (int i = 0; i < 700; i++) {
            bigCache.get(i);
        }

        bigCache.get(5000);
        bigCache.get(5001);
        bigCache.get(5002);
        bigCache.get(5000);

        System.out.println("Small Cache");
        smallCache.printCache();
        System.out.println(smallCache.getMRU());
        System.out.println(smallCache.getLRU());
        System.out.println("\n");

        System.out.println("Big Cache");
        bigCache.printCache();
        System.out.println(bigCache.getMRU());
        System.out.println(bigCache.getLRU());
        System.out.println("\n");

        final long smallStart = System.nanoTime();
        final int smallAnswer = smallCache.get(5002);
        final long smallTime = System.nanoTime() - smallStart;

        final long bigStart = System.nanoTime();
        final int bigAnswer = bigCache.get(5002);
        final long bigTime = System.nanoTime() - bigStart;

        System.out.println(bigTime);
        System.out.println(smallTime);

        assertTrue(Math.abs(bigTime - smallTime) < smallAnswer && bigAnswer == smallAnswer);
    }
}
