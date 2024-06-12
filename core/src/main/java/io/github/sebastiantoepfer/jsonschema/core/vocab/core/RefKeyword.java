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

import io.github.sebastiantoepfer.ddd.common.Media;
import io.github.sebastiantoepfer.jsonschema.JsonSchema;
import io.github.sebastiantoepfer.jsonschema.JsonSchemas;
import io.github.sebastiantoepfer.jsonschema.keyword.Applicator;
import jakarta.json.Json;
import jakarta.json.JsonPointer;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;

/**
 * <b>$ref</b> : <i>URI Reference</i><br/>
 * This keyword is used to reference a statically identified schema.<br/>
 * <br/>
 * <ul>
 * <li>Applicator</li>
 * </ul>
 *
 * source: https://www.learnjsonschema.com/2020-12/core/ref/
 * spec: https://json-schema.org/draft/2020-12/json-schema-core.html#section-8.2.3.1
 */
final class RefKeyword implements Applicator {

    static final String NAME = "$ref";
    private final JsonSchema schema;
    private final URI uri;

    public RefKeyword(final JsonSchema schema, final URI uri) {
        this.schema = Objects.requireNonNull(schema);
        this.uri = Objects.requireNonNull(uri);
    }

    @Override
    public <T extends Media<T>> T printOn(final T media) {
        return media.withValue(NAME, uri.toString());
    }

    @Override
    public boolean applyTo(final JsonValue instance) {
        return retrieveJsonSchema().validator().isValid(instance);
    }

    @Override
    public boolean hasName(final String name) {
        return Objects.equals(NAME, name);
    }

    private JsonSchema retrieveJsonSchema() {
        final JsonSchema json;
        try {
            if (isRemote()) {
                json = retrieveValueFromRemoteLocation();
            } else {
                json = retrieveValueFromLocalSchema();
            }
            return json;
        } catch (IOException ex) {
            throw new IllegalStateException("can not load schema!", ex);
        }
    }

    private JsonSchema retrieveValueFromLocalSchema() throws IOException {
        return schema.rootSchema().subSchema(createPointer()).orElseThrow();
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

    private JsonSchema retrieveValueFromRemoteLocation() throws IOException {
        try (final JsonReader reader = Json.createReader(uri.toURL().openStream())) {
            return JsonSchemas.load(reader.readValue());
        }
    }

    private boolean isRemote() {
        return uri.getScheme() != null;
    }
}
