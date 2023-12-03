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
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

import io.github.sebastiantoepfer.jsonschema.core.DefaultJsonSchemaFactory;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import io.github.sebastiantoepfer.jsonschema.vocabulary.spi.VocabularyDefinition;
import io.github.sebastiantoepfer.jsonschema.vocabulary.spi.VocabularyDefinitions;
import jakarta.json.Json;
import jakarta.json.JsonValue;
import java.net.URI;
import org.junit.jupiter.api.Test;

class VocabularyKeywordTypeTest {

    @Test
    void should_created_keyword_should_know_his_name() {
        final Keyword vocabulary = new VocabularyKeywordType()
            .createKeyword(
                new DefaultJsonSchemaFactory()
                    .create(Json.createObjectBuilder().add("$vocabulary", JsonValue.EMPTY_JSON_OBJECT).build())
            );

        assertThat(vocabulary.hasName("$vocabulary"), is(true));
        assertThat(vocabulary.hasName("$id"), is(false));
    }

    @Test
    void should_create_definitions() {
        assertThat(
            ((VocabularyDefinitions) new VocabularyKeywordType()
                    .createKeyword(
                        new DefaultJsonSchemaFactory()
                            .create(
                                Json
                                    .createObjectBuilder()
                                    .add(
                                        "$vocabulary",
                                        Json
                                            .createObjectBuilder()
                                            .add("https://json-schema.org/draft/2020-12/vocab/core", true)
                                            .add("http://openapi.org/test", false)
                                    )
                                    .build()
                            )
                    )).definitions()
                .toList(),
            containsInAnyOrder(
                new VocabularyDefinition(URI.create("https://json-schema.org/draft/2020-12/vocab/core"), true),
                new VocabularyDefinition(URI.create("http://openapi.org/test"), false)
            )
        );
    }
}
