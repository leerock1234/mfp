package tjava.example;

import tjava.base.Function;
import tjava.base.Result;

import static tjava.base.Result.success;
import static tjava.base.ResultUtils.lift3;

public class ResultLiftExample {
    static Result<String> getFirstName() {
        return success("Mickey");
    }
    static Result<String> getLastName() {
        return success("Mickey");
    }
    static Result<String> getMail() {
        return success("mickey@disney.com");
    }
    public static void main(String[] args){
        Function<String, Function<String, Function<String, Toon>>> createPerson =
                x -> y -> z -> new Toon(x, y, z);
        Result<Toon> toon2 = lift3(createPerson)
                .apply(getFirstName())
                .apply(getLastName())
                .apply(getMail());
        Result<Toon> toon = getFirstName()
                .flatMap(firstName -> getLastName()
                        .flatMap(lastName -> getMail()
                                .map(mail -> new Toon(firstName, lastName, mail))));
    }
}

class Toon {
    String fName;
    String lName;
    String mail;
    public Toon(String fName, String lName, String mail){
        this.fName = fName;
        this.lName = lName;
        this.mail = mail;
    }
}
