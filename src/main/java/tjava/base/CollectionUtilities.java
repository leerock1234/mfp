package tjava.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CollectionUtilities {

	public static <T> List<T> list() {
		return Collections.emptyList();
	}

	public static <T> List<T> list(T t) {
		return Collections.singletonList(t);
	}

	public static <T> List<T> list(List<T> ts) {
		return Collections.unmodifiableList(new ArrayList<>(ts));
	}

	@SafeVarargs
	public static <T> List<T> list(T... t) {
		return Collections.unmodifiableList(Arrays.asList(Arrays.copyOf(t, t.length)));
	}

	public static <T> T head(List<T> list) {
		if (list.size() == 0) {
			throw new IllegalStateException("head of empty list");
		}
		return list.get(0);
	}

	private static <T> List<T> copy(List<T> ts) {
		return new ArrayList<>(ts);
	}

	public static <T> List<T> tail(List<T> list) {
		if (list.size() == 0) {
			return Collections.unmodifiableList(new ArrayList<>());
		}
		List<T> workList = copy(list);
		workList.remove(0);
		return Collections.unmodifiableList(workList);
	}

	public static <T> List<T> append(List<T> list, T t) {
		List<T> ts = copy(list);
		ts.add(t);
		return Collections.unmodifiableList(ts);
	}

	public static <U,V> V foldLeft(List<U> is, V identity, Function<V, Function<U, V>> f) {
		V result = identity;
		for (U i : is) {
			result = f.apply(result).apply(i);
		}
		return result;
	}

	public static <U,V> V foldRight(List<U> is, V identity, Function<V, Function<U, V>> f) {
		if (is.isEmpty()) {
			return identity;
		}else{
			return f.apply(foldRight(tail(is), identity, f)).apply(head(is));
		}
	}
	
	public static <T> List<T> reverse(List<T> list) {
		List<T> result = new ArrayList<>();
		if (list.size()>0){
			result.addAll(reverse(tail(list)));
			result.add(head(list));
		}
		return Collections.unmodifiableList(result);
	}

}
