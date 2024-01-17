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
package io.github.sebastiantoepfer.jsonschema.core.codition;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.List;
import org.junit.jupiter.api.Test;

class CollectionElementsCondtionTest {

    @Test
    void should_be_valid_if_all_elements_match_given_conditon() {
        assertThat(
            new CollectionElementsCondtion<String>(v -> v.startsWith("w"))
                .isFulfilledBy(List.of("world", "whale", "word")),
            is(true)
        );
    }

    @Test
    void should_be_invalid_if_all_elements_not_match_given_conditon() {
        assertThat(
            new CollectionElementsCondtion<String>(v -> v.startsWith("a"))
                .isFulfilledBy(List.of("world", "whale", "word")),
            is(false)
        );
    }

    @Test
    void should_be_invalid_if_one_elements_not_match_given_conditon() {
        assertThat(
            new CollectionElementsCondtion<String>(v -> v.contains("o"))
                .isFulfilledBy(List.of("world", "whale", "word")),
            is(false)
        );
    }
}
