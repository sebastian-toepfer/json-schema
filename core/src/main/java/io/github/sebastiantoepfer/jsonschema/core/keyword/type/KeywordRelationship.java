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
import io.github.sebastiantoepfer.jsonschema.keyword.Annotation;
import io.github.sebastiantoepfer.jsonschema.keyword.Applicator;
import io.github.sebastiantoepfer.jsonschema.keyword.Assertion;
import io.github.sebastiantoepfer.jsonschema.keyword.Identifier;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import io.github.sebastiantoepfer.jsonschema.keyword.ReservedLocation;
import java.util.Collection;
import java.util.Objects;

abstract class KeywordRelationship implements Keyword {

    private final String name;

    protected KeywordRelationship(final String name) {
        this.name = Objects.requireNonNull(name);
    }

    @Override
    public final Collection<Keyword.KeywordCategory> categories() {
        return delegate().categories();
    }

    @Override
    public final boolean hasName(final String name) {
        return Objects.equals(this.name, name);
    }

    @Override
    public final <T extends Media<T>> T printOn(final T media) {
        return delegate().printOn(media);
    }

    @Override
    public final Identifier asIdentifier() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final Assertion asAssertion() {
        return delegate().asAssertion();
    }

    @Override
    public final Annotation asAnnotation() {
        return delegate().asAnnotation();
    }

    @Override
    public final Applicator asApplicator() {
        return delegate().asApplicator();
    }

    @Override
    public final ReservedLocation asReservedLocation() {
        throw new UnsupportedOperationException();
    }

    protected abstract Keyword delegate();
}
