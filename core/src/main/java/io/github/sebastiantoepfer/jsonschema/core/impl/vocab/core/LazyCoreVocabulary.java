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
package io.github.sebastiantoepfer.jsonschema.core.impl.vocab.core;

import io.github.sebastiantoepfer.jsonschema.core.Vocabulary;
import io.github.sebastiantoepfer.jsonschema.core.vocab.spi.LazyVocabularies;
import java.net.URI;
import java.util.Objects;
import java.util.Optional;

public final class LazyCoreVocabulary implements LazyVocabularies {

    private final Vocabulary vocab;

    public LazyCoreVocabulary() {
        this.vocab = new CoreVocabulary();
    }

    @Override
    public Optional<Vocabulary> loadVocabularyWithId(final URI id) {
        final Optional<Vocabulary> result;
        if (Objects.equals(id, vocab.id())) {
            result = Optional.of(vocab);
        } else {
            result = Optional.empty();
        }
        return result;
    }
}
