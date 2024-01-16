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

import io.github.sebastiantoepfer.ddd.common.Media;
import io.github.sebastiantoepfer.jsonschema.keyword.Applicator;
import jakarta.json.JsonValue;
import java.net.URI;
import java.util.Objects;

/**
 * <b>$dynamicRef</b> : <i>URI Reference</i><br/>
 * This keyword is used to reference an identified schema, deferring the full resolution until runtime, at which<br/>
 * point it is resolved each time it is encountered while evaluating an instance.<br/>
 * <br/>
 * <ul>
 * <li>applicator</li>
 * </ul>
 *
 * source: https://www.learnjsonschema.com/2020-12/core/dynamicref/
 * spec: https://json-schema.org/draft/2020-12/json-schema-core.html#section-8.2.3.1
 */
final class DynamicRefKeyword implements Applicator {

    static final String NAME = "$dynamicRef";
    private final URI uri;

    public DynamicRefKeyword(final URI uri) {
        this.uri = Objects.requireNonNull(uri);
    }

    @Override
    public <T extends Media<T>> T printOn(final T media) {
        return media.withValue(NAME, uri.toString());
    }

    @Override
    public boolean applyTo(final JsonValue instance) {
        return true;
    }

    @Override
    public boolean hasName(final String name) {
        return Objects.equals(NAME, name);
    }
}
