package org.jhaws.common.lang;

import java.io.IOException;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.lang.reflect.Array;
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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleSupplier;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.jhaws.common.lang.functions.EBiConsumer;
import org.jhaws.common.lang.functions.EBooleanSupplier;
import org.jhaws.common.lang.functions.EConsumer;
import org.jhaws.common.lang.functions.EDoubleSupplier;
import org.jhaws.common.lang.functions.EFunction;
import org.jhaws.common.lang.functions.EIntSupplier;
import org.jhaws.common.lang.functions.ELongSupplier;
import org.jhaws.common.lang.functions.EPredicate;
import org.jhaws.common.lang.functions.ERunnable;
import org.jhaws.common.lang.functions.ESupplier;

// http://infotechgems.blogspot.be/2011/11/java-collections-performance-time.html
// TODO https://www.techempower.com/blog/2016/10/19/efficient-multiple-stream-concatenation-in-java/
/**
 * @see java.util.stream.Stream
 * @see java.util.stream.StreamSupport
 * @see java.util.stream.Collectors
 * @see java.util.stream.Collector
 * @since 1.8
 * @see https://technology.amis.nl/2013/10/05/java-8-collection-enhancements- leveraging-lambda-expressions-or-how-java-emulates-sql/
 */
public interface CollectionUtils8 {
    public static class Comparators {
        public static class CaseInsensitiveComparator implements Comparator<String> {
            @Override
            public int compare(String o1, String o2) {
                return (o1 == null ? "" : o1).compareToIgnoreCase((o2 == null ? "" : o2));
            }
        }

        public static class MapEntryKVComparator<K, V> implements Comparator<Map.Entry<K, V>> {
            @Override
            public int compare(Entry<K, V> o1, Entry<K, V> o2) {
                return new CompareToBuilder().append(o1.getKey(), o2.getKey()).append(o1.getValue(), o2.getValue()).toComparison();
            }
        }

        public static class MapEntryVKComparator<K, V> implements Comparator<Map.Entry<K, V>> {
            @Override
            public int compare(Entry<K, V> o1, Entry<K, V> o2) {
                return new CompareToBuilder().append(o1.getValue(), o2.getValue()).append(o1.getKey(), o2.getKey()).toComparison();
            }
        }
    }

    public static class RegexIterator implements EnhancedIterator<String> {
        protected Pattern pattern;

        protected String regex;

        protected Matcher matcher;

        protected Boolean hasNext = null;

        public RegexIterator(String text, String regex) {
            this.pattern = Pattern.compile(regex);
            this.regex = regex;
            this.matcher = pattern.matcher(text);
        }

        public RegexIterator(String text, Pattern regex) {
            this.pattern = regex;
            this.regex = regex.toString();
            this.matcher = pattern.matcher(text);
        }

