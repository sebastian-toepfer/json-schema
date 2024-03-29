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

class DynamicRefKeywordTest {

    @Test
    void should_create_keyword_with_name() {
        assertThat(
            createKeywordFrom(Json.createObjectBuilder().add("$dynamicRef", "test").build()).hasName("$dynamicRef"),
            is(true)
        );
    }

    @Test
    void notFinischedYet() {
        final Keyword keyword = createKeywordFrom(Json.createObjectBuilder().add("$dynamicRef", "test").build());

        assertThat(keyword.hasName("$dynamicRef"), is(true));
        assertThat(keyword.hasName("$id"), is(false));

        assertThat(keyword.asApplicator().applyTo(JsonValue.TRUE), is(true));
    }

    @Test
    void should_be_printable() {
        assertThat(
            createKeywordFrom(Json.createObjectBuilder().add("$dynamicRef", "test").build()).printOn(
                new HashMapMedia()
            ),
            (Matcher) hasEntry(is("$dynamicRef"), is("test"))
        );
    }

    private static Keyword createKeywordFrom(final JsonObject json) {
        return new StringKeywordType(
            JsonProvider.provider(),
            DynamicRefKeyword.NAME,
            s -> new DynamicRefKeyword(URI.create(s))
        ).createKeyword(new DefaultJsonSchemaFactory().create(json));
    }
}
