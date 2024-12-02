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

import io.github.sebastiantoepfer.jsonschema.vocabulary.formatassertion.rfc.Rfc;
import io.github.sebastiantoepfer.jsonschema.vocabulary.formatassertion.rfc.Rfc3339;
import io.github.sebastiantoepfer.jsonschema.vocabulary.formatassertion.rfc.Rule;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

final class Formats {

    private static final Map<String, RfcInfo> RFCS = Map.of(
        "date-time",
        new RfcInfo(new Rfc3339(), "date-time"),
        "date",
        new RfcInfo(new Rfc3339(), "full-date"),
        "time",
        new RfcInfo(new Rfc3339(), "full-time"),
        "duration",
        new RfcInfo(new Rfc3339(), "duration")
    );

    Format findByName(final String name) {
        return Optional.ofNullable(RFCS.get(name))
            .flatMap(info -> info.findAs(name))
            .orElseGet(() -> new UnknownFormat(name));
    }

    private static class RfcInfo {

        private final Rfc rfc;
        private final String ruleName;

        public RfcInfo(final Rfc rfc, final String ruleName) {
            this.rfc = rfc;
            this.ruleName = ruleName;
        }

        public Optional<Format> findAs(final String alias) {
            return rfc.findRuleByName(ruleName).map(r -> new RuleFormat(alias, r));
        }

        public static class RuleFormat implements Format {

            private final String alias;
            private final Rule rule;

            public RuleFormat(final String alias, final Rule rule) {
                this.alias = Objects.requireNonNull(alias);
                this.rule = Objects.requireNonNull(rule);
            }

            @Override
            public boolean applyTo(final String value) {
                return rule.applyTo(value);
            }

            @Override
            public String name() {
                return alias;
            }
        }
    }

    interface Format {
        boolean applyTo(String value);
        String name();
    }

    private static class UnknownFormat implements Format {

        private final String name;

        public UnknownFormat(final String name) {
            this.name = name;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public boolean applyTo(final String value) {
            return false;
        }
    }
}
