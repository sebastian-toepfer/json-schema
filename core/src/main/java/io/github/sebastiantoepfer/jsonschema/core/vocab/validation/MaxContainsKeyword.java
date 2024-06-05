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
package io.github.sebastiantoepfer.jsonschema.core.vocab.validation;

import io.github.sebastiantoepfer.ddd.common.Media;
import io.github.sebastiantoepfer.jsonschema.InstanceType;
import io.github.sebastiantoepfer.jsonschema.keyword.Annotation;
import io.github.sebastiantoepfer.jsonschema.keyword.Assertion;
import jakarta.json.JsonArray;
import jakarta.json.JsonValue;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * <b>minProperties</b> : <i>Integer</i>
 * An object instance is valid if its number of properties is less than, or equal to, the value of this keyword.<br/>
 * keyword.<br/>
 * <br/>
 * <ul>
 * <li>assertion</li>
 * </ul>
 *
 * source: https://www.learnjsonschema.com/2020-12/validation/maxcontains/
 * spec: https://json-schema.org/draft/2020-12/json-schema-validation.html#section-6.4.4
 */
final class MaxContainsKeyword implements Assertion {

    static final String NAME = "maxContains";
    private final Collection<Annotation> affects;
    private final BigInteger maxContains;

    public MaxContainsKeyword(final Collection<Annotation> affects, final BigInteger maxContains) {
        this.affects = List.copyOf(affects);
        this.maxContains = Objects.requireNonNull(maxContains);
    }

    @Override
    public boolean hasName(final String name) {
        return Objects.equals(NAME, name);
    }

    @Override
    public <T extends Media<T>> T printOn(final T media) {
        return media.withValue(NAME, maxContains);
    }

    @Override
    public boolean isValidFor(final JsonValue instance) {
        return (!InstanceType.ARRAY.isInstance(instance) || isValidFor(instance.asJsonArray()));
    }

    private boolean isValidFor(final JsonArray instance) {
        return isValidFor(
            affects
                .stream()
                .map(a -> a.valueFor(instance))
                .map(v -> new NumberOfMatches(instance, v))
                .mapToInt(NumberOfMatches::count)
                .sum()
        );
    }

    private boolean isValidFor(final int containing) {
        return containing <= maxContains.intValue();
    }
}
