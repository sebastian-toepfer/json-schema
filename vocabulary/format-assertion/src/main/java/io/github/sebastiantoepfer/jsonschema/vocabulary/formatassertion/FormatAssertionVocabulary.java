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
package io.github.sebastiantoepfer.jsonschema.vocabulary.formatassertion;

import static io.github.sebastiantoepfer.jsonschema.vocabulary.formatassertion.FormatKeyword.NAME;

import io.github.sebastiantoepfer.jsonschema.Vocabulary;
import io.github.sebastiantoepfer.jsonschema.keyword.KeywordType;
import io.github.sebastiantoepfer.jsonschema.vocabulary.spi.LazyVocabularies;
import java.net.URI;
import java.util.Objects;
import java.util.Optional;

public final class FormatAssertionVocabulary implements LazyVocabularies, Vocabulary {

    private final URI id;

    public FormatAssertionVocabulary() {
        id = URI.create("https://json-schema.org/draft/2020-12/vocab/format-assertion");
    }

    @Override
    public Optional<Vocabulary> loadVocabularyWithId(final URI id) {
        final Optional<Vocabulary> result;
        if (id().equals(id)) {
            result = Optional.of(this);
        } else {
            result = Optional.empty();
        }
        return result;
    }

    @Override
    public URI id() {
        return id;
    }

    @Override
    public Optional<KeywordType> findKeywordTypeByName(final String name) {
        final Optional<KeywordType> result;
        if (Objects.equals(NAME, name)) {
            result = Optional.of(new FormatAssertionKeyWordType(new Formats()));
        } else {
            result = Optional.empty();
        }
        return result;
    }
}
