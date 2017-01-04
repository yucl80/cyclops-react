package cyclops.collections.immutable;

import com.aol.cyclops2.data.collections.extensions.lazy.immutable.LazyPBagX;
import com.aol.cyclops2.data.collections.extensions.persistent.PersistentCollectionX;
import com.aol.cyclops2.data.collections.extensions.standard.MutableCollectionX;
import com.aol.cyclops2.types.OnEmptySwitch;
import com.aol.cyclops2.types.To;
import cyclops.Reducers;
import cyclops.collections.ListX;
import cyclops.control.Trampoline;
import cyclops.function.Fn3;
import cyclops.function.Fn4;
import cyclops.function.Monoid;
import cyclops.function.Reducer;
import cyclops.stream.ReactiveSeq;
import org.jooq.lambda.tuple.Tuple2;
import org.jooq.lambda.tuple.Tuple3;
import org.jooq.lambda.tuple.Tuple4;
import org.pcollections.HashTreePBag;
import org.pcollections.MapPBag;
import org.pcollections.PBag;
import org.reactivestreams.Publisher;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Stream;


public interface PBagX<T> extends To<PBagX<T>>,PBag<T>, PersistentCollectionX<T>, OnEmptySwitch<T, PBag<T>> {
    
    /**
     * Narrow a covariant PBagX
     * 
     * <pre>
     * {@code 
     *  PBaagX<? extends Fruit> set = PBagX.of(apple,bannana);
     *  PBagX<Fruit> fruitSet = PBagX.narrowK(set);
     * }
     * </pre>
     * 
     * @param bagX to narrowK generic type
     * @return PBagX with narrowed type
     */
    public static <T> PBagX<T> narrow(final PBagX<? extends T> bagX) {
        return (PBagX<T>) bagX;
    }
    
    /**
     * Create a PBagX that contains the Integers between start and end
     * 
     * @param start
     *            Number of range to start from
     * @param end
     *            Number for range to end at
     * @return Range PBagX
     */
    public static PBagX<Integer> range(final int start, final int end) {
        return ReactiveSeq.range(start, end)
                          .toPBagX();
    }

    /**
     * Create a PBagX that contains the Longs between start and end
     * 
     * @param start
     *            Number of range to start from
     * @param end
     *            Number for range to end at
     * @return Range PBagX
     */
    public static PBagX<Long> rangeLong(final long start, final long end) {
        return ReactiveSeq.rangeLong(start, end)
                          .toPBagX();
    }

    /**
     * Unfold a function into a PBagX
     * 
     * <pre>
     * {@code 
     *  PBagX.unfold(1,i->i<=6 ? Optional.of(Tuple.tuple(i,i+1)) : Optional.empty());
     * 
     * //(1,2,3,4,5)
     * 
     * }</code>
     * 
     * @param seed Initial value 
     * @param unfolder Iteratively applied function, terminated by an empty Optional
     * @return PBagX generated by unfolder function
     */
    static <U, T> PBagX<T> unfold(final U seed, final Function<? super U, Optional<Tuple2<T, U>>> unfolder) {
        return ReactiveSeq.unfold(seed, unfolder)
                          .toPBagX();
    }

    /**
     * Generate a PBagX from the provided Supplier up to the provided limit number of times
     * 
     * @param limit Max number of elements to generate
     * @param s Supplier to generate PBagX elements
     * @return PBagX generated from the provided Supplier
     */
    public static <T> PBagX<T> generate(final long limit, final Supplier<T> s) {

        return ReactiveSeq.generate(s)
                          .limit(limit)
                          .toPBagX();
    }
    /**
     * Generate a PBagX from the provided value up to the provided limit number of times
     * 
     * @param limit Max number of elements to generate
     * @param s Value for PBagX elements
     * @return PBagX generated from the provided Supplier
     */
    public static <T> PBagX<T> fill(final long limit, final T s) {

        return ReactiveSeq.fill(s)
                          .limit(limit)
                          .toPBagX();
    }
    @Override
    default PBagX<T> materialize() {
        return (PBagX<T>)PersistentCollectionX.super.materialize();
    }

