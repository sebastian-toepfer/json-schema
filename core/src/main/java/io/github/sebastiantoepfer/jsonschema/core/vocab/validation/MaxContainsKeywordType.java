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
import io.github.sebastiantoepfer.jsonschema.JsonSchema;
import io.github.sebastiantoepfer.jsonschema.core.keyword.type.IntegerKeywordType;
import io.github.sebastiantoepfer.jsonschema.keyword.Assertion;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import io.github.sebastiantoepfer.jsonschema.keyword.KeywordType;
import jakarta.json.JsonArray;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import java.math.BigInteger;
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
final class MaxContainsKeywordType implements KeywordType {

    private final JsonProvider jsonContext;

    public MaxContainsKeywordType(final JsonProvider jsonContext) {
        this.jsonContext = jsonContext;
    }

    @Override
    public String name() {
        return "maxContains";
    }

    @Override
    public Keyword createKeyword(final JsonSchema schema) {
        return new IntegerKeywordType(
            jsonContext,
            name(),
            value -> new MaxContainsKeyword(schema, value)
        ).createKeyword(schema);
    }

    private final class MaxContainsKeyword implements Assertion {

        private final JsonSchema owner;
        private final BigInteger maxContains;

        public MaxContainsKeyword(final JsonSchema owner, final BigInteger maxContains) {
            this.owner = Objects.requireNonNull(owner);
            this.maxContains = Objects.requireNonNull(maxContains);
        }

        @Override
        public boolean hasName(final String name) {
            return Objects.equals(name(), name);
        }

        @Override
        public <T extends Media<T>> T printOn(final T media) {
            return media.withValue(name(), maxContains);
        }

        @Override
        public boolean isValidFor(final JsonValue instance) {
            return (
                !InstanceType.ARRAY.isInstance(instance) ||
                owner
                    .keywordByName("contains")
                    .map(Keyword::asAnnotation)
                    .map(annotation -> annotation.valueFor(instance))
                    .map(contains -> isValidFor(contains, instance.asJsonArray()))
                    .orElse(true)
            );
        }

        private boolean isValidFor(final JsonValue containing, final JsonArray values) {
            final boolean result;
            if (JsonValue.TRUE.equals(containing)) {
                result = values.size() <= maxContains.intValue();
            } else {
                result = containing.asJsonArray().size() <= maxContains.intValue();
            }
            return result;
        }
    }
}
