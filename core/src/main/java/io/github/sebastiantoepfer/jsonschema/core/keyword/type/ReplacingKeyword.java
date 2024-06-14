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

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toSet;

import io.github.sebastiantoepfer.ddd.common.Media;
import io.github.sebastiantoepfer.jsonschema.keyword.Annotation;
import io.github.sebastiantoepfer.jsonschema.keyword.Applicator;
import io.github.sebastiantoepfer.jsonschema.keyword.Assertion;
import io.github.sebastiantoepfer.jsonschema.keyword.Identifier;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import io.github.sebastiantoepfer.jsonschema.keyword.ReservedLocation;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

public final class ReplacingKeyword implements Keyword {

    private final Keyword keywordToReplace;
    private final Collection<KeywordCategory> categoriesToReplace;

    public ReplacingKeyword(final Keyword keywordToReplace) {
        this(keywordToReplace, EnumSet.of(KeywordCategory.APPLICATOR, KeywordCategory.ASSERTION));
    }

    public ReplacingKeyword(final Keyword keywordToReplace, final Collection<KeywordCategory> categoriesToReplace) {
        this.keywordToReplace = Objects.requireNonNull(keywordToReplace);
        this.categoriesToReplace = List.copyOf(categoriesToReplace);
    }

    @Override
    public Identifier asIdentifier() {
        if (categoriesToReplace.contains(KeywordCategory.IDENTIFIER)) {
            throw new UnsupportedOperationException();
        } else {
            return keywordToReplace.asIdentifier();
        }
    }

    @Override
    public Assertion asAssertion() {
        if (categoriesToReplace.contains(KeywordCategory.ASSERTION)) {
            throw new UnsupportedOperationException();
        } else {
            return keywordToReplace.asAssertion();
        }
    }

    @Override
    public Annotation asAnnotation() {
        if (categoriesToReplace.contains(KeywordCategory.ANNOTATION)) {
            throw new UnsupportedOperationException();
        } else {
            return keywordToReplace.asAnnotation();
        }
    }

    @Override
    public Applicator asApplicator() {
        if (categoriesToReplace.contains(KeywordCategory.APPLICATOR)) {
            throw new UnsupportedOperationException();
        } else {
            return keywordToReplace.asApplicator();
        }
    }

    @Override
    public ReservedLocation asReservedLocation() {
        if (categoriesToReplace.contains(KeywordCategory.RESERVED_LOCATION)) {
            throw new UnsupportedOperationException();
        } else {
            return keywordToReplace.asReservedLocation();
        }
    }

    @Override
    public Collection<Keyword.KeywordCategory> categories() {
        return keywordToReplace.categories().stream().filter(not(categoriesToReplace::contains)).collect(toSet());
    }

    @Override
    public boolean hasName(final String string) {
        return keywordToReplace.hasName(string);
    }

    @Override
    public <T extends Media<T>> T printOn(final T media) {
        return keywordToReplace.printOn(media);
    }
}
