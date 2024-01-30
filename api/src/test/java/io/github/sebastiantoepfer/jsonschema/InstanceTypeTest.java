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
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import jakarta.json.Json;
import jakarta.json.JsonValue;
import java.util.stream.Stream;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
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
            arguments(InstanceType.NULL, JsonValue.NULL),
            arguments(InstanceType.BOOLEAN, JsonValue.FALSE),
            arguments(InstanceType.BOOLEAN, JsonValue.TRUE),
            arguments(InstanceType.OBJECT, JsonValue.EMPTY_JSON_OBJECT),
            arguments(InstanceType.ARRAY, JsonValue.EMPTY_JSON_ARRAY),
            arguments(InstanceType.NUMBER, Json.createValue(23L)),
            arguments(InstanceType.INTEGER, Json.createValue(23L)),
            arguments(InstanceType.INTEGER, Json.createValue(10L)),
            arguments(InstanceType.INTEGER, Json.createValue(0.0))
        );
    }

    @ParameterizedTest
    @MethodSource("provideWithInvalidCombinations")
    void should_be_false_if_instancetype_is_correct(final InstanceType instanceType, final JsonValue value) {
        assertThat(instanceType.isInstance(value), is(false));
    }

    static Stream<? extends Arguments> provideWithInvalidCombinations() {
        return Stream.of(
            arguments(InstanceType.NULL, JsonValue.TRUE),
            arguments(InstanceType.BOOLEAN, JsonValue.EMPTY_JSON_OBJECT),
            arguments(InstanceType.BOOLEAN, JsonValue.EMPTY_JSON_ARRAY),
            arguments(InstanceType.OBJECT, JsonValue.EMPTY_JSON_ARRAY),
            arguments(InstanceType.ARRAY, JsonValue.TRUE),
            arguments(InstanceType.NUMBER, Json.createValue("string")),
            arguments(InstanceType.INTEGER, Json.createValue(23.2)),
            arguments(InstanceType.INTEGER, Json.createValue("test"))
        );
    }

    @ParameterizedTest
    @MethodSource("provideWithName")
    void should_return_his_name(final InstanceType instanceType, final String value) {
        assertThat(instanceType, Matchers.hasToString(value));
    }

    static Stream<? extends Arguments> provideWithName() {
        return Stream.of(
            arguments(InstanceType.NULL, "null"),
            arguments(InstanceType.BOOLEAN, "boolean"),
            arguments(InstanceType.OBJECT, "object"),
            arguments(InstanceType.ARRAY, "array"),
            arguments(InstanceType.NUMBER, "number"),
            arguments(InstanceType.INTEGER, "integer")
        );
    }

    @Test
    void should_be_from_json_type_string() {
        assertThat(InstanceType.INTEGER.getValueType(), is(JsonValue.ValueType.STRING));
    }

    @Test
    void should_retrun_his_value_as_charsquence() {
        assertThat(InstanceType.INTEGER.getChars(), hasToString("integer"));
    }

    @Test
    void should_be_createable_from_lowercase_value() {
        assertThat(InstanceType.fromString("object"), is(InstanceType.OBJECT));
    }

    @Test
    void should_be_createable_from_uppercase_value() {
        assertThat(InstanceType.fromString("NUMBER"), is(InstanceType.NUMBER));
    }

    @Test
    void should_be_createable_from_mixcase_value() {
        assertThat(InstanceType.fromString("Null"), is(InstanceType.NULL));
    }
}
