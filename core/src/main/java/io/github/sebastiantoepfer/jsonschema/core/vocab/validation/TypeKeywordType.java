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
import io.github.sebastiantoepfer.jsonschema.core.codition.Condition;
import io.github.sebastiantoepfer.jsonschema.keyword.Assertion;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import io.github.sebastiantoepfer.jsonschema.keyword.KeywordType;
import jakarta.json.JsonArray;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import java.util.Locale;
import java.util.Objects;

/**
 * see: https://json-schema.org/understanding-json-schema/reference/type.html
 */
final class TypeKeywordType implements KeywordType {

    @Override
    public String name() {
        return "type";
    }

    @Override
    public Keyword createKeyword(final JsonSchema schema) {
        return new TypeKeyword(schema.asJsonObject().get(name()));
    }

    private final class TypeKeyword implements Assertion {

        private final JsonValue definition;

        public TypeKeyword(final JsonValue definition) {
            this.definition = Objects.requireNonNull(definition);
        }

        @Override
        public boolean hasName(final String name) {
            return Objects.equals(name(), name);
        }

        @Override
        public boolean isValidFor(final JsonValue instance) {
            return new JsonMappedTypeConstaint(definition).isFulfilledBy(instance);
        }

        private static final class JsonMappedTypeConstaint implements Condition<JsonValue> {

            private final JsonValue definition;

            public JsonMappedTypeConstaint(final JsonValue definition) {
                this.definition = Objects.requireNonNull(definition);
            }

            @Override
            public boolean isFulfilledBy(final JsonValue value) {
                final Condition<JsonValue> typeContraint =
                    switch (definition.getValueType()) {
                        case STRING -> new JsonStringTypeConstraint((JsonString) definition);
                        default -> new JsonArrayTypeConstraint(definition.asJsonArray());
                    };
                return typeContraint.isFulfilledBy(value);
            }
        }

        private static final class JsonArrayTypeConstraint implements Condition<JsonValue> {

            private final JsonArray types;

            public JsonArrayTypeConstraint(final JsonArray types) {
                this.types = Objects.requireNonNull(types);
            }

            @Override
            public boolean isFulfilledBy(final JsonValue value) {
                return types.stream().map(JsonMappedTypeConstaint::new).anyMatch(c -> c.isFulfilledBy(value));
            }
        }

        private static final class JsonStringTypeConstraint implements Condition<JsonValue> {

            private final String type;

            public JsonStringTypeConstraint(final JsonString type) {
                this.type = Objects.requireNonNull(type).getString().toUpperCase(Locale.US);
            }

            @Override
            public boolean isFulfilledBy(final JsonValue value) {
                return InstanceType.valueOf(type).isInstance(value);
            }
        }
    }
}
