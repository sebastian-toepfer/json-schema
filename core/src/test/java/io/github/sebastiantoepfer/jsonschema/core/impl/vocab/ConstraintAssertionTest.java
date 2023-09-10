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
package io.github.sebastiantoepfer.jsonschema.core.impl.vocab;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.github.sebastiantoepfer.jsonschema.core.ConstraintViolation;
import io.github.sebastiantoepfer.jsonschema.core.impl.constraint.Constraint;
import jakarta.json.JsonValue;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ConstraintAssertionTest {

    @Test
    void should_return_true_if_constraint_doesnt_find_any_violations() {
        assertThat(new PitTestHappyMaker(value -> Set.of()).isValidFor(JsonValue.TRUE), is(true));
    }

    @Test
    void should_return_false_if_constraint_find_any_violations() {
        assertThat(
            new PitTestHappyMaker(value -> Set.of(new ConstraintViolation())).isValidFor(JsonValue.TRUE),
            is(false)
        );
    }

    private static class PitTestHappyMaker implements ConstraintAssertion {

        private final Constraint<JsonValue> constraint;

        public PitTestHappyMaker(final Constraint<JsonValue> constraint) {
            this.constraint = Objects.requireNonNull(constraint);
        }

        @Override
        public boolean hasName(final String name) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Collection<ConstraintViolation> violationsBy(final JsonValue value) {
            return constraint.violationsBy(value);
        }
    }
}
