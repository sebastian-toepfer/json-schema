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
import io.github.sebastiantoepfer.jsonschema.keyword.Annotation;
import io.github.sebastiantoepfer.jsonschema.keyword.Applicator;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonValue;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * <b>prefixItems</b> : <i>Array<Schema></i><br/>
 * Validation succeeds if each element of the instance validates against the schema at the same position, if any.<br/>
 * <br/>
 * <b>kind</b>
 * <ul>
 * <li>Applicator</li>
 * <li>Annotation<br/>
 * This keyword produces an annotation value which is the largest index to which this keyword applied a subschema.
 * The value MAY be a boolean true if a subschema was applied to every index of the instance.
 * </li>
 * </ul>
 *
 * source: https://www.learnjsonschema.com/2020-12/applicator/prefixitems/
 * spec: https://json-schema.org/draft/2020-12/json-schema-core.html#section-10.3.1.1
 */
class PrefixItemsKeyword implements Annotation, Applicator {

    static final String NAME = "prefixItems";
    private final List<JsonSchema> schemas;

    public PrefixItemsKeyword(final List<JsonSchema> schemas) {
        this.schemas = List.copyOf(schemas);
    }

    @Override
    public <T extends Media<T>> T printOn(final T media) {
        return media.withValue(NAME, schemas);
    }

    @Override
    public Collection<KeywordCategory> categories() {
        return List.of(KeywordCategory.ANNOTATION, KeywordCategory.APPLICATOR);
    }

    @Override
    public boolean hasName(final String name) {
        return Objects.equals(NAME, name);
    }

    @Override
    public JsonValue valueFor(final JsonValue value) {
        final JsonValue result;
        if (value.asJsonArray().size() == schemas.size()) {
            result = JsonValue.TRUE;
        } else {
            result = Json.createValue(Math.min(value.asJsonArray().size(), schemas.size()) - 1);
        }
        return result;
    }

    @Override
    public boolean applyTo(final JsonValue instance) {
        return !InstanceType.ARRAY.isInstance(instance) || matchesSchemas(instance.asJsonArray());
    }

    private boolean matchesSchemas(final JsonArray instance) {
        boolean result = true;
        for (int i = 0; i < Math.min(schemas.size(), instance.size()); i++) {
            result &= schemas.get(i).validator().isValid(instance.get(i));
            if (!result) {
                break;
            }
        }
        return result;
    }
}
