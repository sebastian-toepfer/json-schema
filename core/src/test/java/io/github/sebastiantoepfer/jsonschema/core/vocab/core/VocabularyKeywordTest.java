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
package io.github.sebastiantoepfer.jsonschema.core.vocab.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;

import io.github.sebastiantoepfer.ddd.media.core.HashMapMedia;
import io.github.sebastiantoepfer.jsonschema.Vocabulary;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import io.github.sebastiantoepfer.jsonschema.vocabulary.spi.LazyVocabularies;
import io.github.sebastiantoepfer.jsonschema.vocabulary.spi.ListVocabulary;
import io.github.sebastiantoepfer.jsonschema.vocabulary.spi.VocabularyDefinition;
import jakarta.json.Json;
import jakarta.json.JsonValue;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.jupiter.api.Test;

class VocabularyKeywordTest {

    @Test
    void should_created_keyword_should_know_his_name() {
        final Keyword vocabulary = new VocabularyKeyword(JsonValue.EMPTY_JSON_OBJECT, Stream::empty);

        assertThat(vocabulary.hasName("$vocabulary"), is(true));
        assertThat(vocabulary.hasName("$id"), is(false));
    }

    @Test
    void should_create_definitions() {
        assertThat(
            new VocabularyKeyword(
                Json.createObjectBuilder()
                    .add("https://json-schema.org/draft/2020-12/vocab/core", true)
                    .add("http://openapi.org/test", false)
                    .build(),
                Stream::empty
            )
                .definitions()
                .toList(),
            containsInAnyOrder(
                new VocabularyDefinitionMatcher(URI.create("https://json-schema.org/draft/2020-12/vocab/core"), true),
                new VocabularyDefinitionMatcher(URI.create("http://openapi.org/test"), false)
            )
        );
    }

    @Test
    void should_be_printable() {
        assertThat(
            new VocabularyKeyword(
                Json.createObjectBuilder()
                    .add("https://json-schema.org/draft/2020-12/vocab/core", true)
                    .add("http://openapi.org/test", false)
                    .build(),
                Stream::empty
            ).printOn(new HashMapMedia()),
            (Matcher) hasEntry(
                is("$vocabulary"),
                allOf(
                    hasEntry("https://json-schema.org/draft/2020-12/vocab/core", true),
                    hasEntry("http://openapi.org/test", false)
                )
            )
        );
    }

    @Test
    void should_not_lazy_load_offical_vocabs() {
        //security and peformance. it should not be possible to override keywords define by this module!
        assertThat(
            new VocabularyKeyword(
                Json.createObjectBuilder().add("https://json-schema.org/draft/2020-12/vocab/core", true).build(),
                Stream::empty
            )
                .definitions()
                .map(VocabularyDefinition::findVocabulary)
                .flatMap(Optional::stream)
                .map(Vocabulary::id)
                .toList(),
            containsInAnyOrder(URI.create("https://json-schema.org/draft/2020-12/vocab/core"))
        );
    }

    @Test
    void should_provide_vocab_which_know_they_id() {
        final VocabularyDefinition vocabDef = new VocabularyKeyword(
            Json.createObjectBuilder().add("https://json-schema.org/draft/2020-12/vocab/core", true).build(),
            Stream::empty
        )
            .definitions()
            .findFirst()
            .orElseThrow();

        assertThat(vocabDef.hasid(URI.create("https://json-schema.org/draft/2020-12/vocab/core")), is(true));
        assertThat(vocabDef.hasid(URI.create("http://openapi.org/test")), is(false));
    }

    @Test
    void should_lazy_load_non_offical_vocabs() {
        assertThat(
            new VocabularyKeyword(
                Json.createObjectBuilder()
                    .add("https://json-schema.org/draft/2020-12/vocab/core", true)
                    .add("http://openapi.org/test", false)
                    .build(),
                () ->
                    Stream.of(
                        new LazyVocabularies() {
                            @Override
                            public Optional<Vocabulary> loadVocabularyWithId(final URI id) {
                                return Optional.of(new ListVocabulary(id, List.of()));
                            }
                        }
                    )
            )
                .definitions()
                .map(VocabularyDefinition::findVocabulary)
                .flatMap(Optional::stream)
                .map(Vocabulary::id)
                .toList(),
            containsInAnyOrder(
                URI.create("https://json-schema.org/draft/2020-12/vocab/core"),
                URI.create("http://openapi.org/test")
            )
        );
    }

    private static class VocabularyDefinitionMatcher extends TypeSafeMatcher<VocabularyDefinition> {

        private final URI id;
        private final Matcher<Boolean> required;

        public VocabularyDefinitionMatcher(final URI id) {
            this(id, null);
        }

        public VocabularyDefinitionMatcher(final URI id, final Boolean required) {
            super(VocabularyDefinition.class);
            this.id = Objects.requireNonNull(id);
            this.required = required == null ? either(is(true)).or(is(false)) : is(required);
        }

        @Override
        protected boolean matchesSafely(final VocabularyDefinition item) {
            return item.hasid(id) && required.matches(item.isRequired());
        }

        @Override
        public void describeTo(final Description description) {
            description
                .appendText("Vocabulary definition with id: ")
                .appendValue(id)
                .appendText(" and required: ")
                .appendDescriptionOf(required);
        }
    }
}
