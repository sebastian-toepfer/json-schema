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
package io.github.sebastiantoepfer.jsonschema.core.testsuite;

import io.github.sebastiantoepfer.jsonschema.JsonSchemas;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import jakarta.json.stream.JsonParser;
import java.util.Objects;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

class JsonTestSuiteTestCaseProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(final ExtensionContext context) throws Exception {
        return new Resources()
            .all()
            .filter(resource -> resource.hasExtension("json"))
            .map(JsonSchemaTestSuites::new)
            .flatMap(JsonSchemaTestSuites::allTestCases)
            .map(Arguments::of);
    }

    private static class JsonSchemaTestSuites {

        private final Resource jsonResource;

        public JsonSchemaTestSuites(final Resource jsonResource) {
            this.jsonResource = Objects.requireNonNull(jsonResource);
        }

        Stream<JsonTestSuiteTestCase> allTestCases() {
            final Stream<JsonTestSuiteTestCase> result;
            final JsonParser parser = Json.createParser(jsonResource.content());
            if (parser.next() == JsonParser.Event.START_ARRAY) {
                result =
                    parser
                        .getArrayStream()
                        .onClose(() -> parser.close())
                        .map(JsonValue::asJsonObject)
                        .map(JsonSchemaTestSuite::new)
                        .flatMap(JsonSchemaTestSuite::allTestCases);
            } else {
                parser.close();
                result = Stream.empty();
            }
            return result;
        }

        private class JsonSchemaTestSuite {

            private final JsonObject testsuite;

            public JsonSchemaTestSuite(final JsonObject testsuite) {
                this.testsuite = Objects.requireNonNull(testsuite);
            }

            Stream<JsonTestSuiteTestCase> allTestCases() {
                return testsuite
                    .getJsonArray("tests")
                    .stream()
                    .map(test -> new JsonSchemaTest(testsuite.get("schema"), test.asJsonObject()));
            }

            private class JsonSchemaTest implements JsonTestSuiteTestCase {

                private final JsonValue schema;
                private final JsonObject test;

                public JsonSchemaTest(final JsonValue schema, final JsonObject test) {
                    this.schema = schema;
                    this.test = test;
                }

                @Override
                public boolean isValid() {
                    return (JsonSchemas.load(schema).validator().isValid(test.get("data")) == test.getBoolean("valid"));
                }

                @Override
                public String toString() {
                    return (
                        "JsonSchemaTest{" +
                        "schema=" +
                        schema +
                        ", test=" +
                        test +
                        " in " +
                        JsonSchemaTestSuites.this.jsonResource.name() +
                        '}'
                    );
                }
            }
        }
    }
}
