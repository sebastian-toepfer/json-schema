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
package io.github.sebastiantoepfer.jsonschema.core.keyword.type;

import io.github.sebastiantoepfer.ddd.common.Media;
import io.github.sebastiantoepfer.ddd.common.Printable;
import io.github.sebastiantoepfer.jsonschema.JsonSchema;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class NamedJsonSchemas implements Printable {

    private final Map<String, JsonSchema> schemas;

    public NamedJsonSchemas(final Map<String, ? extends JsonSchema> schemas) {
        this.schemas = Map.copyOf(schemas);
    }

    @Override
    public <T extends Media<T>> T printOn(final T media) {
        return schemas
            .entrySet()
            .stream()
            .reduce(media, (m, e) -> m.withValue(e.getKey(), e.getValue()), (l, r) -> null);
    }

    public Optional<JsonSchema> schemaWithName(final String name) {
        return Optional.ofNullable(schemas.get(name));
    }

    public boolean hasSchemaWithName(final String name) {
        return schemas.containsKey(name);
    }

    public Set<Map.Entry<String, JsonSchema>> schemas() {
        return schemas.entrySet();
    }
}
