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
package io.github.sebastiantoepfer.jsonschema.vocabulary.spi;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isEmpty;
import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresentAndIs;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import io.github.sebastiantoepfer.jsonschema.JsonSchema;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import io.github.sebastiantoepfer.jsonschema.keyword.KeywordType;
import java.net.URI;
import org.junit.jupiter.api.Test;

class DefaultVocabularyTest {

    @Test
    void should_return_the_id() {
        assertThat(
            new DefaultVocabulary(URI.create("http::/localhots/vocab")).id(),
            is(URI.create("http::/localhots/vocab"))
        );
    }

    @Test
    void should_return_empty_if_requested_keyword_is_unknown() {
        assertThat(
            new DefaultVocabulary(URI.create("http://localhost"), new SimpleTestKeywordType("name"))
                .findKeywordTypeByName("test"),
            isEmpty()
        );
    }

    @Test
    void should_return_requested_keyword_if_is_it_known() {
        final KeywordType testKeywordType = new SimpleTestKeywordType("test");
        assertThat(
            new DefaultVocabulary(URI.create("http://localhost"), testKeywordType).findKeywordTypeByName("test"),
            isPresentAndIs(testKeywordType)
        );
    }

    private static class SimpleTestKeywordType implements KeywordType {

        private final String name;

        public SimpleTestKeywordType(final String name) {
            this.name = name;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public Keyword createKeyword(final JsonSchema schema) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
