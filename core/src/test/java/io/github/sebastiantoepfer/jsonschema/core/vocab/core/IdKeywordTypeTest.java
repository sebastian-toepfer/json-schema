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
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import jakarta.json.Json;
import jakarta.json.JsonValue;
import java.net.URI;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

class IdKeywordTypeTest {

    @Test
    void should_not_be_creatable_from_non_string() {
        final IdKeywordType keyword = new IdKeywordType();
        final JsonSchema schema = new DefaultJsonSchemaFactory()
            .create(Json.createObjectBuilder().add("$id", JsonValue.FALSE).build());

        assertThrows(IllegalArgumentException.class, () -> keyword.createKeyword(schema));
    }

    @Test
    void should_know_his_name() {
        final Keyword id = new IdKeywordType()
            .createKeyword(
                new DefaultJsonSchemaFactory()
                    .create(Json.createObjectBuilder().add("$id", Json.createValue("/test")).build())
            );

        assertThat(id.hasName("$id"), is(true));
        assertThat(id.hasName("test"), is(false));
    }

    @Test
    void should_retun_his_uri() {
        assertThat(
            new IdKeywordType()
                .createKeyword(
                    new DefaultJsonSchemaFactory()
                        .create(
                            Json
                                .createObjectBuilder()
                                .add("$id", Json.createValue("https://json-schema.org/draft/2020-12/schema"))
                                .build()
                        )
                )
                .asIdentifier()
                .asUri(),
            is(URI.create("https://json-schema.org/draft/2020-12/schema"))
        );
    }

    @Test
    void should_be_printable() {
        assertThat(
            new IdKeywordType()
                .createKeyword(
                    new DefaultJsonSchemaFactory()
                        .create(
                            Json
                                .createObjectBuilder()
                                .add("$id", Json.createValue("https://json-schema.org/draft/2020-12/schema"))
                                .build()
                        )
                )
                .printOn(new HashMapMedia()),
            (Matcher) hasEntry(is("$id"), is("https://json-schema.org/draft/2020-12/schema"))
        );
    }
}
