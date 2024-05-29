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

import static jakarta.json.stream.JsonCollectors.toJsonArray;

import io.github.sebastiantoepfer.ddd.common.Media;
import io.github.sebastiantoepfer.jsonschema.InstanceType;
import io.github.sebastiantoepfer.jsonschema.JsonSchema;
import io.github.sebastiantoepfer.jsonschema.keyword.Annotation;
import io.github.sebastiantoepfer.jsonschema.keyword.Applicator;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import jakarta.json.JsonArray;
import jakarta.json.JsonValue;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * <b>contains</b> : <i>Schema</i>
 * Validation succeeds if the instance contains an element that validates against this schema.<br/>
 * <br/>
 * <ul>
 * <li>applicator</li>
 * <li>annotation</li>
 * </ul>
 *
 * source: https://www.learnjsonschema.com/2020-12/applicator/contains/
 * spec: https://json-schema.org/draft/2020-12/json-schema-core.html#section-10.3.1.3
 */
final class ContainsKeyword implements Applicator, Annotation {

    static final String NAME = "contains";
    private final JsonSchema contains;
    private final List<Keyword> affectedBy;

    public ContainsKeyword(final List<Keyword> affectedBy, final JsonSchema contains) {
        this.affectedBy = List.copyOf(affectedBy);
        this.contains = Objects.requireNonNull(contains);
    }

    @Override
    public Collection<KeywordCategory> categories() {
        return List.of(KeywordCategory.APPLICATOR, KeywordCategory.ANNOTATION);
    }

    @Override
    public boolean hasName(final String name) {
        return Objects.equals(NAME, name);
    }

    @Override
    public <T extends Media<T>> T printOn(final T media) {
        return media.withValue(NAME, contains);
    }

    @Override
    public boolean applyTo(final JsonValue instance) {
        return !InstanceType.ARRAY.isInstance(instance) || contains(instance.asJsonArray());
    }

    private boolean contains(final JsonArray array) {
        return !affectedBy.isEmpty() || matchingValues(array).findAny().isPresent();
    }

    @Override
    public JsonValue valueFor(final JsonValue value) {
        final JsonValue result;
        if (InstanceType.ARRAY.isInstance(value)) {
            result = valueFor(value.asJsonArray());
        } else {
            result = JsonValue.FALSE;
        }
        return result;
    }

    private JsonValue valueFor(final JsonArray values) {
        final JsonValue result;
        final JsonArray matchingItems = matchingValues(values).collect(toJsonArray());
        if (matchingItems.size() == values.size() && !values.isEmpty()) {
            result = JsonValue.TRUE;
        } else {
            result = matchingItems;
        }
        return result;
    }

    Stream<JsonValue> matchingValues(final JsonArray values) {
        return values.stream().filter(contains.validator()::isValid);
    }
}
