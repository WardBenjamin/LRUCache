import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;

public class CallStackTests {
    private class DataSource<T, U> implements DataProvider<T, U> {
        private HashMap<T, U> _data;

        public DataSource() {
            _data = new HashMap<T, U>();
        }

        public void insert(T key, U value) {
            _data.put(key, value);
        }

        public U get(T key) {
            return _data.get(key);
        }
    }

    private DataSource<String, String> _ds1;
    private CallStack<String> _cs1;

    @Before
    public void init() {
        initDp1();
        initCs1();
    }

    private void initDp1() {
        _ds1 = new DataSource();

        _ds1.insert("K0", "V0");
        _ds1.insert("K1", "V1");
        _ds1.insert("K2", "V2");
        _ds1.insert("K3", "V3");
        _ds1.insert("K4", "V4");
        _ds1.insert("K5", "V5");
        _ds1.insert("K6", "V6");
        _ds1.insert("K7", "V7");
        _ds1.insert("K8", "V8");
        _ds1.insert("K9", "V9");
    }

    private void initCs1() {
        _cs1 = new CallStack<>(5);
    }

    private void simpleCalls() {
        _cs1.addCall("K0");
        _cs1.addCall("K1");
        _cs1.addCall("K2");
        _cs1.addCall("K3");
        _cs1.addCall("K4");
    }

    private void simpleCallsJumbled() {
        simpleCalls();

        _cs1.addCall("K1");
        _cs1.addCall("K0");
        _cs1.addCall("K4");
        _cs1.addCall("K4");
        _cs1.addCall("K3");
        _cs1.addCall("K3");
        _cs1.addCall("K2");
        _cs1.addCall("K1");
        _cs1.addCall("K3");
        _cs1.addCall("K0");
    }

    @Test
    public void testLengthInit() {
        assertEquals(0, _cs1.getQueue().size());
    }

    @Test
    public void checkMRUAndLRUSimple() {
        simpleCalls();

        assertEquals("K0", _cs1.getLRU());
        assertEquals("K4", _cs1.getMRU());
    }

    @Test
    public void checkTreeSimple() {
        simpleCalls();


        System.out.println("---- checkTreeSimple() ----");
        _cs1.printMap();
    }

    @Test
    public void checkMRUAndLRUComplexNonAdded() {
        simpleCallsJumbled();

        assertEquals("K4", _cs1.getLRU());
        assertEquals("K0", _cs1.getMRU());
    }

    @Test
    public void checkLRUEviction() {
        simpleCallsJumbled();

        assertEquals("K4", _cs1.addAndEvict("K5"));

        System.out.println("--- checkLRUEviction() ----");
        _cs1.printMap();
    }

    @Test
    public void checkLRUEvictionSize() {
        simpleCallsJumbled();

        _cs1.addAndEvict("K5");

        assertEquals(5, _cs1.getQueue().size());
    }
}
