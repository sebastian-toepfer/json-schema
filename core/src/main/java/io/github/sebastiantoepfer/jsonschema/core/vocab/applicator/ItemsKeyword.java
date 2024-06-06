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
import jakarta.json.JsonArray;
import jakarta.json.JsonNumber;
import jakarta.json.JsonValue;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * <b>items</b> : <i>Schema</i><br/>
 * Validation succeeds if each element of the instance not covered by <i>prefixItems</i> validates against this
 * schema.<br/>
 * <br/>
 * <b>kind</b><br/>
 * <ul>
 * <li>Applicator</li>
 * <li>Annotation<br/>
 * If this keyword is applied to any instance element, it produces an annotation value of true.
 * </li>
 * </ul>
 *
 * source: https://www.learnjsonschema.com/2020-12/applicator/items/
 * sepc: https://json-schema.org/draft/2020-12/json-schema-core.html#section-10.3.1.2
 */
final class ItemsKeyword implements Applicator, Annotation {

    static final String NAME = "items";
    private final Collection<Annotation> affectedBys;
    private final JsonSchema schema;

    public ItemsKeyword(final Collection<Annotation> affectedBys, final JsonSchema schema) {
        this.affectedBys = List.copyOf(affectedBys);
        this.schema = Objects.requireNonNull(schema);
    }

    @Override
    public <T extends Media<T>> T printOn(final T media) {
        return media.withValue(NAME, schema);
    }

    @Override
    public boolean hasName(final String name) {
        return Objects.equals(NAME, name);
    }

    @Override
    public Collection<KeywordCategory> categories() {
        return List.of(KeywordCategory.APPLICATOR, KeywordCategory.ANNOTATION);
    }

    @Override
    public JsonValue valueFor(final JsonValue value) {
        final JsonValue result;
        if (appliesToAnyFor(value.asJsonArray())) {
            result = JsonValue.TRUE;
        } else {
            result = JsonValue.FALSE;
        }
        return result;
    }

    private boolean appliesToAnyFor(final JsonArray value) {
        return itemsForValidation(value).anyMatch(schema.validator()::isValid);
    }

    @Override
    public boolean applyTo(final JsonValue instance) {
        return !InstanceType.ARRAY.isInstance(instance) || applyTo(instance.asJsonArray());
    }

    private boolean applyTo(final JsonArray items) {
        return itemsForValidation(items).allMatch(schema.validator()::isValid);
    }

    private Stream<JsonValue> itemsForValidation(final JsonArray items) {
        return items.stream().skip(startIndexFor(items) + 1L);
    }

    private int startIndexFor(final JsonArray value) {
        return affectedBys
            .stream()
            .map(anno -> anno.valueFor(value))
            .map(v -> new MaxIndexCalculator(value, v))
            .mapToInt(MaxIndexCalculator::maxIndex)
            .sum();
    }

    private static class MaxIndexCalculator {

        private final JsonArray array;
        private final JsonValue index;

        public MaxIndexCalculator(final JsonArray array, final JsonValue index) {
            this.array = array;
            this.index = index;
        }

        int maxIndex() {
            final int result;
            if (index == JsonValue.TRUE) {
                result = array.size();
            } else {
                result = ((JsonNumber) index).intValue();
            }
            return result;
        }
    }
}
