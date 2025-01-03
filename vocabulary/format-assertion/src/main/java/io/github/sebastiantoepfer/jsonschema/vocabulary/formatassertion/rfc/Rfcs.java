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

@SuppressWarnings({ "java:S5843", "java:S6035", "java:S5855" })
public final class Rfcs {

    private static final String DATE = "\\d{4}\\-\\d{2}\\-\\d{2}";
    private static final String TIMEZONE = "([Zz]|([+-]\\d{2}\\:\\d{2}))";
    private static final String TIME = "\\d{2}\\:\\d{2}\\:\\d{2}(\\.\\d+)?" + TIMEZONE;

    private static final Map<Integer, Map<String, Rule>> RULES = Map.ofEntries(
        Map.entry(
            1123,
            Map.of("hostname", new RegExRule(Pattern.compile("(?=\\A[-a-z0-9]{1,63}\\Z)\\A[a-z0-9]+(-[a-z0-9]+)*\\Z")))
        ),
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
            3986,
            Map.of(
                "URI",
                new RegExRule(
                    Pattern.compile(
                        "^([A-Za-z]([A-Za-z0-9]|\\+|\\-|\\.)*)\\:(//(((([-A-Z._a-z0-9]|~)|%[0-9A-Fa-f][0-" +
                        "9A-Fa-f]|(\\!|\\$|&|'|\\(|\\)|\\*|\\+|,|;|\\=)|\\:)*)@)?((\\[((([0-9A-Fa-f]{1,4}" +
                        "\\:){6}(([0-9A-Fa-f]{1,4}\\:[0-9A-Fa-f]{1,4})|(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25" +
                        "[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]" +
                        "\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5]))|\\:\\:([0-9A-Fa-f]{1,4}" +
                        "\\:){5}(([0-9A-Fa-f]{1,4}\\:[0-9A-Fa-f]{1,4})|(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25" +
                        "[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]" +
                        "\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5]))|([0-9A-Fa-f]{1,4})?\\:" +
                        "\\:([0-9A-Fa-f]{1,4}\\:){4}(([0-9A-Fa-f]{1,4}\\:[0-9A-Fa-f]{1,4})|(\\d|[1-9]\\d|" +
                        "1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-" +
                        "9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5]))|(([" +
                        "0-9A-Fa-f]{1,4}\\:)?[0-9A-Fa-f]{1,4})?\\:\\:([0-9A-Fa-f]{1,4}\\:){3}(([0-9A-" +
                        "Fa-f]{1,4}\\:[0-9A-Fa-f]{1,4})|(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[" +
                        "1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\." +
                        "(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5]))|(([0-9A-Fa-f]{1,4}\\:){0,2}[0-9A-Fa-f]" +
                        "{1,4})?\\:\\:([0-9A-Fa-f]{1,4}\\:){2}(([0-9A-Fa-f]{1,4}\\:[0-9A-Fa-f]{1,4})|(\\d" +
                        "|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])" +
                        "\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25" +
                        "[0-5]))|(([0-9A-Fa-f]{1,4}\\:){0,3}[0-9A-Fa-f]{1,4})?\\:\\:[0-9A-Fa-f]{1,4}\\:((" +
                        "[0-9A-Fa-f]{1,4}\\:[0-9A-Fa-f]{1,4})|(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\." +
                        "(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-" +
                        "5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5]))|(([0-9A-Fa-f]{1,4}\\:){0,4}[0-9A" +
                        "-Fa-f]{1,4})?\\:\\:(([0-9A-Fa-f]{1,4}\\:[0-9A-Fa-f]{1,4})|(\\d|[1-9]\\d|1\\d{2}|" +
                        "2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1" +
                        "\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5]))|(([0-9A-Fa" +
                        "-f]{1,4}\\:){0,5}[0-9A-Fa-f]{1,4})?\\:\\:[0-9A-Fa-f]{1,4}|(([0-9A-Fa-f]{1,4}\\:)" +
                        "{0,6}[0-9A-Fa-f]{1,4})?\\:\\:)|([Vv][0-9A-Fa-f]+\\.(([-A-Z._a-z0-9]|~)|(\\!|\\$|" +
                        "&|'|\\(|\\)|\\*|\\+|,|;|\\=)|\\:)+))\\])|(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5]" +
                        ")\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|2" +
                        "5[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])|((([-A-Z._a-z0-9]|~)|%[0-9A-F" +
                        "a-f][0-9A-Fa-f]|(\\!|\\$|&|'|\\(|\\)|\\*|\\+|,|;|\\=))*))(\\:\\d*)?(/(([-A-Z._a-" +
                        "z0-9]|~)|%[0-9A-Fa-f][0-9A-Fa-f]|(\\!|\\$|&|'|\\(|\\)|\\*|\\+|,|;|\\=)|\\:|@)*)*" +
                        "|/((([-A-Z._a-z0-9]|~)|%[0-9A-Fa-f][0-9A-Fa-f]|(\\!|\\$|&|'|\\(|\\)|\\*|\\+|,|;|" +
                        "\\=)|\\:|@)+(/(([-A-Z._a-z0-9]|~)|%[0-9A-Fa-f][0-9A-Fa-f]|(\\!|\\$|&|'|\\(|\\)|" +
                        "\\*|\\+|,|;|\\=)|\\:|@)*)*)?|(([-A-Z._a-z0-9]|~)|%[0-9A-Fa-f][0-9A-Fa-f]|(\\!|" +
                        "\\$|&|'|\\(|\\)|\\*|\\+|,|;|\\=)|\\:|@)+(/(([-A-Z._a-z0-9]|~)|%[0-9A-Fa-f][0-9A-" +
                        "Fa-f]|(\\!|\\$|&|'|\\(|\\)|\\*|\\+|,|;|\\=)|\\:|@)*)*|MISSING-0(([-A-Z._a-z0-9]|" +
                        "~)|%[0-9A-Fa-f][0-9A-Fa-f]|(\\!|\\$|&|'|\\(|\\)|\\*|\\+|,|;|\\=)|\\:|@))(\\?((((" +
                        "[-A-Z._a-z0-9]|~)|%[0-9A-Fa-f][0-9A-Fa-f]|(\\!|\\$|&|'|\\(|\\)|\\*|\\+|,|;|\\=)|" +
                        "\\:|@)|/|\\?)*))?(#(((([-A-Z._a-z0-9]|~)|%[0-9A-Fa-f][0-9A-Fa-f]|(\\!|\\$|&|'|" +
                        "\\(|\\)|\\*|\\+|,|;|\\=)|\\:|@)|/|\\?)*))?$"
                    )
                ),
                "URI-reference",
                new RegExRule(
                    Pattern.compile(
                        "^(([A-Za-z]([A-Za-z0-9]|\\+|\\-|\\.)*)\\:(//(((([-A-Z._a-z0-9]|~)|%[0-9A-Fa-f][0-9A-Fa-f]|" +
                        "(\\!|\\$|&|'|\\(|\\)|\\*|\\+|,|;|\\=)|\\:)*)@)?((\\[((([0-9A-Fa-f]{1,4}\\:){6}(([0-9A-Fa-f]" +
                        "{1,4}\\:[0-9A-Fa-f]{1,4})|(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|" +
                        "2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4" +
                        "]\\d|25[0-5]))|\\:\\:([0-9A-Fa-f]{1,4}\\:){5}(([0-9A-Fa-f]{1,4}\\:[0-9A-Fa-f]{1,4})|(\\d|[1" +
                        "-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]" +
                        "\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5]))|([0-9A-Fa-f]{1,4" +
                        "})?\\:\\:([0-9A-Fa-f]{1,4}\\:){4}(([0-9A-Fa-f]{1,4}\\:[0-9A-Fa-f]{1,4})|(\\d|[1-9]\\d|1\\d{" +
                        "2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[" +
                        "0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5]))|(([0-9A-Fa-f]{1,4}\\:)?[0-9A-F" +
                        "a-f]{1,4})?\\:\\:([0-9A-Fa-f]{1,4}\\:){3}(([0-9A-Fa-f]{1,4}\\:[0-9A-Fa-f]{1,4})|(\\d|[1-9]" +
                        "\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1" +
                        "\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5]))|(([0-9A-Fa-f]{1,4}\\:" +
                        "){0,2}[0-9A-Fa-f]{1,4})?\\:\\:([0-9A-Fa-f]{1,4}\\:){2}(([0-9A-Fa-f]{1,4}\\:[0-9A-Fa-f]{1,4}" +
                        ")|(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(" +
                        "\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5]))|(([0-9A" +
                        "-Fa-f]{1,4}\\:){0,3}[0-9A-Fa-f]{1,4})?\\:\\:[0-9A-Fa-f]{1,4}\\:(([0-9A-Fa-f]{1,4}\\:[0-9A-F" +
                        "a-f]{1,4})|(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-" +
                        "5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5]))|" +
                        "(([0-9A-Fa-f]{1,4}\\:){0,4}[0-9A-Fa-f]{1,4})?\\:\\:(([0-9A-Fa-f]{1,4}\\:[0-9A-Fa-f]{1,4})|(" +
                        "\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[" +
                        "1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5]))|(([0-9A-Fa-f" +
                        "]{1,4}\\:){0,5}[0-9A-Fa-f]{1,4})?\\:\\:[0-9A-Fa-f]{1,4}|(([0-9A-Fa-f]{1,4}\\:){0,6}[0-9A-Fa" +
                        "-f]{1,4})?\\:\\:)|([Vv][0-9A-Fa-f]+\\.(([-A-Z._a-z0-9]|~)|(\\!|\\$|&|'|\\(|\\)|\\*|\\+|,|;|" +
                        "\\=)|\\:)+))\\])|(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d" +
                        "|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0" +
                        "-5])|((([-A-Z._a-z0-9]|~)|%[0-9A-Fa-f][0-9A-Fa-f]|(\\!|\\$|&|'|\\(|\\)|\\*|\\+|,|;|\\=))*))" +
                        "(\\:\\d*)?(/(([-A-Z._a-z0-9]|~)|%[0-9A-Fa-f][0-9A-Fa-f]|(\\!|\\$|&|'|\\(|\\)|\\*|\\+|,|;|" +
                        "\\=)|\\:|@)*)*|/((([-A-Z._a-z0-9]|~)|%[0-9A-Fa-f][0-9A-Fa-f]|(\\!|\\$|&|'|\\(|\\)|\\*|\\+|," +
                        "|;|\\=)|\\:|@)+(/(([-A-Z._a-z0-9]|~)|%[0-9A-Fa-f][0-9A-Fa-f]|(\\!|\\$|&|'|\\(|\\)|\\*|\\+|," +
                        "|;|\\=)|\\:|@)*)*)?|(([-A-Z._a-z0-9]|~)|%[0-9A-Fa-f][0-9A-Fa-f]|(\\!|\\$|&|'|\\(|\\)|\\*|\\" +
                        "+|,|;|\\=)|\\:|@)+(/(([-A-Z._a-z0-9]|~)|%[0-9A-Fa-f][0-9A-Fa-f]|(\\!|\\$|&|'|\\(|\\)|\\*|\\" +
                        "+|,|;|\\=)|\\:|@)*)*|MISSING-0(([-A-Z._a-z0-9]|~)|%[0-9A-Fa-f][0-9A-Fa-f]|(\\!|\\$|&|'|\\(|" +
                        "\\)|\\*|\\+|,|;|\\=)|\\:|@))(\\?(((([-A-Z._a-z0-9]|~)|%[0-9A-Fa-f][0-9A-Fa-f]|(\\!|\\$|&|'|" +
                        "\\(|\\)|\\*|\\+|,|;|\\=)|\\:|@)|/|\\?)*))?(#(((([-A-Z._a-z0-9]|~)|%[0-9A-Fa-f][0-9A-Fa-f]|(" +
                        "\\!|\\$|&|'|\\(|\\)|\\*|\\+|,|;|\\=)|\\:|@)|/|\\?)*))?|(//(((([-A-Z._a-z0-9]|~)|%[0-9A-Fa-f" +
                        "][0-9A-Fa-f]|(\\!|\\$|&|'|\\(|\\)|\\*|\\+|,|;|\\=)|\\:)*)@)?((\\[((([0-9A-Fa-f]{1,4}\\:){6}" +
                        "(([0-9A-Fa-f]{1,4}\\:[0-9A-Fa-f]{1,4})|(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9" +
                        "]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|" +
                        "1\\d{2}|2[0-4]\\d|25[0-5]))|\\:\\:([0-9A-Fa-f]{1,4}\\:){5}(([0-9A-Fa-f]{1,4}\\:[0-9A-Fa-f]{" +
                        "1,4})|(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])" +
                        "\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5]))|([0" +
                        "-9A-Fa-f]{1,4})?\\:\\:([0-9A-Fa-f]{1,4}\\:){4}(([0-9A-Fa-f]{1,4}\\:[0-9A-Fa-f]{1,4})|(\\d|[" +
                        "1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]" +
                        "\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5]))|(([0-9A-Fa-f]{1," +
                        "4}\\:)?[0-9A-Fa-f]{1,4})?\\:\\:([0-9A-Fa-f]{1,4}\\:){3}(([0-9A-Fa-f]{1,4}\\:[0-9A-Fa-f]{1,4" +
                        "})|(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(" +
                        "\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5]))|(([0-9A" +
                        "-Fa-f]{1,4}\\:){0,2}[0-9A-Fa-f]{1,4})?\\:\\:([0-9A-Fa-f]{1,4}\\:){2}(([0-9A-Fa-f]{1,4}\\:[0" +
                        "-9A-Fa-f]{1,4})|(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|" +
                        "25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-" +
                        "5]))|(([0-9A-Fa-f]{1,4}\\:){0,3}[0-9A-Fa-f]{1,4})?\\:\\:[0-9A-Fa-f]{1,4}\\:(([0-9A-Fa-f]{1," +
                        "4}\\:[0-9A-Fa-f]{1,4})|(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0" +
                        "-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]" +
                        "\\d|25[0-5]))|(([0-9A-Fa-f]{1,4}\\:){0,4}[0-9A-Fa-f]{1,4})?\\:\\:(([0-9A-Fa-f]{1,4}\\:[0-9A" +
                        "-Fa-f]{1,4})|(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[" +
                        "0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])" +
                        ")|(([0-9A-Fa-f]{1,4}\\:){0,5}[0-9A-Fa-f]{1,4})?\\:\\:[0-9A-Fa-f]{1,4}|(([0-9A-Fa-f]{1,4}\\:" +
                        "){0,6}[0-9A-Fa-f]{1,4})?\\:\\:)|([Vv][0-9A-Fa-f]+\\.(([-A-Z._a-z0-9]|~)|(\\!|\\$|&|'|\\(|\\" +
                        ")|\\*|\\+|,|;|\\=)|\\:)+))\\])|(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\" +
                        "d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|" +
                        "2[0-4]\\d|25[0-5])|((([-A-Z._a-z0-9]|~)|%[0-9A-Fa-f][0-9A-Fa-f]|(\\!|\\$|&|'|\\(|\\)|\\*|" +
                        "\\+|,|;|\\=))*))(\\:\\d*)?(/(([-A-Z._a-z0-9]|~)|%[0-9A-Fa-f][0-9A-Fa-f]|(\\!|\\$|&|'|\\(|" +
                        "\\)|\\*|\\+|,|;|\\=)|\\:|@)*)*|/((([-A-Z._a-z0-9]|~)|%[0-9A-Fa-f][0-9A-Fa-f]|(\\!|\\$|&|'|" +
                        "\\(|\\)|\\*|\\+|,|;|\\=)|\\:|@)+(/(([-A-Z._a-z0-9]|~)|%[0-9A-Fa-f][0-9A-Fa-f]|(\\!|\\$|&|'|" +
                        "\\(|\\)|\\*|\\+|,|;|\\=)|\\:|@)*)*)?|((([-A-Z._a-z0-9]|~)|%[0-9A-Fa-f][0-9A-Fa-f]|(\\!|\\$|" +
                        "&|'|\\(|\\)|\\*|\\+|,|;|\\=)|@)+)(/(([-A-Z._a-z0-9]|~)|%[0-9A-Fa-f][0-9A-Fa-f]|(\\!|\\$|&|'" +
                        "|\\(|\\)|\\*|\\+|,|;|\\=)|\\:|@)*)*|MISSING-0(([-A-Z._a-z0-9]|~)|%[0-9A-Fa-f][0-9A-Fa-f]|(" +
                        "\\!|\\$|&|'|\\(|\\)|\\*|\\+|,|;|\\=)|\\:|@))(\\?(((([-A-Z._a-z0-9]|~)|%[0-9A-Fa-f][0-9A-Fa-" +
                        "f]|(\\!|\\$|&|'|\\(|\\)|\\*|\\+|,|;|\\=)|\\:|@)|/|\\?)*))?(#(((([-A-Z._a-z0-9]|~)|%[0-9A-Fa" +
                        "-f][0-9A-Fa-f]|(\\!|\\$|&|'|\\(|\\)|\\*|\\+|,|;|\\=)|\\:|@)|/|\\?)*))?)$"
                    )
                )
            )
        ),
        Map.entry(
            3987,
            Map.of(
                "IRI",
                new RegExRule(
                    Pattern.compile(
                        "^[a-z](?:[-a-z0-9\\+\\.])*:(?:\\/\\/(?:(?:%[0-9a-f][0-9a-f]|[-a-z0-9\\._~\\x{A0}-\\x{D7FF}" +
                        "\\x{F900}-\\x{FDCF}\\x{FDF0}-\\x{FFEF}\\x{10000}-\\x{1FFFD}\\x{20000}-\\x{2FFFD}\\x{30000}-" +
                        "\\x{3FFFD}\\x{40000}-\\x{4FFFD}\\x{50000}-\\x{5FFFD}\\x{60000}-\\x{6FFFD}\\x{70000}-" +
                        "\\x{7FFFD}\\x{80000}-\\x{8FFFD}\\x{90000}-\\x{9FFFD}\\x{A0000}-\\x{AFFFD}\\x{B0000}-" +
                        "\\x{BFFFD}\\x{C0000}-\\x{CFFFD}\\x{D0000}-\\x{DFFFD}\\x{E1000}-\\x{EFFFD}!\\$&'\\(\\)\\*\\+," +
                        ";=:])*@)?(?:\\[(?:(?:(?:[0-9a-f]{1,4}:){6}(?:[0-9a-f]{1,4}:[0-9a-f]{1,4}|(?:\\d|[1-9]\\d" +
                        "|1\\d\\d|2[0-4]\\d|25[0-5])(?:\\.(?:\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])){3})|::(?:[0-9a-" +
                        "f]{1,4}:){5}(?:[0-9a-f]{1,4}:[0-9a-f]{1,4}|(?:\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])(?:\\.(" +
                        "?:\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])){3})|(?:[0-9a-f]{1,4})?::(?:[0-9a-f]{1,4}:){4}(?:[" +
                        "0-9a-f]{1,4}:[0-9a-f]{1,4}|(?:\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])(?:\\.(?:\\d|[1-9]\\d|1" +
                        "\\d\\d|2[0-4]\\d|25[0-5])){3})|(?:(?:[0-9a-f]{1,4}:){0,1}[0-9a-f]{1,4})?::(?:[0-9a-f]{1,4}:)" +
                        "{3}(?:[0-9a-f]{1,4}:[0-9a-f]{1,4}|(?:\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])(?:\\.(?:\\d|[1-" +
                        "9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])){3})|(?:(?:[0-9a-f]{1,4}:){0,2}[0-9a-f]{1,4})?::(?:[0-9a-f]" +
                        "{1,4}:){2}(?:[0-9a-f]{1,4}:[0-9a-f]{1,4}|(?:\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])(?:\\.(?:" +
                        "\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])){3})|(?:(?:[0-9a-f]{1,4}:){0,3}[0-9a-f]{1,4})?::[0-9" +
                        "a-f]{1,4}:(?:[0-9a-f]{1,4}:[0-9a-f]{1,4}|(?:\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])(?:\\.(?:" +
                        "\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])){3})|(?:(?:[0-9a-f]{1,4}:){0,4}[0-9a-f]{1,4})?::(?:[" +
                        "0-9a-f]{1,4}:[0-9a-f]{1,4}|(?:\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])(?:\\.(?:\\d|[1-9]\\d|1" +
                        "\\d\\d|2[0-4]\\d|25[0-5])){3})|(?:(?:[0-9a-f]{1,4}:){0,5}[0-9a-f]{1,4})?::[0-9a-f]{1,4}|(?:(" +
                        "?:[0-9a-f]{1,4}:){0,6}[0-9a-f]{1,4})?::)|v[0-9a-f]+\\.[-a-z0-9\\._~!\\$&'\\(\\)\\*\\+,;=:]+)" +
                        "\\]|(?:\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])(?:\\.(?:\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5" +
                        "])){3}|(?:%[0-9a-f][0-9a-f]|[-a-z0-9\\._~\\x{A0}-\\x{D7FF}\\x{F900}-\\x{FDCF}\\x{FDF0}-" +
                        "\\x{FFEF}\\x{10000}-\\x{1FFFD}\\x{20000}-\\x{2FFFD}\\x{30000}-\\x{3FFFD}\\x{40000}-" +
                        "\\x{4FFFD}\\x{50000}-\\x{5FFFD}\\x{60000}-\\x{6FFFD}\\x{70000}-\\x{7FFFD}\\x{80000}-" +
                        "\\x{8FFFD}\\x{90000}-\\x{9FFFD}\\x{A0000}-\\x{AFFFD}\\x{B0000}-\\x{BFFFD}\\x{C0000}-" +
                        "\\x{CFFFD}\\x{D0000}-\\x{DFFFD}\\x{E1000}-\\x{EFFFD}!\\$&'\\(\\)\\*\\+,;=])*)(?::\\d*)?(?:" +
                        "\\/(?:(?:%[0-9a-f][0-9a-f]|[-a-z0-9\\._~\\x{A0}-\\x{D7FF}\\x{F900}-\\x{FDCF}\\x{FDF0}-" +
                        "\\x{FFEF}\\x{10000}-\\x{1FFFD}\\x{20000}-\\x{2FFFD}\\x{30000}-\\x{3FFFD}\\x{40000}-" +
                        "\\x{4FFFD}\\x{50000}-\\x{5FFFD}\\x{60000}-\\x{6FFFD}\\x{70000}-\\x{7FFFD}\\x{80000}-" +
                        "\\x{8FFFD}\\x{90000}-\\x{9FFFD}\\x{A0000}-\\x{AFFFD}\\x{B0000}-\\x{BFFFD}\\x{C0000}-" +
                        "\\x{CFFFD}\\x{D0000}-\\x{DFFFD}\\x{E1000}-\\x{EFFFD}!\\$&'\\(\\)\\*\\+,;=:@]))*)*|\\/(?:(?:(" +
                        "?:(?:%[0-9a-f][0-9a-f]|[-a-z0-9\\._~\\x{A0}-\\x{D7FF}\\x{F900}-\\x{FDCF}\\x{FDF0}-\\x{FFEF}" +
                        "\\x{10000}-\\x{1FFFD}\\x{20000}-\\x{2FFFD}\\x{30000}-\\x{3FFFD}\\x{40000}-\\x{4FFFD}" +
                        "\\x{50000}-\\x{5FFFD}\\x{60000}-\\x{6FFFD}\\x{70000}-\\x{7FFFD}\\x{80000}-\\x{8FFFD}" +
                        "\\x{90000}-\\x{9FFFD}\\x{A0000}-\\x{AFFFD}\\x{B0000}-\\x{BFFFD}\\x{C0000}-\\x{CFFFD}" +
                        "\\x{D0000}-\\x{DFFFD}\\x{E1000}-\\x{EFFFD}!\\$&'\\(\\)\\*\\+,;=:@]))+)(?:\\/(?:(?:%[0-9a-f][" +
                        "0-9a-f]|[-a-z0-9\\._~\\x{A0}-\\x{D7FF}\\x{F900}-\\x{FDCF}\\x{FDF0}-\\x{FFEF}\\x{10000}-" +
                        "\\x{1FFFD}\\x{20000}-\\x{2FFFD}\\x{30000}-\\x{3FFFD}\\x{40000}-\\x{4FFFD}\\x{50000}-" +
                        "\\x{5FFFD}\\x{60000}-\\x{6FFFD}\\x{70000}-\\x{7FFFD}\\x{80000}-\\x{8FFFD}\\x{90000}-" +
                        "\\x{9FFFD}\\x{A0000}-\\x{AFFFD}\\x{B0000}-\\x{BFFFD}\\x{C0000}-\\x{CFFFD}\\x{D0000}-" +
                        "\\x{DFFFD}\\x{E1000}-\\x{EFFFD}!\\$&'\\(\\)\\*\\+,;=:@]))*)*)?|(?:(?:(?:%[0-9a-f][0-9a-f]|[-" +
                        "a-z0-9\\._~\\x{A0}-\\x{D7FF}\\x{F900}-\\x{FDCF}\\x{FDF0}-\\x{FFEF}\\x{10000}-\\x{1FFFD}" +
                        "\\x{20000}-\\x{2FFFD}\\x{30000}-\\x{3FFFD}\\x{40000}-\\x{4FFFD}\\x{50000}-\\x{5FFFD}" +
                        "\\x{60000}-\\x{6FFFD}\\x{70000}-\\x{7FFFD}\\x{80000}-\\x{8FFFD}\\x{90000}-\\x{9FFFD}" +
                        "\\x{A0000}-\\x{AFFFD}\\x{B0000}-\\x{BFFFD}\\x{C0000}-\\x{CFFFD}\\x{D0000}-\\x{DFFFD}" +
                        "\\x{E1000}-\\x{EFFFD}!\\$&'\\(\\)\\*\\+,;=:@]))+)(?:\\/(?:(?:%[0-9a-f][0-9a-f]|[-a-z0-9\\._~" +
                        "\\x{A0}-\\x{D7FF}\\x{F900}-\\x{FDCF}\\x{FDF0}-\\x{FFEF}\\x{10000}-\\x{1FFFD}\\x{20000}-" +
                        "\\x{2FFFD}\\x{30000}-\\x{3FFFD}\\x{40000}-\\x{4FFFD}\\x{50000}-\\x{5FFFD}\\x{60000}-" +
                        "\\x{6FFFD}\\x{70000}-\\x{7FFFD}\\x{80000}-\\x{8FFFD}\\x{90000}-\\x{9FFFD}\\x{A0000}-" +
                        "\\x{AFFFD}\\x{B0000}-\\x{BFFFD}\\x{C0000}-\\x{CFFFD}\\x{D0000}-\\x{DFFFD}\\x{E1000}-" +
                        "\\x{EFFFD}!\\$&'\\(\\)\\*\\+,;=:@]))*)*|(?!(?:%[0-9a-f][0-9a-f]|[-a-z0-9\\._~\\x{A0}-" +
                        "\\x{D7FF}\\x{F900}-\\x{FDCF}\\x{FDF0}-\\x{FFEF}\\x{10000}-\\x{1FFFD}\\x{20000}-\\x{2FFFD}" +
                        "\\x{30000}-\\x{3FFFD}\\x{40000}-\\x{4FFFD}\\x{50000}-\\x{5FFFD}\\x{60000}-\\x{6FFFD}" +
                        "\\x{70000}-\\x{7FFFD}\\x{80000}-\\x{8FFFD}\\x{90000}-\\x{9FFFD}\\x{A0000}-\\x{AFFFD}" +
                        "\\x{B0000}-\\x{BFFFD}\\x{C0000}-\\x{CFFFD}\\x{D0000}-\\x{DFFFD}\\x{E1000}-\\x{EFFFD}!\\$&'\\" +
                        "(\\)\\*\\+,;=:@])))(?:\\?(?:(?:%[0-9a-f][0-9a-f]|[-a-z0-9\\._~\\x{A0}-\\x{D7FF}\\x{F900}-" +
                        "\\x{FDCF}\\x{FDF0}-\\x{FFEF}\\x{10000}-\\x{1FFFD}\\x{20000}-\\x{2FFFD}\\x{30000}-\\x{3FFFD}" +
                        "\\x{40000}-\\x{4FFFD}\\x{50000}-\\x{5FFFD}\\x{60000}-\\x{6FFFD}\\x{70000}-\\x{7FFFD}" +
                        "\\x{80000}-\\x{8FFFD}\\x{90000}-\\x{9FFFD}\\x{A0000}-\\x{AFFFD}\\x{B0000}-\\x{BFFFD}" +
                        "\\x{C0000}-\\x{CFFFD}\\x{D0000}-\\x{DFFFD}\\x{E1000}-\\x{EFFFD}!\\$&'\\(\\)\\*\\+,;=:@])|[" +
                        "\\x{E000}-\\x{F8FF}\\x{F0000}-\\x{FFFFD}\\x{100000}-\\x{10FFFD}\\/\\?])*)?(?:\\#(?:(?:%[0-9a" +
                        "-f][0-9a-f]|[-a-z0-9\\._~\\x{A0}-\\x{D7FF}\\x{F900}-\\x{FDCF}\\x{FDF0}-\\x{FFEF}\\x{10000}-" +
                        "\\x{1FFFD}\\x{20000}-\\x{2FFFD}\\x{30000}-\\x{3FFFD}\\x{40000}-\\x{4FFFD}\\x{50000}-" +
                        "\\x{5FFFD}\\x{60000}-\\x{6FFFD}\\x{70000}-\\x{7FFFD}\\x{80000}-\\x{8FFFD}\\x{90000}-" +
                        "\\x{9FFFD}\\x{A0000}-\\x{AFFFD}\\x{B0000}-\\x{BFFFD}\\x{C0000}-\\x{CFFFD}\\x{D0000}-" +
                        "\\x{DFFFD}\\x{E1000}-\\x{EFFFD}!\\$&'\\(\\)\\*\\+,;=:@])|[\\/\\?])*)?$",
                        Pattern.CASE_INSENSITIVE
                    )
                ),
                "IRI-reference",
                new RegExRule(
                    Pattern.compile(
                        "^[a-z](?:[-a-z0-9\\+\\.])*:(?:\\/\\/(?:(?:%[0-9a-f][0-9a-f]|[-a-z0-9\\._~\\x{A0}-\\x{D7FF}" +
                        "\\x{F900}-\\x{FDCF}\\x{FDF0}-\\x{FFEF}\\x{10000}-\\x{1FFFD}\\x{20000}-\\x{2FFFD}\\x{30000}-" +
                        "\\x{3FFFD}\\x{40000}-\\x{4FFFD}\\x{50000}-\\x{5FFFD}\\x{60000}-\\x{6FFFD}\\x{70000}-" +
                        "\\x{7FFFD}\\x{80000}-\\x{8FFFD}\\x{90000}-\\x{9FFFD}\\x{A0000}-\\x{AFFFD}\\x{B0000}-" +
                        "\\x{BFFFD}\\x{C0000}-\\x{CFFFD}\\x{D0000}-\\x{DFFFD}\\x{E1000}-\\x{EFFFD}!\\$&'\\(\\)\\*\\+," +
                        ";=:])*@)?(?:\\[(?:(?:(?:[0-9a-f]{1,4}:){6}(?:[0-9a-f]{1,4}:[0-9a-f]{1,4}|(?:\\d|[1-9]\\d" +
                        "|1\\d\\d|2[0-4]\\d|25[0-5])(?:\\.(?:\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])){3})|::(?:[0-9a-" +
                        "f]{1,4}:){5}(?:[0-9a-f]{1,4}:[0-9a-f]{1,4}|(?:\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])(?:\\.(" +
                        "?:\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])){3})|(?:[0-9a-f]{1,4})?::(?:[0-9a-f]{1,4}:){4}(?:[" +
                        "0-9a-f]{1,4}:[0-9a-f]{1,4}|(?:\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])(?:\\.(?:\\d|[1-9]\\d|1" +
                        "\\d\\d|2[0-4]\\d|25[0-5])){3})|(?:(?:[0-9a-f]{1,4}:){0,1}[0-9a-f]{1,4})?::(?:[0-9a-f]{1,4}:)" +
                        "{3}(?:[0-9a-f]{1,4}:[0-9a-f]{1,4}|(?:\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])(?:\\.(?:\\d|[1-" +
                        "9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])){3})|(?:(?:[0-9a-f]{1,4}:){0,2}[0-9a-f]{1,4})?::(?:[0-9a-f]" +
                        "{1,4}:){2}(?:[0-9a-f]{1,4}:[0-9a-f]{1,4}|(?:\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])(?:\\.(?:" +
                        "\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])){3})|(?:(?:[0-9a-f]{1,4}:){0,3}[0-9a-f]{1,4})?::[0-9" +
                        "a-f]{1,4}:(?:[0-9a-f]{1,4}:[0-9a-f]{1,4}|(?:\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])(?:\\.(?:" +
                        "\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])){3})|(?:(?:[0-9a-f]{1,4}:){0,4}[0-9a-f]{1,4})?::(?:[" +
                        "0-9a-f]{1,4}:[0-9a-f]{1,4}|(?:\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])(?:\\.(?:\\d|[1-9]\\d|1" +
                        "\\d\\d|2[0-4]\\d|25[0-5])){3})|(?:(?:[0-9a-f]{1,4}:){0,5}[0-9a-f]{1,4})?::[0-9a-f]{1,4}|(?:(" +
                        "?:[0-9a-f]{1,4}:){0,6}[0-9a-f]{1,4})?::)|v[0-9a-f]+\\.[-a-z0-9\\._~!\\$&'\\(\\)\\*\\+,;=:]+)" +
                        "\\]|(?:\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])(?:\\.(?:\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5" +
                        "])){3}|(?:%[0-9a-f][0-9a-f]|[-a-z0-9\\._~\\x{A0}-\\x{D7FF}\\x{F900}-\\x{FDCF}\\x{FDF0}-" +
                        "\\x{FFEF}\\x{10000}-\\x{1FFFD}\\x{20000}-\\x{2FFFD}\\x{30000}-\\x{3FFFD}\\x{40000}-" +
                        "\\x{4FFFD}\\x{50000}-\\x{5FFFD}\\x{60000}-\\x{6FFFD}\\x{70000}-\\x{7FFFD}\\x{80000}-" +
                        "\\x{8FFFD}\\x{90000}-\\x{9FFFD}\\x{A0000}-\\x{AFFFD}\\x{B0000}-\\x{BFFFD}\\x{C0000}-" +
                        "\\x{CFFFD}\\x{D0000}-\\x{DFFFD}\\x{E1000}-\\x{EFFFD}!\\$&'\\(\\)\\*\\+,;=])*)(?::\\d*)?(?:" +
                        "\\/(?:(?:%[0-9a-f][0-9a-f]|[-a-z0-9\\._~\\x{A0}-\\x{D7FF}\\x{F900}-\\x{FDCF}\\x{FDF0}-" +
                        "\\x{FFEF}\\x{10000}-\\x{1FFFD}\\x{20000}-\\x{2FFFD}\\x{30000}-\\x{3FFFD}\\x{40000}-" +
                        "\\x{4FFFD}\\x{50000}-\\x{5FFFD}\\x{60000}-\\x{6FFFD}\\x{70000}-\\x{7FFFD}\\x{80000}-" +
                        "\\x{8FFFD}\\x{90000}-\\x{9FFFD}\\x{A0000}-\\x{AFFFD}\\x{B0000}-\\x{BFFFD}\\x{C0000}-" +
                        "\\x{CFFFD}\\x{D0000}-\\x{DFFFD}\\x{E1000}-\\x{EFFFD}!\\$&'\\(\\)\\*\\+,;=:@]))*)*|\\/(?:(?:(" +
                        "?:(?:%[0-9a-f][0-9a-f]|[-a-z0-9\\._~\\x{A0}-\\x{D7FF}\\x{F900}-\\x{FDCF}\\x{FDF0}-\\x{FFEF}" +
                        "\\x{10000}-\\x{1FFFD}\\x{20000}-\\x{2FFFD}\\x{30000}-\\x{3FFFD}\\x{40000}-\\x{4FFFD}" +
                        "\\x{50000}-\\x{5FFFD}\\x{60000}-\\x{6FFFD}\\x{70000}-\\x{7FFFD}\\x{80000}-\\x{8FFFD}" +
                        "\\x{90000}-\\x{9FFFD}\\x{A0000}-\\x{AFFFD}\\x{B0000}-\\x{BFFFD}\\x{C0000}-\\x{CFFFD}" +
                        "\\x{D0000}-\\x{DFFFD}\\x{E1000}-\\x{EFFFD}!\\$&'\\(\\)\\*\\+,;=:@]))+)(?:\\/(?:(?:%[0-9a-f][" +
                        "0-9a-f]|[-a-z0-9\\._~\\x{A0}-\\x{D7FF}\\x{F900}-\\x{FDCF}\\x{FDF0}-\\x{FFEF}\\x{10000}-" +
                        "\\x{1FFFD}\\x{20000}-\\x{2FFFD}\\x{30000}-\\x{3FFFD}\\x{40000}-\\x{4FFFD}\\x{50000}-" +
                        "\\x{5FFFD}\\x{60000}-\\x{6FFFD}\\x{70000}-\\x{7FFFD}\\x{80000}-\\x{8FFFD}\\x{90000}-" +
                        "\\x{9FFFD}\\x{A0000}-\\x{AFFFD}\\x{B0000}-\\x{BFFFD}\\x{C0000}-\\x{CFFFD}\\x{D0000}-" +
                        "\\x{DFFFD}\\x{E1000}-\\x{EFFFD}!\\$&'\\(\\)\\*\\+,;=:@]))*)*)?|(?:(?:(?:%[0-9a-f][0-9a-f]|[-" +
                        "a-z0-9\\._~\\x{A0}-\\x{D7FF}\\x{F900}-\\x{FDCF}\\x{FDF0}-\\x{FFEF}\\x{10000}-\\x{1FFFD}" +
                        "\\x{20000}-\\x{2FFFD}\\x{30000}-\\x{3FFFD}\\x{40000}-\\x{4FFFD}\\x{50000}-\\x{5FFFD}" +
                        "\\x{60000}-\\x{6FFFD}\\x{70000}-\\x{7FFFD}\\x{80000}-\\x{8FFFD}\\x{90000}-\\x{9FFFD}" +
                        "\\x{A0000}-\\x{AFFFD}\\x{B0000}-\\x{BFFFD}\\x{C0000}-\\x{CFFFD}\\x{D0000}-\\x{DFFFD}" +
                        "\\x{E1000}-\\x{EFFFD}!\\$&'\\(\\)\\*\\+,;=:@]))+)(?:\\/(?:(?:%[0-9a-f][0-9a-f]|[-a-z0-9\\._~" +
                        "\\x{A0}-\\x{D7FF}\\x{F900}-\\x{FDCF}\\x{FDF0}-\\x{FFEF}\\x{10000}-\\x{1FFFD}\\x{20000}-" +
                        "\\x{2FFFD}\\x{30000}-\\x{3FFFD}\\x{40000}-\\x{4FFFD}\\x{50000}-\\x{5FFFD}\\x{60000}-" +
                        "\\x{6FFFD}\\x{70000}-\\x{7FFFD}\\x{80000}-\\x{8FFFD}\\x{90000}-\\x{9FFFD}\\x{A0000}-" +
                        "\\x{AFFFD}\\x{B0000}-\\x{BFFFD}\\x{C0000}-\\x{CFFFD}\\x{D0000}-\\x{DFFFD}\\x{E1000}-" +
                        "\\x{EFFFD}!\\$&'\\(\\)\\*\\+,;=:@]))*)*|(?!(?:%[0-9a-f][0-9a-f]|[-a-z0-9\\._~\\x{A0}-" +
                        "\\x{D7FF}\\x{F900}-\\x{FDCF}\\x{FDF0}-\\x{FFEF}\\x{10000}-\\x{1FFFD}\\x{20000}-\\x{2FFFD}" +
                        "\\x{30000}-\\x{3FFFD}\\x{40000}-\\x{4FFFD}\\x{50000}-\\x{5FFFD}\\x{60000}-\\x{6FFFD}" +
                        "\\x{70000}-\\x{7FFFD}\\x{80000}-\\x{8FFFD}\\x{90000}-\\x{9FFFD}\\x{A0000}-\\x{AFFFD}" +
                        "\\x{B0000}-\\x{BFFFD}\\x{C0000}-\\x{CFFFD}\\x{D0000}-\\x{DFFFD}\\x{E1000}-\\x{EFFFD}!\\$&'\\" +
                        "(\\)\\*\\+,;=:@])))(?:\\?(?:(?:%[0-9a-f][0-9a-f]|[-a-z0-9\\._~\\x{A0}-\\x{D7FF}\\x{F900}-" +
                        "\\x{FDCF}\\x{FDF0}-\\x{FFEF}\\x{10000}-\\x{1FFFD}\\x{20000}-\\x{2FFFD}\\x{30000}-\\x{3FFFD}" +
                        "\\x{40000}-\\x{4FFFD}\\x{50000}-\\x{5FFFD}\\x{60000}-\\x{6FFFD}\\x{70000}-\\x{7FFFD}" +
                        "\\x{80000}-\\x{8FFFD}\\x{90000}-\\x{9FFFD}\\x{A0000}-\\x{AFFFD}\\x{B0000}-\\x{BFFFD}" +
                        "\\x{C0000}-\\x{CFFFD}\\x{D0000}-\\x{DFFFD}\\x{E1000}-\\x{EFFFD}!\\$&'\\(\\)\\*\\+,;=:@])|[" +
                        "\\x{E000}-\\x{F8FF}\\x{F0000}-\\x{FFFFD}\\x{100000}-\\x{10FFFD}\\/\\?])*)?(?:\\#(?:(?:%[0-9a" +
                        "-f][0-9a-f]|[-a-z0-9\\._~\\x{A0}-\\x{D7FF}\\x{F900}-\\x{FDCF}\\x{FDF0}-\\x{FFEF}\\x{10000}-" +
                        "\\x{1FFFD}\\x{20000}-\\x{2FFFD}\\x{30000}-\\x{3FFFD}\\x{40000}-\\x{4FFFD}\\x{50000}-" +
                        "\\x{5FFFD}\\x{60000}-\\x{6FFFD}\\x{70000}-\\x{7FFFD}\\x{80000}-\\x{8FFFD}\\x{90000}-" +
                        "\\x{9FFFD}\\x{A0000}-\\x{AFFFD}\\x{B0000}-\\x{BFFFD}\\x{C0000}-\\x{CFFFD}\\x{D0000}-" +
                        "\\x{DFFFD}\\x{E1000}-\\x{EFFFD}!\\$&'\\(\\)\\*\\+,;=:@])|[\\/\\?])*)?$",
                        Pattern.CASE_INSENSITIVE
                    )
                )
            )
        ),
        Map.entry(
            4122,
            Map.of(
                "UUID",
                new RegExRule(
                    Pattern.compile(
                        "^[0-9A-Fa-f]{8}\\-[0-9A-Fa-f]{4}\\-[0-9A-Fa-f]{4}\\-[0-9A-Fa-f]{4}\\-[0-9A-Fa-f]{12}$"
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
        ),
        Map.entry(
            6570,
            Map.of(
                "URI-Template",
                new RegExRule(
                    Pattern.compile(
                        "(([!#-$&-;=?-\\[\\]_a-z~\\xa0-\\ud7ff\\uf900-\\ufdcf\\ufdf0-\\uffef\\ue000-\\uf8ff]|%[0-9A-F" +
                        "a-f][0-9A-Fa-f])|\\{[+#./;?&=,!@|]?(\\w|%[0-9A-Fa-f][0-9A-Fa-f])((\\.)?(\\w|%[0-9A-" +
                        "Fa-f][0-9A-Fa-f]))*(:[1-9]\\d{0,3}|\\*)?(,(\\w|%[0-9A-Fa-f][0-9A-Fa-f])((\\.)?(\\w|%[0-9A-Fa" +
                        "-f][0-9A-Fa-f]))*(:[1-9]\\d{0,3}|\\*)?)*\\})*"
                    )
                )
            )
        ),
        Map.entry(
            6901,
            Map.of("json-pointer", new RegExRule(Pattern.compile("^(/([\\x00-.0-}\\x7f-\\u010ffff]|\\~[01])*)*$")))
        ),
        //draft -> use expired date as number :)
        Map.entry(
            20180723,
            Map.of(
                "relative-json-pointer",
                new RegExRule(
                    Pattern.compile(
                        "^(0|[1-9][0-9]*)((/([\\x00-.0-}\\x7f-\\u010ffff]|\\~[01])*)*|#)$",
                        Pattern.CASE_INSENSITIVE
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