    /**
     * Create a PBagX by iterative application of a function to an initial element up to the supplied limit number of times
     * 
     * @param limit Max number of elements to generate
     * @param seed Initial element
     * @param f Iteratively applied to each element to generate the next element
     * @return PBagX generated by iterative application
     */
    public static <T> PBagX<T> iterate(final long limit, final T seed, final UnaryOperator<T> f) {
        return ReactiveSeq.iterate(seed, f)
                          .limit(limit)
                          .toPBagX();

    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.data.collections.extensions.CollectionX#forEach4(java.util.function.Function, java.util.function.BiFunction, com.aol.cyclops2.util.function.TriFunction, com.aol.cyclops2.util.function.QuadFunction)
     */
    @Override
    default <R1, R2, R3, R> PBagX<R> forEach4(Function<? super T, ? extends Iterable<R1>> stream1,
            BiFunction<? super T, ? super R1, ? extends Iterable<R2>> stream2,
            Fn3<? super T, ? super R1, ? super R2, ? extends Iterable<R3>> stream3,
            Fn4<? super T, ? super R1, ? super R2, ? super R3, ? extends R> yieldingFunction) {
        
        return (PBagX)PersistentCollectionX.super.forEach4(stream1, stream2, stream3, yieldingFunction);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.data.collections.extensions.CollectionX#forEach4(java.util.function.Function, java.util.function.BiFunction, com.aol.cyclops2.util.function.TriFunction, com.aol.cyclops2.util.function.QuadFunction, com.aol.cyclops2.util.function.QuadFunction)
     */
    @Override
    default <R1, R2, R3, R> PBagX<R> forEach4(Function<? super T, ? extends Iterable<R1>> stream1,
            BiFunction<? super T, ? super R1, ? extends Iterable<R2>> stream2,
            Fn3<? super T, ? super R1, ? super R2, ? extends Iterable<R3>> stream3,
            Fn4<? super T, ? super R1, ? super R2, ? super R3, Boolean> filterFunction,
            Fn4<? super T, ? super R1, ? super R2, ? super R3, ? extends R> yieldingFunction) {
        
        return (PBagX)PersistentCollectionX.super.forEach4(stream1, stream2, stream3, filterFunction, yieldingFunction);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.data.collections.extensions.CollectionX#forEach3(java.util.function.Function, java.util.function.BiFunction, com.aol.cyclops2.util.function.TriFunction)
     */
    @Override
    default <R1, R2, R> PBagX<R> forEach3(Function<? super T, ? extends Iterable<R1>> stream1,
            BiFunction<? super T, ? super R1, ? extends Iterable<R2>> stream2,
            Fn3<? super T, ? super R1, ? super R2, ? extends R> yieldingFunction) {
        
        return (PBagX)PersistentCollectionX.super.forEach3(stream1, stream2, yieldingFunction);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.data.collections.extensions.CollectionX#forEach3(java.util.function.Function, java.util.function.BiFunction, com.aol.cyclops2.util.function.TriFunction, com.aol.cyclops2.util.function.TriFunction)
     */
    @Override
    default <R1, R2, R> PBagX<R> forEach3(Function<? super T, ? extends Iterable<R1>> stream1,
            BiFunction<? super T, ? super R1, ? extends Iterable<R2>> stream2,
            Fn3<? super T, ? super R1, ? super R2, Boolean> filterFunction,
            Fn3<? super T, ? super R1, ? super R2, ? extends R> yieldingFunction) {
        
        return (PBagX)PersistentCollectionX.super.forEach3(stream1, stream2, filterFunction, yieldingFunction);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.data.collections.extensions.CollectionX#forEach2(java.util.function.Function, java.util.function.BiFunction)
     */
    @Override
    default <R1, R> PBagX<R> forEach2(Function<? super T, ? extends Iterable<R1>> stream1,
            BiFunction<? super T, ? super R1, ? extends R> yieldingFunction) {
        
        return (PBagX)PersistentCollectionX.super.forEach2(stream1, yieldingFunction);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.data.collections.extensions.CollectionX#forEach2(java.util.function.Function, java.util.function.BiFunction, java.util.function.BiFunction)
     */
    @Override
    default <R1, R> PBagX<R> forEach2(Function<? super T, ? extends Iterable<R1>> stream1,
            BiFunction<? super T, ? super R1, Boolean> filterFunction,
            BiFunction<? super T, ? super R1, ? extends R> yieldingFunction) {
        
        return (PBagX)PersistentCollectionX.super.forEach2(stream1, filterFunction, yieldingFunction);
    }
    
    @Override
    default PBagX<T> take(final long num) {

        return limit(num);
    }
    @Override
    default PBagX<T> drop(final long num) {

        return skip(num);
    }
    @Override
    default ReactiveSeq<T> stream() {

        return ReactiveSeq.fromIterable(this);
    }

    public static <T> PBagX<T> of(final T... values) {
        return new LazyPBagX<>(
                               HashTreePBag.from(Arrays.asList(values)));
    }

    public static <T> PBagX<T> empty() {
        return new LazyPBagX<>(
                               HashTreePBag.empty());
    }

    public static <T> PBagX<T> singleton(final T value) {
        return new LazyPBagX<>(
                               HashTreePBag.singleton(value));
    }

    /**
     * Construct a PBagX from an Publisher
     * 
     * @param publisher
     *            to construct PBagX from
     * @return PBagX
     */
    public static <T> PBagX<T> fromPublisher(final Publisher<? extends T> publisher) {
        return ReactiveSeq.fromPublisher((Publisher<T>) publisher)
                          .toPBagX();
    }

    public static <T> PBagX<T> fromIterable(final Iterable<T> iterable) {
        if (iterable instanceof PBagX)
            return (PBagX) iterable;
        if (iterable instanceof PBag)
            return new LazyPBagX<>(
                                   (PBag) iterable);
        MapPBag<T> res = HashTreePBag.<T> empty();
        final Iterator<T> it = iterable.iterator();
        while (it.hasNext())
            res = res.plus(it.next());

        return new LazyPBagX<>(
                               res);
    }

    public static <T> PBagX<T> fromCollection(final Collection<T> stream) {
        if (stream instanceof PBagX)
            return (PBagX) stream;
        if (stream instanceof PBag)
            return new LazyPBagX<>(
                                   (PBag) stream);

        return new LazyPBagX<>(
                               HashTreePBag.from(stream));
    }

    public static <T> PBagX<T> fromStream(final Stream<T> stream) {
        return Reducers.<T> toPBagX()
                       .mapReduce(stream);
    }
    /**
     * coflatMap pattern, can be used to perform lazy reductions / collections / folds and other terminal operations
     * 
     * <pre>
     * {@code 
     *   
     *     PBagX.of(1,2,3)
     *          .map(i->i*2)
     *          .coflatMap(s -> s.reduce(0,(a,b)->a+b))
     *      
     *      //PBagX[12]
     * }
     * </pre>
     * 
     * 
     * @param fn mapping function
     * @return Transformed PBagX
     */
    default <R> PBagX<R> coflatMap(Function<? super PBagX<T>, ? extends R> fn){
       return fn.andThen(r ->  this.<R>unit(r))
                .apply(this);
    }

    /**
    * Combine two adjacent elements in a PBagX using the supplied BinaryOperator
    * This is a stateful grouping & reduction operation. The output of a combination may in turn be combined
    * with it's neighbor
    * <pre>
    * {@code 
    *  PBagX.of(1,1,2,3)
                 .combine((a, b)->a.equals(b),Semigroups.intSum)
                 .toListX()
                 
    *  //ListX(3,4) 
    * }</pre>
    * 
    * @param predicate Test to see if two neighbors should be joined
    * @param op Reducer to combine neighbors
    * @return Combined / Partially Reduced PBagX
    */
    @Override
    default PBagX<T> combine(final BiPredicate<? super T, ? super T> predicate, final BinaryOperator<T> op) {
        return (PBagX<T>) PersistentCollectionX.super.combine(predicate, op);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.sequence.traits.ConvertableSequence#toListX()
     */
    @Override
    default PBagX<T> toPBagX() {
        return this;
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.types.Pure#unit(java.lang.Object)
     */
    @Override
    default <R> PBagX<R> unit(final R value) {
        return singleton(value);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.types.IterableFunctor#unitIterable(java.util.Iterator)
     */
    @Override
    default <R> PBagX<R> unitIterator(final Iterator<R> it) {
        return fromIterable(() -> it);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.data.collections.extensions.persistent.PersistentCollectionX#unit(java.util.Collection)
     */
    @Override
    default <R> PBagX<R> unit(final Collection<R> col) {
        return fromCollection(col);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.data.collections.extensions.persistent.PersistentCollectionX#emptyUnit()
     */
    @Override
    default <R> PBagX<R> emptyUnit() {
        return empty();
    }

    /**
     * @return This with typed narrowed to a PBag
     */
    default PBag<T> toPBag() {
        return this;
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.data.collections.extensions.persistent.PersistentCollectionX#from(java.util.Collection)
     */
    @Override
    default <X> PBagX<X> from(final Collection<X> col) {
        return fromCollection(col);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.data.collections.extensions.persistent.PersistentCollectionX#monoid()
     */
    @Override
    default <T> Reducer<PBag<T>> monoid() {
        return Reducers.toPBag();
    }

    /* (non-Javadoc)
     * @see org.pcollections.PSet#plus(java.lang.Object)
     */
    @Override
    public PBagX<T> plus(T e);

    /* (non-Javadoc)
     * @see org.pcollections.PSet#plusAll(java.util.Collection)
     */
    @Override
    public PBagX<T> plusAll(Collection<? extends T> list);

    /* (non-Javadoc)
     * @see org.pcollections.PSet#minus(java.lang.Object)
     */
    @Override
    public PBagX<T> minus(Object e);

    /* (non-Javadoc)
     * @see org.pcollections.PSet#minusAll(java.util.Collection)
     */
    @Override
    public PBagX<T> minusAll(Collection<?> list);

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#reverse()
     */
    @Override
    default PBagX<T> reverse() {
        return (PBagX<T>) PersistentCollectionX.super.reverse();
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#filter(java.util.function.Predicate)
     */
    @Override
    default PBagX<T> filter(final Predicate<? super T> pred) {
        return (PBagX<T>) PersistentCollectionX.super.filter(pred);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#map(java.util.function.Function)
     */
    @Override
    default <R> PBagX<R> map(final Function<? super T, ? extends R> mapper) {
        return (PBagX<R>) PersistentCollectionX.super.map(mapper);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#flatMap(java.util.function.Function)
     */
    @Override
    default <R> PBagX<R> flatMap(final Function<? super T, ? extends Iterable<? extends R>> mapper) {
        return (PBagX<R>) PersistentCollectionX.super.flatMap(mapper);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#limit(long)
     */
    @Override
    default PBagX<T> limit(final long num) {
        return (PBagX<T>) PersistentCollectionX.super.limit(num);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#skip(long)
     */
    @Override
    default PBagX<T> skip(final long num) {
        return (PBagX<T>) PersistentCollectionX.super.skip(num);
    }

    @Override
    default PBagX<T> takeRight(final int num) {
        return (PBagX<T>) PersistentCollectionX.super.takeRight(num);
    }

    @Override
    default PBagX<T> dropRight(final int num) {
        return (PBagX<T>) PersistentCollectionX.super.dropRight(num);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#takeWhile(java.util.function.Predicate)
     */
    @Override
    default PBagX<T> takeWhile(final Predicate<? super T> p) {
        return (PBagX<T>) PersistentCollectionX.super.takeWhile(p);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#dropWhile(java.util.function.Predicate)
     */
    @Override
    default PBagX<T> dropWhile(final Predicate<? super T> p) {
        return (PBagX<T>) PersistentCollectionX.super.dropWhile(p);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#takeUntil(java.util.function.Predicate)
     */
    @Override
    default PBagX<T> takeUntil(final Predicate<? super T> p) {
        return (PBagX<T>) PersistentCollectionX.super.takeUntil(p);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#dropUntil(java.util.function.Predicate)
     */
    @Override
    default PBagX<T> dropUntil(final Predicate<? super T> p) {
        return (PBagX<T>) PersistentCollectionX.super.dropUntil(p);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#trampoline(java.util.function.Function)
     */
    @Override
    default <R> PBagX<R> trampoline(final Function<? super T, ? extends Trampoline<? extends R>> mapper) {
        return (PBagX<R>) PersistentCollectionX.super.trampoline(mapper);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#slice(long, long)
     */
    @Override
    default PBagX<T> slice(final long from, final long to) {
        return (PBagX<T>) PersistentCollectionX.super.slice(from, to);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#sorted(java.util.function.Function)
     */
    @Override
    default <U extends Comparable<? super U>> PBagX<T> sorted(final Function<? super T, ? extends U> function) {
        return (PBagX<T>) PersistentCollectionX.super.sorted(function);
    }

    @Override
    default PBagX<ListX<T>> grouped(final int groupSize) {
        return (PBagX<ListX<T>>) PersistentCollectionX.super.grouped(groupSize);
    }

    @Override
    default <K, A, D> PBagX<Tuple2<K, D>> grouped(final Function<? super T, ? extends K> classifier, final Collector<? super T, A, D> downstream) {
        return (PBagX) PersistentCollectionX.super.grouped(classifier, downstream);
    }

    @Override
    default <K> PBagX<Tuple2<K, ReactiveSeq<T>>> grouped(final Function<? super T, ? extends K> classifier) {
        return (PBagX) PersistentCollectionX.super.grouped(classifier);
    }

    @Override
    default <U> PBagX<Tuple2<T, U>> zip(final Iterable<? extends U> other) {
        return (PBagX) PersistentCollectionX.super.zip(other);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#zip(java.lang.Iterable, java.util.function.BiFunction)
     */
    @Override
    default <U, R> PBagX<R> zip(final Iterable<? extends U> other, final BiFunction<? super T, ? super U, ? extends R> zipper) {

        return (PBagX<R>) PersistentCollectionX.super.zip(other, zipper);
    }



    @Override
    default <U, R> PBagX<R> zipS(final Stream<? extends U> other, final BiFunction<? super T, ? super U, ? extends R> zipper) {

        return (PBagX<R>) PersistentCollectionX.super.zipS(other, zipper);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#permutations()
     */
    @Override
    default PBagX<ReactiveSeq<T>> permutations() {

        return (PBagX<ReactiveSeq<T>>) PersistentCollectionX.super.permutations();
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#combinations(int)
     */
    @Override
    default PBagX<ReactiveSeq<T>> combinations(final int size) {

        return (PBagX<ReactiveSeq<T>>) PersistentCollectionX.super.combinations(size);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#combinations()
     */
    @Override
    default PBagX<ReactiveSeq<T>> combinations() {

        return (PBagX<ReactiveSeq<T>>) PersistentCollectionX.super.combinations();
    }

    @Override
    default PBagX<PVectorX<T>> sliding(final int windowSize) {
        return (PBagX<PVectorX<T>>) PersistentCollectionX.super.sliding(windowSize);
    }

    @Override
    default PBagX<PVectorX<T>> sliding(final int windowSize, final int increment) {
        return (PBagX<PVectorX<T>>) PersistentCollectionX.super.sliding(windowSize, increment);
    }

    @Override
    default PBagX<T> scanLeft(final Monoid<T> monoid) {
        return (PBagX<T>) PersistentCollectionX.super.scanLeft(monoid);
    }

    @Override
    default <U> PBagX<U> scanLeft(final U seed, final BiFunction<? super U, ? super T, ? extends U> function) {
        return (PBagX<U>) PersistentCollectionX.super.scanLeft(seed, function);
    }

    @Override
    default PBagX<T> scanRight(final Monoid<T> monoid) {
        return (PBagX<T>) PersistentCollectionX.super.scanRight(monoid);
    }

    @Override
    default <U> PBagX<U> scanRight(final U identity, final BiFunction<? super T, ? super U, ? extends U> combiner) {
        return (PBagX<U>) PersistentCollectionX.super.scanRight(identity, combiner);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#plusInOrder(java.lang.Object)
     */
    @Override
    default PBagX<T> plusInOrder(final T e) {

        return (PBagX<T>) PersistentCollectionX.super.plusInOrder(e);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#cycle(int)
     */
    @Override
    default PBagX<T> cycle(final long times) {

        return (PBagX<T>) PersistentCollectionX.super.cycle(times);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#cycle(com.aol.cyclops2.sequence.Monoid, int)
     */
    @Override
    default PBagX<T> cycle(final Monoid<T> m, final long times) {

        return (PBagX<T>) PersistentCollectionX.super.cycle(m, times);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#cycleWhile(java.util.function.Predicate)
     */
    @Override
    default PBagX<T> cycleWhile(final Predicate<? super T> predicate) {

        return (PBagX<T>) PersistentCollectionX.super.cycleWhile(predicate);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#cycleUntil(java.util.function.Predicate)
     */
    @Override
    default PBagX<T> cycleUntil(final Predicate<? super T> predicate) {

        return (PBagX<T>) PersistentCollectionX.super.cycleUntil(predicate);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#zip(java.util.stream.Stream)
     */
    @Override
    default <U> PBagX<Tuple2<T, U>> zipS(final Stream<? extends U> other) {
        return (PBagX) PersistentCollectionX.super.zipS(other);
    }



    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#zip3(java.util.stream.Stream, java.util.stream.Stream)
     */
    @Override
    default <S, U> PBagX<Tuple3<T, S, U>> zip3(final Iterable<? extends S> second, final Iterable<? extends U> third) {

        return (PBagX) PersistentCollectionX.super.zip3(second, third);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#zip4(java.util.stream.Stream, java.util.stream.Stream, java.util.stream.Stream)
     */
    @Override
    default <T2, T3, T4> PBagX<Tuple4<T, T2, T3, T4>> zip4(final Iterable<? extends T2> second, final Iterable<? extends T3> third,
            final Iterable<? extends T4> fourth) {

        return (PBagX) PersistentCollectionX.super.zip4(second, third, fourth);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#zipWithIndex()
     */
    @Override
    default PBagX<Tuple2<T, Long>> zipWithIndex() {

        return (PBagX<Tuple2<T, Long>>) PersistentCollectionX.super.zipWithIndex();
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#distinct()
     */
    @Override
    default PBagX<T> distinct() {

        return (PBagX<T>) PersistentCollectionX.super.distinct();
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#sorted()
     */
    @Override
    default PBagX<T> sorted() {

        return (PBagX<T>) PersistentCollectionX.super.sorted();
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#sorted(java.util.Comparator)
     */
    @Override
    default PBagX<T> sorted(final Comparator<? super T> c) {

        return (PBagX<T>) PersistentCollectionX.super.sorted(c);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#skipWhile(java.util.function.Predicate)
     */
    @Override
    default PBagX<T> skipWhile(final Predicate<? super T> p) {

        return (PBagX<T>) PersistentCollectionX.super.skipWhile(p);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#skipUntil(java.util.function.Predicate)
     */
    @Override
    default PBagX<T> skipUntil(final Predicate<? super T> p) {

        return (PBagX<T>) PersistentCollectionX.super.skipUntil(p);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#limitWhile(java.util.function.Predicate)
     */
    @Override
    default PBagX<T> limitWhile(final Predicate<? super T> p) {

        return (PBagX<T>) PersistentCollectionX.super.limitWhile(p);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#limitUntil(java.util.function.Predicate)
     */
    @Override
    default PBagX<T> limitUntil(final Predicate<? super T> p) {

        return (PBagX<T>) PersistentCollectionX.super.limitUntil(p);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#intersperse(java.lang.Object)
     */
    @Override
    default PBagX<T> intersperse(final T value) {

        return (PBagX<T>) PersistentCollectionX.super.intersperse(value);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#shuffle()
     */
    @Override
    default PBagX<T> shuffle() {

        return (PBagX<T>) PersistentCollectionX.super.shuffle();
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#skipLast(int)
     */
    @Override
    default PBagX<T> skipLast(final int num) {

        return (PBagX<T>) PersistentCollectionX.super.skipLast(num);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#limitLast(int)
     */
    @Override
    default PBagX<T> limitLast(final int num) {

        return (PBagX<T>) PersistentCollectionX.super.limitLast(num);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.types.OnEmptySwitch#onEmptySwitch(java.util.function.Supplier)
     */
    @Override
    default PBagX<T> onEmptySwitch(final Supplier<? extends PBag<T>> supplier) {
        if (isEmpty())
            return PBagX.fromIterable(supplier.get());
        return this;
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#onEmpty(java.lang.Object)
     */
    @Override
    default PBagX<T> onEmpty(final T value) {

        return (PBagX<T>) PersistentCollectionX.super.onEmpty(value);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#onEmptyGet(java.util.function.Supplier)
     */
    @Override
    default PBagX<T> onEmptyGet(final Supplier<? extends T> supplier) {

        return (PBagX<T>) PersistentCollectionX.super.onEmptyGet(supplier);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#onEmptyThrow(java.util.function.Supplier)
     */
    @Override
    default <X extends Throwable> PBagX<T> onEmptyThrow(final Supplier<? extends X> supplier) {

        return (PBagX<T>) PersistentCollectionX.super.onEmptyThrow(supplier);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#shuffle(java.util.Random)
     */
    @Override
    default PBagX<T> shuffle(final Random random) {

        return (PBagX<T>) PersistentCollectionX.super.shuffle(random);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#ofType(java.lang.Class)
     */
    @Override
    default <U> PBagX<U> ofType(final Class<? extends U> type) {

        return (PBagX<U>) PersistentCollectionX.super.ofType(type);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#filterNot(java.util.function.Predicate)
     */
    @Override
    default PBagX<T> filterNot(final Predicate<? super T> fn) {

        return (PBagX<T>) PersistentCollectionX.super.filterNot(fn);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#notNull()
     */
    @Override
    default PBagX<T> notNull() {

        return (PBagX<T>) PersistentCollectionX.super.notNull();
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#removeAllS(java.util.stream.Stream)
     */
    @Override
    default PBagX<T> removeAllS(final Stream<? extends T> stream) {

        return (PBagX<T>) PersistentCollectionX.super.removeAllS(stream);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#removeAllS(java.lang.Iterable)
     */
    @Override
    default PBagX<T> removeAllS(final Iterable<? extends T> it) {

        return (PBagX<T>) PersistentCollectionX.super.removeAllS(it);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#removeAllS(java.lang.Object[])
     */
    @Override
    default PBagX<T> removeAllS(final T... values) {

        return (PBagX<T>) PersistentCollectionX.super.removeAllS(values);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#retainAllS(java.lang.Iterable)
     */
    @Override
    default PBagX<T> retainAllS(final Iterable<? extends T> it) {

        return (PBagX<T>) PersistentCollectionX.super.retainAllS(it);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#retainAllS(java.util.stream.Stream)
     */
    @Override
    default PBagX<T> retainAllS(final Stream<? extends T> seq) {

        return (PBagX<T>) PersistentCollectionX.super.retainAllS(seq);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#retainAllS(java.lang.Object[])
     */
    @Override
    default PBagX<T> retainAllS(final T... values) {

        return (PBagX<T>) PersistentCollectionX.super.retainAllS(values);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops2.collections.extensions.persistent.PersistentCollectionX#cast(java.lang.Class)
     */
    @Override
    default <U> PBagX<U> cast(final Class<? extends U> type) {

        return (PBagX<U>) PersistentCollectionX.super.cast(type);
    }



    @Override
    default <C extends Collection<? super T>> PBagX<C> grouped(final int size, final Supplier<C> supplier) {

        return (PBagX<C>) PersistentCollectionX.super.grouped(size, supplier);
    }

    @Override
    default PBagX<ListX<T>> groupedUntil(final Predicate<? super T> predicate) {

        return (PBagX<ListX<T>>) PersistentCollectionX.super.groupedUntil(predicate);
    }

    @Override
    default PBagX<ListX<T>> groupedStatefullyUntil(final BiPredicate<ListX<? super T>, ? super T> predicate) {

        return (PBagX<ListX<T>>) PersistentCollectionX.super.groupedStatefullyUntil(predicate);
    }

    @Override
    default PBagX<ListX<T>> groupedWhile(final Predicate<? super T> predicate) {

        return (PBagX<ListX<T>>) PersistentCollectionX.super.groupedWhile(predicate);
    }

    @Override
    default <C extends Collection<? super T>> PBagX<C> groupedWhile(final Predicate<? super T> predicate, final Supplier<C> factory) {

        return (PBagX<C>) PersistentCollectionX.super.groupedWhile(predicate, factory);
    }

    @Override
    default <C extends Collection<? super T>> PBagX<C> groupedUntil(final Predicate<? super T> predicate, final Supplier<C> factory) {

        return (PBagX<C>) PersistentCollectionX.super.groupedUntil(predicate, factory);
    }

    @Override
    default <R> PBagX<R> retry(final Function<? super T, ? extends R> fn) {
        return (PBagX<R>)PersistentCollectionX.super.retry(fn);
    }

    @Override
    default <R> PBagX<R> retry(final Function<? super T, ? extends R> fn, final int retries, final long delay, final TimeUnit timeUnit) {
        return (PBagX<R>)PersistentCollectionX.super.retry(fn);
    }

    @Override
    default <R> PBagX<R> flatMapS(Function<? super T, ? extends Stream<? extends R>> fn) {
        return (PBagX<R>)PersistentCollectionX.super.flatMapS(fn);
    }

    @Override
    default <R> PBagX<R> flatMapP(Function<? super T, ? extends Publisher<? extends R>> fn) {
        return (PBagX<R>)PersistentCollectionX.super.flatMapP(fn);
    }

    @Override
    default PBagX<T> prependS(Stream<? extends T> stream) {
        return (PBagX<T>)PersistentCollectionX.super.prependS(stream);
    }

    @Override
    default PBagX<T> append(T... values) {
        return (PBagX<T>)PersistentCollectionX.super.append(values);
    }

    @Override
    default PBagX<T> append(T value) {
        return (PBagX<T>)PersistentCollectionX.super.append(value);
    }

    @Override
    default PBagX<T> prepend(T value) {
        return (PBagX<T>)PersistentCollectionX.super.prepend(value);
    }

    @Override
    default PBagX<T> prepend(T... values) {
        return (PBagX<T>)PersistentCollectionX.super.prepend(values);
    }

    @Override
    default PBagX<T> insertAt(int pos, T... values) {
        return (PBagX<T>)PersistentCollectionX.super.insertAt(pos,values);
    }

    @Override
    default PBagX<T> deleteBetween(int start, int end) {
        return (PBagX<T>)PersistentCollectionX.super.deleteBetween(start,end);
    }

    @Override
    default PBagX<T> insertAtS(int pos, Stream<T> stream) {
        return (PBagX<T>)PersistentCollectionX.super.insertAtS(pos,stream);
    }

    @Override
    default PBagX<T> recover(final Function<? super Throwable, ? extends T> fn) {
        return (PBagX<T>)PersistentCollectionX.super.recover(fn);
    }

    @Override
    default <EX extends Throwable> PBagX<T> recover(Class<EX> exceptionClass, final Function<? super EX, ? extends T> fn) {
        return (PBagX<T>)PersistentCollectionX.super.recover(exceptionClass,fn);
    }

    @Override
    default PBagX<T> plusLoop(int max, IntFunction<T> value) {
        return (PBagX<T>)PersistentCollectionX.super.plusLoop(max,value);
    }

    @Override
    default PBagX<T> plusLoop(Supplier<Optional<T>> supplier) {
        return (PBagX<T>)PersistentCollectionX.super.plusLoop(supplier);
    }


}
