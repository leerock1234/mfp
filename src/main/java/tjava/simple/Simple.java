package tjava.simple;

import tjava.base.Function;

public class Simple {

	public static Function<Integer, Integer> triple = arg -> arg * arg * arg;

	public static Function<Integer, Integer> square = arg -> arg * arg;

	public static Function<Integer, Integer> inc = arg -> arg + 1;
	
	Function<Double, Double> sin = Math::sin;

	public static Function<Integer, Function<Integer, Integer>> addTwo = x -> y -> x + y;

	public static Function<Function<Integer,Integer>, Function<Function<Function<Integer, Integer>, Function<Integer, Integer>>, Function<Integer,Integer>>> addTwo1 
	                                   = (Function<Integer, Integer> x) -> (Function<Function<Integer,Integer>, Function<Integer, Integer>> y) -> y.apply(x);

	public static Function<Function<Integer,Integer>, Function<Function<Integer, Integer>, Function<Integer, Integer>>> addTwo2 
	                   = x -> y -> z -> x.apply(y.apply(z));
	                                   
}
