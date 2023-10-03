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
import io.github.sebastiantoepfer.jsonschema.keyword.Identifier;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import io.github.sebastiantoepfer.jsonschema.keyword.KeywordType;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import java.net.URI;
import java.util.Objects;

/**
 *
 * see: https://json-schema.org/draft/2020-12/json-schema-core.html#name-the-id-keyword
 */
final class IdKeywordType implements KeywordType {

    @Override
    public String name() {
        return "$id";
    }

    @Override
    public Keyword createKeyword(final JsonSchema schema, final JsonValue value) {
        if (InstanceType.STRING.isInstance(value)) {
            return new IdKeyword((JsonString) value);
        } else {
            throw new IllegalArgumentException("must be a string!");
        }
    }

    private class IdKeyword implements Identifier {

        private final URI uri;

        private IdKeyword(final JsonString uri) {
            this.uri = URI.create(uri.getString());
        }

        @Override
        public URI asUri() {
            return uri;
        }

        @Override
        public boolean hasName(final String name) {
            return Objects.equals(name(), name);
        }
    }
}
