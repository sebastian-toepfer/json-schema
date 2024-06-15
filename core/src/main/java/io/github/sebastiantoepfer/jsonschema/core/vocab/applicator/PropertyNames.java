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
import io.github.sebastiantoepfer.jsonschema.InstanceType;
import io.github.sebastiantoepfer.jsonschema.JsonSchema;
import io.github.sebastiantoepfer.jsonschema.keyword.Applicator;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import java.util.Objects;

/**
 * <b>propertyNames</b> : <i>Schema</i><br/>
 * Validation succeeds if the schema validates against every property name in the instance.<br/>
 * <br/>
 * <b>Kind</b><br/>
 * <ul>
 * <li>Applicator</li>
 * </li>
 * </ul>
 *
 * source: https://www.learnjsonschema.com/2020-12/applicator/propertynames/
 * spec: https://json-schema.org/draft/2020-12/json-schema-core.html#section-10.3.2.4
 */
final class PropertyNames implements Applicator {

    static final String NAME = "propertyNames";
    private final JsonProvider provider;
    private final JsonSchema names;

    public PropertyNames(final JsonProvider provider, final JsonSchema names) {
        this.provider = Objects.requireNonNull(provider);
        this.names = Objects.requireNonNull(names);
    }

    @Override
    public <T extends Media<T>> T printOn(final T media) {
        return media.withValue(NAME, names);
    }

    @Override
    public boolean hasName(final String name) {
        return Objects.equals(NAME, name);
    }

    @Override
    public boolean applyTo(final JsonValue instance) {
        return !InstanceType.OBJECT.isInstance(instance) || allProperyNamesMatchesSchema(instance.asJsonObject());
    }

    private boolean allProperyNamesMatchesSchema(final JsonObject obj) {
        return obj.keySet().stream().map(provider::createValue).allMatch(names::applyTo);
    }
}
