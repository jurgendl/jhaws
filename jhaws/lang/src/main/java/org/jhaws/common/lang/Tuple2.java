package org.jhaws.common.lang;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("serial")
@XmlRootElement
public class Tuple2<T1, T2> extends Tuple1<T1> {
	public static <T1, T2> Tuple2<T1, T2> of(T1 t1, T2 t2) {
		return new Tuple2<>(t1, t2);
	}

	protected T2 t2;

	public Tuple2() {
		super();
	}

	public Tuple2(T1 t1, T2 t2) {
		super(t1);
		this.t2 = t2;
	}

	@Override
	public String toString() {
		return "( " + t1 + " , " + t2 + " )";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(t2);
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
		Tuple2 other = (Tuple2) obj;
		return Objects.equals(this.t1, other.t1) && Objects.equals(this.t2, other.t2);
	}

	public T2 getT2() {
		return this.t2;
	}

	public void setT2(T2 t2) {
		this.t2 = t2;
	}

	public Tuple2<T1, T2> operateT2(UnaryOperator<T2> operation) {
		return operateT2(t -> true, operation);
	}

	public Tuple2<T1, T2> operateT2(Predicate<T2> when, UnaryOperator<T2> operation) {
		if (when.test(t2)) {
			t2 = operation.apply(t2);
		}
		return this;
	}

	public Tuple2<T1, T2> operateT2(Predicate<T2> when, UnaryOperator<T2> operation, Supplier<T2> elseOperation) {
		if (when.test(t2)) {
			t2 = operation.apply(t2);
		} else {
			t2 = elseOperation.get();
		}
		return this;
	}

	public Tuple2<T1, T2> consumeT2(Consumer<T2> consumer) {
		return consumeT2(t -> true, consumer);
	}

	public Tuple2<T1, T2> consumeT2(Predicate<T2> when, Consumer<T2> consumer) {
		if (when.test(t2)) {
			consumer.accept(t2);
		}
		return this;
	}

	public Tuple2<T1, T2> consumeT2(Predicate<T2> when, Consumer<T2> consume, Supplier<T2> elseOperation) {
		if (when.test(t2)) {
			consume.accept(t2);
		} else {
			t2 = elseOperation.get();
		}
		return this;
	}

	public <X> Tuple2<T1, X> projectT2(Function<T2, X> operation) {
		return projectT2(t -> true, operation);
	}

	public <X> Tuple2<T1, X> projectT2(Predicate<T2> when, Function<T2, X> operation) {
		return projectT2(when, operation, () -> null);
	}

	public <X> Tuple2<T1, X> projectT2(Predicate<T2> when, Function<T2, X> operation, Supplier<X> elseOperation) {
		return new Tuple2<T1, X>(t1, when.test(t2) ? operation.apply(t2) : elseOperation.get());
	}
}
