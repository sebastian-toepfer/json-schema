/*
 * The MIT License
 *
 * Copyright 2023 sebastian.
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
package io.github.sebastiantoepfer.jsonschema.core.vocab.applicator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;

import io.github.sebastiantoepfer.ddd.media.core.HashMapMedia;
import io.github.sebastiantoepfer.jsonschema.JsonSchemas;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import jakarta.json.Json;
import jakarta.json.JsonValue;
import java.util.List;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

class PrefixItemsKeywordTest {

    @Test
    void should_know_his_name() {
        final Keyword items = new PrefixItemsKeyword(List.of(JsonSchemas.load(JsonValue.TRUE)));

        assertThat(items.hasName("prefixItems"), is(true));
        assertThat(items.hasName("test"), is(false));
    }

    @Test
    void should_return_zero_as_value() {
        assertThat(
            new PrefixItemsKeyword(List.of(JsonSchemas.load(JsonValue.TRUE)))
                .asAnnotation()
                .valueFor(Json.createArrayBuilder().add(1).add(2).build()),
            is(Json.createValue(0))
        );
    }

    @Test
    void should_retrun_true_if_is_applies_to_all_values() {
        assertThat(
            new PrefixItemsKeyword(
                Json.createArrayBuilder()
                    .add(JsonValue.TRUE)
                    .add(JsonValue.TRUE)
                    .build()
                    .stream()
                    .map(JsonSchemas::load)
                    .toList()
            )
                .asAnnotation()
                .valueFor(Json.createArrayBuilder().add(1).add(2).build()),
            is(JsonValue.TRUE)
        );
    }

    @Test
    void should_be_valid_for_non_arrays() {
        assertThat(
            new PrefixItemsKeyword(List.of(JsonSchemas.load(JsonValue.FALSE)))
                .asApplicator()
                .applyTo(Json.createValue(1)),
            is(true)
        );
    }

    @Test
    void should_be_valid_if_first_item_match_schema() {
        assertThat(
            new PrefixItemsKeyword(List.of(JsonSchemas.load(JsonValue.TRUE)))
                .asApplicator()
                .applyTo(Json.createArrayBuilder().add(1).build()),
            is(true)
        );
    }

    @Test
    void should_be_invalid_if_second_item_does_not_match_schema() {
        assertThat(
            new PrefixItemsKeyword(
                Json.createArrayBuilder()
                    .add(JsonValue.TRUE)
                    .add(JsonValue.FALSE)
                    .build()
                    .stream()
                    .map(JsonSchemas::load)
                    .toList()
            )
                .asApplicator()
                .applyTo(Json.createArrayBuilder().add(1).add(3).build()),
            is(false)
        );
    }

    @Test
    void should_be_applicator_and_annotation() {
        assertThat(
            new PrefixItemsKeyword(List.of(JsonSchemas.load(JsonValue.TRUE))).categories(),
            containsInAnyOrder(Keyword.KeywordCategory.APPLICATOR, Keyword.KeywordCategory.ANNOTATION)
        );
    }

    @Test
    void should_be_printable() {
        assertThat(
            new PrefixItemsKeyword(List.of(JsonSchemas.load(JsonValue.TRUE))).printOn(new HashMapMedia()),
            (Matcher) hasEntry(is("prefixItems"), contains(anEmptyMap()))
        );
    }
}
