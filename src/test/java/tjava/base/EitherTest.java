package tjava.base;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class EitherTest {

    @Test
    public void shouldChangedByMap(){
        Either<String, Integer> err = Either.left("error");
        Either<String, Integer> integer = Either.right(1);

        assertEquals(err, err.map(x->x.toString()));
        assertEquals(Either.right("1"), integer.map(x->x.toString()));
    }

    @Test
    public void shouldFlatMapEither(){
        Either<String, Integer> err = Either.left("error");
        Either<String, Integer> integer = Either.right(1);

        assertEquals(err, err.flatMap(x->Either.right(x.toString())));
        assertEquals(Either.right("1"), integer.flatMap(x->Either.right(x.toString())));
    }

    @Test
    public void shouldGetOrElse(){
        Either<String, Integer> err = Either.left("error");
        Either<String, Integer> integer = Either.right(1);

        assertEquals(-1, err.getOrElse(()->-1).intValue());
        assertEquals(1, integer.getOrElse(() -> -1).intValue());
    }
}
