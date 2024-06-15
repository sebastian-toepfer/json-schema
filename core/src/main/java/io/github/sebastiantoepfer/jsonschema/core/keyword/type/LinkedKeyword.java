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
import io.github.sebastiantoepfer.jsonschema.keyword.Applicator;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import jakarta.json.JsonValue;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

final class LinkedKeyword implements Applicator {

    private final Keyword firstChainLink;
    private final LINKTYPE linkType;
    private final Keyword secondChanLink;

    public LinkedKeyword(final Keyword firstChainLink, final LINKTYPE linkType, final Keyword secondChanLink) {
        this.firstChainLink = Objects.requireNonNull(firstChainLink);
        this.linkType = Objects.requireNonNull(linkType);
        this.secondChanLink = Objects.requireNonNull(secondChanLink);
    }

    @Override
    public Collection<KeywordCategory> categories() {
        final Set<KeywordCategory> result = EnumSet.copyOf(firstChainLink.categories());
        result.addAll(secondChanLink.categories());
        return result;
    }

    @Override
    public boolean applyTo(final JsonValue instance) {
        return linkType
            .connect(firstChainLink.asApplicator()::applyTo, secondChanLink.asApplicator()::applyTo)
            .test(instance);
    }

    @Override
    public boolean hasName(final String name) {
        return secondChanLink.hasName(name);
    }

    @Override
    public <T extends Media<T>> T printOn(final T media) {
        return secondChanLink.printOn(media);
    }
}
