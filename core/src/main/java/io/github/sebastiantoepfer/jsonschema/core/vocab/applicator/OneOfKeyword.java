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
package io.github.sebastiantoepfer.jsonschema.core.vocab.applicator;

import io.github.sebastiantoepfer.ddd.common.Media;
import io.github.sebastiantoepfer.jsonschema.JsonSchema;
import io.github.sebastiantoepfer.jsonschema.keyword.Applicator;
import jakarta.json.JsonValue;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * <b>oneOf</b> : <i>Array<Schema></i><br/>
 * An instance validates successfully against this keyword if it validates successfully against exactly one schema
 * defined by this keyword’s value.<br/>
 * <br/>
 * <b>kind</b>
 * <ul>
 * <li>Applicator</li>
 * </ul>
 *
 * source: https://www.learnjsonschema.com/2020-12/applicator/oneof/
 * spec: https://json-schema.org/draft/2020-12/json-schema-core.html#section-10.2.1.3
 */
final class OneOfKeyword implements Applicator {

    static final String NAME = "oneOf";
    private final Collection<JsonSchema> schemas;

    public OneOfKeyword(final Collection<JsonSchema> schemas) {
        this.schemas = List.copyOf(schemas);
    }

    @Override
    public boolean applyTo(final JsonValue instance) {
        return schemas.stream().map(JsonSchema::validator).filter(v -> v.isValid(instance)).limit(2).count() == 1;
    }

    @Override
    public boolean hasName(final String name) {
        return Objects.equals(NAME, name);
    }

    @Override
    public <T extends Media<T>> T printOn(final T media) {
        return media.withValue(NAME, schemas);
    }
}
