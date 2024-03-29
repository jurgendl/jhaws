package org.jhaws.common.lang;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;

@SuppressWarnings("serial")
@XmlRootElement
public class Tuple2<T1, T2> extends Tuple1<T1> {
	public static <T1, T2> Tuple2<T1, T2> of(T1 t1, T2 t2) {
		return new Tuple2<>(t1, t2);
	}

	public static <T1, T2> Tuple2<T1, T2> tuple2(T1 t1, T2 t2) {
		return new Tuple2<>(t1, t2);
	}

	public static <T1, T2> Tuple2<T1, T2> of(Map.Entry<T1, T2> entry) {
		return new Tuple2<>(entry.getKey(), entry.getValue());
	}

	public static <T1, T2> Tuple2<T1, T2> tuple2(Map.Entry<T1, T2> entry) {
		return new Tuple2<>(entry.getKey(), entry.getValue());
	}

	protected T2 t2;

	public Tuple2() {
		super();
	}

	public Tuple2(T1 t1, T2 t2) {
		super(t1);
		this.t2 = t2;
	}

	public Tuple2(Map.Entry<T1, T2> entry) {
		super(entry.getKey());
		this.t2 = entry.getValue();
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
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("rawtypes")
		Tuple2 other = (Tuple2) obj;
		return Objects.equals(this.t1, other.t1) && Objects.equals(this.t2, other.t2);
	}

	public <I> boolean isEquals(Function<T1, I> kf, Function<T2, I> vf) {
		return new EqualsBuilder().append(kf.apply(t1), vf.apply(t2)).isEquals();
	}

	public <I> boolean notEquals(Function<T1, I> kf, Function<T2, I> vf) {
		return !isEquals(kf, vf);
	}

	public boolean isEquals() {
		return new EqualsBuilder().append(t1, t2).isEquals();
	}

	public boolean notEquals() {
		return !isEquals();
	}

	public T2 getT2() {
		return this.t2;
	}

	public void setT2(T2 t2) {
		this.t2 = t2;
	}

	public T2 t2() {
		return this.t2;
	}

	public Tuple2<T1, T2> t2(T2 t2) {
		this.t2 = t2;
		return this;
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

	public boolean isT2Null() {
		return t2 == null;
	}

	public boolean isT2NotNull() {
		return t2 != null;
	}

	public <T3> Tuple3<T1, T2, T3> pushT3(T3 t3) {
		return Tuple3.of(getT1(), getT2(), t3);
	}

	public Tuple1<T1> popT1() {
		return Tuple1.of(getT1());
	}

	public Map.Entry<T1, T2> toMapEntry() {
		return Map.entry(getT1(), getT2());
	}

	public Collector<Tuple2<T1, T2>, ?, Map<T1, T2>> collectMap() {
		Collector<Tuple2<T1, T2>, ?, Map<T1, T2>> map = Collectors.toMap(Tuple2::getT1, Tuple2::getT2);
		return map;
	}

	public static <T1, T2> Collector<Tuple2<T1, T2>, ?, Map<T1, T2>> collectTupleToMap() {
		Collector<Tuple2<T1, T2>, ?, Map<T1, T2>> map = Collectors.toMap(Tuple2::getT1, Tuple2::getT2);
		return map;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T get(int i) throws IndexOutOfBoundsException {
		if (i == 1)
			return (T) getT2();
		return super.get(i);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> void set(int i, T t) throws IndexOutOfBoundsException {
		if (i == 1)
			setT2((T2) t);
		else
			super.set(i, t);
	}

	public Tuple2<T1, T2> asT2() {
		return this;
	}
}
