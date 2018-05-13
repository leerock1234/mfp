package tjava.base;

public class Utils {

	public static List<Integer> range(int start, int end){
		return List.unfold(start, x->x+1, x->x<end);
	}

}
