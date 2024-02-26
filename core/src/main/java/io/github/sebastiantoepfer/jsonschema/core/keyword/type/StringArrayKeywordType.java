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
package io.github.sebastiantoepfer.jsonschema.core.keyword.type;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import io.github.sebastiantoepfer.common.condition4j.core.AllOf;
import io.github.sebastiantoepfer.common.condition4j.core.ContainsOnlyItemsWhichFulfilThe;
import io.github.sebastiantoepfer.common.condition4j.core.MappedToFullfillThe;
import io.github.sebastiantoepfer.common.condition4j.core.PredicateCondition;
import io.github.sebastiantoepfer.common.condition4j.json.JsonPropertyWhichFulfilThe;
import io.github.sebastiantoepfer.jsonschema.InstanceType;
import io.github.sebastiantoepfer.jsonschema.JsonSchema;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import io.github.sebastiantoepfer.jsonschema.keyword.KeywordType;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;

public final class StringArrayKeywordType implements KeywordType {

    private final JsonProvider jsonContext;
    private final String name;
    private final Function<Collection<String>, Keyword> keywordCreator;

    public StringArrayKeywordType(
        final JsonProvider jsonContext,
        final String name,
        final Function<Collection<String>, Keyword> keywordCreator
    ) {
        this.jsonContext = Objects.requireNonNull(jsonContext);
        this.name = Objects.requireNonNull(name);
        this.keywordCreator = Objects.requireNonNull(keywordCreator);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Keyword createKeyword(final JsonSchema schema) {
        return createKeyword(schema.asJsonObject());
    }

    private Keyword createKeyword(final JsonObject schema) {
        if (
            new JsonPropertyWhichFulfilThe(
                jsonContext.createPointer(String.format("/%s", name)),
                new AllOf<>(
                    new PredicateCondition<>(InstanceType.ARRAY::isInstance),
                    new MappedToFullfillThe<>(
                        new ContainsOnlyItemsWhichFulfilThe<>(
                            new PredicateCondition<>(InstanceType.STRING::isInstance)
                        ),
                        JsonValue::asJsonArray
                    )
                )
            ).isFulfilledBy(schema)
        ) {
            return schema
                .getJsonArray(name)
                .stream()
                .map(JsonString.class::cast)
                .map(JsonString::getString)
                .collect(collectingAndThen(toList(), keywordCreator::apply));
        } else {
            throw new IllegalArgumentException();
        }
    }
}
