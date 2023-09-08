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
package io.github.sebastiantoepfer.jsonschema.testsuite.junit.engine;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.TestTag;
import org.junit.platform.engine.UniqueId;

abstract class ImmutableTestDescription implements TestDescriptor {

    private final TestDescriptor parent;

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    protected ImmutableTestDescription(final TestDescriptor parent) {
        this.parent = Objects.requireNonNull(parent);
    }

    @Override
    public Set<TestTag> getTags() {
        return Set.of();
    }

    @Override
    public Optional<TestSource> getSource() {
        return Optional.empty();
    }

    @Override
    public Optional<TestDescriptor> getParent() {
        return Optional.of(parent);
    }

    @Override
    public void setParent(final TestDescriptor parent) {
        throw new UnsupportedOperationException("Not supported!");
    }

    @Override
    public void addChild(final TestDescriptor descriptor) {
        throw new UnsupportedOperationException("Not supported!");
    }

    @Override
    public void removeChild(final TestDescriptor descriptor) {
        throw new UnsupportedOperationException("Not supported!");
    }

    @Override
    public void removeFromHierarchy() {}

    @Override
    public Optional<? extends TestDescriptor> findByUniqueId(final UniqueId uniqueId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
