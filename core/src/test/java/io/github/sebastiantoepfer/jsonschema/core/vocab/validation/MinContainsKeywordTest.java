/*
 * The MIT License
 *
 * Copyright 2024 sebastian.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.github.sebastiantoepfer.jsonschema.core.vocab.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;

import io.github.sebastiantoepfer.ddd.media.core.HashMapMedia;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import io.github.sebastiantoepfer.jsonschema.keyword.StaticAnnotation;
import jakarta.json.Json;
import jakarta.json.JsonValue;
import java.math.BigInteger;
import java.util.List;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

class MinContainsKeywordTest {

    @Test
    void should_know_his_name() {
        final Keyword enumKeyword = new MinContainsKeyword(List.of(), BigInteger.ONE);

        assertThat(enumKeyword.hasName("minContains"), is(true));
        assertThat(enumKeyword.hasName("test"), is(false));
    }

    @Test
    void should_be_printable() {
        assertThat(
            new MinContainsKeyword(List.of(), BigInteger.valueOf(2)).printOn(new HashMapMedia()),
            (Matcher) hasEntry(is("minContains"), is(BigInteger.valueOf(2)))
        );
    }

    @Test
    void should_be_valid_for_non_arrays() {
        assertThat(
            new MinContainsKeyword(List.of(), BigInteger.valueOf(2))
                .asAssertion()
                .isValidFor(JsonValue.EMPTY_JSON_OBJECT),
            is(true)
        );
    }

    @Test
    void should_be_valid_if_contains_applies_to_exact_count() {
        assertThat(
            new MinContainsKeyword(
                List.of(new StaticAnnotation("contains", Json.createArrayBuilder().add(0).add(1).build())),
                BigInteger.valueOf(2)
            )
                .asAssertion()
                .isValidFor(Json.createArrayBuilder().add("foo").add("bar").add(1).build()),
            is(true)
        );
    }

    @Test
    void should_be_valid_if_contains_applies_to_more_items() {
        assertThat(
            new MinContainsKeyword(
                List.of(new StaticAnnotation("contains", Json.createArrayBuilder().add(0).add(3).add(4).build())),
                BigInteger.valueOf(2)
            )
                .asAssertion()
                .isValidFor(Json.createArrayBuilder().add("foo").add(2).add(3).add("bar").add("baz").build()),
            is(true)
        );
    }

    @Test
    void should_be_valid_for_empty_arrays() {
        assertThat(
            new MinContainsKeyword(
                List.of(new StaticAnnotation("contains", Json.createArrayBuilder().build())),
                BigInteger.valueOf(0)
            )
                .asAssertion()
                .isValidFor(JsonValue.EMPTY_JSON_ARRAY),
            is(true)
        );
    }

    @Test
    void should_be_invalid_if_contains_applies_to_less_items() {
        assertThat(
            new MinContainsKeyword(
                List.of(new StaticAnnotation("contains", Json.createArrayBuilder().add(0).build())),
                BigInteger.valueOf(2)
            )
                .asAssertion()
                .isValidFor(Json.createArrayBuilder().add("foo").add(1).build()),
            is(false)
        );
    }

    @Test
    void should_be_valid_if_contains_applies_to_all_and_more_items_in_array() {
        assertThat(
            new MinContainsKeyword(List.of(new StaticAnnotation("contains", JsonValue.TRUE)), BigInteger.valueOf(2))
                .asAssertion()
                .isValidFor(Json.createArrayBuilder().add("foo").add("bar").add("baz").build()),
            is(true)
        );
    }

    @Test
    void should_be_valid_if_contains_applies_to_all_and_exact_items_count_in_array() {
        assertThat(
            new MinContainsKeyword(List.of(new StaticAnnotation("contains", JsonValue.TRUE)), BigInteger.valueOf(2))
                .asAssertion()
                .isValidFor(Json.createArrayBuilder().add("foo").add("bar").build()),
            is(true)
        );
    }

    @Test
    void should_be_invalid_if_contains_applies_to_all_and_less_items_in_array() {
        assertThat(
            new MinContainsKeyword(List.of(new StaticAnnotation("contains", JsonValue.TRUE)), BigInteger.valueOf(2))
                .asAssertion()
                .isValidFor(Json.createArrayBuilder().add("foo").build()),
            is(false)
        );
    }
}
