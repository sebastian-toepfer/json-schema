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

import static com.github.npathai.hamcrestopt.OptionalMatchers.isEmpty;
import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresentAndIs;
import static org.hamcrest.MatcherAssert.assertThat;

import io.github.sebastiantoepfer.jsonschema.Vocabulary;
import java.net.URI;
import org.junit.jupiter.api.Test;

class CoreLazyVocabularyTest {

    @Test
    void should_return_empty_for_non_core_id() {
        assertThat(new CoreLazyVocabulary().loadVocabularyWithId(URI.create("")), isEmpty());
    }

    @Test
    void should_return_core_vocabulary_for_core_id() {
        assertThat(
            new CoreLazyVocabulary()
                .loadVocabularyWithId(URI.create("https://json-schema.org/draft/2020-12/vocab/core"))
                .map(Vocabulary::id),
            isPresentAndIs(URI.create("https://json-schema.org/draft/2020-12/vocab/core"))
        );
    }
}