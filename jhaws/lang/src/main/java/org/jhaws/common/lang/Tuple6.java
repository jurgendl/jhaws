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
public class Tuple6<T1, T2, T3, T4, T5, T6> extends Tuple5<T1, T2, T3, T4, T5> {
	public static <T1, T2, T3, T4, T5, T6> Tuple6<T1, T2, T3, T4, T5, T6> of(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
		return new Tuple6<>(t1, t2, t3, t4, t5, t6);
	}

	public static <T1, T2, T3, T4, T5, T6> Tuple6<T1, T2, T3, T4, T5, T6> tuple6(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5,
			T6 t6) {
		return new Tuple6<>(t1, t2, t3, t4, t5, t6);
	}

	public static <T1, T2, T3, T4, T5, T6> Tuple6<T1, T2, T3, T4, T5, T6> tuple6(Map.Entry<T1, T2> entry1,
			Map.Entry<T3, T4> entry2, Map.Entry<T5, T6> entry3) {
		return new Tuple6<>(entry1.getKey(), entry1.getValue(), entry2.getKey(), entry2.getValue(), entry3.getKey(),
				entry3.getValue());
	}

	protected T6 t6;

	public Tuple6() {
		super();
	}

	public Tuple6(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
		super(t1, t2, t3, t4, t5);
		this.t6 = t6;
	}

	public Tuple6(Map.Entry<T1, T2> entry1, Map.Entry<T3, T4> entry2, Map.Entry<T5, T6> entry3) {
		super(entry1, entry2, entry3.getKey());
		this.t6 = entry3.getValue();
	}

	@Override
	public String toString() {
		return "( " + t1 + " , " + t2 + ", " + t3 + ", " + t4 + ", " + t5 + ", " + t6 + " )";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(t6);
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
		Tuple6 other = (Tuple6) obj;
		return Objects.equals(this.t1, other.t1) && Objects.equals(this.t2, other.t2)
				&& Objects.equals(this.t3, other.t3) && Objects.equals(this.t4, other.t4)
				&& Objects.equals(this.t5, other.t5) && Objects.equals(this.t6, other.t6);
	}

	public T6 getT6() {
		return this.t6;
	}

	public void setT6(T6 t6) {
		this.t6 = t6;
	}

	public T6 t6() {
		return this.t6;
	}

	public Tuple6<T1, T2, T3, T4, T5, T6> t6(T6 t6) {
		this.t6 = t6;
		return this;
	}

	public Tuple6<T1, T2, T3, T4, T5, T6> operateT6(UnaryOperator<T6> operation) {
		return operateT6(t -> true, operation);
	}

	public Tuple6<T1, T2, T3, T4, T5, T6> operateT6(Predicate<T6> when, UnaryOperator<T6> operation) {
		if (when.test(t6)) {
			t6 = operation.apply(t6);
		}
		return this;
	}

	public Tuple6<T1, T2, T3, T4, T5, T6> operateT6(Predicate<T6> when, UnaryOperator<T6> operation,
			Supplier<T6> elseOperation) {
		if (when.test(t6)) {
			t6 = operation.apply(t6);
		} else {
			t6 = elseOperation.get();
		}
		return this;
	}

	public Tuple6<T1, T2, T3, T4, T5, T6> consumeT6(Consumer<T6> consumer) {
		return consumeT6(t -> true, consumer);
	}

	public Tuple6<T1, T2, T3, T4, T5, T6> consumeT6(Predicate<T6> when, Consumer<T6> consumer) {
		if (when.test(t6)) {
			consumer.accept(t6);
		}
		return this;
	}

	public Tuple6<T1, T2, T3, T4, T5, T6> consumeT6(Predicate<T6> when, Consumer<T6> consume,
			Supplier<T6> elseOperation) {
		if (when.test(t6)) {
			consume.accept(t6);
		} else {
			t6 = elseOperation.get();
		}
		return this;
	}

	public <X> Tuple6<T1, T2, T3, T4, T5, X> projectT6(Function<T6, X> operation) {
		return projectT6(t -> true, operation);
	}

	public <X> Tuple6<T1, T2, T3, T4, T5, X> projectT6(Predicate<T6> when, Function<T6, X> operation) {
		return projectT6(when, operation, () -> null);
	}

	public <X> Tuple6<T1, T2, T3, T4, T5, X> projectT6(Predicate<T6> when, Function<T6, X> operation,
			Supplier<X> elseOperation) {
		return new Tuple6<T1, T2, T3, T4, T5, X>(t1, t2, t3, t4, t5,
				when.test(t6) ? operation.apply(t6) : elseOperation.get());
	}

	public boolean isT6Null() {
		return t6 == null;
	}

	public boolean isT6NotNull() {
		return t6 != null;
	}

	public Tuple5<T1, T2, T3, T4, T5> popT5() {
		return Tuple5.of(getT1(), getT2(), getT3(), getT4(), getT5());
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T get(int i) throws IndexOutOfBoundsException {
		if (i == 5)
			return (T) getT6();
		return super.get(i);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> void set(int i, T t) throws IndexOutOfBoundsException {
		if (i == 5)
			setT6((T6) t);
		else
			super.set(i, t);
	}

	public Tuple6<T1, T2, T3, T4, T5, T6> asT6() {
		return this;
	}
}
