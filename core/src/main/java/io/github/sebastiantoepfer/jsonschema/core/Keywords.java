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

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toMap;

import io.github.sebastiantoepfer.jsonschema.JsonSchema;
import io.github.sebastiantoepfer.jsonschema.Vocabulary;
import io.github.sebastiantoepfer.jsonschema.core.vocab.applicator.ApplicatorVocabulary;
import io.github.sebastiantoepfer.jsonschema.core.vocab.core.CoreVocabulary;
import io.github.sebastiantoepfer.jsonschema.core.vocab.validation.ValidationVocabulary;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import io.github.sebastiantoepfer.jsonschema.vocabulary.spi.VocabularyDefinition;
import jakarta.json.spi.JsonProvider;
import java.net.URI;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

final class Keywords {

    private static final Map<URI, Vocabulary> MANDANTORY_VOCABS;
    private static final Collection<Vocabulary> DEFAULT_VOCABS;

    static {
        MANDANTORY_VOCABS =
            List
                .of(new BasicVocabulary(), new CoreVocabulary(JsonProvider.provider()))
                .stream()
                .collect(toMap(Vocabulary::id, Function.identity()));

        DEFAULT_VOCABS = List.of(new ValidationVocabulary(JsonProvider.provider()), new ApplicatorVocabulary());
    }

    private final Collection<Vocabulary> vocabularies;

    public Keywords(final Collection<VocabularyDefinition> vocabDefs) {
        if (
            vocabDefs
                .stream()
                .filter(vocabDef -> MANDANTORY_VOCABS.containsKey(vocabDef.id()))
                .anyMatch(not(VocabularyDefinition::required))
        ) {
            throw new IllegalArgumentException("can not be created without core vocabulary is requiered!");
        }
        vocabularies =
            Stream
                .concat(
                    Stream.concat(MANDANTORY_VOCABS.values().stream(), DEFAULT_VOCABS.stream()),
                    vocabDefs.stream().map(VocabularyDefinition::findVocabulary).flatMap(Optional::stream)
                )
                .collect(
                    Collector.of(
                        ArrayDeque::new,
                        ArrayDeque::addFirst,
                        (first, last) -> null //pitest otherwise see mutants here :(
                    )
                );
    }

    public Keyword createKeywordFor(final JsonSchema schema, final String propertyName) {
        return vocabularies
            .stream()
            .map(vocab -> vocab.findKeywordTypeByName(propertyName))
            .flatMap(Optional::stream)
            .findFirst()
            .map(keywordType -> keywordType.createKeyword(schema))
            .orElseThrow();
    }
}
