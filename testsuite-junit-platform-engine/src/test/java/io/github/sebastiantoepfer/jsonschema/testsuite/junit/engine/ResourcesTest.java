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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class ResourcesTest {

    @Test
    void should_find_resources_from_classpath() {
        try (final Stream<Resource> res = new Resources().all()) {
            assertThat(res.toList(), hasItems(new Resource("tests/test_resource.json")));
        }
    }

    @Test
    void should_find_resources_in_subdir_from_classpath() {
        try (final Stream<Resource> res = new Resources("tests").all()) {
            assertThat(res.toList(), hasItems(new Resource("tests/test_resource.json")));
        }
    }

    @Test
    void should_find_resources_in_metainf() {
        try (final Stream<Resource> res = new Resources("META-INF").all()) {
            //to kill all mutants, we need a resource from non-test classpath
            assertThat(res.toList(), hasItems(new Resource("META-INF/services/org.junit.platform.engine.TestEngine")));
        }
    }
}
