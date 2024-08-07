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
package io.github.sebastiantoepfer.jsonschema.core.vocab.core;

import io.github.sebastiantoepfer.ddd.common.Media;
import io.github.sebastiantoepfer.ddd.media.json.JsonObjectPrintable;
import io.github.sebastiantoepfer.jsonschema.Vocabulary;
import io.github.sebastiantoepfer.jsonschema.core.vocab.OfficialVocabularies;
import io.github.sebastiantoepfer.jsonschema.vocabulary.spi.LazyVocabularies;
import io.github.sebastiantoepfer.jsonschema.vocabulary.spi.LazyVocabularyDefinition;
import io.github.sebastiantoepfer.jsonschema.vocabulary.spi.VocabularyDefinition;
import io.github.sebastiantoepfer.jsonschema.vocabulary.spi.VocabularyDefinitions;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * <b>$vocabulary</b> : <i>Object&lt;URI, Boolean&gt;</i><br/>
 * This keyword is used in meta-schemas to identify the required and optional vocabularies available for use in<br/>
 * schemas described by that meta-schema.<br/>
 * <br/>
 * <ul>
 * <li>identifier</li>
 * </ul>
 *
 * source: https://www.learnjsonschema.com/2020-12/core/vocabulary/
 * spec: https://json-schema.org/draft/2020-12/json-schema-core.html#section-8.1.2
 */
final class VocabularyKeyword implements VocabularyDefinitions {

    static final String NAME = "$vocabulary";
    private final JsonObject vocabularies;
    private final Supplier<Stream<LazyVocabularies>> lazyVocabulariesSupplier;

    VocabularyKeyword(final JsonValue vocabularies, final Supplier<Stream<LazyVocabularies>> lazyVocabulariesSupplier) {
        this(vocabularies.asJsonObject(), lazyVocabulariesSupplier);
    }

    VocabularyKeyword(
        final JsonObject vocabularies,
        final Supplier<Stream<LazyVocabularies>> lazyVocabulariesSupplier
    ) {
        this.vocabularies = Objects.requireNonNull(vocabularies);
        this.lazyVocabulariesSupplier = Objects.requireNonNull(lazyVocabulariesSupplier);
    }

    @Override
    public <T extends Media<T>> T printOn(final T media) {
        return media.withValue(NAME, new JsonObjectPrintable(vocabularies));
    }

    @Override
    public boolean hasName(final String name) {
        return Objects.equals(NAME, name);
    }

    @Override
    public Collection<KeywordCategory> categories() {
        //is a identifier after spec ... but how to implement it as it?
        return List.of();
    }

    @Override
    public Stream<VocabularyDefinition> definitions() {
        return vocabularies.entrySet().stream().map(LazyNonOfficalVocabularyDefinition::new);
    }

    private final class LazyNonOfficalVocabularyDefinition implements VocabularyDefinition {

        private final URI id;
        private final JsonValue required;

        public LazyNonOfficalVocabularyDefinition(final Map.Entry<String, JsonValue> property) {
            this(URI.create(property.getKey()), property.getValue());
        }

        public LazyNonOfficalVocabularyDefinition(final URI id, final JsonValue required) {
            this.id = Objects.requireNonNull(id);
            this.required = required;
        }

        @Override
        public Optional<Vocabulary> findVocabulary() {
            return new OfficialVocabularies()
                .loadVocabularyWithId(id)
                .or(() -> new LazyVocabularyDefinition(id, isRequired(), lazyVocabulariesSupplier).findVocabulary());
        }

        @Override
        public boolean hasid(final URI id) {
            return Objects.equals(this.id, id);
        }

        @Override
        public boolean isRequired() {
            return JsonValue.TRUE.equals(required);
        }
    }
}
