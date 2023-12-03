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
package io.github.sebastiantoepfer.jsonschema.core.vocab.applicator;

import io.github.sebastiantoepfer.jsonschema.InstanceType;
import io.github.sebastiantoepfer.jsonschema.JsonSchema;
import io.github.sebastiantoepfer.jsonschema.JsonSchemas;
import io.github.sebastiantoepfer.jsonschema.JsonSubSchema;
import io.github.sebastiantoepfer.jsonschema.Validator;
import io.github.sebastiantoepfer.jsonschema.keyword.Annotation;
import io.github.sebastiantoepfer.jsonschema.keyword.Applicator;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import io.github.sebastiantoepfer.jsonschema.keyword.KeywordType;
import jakarta.json.JsonArray;
import jakarta.json.JsonNumber;
import jakarta.json.JsonValue;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

final class ItemsKeywordType implements KeywordType {

    @Override
    public String name() {
        return "items";
    }

    @Override
    public Keyword createKeyword(final JsonSchema schema) {
        return new ItemsKeyword(schema, JsonSchemas.load(schema.asJsonObject().get(name())));
    }

    private class ItemsKeyword implements Applicator, Annotation, JsonSubSchema {

        private final JsonSchema owner;
        private final JsonSchema schema;

        public ItemsKeyword(final JsonSchema owner, final JsonSchema schema) {
            this.owner = Objects.requireNonNull(owner);
            this.schema = Objects.requireNonNull(schema);
        }

        @Override
        public JsonSchema owner() {
            return owner;
        }

        @Override
        public Validator validator() {
            return schema.validator();
        }

        @Override
        public ValueType getValueType() {
            return schema.getValueType();
        }

        @Override
        public Optional<Keyword> keywordByName(final String name) {
            return schema.keywordByName(name);
        }

        @Override
        public boolean hasName(final String name) {
            return Objects.equals(name(), name);
        }

        @Override
        public Collection<KeywordCategory> categories() {
            return List.of(KeywordCategory.APPLICATOR, KeywordCategory.ANNOTATION);
        }

        @Override
        public JsonValue valueFor(final JsonValue value) {
            final JsonValue result;
            if (appliesToAnyFor(value.asJsonArray())) {
                result = JsonValue.TRUE;
            } else {
                result = JsonValue.FALSE;
            }
            return result;
        }

        private boolean appliesToAnyFor(final JsonArray value) {
            return startIndexFor(value) == -1;
        }

        @Override
        public boolean applyTo(final JsonValue instance) {
            return !InstanceType.ARRAY.isInstance(instance) || matchesSchema(instance.asJsonArray());
        }

        private boolean matchesSchema(final JsonArray items) {
            final Validator itemValidator = validator();
            return items.stream().skip(startIndexFor(items) + 1L).allMatch(itemValidator::isValid);
        }

        private int startIndexFor(final JsonArray value) {
            return owner()
                .keywordByName("prefixItems")
                .map(Keyword::asAnnotation)
                .map(anno -> anno.valueFor(value))
                .map(v -> new MaxIndexCalculator(value, v))
                .map(MaxIndexCalculator::maxIndex)
                .orElse(-1);
        }

        private static class MaxIndexCalculator {

            private final JsonArray array;
            private final JsonValue index;

            public MaxIndexCalculator(final JsonArray array, final JsonValue index) {
                this.array = array;
                this.index = index;
            }

            int maxIndex() {
                final int result;
                if (index == JsonValue.TRUE) {
                    result = array.size();
                } else {
                    result = ((JsonNumber) index).intValue();
                }
                return result;
            }
        }
    }
}
