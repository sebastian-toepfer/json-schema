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
package io.github.sebastiantoepfer.jsonschema.core;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isEmpty;
import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.sebastiantoepfer.ddd.media.core.HashMapMedia;
import jakarta.json.Json;
import jakarta.json.JsonValue;
import org.junit.jupiter.api.Test;

class DefaultJsonObjectSchemaTest {

    private final DefaultJsonObjectSchema schema = new DefaultJsonObjectSchema(
        Json
            .createObjectBuilder()
            .add("type", "array")
            .add("items", Json.createObjectBuilder().add("type", "integer"))
            .build()
    );

    @Test
    void should_be_valid_an_empty_array() {
        assertThat(schema.validator().isValid(JsonValue.EMPTY_JSON_ARRAY), is(true));
    }

    @Test
    void should_be_valid_an_integer_array() {
        assertThat(schema.validator().isValid(Json.createArrayBuilder().add(1).add(2).build()), is(true));
    }

    @Test
    void should_be_invalid_an_string_array() {
        assertThat(schema.validator().isValid(Json.createArrayBuilder().add("a").add("b").build()), is(not(true)));
    }

    @Test
    void should_be_invalid_for_object() {
        assertThat(schema.validator().isValid(JsonValue.EMPTY_JSON_OBJECT), is(not(true)));
    }

    @Test
    void should_not_be_usable_without_mandantory_core_vocabulary() {
        final DefaultJsonObjectSchema invalidSchema = new DefaultJsonObjectSchema(
            Json
                .createObjectBuilder()
                .add(
                    "$vocabulary",
                    Json.createObjectBuilder().add("https://json-schema.org/draft/2020-12/vocab/core", false)
                )
                .build()
        );

        assertThrows(IllegalArgumentException.class, () -> invalidSchema.keywordByName("$vocabulary"));
    }

    @Test
    void should_find_keyword_by_name() {
        assertThat(
            new DefaultJsonObjectSchema(
                Json
                    .createObjectBuilder()
                    .add(
                        "$vocabulary",
                        Json.createObjectBuilder().add("https://json-schema.org/draft/2020-12/vocab/core", true)
                    )
                    .build()
            )
                .keywordByName("$vocabulary"),
            isPresent()
        );
    }

    @Test
    void should_return_empty_for_non_existing_keyword() {
        assertThat(
            new DefaultJsonObjectSchema(
                Json
                    .createObjectBuilder()
                    .add(
                        "$vocabulary",
                        Json.createObjectBuilder().add("https://json-schema.org/draft/2020-12/vocab/core", true)
                    )
                    .build()
            )
                .keywordByName("type"),
            isEmpty()
        );
    }

    @Test
    void should_return_empty_if_non_subschema_exists_under_the_given_name() {
        assertThat(
            new DefaultJsonObjectSchema(Json.createObjectBuilder().add("test", "hallo").build())
                .asSubSchema("properties"),
            isEmpty()
        );
    }

    @Test
    void should_return_subschema_if_subschema_exists_under_the_given_name() {
        assertThat(
            new DefaultJsonObjectSchema(Json.createObjectBuilder().add("test", JsonValue.FALSE).build())
                .asSubSchema("test"),
            isPresent()
        );
    }

    @Test
    void should_return_empty_if_given_name_not_resolve_to_a_valid_schematype() {
        assertThat(
            new DefaultJsonObjectSchema(Json.createObjectBuilder().add("test", JsonValue.EMPTY_JSON_ARRAY).build())
                .asSubSchema("test"),
            isEmpty()
        );
    }

    @Test
    void should_be_printable() {
        assertThat(schema.printOn(new HashMapMedia()), allOf(hasEntry("type", "array"), hasKey("items")));
    }
}
