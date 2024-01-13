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
package io.github.sebastiantoepfer.jsonschema;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

import io.github.sebastiantoepfer.ddd.common.Media;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import jakarta.json.JsonValue;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class JsonSubSchemaTest {

    @Test
    void should_return_its_owner_as_root() {
        final JsonSchema root = new FakeJsonSchemaFactory.FakeJsonSchema();
        assertThat(
            new JsonSubSchema() {
                @Override
                public JsonSchema owner() {
                    return root;
                }

                @Override
                public Validator validator() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public Optional<Keyword> keywordByName(final String name) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public JsonValue.ValueType getValueType() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public Optional<JsonSubSchema> asSubSchema(final String name) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public <T extends Media<T>> T printOn(final T media) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            }
                .rootSchema(),
            is(sameInstance(root))
        );
    }
}
