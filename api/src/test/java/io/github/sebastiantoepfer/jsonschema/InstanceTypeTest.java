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

import jakarta.json.Json;
import jakarta.json.JsonValue;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class InstanceTypeTest {

    @ParameterizedTest
    @MethodSource("provideWithValidCombinations")
    void should_be_true_if_instancetype_is_correct(final InstanceType instanceType, final JsonValue value) {
        assertThat(instanceType.isInstance(value), is(true));
    }

    static Stream<? extends Arguments> provideWithValidCombinations() {
        return Stream.of(
            Arguments.of(InstanceType.NULL, JsonValue.NULL),
            Arguments.of(InstanceType.BOOLEAN, JsonValue.FALSE),
            Arguments.of(InstanceType.BOOLEAN, JsonValue.TRUE),
            Arguments.of(InstanceType.OBJECT, JsonValue.EMPTY_JSON_OBJECT),
            Arguments.of(InstanceType.ARRAY, JsonValue.EMPTY_JSON_ARRAY),
            Arguments.of(InstanceType.NUMBER, Json.createValue(23L)),
            Arguments.of(InstanceType.INTEGER, Json.createValue(23L)),
            Arguments.of(InstanceType.INTEGER, Json.createValue(10L)),
            Arguments.of(InstanceType.INTEGER, Json.createValue(0.0))
        );
    }

    @ParameterizedTest
    @MethodSource("provideWithInvalidCombinations")
    void should_be_false_if_instancetype_is_correct(final InstanceType instanceType, final JsonValue value) {
        assertThat(instanceType.isInstance(value), is(false));
    }

    static Stream<? extends Arguments> provideWithInvalidCombinations() {
        return Stream.of(
            Arguments.of(InstanceType.NULL, JsonValue.TRUE),
            Arguments.of(InstanceType.BOOLEAN, JsonValue.EMPTY_JSON_OBJECT),
            Arguments.of(InstanceType.BOOLEAN, JsonValue.EMPTY_JSON_ARRAY),
            Arguments.of(InstanceType.OBJECT, JsonValue.EMPTY_JSON_ARRAY),
            Arguments.of(InstanceType.ARRAY, JsonValue.TRUE),
            Arguments.of(InstanceType.NUMBER, Json.createValue("string")),
            Arguments.of(InstanceType.INTEGER, Json.createValue(23.2)),
            Arguments.of(InstanceType.INTEGER, Json.createValue("test"))
        );
    }
}
