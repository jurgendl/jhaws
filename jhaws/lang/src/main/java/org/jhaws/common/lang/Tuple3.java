package org.jhaws.common.lang;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("serial")
@XmlRootElement
public class Tuple3<T1, T2, T3> extends Tuple2<T1, T2> {
	public static <T1, T2, T3> Tuple3<T1, T2, T3> of(T1 t1, T2 t2, T3 t3) {
		return new Tuple3<>(t1, t2, t3);
	}

	public static <T1, T2, T3> Tuple3<T1, T2, T3> tuple3(T1 t1, T2 t2, T3 t3) {
		return new Tuple3<>(t1, t2, t3);
	}

	public static <T1, T2, T3> Tuple2<T1, T2> of(Map.Entry<T1, T2> entry, T3 t3) {
		return new Tuple3<>(entry.getKey(), entry.getValue(), t3);
	}

	public static <T1, T2, T3> Tuple2<T1, T2> tuple3(Map.Entry<T1, T2> entry, T3 t3) {
		return new Tuple3<>(entry.getKey(), entry.getValue(), t3);
	}

	protected T3 t3;

	public Tuple3() {
		super();
	}

	public Tuple3(T1 t1, T2 t2, T3 t3) {
		super(t1, t2);
		this.t3 = t3;
	}

	public Tuple3(Map.Entry<T1, T2> entry, T3 t3) {
		super(entry.getKey(), entry.getValue());
		this.t3 = t3;
	}

	@Override
	public String toString() {
		return "( " + t1 + " , " + t2 + ", " + t3 + " )";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(t3);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("rawtypes")
		Tuple3 other = (Tuple3) obj;
		return Objects.equals(this.t1, other.t1) && Objects.equals(this.t2, other.t2)
				&& Objects.equals(this.t3, other.t3);
	}

	@Override
	public <I> boolean isEquals(Function<T1, I> kf, Function<T2, I> vf) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isEquals() {
		throw new UnsupportedOperationException();
	}

	public T3 getT3() {
		return this.t3;
	}

	public void setT3(T3 t3) {
		this.t3 = t3;
	}

	public T3 t3() {
		return this.t3;
	}

	public Tuple3<T1, T2, T3> t3(T3 t3) {
		this.t3 = t3;
		return this;
	}

	public Tuple3<T1, T2, T3> operateT3(UnaryOperator<T3> operation) {
		return operateT3(t -> true, operation);
	}

	public Tuple3<T1, T2, T3> operateT3(Predicate<T3> when, UnaryOperator<T3> operation) {
		if (when.test(t3)) {
			t3 = operation.apply(t3);
		}
		return this;
	}

	public Tuple3<T1, T2, T3> operateT3(Predicate<T3> when, UnaryOperator<T3> operation, Supplier<T3> elseOperation) {
		if (when.test(t3)) {
			t3 = operation.apply(t3);
		} else {
			t3 = elseOperation.get();
		}
		return this;
	}

	public Tuple3<T1, T2, T3> consumeT3(Consumer<T3> consumer) {
		return consumeT3(t -> true, consumer);
	}

	public Tuple3<T1, T2, T3> consumeT3(Predicate<T3> when, Consumer<T3> consumer) {
		if (when.test(t3)) {
			consumer.accept(t3);
		}
		return this;
	}

	public Tuple3<T1, T2, T3> consumeT3(Predicate<T3> when, Consumer<T3> consume, Supplier<T3> elseOperation) {
		if (when.test(t3)) {
			consume.accept(t3);
		} else {
			t3 = elseOperation.get();
		}
		return this;
	}

	public <X> Tuple3<T1, T2, X> projectT3(Function<T3, X> operation) {
		return projectT3(t -> true, operation);
	}

	public <X> Tuple3<T1, T2, X> projectT3(Predicate<T3> when, Function<T3, X> operation) {
		return projectT3(when, operation, () -> null);
	}

	public <X> Tuple3<T1, T2, X> projectT3(Predicate<T3> when, Function<T3, X> operation, Supplier<X> elseOperation) {
		return new Tuple3<T1, T2, X>(t1, t2, when.test(t3) ? operation.apply(t3) : elseOperation.get());
	}

	public boolean isT3Null() {
		return t3 == null;
	}

	public boolean isT3NotNull() {
		return t3 != null;
	}

	public <T4> Tuple4<T1, T2, T3, T4> pushT4(T4 t4) {
		return Tuple4.of(getT1(), getT2(), getT3(), t4);
	}

	public Tuple2<T1, T2> popT2() {
		return Tuple2.of(getT1(), getT2());
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T get(int i) throws IndexOutOfBoundsException {
		if (i == 2)
			return (T) getT3();
		return super.get(i);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> void set(int i, T t) throws IndexOutOfBoundsException {
		if (i == 2)
			setT3((T3) t);
		else
			super.set(i, t);
	}

	public Tuple3<T1, T2, T3> asT3() {
		return this;
	}
}
