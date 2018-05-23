package tjava.base;

import org.junit.Test;

import java.util.function.Supplier;

import static junit.framework.TestCase.assertEquals;
import static tjava.base.utils.MapUtils.smap;

public class MapUtilsTest {

    @Test
    public void supplierMap(){
        Supplier<Integer> iS = ()->1;

        assertEquals("1", smap(iS, x->String.valueOf(x)).get());
    }
}
