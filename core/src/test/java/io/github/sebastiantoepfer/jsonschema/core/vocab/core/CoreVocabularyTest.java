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

import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresentAnd;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;

import com.github.npathai.hamcrestopt.OptionalMatchers;
import io.github.sebastiantoepfer.jsonschema.JsonSchemas;
import io.github.sebastiantoepfer.jsonschema.keyword.KeywordType;
import jakarta.json.Json;
import jakarta.json.spi.JsonProvider;
import java.net.URI;
import org.junit.jupiter.api.Test;

class CoreVocabularyTest {

    private final CoreVocabulary coreVocabulary = new CoreVocabulary(JsonProvider.provider());

    @Test
    void should_return_core_vocabulary_for_core_id() {
        assertThat(coreVocabulary.id(), is(URI.create("https://json-schema.org/draft/2020-12/vocab/core")));
    }

    @Test
    void should_load_schema_keyword() {
        assertThat(
            coreVocabulary.findKeywordTypeByName("$schema").map(KeywordType::name),
            OptionalMatchers.isPresentAndIs("$schema")
        );
    }

    @Test
    void should_create_id_keyword_because_pitest_means_we_need_to_checkit() {
        assertThat(
            coreVocabulary
                .findKeywordTypeByName("$id")
                .map(kt -> kt.createKeyword(JsonSchemas.load(Json.createObjectBuilder().add("$id", "test").build()))),
            isPresentAnd(isA(IdKeyword.class))
        );
    }

    @Test
    void should_create_dynamicRef_keyword_because_pitest_means_we_need_to_checkit() {
        assertThat(
            coreVocabulary
                .findKeywordTypeByName("$dynamicRef")
                .map(
                    kt ->
                        kt.createKeyword(
                            JsonSchemas.load(Json.createObjectBuilder().add("$dynamicRef", "test").build())
                        )
                ),
            isPresentAnd(isA(DynamicRefKeyword.class))
        );
    }
}
