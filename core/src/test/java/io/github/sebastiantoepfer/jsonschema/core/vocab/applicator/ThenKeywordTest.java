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
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;

import io.github.sebastiantoepfer.ddd.media.core.HashMapMedia;
import io.github.sebastiantoepfer.jsonschema.JsonSchemas;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import jakarta.json.Json;
import jakarta.json.JsonValue;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

class ThenKeywordTest {

    @Test
    void should_know_his_name() {
        final Keyword keyword = new ThenKeyword(JsonSchemas.load(JsonValue.TRUE));

        assertThat(keyword.hasName("then"), is(true));
        assertThat(keyword.hasName("test"), is(false));
    }

    @Test
    void should_be_printable() {
        assertThat(
            new ThenKeyword(
                JsonSchemas.load(
                    Json.createObjectBuilder()
                        .add(
                            "properties",
                            Json.createObjectBuilder().add("foo", Json.createObjectBuilder().add("type", "string"))
                        )
                        .add("required", Json.createArrayBuilder().add("foo"))
                        .build()
                )
            ).printOn(new HashMapMedia()),
            (Matcher) hasEntry(is("then"), hasKey("properties"))
        );
    }

    @Test
    void should_be_valid_if_instance_successful_validates_against_schema() {
        assertThat(
            new ThenKeyword(JsonSchemas.load(JsonValue.TRUE)).asApplicator().applyTo(JsonValue.EMPTY_JSON_OBJECT),
            is(true)
        );
    }

    @Test
    void should_be_invalid_if_instance_not_successful_validates_against_schema() {
        assertThat(
            new ThenKeyword(JsonSchemas.load(JsonValue.FALSE)).asApplicator().applyTo(JsonValue.EMPTY_JSON_OBJECT),
            is(false)
        );
    }
}
