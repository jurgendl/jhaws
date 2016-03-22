package org.jhaws.common.lang;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Spliterators;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * @see java.util.stream.Stream
 * @see java.util.stream.StreamSupport
 * @see java.util.stream.Collectors
 * @see java.util.stream.Collector
 * @since 1.8
 */
public interface Collections8 {
	public static class RegexIterator implements Iterator<String> {
		protected Pattern pattern;

		protected String regex;

		protected Matcher matcher;

		protected Boolean hasNext = null;

		public RegexIterator(String text, String regex) {
			this.pattern = Pattern.compile(regex);
			this.regex = regex;
			this.matcher = pattern.matcher(text);
		}

		@Override
		public boolean hasNext() {
			if (hasNext == null) {
				hasNext = matcher.find();
			}
			return hasNext;
		}

		@Override
		public String next() {
			if (hasNext == null) {
				hasNext();
			}
			if (hasNext) {
				hasNext = null;
				return matcher.group();
			}
			return null;
		}
	}

	public static class PathIterator implements Iterator<Path> {
		protected Path path;

		public PathIterator(Path path) {
			this.path = path;
		}

		@Override
		public boolean hasNext() {
			Path parent = this.path.getParent();
			return parent != null;
		}

		@Override
		public Path next() {
			this.path = this.path.getParent();
			return this.path;
		}
	}

	public static interface Opt<T> {
		<X> Opt<X> nest(Function<T, X> getter);

		<X> Opt<X> eager(Function<T, X> getter);

		<X> Opt<X> reusable(Function<T, X> getter);

		T get();

		default <X> X get(Function<T, X> get) {
			return nest(get).get();
		}

		default T or(Supplier<T> supplier) {
			return Optional.ofNullable(get()).orElseGet(supplier);
		}

		public static <T> Opt<T> eager(T value) {
			return new OptEager<>(value);
		}

		public static <T> Opt<T> reusable(T value) {
			return new OptReusable<>(value);
		}
	}

	static class OptEager<T> implements Opt<T> {
		protected final T value;

		protected OptEager(T value) {
			this.value = value;
		}

		@Override
		public T get() {
			return value;
		}

		@Override
		public <P> Opt<P> nest(Function<T, P> get) {
			return eager(get);
		}

		@Override
		public <P> Opt<P> eager(Function<T, P> get) {
			return new OptEager<>(value == null ? null : get.apply(value));
		}

		@Override
		public <P> Opt<P> reusable(Function<T, P> get) {
			return new OptReusable<>(get()).nest(get);
		}
	}

	static class OptReusable<P, T> implements Opt<T> {
		protected final OptReusable<P, T> parent;

		protected final Function<P, T> getter;

		protected final T value;

		protected OptReusable(T value) {
			this.value = value;
			this.parent = null;
			this.getter = null;
		}

		protected OptReusable(OptReusable<P, T> parent, Function<P, T> getter) {
			this.value = null;
			this.parent = parent;
			this.getter = getter;
		}

		@Override
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public T get() {
			if (value != null)
				return value;
			Stack<OptReusable> stack = new Stack<>();
			stack.add(this);
			OptReusable current = this.parent;
			while (current != null) {
				stack.add(current);
				current = current.parent;
			}
			Object currentValue = stack.pop().value;
			while (!stack.isEmpty() && currentValue != null) {
				current = stack.pop();
				currentValue = current.getter.apply(currentValue);
			}
			return (T) currentValue;
		}

		@Override
		public <X> Opt<X> nest(Function<T, X> get) {
			return reusable(get);
		}

		@Override
		public <X> Opt<X> eager(Function<T, X> get) {
			return new OptEager<>(get()).nest(get);
		}

		@Override
		@SuppressWarnings("unchecked")
		public <X> Opt<X> reusable(Function<T, X> get) {
			return new OptReusable<>((OptReusable<T, X>) this, get);
		}
	}

	public static class CustomCollector<T, A, R> implements Collector<T, A, R> {
		protected Supplier<A> supplier;

		protected BiConsumer<A, T> accumulator;

		protected BinaryOperator<A> combiner;

		protected Function<A, R> finisher;

		protected Set<Characteristics> characteristics;

		public CustomCollector(Supplier<A> supplier, BiConsumer<A, T> accumulator, BinaryOperator<A> combiner, Function<A, R> finisher, Set<Characteristics> characteristics) {
			this.supplier = supplier;
			this.accumulator = accumulator;
			this.combiner = combiner;
			this.finisher = finisher;
			this.characteristics = characteristics;
		}

