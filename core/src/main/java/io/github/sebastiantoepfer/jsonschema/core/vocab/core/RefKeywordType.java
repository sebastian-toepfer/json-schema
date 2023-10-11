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
package io.github.sebastiantoepfer.jsonschema.core.vocab.core;

import io.github.sebastiantoepfer.jsonschema.InstanceType;
import io.github.sebastiantoepfer.jsonschema.JsonSchema;
import io.github.sebastiantoepfer.jsonschema.JsonSchemas;
import io.github.sebastiantoepfer.jsonschema.keyword.Applicator;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import io.github.sebastiantoepfer.jsonschema.keyword.KeywordType;
import jakarta.json.Json;
import jakarta.json.JsonPointer;
import jakarta.json.JsonReader;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;

/**
 *
 * see: https://json-schema.org/draft/2020-12/json-schema-core.html#name-direct-references-with-ref
 */
final class RefKeywordType implements KeywordType {

    @Override
    public String name() {
        return "$ref";
    }

    @Override
    public Keyword createKeyword(final JsonSchema schema, final JsonValue value) {
        if (InstanceType.STRING.isInstance(value)) {
            return new RefKeyword(schema, (JsonString) value);
        } else {
            throw new IllegalArgumentException("must be a string");
        }
    }

    private class RefKeyword implements Applicator {

        private final JsonSchema schema;
        private final URI uri;

        private RefKeyword(final JsonSchema schema, final JsonString uri) {
            this.schema = schema;
            this.uri = URI.create(uri.getString());
        }

        @Override
        public boolean applyTo(final JsonValue instance) {
            return retrieveJsonSchema().validator().isValid(instance);
        }

        @Override
        public boolean hasName(final String name) {
            return Objects.equals(name(), name);
        }

        private JsonSchema retrieveJsonSchema() {
            final JsonValue json;
            try {
                if (isRemote()) {
                    json = retrieveValueFromRemoteLocation();
                } else {
                    json = retrievValueFromLocalSchema();
                }
                return JsonSchemas.load(json);
            } catch (IOException ex) {
                throw new IllegalStateException("can not load schema!", ex);
            }
        }

        private JsonValue retrievValueFromLocalSchema() throws IOException {
            final JsonPointer pointer = createPointer();
            if (schema.getValueType() == JsonValue.ValueType.OBJECT && pointer.containsValue(schema.asJsonObject())) {
                return pointer.getValue(schema.asJsonObject());
            } else {
                throw new IOException("can not find referenced value.");
            }
        }

        private JsonPointer createPointer() {
            final String fragment = uri.getFragment();
            final JsonPointer pointer;
            if (fragment.startsWith("/")) {
                pointer = Json.createPointer(fragment);
            } else {
                pointer = Json.createPointer("/".concat(fragment));
            }
            return pointer;
        }

        private JsonValue retrieveValueFromRemoteLocation() throws IOException {
            try (final JsonReader reader = Json.createReader(uri.toURL().openStream())) {
                return reader.readValue();
            }
        }

        private boolean isRemote() {
            return uri.getScheme() != null;
        }
    }
}
