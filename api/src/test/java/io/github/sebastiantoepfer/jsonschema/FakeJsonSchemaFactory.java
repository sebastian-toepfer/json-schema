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
package io.github.sebastiantoepfer.jsonschema;

import io.github.sebastiantoepfer.ddd.common.Media;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import io.github.sebastiantoepfer.jsonschema.spi.JsonSchemaFactory;
import jakarta.json.JsonPointer;
import jakarta.json.JsonValue;
import java.util.Optional;
import java.util.stream.Stream;

public final class FakeJsonSchemaFactory implements JsonSchemaFactory {

    @Override
    public JsonSchema create(final JsonValue schema) {
        return new FakeJsonSchema();
    }

    static class FakeJsonSchema implements JsonSchema {

        @Override
        public Validator validator() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean applyTo(JsonValue instance) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Optional<Keyword> keywordByName(String name) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public JsonValue.ValueType getValueType() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Optional<JsonSubSchema> subSchema(String name) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Stream<JsonSubSchema> subSchemas(String name) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Optional<JsonSubSchema> subSchema(JsonPointer pointer) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public <T extends Media<T>> T printOn(final T media) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
