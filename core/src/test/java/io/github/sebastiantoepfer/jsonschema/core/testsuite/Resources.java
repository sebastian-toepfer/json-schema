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
package io.github.sebastiantoepfer.jsonschema.core.testsuite;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

final class Resources {

    private final String baseDir;

    public Resources(final String baseDir) {
        this.baseDir = baseDir;
    }

    @com.google.errorprone.annotations.MustBeClosed
    Stream<Resource> all() {
        try {
            return StreamSupport
                .stream(
                    Spliterators.spliteratorUnknownSize(
                        Resource.class.getClassLoader().getResources(baseDir).asIterator(),
                        Spliterator.ORDERED
                    ),
                    false
                )
                .map(url -> {
                    try {
                        return url.toURI();
                    } catch (URISyntaxException e) {
                        throw new IllegalStateException(e);
                    }
                })
                .filter(uri -> "file".equals(uri.getScheme()))
                .map(Paths::get)
                .map(PathResources::new)
                .flatMap(PathResources::toResources);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private URI baseUri() {
        try {
            return Resource.class.getClassLoader().getResource(baseDir).toURI();
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }

    private class PathResources {

        private final Path basePath;
        private final Path path;

        public PathResources(final Path path) {
            this.path = Objects.requireNonNull(path);
            this.basePath = Paths.get(baseUri()).getParent();
        }

        @SuppressWarnings("StreamResourceLeak")
        Stream<Resource> toResources() {
            final Stream<Resource> result;
            try {
                if (path.toFile().isFile()) {
                    if (path.startsWith(basePath)) {
                        result = Stream.of(new Resource(basePath.relativize(path).toString()));
                    } else {
                        result = Stream.of(new Resource(path.toString()));
                    }
                } else {
                    result = Files.list(path).map(PathResources::new).flatMap(PathResources::toResources);
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
            return result;
        }
    }
}
