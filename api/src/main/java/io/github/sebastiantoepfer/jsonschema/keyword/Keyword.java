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
package io.github.sebastiantoepfer.jsonschema.keyword;

import java.util.Collection;

/**
 * see: https://json-schema.org/draft/2020-12/json-schema-core.html#name-json-schema-objects-and-key
 **/
public interface Keyword {
    default Identifier asIdentifier() {
        return (Identifier) this;
    }

    default Assertion asAssertion() {
        return (Assertion) this;
    }

    default Annotation asAnnotation() {
        return (Annotation) this;
    }

    default Applicator asApplicator() {
        return (Applicator) this;
    }

    default ReservedLocation asReservedLocation() {
        return (ReservedLocation) this;
    }

    default boolean hasCategory(KeywordCategory category) {
        return categories().contains(category);
    }

    Collection<KeywordCategory> categories();

    boolean hasName(String name);

    enum KeywordCategory {
        IDENTIFIER,
        ASSERTION,
        ANNOTATION,
        APPLICATOR,
        RESERVED_LOCATION,
    }
}
