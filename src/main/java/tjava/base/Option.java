package tjava.base;

import java.util.Objects;
import java.util.function.Supplier;

public abstract class Option<A> {

  @SuppressWarnings("rawtypes")
  private static Option none = new None();

  public abstract A getOrThrow();
  public abstract A getOrElse(Supplier<A> defaultValue);
  public abstract boolean isSome();

  public Option<A> orElse(Supplier<Option<A>> defaultValue) {
    return OptionUtils.map(this, x -> this).getOrElse(defaultValue);
  }

  public abstract Option<A> filter(Function<A, Boolean> fun) ;

  private Option() {}



  private static class None<A> extends Option<A> {
    @Override
    public int hashCode() {
      return 0;
    }

    @Override
    public boolean equals(Object obj) {
      return obj instanceof None;
    }

    @Override
    public boolean isSome() {
      return false;
    }

    @Override
    public Option<A> filter(Function<A, Boolean> fun) {
      return this;
    }

    @Override
    public A getOrThrow() {
      throw new IllegalStateException("getOrThrow called on None");
    }

    @Override
    public A getOrElse(Supplier<A> defaultValue) {
      return defaultValue.get();
    }

    private None() {}

    @Override
    public String toString() {
      return "None";
    }
  }

  private static class Some<A> extends Option<A> {

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Some<?> some = (Some<?>) o;
      return Objects.equals(value, some.value);
    }

    @Override
    public int hashCode() {
      return Objects.hash(value);
    }

    private final A value;

    private Some(A a) {
      value = a;
    }

    @Override
    public Option<A> filter(Function<A, Boolean> fun) {
       return fun.apply(value) ? this : Option.none();
    }

    @Override
    public boolean isSome() {
      return true;
    }

    @Override
    public A getOrThrow() {
      return this.value;
    }

    @Override
    public A getOrElse(Supplier<A> defaultValue) {
      return value;
    }

    @Override
    public String toString() {
      return String.format("Some(%s)", this.value);
    }
}

  public static <A> Option<A> some(A a) {
    return new Some<>(a);
  }

  @SuppressWarnings("unchecked")
  public static <A> Option<A> none() {
    return none;
  }
}
