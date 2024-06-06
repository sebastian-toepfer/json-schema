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
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;

import io.github.sebastiantoepfer.ddd.media.core.HashMapMedia;
import io.github.sebastiantoepfer.jsonschema.JsonSchemas;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import io.github.sebastiantoepfer.jsonschema.keyword.StaticAnnotation;
import jakarta.json.Json;
import jakarta.json.JsonValue;
import java.util.List;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

class AdditionalPropertiesKeywordTest {

    @Test
    void should_know_his_name() {
        final Keyword keyword = new AdditionalPropertiesKeyword(List.of(), JsonSchemas.load(JsonValue.TRUE));
        assertThat(keyword.hasName("additionalProperties"), is(true));
        assertThat(keyword.hasName("test"), is(false));
    }

    @Test
    void should_be_valid_for_non_objects() {
        assertThat(
            new AdditionalPropertiesKeyword(
                List.of(new StaticAnnotation("properties", JsonValue.EMPTY_JSON_ARRAY)),
                JsonSchemas.load(JsonValue.FALSE)
            )
                .asApplicator()
                .applyTo(JsonValue.EMPTY_JSON_ARRAY),
            is(true)
        );
    }

    @Test
    void should_not_valid_if_no_additionals_are_allow() {
        assertThat(
            new AdditionalPropertiesKeyword(
                List.of(new StaticAnnotation("properties", Json.createArrayBuilder().add("test").build())),
                JsonSchemas.load(JsonValue.FALSE)
            )
                .asApplicator()
                .applyTo(Json.createObjectBuilder().add("test", 1).add("foo", 1).build()),
            is(false)
        );
    }

    @Test
    void should_valid_if_additionals_are_allow() {
        assertThat(
            new AdditionalPropertiesKeyword(
                List.of(new StaticAnnotation("properties", Json.createArrayBuilder().add("test").build())),
                JsonSchemas.load(JsonValue.TRUE)
            )
                .asApplicator()
                .applyTo(Json.createObjectBuilder().add("test", 1).add("foo", 1).build()),
            is(true)
        );
    }

    @Test
    void should_valid_if_no_additionals_are_allow_and_no_additionals_their() {
        assertThat(
            new AdditionalPropertiesKeyword(
                List.of(new StaticAnnotation("properties", Json.createArrayBuilder().add("test").build())),
                JsonSchemas.load(JsonValue.FALSE)
            )
                .asApplicator()
                .applyTo(Json.createObjectBuilder().add("test", 1).build()),
            is(true)
        );
    }

    @Test
    void should_be_an_applicator_and_an_annotation() {
        assertThat(
            new AdditionalPropertiesKeyword(
                List.of(new StaticAnnotation("properties", JsonValue.EMPTY_JSON_ARRAY)),
                JsonSchemas.load(JsonValue.FALSE)
            ).categories(),
            containsInAnyOrder(Keyword.KeywordCategory.APPLICATOR, Keyword.KeywordCategory.ANNOTATION)
        );
    }

    @Test
    void should_return_propertynames_which_will_be_validated() {
        assertThat(
            new AdditionalPropertiesKeyword(
                List.of(new StaticAnnotation("properties", Json.createArrayBuilder().add("test").build())),
                JsonSchemas.load(JsonValue.TRUE)
            )
                .asAnnotation()
                .valueFor(Json.createObjectBuilder().add("test", 1).add("foo", 1).build())
                .asJsonArray(),
            containsInAnyOrder(Json.createValue("foo"))
        );
    }

    @Test
    void should_be_printable() {
        assertThat(
            new AdditionalPropertiesKeyword(List.of(), JsonSchemas.load(JsonValue.EMPTY_JSON_OBJECT)).printOn(
                new HashMapMedia()
            ),
            (Matcher) hasEntry(is("additionalProperties"), anEmptyMap())
        );
    }
}
