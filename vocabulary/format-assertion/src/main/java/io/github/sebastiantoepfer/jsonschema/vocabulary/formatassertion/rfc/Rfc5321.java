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

public final class Rfc5321 implements Rfc {

    private static final Map<String, Pattern> RULES = Map.of(
        "mailbox",
        Pattern.compile(
            "^(([A-Za-z0-9!#$%&'*+-/=?\\^_`{|}~]+" +
            "(\\.[A-Za-z0-9!#$%&'*+-/=?\\^_`{|}~]+)*|\"" +
            "(([ -\\!#-\\[]|[\\]-~])|\\\\[ -~])*\")@([A-Za-z0-9](([-A-Za-z0-9]*[A-Za-z0-9]))?" +
            "(\\.[A-Za-z0-9](([-A-Za-z0-9]*[A-Za-z0-9]))?)*|(\\[(\\d{1,3}(\\.\\d{1,3}){3}|[Ii][Pp][Vv]6\\:" +
            "([0-9A-Fa-f]{1,4}(\\:[0-9A-Fa-f]{1,4}){7}|([0-9A-Fa-f]{1,4}(\\:[0-9A-Fa-f]{1,4}){0,5})?\\:\\:" +
            "([0-9A-Fa-f]{1,4}(\\:[0-9A-Fa-f]{1,4}){0,5})?|[0-9A-Fa-f]{1,4}(\\:[0-9A-Fa-f]{1,4}){5}\\:\\d{1,3}" +
            "(\\.\\d{1,3}){3}|([0-9A-Fa-f]{1,4}(\\:[0-9A-Fa-f]{1,4}){0,3})?\\:\\:([0-9A-Fa-f]{1,4}" +
            "(\\:[0-9A-Fa-f]{1,4}){0,3}\\:)?\\d{1,3}(\\.\\d{1,3}){3})|([-A-Za-z0-9]*[A-Za-z0-9])" +
            "\\:[\\!-Z\\^-~]+)\\])))$"
        )
    );

    @Override
    public Optional<Rule> findRuleByName(final String ruleName) {
        return Optional.ofNullable(RULES.get(ruleName)).map(RegExRule::new);
    }
}
