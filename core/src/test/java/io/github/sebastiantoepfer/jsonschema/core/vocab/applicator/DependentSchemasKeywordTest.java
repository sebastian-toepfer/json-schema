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
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;

import io.github.sebastiantoepfer.ddd.media.core.HashMapMedia;
import io.github.sebastiantoepfer.jsonschema.JsonSchemas;
import io.github.sebastiantoepfer.jsonschema.core.keyword.type.NamedJsonSchemas;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import jakarta.json.Json;
import jakarta.json.JsonValue;
import java.util.Map;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

class DependentSchemasKeywordTest {

    @Test
    void should_know_his_name() {
        final Keyword keyword = new DependentSchemasKeyword(new NamedJsonSchemas(Map.of()));

        assertThat(keyword.hasName("dependentSchemas"), is(true));
        assertThat(keyword.hasName("test"), is(false));
    }

    @Test
    void should_be_printable() {
        assertThat(
            new DependentSchemasKeyword(new NamedJsonSchemas(Map.of("foo", JsonSchemas.load(JsonValue.TRUE)))).printOn(
                new HashMapMedia()
            ),
            (Matcher) hasEntry(is("dependentSchemas"), hasEntry(is("foo"), anEmptyMap()))
        );
    }

    @Test
    void should_be_valid_for_non_object() {
        assertThat(
            new DependentSchemasKeyword(new NamedJsonSchemas(Map.of())).asApplicator().applyTo(JsonValue.FALSE),
            is(true)
        );
    }

    @Test
    void should_be_valid_for_empty_object() {
        assertThat(
            new DependentSchemasKeyword(new NamedJsonSchemas(Map.of()))
                .asApplicator()
                .applyTo(JsonValue.EMPTY_JSON_OBJECT),
            is(true)
        );
    }

    @Test
    void should_be_valid_if_property_not_present() {
        assertThat(
            new DependentSchemasKeyword(new NamedJsonSchemas(Map.of("test", JsonSchemas.load(JsonValue.FALSE))))
                .asApplicator()
                .applyTo(Json.createObjectBuilder().add("foo", 1).build()),
            is(true)
        );
    }

    @Test
    void should_be_valid_if_property_is_present_and_schema_apply() {
        assertThat(
            new DependentSchemasKeyword(new NamedJsonSchemas(Map.of("test", JsonSchemas.load(JsonValue.TRUE))))
                .asApplicator()
                .applyTo(Json.createObjectBuilder().add("true", 1).build()),
            is(true)
        );
    }

    @Test
    void should_be_invalid_if_property_is_present_and_schema_not_apply() {
        assertThat(
            new DependentSchemasKeyword(new NamedJsonSchemas(Map.of("test", JsonSchemas.load(JsonValue.FALSE))))
                .asApplicator()
                .applyTo(Json.createObjectBuilder().add("true", 1).build()),
            is(true)
        );
    }
}
