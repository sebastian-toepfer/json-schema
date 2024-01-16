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
package io.github.sebastiantoepfer.jsonschema.core.vocab;

import io.github.sebastiantoepfer.jsonschema.Vocabulary;
import io.github.sebastiantoepfer.jsonschema.core.vocab.applicator.ApplicatorVocabulary;
import io.github.sebastiantoepfer.jsonschema.core.vocab.content.ContentVocabulary;
import io.github.sebastiantoepfer.jsonschema.core.vocab.core.CoreVocabulary;
import io.github.sebastiantoepfer.jsonschema.core.vocab.format.FormatVocabulary;
import io.github.sebastiantoepfer.jsonschema.core.vocab.meta.MetaDataVocabulary;
import io.github.sebastiantoepfer.jsonschema.core.vocab.unevaluated.UnevaluatedVocabulary;
import io.github.sebastiantoepfer.jsonschema.core.vocab.validation.ValidationVocabulary;
import io.github.sebastiantoepfer.jsonschema.vocabulary.spi.LazyVocabularies;
import jakarta.json.spi.JsonProvider;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public final class OfficialVocabularies implements LazyVocabularies {

    private static final JsonProvider JSONP = JsonProvider.provider();
    private static final Collection<Vocabulary> OFFICEAL_VOCABS;

    static {
        OFFICEAL_VOCABS =
            List.of(
                new CoreVocabulary(JSONP),
                new ApplicatorVocabulary(),
                new ValidationVocabulary(),
                new MetaDataVocabulary(),
                new FormatVocabulary(),
                new UnevaluatedVocabulary(),
                new ContentVocabulary()
            );
    }

    @Override
    public Optional<Vocabulary> loadVocabularyWithId(final URI id) {
        return OFFICEAL_VOCABS.stream().filter(vocab -> vocab.id().equals(id)).findFirst();
    }
}
