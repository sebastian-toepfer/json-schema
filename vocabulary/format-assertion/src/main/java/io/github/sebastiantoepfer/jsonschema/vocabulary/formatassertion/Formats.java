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

import io.github.sebastiantoepfer.jsonschema.vocabulary.formatassertion.rfc.Rfcs;
import java.util.Map;
import java.util.Optional;

final class Formats {

    private static final Map<String, Format> CUSTOM_FORMAT = Map.of("regex", new RegExFormat());

    private static final Map<String, Map.Entry<Integer, String>> RFCS = Map.ofEntries(
        Map.entry("hostname", Map.entry(1123, "hostname")),
        Map.entry("date-time", Map.entry(3339, "date-time")),
        Map.entry("date", Map.entry(3339, "full-date")),
        Map.entry("time", Map.entry(3339, "full-time")),
        Map.entry("duration", Map.entry(3339, "duration")),
        Map.entry("uri", Map.entry(3986, "URI")),
        Map.entry("uri-reference", Map.entry(3986, "URI-reference")),
        Map.entry("uri-template", Map.entry(6570, "URI-Template")),
        Map.entry("email", Map.entry(5321, "mailbox")),
        Map.entry("ipv4", Map.entry(2673, "dotted-quad")),
        Map.entry("uuid", Map.entry(4122, "UUID")),
        Map.entry("ipv6", Map.entry(4291, "IPv6address")),
        Map.entry("json-pointer", Map.entry(6901, "json-pointer")),
        Map.entry("iri", Map.entry(3987, "IRI")),
        Map.entry("iri-reference", Map.entry(3987, "IRI-reference"))
    );

    Format findByName(final String name) {
        return Optional.ofNullable(RFCS.get(name))
            .flatMap(entry ->
                Rfcs.findRfcByNumber(entry.getKey()).map(rfc -> new RfcBasedFormat(name, rfc, entry.getValue()))
            )
            .map(Format.class::cast)
            .or(() -> Optional.ofNullable(CUSTOM_FORMAT.get(name)))
            .orElseGet(() -> new UnknownFormat(name));
    }
}
