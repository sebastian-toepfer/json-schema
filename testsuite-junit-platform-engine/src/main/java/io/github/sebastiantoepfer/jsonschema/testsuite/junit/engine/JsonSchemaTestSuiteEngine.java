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

import java.util.stream.Stream;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestEngine;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;

public class JsonSchemaTestSuiteEngine implements TestEngine {

    @Override
    public String getId() {
        return "jsonschema";
    }

    @Override
    public TestDescriptor discover(final EngineDiscoveryRequest discoveryRequest, final UniqueId uniqueId) {
        final TestDescriptor result = new EngineDescriptor(uniqueId, "Json Schema Test");
        new Resources()
            .all()
            .filter(resource -> resource.hasExtension("json"))
            .map(JsonSchemaSuitesTestDescriptor::new)
            .forEach(result::addChild);
        return result;
    }

    @Override
    public void execute(final ExecutionRequest request) {
        new TestFinder(request.getRootTestDescriptor()).findTests().forEach(test -> test.run(request));
    }

    private static class TestFinder {

        private final TestDescriptor desc;

        public TestFinder(final TestDescriptor desc) {
            this.desc = desc;
        }

        Stream<ExecuteableTest> findTests() {
            final Stream<ExecuteableTest> result;
            if (desc instanceof ExecuteableTest test) {
                result = Stream.of(test);
            } else {
                result = desc.getChildren().stream().map(TestFinder::new).flatMap(TestFinder::findTests);
            }
            return result;
        }
    }
}
