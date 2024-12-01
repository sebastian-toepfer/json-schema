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
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public final class OptionalSequence implements Element {

    public static OptionalSequence of(final Element optionalElement) {
        return of(List.of(Objects.requireNonNull(optionalElement)));
    }

    public static OptionalSequence of(final Collection<Element> optionalElements) {
        return new OptionalSequence(optionalElements);
    }

    private final List<Element> optionalElements;

    public OptionalSequence(final Collection<Element> optionalElements) {
        this.optionalElements = List.copyOf(optionalElements);
    }

    @Override
    public <T extends Media<T>> T printOn(final T media) {
        return media.withValue("type", "option").withValue("optionals", List.copyOf(optionalElements));
    }

    @Override
    public boolean isValidFor(final ValidateableCodePoint codePoint) {
        return true;
    }

    @Override
    public Dimension dimension() {
        return Dimension
            .zero()
            .expandTo(
                optionalElements.stream().map(Element::dimension).sorted().reduce(Dimension.zero(), Dimension::plus)
            );
    }

    @Override
    public String toString() {
        return "OptionalSequence{" + "optionalElements=" + optionalElements + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.optionalElements);
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
        final OptionalSequence other = (OptionalSequence) obj;
        return Objects.equals(this.optionalElements, other.optionalElements);
    }
}
