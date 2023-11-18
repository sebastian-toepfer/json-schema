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
import static org.hamcrest.Matchers.is;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class ResourceTest {

    @Test
    void should_determine_file_extension() {
        assertThat(new Resource("tests/test_resource.json").hasExtension("json"), is(true));
    }

    @Test
    void should_determine_name() {
        assertThat(new Resource("tests/test_resource.json").hasName("tests/test_resource.json"), is(true));
        assertThat(new Resource("tests/test_resource.json").hasName("tests/pitest"), is(false));
    }

    @Test
    void should_return_content() throws Exception {
        try (final InputStream content = new Resource("tests/test_resource.json").content()) {
            assertThat(
                new String(content.readAllBytes(), StandardCharsets.UTF_8),
                is(
                    """
                    {
                        "name": "sebastian"
                    }
                    """
                )
            );
        }
    }

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(Resource.class).verify();
    }
}
