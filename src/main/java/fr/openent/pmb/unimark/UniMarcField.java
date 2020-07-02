package fr.openent.pmb.unimark;

import java.util.HashMap;
import java.util.Map;

public enum UniMarcField {
    ID("001", "id", null),
    ISBN("010", "isbn", "a"),
    TITLE("200", "title", "a"),
    EDITOR("210", "publication", "c"),
    DESCRIPTION("330", "description", "a"),
    METADATA("610", "metadata", "a"),
    AUTHOR1("700", "author", "a"),
    AUTHOR2("701", "author", "a"),
    AUTHOR3("702", "author", "a"),
    LINK("856", "link", "u"),
    IMAGE("896", "image", "a"),
    DOCUMENT_TYPE("995", "document_type", "r");

    private final String code;
    private final String fieldName;
    private final String required;

    private static final Map<String, UniMarcField> lookup = new HashMap<>();

    UniMarcField(String code, String fieldName, String required) {
        this.code = code;
        this.fieldName = fieldName;
        this.required = required;
    }

    static {
        for (UniMarcField u : UniMarcField.values()) {
            lookup.put(u.code(), u);
        }
    }

    public static boolean containsCode(String code) {
        return lookup.containsKey(code);
    }

    public static UniMarcField get(String code) {
        return lookup.get(code);
    }

    public String code() {
        return this.code;
    }

    public String fieldName() {
        return this.fieldName;
    }

    public String required() {
        return this.required;
    }

}
