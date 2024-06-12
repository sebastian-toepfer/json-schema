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
package io.github.sebastiantoepfer.jsonschema.core.keyword.type;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import io.github.sebastiantoepfer.jsonschema.JsonSchemas;
import jakarta.json.Json;
import jakarta.json.JsonValue;
import org.junit.jupiter.api.Test;

class AffectsTest {

    @Test
    void should_handle_non_annotation_as_absence() {
        assertThat(
            new Affects("type", new Affects.ProvideDefaultValue(JsonValue.EMPTY_JSON_OBJECT))
                .findAffectsKeywordIn(JsonSchemas.load(Json.createObjectBuilder().add("type", "number").build()))
                .getKey()
                .valueFor(JsonValue.FALSE),
            is(JsonValue.EMPTY_JSON_OBJECT)
        );
    }

    @Test
    void should_return_original_annotation() {
        assertThat(
            new Affects("properties", new Affects.ProvideDefaultValue(JsonValue.EMPTY_JSON_OBJECT))
                .findAffectsKeywordIn(
                    JsonSchemas.load(
                        Json.createObjectBuilder()
                            .add(
                                "properties",
                                Json.createObjectBuilder().add("test", Json.createObjectBuilder().add("type", "number"))
                            )
                            .build()
                    )
                )
                .getKey()
                .valueFor(Json.createObjectBuilder().add("test", 1L).build()),
            is(Json.createArrayBuilder().add("test").build())
        );
    }

    @Test
    void should_create_original_annotation() {
        assertThat(
            new Affects("properties", new Affects.ProvideDefaultValue(JsonValue.EMPTY_JSON_OBJECT))
                .findAffectsKeywordIn(
                    JsonSchemas.load(
                        Json.createObjectBuilder()
                            .add(
                                "properties",
                                Json.createObjectBuilder().add("test", Json.createObjectBuilder().add("type", "number"))
                            )
                            .build()
                    )
                )
                .getValue()
                .apply(new MockKeyword("test"))
                .hasName("test"),
            is(true)
        );
    }

    @Test
    void should_create_original_annotation_also_for_missing() {
        assertThat(
            new Affects("properties", new Affects.ProvideDefaultValue(JsonValue.EMPTY_JSON_OBJECT))
                .findAffectsKeywordIn(JsonSchemas.load(JsonValue.TRUE))
                .getValue()
                .apply(new MockKeyword("test"))
                .hasName("test"),
            is(true)
        );
    }
}
