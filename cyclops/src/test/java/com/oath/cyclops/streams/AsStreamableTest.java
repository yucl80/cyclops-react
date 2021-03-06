package com.oath.cyclops.streams;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Test;

import cyclops.reactive.Streamable;

import lombok.val;

public class AsStreamableTest {

	@Test
	public void testAsStreamableT() {

		val result = Streamable.<Integer>fromIterable(Arrays.asList(1,2,3)).stream().map(i->i+2).collect(Collectors.toList());

		assertThat(result,equalTo(Arrays.asList(3,4,5)));
	}

	@Test
	public void testAsStreamableStreamOfT() {
		Stream<Integer> stream = Stream.of(1,2,3,4,5);
		val streamable = Streamable.<Integer>fromStream(stream);
		val result1 = streamable.stream().map(i->i+2).collect(Collectors.toList());
		val result2 = streamable.stream().map(i->i+2).collect(Collectors.toList());
		val result3 = streamable.stream().map(i->i+2).collect(Collectors.toList());

		assertThat(result1,equalTo(Arrays.asList(3,4,5,6,7)));
		assertThat(result1,equalTo(result2));
		assertThat(result1,equalTo(result3));
	}



}
