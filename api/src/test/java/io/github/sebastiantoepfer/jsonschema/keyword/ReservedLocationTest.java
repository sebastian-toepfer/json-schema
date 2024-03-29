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
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class ReservedLocationTest {

    @Test
    void should_return_assertion_has_his_keyword_category_by_default() {
        assertThat(
            new TestReservedLocation().categories(),
            Matchers.contains(Keyword.KeywordCategory.RESERVED_LOCATION)
        );
    }

    @Test
    void should_know_his_category() {
        assertThat(new TestReservedLocation().hasCategory(Keyword.KeywordCategory.RESERVED_LOCATION), is(true));
        assertThat(new TestReservedLocation().hasCategory(Keyword.KeywordCategory.APPLICATOR), is(false));
    }

    @Test
    void should_return_this_as_reservedLocation() {
        final Keyword keyword = new TestReservedLocation();
        assertThat(keyword.asReservedLocation(), is(Matchers.sameInstance(keyword)));
    }

    private static class TestReservedLocation implements ReservedLocation {

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
