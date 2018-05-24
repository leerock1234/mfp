package tjava.example;

import tjava.base.IO;
import tjava.base.iop.Console;
import tjava.base.utils.Nothing;

public class ConsoleExample {

    public static void main(String... args) {
        IO<Nothing> script = sayHello();
        script.run();
    }

    private static IO<Nothing> sayHello() {
        return Console.printLine("Enter your name: ")
                .flatMap(Console::readLine)
                .map(ConsoleExample::buildMessage)
                .flatMap(Console::printLine);
    }

    private static String buildMessage(String name) {
        return String.format("Hello, %s!", name);
    }
}
