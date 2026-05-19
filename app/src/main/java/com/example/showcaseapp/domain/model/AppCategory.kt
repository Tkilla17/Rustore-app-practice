package com.example.showcaseapp.domain.model

enum class AppCategory(val title: String) {
    FINANCE("Финансы"),
    TOOLS("Инструменты"),
    GAMES("Игры"),
    GOVERNMENT("Государственные"),
    TRANSPORT("Транспорт");

    companion object {
        fun fromTitle(title: String): AppCategory =
            entries.firstOrNull { it.title == title } ?: TOOLS
    }
}
