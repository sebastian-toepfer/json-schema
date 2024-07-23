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
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.sebastiantoepfer.jsonschema.Vocabulary;
import java.net.URI;
import java.util.stream.Stream;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class LazyVocabularyDefinitionTest {

    @Test
    void verifyEqualsContract() {
        EqualsVerifier.forClass(LazyVocabularyDefinition.class).suppress(Warning.ALL_FIELDS_SHOULD_BE_USED).verify();
    }

    @Test
    void should_throw_illegal_state_if_a_required_vocabulary_can_not_be_loaded() {
        final VocabularyDefinition vocabDef = new LazyVocabularyDefinition(URI.create("https://invalid"), true, () ->
            Stream.empty()
        );
        assertThrows(IllegalStateException.class, () -> vocabDef.findVocabulary());
    }

    @Test
    void should_find_mandatory_core_vocabulary() {
        assertThat(
            new LazyVocabularyDefinition(
                URI.create("https://json-schema.org/draft/2020-12/vocab/core"),
                true,
                new ServiceLoaderLazyVocabulariesSupplier()
            )
                .findVocabulary()
                .map(Vocabulary::id),
            isPresentAndIs(URI.create("https://json-schema.org/draft/2020-12/vocab/core"))
        );
    }

    @Test
    void should_retrun_empty_for_optional_vocabulary_which_can_not_be_loaded() {
        assertThat(
            new LazyVocabularyDefinition(URI.create("https://invalid"), false, () -> Stream.empty())
                .findVocabulary()
                .map(Vocabulary::id),
            isEmpty()
        );
    }

    @Test
    void should_know_this_id() {
        final LazyVocabularyDefinition vocbDef = new LazyVocabularyDefinition(
            URI.create("https://json-schema.org/draft/2020-12/vocab/core"),
            false,
            () -> Stream.empty()
        );

        assertThat(vocbDef.hasid(URI.create("https://json-schema.org/draft/2020-12/vocab/core")), is(true));
        assertThat(vocbDef.hasid(URI.create("https://invalid")), is(false));
    }
}
