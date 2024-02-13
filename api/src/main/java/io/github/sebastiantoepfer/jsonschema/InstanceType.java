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
package io.github.sebastiantoepfer.jsonschema;

import jakarta.json.JsonNumber;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

/**
 * see: http://json-schema.org/draft/2020-12/json-schema-core.html#name-instance-data-model
 */
public enum InstanceType implements JsonString {
    NULL(JsonValue.ValueType.NULL),
    BOOLEAN(JsonValue.ValueType.TRUE, JsonValue.ValueType.FALSE),
    OBJECT(JsonValue.ValueType.OBJECT),
    ARRAY(JsonValue.ValueType.ARRAY),
    NUMBER(JsonValue.ValueType.NUMBER),
    INTEGER() {
        @Override
        public boolean isInstance(final JsonValue value) {
            return value instanceof JsonNumber nr && isIntegral(nr.bigDecimalValue());
        }

        @SuppressWarnings("BigDecimalEquals")
        private boolean isIntegral(final BigDecimal decimal) {
            return (
                BigDecimal.ZERO.equals(decimal) ||
                BigDecimal.ONE.equals(decimal) ||
                BigDecimal.TEN.equals(decimal) ||
                decimal.stripTrailingZeros().scale() <= 0
            );
        }
    },
    STRING(JsonValue.ValueType.STRING);

    public static InstanceType fromString(final String name) {
        return valueOf(name.toUpperCase(Locale.US));
    }

    @SuppressWarnings("ImmutableEnumChecker")
    private final Collection<JsonValue.ValueType> validTypes;

    InstanceType(final JsonValue.ValueType... validTypes) {
        this.validTypes = Arrays.asList(validTypes);
    }

    public boolean isInstance(final JsonValue value) {
        return validTypes.contains(value.getValueType());
    }

    @Override
    public String toString() {
        return getString();
    }

    @Override
    public CharSequence getChars() {
        return getString();
    }

    @Override
    public String getString() {
        return name().toLowerCase(Locale.US);
    }

    @Override
    public ValueType getValueType() {
        return JsonValue.ValueType.STRING;
    }
}
