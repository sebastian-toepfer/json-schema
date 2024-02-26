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

import io.github.sebastiantoepfer.common.condition4j.Fulfilable;
import io.github.sebastiantoepfer.common.condition4j.core.AllOf;
import io.github.sebastiantoepfer.common.condition4j.core.PredicateCondition;
import io.github.sebastiantoepfer.jsonschema.Validator;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import jakarta.json.JsonValue;
import java.util.Collection;
import java.util.Optional;

final class KeywordBasedValidator implements Validator {

    private final DefaultValidator validator;

    public KeywordBasedValidator(final Collection<Keyword> keywords) {
        this.validator = keywords
            .stream()
            .map(KeywordBasedValidator::asContraint)
            .flatMap(Optional::stream)
            .collect(collectingAndThen(toList(), constraints -> new DefaultValidator(new AllOf<>(constraints))));
    }

    @Override
    public boolean isValid(final JsonValue data) {
        return validator.isValid(data);
    }

    private static Optional<Fulfilable<JsonValue>> asContraint(final Keyword keyword) {
        final Fulfilable<JsonValue> result;
        if (keyword.hasCategory(Keyword.KeywordCategory.ASSERTION)) {
            result = new PredicateCondition<>(json -> keyword.asAssertion().isValidFor(json));
        } else if (keyword.hasCategory(Keyword.KeywordCategory.APPLICATOR)) {
            result = new PredicateCondition<>(json -> keyword.asApplicator().applyTo(json));
        } else {
            result = null;
        }
        return Optional.ofNullable(result);
    }
}
