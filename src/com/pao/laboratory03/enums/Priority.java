package com.pao.laboratory03.enums;

public enum Priority {
    LOW(1, "green") {
        @Override
        public String getEmoji() { return "\uD83D\uDD34"; }
    },
    MEDIUM(2, "yellow") {
        @Override
        public String getEmoji() { return "\uD83D\uDFE0"; }
    },
    HIGH(3, "orange") {
        @Override
        public String getEmoji() { return "\uD83D\uDFE1"; }
    },
    CRITICAL(4, "red") {
        @Override
        public String getEmoji() { return "\uD83D\uDFE2"; }
    };

    private final int level;
    private final String color;

    // Constructorul este privat implicit pentru enum-uri
    Priority(int level, String color) {
        this.level = level;
        this.color = color;
    }

    public int getLevel() { return level; }
    public String getColor() { return color; }

    // Metodă abstractă implementată de fiecare constantă în parte
    public abstract String getEmoji();
}