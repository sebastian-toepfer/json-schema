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
import static org.hamcrest.Matchers.is;

import io.github.sebastiantoepfer.ddd.media.core.HashMapMedia;
import io.github.sebastiantoepfer.jsonschema.core.DefaultJsonSchemaFactory;
import io.github.sebastiantoepfer.jsonschema.core.keywordtype.ObjectSubSchemaKeywordType;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import java.util.Map;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

class PropertiesKeywordTest {

    @Test
    void should_be_know_his_name() {
        final Keyword keyword = createKeywordFrom(
            Json.createObjectBuilder().add("properties", JsonValue.EMPTY_JSON_OBJECT).build()
        );

        assertThat(keyword.hasName("properties"), is(true));
        assertThat(keyword.hasName("test"), is(false));
    }

    @Test
    void should_be_an_applicator_and_annotation() {
        assertThat(
            createKeywordFrom(Json.createObjectBuilder().add("properties", JsonValue.EMPTY_JSON_OBJECT).build())
                .categories(),
            containsInAnyOrder(Keyword.KeywordCategory.APPLICATOR, Keyword.KeywordCategory.ANNOTATION)
        );
    }

    @Test
    void should_be_valid_for_non_objects() {
        assertThat(
            createKeywordFrom(Json.createObjectBuilder().add("properties", JsonValue.EMPTY_JSON_OBJECT).build())
                .asApplicator()
                .applyTo(JsonValue.EMPTY_JSON_ARRAY),
            is(true)
        );
    }

    @Test
    void should_be_valid_if_properties_applies_to_his_schema() {
        assertThat(
            createKeywordFrom(
                Json
                    .createObjectBuilder()
                    .add("properties", Json.createObjectBuilder().add("test", JsonValue.TRUE))
                    .build()
            )
                .asApplicator()
                .applyTo(Json.createObjectBuilder().add("test", 1).build()),
            is(true)
        );
    }

    @Test
    void should_be_invalid_if_properties_not_applies_to_his_schema() {
        assertThat(
            createKeywordFrom(
                Json
                    .createObjectBuilder()
                    .add("properties", Json.createObjectBuilder().add("test", JsonValue.FALSE))
                    .build()
            )
                .asApplicator()
                .applyTo(Json.createObjectBuilder().add("test", 1).build()),
            is(false)
        );
    }

    @Test
    void should_be_valid_for_empty_objects() {
        assertThat(
            createKeywordFrom(
                Json
                    .createObjectBuilder()
                    .add("properties", Json.createObjectBuilder().add("test", JsonValue.FALSE))
                    .build()
            )
                .asApplicator()
                .applyTo(JsonValue.EMPTY_JSON_OBJECT),
            is(true)
        );
    }

    @Test
    void should_return_all_matched_propertynames() {
        assertThat(
            createKeywordFrom(
                Json
                    .createObjectBuilder()
                    .add("properties", Json.createObjectBuilder().add("test", true).add("foo", true))
                    .build()
            )
                .asAnnotation()
                .valueFor(Json.createObjectBuilder().add("foo", 1).build())
                .asJsonArray()
                .stream()
                .toList(),
            contains(Json.createValue("foo"))
        );
    }

    @Test
    void should_be_printable() {
        assertThat(
            ((Map) createKeywordFrom(
                    Json
                        .createObjectBuilder()
                        .add("properties", Json.createObjectBuilder().add("test", JsonValue.TRUE))
                        .build()
                )
                    .printOn(new HashMapMedia())
                    .get("properties")).get("test"),
            (Matcher) anEmptyMap()
        );
    }

    private static Keyword createKeywordFrom(final JsonObject json) {
        return new ObjectSubSchemaKeywordType("properties", PropertiesKeyword::new)
            .createKeyword(new DefaultJsonSchemaFactory().create(json));
    }
}
