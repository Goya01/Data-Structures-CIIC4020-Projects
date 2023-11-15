package interfaces;

@FunctionalInterface
public interface FilterFunction<E> {
	public boolean filter(E e);
}
