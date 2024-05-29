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
package io.github.sebastiantoepfer.jsonschema.keyword;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;

import io.github.sebastiantoepfer.ddd.media.core.HashMapMedia;
import jakarta.json.Json;
import jakarta.json.JsonValue;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.junit.jupiter.api.Test;

class StaticAnnotationTest {

    @Test
    void should_know_his_name() {
        assertThat(new StaticAnnotation("myname", Json.createValue("string")).hasName("myname"), is(true));
    }

    @Test
    void should_know_other_names() {
        assertThat(new StaticAnnotation("myname", Json.createValue("string")).hasName("id"), is(false));
    }

    @Test
    void should_return_his_value() {
        assertThat(
            new StaticAnnotation("myname", Json.createValue("string")).valueFor(JsonValue.FALSE),
            is(Json.createValue("string"))
        );
    }

    @Test
    void should_be_printable_with_null_as_value() {
        assertThat(new StaticAnnotation("value", null).printOn(new HashMapMedia()).entrySet(), is(empty()));
    }

    @Test
    void should_be_printable_with_decimalnumber_as_value() {
        assertThat(
            new StaticAnnotation("default", Json.createValue(32)).printOn(new HashMapMedia()),
            hasEntry("default", BigInteger.valueOf(32L))
        );
    }

    @Test
    void should_be_printable_with_decimal_as_value() {
        assertThat(
            new StaticAnnotation("default", Json.createValue(32.1)).printOn(new HashMapMedia()),
            hasEntry("default", BigDecimal.valueOf(32.1))
        );
    }

    @Test
    void should_be_printable_with_string_as_value() {
        assertThat(
            new StaticAnnotation("format", Json.createValue("datetime")).printOn(new HashMapMedia()),
            hasEntry("format", "datetime")
        );
    }

    @Test
    void should_be_printable_with_true_as_value() {
        assertThat(new StaticAnnotation("value", JsonValue.TRUE).printOn(new HashMapMedia()), hasEntry("value", true));
    }

    @Test
    void should_be_printable_with_false_as_value() {
        assertThat(
            new StaticAnnotation("value", JsonValue.FALSE).printOn(new HashMapMedia()),
            hasEntry("value", false)
        );
    }
}
