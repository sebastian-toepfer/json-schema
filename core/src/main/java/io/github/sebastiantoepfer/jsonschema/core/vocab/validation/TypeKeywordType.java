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

import io.github.sebastiantoepfer.ddd.common.Media;
import io.github.sebastiantoepfer.jsonschema.InstanceType;
import io.github.sebastiantoepfer.jsonschema.JsonSchema;
import io.github.sebastiantoepfer.jsonschema.keyword.Assertion;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import io.github.sebastiantoepfer.jsonschema.keyword.KeywordType;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import java.util.Collection;
import java.util.List;
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
        final JsonValue typeDefinition = schema.asJsonObject().get(name());
        final Collection<String> allowedTypes =
            switch (typeDefinition.getValueType()) {
                case STRING -> List.of(((JsonString) typeDefinition).getString());
                case ARRAY -> typeDefinition
                    .asJsonArray()
                    .stream()
                    .map(JsonString.class::cast)
                    .map(JsonString::getString)
                    .toList();
                default -> throw new IllegalArgumentException();
            };

        return new TypeKeyword(allowedTypes);
    }

    private final class TypeKeyword implements Assertion {

        private final Collection<String> allowedTypes;

        public TypeKeyword(final Collection<String> allowedTypes) {
            this.allowedTypes = Objects.requireNonNull(allowedTypes);
        }

        @Override
        public <T extends Media<T>> T printOn(final T media) {
            final T result;
            if (allowedTypes.size() == 1) {
                result = media.withValue(name(), allowedTypes.iterator().next());
            } else {
                result = media.withValue(name(), allowedTypes);
            }
            return result;
        }

        @Override
        public boolean hasName(final String name) {
            return Objects.equals(name(), name);
        }

        @Override
        public boolean isValidFor(final JsonValue instance) {
            return allowedTypes
                .stream()
                .map(String::toLowerCase)
                .map(InstanceType::fromString)
                .anyMatch(instanceType -> instanceType.isInstance(instance));
        }
    }
}
