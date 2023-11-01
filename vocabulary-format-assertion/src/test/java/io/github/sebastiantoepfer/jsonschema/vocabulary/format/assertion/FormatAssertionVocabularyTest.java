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
package io.github.sebastiantoepfer.jsonschema.vocabulary.format.assertion;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isEmpty;
import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresent;
import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresentAnd;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import io.github.sebastiantoepfer.jsonschema.JsonSchemas;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import jakarta.json.Json;
import java.net.URI;
import org.junit.jupiter.api.Test;

class FormatAssertionVocabularyTest {

    @Test
    void should_be_return_empty_for_wrong_vocab_id() {
        assertThat(new FormatAssertionVocabulary().loadVocabularyWithId(URI.create("http://localhost")), isEmpty());
    }

    @Test
    void should_be_return_vocab_for_right_vocab_id() {
        assertThat(
            new FormatAssertionVocabulary()
                .loadVocabularyWithId(URI.create("https://json-schema.org/draft/2020-12/vocab/format-assertion")),
            isPresent()
        );
    }

    @Test
    void should_be_loaded_by_lazy_vocab_and_keyword_format_should_be_an_assertion_results_in_false() {
        assertThat(
            JsonSchemas
                .load(
                    Json
                        .createObjectBuilder()
                        .add(
                            "$vocabulary",
                            Json
                                .createObjectBuilder()
                                .add("https://json-schema.org/draft/2020-12/vocab/format-assertion", true)
                        )
                        .add("format", "email")
                        .build()
                )
                .keywordByName("format")
                .map(Keyword::asAssertion)
                .map(assertion -> assertion.isValidFor(Json.createValue("test"))),
            isPresentAnd(is(false))
        );
    }

    @Test
    void should_be_loaded_by_lazy_vocab_and_keyword_format_should_be_an_assertion_results_in_true() {
        assertThat(
            JsonSchemas
                .load(
                    Json
                        .createObjectBuilder()
                        .add(
                            "$vocabulary",
                            Json
                                .createObjectBuilder()
                                .add("https://json-schema.org/draft/2020-12/vocab/format-assertion", true)
                        )
                        .add("format", "email")
                        .build()
                )
                .keywordByName("format")
                .map(Keyword::asAssertion)
                .map(assertion ->
                    assertion.isValidFor(Json.createValue("61313468+sebastian-toepfer@users.noreply.github.com"))
                ),
            isPresentAnd(is(true))
        );
    }
}
