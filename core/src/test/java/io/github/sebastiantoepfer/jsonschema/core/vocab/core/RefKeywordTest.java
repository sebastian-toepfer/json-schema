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

import io.github.sebastiantoepfer.ddd.media.core.HashMapMedia;
import io.github.sebastiantoepfer.jsonschema.JsonSchema;
import io.github.sebastiantoepfer.jsonschema.JsonSchemas;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import jakarta.json.Json;
import java.io.IOException;
import java.net.URI;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

class RefKeywordTest {

    @Test
    void should_know_his_name() {
        final Keyword ref = new RefKeyword(
            JsonSchemas.load(Json.createObjectBuilder().add("$ref", Json.createValue("#")).build()),
            URI.create("#"),
            new SchemaRegistry.DefaultSchemaRegistry()
        );
        assertThat(ref.hasName("$ref"), is(true));
        assertThat(ref.hasName("test"), is(false));
    }

    @Test
    void should_use_local_referenced_schema_for_validation() {
        final Keyword keyword = new RefKeyword(
            JsonSchemas.load(
                Json.createObjectBuilder()
                    .add(
                        "$defs",
                        Json.createObjectBuilder()
                            .add("positiveInteger", Json.createObjectBuilder().add("type", "integer"))
                    )
                    .add("$ref", Json.createValue("#/$defs/positiveInteger"))
                    .build()
            ),
            URI.create("#/$defs/positiveInteger"),
            new SchemaRegistry.DefaultSchemaRegistry()
        );

        assertThat(keyword.asApplicator().applyTo(Json.createValue(1L)), is(true));
        assertThat(keyword.asApplicator().applyTo(Json.createValue("invalid")), is(false));
    }

    @Test
    void should_use_remote_referenced_schema_for_validation() {
        final Keyword keyword = new RefKeyword(
            JsonSchemas.load(
                Json.createObjectBuilder().add("$ref", Json.createValue("https://schema.org/byMonth")).build()
            ),
            URI.create("https://schema.org/byMonth"),
            new SchemaRegistry() {
                @Override
                public JsonSchema schemaForUrl(final URI uri) throws IOException {
                    if (uri.equals(URI.create("https://schema.org/byMonth"))) {
                        //hen egg issue -> we need more than core :(
                        return JsonSchemas.load(
                            Json.createObjectBuilder()
                                .add("type", "integer")
                                .add("minimum", 1)
                                .add("maximum", 12)
                                .build()
                        );
                    } else {
                        throw new IOException("can not retrieve remote schema!");
                    }
                }
            }
        );
        assertThat(keyword.asApplicator().applyTo(Json.createValue(1L)), is(true));
        assertThat(keyword.asApplicator().applyTo(Json.createValue(12L)), is(true));
        assertThat(keyword.asApplicator().applyTo(Json.createValue(0L)), is(false));
        assertThat(keyword.asApplicator().applyTo(Json.createValue(13L)), is(false));
    }

    @Test
    void should_be_printable() {
        assertThat(
            new RefKeyword(
                JsonSchemas.load(Json.createObjectBuilder().add("$ref", Json.createValue("#")).build()),
                URI.create("#"),
                new SchemaRegistry.DefaultSchemaRegistry()
            ).printOn(new HashMapMedia()),
            (Matcher) hasEntry(is("$ref"), is("#"))
        );
    }
}
