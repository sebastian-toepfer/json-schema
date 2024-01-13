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

import io.github.sebastiantoepfer.ddd.common.Media;
import io.github.sebastiantoepfer.ddd.media.json.JsonObjectPrintable;
import io.github.sebastiantoepfer.jsonschema.InstanceType;
import io.github.sebastiantoepfer.jsonschema.JsonSchema;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import io.github.sebastiantoepfer.jsonschema.keyword.KeywordType;
import io.github.sebastiantoepfer.jsonschema.vocabulary.spi.VocabularyDefinition;
import io.github.sebastiantoepfer.jsonschema.vocabulary.spi.VocabularyDefinitions;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * see: https://json-schema.org/draft/2020-12/json-schema-core.html#name-the-vocabulary-keyword
 */
public final class VocabularyKeywordType implements KeywordType {

    @Override
    public String name() {
        return "$vocabulary";
    }

    @Override
    public VocabularyKeyword createKeyword(final JsonSchema schema) {
        final JsonValue value = schema.asJsonObject().get((name()));
        final VocabularyKeyword result;
        if (InstanceType.OBJECT.isInstance(value)) {
            result = new VocabularyKeyword(value);
        } else {
            throw new IllegalArgumentException(
                "must be an object! " +
                "read https://json-schema.org/draft/2020-12/json-schema-core.html#name-the-vocabulary-keyword" +
                "for more infromations"
            );
        }
        return result;
    }

    public final class VocabularyKeyword implements Keyword, VocabularyDefinitions {

        private final JsonObject vocabularies;

        VocabularyKeyword(final JsonValue vocabularies) {
            this(vocabularies.asJsonObject());
        }

        VocabularyKeyword(final JsonObject vocabularies) {
            this.vocabularies = Objects.requireNonNull(vocabularies);
        }

        @Override
        public <T extends Media<T>> T printOn(final T media) {
            return media.withValue(name(), new JsonObjectPrintable(vocabularies));
        }

        @Override
        public boolean hasName(final String name) {
            return Objects.equals(name(), name);
        }

        @Override
        public Collection<KeywordCategory> categories() {
            //is a identifier after spec ... but how to implement it as it?
            return List.of();
        }

        @Override
        public Stream<VocabularyDefinition> definitions() {
            return vocabularies
                .entrySet()
                .stream()
                .map(entry -> new VocabularyDefinition(URI.create(entry.getKey()), entry.getValue() == JsonValue.TRUE));
        }
    }
}
