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
package io.github.sebastiantoepfer.jsonschema.keyword;

import io.github.sebastiantoepfer.ddd.common.Media;
import io.github.sebastiantoepfer.ddd.media.json.JsonObjectPrintable;
import jakarta.json.Json;
import jakarta.json.JsonValue;
import java.util.Objects;

/**
 * A JSON Schema MAY contain properties which are not schema keywords. Unknown keywords SHOULD be treated as
 * annotations, where the value of the keyword is the value of the annotation
 *
 * see: https://json-schema.org/draft/2020-12/json-schema-core.html#name-json-schema-objects-and-key
 */
public final class StaticAnnotation implements Annotation {

    private final String name;
    private final JsonValue value;

    public StaticAnnotation(final String name, final JsonValue value) {
        this.name = Objects.requireNonNull(name);
        this.value = Objects.requireNonNullElse(value, JsonValue.NULL);
    }

    @Override
    public boolean hasName(final String name) {
        return Objects.equals(this.name, name);
    }

    @Override
    public JsonValue valueFor(final JsonValue instance) {
        return value;
    }

    @Override
    public <T extends Media<T>> T printOn(final T media) {
        return new JsonObjectPrintable(Json.createObjectBuilder().add(name, value).build()).printOn(media);
    }
}
