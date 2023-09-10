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
package io.github.sebastiantoepfer.jsonschema.core.impl.spi;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import io.github.sebastiantoepfer.jsonschema.core.Validator;
import io.github.sebastiantoepfer.jsonschema.core.impl.constraint.AllOfConstraint;
import io.github.sebastiantoepfer.jsonschema.core.impl.constraint.Constraint;
import io.github.sebastiantoepfer.jsonschema.core.impl.vocab.core.VocabularyKeywordType;
import io.github.sebastiantoepfer.jsonschema.core.vocab.spi.VocabularyDefinition;
import io.github.sebastiantoepfer.jsonschema.core.vocab.spi.VocabularyDefinitions;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import java.util.Collection;

final class DefaultJsonSchema extends AbstractJsonValueSchema {

    private final Keywords keywords;

    public DefaultJsonSchema(final JsonObject value) {
        super(value);
        keywords = new Keywords(vocabulary());
    }

    @Override
    public Validator validator() {
        return asJsonObject()
            .entrySet()
            .stream()
            .map(keywords::createKeywordFor)
            .filter(Constraint.class::isInstance)
            .map(k -> (Constraint<JsonValue>) k)
            .collect(
                collectingAndThen(toList(), constraints -> new DefaultValidator(new AllOfConstraint<>(constraints)))
            );
    }

    private Collection<VocabularyDefinition> vocabulary() {
        return new KeywordSearch(new VocabularyKeywordType())
            .searchForKeywordIn(asJsonObject())
            .filter(VocabularyDefinitions.class::isInstance)
            .map(VocabularyDefinitions.class::cast)
            .stream()
            .flatMap(VocabularyDefinitions::definitions)
            .toList();
    }
}
