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
import static org.hamcrest.Matchers.anEmptyMap;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;

import io.github.sebastiantoepfer.ddd.media.core.HashMapMedia;
import io.github.sebastiantoepfer.jsonschema.core.DefaultJsonSchemaFactory;
import io.github.sebastiantoepfer.jsonschema.core.keyword.type.NamedJsonSchemaKeywordType;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

class DefsKeywordTest {

    @Test
    void should_know_his_name() {
        final Keyword defs = createKeywordFrom(
            Json.createObjectBuilder().add("$defs", JsonValue.EMPTY_JSON_OBJECT).build()
        );

        assertThat(defs.hasName("$defs"), is(true));
        assertThat(defs.hasName("test"), is(false));
    }

    @Test
    void should_be_printable() {
        assertThat(
            createKeywordFrom(Json.createObjectBuilder().add("$defs", JsonValue.EMPTY_JSON_OBJECT).build()).printOn(
                new HashMapMedia()
            ),
            (Matcher) hasEntry(is("$defs"), anEmptyMap())
        );
    }

    private static Keyword createKeywordFrom(final JsonObject json) {
        return new NamedJsonSchemaKeywordType(DefsKeyword.NAME, DefsKeyword::new).createKeyword(
            new DefaultJsonSchemaFactory().create(json)
        );
    }
}
