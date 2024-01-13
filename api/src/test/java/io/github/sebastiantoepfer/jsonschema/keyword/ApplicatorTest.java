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
package io.github.sebastiantoepfer.jsonschema.keyword;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import io.github.sebastiantoepfer.ddd.common.Media;
import jakarta.json.JsonValue;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class ApplicatorTest {

    @Test
    void should_return_applicator_has_his_keyword_category_by_default() {
        assertThat(new TestApplicator().categories(), Matchers.contains(Keyword.KeywordCategory.APPLICATOR));
    }

    @Test
    void should_know_his_category() {
        assertThat(new TestApplicator().hasCategory(Keyword.KeywordCategory.APPLICATOR), is(true));
        assertThat(new TestApplicator().hasCategory(Keyword.KeywordCategory.ANNOTATION), is(false));
    }

    @Test
    void should_return_this_as_applicator() {
        final Keyword keyword = new TestApplicator();
        assertThat(keyword.asApplicator(), is(Matchers.sameInstance(keyword)));
    }

    private static class TestApplicator implements Applicator {

        @Override
        public boolean applyTo(final JsonValue instance) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean hasName(final String name) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public <T extends Media<T>> T printOn(final T media) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
