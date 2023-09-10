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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DefaultJsonSchemaTest {

    private final DefaultJsonSchema schema = new DefaultJsonSchema(
        Json.createObjectBuilder().add("type", "string").build()
    );

    @Test
    void should_be_valid_for_string() {
        assertThat(schema.validator().validate(Json.createValue("test")), is(empty()));
    }

    @Test
    void should_be_invalid_for_object() {
        assertThat(schema.validator().validate(JsonValue.EMPTY_JSON_OBJECT), is(not(empty())));
    }

    @Test
    void should_not_be_loadable_without_mandantory_core_vocabulary() {
        final JsonObject invalidSchema = Json
            .createObjectBuilder()
            .add(
                "$vocabulary",
                Json.createObjectBuilder().add("https://json-schema.org/draft/2020-12/vocab/core", false)
            )
            .build();

        Assertions.assertThrows(Exception.class, () -> new DefaultJsonSchema(invalidSchema));
    }
}
