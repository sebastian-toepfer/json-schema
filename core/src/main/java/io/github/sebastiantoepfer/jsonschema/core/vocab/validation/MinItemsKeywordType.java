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
import jakarta.json.JsonNumber;
import jakarta.json.JsonValue;
import java.util.Objects;

final class MinItemsKeywordType implements KeywordType {

    @Override
    public String name() {
        return "minItems";
    }

    @Override
    public Keyword createKeyword(final JsonSchema schema) {
        return new MinItemsKeyword(schema.asJsonObject().getJsonNumber(name()));
    }

    private class MinItemsKeyword implements Assertion {

        private final int minItems;

        public MinItemsKeyword(final JsonNumber minItems) {
            this.minItems = minItems.intValue();
        }

        @Override
        public boolean isValidFor(final JsonValue instance) {
            return !InstanceType.ARRAY.isInstance(instance) || instance.asJsonArray().size() >= minItems;
        }

        @Override
        public boolean hasName(final String name) {
            return Objects.equals(name(), name);
        }
    }
}
