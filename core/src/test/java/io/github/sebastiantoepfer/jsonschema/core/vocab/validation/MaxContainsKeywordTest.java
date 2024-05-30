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
import io.github.sebastiantoepfer.jsonschema.core.DefaultJsonSchemaFactory;
import io.github.sebastiantoepfer.jsonschema.core.keyword.type.AffectsKeywordType;
import io.github.sebastiantoepfer.jsonschema.core.keyword.type.IntegerKeywordType;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import io.github.sebastiantoepfer.jsonschema.keyword.StaticAnnotation;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import java.math.BigInteger;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

class MaxContainsKeywordTest {

    @Test
    void should_know_his_name() {
        final Keyword enumKeyword = new MaxContainsKeyword(new StaticAnnotation("", JsonValue.NULL), BigInteger.ONE);
        assertThat(enumKeyword.hasName("maxContains"), is(true));
        assertThat(enumKeyword.hasName("test"), is(false));
    }

    @Test
    void should_be_printable() {
        assertThat(
            createKeywordFrom(Json.createObjectBuilder().add("maxContains", 2).build()).printOn(new HashMapMedia()),
            (Matcher) hasEntry(is("maxContains"), is(BigInteger.valueOf(2)))
        );
    }

    @Test
    void should_be_valid_for_non_arrays() {
        assertThat(
            createKeywordFrom(Json.createObjectBuilder().add("maxContains", 2).build())
                .asAssertion()
                .isValidFor(JsonValue.EMPTY_JSON_OBJECT),
            is(true)
        );
    }

    @Test
    void should_be_valid_if_no_contains_is_present() {
        assertThat(
            createKeywordFrom(Json.createObjectBuilder().add("maxContains", 2).build())
                .asAssertion()
                .isValidFor(Json.createArrayBuilder().add("foo").build()),
            is(true)
        );
    }

    @Test
    void should_be_valid_if_contains_applies_to_exact_count() {
        assertThat(
            createKeywordFrom(
                Json.createObjectBuilder()
                    .add("contains", Json.createObjectBuilder().add("type", "string"))
                    .add("maxContains", 2)
                    .build()
            )
                .asAssertion()
                .isValidFor(Json.createArrayBuilder().add("foo").add("bar").add(1).build()),
            is(true)
        );
    }

    @Test
    void should_be_valid_if_contains_applies_to_less_items() {
        assertThat(
            createKeywordFrom(
                Json.createObjectBuilder()
                    .add("contains", Json.createObjectBuilder().add("type", "string"))
                    .add("maxContains", 2)
                    .build()
            )
                .asAssertion()
                .isValidFor(Json.createArrayBuilder().add("foo").add(2).add(3).build()),
            is(true)
        );
    }

    @Test
    void should_be_valid_for_empty_arrays() {
        assertThat(
            createKeywordFrom(
                Json.createObjectBuilder()
                    .add("contains", Json.createObjectBuilder().add("type", "string"))
                    .add("maxContains", 2)
                    .build()
            )
                .asAssertion()
                .isValidFor(JsonValue.EMPTY_JSON_ARRAY),
            is(true)
        );
    }

    @Test
    void should_be_invalid_if_contains_applies_to_more_items() {
        assertThat(
            createKeywordFrom(
                Json.createObjectBuilder()
                    .add("contains", Json.createObjectBuilder().add("type", "string"))
                    .add("maxContains", 2)
                    .build()
            )
                .asAssertion()
                .isValidFor(Json.createArrayBuilder().add("foo").add("bar").add(1).add("baz").build()),
            is(false)
        );
    }

    @Test
    void should_be_valid_if_contains_applies_to_all_and_less_items_in_array() {
        assertThat(
            createKeywordFrom(
                Json.createObjectBuilder()
                    .add("contains", Json.createObjectBuilder().add("type", "string"))
                    .add("maxContains", 2)
                    .build()
            )
                .asAssertion()
                .isValidFor(Json.createArrayBuilder().add("foo").build()),
            is(true)
        );
    }

    @Test
    void should_be_valid_if_contains_applies_to_all_and_exact_items_count_in_array() {
        assertThat(
            createKeywordFrom(
                Json.createObjectBuilder()
                    .add("contains", Json.createObjectBuilder().add("type", "string"))
                    .add("maxContains", 2)
                    .build()
            )
                .asAssertion()
                .isValidFor(Json.createArrayBuilder().add("foo").add("bar").build()),
            is(true)
        );
    }

    @Test
    void should_be_invalid_if_contains_applies_to_all_and_more_items_in_array() {
        assertThat(
            createKeywordFrom(
                Json.createObjectBuilder()
                    .add("contains", Json.createObjectBuilder().add("type", "string"))
                    .add("maxContains", 2)
                    .build()
            )
                .asAssertion()
                .isValidFor(Json.createArrayBuilder().add("foo").add("bar").add("baz").build()),
            is(false)
        );
    }

    private static Keyword createKeywordFrom(final JsonObject json) {
        return new AffectsKeywordType(
            "maxContains",
            "contains",
            (a, s) ->
                new IntegerKeywordType(
                    JsonProvider.provider(),
                    "maxContains",
                    value -> new MaxContainsKeyword(a, value)
                ).createKeyword(s)
        ).createKeyword(new DefaultJsonSchemaFactory().create(json));
    }
}
