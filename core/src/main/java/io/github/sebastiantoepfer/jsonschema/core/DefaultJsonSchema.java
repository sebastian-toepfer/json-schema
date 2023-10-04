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

import io.github.sebastiantoepfer.jsonschema.ConstraintViolation;
import io.github.sebastiantoepfer.jsonschema.Validator;
import io.github.sebastiantoepfer.jsonschema.core.constraint.AllOfConstraint;
import io.github.sebastiantoepfer.jsonschema.core.constraint.Constraint;
import io.github.sebastiantoepfer.jsonschema.core.vocab.core.VocabularyKeywordType;
import io.github.sebastiantoepfer.jsonschema.keyword.Applicator;
import io.github.sebastiantoepfer.jsonschema.keyword.Assertion;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import io.github.sebastiantoepfer.jsonschema.vocabulary.spi.VocabularyDefinition;
import io.github.sebastiantoepfer.jsonschema.vocabulary.spi.VocabularyDefinitions;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;

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
            .map(this::asContraint)
            .flatMap(Optional::stream)
            .collect(
                collectingAndThen(toList(), constraints -> new DefaultValidator(new AllOfConstraint<>(constraints)))
            );
    }

    private Collection<VocabularyDefinition> vocabulary() {
        return new KeywordSearch(new VocabularyKeywordType())
            .searchForKeywordIn(this)
            .filter(VocabularyDefinitions.class::isInstance)
            .map(VocabularyDefinitions.class::cast)
            .stream()
            .flatMap(VocabularyDefinitions::definitions)
            .toList();
    }

    private Optional<Constraint<JsonValue>> asContraint(final Entry<String, JsonValue> property) {
        final Keyword keyword = keywords.createKeywordFor(this, property);
        final Constraint<JsonValue> result;
        if (keyword.hasCategory(Keyword.KeywordCategory.ASSERTION)) {
            result = new AssertionConstraint(keyword.asAssertion());
        } else if (keyword.hasCategory(Keyword.KeywordCategory.APPLICATOR)) {
            result = new ApplicatorConstaint(keyword.asApplicator());
        } else {
            result = null;
        }
        return Optional.ofNullable(result);
    }

    private static final class AssertionConstraint implements Constraint<JsonValue> {

        private final Assertion assertion;

        public AssertionConstraint(final Assertion assertion) {
            this.assertion = Objects.requireNonNull(assertion);
        }

        @Override
        public Collection<ConstraintViolation> violationsBy(final JsonValue value) {
            final Collection<ConstraintViolation> result;
            if (assertion.isValidFor(value)) {
                result = List.of();
            } else {
                result = List.of(new ConstraintViolation());
            }
            return result;
        }
    }

    private static final class ApplicatorConstaint implements Constraint<JsonValue> {

        private final Applicator applicator;

        public ApplicatorConstaint(final Applicator applicator) {
            this.applicator = Objects.requireNonNull(applicator);
        }

        @Override
        public Collection<ConstraintViolation> violationsBy(final JsonValue value) {
            final Collection<ConstraintViolation> result;
            if (applicator.applyTo(value)) {
                result = List.of();
            } else {
                result = List.of(new ConstraintViolation());
            }
            return result;
        }
    }
}