        public RegexIterator(Pattern regex, String text) {
            this.pattern = regex;
            this.regex = regex.toString();
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

    public static class PathIterator implements EnhancedIterator<Path> {
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

    public static class ProjectionIterator<T> implements EnhancedIterator<T> {
        protected T current;

        protected T next;

        protected Function<T, T> projection;

        public ProjectionIterator(T object, boolean include, Function<T, T> projection) {
            this.current = object;
            this.projection = projection;
            if (include) next = current;
        }

        @Override
        public boolean hasNext() {
            if (next == null) {
                next = projection.apply(current);
            }
            return next != null;
        }

        @Override
        public T next() {
            hasNext();
            if (next == null) {
                throw new NoSuchElementException();
            }
            current = next;
            next = null;
            return current;
        }
    }

    public static interface Opt<T> extends Supplier<T> {
        /** default vorig type */
        <X> Opt<X> opt(Function<T, X> getter);

        /** default vorig type */
        default <X> Opt<X> nest(Function<T, X> getter) {
            return opt(getter);
        }

        /** ga verder eager */
        <X> OptEager<X> eager(Function<T, X> getter);

        /** ga verder reusable = lazy */
        <X> OptReusable<T, X> reusable(Function<T, X> getter);

        /** ga verder reusable = lazy */
        default <X> OptReusable<T, X> lazy(Function<T, X> getter) {
            return reusable(getter);
        };

        /** geeft waarde terug */
        @Override
        T get();

        /** voert {@link #opt(Function)} uit en dan {@link #get()} */
        default <X> X get(Function<T, X> get) {
            return opt(get).get();
        }

        /**
         * voert {@link #get()} uit en wanneer null, voert {@link Supplier} uit en geeft die waarde
         */
        default T or(Supplier<T> supplier) {
            return Optional.ofNullable(get()).orElseGet(supplier);
        }

        /** voert {@link #get()} uit en wanneer null, geeft meegegeven waarde */
        default T or(T value) {
            return Optional.ofNullable(get()).orElse(value);
        }

        default T orNull() {
            return or((T) null);
        }

        default T or(Opt<T> supplier) {
            return or(supplier.get());
        }

        default <X> OptEager<X> map(Function<T, X> map) {
            T v = get();
            return Opt.eager(/* v == null ? null : */ map.apply(v));
        }

        default <X> OptEager<X> mapOrNull(Function<T, X> map) {
            return mapOr(map, (X) null);
        }

        default <X> OptEager<X> mapOr(Function<T, X> map, X value) {
            return mapOr(map, (Supplier<X>) () -> value);
        }

        default <X> OptEager<X> mapOr(Function<T, X> map, Supplier<X> value) {
            T v = get();
            return Opt.eager(v == null ? value.get() : map.apply(v));
        }

        /** default eager */
        public static <T> Opt<T> optional(T value) {
            return eager(value);
        }

        /** start eager */
        public static <T> OptEager<T> eager(T value) {
            return new OptEager<>(value);
        }

        /** start reusable = lazy */
        public static <T> OptReusable<?, T> reusable(T value) {
            return new OptReusable<>(value);
        }

        /** start reusable = lazy */
        public static <T> OptReusable<?, T> lazy(T value) {
            return reusable(value);
        }

        <X> X first(@SuppressWarnings("unchecked") Function<T, X>... getters);

        default boolean isNull() {
            return CollectionUtils8.isNull().test(get());
        }

        default boolean isBlankOrNull() {
            return CollectionUtils8.isBlankOrNull().test(get());
        }

        default boolean isNotNull() {
            return CollectionUtils8.isNotNull().test(get());
        }

        default boolean isNotBlankAndNotNull() {
            return CollectionUtils8.isNotBlankAndNotNull().test(get());
        }

        default void consume(Consumer<T> consumer) {
            T value = get();
            if (value != null) {
                consumer.accept(value);
            }
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
        public <P> OptEager<P> opt(Function<T, P> get) {
            return eager(get);
        }

        @Override
        public <P> OptEager<P> eager(Function<T, P> get) {
            return new OptEager<>(value == null ? null : get.apply(value));
        }

        @Override
        public <P> OptReusable<T, P> reusable(Function<T, P> get) {
            return new OptReusable<>(get()).opt(get);
        }

        @Override
        public <X> X first(@SuppressWarnings("unchecked") Function<T, X>... getters) {
            return streamArray(getters).map(g -> get(g)).filter(CollectionUtils8.isNotBlankAndNotNull()).findFirst().orElse(null);
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
            if (value != null) return value;
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
        public <X> OptReusable<T, X> opt(Function<T, X> get) {
            return reusable(get);
        }

        @Override
        public <X> OptEager<X> eager(Function<T, X> get) {
            return new OptEager<>(get()).opt(get);
        }

        @Override
        @SuppressWarnings("unchecked")
        public <X> OptReusable<T, X> reusable(Function<T, X> get) {
            return new OptReusable<>((OptReusable<T, X>) this, get);
        }

        @Override
        public <X> X first(@SuppressWarnings("unchecked") Function<T, X>... getters) {
            return streamArray(getters).map(g -> get(g)).filter(CollectionUtils8.isNotBlankAndNotNull()).findFirst().orElse(null);
        }
    }

    public static class CustomCollector<T, A, R> implements Collector<T, A, R> {
        protected Supplier<A> supplier;

        protected BiConsumer<A, T> accumulator;

        protected BinaryOperator<A> combiner;

        protected Function<A, R> finisher;

        protected Set<Characteristics> characteristics;

        public CustomCollector(Supplier<A> supplier, BiConsumer<A, T> accumulator, BinaryOperator<A> combiner, Function<A, R> finisher,
                Set<Characteristics> characteristics) {
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
            setSupplier(newList());
            BiConsumer<List<T>, T> accumulator0 = elementsListAccumulator();
            setAccumulator(accumulator0);
            setCombiner(elementsListCombiner(accumulator0));
            setFinisher(CollectionUtils8.cast());
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

    /**
     * always use first value for key
     */
    public static <T> BinaryOperator<T> keepFirst() {
        return (p1, p2) -> p1;
    }

    /**
     * always use last value for key
     */
    public static <T> BinaryOperator<T> keepLast() {
        return (p1, p2) -> p2;
    }

    public static <T> T last(Stream<T> stream) {
        return stream.reduce(keepLast()).orElse(null);
    }

    /**
     * see {@link #keepLast()}
     */
    public static <V> BinaryOperator<V> acceptDuplicateKeys() {
        return keepLast();
    }

    /**
     * rejects !different! keys for the same value
     * 
     * @throws IllegalArgumentException
     */
    public static <V> BinaryOperator<V> rejectDuplicateKeys() throws IllegalArgumentException {
        return (a, b) -> {
            if (new EqualsBuilder().append(a, b).isEquals()) return a;
            throw new IllegalArgumentException("duplicate key: " + a + " <> " + b);
        };
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] array(T item, T... items) {
        if (item == null && (items == null || items.length == 0)) {
            return (T[]) new Object[0];
        }
        if (items == null || items.length == 0) {
            return (T[]) new Object[] { item };
        }
        if (item == null) {
            return items;
        }
        int length = items.length;
        // FIXME cheaper // operation
        T[] newarray = Arrays.copyOf(items, 1 + length);
        newarray[0] = item;
        System.arraycopy(items, 0, newarray, 1, length);
        return newarray;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] array(T[] items, T item) {
        if (item == null && (items == null || items.length == 0)) {
            return (T[]) new Object[0];
        }
        if (items == null || items.length == 0) {
            return (T[]) new Object[] { item };
        }
        if (item == null) {
            return items;
        }
        int length = items.length;
        T[] newarray = Arrays.copyOf(items, 1 + length);
        newarray[newarray.length - 1] = item;
        return newarray;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] array(T[] items1, T[] items2) {
        if ((items1 == null || items1.length == 0) && (items2 == null || items2.length == 0)) {
            return (T[]) new Object[0];
        }
        if (items1 == null || items1.length == 0) {
            return items2;
        }
        if (items2 == null || items2.length == 0) {
            return items1;
        }
        int length1 = items1.length;
        int length2 = items2.length;
        T[] newarray = Arrays.copyOf(items1, length1 + length2);
        System.arraycopy(items2, 0, newarray, length1, length2);
        return newarray;
    }

    public static <T> T[] array(Class<T> componentType, Collection<T> collection) {
        return array(componentType, collection.stream());
    }

    public static <T> T[] array(Class<T> componentType, Stream<T> stream) {
        return stream.toArray(newArray(componentType));
    }

    @SuppressWarnings("unchecked")
    public static <T> IntFunction<T[]> newArray(Class<T> componentType) {
        return length -> (T[]) Array.newInstance(componentType, length);
    }

    public static <T> T[] newArray(Class<T> componentType, int size) {
        return CollectionUtils8.<T> newArray(componentType).apply(size);
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
            return (Collector<T, ?, C>) CollectionUtils8.<T> collectDeque();
        }
        if (collection instanceof Queue) {
            return (Collector<T, ?, C>) CollectionUtils8.<T> collectQueue();
        }
        if (collection instanceof List) {
            return (Collector<T, ?, C>) CollectionUtils8.<T> collectList();
        }
        if (collection instanceof SortedSet) {
            return (Collector<T, ?, C>) CollectionUtils8.<T> collectSortedSet();
        }
        if (collection instanceof Set) {
            return (Collector<T, ?, C>) CollectionUtils8.<T> collectSet();
        }
        throw new UnsupportedOperationException(collection.getClass().getName());
    }

    @SafeVarargs
    public static <T, C extends Collection<T>> C filter(boolean parallel, C collection, Predicate<? super T>... predicates) {
        AtomicReference<Stream<T>> streamReference = new AtomicReference<>(stream(parallel, collection));
        streamArray(predicates).forEach(predicate -> streamReference.set(streamReference.get().filter(predicate)));
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

    public static <K, V> EnhancedMap<K, V> map(Collection<Map.Entry<K, V>> entries) {
        return map(false, entries);
    }

    public static <K, V> EnhancedMap<K, V> map(boolean parallel, Collection<? extends Map.Entry<K, V>> entries) {
        return map(stream(parallel, entries));
    }

    public static <K, V> EnhancedMap<K, V> map(Stream<? extends Map.Entry<K, V>> stream) {
        return map(stream, CollectionUtils8.<K, V> newLinkedMap());
    }

    public static <K, V, M extends Map<K, V>> M map(Stream<? extends Map.Entry<K, V>> stream, Supplier<M> mapSupplier) {
        BinaryOperator<V> keepLast = CollectionUtils8.<V> keepLast();
        return map(stream, mapSupplier, keepLast);
    }

    public static <K, V, M extends Map<K, V>> M map(Stream<? extends Map.Entry<K, V>> stream, Supplier<M> mapSupplier, BinaryOperator<V> choice) {
        Function<Entry<K, V>, K> keyMapper = CollectionUtils8.<K, V> keyMapper();
        Function<Entry<K, V>, V> valueMapper = CollectionUtils8.<K, V> valueMapper();
        Collector<Entry<K, V>, ?, M> c = Collectors.toMap(keyMapper, valueMapper, choice, mapSupplier);
        return stream.collect(c);
    }

    public static <K, V> Supplier<EnhancedLinkedHashMap<K, V>> newLinkedMap() {
        return EnhancedLinkedHashMap::new;
    }

    public static <K, V> Supplier<EnhancedMap<K, V>> newMap() {
        return EnhancedHashMap::new;
    }

    public static <K, V> Supplier<EnhancedSortedMap<K, V>> newSortedMap() {
        return EnhancedTreeMap::new;
    }

    public static <K, V> Supplier<EnhancedTreeMap<K, V>> newNavigableMap() {
        return EnhancedTreeMap::new;
    }

    public static <K, V> Supplier<ConcurrentSkipListMap<K, V>> newConcurrentNavigableMap() {
        return ConcurrentSkipListMap::new;
    }

    public static <K, V> Supplier<ConcurrentHashMap<K, V>> newConcurrentMap() {
        return ConcurrentHashMap::new;
    }

    public static <T> Supplier<Stack<T>> newStack() {
        return Stack::new;
    }

    public static <T> Supplier<Deque<T>> newDeque() {
        return EnhancedLinkedList::new;
    }

    public static <T> Supplier<List<T>> newList() {
        return EnhancedArrayList::new;
    }

    public static <T> Supplier<Queue<T>> newQueue() {
        return EnhancedLinkedList::new;
    }

    public static <T> Supplier<Set<T>> newSet() {
        return EnhancedHashSet::new;
    }

    public static <T> Supplier<SortedSet<T>> newSortedSet() {
        return EnhancedTreeSet::new;
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

    public static <T> Collector<T, ?, Stack<T>> collectStack() {
        return toCollector(newStack());
    }

    public static <T> Collector<T, ?, Deque<T>> collectDeque() {
        return toCollector(newDeque());
    }

    public static <T> Collector<T, ?, List<T>> collectList() {
        return toCollector(newList());
    }

    public static <T, C extends Collection<T>> Collector<T, ?, C> collection(Supplier<C> newCollection) {
        return Collectors.toCollection(newCollection);
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
        return collection.stream().sorted(comparator(orderByMe, map)).collect(collectList());
    }

    public static <T, A> Comparator<T> comparator(List<A> orderByMe, Function<T, A> map) {
        return (x, y) -> new Integer(noNegIndex(orderByMe.indexOf(map.apply(x)))).compareTo(new Integer(noNegIndex(orderByMe.indexOf(map.apply(y)))));
    }

    public static <T> Comparator<T> comparator(List<T> orderByMe) {
        return comparator(orderByMe, self());
    }

    public static <T> List<T> sortBy(Collection<T> collection, List<T> orderByMe) {
        return sortBy(collection, orderByMe, self());
    }

    public static <T, S extends Comparable<? super S>> Stream<T> sortBy(Stream<T> sortMe, Map<T, S> sortByMe) {
        return sortMe.sorted((x, y) -> sortByMe.get(x).compareTo(sortByMe.get(y)));
    }

    public static <T, A> Stream<T> sortBy(Stream<T> collection, List<A> orderByMe, Function<T, A> map) {
        return collection.sorted(comparator(orderByMe, map));
    }

    public static <T> Stream<T> sortBy(Stream<T> collection, List<T> orderByMe) {
        return sortBy(collection, orderByMe, self());
    }

    public static <T, S extends Comparable<? super S>> List<T> sortBy(Collection<T> sortMe, Map<T, S> sortByMe) {
        return sortMe.stream().sorted((x, y) -> sortByMe.get(x).compareTo(sortByMe.get(y))).collect(collectList());
    }

    public static <T> Function<T, T> id() {
        return self();
    }

    public static <T> Function<T, T> self() {
        return Function.identity();
    }

    public static <T> UnaryOperator<T> idu() {
        return selfu();
    }

    public static <T> UnaryOperator<T> selfu() {
        return t -> t;
    }

    public static int noNegIndex(int i) {
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
    public static <T> Stream<T> streamArray(T... array) {
        return streamArray(false, array);
    }

    @SafeVarargs
    public static <T> Stream<T> streamArray(boolean parallel, T... array) {
        if (array == null) return Stream.empty();
        // StreamSupport.stream(Spliterators.spliterator(array, 0, array.length,
        // Spliterator.ORDERED | Spliterator.IMMUTABLE), parallel);
        Stream<T> stream = Arrays.stream(array);
        if (parallel) stream = stream.parallel();
        return stream;
    }

    public static <T> Stream<T> streamDetached(Collection<T> collection) {
        return streamDetached(false, collection);
    }

    @SuppressWarnings("unchecked")
    public static <T> Stream<T> streamDetached(boolean parallel, Collection<T> collection) {
        return streamArray(parallel, array((Class<T>) Object.class, collection));
    }

    public static <K, V> Stream<Map.Entry<K, V>> streamDetached(Map<K, V> map) {
        return streamDetached(false, map);
    }

    public static <K, V> Stream<Map.Entry<K, V>> streamDetached(boolean parallel, Map<K, V> map) {
        return streamArray(parallel, array(map));
    }

    public static Stream<Path> streamFully(Path path) {
        return stream(new PathIterator(path));
    }

    public static <T, U> Stream<U> stream(Collection<T> input, Function<T, Collection<U>> mapping) {
        return input.stream().map(mapping).map(Collection::stream).flatMap(id());
    }

    public static <T> List<T> toList(Collection<T> collection) {
        return toList(true, collection);
    }

    public static <T> List<T> toList(boolean copy, Collection<T> collection) {
        return !copy && collection instanceof List ? (List<T>) collection : stream(collection).collect(collectList());
    }

    public static <K, V> Map<K, V> toMap(Map<K, V> map) {
        return toMap(true, map);
    }

    public static <K, V> Map<K, V> toMap(boolean copy, Map<K, V> map) {
        return !copy /* && map instanceof Map */ ? (Map<K, V>) map : stream(map).collect(collectMap(Entry::getKey, Entry::getValue));
    }

    public static <K, V> SortedMap<K, V> toSortedMap(Map<K, V> map) {
        return toSortedMap(true, map);
    }

    public static <K, V> SortedMap<K, V> toSortedMap(boolean copy, Map<K, V> map) {
        return !copy && map instanceof SortedMap ? (SortedMap<K, V>) map : stream(map).collect(collectSortedMap(Entry::getKey, Entry::getValue));
    }

    @SafeVarargs
    public static <T> List<T> toList(T... array) {
        return streamArray(array).collect(collectList());
    }

    public static <T> Set<T> toSet(Collection<T> collection) {
        return toSet(true, collection);
    }

    public static <T> Set<T> toSet(boolean copy, Collection<T> collection) {
        return !copy && collection instanceof Set ? (Set<T>) collection : stream(collection).collect(collectSet());
    }

    @SafeVarargs
    public static <T> Set<T> toSet(T... array) {
        return streamArray(array).collect(collectSet());
    }

    public static <T> SortedSet<T> toSortedSet(Collection<T> collection) {
        return toSortedSet(true, collection);
    }

    public static <T> SortedSet<T> toSortedSet(boolean copy, Collection<T> collection) {
        return !copy && collection instanceof SortedSet ? (SortedSet<T>) collection : stream(collection).collect(collectSortedSet());
    }

    @SafeVarargs
    public static <T> SortedSet<T> toSortedSet(T... array) {
        return streamArray(array).collect(collectSortedSet());
    }

    public static <T> Queue<T> toQueue(Collection<T> collection) {
        return stream(collection).collect(collectQueue());
    }

    @SafeVarargs
    public static <T> Queue<T> toQueue(T... array) {
        return streamArray(array).collect(collectQueue());
    }

    public static <T> Deque<T> toDeque(Collection<T> collection) {
        return stream(collection).collect(collectDeque());
    }

    @SafeVarargs
    public static <T> Deque<T> toDeque(T... array) {
        return streamArray(array).collect(collectDeque());
    }

    public static <T> Stack<T> toStack(Collection<T> collection) {
        return stream(collection).collect(collectStack());
    }

    @SafeVarargs
    public static <T> Stack<T> toStack(T... array) {
        return streamArray(array).collect(collectStack());
    }

    public static <T, C extends Collection<T>> C to(T[] array, Supplier<C> supplier) {
        return to(array, toCollector(supplier));
    }

    public static <T, C extends Collection<T>> C to(T[] array, Collector<T, ?, C> collector) {
        return streamArray(array).collect(collector);
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

    public static <T> Predicate<T> isNotBlankAndNotNull() {
        return t -> t instanceof String ? isNotBlank().test(String.class.cast(t)) : isNotNull().test(t);
    }

    public static <T> Predicate<T> isBlankOrNull() {
        return t -> t instanceof String ? isBlank().test(String.class.cast(t)) : isNull().test(t);
    }

    public static Predicate<String> isNotBlank() {
        return org.apache.commons.lang3.StringUtils::isNotBlank;
    }

    public static Predicate<String> isBlank() {
        return org.apache.commons.lang3.StringUtils::isBlank;
    }

    public static <T> Predicate<T> isNotNull() {
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

    public static <X> Predicate<X> always(boolean b) {
        return x -> b;
    }

    public static <X> Predicate<X> never() {
        return x -> false;
    }

    public static <X> Predicate<X> never(boolean b) {
        return x -> !b;
    }

    public static <X, Y> BiPredicate<X, Y> always2() {
        return (x, y) -> true;
    }

    public static <X, Y> BiPredicate<X, Y> always2(boolean b) {
        return (x, y) -> b;
    }

    public static <X, Y> BiPredicate<X, Y> never2() {
        return (x, y) -> false;
    }

    public static <X, Y> BiPredicate<X, Y> never2(boolean b) {
        return (x, y) -> !b;
    }

    public static IntStream streamp(String text) {
        return text.chars();
    }

    public static Stream<Character> stream(String text) {
        return streamp(text).mapToObj(i -> (char) i);
    }

    public static Stream<Character> stream(char[] text) {
        return stream(new String(text));
    }

    public static Stream<Character> stream(Character[] text) {
        return Arrays.stream(text);
    }

    public static <T> List<Map.Entry<T, T>> match(List<T> keys, List<T> values) {
        return values.stream().parallel().filter(containedIn(keys)).map(value -> new Pair<>(keys.get(keys.indexOf(value)), value)).collect(
                collectList());
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
            return;
        };
    }

    public static <T> String joinArray(String delimiter, T[] strings) {
        return join(delimiter, streamArray(strings));
    }

    public static <T> String joinArray(T[] strings) {
        return join(" ", streamArray(strings));
    }

    public static <T> String join(Collection<T> strings) {
        return join(" ", stream(strings));
    }

    public static <T> String join(String delimiter, Collection<T> strings) {
        return join(delimiter, stream(strings));
    }

    public static <T> String join(Stream<T> stream) {
        return join(" ", stream);
    }

    public static <T> String join(String delimiter, Stream<T> stream) {
        return stream//
                .filter(Objects::nonNull)//
                .map(String::valueOf)//
                .map(String::trim)//
                .filter(StringUtils::isNotBlank)//
                .collect(Collectors.joining(delimiter));
    }

    public static <T> String join(String delimiter, boolean skipNull, boolean trim, Stream<T> stream) {
        if (skipNull) stream = stream.filter(Objects::nonNull);
        Stream<String> stream2 = stream.map(String::valueOf);
        if (trim) stream2 = stream2.map(String::trim);
        if (skipNull) stream2 = stream2.filter(StringUtils::isNotBlank);
        return stream2.collect(Collectors.joining(delimiter));
    }

    public static Stream<String> split(String string, String delimiter) {
        return streamArray(string.split(delimiter));
    }

    public static String joining(Stream<String> stream, String delimiter) {
        return stream.collect(Collectors.joining(delimiter));
    }

    /**
     * !!!!!!!!!!! terminates source stream !!!!!!!!!!! <br>
     * !!!!!!!!!!! cannot be used on endless streams !!!!!!!!!!!
     * 
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
        if (first == second) return true;
        if (first == null && second == null) return true;
        if (first == null || second == null) return false;
        return first.equals(second);
    }

    public static <V, K> Map<K, List<V>> groupBy(Stream<V> stream, Function<V, K> groupBy) {
        return stream.collect(Collectors.groupingBy(groupBy));
    }

    public static <T> Comparator<T> dummyComparator() {
        return (x, y) -> 0;
    }

    public static <K, V> Function<Entry<K, V>, V> valueMapper() {
        return Entry::getValue;
    }

    public static <K, V> Function<Entry<K, V>, K> keyMapper() {
        return Entry::getKey;
    }

    public static Stream<String> regex(String text, String regex) {
        return stream(new RegexIterator(text, regex));
    }

    public static <T> T[] copy(T[] array) {
        return copy(array, array.length);
    }

    public static <T> T[] copy(T[] array, int length) {
        return Arrays.copyOf(array, length);
    }

    @SafeVarargs
    public static <K, V> Stream<Entry<K, V>> streamMaps(Map<K, V>... maps) {
        return Stream.of(maps).map(Map::entrySet).flatMap(Collection::stream);
    }

    @SafeVarargs
    public static <K, V> Stream<Entry<K, V>> streamMapsUnique(Map<K, V>... maps) {
        return stream(streamMaps(maps).collect(Collectors.toMap(Entry::getKey, Entry::getValue, keepLast(), newLinkedMap())));
    }

    public static <K, V> Collector<Entry<K, V>, ?, EnhancedMap<K, V>> collectMapEntries(Function<Entry<K, V>, V> valueMapper) {
        return Collectors.toMap(Entry::getKey, valueMapper, rejectDuplicateKeys(), newMap());
    }

    public static <K, V> Collector<Entry<K, V>, ?, EnhancedMap<K, V>> collectMapEntriesAlt(Function<Entry<K, V>, K> keyMapper) {
        return Collectors.toMap(keyMapper, Entry::getValue, rejectDuplicateKeys(), newMap());
    }

    public static <K, V> Collector<Entry<K, V>, ?, EnhancedMap<K, V>> collectMapEntries() {
        return Collectors.toMap(Entry::getKey, Entry::getValue, rejectDuplicateKeys(), newMap());
    }

    public static <T, K, V> Collector<T, ?, EnhancedMap<K, V>> collectMap(Function<T, K> keyMapper, Function<T, V> valueMapper) {
        return collectMap(keyMapper, valueMapper, rejectDuplicateKeys());
    }

    public static <T, K, V> Collector<T, ?, EnhancedMap<K, V>> collectMap(Function<T, K> keyMapper, Function<T, V> valueMapper,
            BinaryOperator<V> duplicateValues) {
        return Collectors.toMap(keyMapper, valueMapper, duplicateValues, newMap());
    }

    public static <T, K, V> Collector<T, ?, EnhancedMap<K, V>> collectMapAlt(Function<T, V> valueMapper, BinaryOperator<V> duplicateValues) {
        @SuppressWarnings("unchecked")
        Function<T, K> keyMapper = (Function<T, K>) id();
        return Collectors.toMap(keyMapper, valueMapper, duplicateValues, newMap());
    }

    public static <K, V> Collector<V, ?, EnhancedMap<K, V>> collectMap(Function<V, K> keyMapper, BinaryOperator<V> duplicateValues) {
        return collectMap(keyMapper, self(), duplicateValues);
    }

    public static <K, V> Collector<V, ?, EnhancedMap<K, V>> collectMap(Function<V, K> keyMapper) {
        return collectMap(keyMapper, rejectDuplicateKeys());
    }

    public static <T, K, V> Collector<T, ?, EnhancedSortedMap<K, V>> collectSortedMap(Function<T, K> keyMapper, Function<T, V> valueMapper) {
        return collectSortedMap(keyMapper, valueMapper, rejectDuplicateKeys());
    }

    public static <T, K, V> Collector<T, ?, EnhancedSortedMap<K, V>> collectSortedMap(Function<T, K> keyMapper, Function<T, V> valueMapper,
            BinaryOperator<V> duplicateValues) {
        return Collectors.toMap(keyMapper, valueMapper, duplicateValues, newSortedMap());
    }

    public static <K, V> Collector<V, ?, EnhancedSortedMap<K, V>> collectSortedMap(Function<V, K> keyMapper, BinaryOperator<V> duplicateValues) {
        return collectSortedMap(keyMapper, self(), duplicateValues);
    }

    public static <K, V> Collector<V, ?, EnhancedSortedMap<K, V>> collectSortedMap(Function<V, K> keyMapper) {
        return collectSortedMap(keyMapper, rejectDuplicateKeys());
    }

    public static <T extends Comparable<? super T>> Comparator<T> natural() {
        return Comparator.<T> naturalOrder();
    }

    public static <T extends Comparable<? super T>> BinaryOperator<T> keepMin() {
        return keepMin(CollectionUtils8.<T> natural());
    }

    public static <T> BinaryOperator<T> keepMin(Comparator<T> comparator) {
        return BinaryOperator.minBy(comparator);
    }

    public static <T extends Comparable<? super T>> BinaryOperator<T> keepMax() {
        return keepMax(CollectionUtils8.<T> natural());
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

    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Stream<T> stream, Class<T> type) {
        return stream.toArray(size -> (T[]) Array.newInstance(type, size));
    }

    public static <T> Stream<T> flatMapCollections(Collection<? extends Collection<T>> collectionOfCollections) {
        return flatMapStreams(collectionOfCollections.stream().map(Collection::stream));
    }

    public static <T> Stream<T> flatMap(Stream<? extends Collection<T>> streamOfCollections) {
        return flatMapStreams(streamOfCollections.map(Collection::stream));
    }

    public static <T> Stream<T> flatMap(Collection<Stream<T>> collentionOfStreams) {
        return flatMapStreams(collentionOfStreams.stream());
    }

    public static <T> Stream<T> flatMapStreams(Stream<Stream<T>> streamOfStreams) {
        return streamOfStreams.flatMap(id());
    }

    public static <T> Stream<T> flatMapArrays(T[][] streamOfStreams) {
        Stream<Stream<T>> b = streamArray(streamOfStreams).map(a -> streamArray(a));
        return flatMapStreams(b);
    }

    public static <T> Opt<T> eager(T value) {
        return Opt.eager(value);
    }

    public static <T> Opt<T> reusable(T value) {
        return Opt.reusable(value);
    }

    /**
     * c1 - c2, does not change input collections, returns new collection (list)
     */
    public static <T> List<T> subtract(Collection<T> c1, Collection<T> c2) {
        if (c1 == null) {
            return Collections.emptyList();
        }
        List<T> tmp = new ArrayList<>(c1);
        if (c2 != null) {
            tmp.removeAll(c2);
        }
        return tmp;
    }

    /**
     * intersection of c1 and c2, does not change input collections, returns new collection (list)
     */
    public static <T> List<T> intersection(Collection<T> c1, Collection<T> c2) {
        if (c1 == null || c2 == null) {
            return Collections.emptyList();
        }
        return c1.stream().filter(c2::contains).collect(collectList());
    }

    /**
     * intersection of c1 and c2, does not change input collections, returns new collection (list)
     */
    public static <T> List<T> union(Collection<T> c1, Collection<T> c2) {
        if (c1 == null && c2 == null) {
            return Collections.emptyList();
        }
        if (c1 == null) {
            return new ArrayList<>(c2);
        }
        if (c2 == null) {
            return new ArrayList<>(c1);
        }
        List<T> tmp = new ArrayList<>(c1.size() + c2.size());
        tmp.addAll(c1);
        tmp.addAll(c2);
        return tmp;
    }

    /**
     * @see https://stackoverflow.com/questions/42214519/java-intersection-of-multiple-collections-using-stream-lambdas
     */
    public static <T, S extends Collection<T>> Collector<S, ?, Set<T>> intersecting() {
        class IntersectAcc {
            Set<T> result;

            void accept(S s) {
                if (result == null)
                    result = new HashSet<>(s);
                else
                    result.retainAll(s);
            }

            IntersectAcc combine(IntersectAcc other) {
                if (result == null) return other;
                if (other.result != null) result.retainAll(other.result);
                return this;
            }
        }
        return Collector.of(IntersectAcc::new, IntersectAcc::accept, IntersectAcc::combine,
                acc -> acc.result == null ? Collections.emptySet() : acc.result, Collector.Characteristics.UNORDERED);
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
        if (constructor == null || teBewaren == null) throw new NullPointerException();
        sync(nieuwe, bestaande, teVerwijderenAct, (k, i) -> teBewaren.accept(i, constructor.get()), teBewaren);
    }

    /**
     * synchroniseer een nieuwe collectie met een bestaande collecte
     *
     * @param nieuwe nieuwe collectie vooraf gemapt op key, zie {@link #getMap(Collection, Function)}
     * @param bestaande bestaande collectie vooraf gemapt op key, zie {@link #getMap(Collection, Function)}
     * @param teVerwijderenAct nullable, actie op te roepen indien verwijderen, krijgt key en bestaand object binnen
     * @param aanTeMakenAct nullable, actie op te roepen indien nieuw object aan te maken, krijgt key en nieuw object binnen
     * @param teBewaren nullable, actie op te roepen indien overeenkomst, krijgt key en nieuw en bestaand object binnen
     *
     * @param <K> key waarop vergeleken moet worden
     * @param <N> nieuwe object
     * @param <B> bestaand object
     */
    public static <K, N, B> void sync(//
            Map<K, N> nieuwe, //
            Map<K, B> bestaande, //
            BiConsumer<K, B> teVerwijderenAct, //
            BiConsumer<K, N> aanTeMakenAct, //
            BiConsumer<N, B> teBewaren//
    ) {
        Map<K, B> teVerwijderen = subtract(bestaande.keySet(), nieuwe.keySet()).stream().collect(Collectors.toMap(id(), bestaande::get));
        Map<K, N> aanTeMaken = subtract(nieuwe.keySet(), bestaande.keySet()).stream().collect(Collectors.toMap(id(), nieuwe::get));
        Map<N, B> overeenkomst = intersection(nieuwe.keySet(), bestaande.keySet()).stream().collect(Collectors.toMap(nieuwe::get, bestaande::get));
        if (teBewaren != null) overeenkomst.forEach(teBewaren);
        if (aanTeMakenAct != null) aanTeMaken.forEach(aanTeMakenAct);
        if (teVerwijderenAct != null) teVerwijderen.forEach(teVerwijderenAct);
    }

    public static <K, V> Map<K, V> getMap(Stream<V> values, Function<V, K> keyMapper) {
        return values.collect(Collectors.toMap(keyMapper, self()));
    }

    public static <K, V> Map<K, V> getMap(Collection<V> values, Function<V, K> keyMapper) {
        return getMap(values.stream(), keyMapper);
    }

    public static <K, V> Map<K, V> getMap(Stream<V> values, Supplier<K> keyMapper) {
        return values.collect(Collectors.toMap(supplierToFunction(keyMapper), self()));
    }

    public static <K, V> Map<K, V> getMap(Collection<V> values, Supplier<K> keyMapper) {
        return getMap(values.stream(), keyMapper);
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
            if (!a.contains(t)) a.add(t);
        };
    }

    public static <T> BinaryOperator<List<T>> elementsListCombiner(BiConsumer<List<T>, T> elementsListAccumulator) {
        return (List<T> a1, List<T> a2) -> {
            a2.stream().forEach(a2e -> elementsListAccumulator.accept(a1, a2e));
            return a1;
        };
    }

    public static <T> Consumer<T> when(Predicate<T> restriction, Consumer<T> whenTrue, Consumer<T> whenFalse) {
        return t -> (restriction.test(t) ? whenTrue : whenFalse).accept(t);
    }

    public static <T> Consumer<T> when(boolean restriction, Consumer<T> whenTrue, Consumer<T> whenFalse) {
        return when(always(restriction), whenTrue, whenFalse);
    }

    public static <T> void when(Predicate<T> restriction, Consumer<T> whenTrue, Consumer<T> whenFalse, T subject) {
        when(restriction, whenTrue, whenFalse).accept(subject);
    }

    public static <T> void when(boolean restriction, Consumer<T> whenTrue, Consumer<T> whenFalse, T subject) {
        when(restriction, whenTrue, whenFalse).accept(subject);
    }

    public static <T> Collection<Collection<T>> split(Collection<T> all, int maxSize) {
        if (maxSize < 1) throw new IllegalArgumentException("maxSize<1");
        if (all.size() <= maxSize) return Arrays.asList(all);
        int totalSize = all.size();
        AtomicInteger ai = new AtomicInteger(0);
        int groups = (totalSize / maxSize) + (totalSize % maxSize > 0 ? 1 : 0);
        Map<Integer, List<T>> g = all.stream().parallel().collect(Collectors.groupingBy(s -> ai.addAndGet(1) % groups));
        return stream(g).map(valueMapper()).collect(collectList());
    }

    public static <T> Stream<List<T>> split(List<T> elements, int count) {
        return elements.stream().collect(Collectors.groupingBy(c -> elements.indexOf(c) / count)).values().stream();
    }

    public static <T> Function<List<T>, T> first() {
        return list -> list.isEmpty() ? null : list.get(0);
    }

    public static <T> T first(List<T> list) {
        return list.get(0);
    }

    public static <T> T first(LinkedBlockingDeque<T> dq) {
        return dq.iterator().next();
    }

    public static <T> T first(LinkedBlockingQueue<T> q) {
        return q.iterator().next();
    }

    public static <T> T first(LinkedHashSet<T> set) {
        return set.iterator().next();
    }

    public static <K, V> Entry<K, V> first(LinkedHashMap<K, V> map) {
        return map.entrySet().iterator().next();
    }

    @SafeVarargs
    public static <T> List<T> sortBy(Collection<T> collection, Function<T, ? extends Comparable<?>> map,
            Function<T, ? extends Comparable<?>>... mapAdditional) {
        return collection.stream().sorted(comparator(map, mapAdditional)).collect(collectList());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <T extends Comparable<? super T>> List<T> sortInPlace(List<T> list) {
        Collections.sort((List<? extends Comparable>) list);
        return list;
    }

    @SafeVarargs
    public static <T> List<T> sortInPlace(List<T> list, Function<T, ? extends Comparable<?>> map,
            Function<T, ? extends Comparable<?>>... mapAdditional) {
        list.sort(comparator(map, mapAdditional));
        return list;
    }

    @SafeVarargs
    public static <T> Comparator<T> comparator(Function<T, ? extends Comparable<?>> compare,
            Function<T, ? extends Comparable<?>>... compareAdditional) {
        return (t1, t2) -> {
            CompareToBuilder cb = new CompareToBuilder();
            cb.append(compare.apply(t1), compare.apply(t2));
            streamArray(compareAdditional).forEach(compareThisEl -> cb.append(compareThisEl.apply(t1), compareThisEl.apply(t2)));
            return cb.toComparison();
        };
    }

    public static <T, U> Stream<U> filterClass(Stream<T> stream, Class<U> type) {
        return stream.filter(t -> type.isAssignableFrom(t.getClass())).map(t -> type.cast(t));
    }

    public static Stream<Map.Entry<String, String>> stream(Properties properties) {
        return stream(false, properties).map(entry -> new Pair<>(String.valueOf(entry.getKey()), String.valueOf(entry.getValue())));
    }

    public static <K, V> Function<Entry<K, V>, K> mapKey() {
        return Entry::getKey;
    }

    public static <K, V> Function<Entry<K, V>, V> mapValue() {
        return Entry::getValue;
    }

    public static <K, V, T> Map<K, T> map(Map<K, V> map, Function<V, T> valueMapper) {
        return stream(map).collect(collectMap(mapKey(), e -> valueMapper.apply(e.getValue())));
    }

    public static <K, V, T> Map<T, V> mapKey(Map<K, V> map, Function<K, T> keyMapper) {
        return stream(map).collect(collectMap(e -> keyMapper.apply(e.getKey()), mapValue()));
    }

    public static <T> Comparator<T> comparatorCaseInsensitive(Function<T, String> compare) {
        Comparators.CaseInsensitiveComparator comparator = new Comparators.CaseInsensitiveComparator();
        return (t1, t2) -> {
            return comparator.compare(compare.apply(t1), compare.apply(t2));
        };
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static EnhancedMap<String, List<String>> groupByValue(Properties properties) {
        return groupByValue((Map<String, String>) (Map) properties);
    }

    public static <K, V> EnhancedMap<V, List<K>> groupByValue(Map<K, V> map) {
        return stream(map.entrySet())
                .collect(Collectors.groupingBy(e -> e.getValue(), newMap(), Collectors.mapping(e -> e.getKey(), Collectors.toList())));
    }

    @SuppressWarnings("unchecked")
    public static <X, Y> Function<X, Y> functions(Function<X, Y>... functions) {
        Value<Function<X, Y>> returningFunction = new Value<>(functions[0]);
        Arrays.stream(functions).skip(1).forEach(f -> returningFunction.set(returningFunction.get().andThen(Function.class.cast(f))));
        return returningFunction.get();
    }

    public static <X> X nn(X nullable, X defaultValue) {
        return nullable == null ? defaultValue : nullable;
    }

    public static <S, T> Function<S, T> getDefaulted(Function<S, T> getter, T defaultValue) {
        return ((Function<S, T>) s -> getter.apply(s)).andThen(e -> nn(e, defaultValue));
    }

    public static <X, Y> Y getMapValue(Map<? extends Predicate<X>, Y> map, X in) {
        return map.entrySet().stream().filter(entry -> entry.getKey().test(in)).map(Map.Entry::getValue).findFirst().orElse(null);
    }

    public static <T> List<T> emptyList() {
        return Collections.emptyList();
    }

    public static <T> Stream<T> emptyStream() {
        return Stream.empty();
    }

    public static <T, U> Map<T, U> emptyMap() {
        return Collections.emptyMap();
    }

    public static <T> Set<T> emptySet() {
        return Collections.emptySet();
    }

    public static String toString(IntStream s) {
        return s.mapToObj(String::valueOf).collect(Collectors.joining());
    }

    @SafeVarargs
    public static <T> BiPredicate<T, T> equals(Function<T, ?>... methods) {
        return (a, b) -> {
            EqualsBuilder eb = new EqualsBuilder();
            Arrays.asList(methods).forEach(g -> eb.append(g.apply(a), g.apply(b)));
            return eb.isEquals();
        };
    }

    @SafeVarargs
    public static <T> BiFunction<T, T, Integer> bifunction(Function<T, ?>... methods) {
        return (a, b) -> {
            if (methods == null || methods.length == 0) {
                return 0;
            }
            CompareToBuilder ctb = new CompareToBuilder();
            Arrays.asList(methods).forEach(g -> ctb.append(g.apply(a), g.apply(b)));
            return ctb.toComparison();
        };
    }

    @SafeVarargs
    public static <T> Comparator<T> comparatorFor(Function<T, ?>... methods) {
        return comparatorFor(bifunction(methods));
    }

    public static <T> Comparator<T> comparatorFor(BiFunction<T, T, Integer> method) {
        return method::apply;
    }

    public static <T> Collection<T> toUnmodifiableCollection(Collection<T> collection) {
        return Collections.unmodifiableCollection(toList(true, collection));
    }

    public static <T> List<T> toUnmodifiableList(Collection<T> collection) {
        return Collections.unmodifiableList(toList(true, collection));
    }

    public static <K, V> Map<K, V> toUnmodifiableMap(Map<K, V> map) {
        return Collections.unmodifiableMap(toMap(true, map));
    }

    public static <T> Set<T> toUnmodifiableSet(Collection<T> collection) {
        return Collections.unmodifiableSet(toSet(true, collection));
    }

    public static <K, V> SortedMap<K, V> toUnmodifiableSortedMap(SortedMap<K, V> map) {
        return Collections.unmodifiableSortedMap(toSortedMap(true, map));
    }

    public static <T> SortedSet<T> toUnmodifiableSortedSet(Collection<T> collection) {
        return Collections.unmodifiableSortedSet(toSortedSet(true, collection));
    }

    public static <T> Collection<T> toSynchronizedCollection(Collection<T> collection) {
        return Collections.synchronizedCollection(toList(true, collection));
    }

    public static <T> List<T> toSynchronizedList(Collection<T> collection) {
        return Collections.synchronizedList(toList(true, collection));
    }

    public static <K, V> Map<K, V> toSynchronizedMap(Map<K, V> map) {
        return Collections.synchronizedMap(toMap(true, map));
    }

    public static <T> Set<T> toSynchronizedSet(Collection<T> collection) {
        return Collections.synchronizedSet(toSet(true, collection));
    }

    public static <K, V> SortedMap<K, V> toSynchronizedSortedMap(SortedMap<K, V> map) {
        return Collections.synchronizedSortedMap(toSortedMap(true, map));
    }

    public static <T> SortedSet<T> toSynchronizedSortedSet(Collection<T> collection) {
        return Collections.synchronizedSortedSet(toSortedSet(true, collection));
    }

    public static <T> Stream<KeyValue<Integer, T>> index(Stream<T> stream) {
        IntegerValue index = new IntegerValue(-1);
        return stream.map(t -> new KeyValue<Integer, T>(index.add().get(), t));
    }

    public static <T> Map<T, Long> countBy(Stream<T> stream) {
        return stream.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    /**
     * @see http://stackoverflow.com/questions/26403319/skip-last-x-elements-in-streamt
     */
    public static <T> Stream<T> skip(Stream<T> s, int first, int last) {
        if (last <= 0 && first <= 0) {
            return s;
        }
        if (last <= 0) {
            return s.skip(first);
        }
        if (first > 0) {
            s = s.skip(first);
        }
        ArrayDeque<T> pending = new ArrayDeque<T>(last + 1);
        Spliterator<T> src = s.spliterator();
        return StreamSupport.stream(new Spliterator<T>() {
            @Override
            public boolean tryAdvance(Consumer<? super T> action) {
                while (pending.size() <= last && src.tryAdvance(pending::add))
                    ;
                if (pending.size() > last) {
                    action.accept(pending.remove());
                    return true;
                }
                return false;
            }

            @Override
            public Spliterator<T> trySplit() {
                return null;
            }

            @Override
            public long estimateSize() {
                return src.estimateSize() - last;
            }

            @Override
            public int characteristics() {
                return src.characteristics();
            }
        }, false);
    }

    public static <T> Stream<T> skip(Integer first, Integer last, Stream<T> s) {
        return skip(s, first == null ? 0 : first, last == null ? 0 : last);
    }

    public static <T> T merge(List<T> list, int index, T object, BinaryOperator<T> operate) {
        return list.set(index, operate.apply(list.get(index), object));
    }

    public static String append(List<String> list, int index, String string, String seperator) {
        return merge(list, index, string, (a, b) -> StringUtils.isBlank(a) ? b : a + seperator + b);
    }

    public static String firstNotBlank(String... strings) {
        return streamArray(strings).filter(isNotBlank()).findFirst().orElse(null);
    }

    @SafeVarargs
    public static <T> T firstNotBlankAndNotNull(T... o) {
        return streamArray(o).filter(isNotBlankAndNotNull()).findFirst().orElse(null);
    }

    @SafeVarargs
    public static <T> T firstNotNull(T... o) {
        return streamArray(o).filter(isNotNull()).findFirst().orElse(null);
    }

    public static <T> List<T> copy(List<T> list, int maxSize) {
        if (list.size() <= maxSize) return list;
        return list.stream().limit(maxSize).collect(collectList());
    }

    public static <T> List<T> listLI(List<T> list, T item) {
        list.add(item);
        return list;
    }

    public static <T> List<T> listIL(T item, List<T> list) {
        list.add(0, item);
        return list;
    }

    public static <T> List<T> listLL(List<T> list1, List<T> list2) {
        list1.addAll(list2);
        return list1;
    }

    public static <T> List<T> listCLI(List<T> list, T item) {
        List<T> tmp = new ArrayList<>();
        tmp.addAll(list);
        tmp.add(item);
        return tmp;
    }

    public static <T> List<T> listCIL(T item, List<T> list) {
        List<T> tmp = new ArrayList<>();
        tmp.add(item);
        tmp.addAll(list);
        return tmp;
    }

    public static <T> List<T> listCLL(List<T> list1, List<T> list2) {
        List<T> tmp = new ArrayList<>();
        tmp.addAll(list1);
        tmp.addAll(list2);
        return tmp;
    }

    public static <R, K, V> List<R> flatten(Collection<R> records, Function<R, K> toKey, Function<R, V> toValue, BinaryOperator<V> mergeValue,
            BiFunction<K, V, R> mergeToRecord) {
        Map<K, List<R>> map = records.stream().collect(Collectors.groupingBy(toKey));
        return map.entrySet().stream().map(entry -> {
            return new KeyValue<>(entry.getKey(), entry.getValue().stream().map(toValue).reduce(mergeValue).get());
        }).map(entry -> {
            return mergeToRecord.apply(entry.getKey(), entry.getValue());
        }).collect(Collectors.toList());
    }

    public static <T> Collector<T, ?, EnhancedMap<T, Integer>> groupingByCount() {
        return Collectors.groupingBy(id(), newMap(), Collectors.reducing(0, e -> 1, Integer::sum));
    }

    public static <T> Collector<T, ?, EnhancedMap<T, Long>> groupingByCountMany() {
        return Collectors.groupingBy(id(), newMap(), Collectors.counting());
    }

    public static Comparator<Float> ascFloat() {
        return (a, b) -> new CompareToBuilder().append(a, b).toComparison();
    }

    public static Comparator<Float> descFloat() {
        return (a, b) -> -new CompareToBuilder().append(a, b).toComparison();
    }

    public static Comparator<Double> ascDouble() {
        return (a, b) -> new CompareToBuilder().append(a, b).toComparison();
    }

    public static Comparator<Double> descDouble() {
        return (a, b) -> -new CompareToBuilder().append(a, b).toComparison();
    }

    public static Comparator<Byte> ascByte() {
        return (a, b) -> a - b;
    }

    public static Comparator<Byte> descByte() {
        return (a, b) -> b - a;
    }

    public static Comparator<Short> ascShort() {
        return (a, b) -> a - b;
    }

    public static Comparator<Short> descShort() {
        return (a, b) -> b - a;
    }

    public static Comparator<Integer> ascInt() {
        return (a, b) -> a - b;
    }

    public static Comparator<Integer> descInt() {
        return (a, b) -> b - a;
    }

    public static Comparator<Long> ascLong() {
        return (a, b) -> new CompareToBuilder().append(a, b).toComparison();
    }

    public static Comparator<Long> descLong() {
        return (a, b) -> -new CompareToBuilder().append(a, b).toComparison();
    }

    public static <K, V> LinkedHashMap<K, V> sortByValue(Map<K, V> map, Comparator<V> comparator) {
        return map.entrySet().stream().sorted(Map.Entry.comparingByValue(comparator)).collect(
                Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public static <T> List<T> repeat(int times, T object) {
        return IntStream.range(0, times).mapToObj(i -> object).collect(Collectors.toList());
    }

    public static <T> Stream<T> stream(Optional<T> optional) {
        return optional.isPresent() ? Stream.of(optional.get()) : Stream.empty();
    }

    public static <T extends Serializable> List<T> serializable(List<T> list) {
        return list instanceof Serializable ? list : new ArrayList<>(list);
    }

    public static <T extends Serializable> Set<T> serializable(Set<T> set) {
        return set instanceof Serializable ? set : new HashSet<>(set);
    }

    public static <T extends Serializable> SortedSet<T> serializable(SortedSet<T> set) {
        return set instanceof Serializable ? set : new TreeSet<>(set);
    }

    public static <K extends Serializable, V extends Serializable> Map<K, V> serializable(Map<K, V> set) {
        return set instanceof Serializable ? set : new HashMap<>(set);
    }

    public static <K extends Serializable, V extends Serializable> SortedMap<K, V> serializable(SortedMap<K, V> set) {
        return set instanceof Serializable ? set : new TreeMap<>(set);
    }

    public static IntStream stream(byte[] bytes) {
        return IntStream.range(0, bytes.length).map(idx -> bytes[idx]);
    }

    public static IntStream stream(Byte[] bytes) {
        return IntStream.range(0, bytes.length).map(idx -> bytes[idx]);
    }

    public static <A> Predicate<A> notNullPredicate() {
        return a -> a instanceof String ? StringUtils.isNotBlank((String) a) : a != null;
    }

    public static <A, B> BiPredicate<A, B> notNullBiPredicate() {
        return (a, b) -> notNullPredicate().test(a) || notNullPredicate().test(b);
    }

    public static <A, B, C> TriplePredicate<A, B, C> notNullTriplePredicate() {
        return (a, b, c) -> notNullPredicate().test(a) || notNullPredicate().test(b) || notNullPredicate().test(c);
    }

    public static <A, B, C, D> QuadruplePredicate<A, B, C, D> notNullQuadruplePredicate() {
        return (a, b, c, d) -> notNullPredicate().test(a) || notNullPredicate().test(b) || notNullPredicate().test(c) || notNullPredicate().test(d);
    }

    public static <A, B, C, D, E> QuintuplePredicate<A, B, C, D, E> notNullQuintuplePredicate() {
        return (a, b, c, d, e) -> notNullPredicate().test(a) || notNullPredicate().test(b) || notNullPredicate().test(c) || notNullPredicate().test(d)
                || notNullPredicate().test(e);
    }

    public static <T> List<T> replaceOrAdd(List<T> list, T object) {
        if (list == null || object == null) return list;
        int i = list.indexOf(object);
        if (i != -1) {
            list.remove(object);
            list.add(i, object);
        } else {
            list.add(object);
        }
        return list;
    }

    public static <T> Consumer<T> throwing(Supplier<RuntimeException> exception) {
        return t -> {
            throw exception.get();
        };
    }

    public static <T> Consumer<T> throwing(RuntimeException exception) {
        return t -> {
            throw exception;
        };
    }

    public static <T> Consumer<T> throwing(Function<T, RuntimeException> exception) {
        return t -> {
            throw exception.apply(t);
        };
    }

    public static BooleanSupplier enhanceBooleanSupplier(EBooleanSupplier supplier) {
        return EBooleanSupplier.enhance(supplier);
    }

    public static <T> Consumer<T> enhanceConsumer(EConsumer<T> consumer) {
        return EConsumer.enhance(consumer);
    }

    public static <T, U> BiConsumer<T, U> enhanceBiConsumer(EBiConsumer<T, U> consumer) {
        return EBiConsumer.enhance(consumer);
    }

    public static DoubleSupplier enhanceDoubleSupplier(EDoubleSupplier supplier) {
        return EDoubleSupplier.enhance(supplier);
    }

    public static <T, R> Function<T, R> enhanceFunction(EFunction<T, R> function) {
        return EFunction.enhance(function);
    }

    public static IntSupplier enhanceIntegerSupplier(EIntSupplier supplier) {
        return EIntSupplier.enhance(supplier);
    }

    public static LongSupplier enhanceLongSupplier(ELongSupplier supplier) {
        return ELongSupplier.enhance(supplier);
    }

    public static <T> Predicate<T> enhancePredicate(EPredicate<T> predicate) {
        return EPredicate.enhance(predicate);
    }

    public static Runnable enhanceRunnable(ERunnable runnable) {
        return ERunnable.enhance(runnable);
    }

    public static <T> Supplier<T> enhanceSupplier(ESupplier<T> predicate) {
        return ESupplier.enhance(predicate);
    }
}