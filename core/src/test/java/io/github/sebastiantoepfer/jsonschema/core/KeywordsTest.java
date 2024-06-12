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
package io.github.sebastiantoepfer.jsonschema.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.sebastiantoepfer.jsonschema.Vocabulary;
import io.github.sebastiantoepfer.jsonschema.core.vocab.core.CoreVocabulary;
import io.github.sebastiantoepfer.jsonschema.vocabulary.spi.VocabularyDefinition;
import jakarta.json.spi.JsonProvider;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class KeywordsTest {

    @Test
    void should_not_be_createbale_without_mandantory_core_vocabulary() {
        final Collection<VocabularyDefinition> vocabDefs = List.of(
            new TestVocabularyDefinition(new CoreVocabulary(JsonProvider.provider()).id(), false)
        );
        assertThrows(IllegalArgumentException.class, () -> new Keywords(vocabDefs));
    }

    @Test
    void should_not_be_createbale_without_mandantory_base_vocabulary() {
        final Collection<VocabularyDefinition> vocabDefs = List.of(
            new TestVocabularyDefinition(new BasicVocabulary().id(), false)
        );
        assertThrows(IllegalArgumentException.class, () -> new Keywords(vocabDefs));
    }

    @Test
    void should_be_createbale_with_optional_vocabularies() {
        assertThat(
            new Keywords(List.of(new TestVocabularyDefinition(URI.create("http://optinal"), false))),
            is(not(nullValue()))
        );
    }

    private static final class TestVocabularyDefinition implements VocabularyDefinition {

        private final URI id;
        private final boolean required;

        public TestVocabularyDefinition(final URI id) {
            this(id, false);
        }

        public TestVocabularyDefinition(final URI id, final boolean required) {
            this.id = id;
            this.required = required;
        }

        @Override
        public Optional<Vocabulary> findVocabulary() {
            return Optional.empty();
        }

        @Override
        public boolean hasid(URI id) {
            return Objects.equals(this.id, id);
        }

        @Override
        public boolean isRequired() {
            return required;
        }
    }
}
