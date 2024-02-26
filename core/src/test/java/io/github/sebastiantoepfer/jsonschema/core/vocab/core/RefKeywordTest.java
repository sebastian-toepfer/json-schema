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
package io.github.sebastiantoepfer.jsonschema.core.vocab.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.sebastiantoepfer.ddd.media.core.HashMapMedia;
import io.github.sebastiantoepfer.jsonschema.JsonSchema;
import io.github.sebastiantoepfer.jsonschema.core.DefaultJsonSchemaFactory;
import io.github.sebastiantoepfer.jsonschema.core.keyword.type.StringKeywordType;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import java.net.URI;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

class RefKeywordTest {

    @Test
    void should_be_not_createable_from_non_string() {
        final RefKeywordType keywordType = new RefKeywordType(JsonProvider.provider());
        final JsonSchema schema = new DefaultJsonSchemaFactory()
            .create(Json.createObjectBuilder().add("$ref", JsonValue.TRUE).build());
        assertThrows(IllegalArgumentException.class, () -> keywordType.createKeyword(schema));
    }

    @Test
    void should_know_his_name() {
        final Keyword ref = new RefKeywordType(JsonProvider.provider()).createKeyword(
            new DefaultJsonSchemaFactory().create(Json.createObjectBuilder().add("$ref", Json.createValue("#")).build())
        );

        assertThat(ref.hasName("$ref"), is(true));
        assertThat(ref.hasName("test"), is(false));
    }

    @Test
    void should_use_local_referenced_schema_for_validation() {
        final Keyword keyword = new RefKeywordType(JsonProvider.provider()).createKeyword(
            new DefaultJsonSchemaFactory()
                .create(
                    Json.createObjectBuilder()
                        .add(
                            "$defs",
                            Json.createObjectBuilder()
                                .add("positiveInteger", Json.createObjectBuilder().add("type", "integer"))
                        )
                        .add("$ref", Json.createValue("#/$defs/positiveInteger"))
                        .build()
                )
        );

        assertThat(keyword.asApplicator().applyTo(Json.createValue(1L)), is(true));
        assertThat(keyword.asApplicator().applyTo(Json.createValue("invalid")), is(false));
    }

    @Test
    void should_use_remote_referenced_schema_for_validation() {
        final Keyword keyword = new RefKeywordType(JsonProvider.provider()).createKeyword(
            new DefaultJsonSchemaFactory()
                .create(
                    Json.createObjectBuilder()
                        .add(
                            "$defs",
                            Json.createObjectBuilder()
                                .add("positiveInteger", Json.createObjectBuilder().add("type", "integer"))
                        )
                        .add("$ref", Json.createValue("#/$defs/positiveInteger"))
                        .build()
                )
        );

        assertThat(keyword.asApplicator().applyTo(Json.createValue(1L)), is(true));
    }

    @Test
    void should_be_printable() {
        assertThat(
            new RefKeywordType(JsonProvider.provider())
                .createKeyword(
                    new DefaultJsonSchemaFactory()
                        .create(Json.createObjectBuilder().add("$ref", Json.createValue("#")).build())
                )
                .printOn(new HashMapMedia()),
            (Matcher) hasEntry(is("$ref"), is("#"))
        );
    }

    private static Keyword createKeywordFrom(final JsonObject json) {
        final JsonSchema schema = new DefaultJsonSchemaFactory().create(json);
        return new StringKeywordType(
            JsonProvider.provider(),
            "$ref",
            s -> new RefKeyword(schema, URI.create(s))
        ).createKeyword(schema);
    }
}
