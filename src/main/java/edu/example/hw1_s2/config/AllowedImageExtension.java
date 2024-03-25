package edu.example.hw1_s2.config;

public enum AllowedImageExtension {
    PNG,
    JPG;

    public static AllowedImageExtension caseIgnoreValueOf(String value) {
        for (AllowedImageExtension enumValue : values()) {
            if (enumValue.name().equalsIgnoreCase(value)) {
                return enumValue;
            }
        }
        throw new IllegalArgumentException("No enum constant " + AllowedImageExtension.class + "." + value);
    }
}
