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
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

import io.github.sebastiantoepfer.ddd.common.Media;
import io.github.sebastiantoepfer.ddd.media.core.HashMapMedia;
import io.github.sebastiantoepfer.jsonschema.keyword.Applicator;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import jakarta.json.JsonValue;
import java.util.Collection;
import java.util.EnumSet;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class LinkedKeywordTest {

    @Test
    void should_return_categories_from_both_keywords() {
        assertThat(
            new LinkedKeyword(new MockAnnotation(), LINKTYPE.VALID, new MockApplicator()).categories(),
            containsInAnyOrder(Keyword.KeywordCategory.ANNOTATION, Keyword.KeywordCategory.APPLICATOR)
        );
    }

    @Nested
    class LinkTypeIsValid {

        @Test
        void should_valid_if_both_are_valid() {
            assertThat(
                new LinkedKeyword(new MockApplicator(true), LINKTYPE.VALID, new MockApplicator(true))
                    .asApplicator()
                    .applyTo(JsonValue.TRUE),
                is(true)
            );
        }

        @Test
        void should_invalid_if_first_is_valid_and_second_is_invalid() {
            assertThat(
                new LinkedKeyword(new MockApplicator(true), LINKTYPE.VALID, new MockApplicator(false))
                    .asApplicator()
                    .applyTo(JsonValue.TRUE),
                is(false)
            );
        }

        @Test
        void should_valid_if_first_is_invalid() {
            assertThat(
                new LinkedKeyword(new MockApplicator(false), LINKTYPE.VALID, new MockApplicator(true))
                    .asApplicator()
                    .applyTo(JsonValue.TRUE),
                is(true)
            );
        }
    }

    @Nested
    class LinkTypeIsInvalid {

        @Test
        void should_be_valid_if_first_is_valid() {
            assertThat(
                new LinkedKeyword(new MockApplicator(true), LINKTYPE.INVALID, new MockApplicator(false))
                    .asApplicator()
                    .applyTo(JsonValue.TRUE),
                is(true)
            );
        }

        @Test
        void should_be_valid_if_first_is_invalid_and_second_is_valid() {
            assertThat(
                new LinkedKeyword(new MockApplicator(false), LINKTYPE.INVALID, new MockApplicator(true))
                    .asApplicator()
                    .applyTo(JsonValue.TRUE),
                is(true)
            );
        }

        @Test
        void should_be_invalid_if_both_are_invalid() {
            assertThat(
                new LinkedKeyword(new MockApplicator(false), LINKTYPE.INVALID, new MockApplicator(false))
                    .asApplicator()
                    .applyTo(JsonValue.TRUE),
                is(false)
            );
        }
    }

    @Test
    void should_know_his_name() {
        final Keyword keyword = new LinkedKeyword(new MockKeyword("if"), LINKTYPE.VALID, new MockKeyword("then"));

        assertThat(keyword.hasName("if"), is(false));
        assertThat(keyword.hasName("then"), is(true));
    }

    @Test
    void should_use_second_to_print() {
        assertThat(
            new LinkedKeyword(new MockKeyword("if"), LINKTYPE.VALID, new MockKeyword("then")).printOn(
                new HashMapMedia()
            ),
            is(new MockKeyword("then").printOn(new HashMapMedia()))
        );
    }

    private static final class MockAnnotation implements Keyword {

        @Override
        public Collection<KeywordCategory> categories() {
            return EnumSet.of(KeywordCategory.ANNOTATION, KeywordCategory.APPLICATOR);
        }

        @Override
        public boolean hasName(String name) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public <T extends Media<T>> T printOn(T media) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    private static final class MockApplicator implements Applicator {

        private final boolean result;

        public MockApplicator() {
            this(true);
        }

        public MockApplicator(final boolean result) {
            this.result = result;
        }

        @Override
        public boolean applyTo(final JsonValue instance) {
            return result;
        }

        @Override
        public boolean hasName(String name) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public <T extends Media<T>> T printOn(T media) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
