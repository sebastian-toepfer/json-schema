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
package io.github.sebastiantoepfer.jsonschema.core.keyword.type;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.sebastiantoepfer.ddd.media.core.HashMapMedia;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword.KeywordCategory;
import java.util.EnumSet;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class ReplacingKeywordTest {

    @Test
    void should_not_be_createable_as_identifier_if_it_should_replace_it() {
        final Keyword keyword = new ReplacingKeyword(
            new MockKeyword("test"),
            EnumSet.of(Keyword.KeywordCategory.IDENTIFIER)
        );
        assertThrows(UnsupportedOperationException.class, () -> keyword.asIdentifier());
    }

    @Test
    void should_return_identifier_if_it_should_not_replaced() {
        final Keyword keyword = new MockKeyword("test");
        assertThat(
            new ReplacingKeyword(
                keyword,
                EnumSet.complementOf(EnumSet.of(Keyword.KeywordCategory.IDENTIFIER))
            ).asIdentifier(),
            is(sameInstance(keyword))
        );
    }

    @Test
    void should_not_be_createable_as_assertion_if_it_should_replace_it() {
        final Keyword keyword = new ReplacingKeyword(
            new MockKeyword("test"),
            EnumSet.of(Keyword.KeywordCategory.ASSERTION)
        );
        assertThrows(UnsupportedOperationException.class, () -> keyword.asAssertion());
    }

    @Test
    void should_return_assertion_if_it_should_not_replaced() {
        final Keyword keyword = new MockKeyword("test");
        assertThat(
            new ReplacingKeyword(
                keyword,
                EnumSet.complementOf(EnumSet.of(Keyword.KeywordCategory.ASSERTION))
            ).asAssertion(),
            is(sameInstance(keyword))
        );
    }

    @Test
    void should_not_be_createable_as_annotation_if_it_should_replace_it() {
        final Keyword keyword = new ReplacingKeyword(
            new MockKeyword("test"),
            EnumSet.of(Keyword.KeywordCategory.ANNOTATION)
        );
        assertThrows(UnsupportedOperationException.class, () -> keyword.asAnnotation());
    }

    @Test
    void should_return_annotation_if_it_should_not_replaced() {
        final Keyword keyword = new MockKeyword("test");
        assertThat(
            new ReplacingKeyword(
                keyword,
                EnumSet.complementOf(EnumSet.of(Keyword.KeywordCategory.ANNOTATION))
            ).asAnnotation(),
            is(sameInstance(keyword))
        );
    }

    @Test
    void should_not_be_createable_as_applicator_if_it_should_replace_it() {
        final Keyword keyword = new ReplacingKeyword(
            new MockKeyword("test"),
            EnumSet.of(Keyword.KeywordCategory.APPLICATOR)
        );
        assertThrows(UnsupportedOperationException.class, () -> keyword.asApplicator());
    }

    @Test
    void should_return_applicator_if_it_should_not_replaced() {
        final Keyword keyword = new MockKeyword("test");
        assertThat(
            new ReplacingKeyword(
                keyword,
                EnumSet.complementOf(EnumSet.of(Keyword.KeywordCategory.APPLICATOR))
            ).asApplicator(),
            is(sameInstance(keyword))
        );
    }

    @Test
    void should_not_be_createable_as_reserved_location_if_it_should_replace_it() {
        final Keyword keyword = new ReplacingKeyword(
            new MockKeyword("test"),
            EnumSet.of(Keyword.KeywordCategory.RESERVED_LOCATION)
        );
        assertThrows(UnsupportedOperationException.class, () -> keyword.asReservedLocation());
    }

    @Test
    void should_return_reserved_location_if_it_should_not_replaced() {
        final Keyword keyword = new MockKeyword("test");
        assertThat(
            new ReplacingKeyword(
                keyword,
                EnumSet.complementOf(EnumSet.of(Keyword.KeywordCategory.RESERVED_LOCATION))
            ).asReservedLocation(),
            is(sameInstance(keyword))
        );
    }

    @Test
    void should_name_of_decoded_keyword() {
        final Keyword keyword = new ReplacingKeyword(new MockKeyword("test"));

        assertThat(keyword.hasName("test"), is(true));
        assertThat(keyword.hasName("bunny"), is(false));
    }

    @Test
    void should_print_as_decored_keyword() {
        assertThat(
            new ReplacingKeyword(new MockKeyword("test")).printOn(new HashMapMedia()),
            Matchers.hasEntry("test", "test")
        );
    }

    @ParameterizedTest
    @EnumSource(KeywordCategory.class)
    void should_not_return_replaced_keyword_category(final Keyword.KeywordCategory category) {
        assertThat(
            new ReplacingKeyword(new MockKeyword("test"), EnumSet.of(category)).categories(),
            both(not(hasItem(category))).and((Matcher) is(not(empty())))
        );
    }
}
