package tjava.homework;

import static tjava.base.Case.match;
import static tjava.base.Case.mcase;
import static tjava.base.Result.failure;
import static tjava.base.Result.success;

import java.util.regex.Pattern;

import tjava.base.Effect;
import tjava.base.Function;
import tjava.base.Result;

public class EmailValidation {
	static Pattern emailPattern = Pattern.compile("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$");
	static Effect<String> success = s -> System.out.println("Mail sent to " + s);
	static Effect<String> failure = s -> System.err.println("Error message logged: " + s);

	public static void main(String... args) {
/*		emailChecker.apply("this.is@my.email").bind(success, failure);
		emailChecker.apply(null).bind(success, failure);
		emailChecker.apply("").bind(success, failure);
		emailChecker.apply("john.doe@acme.com").bind(success, failure);*/
	}

	static Function<String, Result<String>> emailChecker = s -> match(
			mcase(() -> success(s)),
			mcase(() -> s == null, () -> failure("email must not be null")),
			mcase(() -> s.length() == 0, () -> failure("email must not be empty")),
			mcase(() -> !emailPattern.matcher(s).matches(), () -> failure("email " + s + " is invalid."))
			);
}