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
public class Tuple5<T1, T2, T3, T4, T5> extends Tuple4<T1, T2, T3, T4> {
	public static <T1, T2, T3, T4, T5> Tuple5<T1, T2, T3, T4, T5> of(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
		return new Tuple5<>(t1, t2, t3, t4, t5);
	}

	public static <T1, T2, T3, T4, T5> Tuple5<T1, T2, T3, T4, T5> tuple5(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
		return new Tuple5<>(t1, t2, t3, t4, t5);
	}

	public static <T1, T2, T3, T4, T5> Tuple5<T1, T2, T3, T4, T5> tuple5(Map.Entry<T1, T2> entry1, Map.Entry<T3, T4> entry2, T5 t5) {
		return new Tuple5<>(entry1.getKey(), entry1.getValue(), entry2.getKey(), entry2.getValue(), t5);
	}

	protected T5 t5;

	public Tuple5() {
		super();
	}

	public Tuple5(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
		super(t1, t2, t3, t4);
		this.t5 = t5;
	}

	public Tuple5(Map.Entry<T1, T2> entry1, Map.Entry<T3, T4> entry2, T5 t5) {
		super(entry1, entry2);
		this.t5 = t5;
	}

	@Override
	public String toString() {
		return "( " + t1 + " , " + t2 + ", " + t3 + ", " + t4 + ", " + t5 + " )";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(t5);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!super.equals(obj)) return false;
		if (getClass() != obj.getClass()) return false;
		@SuppressWarnings("rawtypes")
		Tuple5 other = (Tuple5) obj;
		return Objects.equals(this.t1, other.t1) && Objects.equals(this.t2, other.t2) && Objects.equals(this.t3, other.t3) && Objects.equals(this.t4, other.t4) && Objects.equals(this.t5, other.t5);
	}

	public T5 getT5() {
		return this.t5;
	}

	public void setT5(T5 t5) {
		this.t5 = t5;
	}

	public Tuple5<T1, T2, T3, T4, T5> operateT5(UnaryOperator<T5> operation) {
		return operateT5(t -> true, operation);
	}

	public Tuple5<T1, T2, T3, T4, T5> operateT5(Predicate<T5> when, UnaryOperator<T5> operation) {
		if (when.test(t5)) {
			t5 = operation.apply(t5);
		}
		return this;
	}

	public Tuple5<T1, T2, T3, T4, T5> operateT5(Predicate<T5> when, UnaryOperator<T5> operation, Supplier<T5> elseOperation) {
		if (when.test(t5)) {
			t5 = operation.apply(t5);
		} else {
			t5 = elseOperation.get();
		}
		return this;
	}

	public Tuple5<T1, T2, T3, T4, T5> consumeT5(Consumer<T5> consumer) {
		return consumeT5(t -> true, consumer);
	}

	public Tuple5<T1, T2, T3, T4, T5> consumeT5(Predicate<T5> when, Consumer<T5> consumer) {
		if (when.test(t5)) {
			consumer.accept(t5);
		}
		return this;
	}

	public Tuple5<T1, T2, T3, T4, T5> consumeT5(Predicate<T5> when, Consumer<T5> consume, Supplier<T5> elseOperation) {
		if (when.test(t5)) {
			consume.accept(t5);
		} else {
			t5 = elseOperation.get();
		}
		return this;
	}

	public <X> Tuple5<T1, T2, T3, T4, X> projectT5(Function<T5, X> operation) {
		return projectT5(t -> true, operation);
	}

	public <X> Tuple5<T1, T2, T3, T4, X> projectT5(Predicate<T5> when, Function<T5, X> operation) {
		return projectT5(when, operation, () -> null);
	}

	public <X> Tuple5<T1, T2, T3, T4, X> projectT5(Predicate<T5> when, Function<T5, X> operation, Supplier<X> elseOperation) {
		return new Tuple5<T1, T2, T3, T4, X>(t1, t2, t3, t4, when.test(t5) ? operation.apply(t5) : elseOperation.get());
	}

	public boolean isT5Null() {
		return t5 == null;
	}

	public boolean isT5NotNull() {
		return t5 != null;
	}

	public Tuple4<T1, T2, T3, T4> popT4() {
		return Tuple4.of(getT1(), getT2(), getT3(), getT4());
	}
}