		@SuppressWarnings("unchecked")
		public CustomCollector(Supplier<A> supplier, BiConsumer<A, T> accumulator, BinaryOperator<A> combiner, Set<Characteristics> characteristics) {
			this(supplier, accumulator, combiner, x -> (R) x, characteristics);
		}

		@SuppressWarnings("unchecked")
		public CustomCollector(Supplier<A> supplier, BiConsumer<A, T> accumulator, BinaryOperator<A> combiner) {
			this(supplier, accumulator, combiner, x -> (R) x, Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH)));
		}

		public CustomCollector() {
			super();
		}

		@Override
		public Supplier<A> supplier() {
			return supplier;
		}

		@Override
		public BiConsumer<A, T> accumulator() {
			return accumulator;
		}

		@Override
		public BinaryOperator<A> combiner() {
			return combiner;
		}

		@Override
		public Function<A, R> finisher() {
			return finisher;
		}

		@Override
		public Set<Characteristics> characteristics() {
			return characteristics;
		}

		public Supplier<A> getSupplier() {
			return this.supplier;
		}

		public void setSupplier(Supplier<A> supplier) {
			this.supplier = supplier;
		}

		public BiConsumer<A, T> getAccumulator() {
			return this.accumulator;
		}

		public void setAccumulator(BiConsumer<A, T> accumulator) {
			this.accumulator = accumulator;
		}

		public BinaryOperator<A> getCombiner() {
			return this.combiner;
		}

		public void setCombiner(BinaryOperator<A> combiner) {
			this.combiner = combiner;
		}

		public Function<A, R> getFinisher() {
			return this.finisher;
		}

		public void setFinisher(Function<A, R> finisher) {
			this.finisher = finisher;
		}

		public Set<Characteristics> getCharacteristics() {
			return this.characteristics;
		}

		public void setCharacteristics(Set<Characteristics> characteristics) {
			this.characteristics = characteristics;
		}
	}

	public static class ListCollector<T> extends CustomCollector<T, List<T>, List<T>> {
		public ListCollector() {
			setSupplier(ArrayList::new);
			BiConsumer<List<T>, T> accumulator0 = elementsListAccumulator();
			setAccumulator(accumulator0);
			setCombiner(elementsListCombiner(accumulator0));
			setFinisher(Collections8.cast());
			setCharacteristics(Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH)));
		}
	}

	public static class ListUQCollector<T> extends ListCollector<T> {
		public ListUQCollector() {
			BiConsumer<List<T>, T> accumulator0 = uniqueElementsListAccumulator();
			setAccumulator(accumulator0);
			setCombiner(elementsListCombiner(accumulator0));
		}
	}

	public static <T> BinaryOperator<T> keepFirst() {
		return (p1, p2) -> p1;
	}

	public static <T> BinaryOperator<T> keepLast() {
		return (p1, p2) -> p2;
	}

	/**
	 * BinaryOperator<V> binaryOperator = (k, v) -> k;
	 */
	public static <V> BinaryOperator<V> acceptDuplicateKeys() {
		return keepFirst();
	}

	/**
	 * @throws IllegalArgumentException
	 */
	public static <V> BinaryOperator<V> rejectDuplicateKeys() throws IllegalArgumentException {
		return (a, b) -> {
			if (new EqualsBuilder().append(a, b).isEquals())
				return a;
			throw new IllegalArgumentException("duplicate key: " + a + " <> " + b);
		};
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] array(T item) {
		return (T[]) new Object[] { item };
	}

	public static <T> T[] array(Collection<T> collection) {
		return array(collection.stream());
	}

	public static <T> T[] array(Stream<T> stream) {
		return stream.toArray(newArray());
	}

	@SuppressWarnings("unchecked")
	public static <T> IntFunction<T[]> newArray() {
		return l -> (T[]) new Object[l];
	}

	public static <T> T[] newArray(int size) {
		return Collections8.<T>newArray().apply(size);
	}

	@SuppressWarnings("unchecked")
	public static <K, V> Entry<K, V>[] array(Map<K, V> map) {
		return map.entrySet().toArray((Map.Entry<K, V>[]) new Map.Entry[0]);
	}

	@SuppressWarnings("unchecked")
	public static <X, T> Function<X, T> cast() {
		return x -> (T) x;
	}

	public static <T> Function<Object, T> cast(Class<T> type) {
		return o -> type.cast(o);
	}

	public static <T> T cast(Class<T> type, Object object) {
		return cast(type).apply(object);
	}

	@SuppressWarnings("unchecked")
	public static <T, C extends Collection<T>> Collector<T, ?, C> collector(C collection) {
		if (collection instanceof Deque) {
			return (Collector<T, ?, C>) Collections8.<T>collectDeque();
		}
		if (collection instanceof Queue) {
			return (Collector<T, ?, C>) Collections8.<T>collectQueue();
		}
		if (collection instanceof List) {
			return (Collector<T, ?, C>) Collections8.<T>collectList();
		}
		if (collection instanceof SortedSet) {
			return (Collector<T, ?, C>) Collections8.<T>collectSortedSet();
		}
		if (collection instanceof Set) {
			return (Collector<T, ?, C>) Collections8.<T>collectSet();
		}
		throw new UnsupportedOperationException(collection.getClass().getName());
	}

	@SafeVarargs
	public static <T, C extends Collection<T>> C filter(boolean parallel, C collection, Predicate<? super T>... predicates) {
		AtomicReference<Stream<T>> streamReference = new AtomicReference<>(stream(parallel, collection));
		stream(predicates).forEach(predicate -> streamReference.set(streamReference.get().filter(predicate)));
		C collected = streamReference.get().collect(collector(collection));
		return collected;
	}

	@SafeVarargs
	public static <T, C extends Collection<T>> C filter(C collection, Predicate<? super T>... predicates) {
		return filter(false, collection, predicates);
	}

	public static <T> T last(List<T> dd) {
		int size = dd.size();
		return size == 0 ? null : dd.get(size - 1);
	}

	public static <K, V> Map<K, V> map(Collection<Map.Entry<K, V>> entries) {
		return map(false, entries);
	}

	public static <K, V> Map<K, V> map(boolean parallel, Collection<Map.Entry<K, V>> entries) {
		return map(stream(parallel, entries));
	}

	public static <K, V> Map<K, V> map(Stream<Entry<K, V>> stream) {
		return map(stream, Collections8.<K, V>newMap());
	}

	public static <K, V> Map<K, V> map(Stream<Entry<K, V>> stream, Supplier<? extends Map<K, V>> mapSupplier) {
		Function<Entry<K, V>, K> keyMapper = Collections8.<K, V>keyMapper();
		Function<Entry<K, V>, V> valueMapper = Collections8.<K, V>valueMapper();
		BinaryOperator<V> keepFirst = Collections8.<V>keepFirst();
		Collector<Entry<K, V>, ?, ? extends Map<K, V>> c = Collectors.toMap(keyMapper, valueMapper, keepFirst, mapSupplier);
		Map<K, V> map = stream.collect(c);
		return map;
	}

	public static <K, V> Supplier<Map<K, V>> newLinkedMap() {
		return LinkedHashMap::new;
	}

	public static <K, V> Supplier<? extends Map<K, V>> newMap() {
		return HashMap::new;
	}

	public static <K, V> Supplier<? extends NavigableMap<K, V>> newNavigableMap() {
		return TreeMap::new;
	}

	public static <K, V> Supplier<? extends ConcurrentNavigableMap<K, V>> newConcurrentNavigableMap() {
		return ConcurrentSkipListMap::new;
	}

	public static <K, V> Supplier<? extends ConcurrentMap<K, V>> newConcurrentMap() {
		return ConcurrentHashMap::new;
	}

	public static <T> Supplier<Deque<T>> newDeque() {
		return LinkedList::new;
	}

	public static <T> Supplier<List<T>> newList() {
		return ArrayList::new;
	}

	public static <T> Supplier<Queue<T>> newQueue() {
		return LinkedList::new;
	}

	public static <T> Supplier<Set<T>> newSet() {
		return HashSet::new;
	}

	public static <T> Supplier<SortedSet<T>> newSortedSet() {
		return TreeSet::new;
	}

	public static <T> Supplier<BlockingQueue<T>> newBlockingQueue() {
		return LinkedBlockingDeque::new;
	}

	public static <T> Supplier<BlockingDeque<T>> newBlockingDeque() {
		return LinkedBlockingDeque::new;
	}

	public static <T> Supplier<TransferQueue<T>> newTransferQueue() {
		return LinkedTransferQueue::new;
	}

	public static <T, C extends Collection<T>> Collector<T, ?, C> toCollector(Supplier<C> supplier) {
		return Collectors.toCollection(supplier);
	}

	public static <T> Collector<T, ?, Deque<T>> collectDeque() {
		return toCollector(newDeque());
	}

	public static <T> Collector<T, ?, List<T>> collectList() {
		return Collectors.toList();
	}

	public static <T> ListCollector<T> collectListUQ() {
		return new ListUQCollector<>();
	}

	public static <T> Collector<T, ?, Queue<T>> collectQueue() {
		return toCollector(newQueue());
	}

	public static <T> Collector<T, ?, Set<T>> collectSet() {
		return Collectors.toSet();
	}

	public static <T> Collector<T, ?, SortedSet<T>> collectSortedSet() {
		return toCollector(newSortedSet());
	}

	/**
	 * Supplier<SortedMap<K, V>> mapSupplier = TreeMap::new;
	 */
	public static <K, V> Supplier<SortedMap<K, V>> newSortedMap() {
		return TreeMap::new;
	}

	public static <T extends Comparable<? super T>> List<T> sort(Collection<T> collection) {
		return sort(false, collection);
	}

	public static <T extends Comparable<? super T>> List<T> sort(boolean parallel, Collection<T> collection) {
		return stream(parallel, collection).sorted().collect(toCollector(newList()));
	}

	public static <T> List<T> sort(boolean parallel, Collection<T> collection, Comparator<? super T> comparator) {
		return stream(parallel, collection).sorted(comparator).collect(toCollector(newList()));
	}

	public static <T> List<T> sort(Collection<T> collection, Comparator<? super T> comparator) {
		return sort(false, collection, comparator);
	}

	public static <T, A> List<T> sortBy(Collection<T> collection, List<A> orderByMe, Function<T, A> map) {
		return collection.stream().sorted(sortBy(orderByMe, map)).collect(collectList());
	}

	public static <T, A> Comparator<T> sortBy(List<A> orderByMe, Function<T, A> map) {
		return (x, y) -> new Integer(noNegIndex(orderByMe.indexOf(map.apply(x)))).compareTo(new Integer(noNegIndex(orderByMe.indexOf(map.apply(y)))));
	}

	public static <T> Comparator<T> sortBy(List<T> orderByMe) {
		return sortBy(orderByMe, self());
	}

	public static <T> List<T> sortBy(Collection<T> collection, List<T> orderByMe) {
		return sortBy(collection, orderByMe, self());
	}

	public static <T> Function<T, T> id() {
		return self();
	}

	public static <T> Function<T, T> self() {
		return Function.identity();
	}

	static int noNegIndex(int i) {
		return i == -1 ? Integer.MAX_VALUE : i;
	}

	public static <T> Stream<T> stream(Collection<T> collection) {
		return stream(false, collection);
	}

	public static <T> Stream<T> stream(boolean parallel, Collection<T> collection) {
		return collection == null ? Stream.empty() : StreamSupport.stream(collection.spliterator(), parallel);
	}

	public static <T> Stream<T> stream(Iterable<T> iterable) {
		return stream(false, iterable);
	}

	public static <T> Stream<T> stream(Iterator<T> iterator) {
		return stream(false, iterator);
	}

	public static <T> Stream<T> stream(boolean parallel, Iterable<T> iterable) {
		return iterable == null ? Stream.empty() : StreamSupport.stream(iterable.spliterator(), parallel);
	}

	public static <T> Stream<T> stream(boolean parallel, Iterator<T> iterator) {
		return iterator == null ? Stream.empty() : StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), parallel);
	}

	public static Stream<String> lines(Path path) {
		try {
			return Files.lines(path);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	public static <K, V> Stream<V> streamValues(Map<K, V> map) {
		return stream(map).map(valueMapper());
	}

	public static <K, V> Stream<Stream<V>> streamDeepValues(Map<K, ? extends Collection<V>> map) {
		return stream(map).map(valueMapper()).map(collection -> stream(collection));
	}

	public static <K, V> Stream<Map.Entry<K, V>> stream(Map<K, V> map) {
		return stream(false, map);
	}

	public static <K, V> Stream<Map.Entry<K, V>> stream(boolean parallel, Map<K, V> map) {
		return map == null ? Stream.empty() : StreamSupport.stream(map.entrySet().spliterator(), parallel);
	}

	public static Stream<Path> stream(Path path) {
		return stream(false, path);
	}

	public static Stream<Path> stream(boolean parallel, Path path) {
		return StreamSupport.stream(path.spliterator(), parallel);
	}

	@SafeVarargs
	public static <T> Stream<T> stream(T... array) {
		return stream(false, array);
	}

	@SafeVarargs
	public static <T> Stream<T> stream(boolean parallel, T... array) {
		if (array == null)
			return Stream.empty();
		// StreamSupport.stream(Spliterators.spliterator(array, 0, array.length, Spliterator.ORDERED | Spliterator.IMMUTABLE), parallel);
		Stream<T> stream = Arrays.stream(array);
		if (parallel)
			stream = stream.parallel();
		return stream;
	}

	public static <T> Stream<T> streamDetached(Collection<T> collection) {
		return streamDetached(false, collection);
	}

	public static <T> Stream<T> streamDetached(boolean parallel, Collection<T> collection) {
		return stream(parallel, array(collection));
	}

	public static <K, V> Stream<Map.Entry<K, V>> streamDetached(Map<K, V> map) {
		return streamDetached(false, map);
	}

	public static <K, V> Stream<Map.Entry<K, V>> streamDetached(boolean parallel, Map<K, V> map) {
		return stream(parallel, array(map));
	}

	public static Stream<Path> streamFully(Path path) {
		return stream(new PathIterator(path));
	}

	public static <T, U> Stream<U> stream(Collection<T> input, Function<T, Collection<U>> mapping) {
		return input.stream().map(mapping).map(Collection::stream).flatMap(id());
	}

	public static <T> List<T> toList(Collection<T> collection) {
		return stream(collection).collect(collectList());
	}

	@SafeVarargs
	public static <T> List<T> toList(T... array) {
		return stream(array).collect(collectList());
	}

	public static <T> Set<T> toSet(Collection<T> collection) {
		return stream(collection).collect(collectSet());
	}

	@SafeVarargs
	public static <T> Set<T> toSet(T... array) {
		return stream(array).collect(collectSet());
	}

	public static <T> Set<T> toSortedSet(Collection<T> collection) {
		return stream(collection).collect(collectSortedSet());
	}

	@SafeVarargs
	public static <T> SortedSet<T> toSortedSet(T... array) {
		return stream(array).collect(collectSortedSet());
	}

	public static <T> Queue<T> toQueue(Collection<T> collection) {
		return stream(collection).collect(collectQueue());
	}

	@SafeVarargs
	public static <T> Queue<T> toQueue(T... array) {
		return stream(array).collect(collectQueue());
	}

	public static <T> Deque<T> toDeque(Collection<T> collection) {
		return stream(collection).collect(collectDeque());
	}

	@SafeVarargs
	public static <T> Deque<T> toDeque(T... array) {
		return stream(array).collect(collectDeque());
	}

	public static <T, C extends Collection<T>> C to(T[] array, Supplier<C> supplier) {
		return to(array, toCollector(supplier));
	}

	public static <T, C extends Collection<T>> C to(T[] array, Collector<T, ?, C> collector) {
		return stream(array).collect(collector);
	}

	public static <T, C extends Collection<T>> C to(Collection<T> collection, Supplier<C> supplier) {
		return to(collection, toCollector(supplier));
	}

	public static <T, C extends Collection<T>> C to(Collection<T> collection, Collector<T, ?, C> collector) {
		return stream(collection).collect(collector);
	}

	public static <T> boolean containedInAny(Collection<T> c1, Collection<T> c2) {
		return c1.stream().anyMatch(c2::contains);
	}

	public static <T> Predicate<T> not(Predicate<T> t) {
		return t.negate();
	}

	public static <T> Predicate<T> notNull() {
		return Objects::nonNull;
	}

	public static <T> Predicate<T> isNull() {
		return Objects::isNull;
	}

	public static <T> Predicate<T> is(T x) {
		return t -> isEquals(t, x);
	}

	public static <T> Predicate<T> isNot(T x) {
		return not(is(x));
	}

	public static <E> Predicate<E> containedIn(Collection<E> collection) {
		return collection::contains;
	}

	public static <T, E> Predicate<T> containedIn(Collection<E> collection, Function<T, E> converter) {
		return e -> collection.contains(converter.apply(e));
	}

	public static <K> Predicate<K> keyContainedIn(Map<K, ?> map) {
		return map::containsKey;
	}

	public static <T, K> Predicate<T> keyContainedIn(Map<K, ?> map, Function<T, K> converter) {
		return e -> map.containsKey(converter.apply(e));
	}

	public static <V> Predicate<V> valueContainedIn(Map<?, V> map) {
		return map::containsValue;
	}

	public static <T, V> Predicate<T> valueContainedIn(Map<?, V> map, Function<T, V> converter) {
		return e -> map.containsValue(converter.apply(e));
	}

	public static <E> Predicate<E> notContainedIn(Collection<E> collection) {
		return not(containedIn(collection));
	}

	public static <T, E> Predicate<T> notContainedIn(Collection<E> collection, Function<T, E> converter) {
		return not(containedIn(collection, converter));
	}

	public static <K> Predicate<K> keyNotContainedIn(Map<K, ?> map) {
		return not(keyContainedIn(map));
	}

	public static <T, K> Predicate<T> keyNotContainedIn(Map<K, ?> map, Function<T, K> converter) {
		return not(keyContainedIn(map, converter));
	}

	public static <V> Predicate<V> valueNotContainedIn(Map<?, V> map) {
		return not(valueContainedIn(map));
	}

	public static <T, V> Predicate<T> valueNotContainedIn(Map<?, V> map, Function<T, V> converter) {
		return not(valueContainedIn(map, converter));
	}

	public static <X, Y> Function<X, Y> makeNull() {
		return x -> null;
	}

	public static <X> Supplier<X> supplyNull() {
		return () -> null;
	}

	public static <X> Predicate<X> always() {
		return x -> true;
	}

	public static <X> Predicate<X> never() {
		return x -> false;
	}

	public static IntStream streamp(String text) {
		return text.chars();
	}

	public static Stream<Character> stream(String text) {
		return streamp(text).mapToObj(i -> (char) i);
	}

	public static <T, S extends Comparable<? super S>> List<T> sortBy(Collection<T> sortMe, Map<T, S> sortByMe) {
		return sortMe.stream().sorted((x, y) -> sortByMe.get(x).compareTo(sortByMe.get(y))).collect(collectList());
	}

	public static <T> List<Pair<T>> match(List<T> keys, List<T> values) {
		return values.stream().parallel().filter(containedIn(keys)).map(value -> new Pair<>(keys.get(keys.indexOf(value)), value)).collect(collectList());
	}

	public static <T> T optional(T value, Supplier<T> orElse) {
		return Optional.ofNullable(value).orElseGet(orElse);
	}

	public static <T> T optional(T oldValue, T newValue, Consumer<T> whenDifferent) {
		if (notEquals(oldValue, newValue)) {
			whenDifferent.accept(newValue);
			return newValue;
		}
		return oldValue;
	}

	public static <T> Consumer<T> consume() {
		return x -> {
			//
		};
	}

	public static <T, U> BiConsumer<T, U> biconsume() {
		return (x, y) -> {
			//
		};
	}

	public static String join(Collection<String> strings) {
		return join(strings, " ");
	}

	public static String join(Collection<String> strings, String delimiter) {
		return stream(strings).collect(Collectors.joining(delimiter));
	}

	/**
	 * stackoverflow.com/questions/24010109/java-8-stream-reverse-order
	 */
	@SuppressWarnings("unchecked")
	public static <T> Stream<T> reverse(Stream<T> input) {
		Object[] temp = input.toArray();
		return (Stream<T>) IntStream.range(0, temp.length).mapToObj(i -> temp[temp.length - i - 1]);
	}

	public static boolean notEquals(Object first, Object second) {
		return !isEquals(first, second);
	}

	public static boolean isEquals(Object first, Object second) {
		if (first == second)
			return true;
		if (first == null && second == null)
			return true;
		if (first == null || second == null)
			return false;
		return first.equals(second);
	}

	public static <V, K> Map<K, List<V>> groupBy(Stream<V> stream, Function<V, K> groupBy) {
		return stream.collect(Collectors.groupingBy(groupBy));
	}

	public static <T> Comparator<T> dummyComparator() {
		return (x, y) -> 0;
	}

	public static <T> Collection<Collection<T>> split(Collection<T> all, int maxSize) {
		if (maxSize < 1)
			throw new IllegalArgumentException("maxSize<1");
		if (all.size() <= maxSize)
			return Arrays.asList(all);
		int totalSize = all.size();
		AtomicInteger ai = new AtomicInteger(0);
		int groups = (totalSize / maxSize) + (totalSize % maxSize > 0 ? 1 : 0);
		Map<Integer, List<T>> g = all.stream().parallel().collect(Collectors.groupingBy(s -> ai.addAndGet(1) % groups));
		return stream(g).map(valueMapper()).collect(collectList());
	}

	public static <K, V> Function<Entry<K, V>, V> valueMapper() {
		return e -> e.getValue();
	}

	public static <K, V> Function<Entry<K, V>, K> keyMapper() {
		return e -> e.getKey();
	}

	public static Stream<String> regex(String text, String regex) {
		return stream(new RegexIterator(text, regex));
	}

	public static <T> T[] copy(T[] array) {
		return Arrays.copyOf(array, array.length);
	}

	@SafeVarargs
	public static <K, V> Stream<Entry<K, V>> streamMaps(Map<K, V>... maps) {
		return Stream.of(maps).map(Map::entrySet).flatMap(Collection::stream);
	}

	public static <T, K, V> Collector<T, ?, Map<K, V>> collectMap(Function<T, K> keyMapper, Function<T, V> valueMapper, BinaryOperator<V> duplicateValues) {
		return Collectors.toMap(keyMapper, valueMapper, duplicateValues);
	}

	public static <K, V> Collector<V, ?, Map<K, V>> collectMap(Function<V, K> keyMapper, BinaryOperator<V> duplicateValues) {
		return collectMap(keyMapper, self(), duplicateValues);
	}

	public static <K, V> Collector<V, ?, Map<K, V>> collectMap(Function<V, K> keyMapper) {
		return collectMap(keyMapper, keepFirst());
	}

	public static <T extends Comparable<? super T>> Comparator<T> natural() {
		return Comparator.<T>naturalOrder();
	}

	public static <T extends Comparable<? super T>> BinaryOperator<T> keepMin() {
		return keepMin(Collections8.<T>natural());
	}

	public static <T> BinaryOperator<T> keepMin(Comparator<T> comparator) {
		return BinaryOperator.minBy(comparator);
	}

	public static <T extends Comparable<? super T>> BinaryOperator<T> keepMax() {
		return keepMax(Collections8.<T>natural());
	}

	public static <T> BinaryOperator<T> keepMax(Comparator<T> comparator) {
		return BinaryOperator.maxBy(comparator);
	}

	public static IntStream streamInt(int max) {
		return streamInt(0, 1, max);
	}

	public static LongStream streamLong(long max) {
		return streamLong(0, 1, max);
	}

	public static DoubleStream streamDouble(double max) {
		return streamDouble(0.0, 1.0, max);
	}

	public static IntStream streamInt(int start, int max) {
		return streamInt(start, 1, max);
	}

	public static LongStream streamLong(long start, long max) {
		return streamLong(start, 1, max);
	}

	public static DoubleStream streamDouble(double start, double max) {
		return streamDouble(start, 1.0, max);
	}

	public static IntStream streamInt(int start, int step, int max) {
		return IntStream.iterate(start, i -> i + step).limit(max - start);
	}

	public static LongStream streamLong(long start, long step, long max) {
		return LongStream.iterate(start, i -> i + step).limit(max - start);
	}

	public static DoubleStream streamDouble(double start, double step, double max) {
		return DoubleStream.iterate(start, i -> i + step).limit((long) (max - start));
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] toArray(Collection<T> collection) {
		return (T[]) collection.toArray();
	}

	public static <T> Stream<T> concatenate(Stream<Collection<T>> streams) {
		return concatenateStreams(streams.map(Collection::stream));
	}

	public static <T> Stream<T> concatenate(Collection<Stream<T>> collentionStreams) {
		return concatenateStreams(collentionStreams.stream());
	}

	public static <T> Stream<T> concatenateStreams(Stream<Stream<T>> streamStreams) {
		return streamStreams.flatMap(Function.identity());
	}

	public static <T> Opt<T> eager(T value) {
		return Opt.eager(value);
	}

	public static <T> Opt<T> reusable(T value) {
		return Opt.reusable(value);
	}

	/**
	 * c1 - c2, does not change input collections
	 */
	public static <T> Collection<T> subtract(Collection<T> c1, Collection<T> c2) {
		Collection<T> tmp = new ArrayDeque<T>(c1);
		tmp.removeAll(c2);
		return tmp;
	}

	/**
	 * intersection of c1 and c2, does not change input collections
	 */
	public static <T> Collection<T> intersection(Collection<T> c1, Collection<T> c2) {
		return c1.stream().filter(c2::contains).collect(collectList());
	}

	/**
	 * intersection of c1 and c2, does not change input collections
	 */
	public static <T> Collection<T> union(Collection<T> c1, Collection<T> c2) {
		Collection<T> tmp = new ArrayDeque<T>(c1.size() + c2.size());
		tmp.addAll(c1);
		tmp.addAll(c2);
		return tmp;
	}

	/**
	 * aanTeMakenAct roept teBewaren op met als nieuw object aangeboden door param constructor, constructor en teBewaren zijn niet nullable
	 * 
	 * @see #sync(Map, Map, BiConsumer, BiConsumer, BiConsumer)
	 */
	public static <K, N, B> void sync(//
			Map<K, N> nieuwe, //
			Map<K, B> bestaande, //
			BiConsumer<K, B> teVerwijderenAct, //
			Supplier<B> constructor, //
			BiConsumer<N, B> teBewaren//
	) {
		if (constructor == null || teBewaren == null)
			throw new NullPointerException();
		sync(nieuwe, bestaande, teVerwijderenAct, (k, i) -> teBewaren.accept(i, constructor.get()), teBewaren);
	}

	/**
	 * synchroniseer een nieuwe collectie met een bestaande collecte
	 *
	 * @param nieuwe
	 *            nieuwe collectie vooraf gemapt op key, zie {@link #getMap(Collection, Function)}
	 * @param bestaande
	 *            bestaande collectie vooraf gemapt op key, zie {@link #getMap(Collection, Function)}
	 * @param teVerwijderenAct
	 *            nullable, actie op te roepen indien verwijderen, krijgt key en bestaand object binnen
	 * @param aanTeMakenAct
	 *            nullable, actie op te roepen indien nieuw object aan te maken, krijgt key en nieuw object binnen
	 * @param teBewaren
	 *            nullable, actie op te roepen indien overeenkomst, krijgt key en nieuw en bestaand object binnen
	 * 
	 * @param <K>
	 *            key waarop vergeleken moet worden
	 * @param <N>
	 *            nieuwe object
	 * @param <B>
	 *            bestaand object
	 */
	public static <K, N, B> void sync(//
			Map<K, N> nieuwe, //
			Map<K, B> bestaande, //
			BiConsumer<K, B> teVerwijderenAct, //
			BiConsumer<K, N> aanTeMakenAct, //
			BiConsumer<N, B> teBewaren//
	) {
		Map<K, B> teVerwijderen = subtract(bestaande.keySet(), nieuwe.keySet()).stream().collect(Collectors.toMap(Function.identity(), bestaande::get));
		Map<K, N> aanTeMaken = subtract(nieuwe.keySet(), bestaande.keySet()).stream().collect(Collectors.toMap(Function.identity(), nieuwe::get));
		Map<N, B> overeenkomst = intersection(nieuwe.keySet(), bestaande.keySet()).stream().collect(Collectors.toMap(nieuwe::get, bestaande::get));
		if (teBewaren != null)
			overeenkomst.forEach(teBewaren);
		if (aanTeMakenAct != null)
			aanTeMaken.forEach(aanTeMakenAct);
		if (teVerwijderenAct != null)
			teVerwijderen.forEach(teVerwijderenAct);
	}

	public static <K, V> Map<K, V> getMap(Collection<V> values, Function<V, K> keyMapper) {
		return values.stream().collect(Collectors.toMap(keyMapper, self()));
	}

	public static <K, V> Map<K, V> getMap(Collection<V> values, Supplier<K> keyMapper) {
		return values.stream().collect(Collectors.toMap(supplierToFunction(keyMapper), self()));
	}

	public static <K, V> Map<K, V> getKeyMap(Collection<K> values, Function<K, V> valueMapper) {
		return values.stream().collect(Collectors.toMap(self(), valueMapper));
	}

	public static <K, V> Map<K, V> getKeyMap(Collection<K> values, Supplier<V> valueMapper) {
		return values.stream().collect(Collectors.toMap(self(), supplierToFunction(valueMapper)));
	}

	public static <S, X> Function<X, S> supplierToFunction(Supplier<S> supplier) {
		return any -> supplier.get();
	}

	public static <T> Supplier<T> supplyAny(T object) {
		return () -> object;
	}

	public static <T> BiConsumer<List<T>, T> elementsListAccumulator() {
		return (List<T> a, T t) -> a.add(t);
	}

	public static <T> BiConsumer<List<T>, T> uniqueElementsListAccumulator() {
		return (List<T> a, T t) -> {
			if (!a.contains(t))
				a.add(t);
		};
	}

	public static <T> BinaryOperator<List<T>> elementsListCombiner(BiConsumer<List<T>, T> elementsListAccumulator) {
		return (List<T> a1, List<T> a2) -> {
			a2.stream().forEach(a2e -> elementsListAccumulator.accept(a1, a2e));
			return a1;
		};
	}
}
