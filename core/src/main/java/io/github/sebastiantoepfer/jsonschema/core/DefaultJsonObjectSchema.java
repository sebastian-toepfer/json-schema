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

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import io.github.sebastiantoepfer.jsonschema.InstanceType;
import io.github.sebastiantoepfer.jsonschema.JsonSubSchema;
import io.github.sebastiantoepfer.jsonschema.Validator;
import io.github.sebastiantoepfer.jsonschema.core.codition.AllOfCondition;
import io.github.sebastiantoepfer.jsonschema.core.codition.ApplicatorBasedCondtion;
import io.github.sebastiantoepfer.jsonschema.core.codition.AssertionBasedCondition;
import io.github.sebastiantoepfer.jsonschema.core.codition.Condition;
import io.github.sebastiantoepfer.jsonschema.core.vocab.core.VocabularyKeywordType;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import io.github.sebastiantoepfer.jsonschema.keyword.KeywordType;
import io.github.sebastiantoepfer.jsonschema.vocabulary.spi.VocabularyDefinition;
import io.github.sebastiantoepfer.jsonschema.vocabulary.spi.VocabularyDefinitions;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

final class DefaultJsonObjectSchema extends AbstractJsonValueSchema {

    public DefaultJsonObjectSchema(final JsonObject value) {
        super(value);
    }

    @Override
    public Validator validator() {
        return keywords()
            .map(this::asContraint)
            .flatMap(Optional::stream)
            .collect(
                collectingAndThen(toList(), constraints -> new DefaultValidator(new AllOfCondition<>(constraints)))
            );
    }

    @Override
    public Optional<Keyword> keywordByName(final String name) {
        return keywords().filter(k -> k.hasName(name)).findFirst();
    }

    private Stream<Keyword> keywords() {
        final Keywords keywords = new Keywords(vocabulary());
        return asJsonObject().keySet().stream().map(propertyName -> keywords.createKeywordFor(this, propertyName));
    }

    private Collection<VocabularyDefinition> vocabulary() {
        final KeywordType keywordType = new VocabularyKeywordType();
        return Optional
            .ofNullable(asJsonObject().get(keywordType.name()))
            .map(keywordValue -> keywordType.createKeyword(this))
            .filter(VocabularyDefinitions.class::isInstance)
            .map(VocabularyDefinitions.class::cast)
            .stream()
            .flatMap(VocabularyDefinitions::definitions)
            .toList();
    }

    @Override
    public Optional<JsonSubSchema> asSubSchema(final String name) {
        return Optional
            .ofNullable(asJsonObject().get(name))
            .filter(value ->
                Stream.of(InstanceType.BOOLEAN, InstanceType.OBJECT).anyMatch(type -> type.isInstance(value))
            )
            .map(new DefaultJsonSchemaFactory()::create)
            .map(subSchema -> new DefaultJsonSubSchema(this, subSchema));
    }

    private Optional<Condition<JsonValue>> asContraint(final Keyword keyword) {
        final Condition<JsonValue> result;
        if (keyword.hasCategory(Keyword.KeywordCategory.ASSERTION)) {
            result = new AssertionBasedCondition(keyword.asAssertion());
        } else if (keyword.hasCategory(Keyword.KeywordCategory.APPLICATOR)) {
            result = new ApplicatorBasedCondtion(keyword.asApplicator());
        } else {
            result = null;
        }
        return Optional.ofNullable(result);
    }
}
