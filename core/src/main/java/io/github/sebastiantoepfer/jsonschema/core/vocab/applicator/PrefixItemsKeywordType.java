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
import io.github.sebastiantoepfer.jsonschema.keyword.Annotation;
import io.github.sebastiantoepfer.jsonschema.keyword.Applicator;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import io.github.sebastiantoepfer.jsonschema.keyword.KeywordType;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonValue;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * https://json-schema.org/draft/2020-12/json-schema-core#section-10.3.1.1
 * https://www.learnjsonschema.com/2020-12/applicator/prefixitems/
 */
final class PrefixItemsKeywordType implements KeywordType {

    @Override
    public String name() {
        return "prefixItems";
    }

    @Override
    public Keyword createKeyword(final JsonSchema schema, final JsonValue value) {
        if (InstanceType.ARRAY.isInstance(value)) {
            return new PrefixItemsKeyword(value.asJsonArray());
        } else {
            throw new IllegalArgumentException("must be a non empty array!");
        }
    }

    private class PrefixItemsKeyword implements Annotation, Applicator {

        private final List<JsonSchema> schemas;

        public PrefixItemsKeyword(final JsonArray schemas) {
            this.schemas = schemas.stream().map(JsonSchemas::load).toList();
        }

        @Override
        public Collection<KeywordCategory> categories() {
            return List.of(KeywordCategory.ANNOTATION, KeywordCategory.APPLICATOR);
        }

        @Override
        public boolean hasName(final String name) {
            return Objects.equals(name(), name);
        }

        @Override
        public JsonValue value() {
            return Json.createValue(schemas.size() - 1);
        }

        @Override
        public boolean applyTo(final JsonValue instance) {
            return !InstanceType.ARRAY.isInstance(instance) || matchesSchemas(instance.asJsonArray());
        }

        private boolean matchesSchemas(final JsonArray instance) {
            boolean result = true;
            for (int i = 0; i < Math.min(schemas.size(), instance.size()); i++) {
                result &= schemas.get(i).validator().validate(instance.get(i)).isEmpty();
                if (!result) {
                    break;
                }
            }
            return result;
        }
    }
}
