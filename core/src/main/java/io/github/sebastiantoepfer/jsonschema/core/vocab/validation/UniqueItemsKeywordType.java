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
package io.github.sebastiantoepfer.jsonschema.core.vocab.validation;

import io.github.sebastiantoepfer.jsonschema.InstanceType;
import io.github.sebastiantoepfer.jsonschema.JsonSchema;
import io.github.sebastiantoepfer.jsonschema.keyword.Assertion;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import io.github.sebastiantoepfer.jsonschema.keyword.KeywordType;
import jakarta.json.JsonArray;
import jakarta.json.JsonNumber;
import jakarta.json.JsonValue;
import jakarta.json.JsonValue.ValueType;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

final class UniqueItemsKeywordType implements KeywordType {

    @Override
    public String name() {
        return "uniqueItems";
    }

    @Override
    public Keyword createKeyword(final JsonSchema schema, final JsonValue value) {
        if (InstanceType.BOOLEAN.isInstance(value)) {
            return new UniqueItemsKeyword(value.getValueType() == JsonValue.ValueType.TRUE);
        } else {
            throw new IllegalArgumentException("Value must be a boolean!");
        }
    }

    private class UniqueItemsKeyword implements Assertion {

        private final boolean unique;

        private UniqueItemsKeyword(final boolean unique) {
            this.unique = unique;
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
            return Objects.equals(name(), name);
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
