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
package io.github.sebastiantoepfer.jsonschema.core.vocab.core;

import io.github.sebastiantoepfer.jsonschema.JsonSchema;
import io.github.sebastiantoepfer.jsonschema.JsonSchemas;
import jakarta.json.Json;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import java.io.IOException;
import java.net.URI;

interface SchemaRegistry {
    JsonSchema schemaForUrl(URI uri) throws IOException;

    class DefaultSchemaRegistry implements SchemaRegistry {

        private JsonSchema schema;

        public DefaultSchemaRegistry() {
            schema = JsonSchemas.load(JsonValue.FALSE);
        }

        @Override
        public JsonSchema schemaForUrl(final URI uri) throws IOException {
            return schema;
        }
    }

    class RemoteSchemaRegistry implements SchemaRegistry {

        @Override
        public JsonSchema schemaForUrl(final URI uri) throws IOException {
            try (JsonReader reader = Json.createReader(uri.toURL().openStream())) {
                return JsonSchemas.load(reader.readValue());
            }
        }
    }
}
