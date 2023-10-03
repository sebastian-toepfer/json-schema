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
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.sebastiantoepfer.jsonschema.JsonSchema;
import io.github.sebastiantoepfer.jsonschema.core.DefaultJsonSchemaFactory;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import jakarta.json.Json;
import jakarta.json.JsonValue;
import org.junit.jupiter.api.Test;

class RefKeywordTypeTest {

    @Test
    void should_be_not_createbale_from_non_string() {
        final RefKeywordType keywordType = new RefKeywordType();
        final JsonSchema schema = new DefaultJsonSchemaFactory().create(JsonValue.TRUE);
        assertThrows(IllegalArgumentException.class, () -> keywordType.createKeyword(schema, JsonValue.TRUE));
    }

    @Test
    void should_know_his_name() {
        final Keyword ref = new RefKeywordType()
            .createKeyword(new DefaultJsonSchemaFactory().create(JsonValue.TRUE), Json.createValue("#"));

        assertThat(ref.hasName("$ref"), is(true));
        assertThat(ref.hasName("test"), is(false));
    }

    @Test
    void should_use_local_referenced_schema_for_validation() {
        final Keyword keyword = new RefKeywordType()
            .createKeyword(
                new DefaultJsonSchemaFactory()
                    .create(
                        Json
                            .createObjectBuilder()
                            .add(
                                "$defs",
                                Json
                                    .createObjectBuilder()
                                    .add("positiveInteger", Json.createObjectBuilder().add("type", "integer"))
                            )
                            .build()
                    ),
                Json.createValue("#/$defs/positiveInteger")
            );

        assertThat(keyword.asApplicator().applyTo(Json.createValue(1L)), is(true));
        assertThat(keyword.asApplicator().applyTo(Json.createValue("invalid")), is(false));
    }

    @Test
    void should_use_remote_referenced_schema_for_validation() {
        final Keyword keyword = new RefKeywordType()
            .createKeyword(
                new DefaultJsonSchemaFactory()
                    .create(
                        Json
                            .createObjectBuilder()
                            .add(
                                "$defs",
                                Json
                                    .createObjectBuilder()
                                    .add("positiveInteger", Json.createObjectBuilder().add("type", "integer"))
                            )
                            .build()
                    ),
                Json.createValue("#/$defs/positiveInteger")
            );

        assertThat(keyword.asApplicator().applyTo(Json.createValue(1L)), is(true));
    }
}
