package org.jhaws.common.lang;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Spliterator;
import java.util.Spliterators;
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
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * @see java.util.stream.Stream
 * @see java.util.stream.StreamSupport
 * @see java.util.stream.Collectors
 * @see java.util.stream.Collector
 * 
 * @since 1.8
 */
public interface Collections8 {
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

	/**
	 * BinaryOperator<V> binaryOperator = (k, v) -> k;
	 */
	public static <V> BinaryOperator<V> acceptDuplicateKeys() {
		return (k, v) -> k;
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
		return Collections8.<T> newArray().apply(size);
	}

	@SuppressWarnings("unchecked")
	public static <K, V> Entry<K, V>[] array(Map<K, V> map) {
		return map.entrySet().toArray((Map.Entry<K, V>[]) new Map.Entry[0]);
	}

	@SuppressWarnings("unchecked")
	public static <T> Function<Object, T> cast() {
		return o -> (T) o;
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
			return (Collector<T, ?, C>) Collections8.<T> collectDeque();
		}
		if (collection instanceof Queue) {
			return (Collector<T, ?, C>) Collections8.<T> collectQueue();
		}
		if (collection instanceof List) {
			return (Collector<T, ?, C>) Collections8.<T> collectList();
		}
		if (collection instanceof SortedSet) {
			return (Collector<T, ?, C>) Collections8.<T> collectSortedSet();
		}
		if (collection instanceof Set) {
			return (Collector<T, ?, C>) Collections8.<T> collectSet();
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
		return map(stream, Collections8.<K, V> newMap());
	}

	public static <K, V> Map<K, V> map(Stream<Entry<K, V>> stream, Supplier<? extends Map<K, V>> mapSupplier) {
		return stream.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), rejectDuplicateKeys(), mapSupplier));
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
		return Collections8.toCollector(Collections8.newDeque());
	}

	public static <T> Collector<T, ?, List<T>> collectList() {
		return Collections8.toCollector(Collections8.newList());
	}

	public static <T> Collector<T, ?, Queue<T>> collectQueue() {
		return Collections8.toCollector(Collections8.newQueue());
	}

	public static <T> Collector<T, ?, Set<T>> collectSet() {
		return Collections8.toCollector(Collections8.newSet());
	}

	public static <T> Collector<T, ?, SortedSet<T>> collectSortedSet() {
		return Collections8.toCollector(Collections8.newSortedSet());
	}

	/**
	 * Supplier<SortedMap<K, V>> mapSupplier = TreeMap::new;
	 */
	public static <K, V> Supplier<SortedMap<K, V>> newSortedMap() {
		return TreeMap::new;
	}

	/**
	 * @throws IllegalArgumentException
	 */
	public static <V> BinaryOperator<V> rejectDuplicateKeys() {
		return (k, v) -> {
			throw new IllegalArgumentException("duplicate key");
		};
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

	public static <T, P> List<T> sortBy(Collection<T> collection, List<P> orderByMe, Function<T, P> map) {
		return collection.stream()
				.sorted((o1, o2) -> new CompareToBuilder().append(noNegIndex(orderByMe.indexOf(map.apply(o1))), noNegIndex(orderByMe.indexOf(map.apply(o2)))).toComparison())
				.collect(collectList());
	}

	public static <T, P> List<T> sortBy(Collection<T> collection, List<T> orderByMe) {
		return sortBy(collection, orderByMe, id());
	}

	public static <T> Function<T, T> id() {
		return Function.identity();
	}

	static int noNegIndex(int i) {
		return i == -1 ? Integer.MAX_VALUE : i;
	}

	public static <T> Stream<T> stream(Collection<T> collection) {
		return stream(false, collection);
	}

	public static <T> Stream<T> stream(boolean parallel, Collection<T> collection) {
		return StreamSupport.stream(collection.spliterator(), parallel);
	}

	public static <T> Stream<T> stream(Iterable<T> iteratable) {
		return StreamSupport.stream(iteratable.spliterator(), false);
	}

	public static <T> Stream<T> stream(Iterator<T> iterator) {
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), false);
	}

	public static Stream<String> line(Path path) {
		try {
			return Files.lines(path);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	public static <K, V> Stream<Map.Entry<K, V>> stream(Map<K, V> map) {
		return stream(false, map);
	}

	public static <K, V> Stream<Map.Entry<K, V>> stream(boolean parallel, Map<K, V> map) {
		return StreamSupport.stream(map.entrySet().spliterator(), parallel);
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
		return StreamSupport.stream(Spliterators.spliterator(array, 0, array.length, Spliterator.ORDERED | Spliterator.IMMUTABLE), parallel);
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

	public static <T> boolean containsAny(Collection<T> c1, Collection<T> c2) {
		return c1.stream().anyMatch(c2::contains);
	}

	public static <T> Predicate<T> not(Predicate<T> t) {
		return t.negate();
	}

	public static <T> Predicate<T> notNull() {
		return t -> t != null;
	}

	public static <T> Predicate<T> isNull() {
		return t -> t == null;
	}

	public static <T> Predicate<T> is(T x) {
		return t -> new EqualsBuilder().append(t, x).isEquals();
	}

	public static <T> Predicate<T> isNot(T x) {
		return not(is(x));
	}

	public static <E> Predicate<E> contains(Collection<E> collection) {
		return collection::contains;
	}

	public static <T, E> Predicate<T> contains(Collection<E> collection, Function<T, E> converter) {
		return e -> collection.contains(converter.apply(e));
	}

	public static <K> Predicate<K> containsKey(Map<K, ?> map) {
		return map::containsKey;
	}

	public static <T, K> Predicate<T> containsKey(Map<K, ?> map, Function<T, K> converter) {
		return e -> map.containsKey(converter.apply(e));
	}

	public static <V> Predicate<V> containsValue(Map<?, V> map) {
		return map::containsValue;
	}

	public static <T, V> Predicate<T> containsValue(Map<?, V> map, Function<T, V> converter) {
		return e -> map.containsValue(converter.apply(e));
	}

	public static <E> Predicate<E> containsNot(Collection<E> collection) {
		return not(contains(collection));
	}

	public static <T, E> Predicate<T> containsNot(Collection<E> collection, Function<T, E> converter) {
		return not(contains(collection, converter));
	}

	public static <K> Predicate<K> containsNotKey(Map<K, ?> map) {
		return not(containsKey(map));
	}

	public static <T, K> Predicate<T> containsNotKey(Map<K, ?> map, Function<T, K> converter) {
		return not(containsKey(map, converter));
	}

	public static <V> Predicate<V> containsNotValue(Map<?, V> map) {
		return not(containsValue(map));
	}

	public static <T, V> Predicate<T> containsNotValue(Map<?, V> map, Function<T, V> converter) {
		return not(containsValue(map, converter));
	}

	public static <X, Y> Function<X, Y> makeNull() {
		return x -> null;
	}

	public static <X> Supplier<X> getNull() {
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
		return sortMe.stream().sorted((x, y) -> new CompareToBuilder().append(sortByMe.get(x), sortByMe.get(y)).toComparison()).collect(collectList());
	}

	public static <T> List<KeyValue<T, T>> match(List<T> keys, List<T> values) {
		return values.stream().parallel().filter(Collections8.contains(keys)).map(value -> new KeyValue<>(keys.get(keys.indexOf(value)), value))
				.collect(Collections8.collectList());
	}
}
