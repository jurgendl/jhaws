package org.jhaws.common.lang;

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
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.TreeMap;
import java.util.TreeSet;
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
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.builder.CompareToBuilder;

/**
 * @see java.util.stream.Stream
 * @see java.util.stream.StreamSupport
 * @see java.util.stream.Collectors
 * @see java.util.stream.Collector
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
			return (Collector<T, ?, C>) Collections8.<T> newDeque();
		}
		if (collection instanceof Queue) {
			return (Collector<T, ?, C>) Collections8.<T> newQueue();
		}
		if (collection instanceof List) {
			return (Collector<T, ?, C>) Collections8.<T> newList();
		}
		if (collection instanceof SortedSet) {
			return (Collector<T, ?, C>) Collections8.<T> newSortedSet();
		}
		if (collection instanceof Set) {
			return (Collector<T, ?, C>) Collections8.<T> newSet();
		}
		throw new UnsupportedOperationException(collection.getClass().getName());
	}

	@SafeVarargs
	public static <T, C extends Collection<T>> C filter(C collection, boolean parallel, Predicate<? super T>... predicates) {
		AtomicReference<Stream<T>> streamReference = new AtomicReference<>(stream(collection, parallel));
		stream(predicates).forEach((predicate) -> streamReference.set(streamReference.get().filter(predicate)));
		C collected = streamReference.get().collect(collector(collection));
		return collected;
	}

	@SafeVarargs
	public static <T, C extends Collection<T>> C filter(C collection, Predicate<? super T>... predicates) {
		return filter(collection, false, predicates);
	}

	public static <T> T last(List<T> dd) {
		int size = dd.size();
		return size == 0 ? null : dd.get(size - 1);
	}

	public static <K, V> Map<K, V> map(Collection<Map.Entry<K, V>> entries) {
		return map(entries, false);
	}

	public static <K, V> Map<K, V> map(Collection<Map.Entry<K, V>> entries, boolean parallel) {
		return map(stream(entries, parallel));
	}

	public static <K, V> Map<K, V> map(Stream<Entry<K, V>> stream) {
		return map(stream, Collections8.<K, V> newMap());
	}

	public static <K, V> Map<K, V> map(Stream<Entry<K, V>> stream, Supplier<Map<K, V>> mapSupplier) {
		return stream.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), rejectDuplicateKeys(), mapSupplier));
	}

	/**
	 * Supplier<Map<K, V>> mapSupplier = LinkedHashMap::new;
	 */
	public static <K, V> Supplier<Map<K, V>> newLinkedMap() {
		return LinkedHashMap::new;
	}

	/**
	 * Supplier<Map<K, V>> mapSupplier = HashMap::new;
	 */
	public static <K, V> Supplier<Map<K, V>> newMap() {
		return HashMap::new;
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

	public static <T, C extends Collection<T>> Collector<T, ?, C> dequeCollector() {
		return toCollector(newDeque());
	}

	public static <T, C extends Collection<T>> Collector<T, ?, C> listCollector() {
		return toCollector(newList());
	}

	public static <T, C extends Collection<T>> Collector<T, ?, C> queueCollector() {
		return toCollector(newQueue());
	}

	public static <T, C extends Set<T>> Collector<T, ?, C> setCollector() {
		return toCollector(newSet());
	}

	public static <T, C extends Collection<T>> Collector<T, ?, C> toCollector(Supplier<C> supplier) {
		return Collectors.toCollection(supplier);
	}

	/**
	 * Supplier<SortedMap<K, V>> mapSupplier = TreeMap::new;
	 */
	public static <K, V> Supplier<SortedMap<K, V>> newSortedMap() {
		return TreeMap::new;
	}

	public static <T> Collector<T, ?, SortedSet<T>> newSortedSet() {
		return Collectors.toCollection(TreeSet::new);
	}

	public static <T> Collector<T, ?, TransferQueue<T>> newTransferQueue() {
		return Collectors.toCollection(LinkedTransferQueue::new);
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
		return sort(collection, false);
	}

	public static <T extends Comparable<? super T>> List<T> sort(Collection<T> collection, boolean parallel) {
		return stream(collection, parallel).sorted().collect(toCollector(newList()));
	}

	public static <T> List<T> sort(Collection<T> collection, boolean parallel, Comparator<? super T> comparator) {
		return stream(collection, parallel).sorted(comparator).collect(toCollector(newList()));
	}

	public static <T> List<T> sort(Collection<T> collection, Comparator<? super T> comparator) {
		return sort(collection, false, comparator);
	}

	public static <T, P> List<T> sortBy(Collection<T> collection, List<P> orderByMe, Function<T, P> map) {
		return collection.stream()
				.sorted((o1, o2) -> new CompareToBuilder().append(noNegIndex(orderByMe.indexOf(map.apply(o1))), noNegIndex(orderByMe.indexOf(map.apply(o2)))).toComparison())
				.collect(toCollector(newList()));
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
		return stream(collection, false);
	}

	public static <T> Stream<T> stream(Collection<T> collection, boolean parallel) {
		return StreamSupport.stream(collection.spliterator(), parallel);
	}

	public static <T> Stream<T> stream(Iterable<T> iteratable) {
		return StreamSupport.stream(iteratable.spliterator(), false);
	}

	public static <T> Stream<T> stream(Iterator<T> iterator) {
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), false);
	}

	public static <K, V> Stream<Map.Entry<K, V>> stream(Map<K, V> map) {
		return stream(map, false);
	}

	public static <K, V> Stream<Map.Entry<K, V>> stream(Map<K, V> map, boolean parallel) {
		return StreamSupport.stream(map.entrySet().spliterator(), parallel);
	}

	public static Stream<Path> stream(Path path) {
		return stream(path, false);
	}

	public static Stream<Path> stream(Path path, boolean parallel) {
		return StreamSupport.stream(path.spliterator(), parallel);
	}

	public static <T> Stream<T> stream(T[] array) {
		return stream(array, false);
	}

	public static <T> Stream<T> stream(T[] array, boolean parallel) {
		return StreamSupport.stream(Spliterators.spliterator(array, 0, array.length, Spliterator.ORDERED | Spliterator.IMMUTABLE), parallel);
	}

	public static <T> Stream<T> streamDetached(Collection<T> collection) {
		return streamDetached(collection, false);
	}

	public static <T> Stream<T> streamDetached(Collection<T> collection, boolean parallel) {
		return stream(array(collection), parallel);
	}

	public static <K, V> Stream<Map.Entry<K, V>> streamDetached(Map<K, V> map) {
		return streamDetached(map, false);
	}

	public static <K, V> Stream<Map.Entry<K, V>> streamDetached(Map<K, V> map, boolean parallel) {
		return stream(array(map), parallel);
	}

	public static Stream<Path> streamFully(Path path) {
		return stream(new PathIterator(path));
	}

	public static <T> List<T> toList(Collection<T> collection) {
		return stream(collection).collect(toCollector(newList()));
	}

	public static <T> List<T> toList(T[] array) {
		return stream(array).collect(toCollector(newList()));

	}

	public static <T> Set<T> toSet(Collection<T> collection) {
		return stream(collection).collect(toCollector(newSet()));
	}

	public static <T> Set<T> toSet(T[] array) {
		return stream(array).collect(toCollector(newSet()));

	}

	public static <T> Set<T> toSortedSet(Collection<T> collection) {
		return stream(collection).collect(newSortedSet());
	}

	public static <T> SortedSet<T> toSortedSet(T[] array) {
		return stream(array).collect(newSortedSet());
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
}
