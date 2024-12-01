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
package io.github.sebastiantoepfer.jsonschema.vocabulary.format.assertion.abnf.element;

import io.github.sebastiantoepfer.ddd.common.Media;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public final class SequenceGroup implements Element {

    public static SequenceGroup of(final Element term) {
        return of(List.of(Objects.requireNonNull(term)));
    }

    public static SequenceGroup of(final Element first, final Element second, final Element... more) {
        final List<Element> elements = new ArrayList<>();
        elements.add(Objects.requireNonNull(first));
        elements.add(Objects.requireNonNull(second));
        elements.addAll(Arrays.asList(more));
        return of(elements);
    }

    public static SequenceGroup of(final Collection<? extends Element> elements) {
        return new SequenceGroup(elements);
    }

    private final List<Element> elements;

    private SequenceGroup(final Collection<? extends Element> elements) {
        this.elements = List.copyOf(elements);
    }

    @Override
    public <T extends Media<T>> T printOn(final T media) {
        return media.withValue("type", "group").withValue("elements", List.copyOf(elements));
    }

    @Override
    public boolean isValidFor(final ValidateableCodePoint codePoint) {
        final boolean result;
        if (dimension().isInRange(codePoint)) {
            result = new ListValidation(elements, SequenceGroup::of).isValidFor(codePoint);
        } else {
            result = false;
        }
        return result;
    }

    @Override
    public Dimension dimension() {
        return elements.stream().map(Element::dimension).reduce(Dimension.zero(), Dimension::plus);
    }

    @Override
    public String toString() {
        return "SequenceGroup{" + "elements=" + elements + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.elements);
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
        final SequenceGroup other = (SequenceGroup) obj;
        return Objects.equals(this.elements, other.elements);
    }
}
