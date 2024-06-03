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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import java.util.List;
import java.util.TreeSet;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class AffectedByTest {

    @Test
    void should_fullfil_equals_contract() {
        EqualsVerifier.forClass(AffectedBy.class).withNonnullFields("type", "name").verify();
    }

    @Test
    void should_be_sorted_correctly() {
        assertThat(
            new TreeSet<>(
                List.of(
                    new AffectedBy(AffectByType.REPLACE, "d"),
                    new AffectedBy(AffectByType.EXTENDS, "c"),
                    new AffectedBy(AffectByType.EXTENDS, "b"),
                    new AffectedBy(AffectByType.REPLACE, "a")
                )
            ),
            contains(
                new AffectedBy(AffectByType.EXTENDS, "b"),
                new AffectedBy(AffectByType.EXTENDS, "c"),
                new AffectedBy(AffectByType.REPLACE, "a"),
                new AffectedBy(AffectByType.REPLACE, "d")
            )
        );
    }
}
