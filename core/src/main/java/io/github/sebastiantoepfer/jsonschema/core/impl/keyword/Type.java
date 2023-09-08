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
package io.github.sebastiantoepfer.jsonschema.core.impl.keyword;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import io.github.sebastiantoepfer.jsonschema.core.ConstraintViolation;
import io.github.sebastiantoepfer.jsonschema.core.InstanceType;
import io.github.sebastiantoepfer.jsonschema.core.impl.constraint.AnyConstraint;
import io.github.sebastiantoepfer.jsonschema.core.impl.constraint.Constraint;
import jakarta.json.JsonArray;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import java.util.Collection;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

/**
 * see: https://json-schema.org/understanding-json-schema/reference/type.html
 */
final class Type implements ConstraintAssertion, Constraint<JsonValue> {

    private final JsonValue definition;

    public Type(final JsonValue definition) {
        this.definition = Objects.requireNonNull(definition);
    }

    @Override
    public boolean hasName(final String name) {
        return Objects.equals("type", name);
    }

    @Override
    public Collection<ConstraintViolation> violationsBy(final JsonValue value) {
        return new JsonMappedTypeConstaint(definition).violationsBy(value);
    }

    private static final class JsonMappedTypeConstaint implements Constraint<JsonValue> {

        private final JsonValue definition;

        public JsonMappedTypeConstaint(final JsonValue definition) {
            this.definition = Objects.requireNonNull(definition);
        }

        @Override
        public Collection<ConstraintViolation> violationsBy(final JsonValue value) {
            final Constraint<JsonValue> typeContraint =
                switch (definition.getValueType()) {
                    case STRING -> new JsonStringTypeConstraint((JsonString) definition);
                    default -> new JsonArrayTypeConstraint(definition.asJsonArray());
                };
            return typeContraint.violationsBy(value);
        }
    }

    private static final class JsonArrayTypeConstraint implements Constraint<JsonValue> {

        private final JsonArray types;

        public JsonArrayTypeConstraint(final JsonArray types) {
            this.types = Objects.requireNonNull(types);
        }

        @Override
        public Collection<ConstraintViolation> violationsBy(final JsonValue value) {
            return types
                .stream()
                .map(JsonMappedTypeConstaint::new)
                .collect(collectingAndThen(toList(), AnyConstraint::new))
                .violationsBy(value);
        }
    }

    private static final class JsonStringTypeConstraint implements Constraint<JsonValue> {

        private final String type;

        public JsonStringTypeConstraint(final JsonString type) {
            this.type = Objects.requireNonNull(type).getString().toUpperCase(Locale.US);
        }

        @Override
        public Collection<ConstraintViolation> violationsBy(final JsonValue value) {
            final InstanceType instanceType = InstanceType.valueOf(type);
            final Collection<ConstraintViolation> result;
            if (instanceType.isInstance(value)) {
                result = Set.of();
            } else {
                result = Set.of(new ConstraintViolation());
            }
            return result;
        }
    }
}
