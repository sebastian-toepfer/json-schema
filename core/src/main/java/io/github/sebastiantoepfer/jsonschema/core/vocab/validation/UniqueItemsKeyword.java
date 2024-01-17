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
import io.github.sebastiantoepfer.jsonschema.keyword.Assertion;
import jakarta.json.JsonArray;
import jakarta.json.JsonNumber;
import jakarta.json.JsonValue;
import jakarta.json.JsonValue.ValueType;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * <b>uniqueItems</b> : <i>Boolean</i><br/>
 * If this keyword is set to the boolean value true, the instance validates successfully if all of its elements are<br/>
 * unique.<br/>
 * <br/>
 * <ul>
 * <li>assertion</li>
 * </ul>
 *
 * source: https://www.learnjsonschema.com/2020-12/validation/uniqueitems/
 * spec: https://json-schema.org/draft/2020-12/json-schema-validation.html#section-6.4.3
 */
final class UniqueItemsKeyword implements Assertion {

    static final String NAME = "uniqueItems";
    private final boolean unique;

    public UniqueItemsKeyword(final boolean unique) {
        this.unique = unique;
    }

    @Override
    public <T extends Media<T>> T printOn(final T media) {
        return media.withValue(NAME, unique);
    }

    @Override
    public boolean isValidFor(final JsonValue instance) {
        return (
            !InstanceType.ARRAY.isInstance(instance) ||
            !unique ||
            new JsonArrayChecks(instance.asJsonArray()).areAllElementsUnique()
        );
    }

    @Override
    public boolean hasName(final String name) {
        return Objects.equals(NAME, name);
    }

    private static class JsonArrayChecks {

        private final JsonArray values;

        public JsonArrayChecks(final JsonArray values) {
            this.values = Objects.requireNonNull(values);
        }

        public boolean areAllElementsUnique() {
            final Set<JsonValueNumberEqualsFix> uniqueValues = new HashSet<>();
            return values.stream().map(JsonValueNumberEqualsFix::new).allMatch(uniqueValues::add);
        }
    }

    /*
    bad class name :( ->
    we must override equals for numbers. 1.0 and 1.00 must be equals.
    but neither new BigDecimal("1.0").equals(new BigDecimal("1.00")
    nor Json.create(new BigDecimal("1.0")).equals(Json.create(new BigDecimal("1.00"))
    are.
     */
    static class JsonValueNumberEqualsFix {

        private final JsonValue value;

        public JsonValueNumberEqualsFix(final JsonValue value) {
            this.value = value;
        }

        @Override
        public int hashCode() {
            final int result;
            if (isNumber()) {
                result = normalizeValue().hashCode();
            } else {
                result = value.hashCode();
            }
            return result;
        }

        @Override
        public boolean equals(final Object obj) {
            return (
                this == obj || (obj != null && getClass() == obj.getClass() && isEquals((JsonValueNumberEqualsFix) obj))
            );
        }

        @SuppressWarnings("BigDecimalEquals")
        private boolean isEquals(final JsonValueNumberEqualsFix other) {
            final boolean result;
            if (isNumber() && other.isNumber()) {
                result = normalizeValue().equals(other.normalizeValue());
            } else if (isNumber()) {
                result = false;
            } else {
                result = value.equals(other.value);
            }
            return result;
        }

        private BigDecimal normalizeValue() {
            return ((JsonNumber) value).bigDecimalValue().stripTrailingZeros();
        }

        private boolean isNumber() {
            return value.getValueType() == ValueType.NUMBER;
        }
    }
}
