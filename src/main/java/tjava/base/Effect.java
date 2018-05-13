package tjava.base;

public interface Effect<T> {

    /**
     * Can apply each element in list to the effect
     * @param list
     * @param effect
     * @param <U>
     */
	static <U> void forEach(List<U> list, Effect<U> effect) {
	    List<U> inList = list;
	    while(!inList.isEmpty()){
	        effect.apply(inList.head());
	        inList = inList.tail();
        }
	}

	void apply(T t);
}
