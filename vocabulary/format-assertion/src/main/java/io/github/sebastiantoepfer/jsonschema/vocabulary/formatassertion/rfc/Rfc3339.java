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

//create regex with: https://abnf.msweet.org/index.php
public final class Rfc3339 implements Rfc {

    private static final String DATE = "\\d{4}\\-\\d{2}\\-\\d{2}";
    private static final String TIMEZONE = "([Zz]|([+-]\\d{2}\\:\\d{2}))";
    private static final String TIME = "\\d{2}\\:\\d{2}\\:\\d{2}(\\.\\d+)?" + TIMEZONE;

    private static final Map<String, Pattern> RULES = Map.of(
        "date-time",
        Pattern.compile("^" + DATE + "[Tt]" + TIME + "$"),
        "full-date",
        Pattern.compile("^" + DATE + "$"),
        "full-time",
        Pattern.compile("^" + TIME + "$"),
        "duration",
        Pattern.compile(
            "^" +
            "([Pp]((" +
            "(\\d+[Dd]|\\d+[Mm](\\d+[Dd])?|\\d+[Yy](\\d+[Mm](\\d+[Dd])?)?)" +
            "(([Tt](\\d+[Hh](\\d+[Mm](\\d+[Ss])?)?|\\d+[Mm](\\d+[Ss])?|\\d+[Ss])))?" +
            ")|([Tt](\\d+[Hh](\\d+[Mm](\\d+[Ss])?)?|\\d+[Mm](\\d+[Ss])?|\\d+[Ss]))|\\d+[Ww]))" +
            "$"
        )
    );

    @Override
    public Optional<Rule> findRuleByName(final String ruleName) {
        return Optional.ofNullable(RULES.get(ruleName)).map(RegExRule::new);
    }
}
