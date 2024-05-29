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
package io.github.sebastiantoepfer.jsonschema.core.vocab.applicator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;

import io.github.sebastiantoepfer.ddd.media.core.HashMapMedia;
import io.github.sebastiantoepfer.jsonschema.core.DefaultJsonSchemaFactory;
import io.github.sebastiantoepfer.jsonschema.core.keyword.type.SchemaArrayKeywordType;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

class OneOfKeywordTest {

    @Test
    void should_know_his_name() {
        final Keyword items = createKeywordFrom(
            Json.createObjectBuilder().add("oneOf", Json.createArrayBuilder().add(JsonValue.TRUE)).build()
        );

        assertThat(items.hasName("oneOf"), is(true));
        assertThat(items.hasName("test"), is(false));
    }

    @Test
    void should_be_printable() {
        assertThat(
            createKeywordFrom(
                Json.createObjectBuilder()
                    .add(
                        "oneOf",
                        Json.createArrayBuilder()
                            .add(
                                Json.createObjectBuilder()
                                    .add(
                                        "properties",
                                        Json.createObjectBuilder()
                                            .add("foo", Json.createObjectBuilder().add("type", "string"))
                                    )
                                    .add("required", Json.createArrayBuilder().add("foo"))
                            )
                            .add(
                                Json.createObjectBuilder()
                                    .add(
                                        "properties",
                                        Json.createObjectBuilder()
                                            .add("bar", Json.createObjectBuilder().add("type", "number"))
                                    )
                                    .add("required", Json.createArrayBuilder().add("bar"))
                            )
                    )
                    .build()
            ).printOn(new HashMapMedia()),
            (Matcher) hasEntry(is("oneOf"), hasItem((hasKey("properties"))))
        );
    }

    @Test
    void should_be_valid_if_exactly_one_schema_applies() {
        assertThat(
            createKeywordFrom(
                Json.createObjectBuilder()
                    .add(
                        "oneOf",
                        Json.createArrayBuilder()
                            .add(
                                Json.createObjectBuilder()
                                    .add(
                                        "properties",
                                        Json.createObjectBuilder()
                                            .add("foo", Json.createObjectBuilder().add("type", "string"))
                                    )
                                    .add("required", Json.createArrayBuilder().add("foo"))
                            )
                            .add(
                                Json.createObjectBuilder()
                                    .add(
                                        "properties",
                                        Json.createObjectBuilder()
                                            .add("bar", Json.createObjectBuilder().add("type", "number"))
                                    )
                                    .add("required", Json.createArrayBuilder().add("bar"))
                            )
                    )
                    .build()
            )
                .asApplicator()
                .applyTo(Json.createObjectBuilder().add("foo", "foo").build()),
            is(true)
        );
    }

    @Test
    void should_be_invalid_if_none_schema_not_apply() {
        assertThat(
            createKeywordFrom(
                Json.createObjectBuilder()
                    .add(
                        "oneOf",
                        Json.createArrayBuilder()
                            .add(
                                Json.createObjectBuilder()
                                    .add(
                                        "properties",
                                        Json.createObjectBuilder()
                                            .add("foo", Json.createObjectBuilder().add("type", "string"))
                                    )
                                    .add("required", Json.createArrayBuilder().add("foo"))
                            )
                            .add(
                                Json.createObjectBuilder()
                                    .add(
                                        "properties",
                                        Json.createObjectBuilder()
                                            .add("bar", Json.createObjectBuilder().add("type", "number"))
                                    )
                                    .add("required", Json.createArrayBuilder().add("bar"))
                            )
                    )
                    .build()
            )
                .asApplicator()
                .applyTo(Json.createObjectBuilder().add("foo", 3).add("bar", "bar").build()),
            is(false)
        );
    }

    @Test
    void should_be_invalid_if_more_than_one_schema_apply() {
        assertThat(
            createKeywordFrom(
                Json.createObjectBuilder()
                    .add(
                        "oneOf",
                        Json.createArrayBuilder()
                            .add(
                                Json.createObjectBuilder()
                                    .add(
                                        "properties",
                                        Json.createObjectBuilder()
                                            .add("foo", Json.createObjectBuilder().add("type", "string"))
                                    )
                                    .add("required", Json.createArrayBuilder().add("foo"))
                            )
                            .add(
                                Json.createObjectBuilder()
                                    .add(
                                        "properties",
                                        Json.createObjectBuilder()
                                            .add("bar", Json.createObjectBuilder().add("type", "number"))
                                    )
                                    .add("required", Json.createArrayBuilder().add("bar"))
                            )
                    )
                    .build()
            )
                .asApplicator()
                .applyTo(Json.createObjectBuilder().add("foo", "foo").add("bar", 33).build()),
            is(false)
        );
    }

    private static Keyword createKeywordFrom(final JsonObject json) {
        return new SchemaArrayKeywordType("oneOf", OneOfKeyword::new).createKeyword(
            new DefaultJsonSchemaFactory().create(json)
        );
    }
}
