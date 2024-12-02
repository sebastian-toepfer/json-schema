/*
 * The MIT License
 *
 * Copyright 2024 sebastian.
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
package io.github.sebastiantoepfer.jsonschema.vocabulary.formatassertion;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class FormatsTest {

    @Test
    void should_return_unknown() {
        assertThat(new Formats().findByName("unknow").name(), is("unknow"));
        assertThat(new Formats().findByName("unknow").applyTo("value"), is(false));
    }

    @Test
    void should_found_dateTimeFormat() {
        assertThat(new Formats().findByName("date-time").applyTo("2024-11-29T00:05:00+01:00"), is(true));
        assertThat(new Formats().findByName("date-time").applyTo("noDateTime"), is(false));
    }

    @Test
    void should_found_dateFormat() {
        assertThat(new Formats().findByName("date").applyTo("2024-11-29"), is(true));
        assertThat(new Formats().findByName("date").applyTo("noDate"), is(false));
    }

    @Test
    void should_found_timeFormat() {
        assertThat(new Formats().findByName("time").applyTo("00:05:00.0182Z"), is(true));
        assertThat(new Formats().findByName("time").applyTo("noTime"), is(false));
    }

    @Test
    void should_found_durationFormat() {
        assertThat(new Formats().findByName("duration").applyTo("PT1H10M"), is(true));
        assertThat(new Formats().findByName("duration").applyTo("noDuration"), is(false));
    }

    @Test
    void should_found_emailFormat() {
        assertThat(
            new Formats().findByName("email").applyTo("61313468+sebastian-toepfer@users.noreply.github.com"),
            is(true)
        );
        assertThat(new Formats().findByName("email").applyTo("noEmail"), is(false));
    }
}
