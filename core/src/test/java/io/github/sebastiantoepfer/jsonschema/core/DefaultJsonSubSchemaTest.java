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
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;

import io.github.sebastiantoepfer.ddd.media.core.HashMapMedia;
import io.github.sebastiantoepfer.jsonschema.JsonSchema;
import jakarta.json.Json;
import jakarta.json.JsonValue;
import org.junit.jupiter.api.Test;

class DefaultJsonSubSchemaTest {

    @Test
    void should_return_owner() {
        final JsonSchema owner = new DefaultJsonSchemaFactory()
            .create(Json.createObjectBuilder().add("test", JsonValue.TRUE).build());
        assertThat(
            new DefaultJsonSubSchema(owner, new DefaultJsonSchemaFactory().create(JsonValue.TRUE)).owner(),
            is(owner)
        );
    }

    @Test
    void should_return_subschema() {
        final JsonSchema owner = new DefaultJsonSchemaFactory()
            .create(
                Json.createObjectBuilder().add("test", Json.createObjectBuilder().add("sub", JsonValue.TRUE)).build()
            );
        assertThat(
            new DefaultJsonSubSchema(
                owner,
                new DefaultJsonSchemaFactory().create(Json.createObjectBuilder().add("sub", JsonValue.TRUE).build())
            ).asSubSchema("sub"),
            isPresent()
        );
    }

    @Test
    void should_convertable_to_plain_jsonobject() {
        final JsonSchema owner = new DefaultJsonSchemaFactory()
            .create(
                Json.createObjectBuilder().add("test", Json.createObjectBuilder().add("sub", JsonValue.TRUE)).build()
            );
        assertThat(
            new DefaultJsonSubSchema(
                owner,
                new DefaultJsonSchemaFactory().create(Json.createObjectBuilder().add("sub", JsonValue.TRUE).build())
            ).asJsonObject(),
            is(Json.createObjectBuilder().add("sub", JsonValue.TRUE).build())
        );
    }

    @Test
    void should_return_empty_for_unknown_keyword() {
        assertThat(
            new DefaultJsonSubSchema(
                new DefaultJsonSchemaFactory().create(Json.createObjectBuilder().add("test", JsonValue.TRUE).build()),
                new DefaultJsonSchemaFactory().create(Json.createObjectBuilder().add("type", "array").build())
            ).keywordByName("test"),
            isEmpty()
        );
    }

    @Test
    void should_return_known_keyword() {
        assertThat(
            new DefaultJsonSubSchema(
                new DefaultJsonSchemaFactory().create(Json.createObjectBuilder().add("test", JsonValue.TRUE).build()),
                new DefaultJsonSchemaFactory().create(Json.createObjectBuilder().add("type", "array").build())
            ).keywordByName("type"),
            isPresent()
        );
    }

    @Test
    void should_be_printable() {
        assertThat(
            new DefaultJsonSubSchema(
                new DefaultJsonSchemaFactory().create(Json.createObjectBuilder().add("test", JsonValue.TRUE).build()),
                new DefaultJsonSchemaFactory().create(Json.createObjectBuilder().add("type", "array").build())
            ).printOn(new HashMapMedia()),
            hasEntry("type", "array")
        );
    }
}
