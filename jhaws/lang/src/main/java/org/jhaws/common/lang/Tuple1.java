package org.jhaws.common.lang;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("serial")
@XmlRootElement
public class Tuple1<T1> implements Serializable {
	public static <T1> Tuple1<T1> of(T1 t1) {
		return new Tuple1<>(t1);
	}

	public static <T1> Tuple1<T1> tuple1(T1 t1) {
		return new Tuple1<>(t1);
	}

	protected T1 t1;

	public Tuple1() {
		super();
	}

	public Tuple1(T1 t1) {
		this.t1 = t1;
	}

	@Override
	public String toString() {
		return "( " + t1 + " )";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((t1 == null) ? 0 : t1.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("rawtypes")
		Tuple1 other = (Tuple1) obj;
		return Objects.equals(this.t1, other.t1);
	}

	public T1 getT1() {
		return this.t1;
	}

	public void setT1(T1 t1) {
		this.t1 = t1;
	}

	public Tuple1<T1> operateT1(UnaryOperator<T1> operation) {
		return operateT1(t -> true, operation);
	}

	public Tuple1<T1> operateT1(Predicate<T1> when, UnaryOperator<T1> operation) {
		if (when.test(t1)) {
			t1 = operation.apply(t1);
		}
		return this;
	}

	public Tuple1<T1> operateT1(Predicate<T1> when, UnaryOperator<T1> operation, Supplier<T1> elseOperation) {
		if (when.test(t1)) {
			t1 = operation.apply(t1);
		} else {
			t1 = elseOperation.get();
		}
		return this;
	}

	public Tuple1<T1> consumeT1(Consumer<T1> consumer) {
		return consumeT1(t -> true, consumer);
	}

	public Tuple1<T1> consumeT1(Predicate<T1> when, Consumer<T1> consumer) {
		if (when.test(t1)) {
			consumer.accept(t1);
		}
		return this;
	}

	public Tuple1<T1> consumeT1(Predicate<T1> when, Consumer<T1> consume, Supplier<T1> elseOperation) {
		if (when.test(t1)) {
			consume.accept(t1);
		} else {
			t1 = elseOperation.get();
		}
		return this;
	}

	public <X> Tuple1<X> projectT1(Function<T1, X> operation) {
		return projectT1(t -> true, operation);
	}

	public <X> Tuple1<X> projectT1(Predicate<T1> when, Function<T1, X> operation) {
		return projectT1(when, operation, () -> null);
	}

	public <X> Tuple1<X> projectT1(Predicate<T1> when, Function<T1, X> operation, Supplier<X> elseOperation) {
		return new Tuple1<X>(when.test(t1) ? operation.apply(t1) : elseOperation.get());
	}

	public boolean isT1Null() {
		return t1 == null;
	}

	public boolean isT1NotNull() {
		return t1 != null;
	}
}
