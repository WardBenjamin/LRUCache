import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;

public class LRUCacheTester {

    DataProvider<Integer, Integer> _testDataProvider;
    Cache<Integer, Integer> _testCache;


    @Test
    public void init(){
        _testDataProvider = new dataSource<Integer, Integer>();
        _testCache = new LRUCache<Integer, Integer>(testDataProvider,5);
    }

    @Test
    public void testCacheSize (){
        init();

        testCache.get(1);
        testCache.get(2);
        testCache.get(3);
        testCache.get(4);
        testCache.get(5);

        testCache.get(1);
        assertEquals(testCache.getNumMisses(), 5);

        testCache.get(6);
        assertEquals(testCache.getNumMisses(), 6);

        testCache.get(2);
        assertEquals(testCache.getNumMisses(), 6);

        testCache.get(1);
        assertEquals(testCache.getNumMisses(), 7);
    }
}
