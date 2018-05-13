package tjava.base;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class ExecutableTest {

    List<String> stringSet;

    @Test
    public void executeSomething(){
        stringSet = new ArrayList<>();
        Executable exe = () -> stringSet.add("1");

        exe.exec();

        assertTrue(stringSet.contains("1"));
    }

    @Test
    public void composeExecution(){
        stringSet = new ArrayList<>();
        Executable exe1 = () -> stringSet.add("1");
        Executable exe2 = () -> stringSet.add("2");
        Executable exeComposed = Executable.compose.apply(exe1).apply(exe2);

        exeComposed.exec();

        assertEquals("1", stringSet.get(0));
        assertEquals("2", stringSet.get(1));
    }

    @Test
    public void composeAllExecutesOfList(){
        stringSet = new ArrayList<>();
        tjava.base.List<Integer> srcList = tjava.base.List.list(0,1,2);

        Executable programe = srcList.foldLeft(Executable.ez, le->i->Executable.compose.apply(le).apply(()->stringSet.add(i.toString())));
        programe.exec();

        assertEquals("0", stringSet.get(0));
        assertEquals("1", stringSet.get(1));
        assertEquals("2", stringSet.get(2));
        assertEquals(3, stringSet.size());
    }
}
