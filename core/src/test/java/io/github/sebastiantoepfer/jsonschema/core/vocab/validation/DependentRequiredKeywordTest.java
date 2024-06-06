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
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.sebastiantoepfer.ddd.media.core.HashMapMedia;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import java.math.BigDecimal;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

class DependentRequiredKeywordTest {

    @Test
    void should_not_be_createable_with_object_contains_non_string_array_property() {
        final JsonObject obj = Json.createObjectBuilder()
            .add("totalCost", Json.createArrayBuilder().add("units"))
            .add("trackingId", BigDecimal.ONE)
            .build();
        assertThrows(IllegalArgumentException.class, () -> new DependentRequiredKeyword(obj));
    }

    @Test
    void should_not_be_createable_with_object_contains_only_non_string_array_property() {
        final JsonObject obj = Json.createObjectBuilder()
            .add("totalCost", 12.7)
            .add("trackingId", BigDecimal.ONE)
            .build();
        assertThrows(IllegalArgumentException.class, () -> new DependentRequiredKeyword(obj));
    }

    @Test
    void should_know_his_name() {
        final Keyword enumKeyword = new DependentRequiredKeyword(JsonValue.EMPTY_JSON_OBJECT);

        assertThat(enumKeyword.hasName("dependentRequired"), is(true));
        assertThat(enumKeyword.hasName("test"), is(false));
    }

    @Test
    void should_be_valid_if_both_properties_available() {
        assertThat(
            new DependentRequiredKeyword(
                Json.createObjectBuilder().add("license", Json.createArrayBuilder().add("age")).build()
            )
                .asAssertion()
                .isValidFor(
                    Json.createObjectBuilder().add("name", "John").add("age", 25).add("license", "XYZ123").build()
                ),
            is(true)
        );
    }

    @Test
    void should_be_valid_if_required_properties_is_missing() {
        assertThat(
            new DependentRequiredKeyword(
                Json.createObjectBuilder().add("license", Json.createArrayBuilder().add("age")).build()
            )
                .asAssertion()
                .isValidFor(Json.createObjectBuilder().add("name", "John").add("license", "XYZ123").build()),
            is(false)
        );
    }

    @Test
    void should_be_valid_if_both_properties_are_missing() {
        assertThat(
            new DependentRequiredKeyword(
                Json.createObjectBuilder().add("license", Json.createArrayBuilder().add("age")).build()
            )
                .asAssertion()
                .isValidFor(Json.createObjectBuilder().add("name", "John").build()),
            is(true)
        );
    }

    @Test
    void should_be_printable() {
        assertThat(
            new DependentRequiredKeyword(
                Json.createObjectBuilder().add("license", Json.createArrayBuilder().add("age")).build()
            ).printOn(new HashMapMedia()),
            (Matcher) hasEntry(is("dependentRequired"), hasEntry(is("license"), contains("age")))
        );
    }
}
