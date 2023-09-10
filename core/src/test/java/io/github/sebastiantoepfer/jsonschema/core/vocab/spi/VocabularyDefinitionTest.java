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
package io.github.sebastiantoepfer.jsonschema.core.vocab.spi;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isEmpty;
import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresentAndIs;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.sebastiantoepfer.jsonschema.core.Vocabulary;
import java.net.URI;
import org.junit.jupiter.api.Test;

class VocabularyDefinitionTest {

    @Test
    void should_throw_illegal_state_if_a_required_vocabulary_can_not_be_loaded() {
        final VocabularyDefinition vocabDef = new VocabularyDefinition(URI.create("https://invalid"), true);
        assertThrows(IllegalStateException.class, () -> vocabDef.findVocabulary());
    }

    @Test
    void should_find_mandatory_core_vocabulary() {
        assertThat(
            new VocabularyDefinition(URI.create("https://json-schema.org/draft/2020-12/vocab/core"), true)
                .findVocabulary()
                .map(Vocabulary::id),
            isPresentAndIs(URI.create("https://json-schema.org/draft/2020-12/vocab/core"))
        );
    }

    @Test
    void should_retrun_empty_for_optional_vocabulary_which_can_not_be_loaded() {
        assertThat(
            new VocabularyDefinition(URI.create("https://invalid"), false).findVocabulary().map(Vocabulary::id),
            isEmpty()
        );
    }
}
