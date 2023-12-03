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
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.sebastiantoepfer.jsonschema.JsonSchema;
import io.github.sebastiantoepfer.jsonschema.core.DefaultJsonSchemaFactory;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import io.github.sebastiantoepfer.jsonschema.keyword.KeywordType;
import jakarta.json.Json;
import jakarta.json.JsonValue;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class PropertiesKeywordTypeTest {

    @Test
    void should_not_be_createable_with_array_in_schemas() {
        final JsonSchema schema = new DefaultJsonSchemaFactory()
            .create(
                Json
                    .createObjectBuilder()
                    .add(
                        "properties",
                        Json
                            .createObjectBuilder()
                            .add("test", JsonValue.TRUE)
                            .add("invalid", JsonValue.EMPTY_JSON_ARRAY)
                    )
                    .build()
            );
        final KeywordType keywordType = new PropertiesKeywordType();
        assertThrows(IllegalArgumentException.class, () -> keywordType.createKeyword(schema));
    }

    @Test
    void should_not_be_createable_with_string_in_schemas() {
        final JsonSchema schema = new DefaultJsonSchemaFactory()
            .create(
                Json
                    .createObjectBuilder()
                    .add(
                        "properties",
                        Json.createObjectBuilder().add("test", JsonValue.TRUE).add("invalid", Json.createValue("value"))
                    )
                    .build()
            );
        final KeywordType keywordType = new PropertiesKeywordType();
        assertThrows(IllegalArgumentException.class, () -> keywordType.createKeyword(schema));
    }

    @Test
    void should_not_be_createable_with_number_in_schemas() {
        final JsonSchema schema = new DefaultJsonSchemaFactory()
            .create(
                Json
                    .createObjectBuilder()
                    .add(
                        "properties",
                        Json.createObjectBuilder().add("test", JsonValue.TRUE).add("invalid", Json.createValue(3.14))
                    )
                    .build()
            );
        final KeywordType keywordType = new PropertiesKeywordType();
        assertThrows(IllegalArgumentException.class, () -> keywordType.createKeyword(schema));
    }

    @Test
    void should_be_know_his_name() {
        final Keyword keyword = new PropertiesKeywordType()
            .createKeyword(
                new DefaultJsonSchemaFactory()
                    .create(Json.createObjectBuilder().add("properties", JsonValue.EMPTY_JSON_OBJECT).build())
            );

        assertThat(keyword.hasName("properties"), is(true));
        assertThat(keyword.hasName("test"), is(false));
    }

    @Test
    void should_be_an_applicator_and_annotation() {
        assertThat(
            new PropertiesKeywordType()
                .createKeyword(
                    new DefaultJsonSchemaFactory()
                        .create(Json.createObjectBuilder().add("properties", JsonValue.EMPTY_JSON_OBJECT).build())
                )
                .categories(),
            Matchers.containsInAnyOrder(Keyword.KeywordCategory.APPLICATOR, Keyword.KeywordCategory.ANNOTATION)
        );
    }

    @Test
    void should_be_valid_for_non_objects() {
        assertThat(
            new PropertiesKeywordType()
                .createKeyword(
                    new DefaultJsonSchemaFactory()
                        .create(Json.createObjectBuilder().add("properties", JsonValue.EMPTY_JSON_OBJECT).build())
                )
                .asApplicator()
                .applyTo(JsonValue.EMPTY_JSON_ARRAY),
            is(true)
        );
    }

    @Test
    void should_be_valid_if_properties_applies_to_his_schema() {
        assertThat(
            new PropertiesKeywordType()
                .createKeyword(
                    new DefaultJsonSchemaFactory()
                        .create(
                            Json
                                .createObjectBuilder()
                                .add("properties", Json.createObjectBuilder().add("test", JsonValue.TRUE))
                                .build()
                        )
                )
                .asApplicator()
                .applyTo(Json.createObjectBuilder().add("test", 1).build()),
            is(true)
        );
    }

    @Test
    void should_be_invalid_if_properties_not_applies_to_his_schema() {
        assertThat(
            new PropertiesKeywordType()
                .createKeyword(
                    new DefaultJsonSchemaFactory()
                        .create(
                            Json
                                .createObjectBuilder()
                                .add("properties", Json.createObjectBuilder().add("test", JsonValue.FALSE))
                                .build()
                        )
                )
                .asApplicator()
                .applyTo(Json.createObjectBuilder().add("test", 1).build()),
            is(false)
        );
    }

    @Test
    void should_be_valid_for_empty_objects() {
        assertThat(
            new PropertiesKeywordType()
                .createKeyword(
                    new DefaultJsonSchemaFactory()
                        .create(
                            Json
                                .createObjectBuilder()
                                .add("properties", Json.createObjectBuilder().add("test", JsonValue.FALSE))
                                .build()
                        )
                )
                .asApplicator()
                .applyTo(JsonValue.EMPTY_JSON_OBJECT),
            is(true)
        );
    }

    @Test
    void should_return_all_matched_propertynames() {
        assertThat(
            new PropertiesKeywordType()
                .createKeyword(
                    new DefaultJsonSchemaFactory()
                        .create(
                            Json
                                .createObjectBuilder()
                                .add("properties", Json.createObjectBuilder().add("test", true).add("foo", true))
                                .build()
                        )
                )
                .asAnnotation()
                .valueFor(Json.createObjectBuilder().add("foo", 1).build())
                .asJsonArray()
                .stream()
                .toList(),
            contains(Json.createValue("foo"))
        );
    }
}
