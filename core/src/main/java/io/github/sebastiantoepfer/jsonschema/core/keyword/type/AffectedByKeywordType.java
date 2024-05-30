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

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import io.github.sebastiantoepfer.jsonschema.JsonSchema;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import io.github.sebastiantoepfer.jsonschema.keyword.KeywordType;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

public class AffectedByKeywordType implements KeywordType {

    private final String name;
    private final List<String> affectedBy;
    private final BiFunction<List<Keyword>, JsonSchema, Keyword> keywordCreator;

    public AffectedByKeywordType(
        final String name,
        final List<String> affectedBy,
        final BiFunction<List<Keyword>, JsonSchema, Keyword> keywordCreator
    ) {
        this.name = Objects.requireNonNull(name);
        this.affectedBy = List.copyOf(affectedBy);
        this.keywordCreator = Objects.requireNonNull(keywordCreator);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Keyword createKeyword(final JsonSchema schema) {
        return new AffectedByKeyword(schema, name, affectedBy, keywordCreator);
    }

    static final class AffectedByKeyword extends KeywordRelationship {

        private final JsonSchema schema;
        private final List<String> affectedBy;
        private final BiFunction<List<Keyword>, JsonSchema, Keyword> keywordCreator;

        public AffectedByKeyword(
            final JsonSchema schema,
            final String name,
            final List<String> affectedBy,
            final BiFunction<List<Keyword>, JsonSchema, Keyword> keywordCreator
        ) {
            super(name);
            this.schema = Objects.requireNonNull(schema);
            this.affectedBy = List.copyOf(affectedBy);
            this.keywordCreator = Objects.requireNonNull(keywordCreator);
        }

        @Override
        protected Keyword delegate() {
            return affectedBy
                .stream()
                .map(schema::keywordByName)
                .flatMap(Optional::stream)
                .collect(collectingAndThen(toList(), k -> keywordCreator.apply(k, schema)));
        }
    }
}
