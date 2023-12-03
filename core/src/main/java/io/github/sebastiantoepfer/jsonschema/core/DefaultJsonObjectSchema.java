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
package io.github.sebastiantoepfer.jsonschema.core;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import io.github.sebastiantoepfer.jsonschema.JsonSubSchema;
import io.github.sebastiantoepfer.jsonschema.Validator;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import jakarta.json.JsonObject;
import java.util.Optional;
import java.util.stream.Stream;

public final class DefaultJsonObjectSchema extends AbstractJsonValueSchema {

    public DefaultJsonObjectSchema(final JsonObject value) {
        super(value);
    }

    @Override
    public Validator validator() {
        return keywords().collect(collectingAndThen(toList(), KeywordBasedValidator::new));
    }

    @Override
    public Optional<Keyword> keywordByName(final String name) {
        return keywords().filter(k -> k.hasName(name)).findFirst();
    }

    private Stream<Keyword> keywords() {
        final Keywords keywords = new KeywordExtractor(this).createKeywords();
        return asJsonObject().keySet().stream().map(propertyName -> keywords.createKeywordFor(this, propertyName));
    }

    @Override
    public Optional<JsonSubSchema> asSubSchema(final String name) {
        return Optional
            .ofNullable(asJsonObject().get(name))
            .flatMap(new DefaultJsonSchemaFactory()::tryToCreateSchemaFrom)
            .map(subSchema -> new DefaultJsonSubSchema(this, subSchema));
    }
}
