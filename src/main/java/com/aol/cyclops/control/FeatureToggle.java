package com.aol.cyclops.control;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple2;
import org.reactivestreams.Publisher;

import com.aol.cyclops.Monoid;
import com.aol.cyclops.control.Matchable.CheckValue1;
import com.aol.cyclops.types.Combiner;
import com.aol.cyclops.types.Filterable;
import com.aol.cyclops.types.Functor;
import com.aol.cyclops.types.MonadicValue;
import com.aol.cyclops.types.MonadicValue1;
import com.aol.cyclops.types.To;
import com.aol.cyclops.types.Value;
import com.aol.cyclops.types.anyM.AnyMValue;
import com.aol.cyclops.types.applicative.ApplicativeFunctor;
import com.aol.cyclops.util.function.Curry;
import com.aol.cyclops.util.function.QuadFunction;
import com.aol.cyclops.util.function.TriFunction;


/**
 * An 'Optional' like data type that can either be enabled or disabled
 * 
 * @author johnmcclean
 *
 * @param <T> Type of value storable in this FeatureToggle
 */
public interface FeatureToggle<T>
        extends To<FeatureToggle<T>>,Supplier<T>, MonadicValue1<T>, Filterable<T>, Functor<T>, ApplicativeFunctor<T>, Matchable.ValueAndOptionalMatcher<T> {

    
    /* (non-Javadoc)
     * @see com.aol.cyclops.types.Applicative#combine(java.util.function.BinaryOperator, com.aol.cyclops.types.Applicative)
     */
    @Override
    default  FeatureToggle<T> combine(BinaryOperator<Combiner<T>> combiner, Combiner<T> app) {
       
        return (FeatureToggle<T>)ApplicativeFunctor.super.combine(combiner, app);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops.types.MonadicValue#forEach4(java.util.function.Function, java.util.function.BiFunction, com.aol.cyclops.util.function.TriFunction, com.aol.cyclops.util.function.QuadFunction)
     */
    @Override
    default <T2, R1, R2, R3, R> FeatureToggle<R> forEach4(Function<? super T, ? extends MonadicValue<R1>> value1,
            BiFunction<? super T, ? super R1, ? extends MonadicValue<R2>> value2,
            TriFunction<? super T, ? super R1, ? super R2, ? extends MonadicValue<R3>> value3,
            QuadFunction<? super T, ? super R1, ? super R2, ? super R3, ? extends R> yieldingFunction) {
        return (FeatureToggle<R>)MonadicValue1.super.forEach4(value1, value2, value3, yieldingFunction);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops.types.MonadicValue#forEach4(java.util.function.Function, java.util.function.BiFunction, com.aol.cyclops.util.function.TriFunction, com.aol.cyclops.util.function.QuadFunction, com.aol.cyclops.util.function.QuadFunction)
     */
    @Override
    default <T2, R1, R2, R3, R> FeatureToggle<R> forEach4(Function<? super T, ? extends MonadicValue<R1>> value1,
            BiFunction<? super T, ? super R1, ? extends MonadicValue<R2>> value2,
            TriFunction<? super T, ? super R1, ? super R2, ? extends MonadicValue<R3>> value3,
            QuadFunction<? super T, ? super R1, ? super R2, ? super R3, Boolean> filterFunction,
            QuadFunction<? super T, ? super R1, ? super R2, ? super R3, ? extends R> yieldingFunction) {
        
        return (FeatureToggle<R>)MonadicValue1.super.forEach4(value1, value2, value3, filterFunction, yieldingFunction);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops.types.MonadicValue#forEach3(java.util.function.Function, java.util.function.BiFunction, com.aol.cyclops.util.function.TriFunction)
     */
    @Override
    default <T2, R1, R2, R> FeatureToggle<R> forEach3(Function<? super T, ? extends MonadicValue<R1>> value1,
            BiFunction<? super T, ? super R1, ? extends MonadicValue<R2>> value2,
            TriFunction<? super T, ? super R1, ? super R2, ? extends R> yieldingFunction) {
      
        return (FeatureToggle<R>)MonadicValue1.super.forEach3(value1, value2, yieldingFunction);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops.types.MonadicValue#forEach3(java.util.function.Function, java.util.function.BiFunction, com.aol.cyclops.util.function.TriFunction, com.aol.cyclops.util.function.TriFunction)
     */
    @Override
    default <T2, R1, R2, R> FeatureToggle<R> forEach3(Function<? super T, ? extends MonadicValue<R1>> value1,
            BiFunction<? super T, ? super R1, ? extends MonadicValue<R2>> value2,
            TriFunction<? super T, ? super R1, ? super R2, Boolean> filterFunction,
            TriFunction<? super T, ? super R1, ? super R2, ? extends R> yieldingFunction) {

        return (FeatureToggle<R>)MonadicValue1.super.forEach3(value1, value2, filterFunction, yieldingFunction);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops.types.MonadicValue#forEach2(java.util.function.Function, java.util.function.BiFunction)
     */
    @Override
    default <R1, R> FeatureToggle<R> forEach2(Function<? super T, ? extends MonadicValue<R1>> value1,
            BiFunction<? super T, ? super R1, ? extends R> yieldingFunction) {

        return (FeatureToggle<R>)MonadicValue1.super.forEach2(value1, yieldingFunction);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops.types.MonadicValue#forEach2(java.util.function.Function, java.util.function.BiFunction, java.util.function.BiFunction)
     */
    @Override
    default <R1, R> FeatureToggle<R> forEach2(Function<? super T, ? extends MonadicValue<R1>> value1,
            BiFunction<? super T, ? super R1, Boolean> filterFunction,
            BiFunction<? super T, ? super R1, ? extends R> yieldingFunction) {
        return (FeatureToggle<R>)MonadicValue1.super.forEach2(value1, filterFunction, yieldingFunction);
    }

    /**
     * @return true if enabled
     */
    boolean isEnabled();

    /**
     * @return false if disabled
     * 
     */
    boolean isDisabled();

    /**
     * Narrow covariant type parameter
     * 
     * @param toggle FeatureToggle with covariant type parameter
     * @return Narrowed FeatureToggle
     */
    static <T> FeatureToggle<T> narrow(final FeatureToggle<? extends T> toggle) {
        return (FeatureToggle<T>) toggle;
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops.types.Functor#cast(java.lang.Class)
     */
    @Override
    default <U> FeatureToggle<U> cast(final Class<? extends U> type) {
        return (FeatureToggle<U>) ApplicativeFunctor.super.cast(type);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops.types.Functor#trampoline(java.util.function.Function)
     */
    @Override
    default <R> FeatureToggle<R> trampoline(final Function<? super T, ? extends Trampoline<? extends R>> mapper) {

        return (FeatureToggle<R>) ApplicativeFunctor.super.trampoline(mapper);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops.types.Filterable#ofType(java.lang.Class)
     */
    @Override
    default <U> FeatureToggle<U> ofType(final Class<? extends U> type) {

        return (FeatureToggle<U>) Filterable.super.ofType(type);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops.types.Filterable#filterNot(java.util.function.Predicate)
     */
    @Override
    default FeatureToggle<T> filterNot(final Predicate<? super T> fn) {

        return (FeatureToggle<T>) Filterable.super.filterNot(fn);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops.types.Filterable#notNull()
     */
    @Override
    default FeatureToggle<T> notNull() {

        return (FeatureToggle<T>) Filterable.super.notNull();
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops.types.MonadicValue#unit(java.lang.Object)
     */
    @Override
    default <T> FeatureToggle<T> unit(final T unit) {
        return FeatureToggle.enable(unit);
    }

    /**
     * If this FeatureToggle is enabled the enabled function will be executed
     * If this FeatureToggle is disabled the disabled function will be executed
     * 
     * @param enabled Function to execute if enabled
     * @param disabled Function to execute if disabled
     * @return
     */
    <R> R visit(Function<? super T, ? extends R> enabled, Function<? super T, ? extends R> disabled);

    /* (non-Javadoc)
     * @see com.aol.cyclops.types.Functor#patternMatch(java.util.function.Function, java.util.function.Supplier)
     */
    @Override
    default <R> FeatureToggle<R> patternMatch(final Function<CheckValue1<T, R>, CheckValue1<T, R>> case1, final Supplier<? extends R> otherwise) {

        return (FeatureToggle<R>) ApplicativeFunctor.super.patternMatch(case1, otherwise);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops.types.Value#toFeatureToggle()
     */
    @Override
    default FeatureToggle<T> toFeatureToggle() {
        return this;
    }

    /**
     * @return This monad, wrapped as AnyM
     */
    @Override
    public AnyMValue<T> anyM();

    /**
     * @return This monad, wrapped as AnyM of Disabled
     */
    public AnyM<T> anyMDisabled();

    /**
     * @return This monad, wrapped as AnyM of Enabled
     */
    public AnyM<T> anyMEnabled();

    /* (non-Javadoc)
     * @see java.util.function.Supplier#get()
     */
    @Override
    T get();
    
    

    /* (non-Javadoc)
     * @see com.aol.cyclops.types.MonadicValue1#flatMapIterable(java.util.function.Function)
     */
    @Override
    default <R> FeatureToggle<R> flatMapIterable(Function<? super T, ? extends Iterable<? extends R>> mapper) {
        
        return (FeatureToggle<R> )MonadicValue1.super.flatMapIterable(mapper);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops.types.MonadicValue1#flatMapPublisher(java.util.function.Function)
     */
    @Override
    default <R> FeatureToggle<R>  flatMapPublisher(Function<? super T, ? extends Publisher<? extends R>> mapper) {
       
        return (FeatureToggle<R> )MonadicValue1.super.flatMapPublisher(mapper);
    }

    /**
     * Create a new enabled switch
     * 
     * @param f switch value
     * @return enabled switch
     */
    public static <F> Enabled<F> enable(final F f) {
        return new Enabled<F>(
                              f);
    }

    /**
     * Create a new disabled switch
     * 
     * @param f switch value
     * @return disabled switch
     */
    public static <F> Disabled<F> disable(final F f) {
        return new Disabled<F>(
                               f);
    }

    /**
     * 
     * 
     * @param from Create a switch with the same type
     * @param f but with this value (f)
     * @return new switch
     */
    public static <F> FeatureToggle<F> from(final FeatureToggle<F> from, final F f) {
        if (from.isEnabled())
            return enable(f);
        return disable(f);
    }

    /**
     * Peek at current switch value
     * 
     * @param consumer Consumer to provide current value to
     * @return This Switch
     */
    @Override
    default FeatureToggle<T> peek(final Consumer<? super T> consumer) {
        if (this.isEnabled())
            consumer.accept(get());
        return this;
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops.types.MonadicValue#nest()
     */
    @Override
    default FeatureToggle<MonadicValue<T>> nest() {

        return (FeatureToggle<MonadicValue<T>>) MonadicValue1.super.nest();
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops.types.MonadicValue1#coflatMap(java.util.function.Function)
     */
    @Override
    default <R> FeatureToggle<R> coflatMap(final Function<? super MonadicValue<T>, R> mapper) {

        return (FeatureToggle<R>) MonadicValue1.super.coflatMap(mapper);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops.types.MonadicValue1#combineEager(com.aol.cyclops.Monoid, com.aol.cyclops.types.MonadicValue)
     */
    @Override
    default FeatureToggle<T> combineEager(final Monoid<T> monoid, final MonadicValue<? extends T> v2) {

        return (FeatureToggle<T>) MonadicValue1.super.combineEager(monoid, v2);
    }

    /**
     * @param flatMapper Create a new Switch with provided function
     * @return switch from function
     */
    @Override
    default <X> FeatureToggle<X> flatMap(final Function<? super T, ? extends MonadicValue<? extends X>> flatMapper) {
        if (isDisabled())
            return (FeatureToggle<X>) this;
        return narrow(flatMapper.apply(get())
                                .toFeatureToggle());
    }

    /**
     * @param map transform the value inside this Switch into new Switch object
     * @return new Switch with transformed value
     */
    @Override
    default <X> FeatureToggle<X> map(final Function<? super T, ? extends X> map) {
        if (isDisabled())
            return (FeatureToggle<X>) this;
        return enable(map.apply(get()));
    }

    /**
     * Filter this Switch. If current value does not meet criteria,
     * a disabled Switch is returned
     * 
     * @param p Predicate to test for
     * @return Filtered switch
     */
    @Override
    default FeatureToggle<T> filter(final Predicate<? super T> p) {
        if (isDisabled())
            return this;
        if (!p.test(get()))
            return FeatureToggle.disable(get());
        return this;
    }

    /**
     * Iterate over value in switch (single value, so one iteration)
     * @param consumer to provide value to.
     */
    @Override
    default void forEach(final Consumer<? super T> consumer) {
        if (isDisabled())
            return;
        consumer.accept(get());
    }

    /**
     * @return transform this Switch into an enabled Switch
     */
    default Enabled<T> enable() {
        return new Enabled<T>(
                              get());
    }

    /**
     * @return transform this Switch into a disabled Switch
     */
    default Disabled<T> disable() {
        return new Disabled<T>(
                               get());
    }

    /**
     * @return flip this Switch
     */
    default FeatureToggle<T> flip() {

        if (isEnabled())
            return disable();
        else
            return enable();
    }

    /**
     * @return Optional.empty() if disabled, Optional containing current value if enabled
     */
    default Optional<T> optional() {
        return stream().findFirst();
    }

    /**
     * @return emty Stream if disabled, Stream with current value if enabled.
     */
    @Override
    default ReactiveSeq<T> stream() {
        if (isEnabled())
            return ReactiveSeq.of(get());
        else
            return ReactiveSeq.of();
    }

    @Override
    default Iterator<T> iterator() {

        return Matchable.ValueAndOptionalMatcher.super.iterator();
    }

    /**
     * An enabled switch
     * 
     * <pre>
     * 
     * Switch&lt;Data&gt; data = Switch.enabled(data);
     * 
     * data.map(this::load);  //data will be loaded because Switch is of type Enabled
     * 
     * </pre>
     * @author johnmcclean
     *
     * @param <F> Type of value Enabled Switch holds
     */
    @lombok.Value
    public static class Enabled<F> implements FeatureToggle<F> {

        private final F enabled;

        /**
         * @return This monad, wrapped as AnyM
         */
        @Override
        public AnyMValue<F> anyM() {
            return AnyM.ofValue(this);
        }

        /**
         * @return This monad, wrapped as AnyM of Disabled
         */
        @Override
        public AnyMValue<F> anyMDisabled() {
            return AnyM.ofValue(Optional.empty());
        }

        /**
         * @return This monad, wrapped as AnyM of Enabled
         */
        @Override
        public AnyMValue<F> anyMEnabled() {
            return anyM();
        }

        /**
         * Create a new enabled switch
         * 
         * @param f switch value
         * @return enabled switch
         */
        public static <F> Enabled<F> of(final F f) {
            return new Enabled<F>(
                                  f);
        }

        /**
         * Create a new enabled switch
         * 
         * @param f switch value
         * @return enabled switch
         */
        public static <F> AnyM<F> anyMOf(final F f) {
            return new Enabled<F>(
                                  f).anyM();
        }

        /* 
         *	@return
         * @see com.aol.cyclops.enableswitch.Switch#get()
         */
        @Override
        public F get() {
            return enabled;
        }

        /**
         * Constructs an Enabled Switch
         *
         * @param enabled The value of this Enabled Switch
         */
        Enabled(final F enabled) {
            this.enabled = enabled;
        }

        /* 
         * @param obj to check equality with
         * @return whether objects are equal
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(final Object obj) {
            return obj == this || obj instanceof Enabled && Objects.equals(enabled, ((Enabled<?>) obj).enabled);
        }

        /* 
         *
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            return Objects.hashCode(enabled);
        }

        /*  
         *
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return String.format("Enabled(%s)", enabled);
        }

        /* 
         *	@return true - is Enabled
         * @see com.aol.cyclops.enableswitch.Switch#isEnabled()
         */
        @Override
        public final boolean isEnabled() {
            return true;
        }

        /* 
         *	@return false - is not Disabled
         * @see com.aol.cyclops.enableswitch.Switch#isDisabled()
         */
        @Override
        public final boolean isDisabled() {

            return false;
        }

        /* (non-Javadoc)
         * @see com.aol.cyclops.featuretoggle.FeatureToggle#when(java.util.function.Function, java.util.function.Supplier)
         */
        @Override
        public <R> R visit(final Function<? super F, ? extends R> enabled, final Function<? super F, ? extends R> disabled) {
            return enabled.apply(get());
        }
    }

    /**
     * An disabled switch
     * 
     * <pre>
     * 
     * Switch&lt;Data&gt; data = Switch.disabled(data);
     * 
     * data.map(this::load);  //data will NOT be loaded because Switch is of type Disabled
     * 
     * </pre>
     * @author johnmcclean
     *
     * @param <F> Type of value Enabled Switch holds
     */
    @lombok.Value
    public static class Disabled<F> implements FeatureToggle<F> {

        private final F disabled;

        @Override
        public Enabled<F> enable() {
            return new Enabled<F>(
                                  disabled);
        }

        /**
         * Constructs a left.
         *
         * @param disabled The value of this Left
         */
        Disabled(final F disabled) {
            this.disabled = disabled;
        }

        @Override
        public boolean isPresent() {
            return false;
        }

        /**
         * @return This monad, wrapped as AnyM
         */
        @Override
        public AnyMValue<F> anyM() {
            return AnyM.fromOptional(Optional.empty());
        }

        /**
         * @return This monad, wrapped as AnyM of Disabled
         */
        @Override
        public AnyM<F> anyMDisabled() {
            return AnyM.ofValue(this);
        }

        /**
         * @return This monad, wrapped as AnyM of Enabled
         */
        @Override
        public AnyM<F> anyMEnabled() {
            return anyM();
        }

        /**
         * Create a new disabled switch
         * 
         * @param f switch value
         * @return disabled switch
         */
        public static <F> Disabled<F> of(final F f) {
            return new Disabled<F>(
                                   f);
        }

        /**
         * Create a new disabled switch
         * 
         * @param f switch value
         * @return disabled switch
         */
        public static <F> AnyM<F> anyMOf(final F f) {
            return new Disabled<F>(
                                   f).anyM();
        }

        /* 
         *	@return value of this Disabled
         * @see com.aol.cyclops.enableswitch.Switch#get()
         */
        @Override
        public F get() {
            Optional.ofNullable(null)
                    .get();
            return null;
        }

        /* 
         *
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(final Object obj) {
            return obj == this || obj instanceof Disabled && Objects.equals(disabled, ((Disabled<?>) obj).disabled);
        }

        /* 
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            return Objects.hashCode(disabled);
        }

        /* 
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return String.format("Disabled(%s)", disabled);
        }

        /* 
         *	@return false - is NOT enabled
         * @see com.aol.cyclops.enableswitch.Switch#isEnabled()
         */
        @Override
        public final boolean isEnabled() {
            return false;
        }

        /* 
         *	@return true - is Disabled
         * @see com.aol.cyclops.enableswitch.Switch#isDisabled()
         */
        @Override
        public final boolean isDisabled() {
            return true;
        }

        /* (non-Javadoc)
         * @see com.aol.cyclops.featuretoggle.FeatureToggle#when(java.util.function.Function, java.util.function.Supplier)
         */
        @Override
        public <R> R visit(final Function<? super F, ? extends R> enabled, final Function<? super F, ? extends R> disabled) {
            return disabled.apply(get());
        }
    }

    /**
     * Apply a function across to values at once. If this Featue is disabled, or the supplied value represents null, none or disabled then a disabled Feature is returned.
     * Otherwise a Maybe with the function applied with this value and the supplied value is returned
     * 
     * @param app
     * @param fn
     * @return
     */
    @Override
    default <T2, R> FeatureToggle<R> combine(final Value<? extends T2> app, final BiFunction<? super T, ? super T2, ? extends R> fn) {

        return map(v -> Tuple.tuple(v, Curry.curry2(fn)
                                            .apply(v))).flatMap(tuple -> app.visit(i -> Maybe.just(tuple.v2.apply(i)), () -> Maybe.none()));
    }

    /**
     * Equivalent to ap, but accepts an Iterable and takes the first value only from that iterable.
     * 
     * @param app
     * @param fn
     * @return
     */
    @Override
    default <T2, R> FeatureToggle<R> zip(final Iterable<? extends T2> app, final BiFunction<? super T, ? super T2, ? extends R> fn) {

        return map(v -> Tuple.tuple(v, Curry.curry2(fn)
                                            .apply(v))).flatMap(tuple -> Maybe.fromIterable(app)
                                                                              .visit(i -> Maybe.just(tuple.v2.apply(i)), () -> Maybe.none()));
    }

    /**
     * Equivalent to ap, but accepts a Publisher and takes the first value only from that publisher.
     * 
     * @param app
     * @param fn
     * @return
     */
    @Override
    default <T2, R> FeatureToggle<R> zip(final BiFunction<? super T, ? super T2, ? extends R> fn, final Publisher<? extends T2> app) {
        return map(v -> Tuple.tuple(v, Curry.curry2(fn)
                                            .apply(v))).flatMap(tuple -> Maybe.fromPublisher(app)
                                                                              .visit(i -> Maybe.just(tuple.v2.apply(i)), () -> Maybe.none()));

    }

    /* (non-Javadoc)
     * @see com.aol.cyclops.types.Zippable#zip(org.jooq.lambda.Seq, java.util.function.BiFunction)
     */
    @Override
    default <U, R> FeatureToggle<R> zip(final Seq<? extends U> other, final BiFunction<? super T, ? super U, ? extends R> zipper) {

        return (FeatureToggle<R>) MonadicValue1.super.zip(other, zipper);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops.types.Zippable#zip(java.util.stream.Stream, java.util.function.BiFunction)
     */
    @Override
    default <U, R> FeatureToggle<R> zip(final Stream<? extends U> other, final BiFunction<? super T, ? super U, ? extends R> zipper) {

        return (FeatureToggle<R>) MonadicValue1.super.zip(other, zipper);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops.types.Zippable#zip(java.util.stream.Stream)
     */
    @Override
    default <U> FeatureToggle<Tuple2<T, U>> zip(final Stream<? extends U> other) {

        return (FeatureToggle) MonadicValue1.super.zip(other);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops.types.Zippable#zip(org.jooq.lambda.Seq)
     */
    @Override
    default <U> FeatureToggle<Tuple2<T, U>> zip(final Seq<? extends U> other) {

        return (FeatureToggle) MonadicValue1.super.zip(other);
    }

    /* (non-Javadoc)
     * @see com.aol.cyclops.types.Zippable#zip(java.lang.Iterable)
     */
    @Override
    default <U> FeatureToggle<Tuple2<T, U>> zip(final Iterable<? extends U> other) {

        return (FeatureToggle) MonadicValue1.super.zip(other);
    }

}
