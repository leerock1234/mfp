package tjava.base;

import org.junit.Test;
import tjava.base.utils.Nothing;

import static junit.framework.TestCase.assertEquals;

public class IOTest {

    String output="";

    @Test
    public void combineWithFoldRight(){
        output = "";
        List<IO<String>> instructions = List.listInverse(
                print("Hello, "),
                print("Mike"),
                print("!")
        );

        IO program = instructions.foldLeft(IO.empty, io -> io::add);
        program.run();

        assertEquals("Hello, Mike!", output);
    }

    IO<String> print(String input){
        return ()-> output = output + input;
    }

    @Test
    public void input(){

    }
}
