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
package io.github.sebastiantoepfer.jsonschema.core.impl.constraint;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;

import io.github.sebastiantoepfer.jsonschema.core.ConstraintViolation;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class AnyConstraint<T> implements Constraint<T> {

    private final List<Constraint<? super T>> contraints;

    public AnyConstraint(final Constraint<? super T>... constraints) {
        this(asList(constraints));
    }

    public AnyConstraint(final Collection<? extends Constraint<? super T>> contraints) {
        if (contraints.isEmpty()) {
            throw new IllegalArgumentException("min one constraint must be provided!");
        }
        this.contraints = List.copyOf(contraints);
    }

    @Override
    public Collection<ConstraintViolation> violationsBy(final T value) {
        final Collection<ConstraintViolation> result;
        if (contraints.stream().anyMatch(c -> c.violationsBy(value).isEmpty())) {
            result = Set.of();
        } else {
            result = contraints.stream().map(c -> c.violationsBy(value)).flatMap(Collection::stream).collect(toSet());
        }
        return result;
    }
}
