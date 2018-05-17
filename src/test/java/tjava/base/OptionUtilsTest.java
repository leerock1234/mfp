package tjava.base;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class OptionUtilsTest {
    @Test
    public void someShouldBeMap(){
        Option<AClass> aClass = Option.some(AClass.build(1));
        Option<BClass> bClass = OptionUtils.<AClass, BClass>map(aClass, x->BClass.build(x.i.toString()));
        assertEquals("1", bClass.getOrThrow().i);
    }

    @Test
    public void noneShouldBeMap(){
        Option<AClass> aClass = Option.none();
        Option<BClass> bClass = OptionUtils.<AClass, BClass>map(aClass, x->BClass.build(x.i.toString()));
        assertTrue(bClass.getOrElse(() -> BClass.build("err")).i.equals("err"));
    }

    @Test
    public void someShouldBeFlatMap(){
        Option<AClass> aClass = Option.some(AClass.build(1));
        Option<BClass> bClass = OptionUtils.<AClass, BClass>flatMap(aClass, x->Option.some(BClass.build(x.i.toString())));
        assertEquals("1", bClass.getOrThrow().i);
    }

    @Test
    public void noneShouldBeFlatMap(){
        Option<AClass> aClass = Option.none();
        Option<BClass> bClass = OptionUtils.<AClass, BClass>flatMap(aClass, x->Option.some(BClass.build(x.i.toString())));
        assertTrue(!bClass.isSome());
    }

    Function<Option<Integer>, Option<Integer>> onlyNature = OptionUtils.lift(OptionUtilsTest::OnlyNature);

    static Integer OnlyNature(Integer i){
        if (i>=0) return i;
        throw new RuntimeException();
    }

    @Test
    public void shouldBeNoneWhenInputNoneForOnlyNature(){
        assertFalse(onlyNature.apply(Option.none()).isSome());
    }

    @Test
    public void shouldNotBePositiveForOnlyNature(){
        assertFalse(onlyNature.apply(Option.some(-1)).isSome());
    }

    @Test
    public void shouldPassForPositiveForOnlyNature(){
        assertEquals(1, onlyNature.apply(Option.some(1)).getOrThrow().intValue());
    }

}

class AClass {
    public static AClass build(int i){
        AClass aClass = new AClass();
        aClass.i = i;
        return aClass;
    }
    Integer i;
}

class BClass {
    public static BClass build(String i){
        BClass bClass = new BClass();
        bClass.i = i;
        return bClass;
    }
    String i;
}
