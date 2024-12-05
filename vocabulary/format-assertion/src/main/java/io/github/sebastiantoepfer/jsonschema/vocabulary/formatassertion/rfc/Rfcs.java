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
package io.github.sebastiantoepfer.jsonschema.vocabulary.formatassertion.rfc;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@SuppressWarnings({ "java:S5843", "java:S6035" })
public final class Rfcs {

    private static final String DATE = "\\d{4}\\-\\d{2}\\-\\d{2}";
    private static final String TIMEZONE = "([Zz]|([+-]\\d{2}\\:\\d{2}))";
    private static final String TIME = "\\d{2}\\:\\d{2}\\:\\d{2}(\\.\\d+)?" + TIMEZONE;

    //create regex with: https://abnf.msweet.org/index.php
    private static final Map<Integer, Map<String, Rule>> RULES = Map.ofEntries(
        Map.entry(
            2673,
            Map.of("dotted-quad", new RegExRule(Pattern.compile("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$")))
        ),
        Map.entry(
            3339,
            Map.of(
                "date-time",
                new RegExRule(Pattern.compile("^" + DATE + "[Tt]" + TIME + "$")),
                "full-date",
                new RegExRule(Pattern.compile("^" + DATE + "$")),
                "full-time",
                new RegExRule(Pattern.compile("^" + TIME + "$")),
                "duration",
                new RegExRule(
                    Pattern.compile(
                        "^" +
                        "([Pp]((" +
                        "(\\d+[Dd]|\\d+[Mm](\\d+[Dd])?|\\d+[Yy](\\d+[Mm](\\d+[Dd])?)?)" +
                        "(([Tt](\\d+[Hh](\\d+[Mm](\\d+[Ss])?)?|\\d+[Mm](\\d+[Ss])?|\\d+[Ss])))?" +
                        ")|([Tt](\\d+[Hh](\\d+[Mm](\\d+[Ss])?)?|\\d+[Mm](\\d+[Ss])?|\\d+[Ss]))|\\d+[Ww]))" +
                        "$"
                    )
                )
            )
        ),
        Map.entry(
            4291,
            Map.of(
                "IPv6address",
                new RegExRule(
                    Pattern.compile(
                        "^(([0-9A-Fa-f]{1,4}\\:){6}(([0-9A-Fa-f]{1,4}\\:[0-9A-Fa-f]{1,4})|(\\d|[1-9]\\d|1\\d{2}|" +
                        "2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]" +
                        "\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5]))|\\:\\:([0-9A-Fa-f]{1,4}\\:){5}" +
                        "(([0-9A-Fa-f]{1,4}\\:[0-9A-Fa-f]{1,4})|(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\." +
                        "(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\." +
                        "(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5]))|([0-9A-Fa-f]{1,4})?\\:\\:([0-9A-Fa-f]{1,4}\\:){4}" +
                        "(([0-9A-Fa-f]{1,4}\\:[0-9A-Fa-f]{1,4})|(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]" +
                        "\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1" +
                        "\\d{2}|2[0-4]\\d|25[0-5]))|(([0-9A-Fa-f]{1,4}\\:)?[0-9A-Fa-f]{1,4})?\\:\\:" +
                        "([0-9A-Fa-f]{1,4}\\:){3}(([0-9A-Fa-f]{1,4}\\:[0-9A-Fa-f]{1,4})|(\\d|[1-9]\\d|1\\d{2}|" +
                        "2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]" +
                        "\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5]))|(([0-9A-Fa-f]{1,4}\\:){0,2}" +
                        "[0-9A-Fa-f]{1,4})?\\:\\:([0-9A-Fa-f]{1,4}\\:){2}(([0-9A-Fa-f]{1,4}\\:[0-9A-Fa-f]{1,4})|(\\d|" +
                        "[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]" +
                        "\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5]))|" +
                        "(([0-9A-Fa-f]{1,4}\\:){0,3}[0-9A-Fa-f]{1,4})?\\:\\:[0-9A-Fa-f]{1,4}\\:(([0-9A-Fa-f]{1,4}\\:" +
                        "[0-9A-Fa-f]{1,4})|(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]" +
                        "\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|" +
                        "25[0-5]))|(([0-9A-Fa-f]{1,4}\\:){0,4}[0-9A-Fa-f]{1,4})?\\:\\:(([0-9A-Fa-f]{1,4}\\:" +
                        "[0-9A-Fa-f]{1,4})|(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]" +
                        "\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|" +
                        "25[0-5]))|(([0-9A-Fa-f]{1,4}\\:){0,5}[0-9A-Fa-f]{1,4})?\\:\\:[0-9A-Fa-f]{1,4}|" +
                        "(([0-9A-Fa-f]{1,4}\\:){0,6}[0-9A-Fa-f]{1,4})?\\:\\:)$"
                    )
                )
            )
        ),
        Map.entry(
            5321,
            Map.of(
                "mailbox",
                new MaxLengthRule(
                    256,
                    new RegExRule(
                        Pattern.compile(
                            "^((([A-Za-z0-9]|\\!|#|\\$|%|&|'|\\*|\\+|\\-|/|\\=|\\?|\\^|_|`|\\{|\\||\\}|~)+(\\." +
                            "([A-Za-z0-9]|\\!|#|\\$|%|&|'|\\*|\\+|\\-|/|\\=|\\?|\\^|_|`|\\{|\\||\\}|~)+)*|\"" +
                            "(([ -\\!#-\\[]|[\\]-~])|\\\\[ -~])*\")@([A-Za-z0-9](([-A-Za-z0-9]*[A-Za-z0-9]))?(\\." +
                            "[A-Za-z0-9](([-A-Za-z0-9]*[A-Za-z0-9]))?)*|(\\[(\\d{1,3}(\\.\\d{1,3}){3}|[Ii][Pp][Vv]6" +
                            "\\:([0-9A-Fa-f]{1,4}(\\:[0-9A-Fa-f]{1,4}){7}|([0-9A-Fa-f]{1,4}" +
                            "(\\:[0-9A-Fa-f]{1,4}){0,5})?\\:\\:([0-9A-Fa-f]{1,4}(\\:[0-9A-Fa-f]{1,4}){0,5})?|" +
                            "[0-9A-Fa-f]{1,4}(\\:[0-9A-Fa-f]{1,4}){5}\\:\\d{1,3}(\\.\\d{1,3}){3}|([0-9A-Fa-f]{1,4}" +
                            "(\\:[0-9A-Fa-f]{1,4}){0,3})?\\:\\:([0-9A-Fa-f]{1,4}(\\:[0-9A-Fa-f]{1,4}){0,3}\\:)?" +
                            "\\d{1,3}(\\.\\d{1,3}){3})|([-A-Za-z0-9]*[A-Za-z0-9])\\:[\\!-Z\\^-~]+)\\])))$"
                        )
                    )
                )
            )
        )
    );

    public static Optional<Rfc> findRfcByNumber(final int number) {
        return Optional.ofNullable(RULES.get(number)).map(DefaultRfc::new);
    }

    private Rfcs() {}
}
