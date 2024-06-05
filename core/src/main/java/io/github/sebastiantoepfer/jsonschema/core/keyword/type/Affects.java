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
package io.github.sebastiantoepfer.jsonschema.core.keyword.type;

import io.github.sebastiantoepfer.jsonschema.JsonSchema;
import io.github.sebastiantoepfer.jsonschema.keyword.Annotation;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import io.github.sebastiantoepfer.jsonschema.keyword.StaticAnnotation;
import jakarta.json.JsonValue;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public final class Affects {

    private final String name;
    private final AbsenceStrategy strategy;

    public Affects(final String name, final JsonValue answerInAbsence) {
        this(name, new ProvideDefaultValue(answerInAbsence));
    }

    public Affects(final String name, final AbsenceStrategy strategy) {
        this.name = Objects.requireNonNull(name);
        this.strategy = Objects.requireNonNull(strategy);
    }

    @Override
    public String toString() {
        return "Affects{" + "name=" + name + ", strategy=" + strategy.getClass() + '}';
    }

    Map.Entry<Annotation, Function<Keyword, Keyword>> findAffectsKeywordIn(final JsonSchema schema) {
        final Map.Entry<Annotation, Function<Keyword, Keyword>> result;
        final Optional<Annotation> annotation = schema
            .keywordByName(name)
            .filter(k -> k.hasCategory(Keyword.KeywordCategory.ANNOTATION))
            .map(Keyword::asAnnotation);
        if (annotation.isPresent()) {
            result = Map.entry(annotation.get(), k -> k);
        } else {
            result = strategy.create(name);
        }
        return result;
    }

    public interface AbsenceStrategy {
        Map.Entry<Annotation, Function<Keyword, Keyword>> create(String name);
    }

    public static final class ReplaceKeyword implements AbsenceStrategy {

        @Override
        public Map.Entry<Annotation, Function<Keyword, Keyword>> create(final String name) {
            return Map.entry(new StaticAnnotation(name, JsonValue.NULL), ReplacingKeyword::new);
        }
    }

    public static final class ProvideDefaultValue implements AbsenceStrategy {

        private final JsonValue answerInAbsence;

        public ProvideDefaultValue(final JsonValue answerInAbsence) {
            this.answerInAbsence = Objects.requireNonNullElse(answerInAbsence, JsonValue.NULL);
        }

        @Override
        public Map.Entry<Annotation, Function<Keyword, Keyword>> create(final String name) {
            return Map.entry(new StaticAnnotation(name, answerInAbsence), k -> k);
        }
    }
}
