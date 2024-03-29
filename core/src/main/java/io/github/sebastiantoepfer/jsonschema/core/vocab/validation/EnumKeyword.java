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
import io.github.sebastiantoepfer.ddd.media.json.JsonObjectPrintable;
import io.github.sebastiantoepfer.jsonschema.InstanceType;
import io.github.sebastiantoepfer.jsonschema.keyword.Assertion;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonNumber;
import jakarta.json.JsonValue;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * <b>enum</b> : <i>Array<Any></i><br/>
 * Validation succeeds if the instance is equal to one of the elements in this keyword’s array value.<br/>
 * <br/>
 * <ul>
 * <li>assertion</li>
 * </ul>
 *
 * source: https://www.learnjsonschema.com/2020-12/validation/enum/
 * spec: https://json-schema.org/draft/2020-12/json-schema-validation.html#section-6.1.2
 */
final class EnumKeyword implements Assertion {

    static final String NAME = "enum";
    private final JsonArray allowedValues;

    public EnumKeyword(final JsonArray allowedValues) {
        this.allowedValues = allowedValues;
    }

    @Override
    public <T extends Media<T>> T printOn(final T media) {
        return new JsonObjectPrintable(Json.createObjectBuilder().add(NAME, allowedValues).build()).printOn(media);
    }

    @Override
    public boolean isValidFor(final JsonValue instance) {
        final boolean result;
        if (InstanceType.NUMBER.isInstance(instance)) {
            result = allowedValues
                .stream()
                .filter(InstanceType.NUMBER::isInstance)
                .map(JsonNumber.class::cast)
                .map(JsonNumber::bigDecimalValue)
                .map(BigDecimal::stripTrailingZeros)
                .anyMatch(Predicate.isEqual(((JsonNumber) instance).bigDecimalValue().stripTrailingZeros()));
        } else {
            result = allowedValues.stream().anyMatch(Predicate.isEqual(instance));
        }
        return result;
    }

    @Override
    public boolean hasName(final String name) {
        return Objects.equals(NAME, name);
    }
}
