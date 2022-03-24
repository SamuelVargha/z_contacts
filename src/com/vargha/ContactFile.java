package com.vargha;

import java.nio.file.Path;
import java.util.List;

public class ContactFile {
    private final Path path;
    private final List<String> content;

    public ContactFile(Path path, List<String> content) {
        this.path = path;
        this.content = content;
    }

    public Path getPath() {
        return path;
    }

    public List<String> getContent() {
        return content;
    }
}