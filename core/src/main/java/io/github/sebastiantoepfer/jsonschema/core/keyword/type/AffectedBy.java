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

import io.github.sebastiantoepfer.jsonschema.JsonSchema;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public final class AffectedBy implements Comparable<AffectedBy> {

    private final AffectByType type;
    private final String name;

    public AffectedBy(final AffectByType type, final String name) {
        this.type = Objects.requireNonNull(type);
        this.name = Objects.requireNonNull(name);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.type);
        hash = 83 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        return compareTo((AffectedBy) obj) == 0;
    }

    @Override
    public int compareTo(final AffectedBy other) {
        final int result;
        if (type.compareTo(other.type) == 0) {
            result = name.compareTo(other.name);
        } else {
            result = type.compareTo(other.type);
        }
        return result;
    }

    Function<Keyword, Keyword> findAffectedByKeywordIn(final JsonSchema schema) {
        final UnaryOperator<Keyword> result;
        if (schema.keywordByName(name).isPresent()) {
            result = type::affect;
        } else {
            result = k -> k;
        }
        return result;
    }

    @Override
    public String toString() {
        return "AffectedBy{" + "type=" + type + ", name=" + name + '}';
    }
}
