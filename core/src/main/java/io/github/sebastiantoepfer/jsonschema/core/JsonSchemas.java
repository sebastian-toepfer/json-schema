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
package io.github.sebastiantoepfer.jsonschema.core;

import jakarta.json.Json;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import java.io.StringReader;

public final class JsonSchemas {

    public static JsonSchema load(final String schema) {
        try (JsonReader reader = Json.createReader(new StringReader(schema))) {
            return load(reader.readValue());
        }
    }

    public static JsonSchema load(final JsonValue schema) {
        final JsonSchema result;
        if (schema == JsonValue.TRUE) {
            result = new TrueSchema();
        } else if (schema == JsonValue.FALSE) {
            result = new FalseSchema();
        } else if (schema.equals(JsonValue.EMPTY_JSON_OBJECT)) {
            result = new EmptyJsonSchema();
        } else {
            throw new IllegalArgumentException("not supported yet!");
        }
        return result;
    }

    private JsonSchemas() {}
}
