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
package io.github.sebastiantoepfer.jsonschema.testsuite.junit;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

final class Resources {

    private final String baseDir;

    public Resources() {
        this(".");
    }

    public Resources(final String baseDir) {
        this.baseDir = baseDir;
    }

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

    private static class PathResources {

        private static final Pattern PATH_SEPARATOR = Pattern.compile(File.pathSeparator);
        private final Path path;

        public PathResources(final Path path) {
            this.path = Objects.requireNonNull(path);
        }

        Stream<Resource> toResources() {
            final Stream<Resource> result;
            try {
                if (path.toFile().isFile()) {
                    result =
                        Stream.of(
                            new Resource(
                                path
                                    .toFile()
                                    .getAbsolutePath()
                                    .substring(
                                        PATH_SEPARATOR
                                            .splitAsStream(System.getProperty("java.class.path"))
                                            .map(Path::of)
                                            .filter(path::startsWith)
                                            .map(Path::toFile)
                                            .map(File::getAbsolutePath)
                                            .map(String::length)
                                            .map(i -> i + 1)
                                            .findAny()
                                            .orElse(0)
                                    )
                            )
                        );
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
