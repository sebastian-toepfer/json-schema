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
package io.github.sebastiantoepfer.jsonschema.vocabulary.spi;

import io.github.sebastiantoepfer.jsonschema.Vocabulary;
import java.net.URI;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public final class LazyVocabularyDefinition implements VocabularyDefinition {

    private final URI id;
    private final boolean required;
    private final Supplier<Stream<LazyVocabularies>> lazyVocabularies;

    public LazyVocabularyDefinition(
        final URI id,
        final boolean required,
        final Supplier<Stream<LazyVocabularies>> lazyVocabularies
    ) {
        this.id = Objects.requireNonNull(id);
        this.required = required;
        this.lazyVocabularies = Objects.requireNonNull(lazyVocabularies);
    }

    @Override
    public Optional<Vocabulary> findVocabulary() {
        final Optional<Vocabulary> result = lazyVocabularies
            .get()
            .map(loader -> loader.loadVocabularyWithId(id))
            .flatMap(Optional::stream)
            .findFirst();
        if (result.isEmpty() && isRequired()) {
            throw new IllegalStateException("can not find required vocabulary: " + id);
        }
        return result;
    }

    @Override
    public boolean hasid(final URI id) {
        return Objects.equals(this.id, id);
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public String toString() {
        return "LazyVocabularyDefinition{" + "id=" + id + ", required=" + required + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.id);
        hash = 59 * hash + (this.required ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LazyVocabularyDefinition other = (LazyVocabularyDefinition) obj;
        if (this.required != other.required) {
            return false;
        }
        return Objects.equals(this.id, other.id);
    }
}
