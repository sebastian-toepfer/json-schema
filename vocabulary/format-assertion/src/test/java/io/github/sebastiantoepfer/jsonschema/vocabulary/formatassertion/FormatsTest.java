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

    @Test
    void should_found_ipv4Format() {
        assertThat(new Formats().findByName("ipv4").applyTo("161.111.26.9"), is(true));
        assertThat(new Formats().findByName("ipv4").applyTo("125.158.4589.1"), is(false));
    }

    @Test
    void should_found_ipv6Format() {
        assertThat(new Formats().findByName("ipv6").applyTo("ABCD:EF01:2345:6789:ABCD:EF01:2345:6789"), is(true));
        assertThat(new Formats().findByName("ipv6").applyTo("2001:DB8:0:0:8:800:200C:417A"), is(true));
        assertThat(new Formats().findByName("ipv6").applyTo("FF01:0:0:0:0:0:0:101"), is(true));
        assertThat(new Formats().findByName("ipv6").applyTo("0:0:0:0:0:0:0:1"), is(true));
        assertThat(new Formats().findByName("ipv6").applyTo("0:0:0:0:0:0:0:0"), is(true));
        //compressed
        assertThat(new Formats().findByName("ipv6").applyTo("2001:DB8::8:800:200C:417A"), is(true));
        assertThat(new Formats().findByName("ipv6").applyTo("FF01::101"), is(true));
        assertThat(new Formats().findByName("ipv6").applyTo("::1"), is(true));
        assertThat(new Formats().findByName("ipv6").applyTo("::"), is(true));
        //ipv4 representation
        assertThat(new Formats().findByName("ipv6").applyTo("0:0:0:0:0:0:13.1.68.3"), is(true));
        assertThat(new Formats().findByName("ipv6").applyTo("0:0:0:0:0:FFFF:129.144.52.38"), is(true));
        //ipv4 representation compressed
        assertThat(new Formats().findByName("ipv6").applyTo("::13.1.68.3"), is(true));
        assertThat(new Formats().findByName("ipv6").applyTo("::FFFF:129.144.52.38"), is(true));
        //ipv4 only -> invalid!
        assertThat(new Formats().findByName("ipv6").applyTo("125.158.4589.1"), is(false));
    }

    @Test
    void should_found_hostname() {
        assertThat(new Formats().findByName("hostname").applyTo("www"), is(true));
        assertThat(new Formats().findByName("hostname").applyTo("bar_baz"), is(false));
    }

    @Test
    void should_found_uriformat() {
        assertThat(new Formats().findByName("uri").applyTo("http://www.example.com"), is(true));
        assertThat(new Formats().findByName("uri").applyTo("1://noUri"), is(false));
    }

    @Test
    void should_found_urireferenceformat() {
        assertThat(new Formats().findByName("uri-reference").applyTo("http://www.example.com"), is(true));
        assertThat(new Formats().findByName("uri-reference").applyTo("/json-schema"), is(true));
        assertThat(new Formats().findByName("uri-reference").applyTo("1://noUri"), is(false));
    }

    @Test
    void should_found_uri_templateformat() {
        assertThat(
            new Formats().findByName("uri-template").applyTo("http://www.example.com/foo{?query,number}"),
            is(true)
        );
    }

    @Test
    void should_found_uuidformat() {
        assertThat(new Formats().findByName("uuid").applyTo("b7128c5b-eafc-4b6b-9b35-1c9e3dbaabb1"), is(true));
        assertThat(new Formats().findByName("uuid").applyTo("b7128c5b-eafc-4b6b-9b35"), is(false));
    }

    @Test
    void should_found_jsonpointerformat() {
        assertThat(new Formats().findByName("json-pointer").applyTo("/abc"), is(true));
        assertThat(new Formats().findByName("json-pointer:").applyTo("abc"), is(false));
    }
}
