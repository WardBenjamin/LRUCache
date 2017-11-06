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

    private DataSource _dp1;
    private CallStack<String> _cs1;

    @Before
    public void init() {
        initDp1();
        initCs1();
    }

    private void initDp1() {
        _dp1 = new DataSource();

        _dp1.insert("K0", "V0");
        _dp1.insert("K1", "V1");
        _dp1.insert("K2", "V2");
        _dp1.insert("K3", "V3");
        _dp1.insert("K4", "V4");
        _dp1.insert("K5", "V5");
        _dp1.insert("K6", "V6");
        _dp1.insert("K7", "V7");
        _dp1.insert("K8", "V8");
        _dp1.insert("K9", "V9");
    }

    @Test
    public void test1() {

    }
}
