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
import io.github.sebastiantoepfer.jsonschema.core.DefaultJsonSchemaFactory;
import io.github.sebastiantoepfer.jsonschema.core.keyword.type.Affects;
import io.github.sebastiantoepfer.jsonschema.core.keyword.type.AffectsKeywordType;
import io.github.sebastiantoepfer.jsonschema.core.keyword.type.SubSchemaKeywordType;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import jakarta.json.Json;
import jakarta.json.JsonObject;
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
            createKeywordFrom(
                Json.createObjectBuilder()
                    .add("properties", Json.createObjectBuilder().add("test", JsonValue.TRUE))
                    .add("additionalProperties", JsonValue.FALSE)
                    .build()
            )
                .asApplicator()
                .applyTo(JsonValue.EMPTY_JSON_ARRAY),
            is(true)
        );
    }

    @Test
    void should_not_valid_if_no_additionals_are_allow() {
        assertThat(
            createKeywordFrom(
                Json.createObjectBuilder()
                    .add("properties", Json.createObjectBuilder().add("test", JsonValue.TRUE))
                    .add("additionalProperties", JsonValue.FALSE)
                    .build()
            )
                .asApplicator()
                .applyTo(Json.createObjectBuilder().add("test", 1).add("foo", 1).build()),
            is(false)
        );
    }

    @Test
    void should_valid_if_additionals_are_allow() {
        assertThat(
            createKeywordFrom(
                Json.createObjectBuilder()
                    .add("properties", Json.createObjectBuilder().add("test", JsonValue.TRUE))
                    .add("additionalProperties", JsonValue.TRUE)
                    .build()
            )
                .asApplicator()
                .applyTo(Json.createObjectBuilder().add("test", 1).add("foo", 1).build()),
            is(true)
        );
    }

    @Test
    void should_valid_if_no_additionals_are_allow_and_no_additionals_their() {
        assertThat(
            createKeywordFrom(
                Json.createObjectBuilder()
                    .add("properties", Json.createObjectBuilder().add("test", JsonValue.TRUE))
                    .add("additionalProperties", JsonValue.FALSE)
                    .build()
            )
                .asApplicator()
                .applyTo(Json.createObjectBuilder().add("test", 1).build()),
            is(true)
        );
    }

    @Test
    void should_be_an_applicator_and_an_annotation() {
        assertThat(
            createKeywordFrom(
                Json.createObjectBuilder().add("additionalProperties", JsonValue.TRUE).build()
            ).categories(),
            containsInAnyOrder(Keyword.KeywordCategory.APPLICATOR, Keyword.KeywordCategory.ANNOTATION)
        );
    }

    @Test
    void should_return_propertynames_which_will_be_validated() {
        assertThat(
            createKeywordFrom(
                Json.createObjectBuilder()
                    .add("properties", Json.createObjectBuilder().add("test", JsonValue.TRUE))
                    .add("additionalProperties", JsonValue.TRUE)
                    .build()
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
            createKeywordFrom(
                Json.createObjectBuilder().add("additionalProperties", JsonValue.EMPTY_JSON_OBJECT).build()
            ).printOn(new HashMapMedia()),
            (Matcher) hasEntry(is("additionalProperties"), anEmptyMap())
        );
    }

    private static Keyword createKeywordFrom(final JsonObject json) {
        return new AffectsKeywordType(
            "additionalProperties",
            List.of(
                new Affects("properties", JsonValue.EMPTY_JSON_ARRAY),
                new Affects("patternProperties", JsonValue.EMPTY_JSON_ARRAY)
            ),
            (affects, schema) ->
                new SubSchemaKeywordType(
                    "additionalProperties",
                    s -> new AdditionalPropertiesKeyword(affects, s)
                ).createKeyword(schema)
        ).createKeyword(new DefaultJsonSchemaFactory().create(json));
    }
}
