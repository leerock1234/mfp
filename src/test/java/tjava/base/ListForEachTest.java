package tjava.base;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;

public class ListForEachTest {

    Set<String> stringSet;

    @Test
    public void canMapToEffect(){
        stringSet = new HashSet<>();
        List list = List.list(1,2,3,4);

        Effect.forEach(list, new PrintEffect());

        Set<String> expectSet = new HashSet<>();
        expectSet.add("1");
        expectSet.add("2");
        expectSet.add("3");
        expectSet.add("4");
        assertEquals(expectSet, stringSet);
    }

    private class PrintEffect implements Effect<Integer> {
        @Override
        public void apply(Integer integer) {
            stringSet.add(integer.toString());
        }
    }
}
