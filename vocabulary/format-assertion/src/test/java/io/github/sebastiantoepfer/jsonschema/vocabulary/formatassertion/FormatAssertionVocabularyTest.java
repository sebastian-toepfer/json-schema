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
package io.github.sebastiantoepfer.jsonschema.vocabulary.formatassertion;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isEmpty;
import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresent;
import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresentAnd;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.net.URI;
import org.junit.jupiter.api.Test;

class FormatAssertionVocabularyTest {

    @Test
    void should_return_empty_optional_for_unknow_id() {
        assertThat(new FormatAssertionVocabulary().loadVocabularyWithId(URI.create("hello")), isEmpty());
    }

    @Test
    void should_return_non_empty_optional_for_right_id() {
        assertThat(
            new FormatAssertionVocabulary()
                .loadVocabularyWithId(URI.create("https://json-schema.org/draft/2020-12/vocab/format-assertion")),
            isPresent()
        );
    }

    @Test
    void should_return_empty_for_invalid_keywordname() {
        assertThat(new FormatAssertionVocabulary().findKeywordTypeByName("$schema"), isEmpty());
    }

    @Test
    void should_return_keywordtype_for_format_keyword() {
        assertThat(
            new FormatAssertionVocabulary().findKeywordTypeByName("format"),
            isPresentAnd(is(instanceOf(FormatAssertionKeyWordType.class)))
        );
    }
}
