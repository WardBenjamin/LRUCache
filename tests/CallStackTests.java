import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;

public class CallStackTests {
    private class DataSource<T, U> implements DataProvider<T, U> {
        private HashMap<T, U> _data;

        public DataSource() {
            _data = new HashMap<>();
        }

        public void insert(T key, U value) {
            _data.put(key, value);
        }

        public U get(T key) {
            return _data.get(key);
        }
    }

    private DataProvider _dp1;

    @Before
    public void init() {
        initDp1();
    }

    private void initDp1() {
    }

    @Test
    public void test1() {

    }
}
