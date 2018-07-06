package com.arny.celestiatools.utils

import com.arny.celestiatools.controller.Controller

fun getTitle(method: String): String {
    var message = "Операция"
    when (method) {
        "json" -> message = "Парсинг"
        Controller.METHOD_DOWNLOAD -> message = "Загрузка"
        "unzip" -> message = "Распаковка"
        "writessc" -> message = "Запись орбит"
        "dbwrite" -> message = "Обновление БД"
    }
    return message
}

fun getMessage(success: Boolean, method: String): String {
    var message = "Операция завершена"
    if (success) {
        when (method) {
            "json" -> message = "Парсинг закончен успешно"
            "download" -> message = "Файл загружен"
            "unzip" -> message = "Файл распакован"
            "writessc" -> message = "Орбиты записаны"
            "dbwrite" -> message = "База обновлена"
        }
    } else {
        when (method) {
            "json" -> message = "Ошибка парсинга"
            "download" -> message = "Ошибка загрузки файла"
            "unzip" -> message = "Файл не распакован"
            "dbwrite" -> message = "База не обновлена"
        }
    }

    return message
}

fun String?.ifNull(string: String): String {
    val blank = this?.isBlank() ?: true
    return if (blank) string else this!!
}

fun <T> Any?.ifNull(default: T): T {
    if (this == null) return default
    return this as T
}