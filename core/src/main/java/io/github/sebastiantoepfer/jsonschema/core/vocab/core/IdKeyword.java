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
import io.github.sebastiantoepfer.jsonschema.keyword.Identifier;
import java.net.URI;
import java.util.Objects;

/**
 * <b>$id</b> : <i>URI Reference</i><br/>
 * This keyword declares an identifier for the schema resource.<br/>
 * <br/>
 * <ul>
 * <li>identifier</li>
 * </ul>
 *
 * source: https://www.learnjsonschema.com/2020-12/core/id/
 * spec: https://json-schema.org/draft/2020-12/json-schema-core.html#section-8.2.1
 */
final class IdKeyword implements Identifier {

    static final String NAME = "$id";
    private final URI uri;

    public IdKeyword(final URI uri) {
        this.uri = Objects.requireNonNull(uri);
    }

    @Override
    public <T extends Media<T>> T printOn(final T media) {
        return media.withValue(NAME, uri.toString());
    }

    @Override
    public URI asUri() {
        return uri;
    }

    @Override
    public boolean hasName(final String name) {
        return Objects.equals(NAME, name);
    }
}
