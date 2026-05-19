package com.example.showcaseapp.data.remote

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.inject.Inject

class MockBackendInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val segments = request.url.encodedPathSegments
        val body = when {
            segments == listOf("apps") -> appsJson
            segments.size == 2 && segments.first() == "apps" -> findAppJson(segments[1])
            segments == listOf("categories") -> categoriesJson
            segments == listOf("popular") -> popularJson
            else -> null
        }

        return Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(if (body == null) 404 else 200)
            .message(if (body == null) "Not found" else "OK")
            .body((body ?: """{"error":"Not found"}""").toResponseBody(JSON))
            .build()
    }

    private fun findAppJson(id: String): String? {
        val marker = """"id":"$id""""
        val startIndex = appsJson.indexOf(marker)
        if (startIndex == -1) return null

        var objectStart = startIndex
        while (objectStart > 0 && appsJson[objectStart] != '{') objectStart--

        var depth = 0
        for (index in objectStart until appsJson.length) {
            when (appsJson[index]) {
                '{' -> depth++
                '}' -> {
                    depth--
                    if (depth == 0) return appsJson.substring(objectStart, index + 1)
                }
            }
        }
        return null
    }

    private val popularJson: String
        get() = """
            [
              ${findAppJson("budgetrail").orEmpty()},
              ${findAppJson("trackmate").orEmpty()},
              ${findAppJson("civicpass").orEmpty()}
            ]
        """.trimIndent()

    private val categoriesJson = """
        [
          {"name":"Финансы","count":2},
          {"name":"Инструменты","count":2},
          {"name":"Игры","count":2},
          {"name":"Транспорт","count":2},
          {"name":"Государственные","count":2}
        ]
    """.trimIndent()

    private val appsJson = """
        [
          {
            "id":"budgetrail",
            "name":"BudgetRail",
            "developer":"Ledger Point",
            "category":"Финансы",
            "shortDescription":"Планирование бюджета по дням и целям",
            "fullDescription":"BudgetRail помогает распределять доходы по категориям, следить за лимитами и заранее видеть свободный остаток до следующей зарплаты.",
            "ageRating":"0+",
            "iconGradient":["#C94F2D","#F2A65A"],
            "popular":true,
            "screenshots":[
              {"id":"budgetrail-1","title":"Лимиты месяца","accentColor":"#C94F2D","backgroundColor":"#FFF1E8"},
              {"id":"budgetrail-2","title":"Цели","accentColor":"#087B67","backgroundColor":"#E8F6F2"},
              {"id":"budgetrail-3","title":"Динамика","accentColor":"#B98200","backgroundColor":"#FFF6D8"}
            ]
          },
          {
            "id":"splitly",
            "name":"Splitly",
            "developer":"Fair Share Apps",
            "category":"Финансы",
            "shortDescription":"Разделение общих расходов за пару касаний",
            "fullDescription":"Splitly считает доли в поездках, встречах и семейных покупках, а затем показывает понятный список кто кому должен.",
            "ageRating":"6+",
            "iconGradient":["#087B67","#2C9CA0"],
            "popular":false,
            "screenshots":[
              {"id":"splitly-1","title":"Счет компании","accentColor":"#087B67","backgroundColor":"#E8F6F2"},
              {"id":"splitly-2","title":"Участники","accentColor":"#C94F2D","backgroundColor":"#FFF1E8"},
              {"id":"splitly-3","title":"Итог","accentColor":"#2C9CA0","backgroundColor":"#E7F6F7"}
            ]
          },
          {
            "id":"quickpin",
            "name":"QuickPin",
            "developer":"Craft Tools",
            "category":"Инструменты",
            "shortDescription":"Закрепленные заметки и быстрые напоминания",
            "fullDescription":"QuickPin хранит короткие заметки, списки и важные ссылки на главном экране, чтобы нужная информация была всегда рядом.",
            "ageRating":"0+",
            "iconGradient":["#7A5CFF","#087B67"],
            "popular":false,
            "screenshots":[
              {"id":"quickpin-1","title":"Доска","accentColor":"#7A5CFF","backgroundColor":"#F1EEFF"},
              {"id":"quickpin-2","title":"Напоминание","accentColor":"#087B67","backgroundColor":"#E8F6F2"},
              {"id":"quickpin-3","title":"Метки","accentColor":"#B98200","backgroundColor":"#FFF6D8"}
            ]
          },
          {
            "id":"filedock",
            "name":"FileDock",
            "developer":"Northbyte",
            "category":"Инструменты",
            "shortDescription":"Сортировка документов, фото и архивов",
            "fullDescription":"FileDock помогает быстро находить файлы, раскладывать их по коллекциям и очищать память от повторов.",
            "ageRating":"0+",
            "iconGradient":["#3E6C9A","#C94F2D"],
            "popular":false,
            "screenshots":[
              {"id":"filedock-1","title":"Папки","accentColor":"#3E6C9A","backgroundColor":"#EBF2F8"},
              {"id":"filedock-2","title":"Дубликаты","accentColor":"#C94F2D","backgroundColor":"#FFF1E8"},
              {"id":"filedock-3","title":"Поиск","accentColor":"#087B67","backgroundColor":"#E8F6F2"}
            ]
          },
          {
            "id":"orbitquest",
            "name":"Orbit Quest",
            "developer":"Nova Play",
            "category":"Игры",
            "shortDescription":"Космическая аркада с короткими миссиями",
            "fullDescription":"Orbit Quest предлагает быстрые полеты, сбор ресурсов и улучшение корабля между уровнями.",
            "ageRating":"8+",
            "iconGradient":["#2D3A8C","#C94F2D"],
            "popular":false,
            "screenshots":[
              {"id":"orbitquest-1","title":"Орбита","accentColor":"#2D3A8C","backgroundColor":"#EEF0FF"},
              {"id":"orbitquest-2","title":"Ангар","accentColor":"#C94F2D","backgroundColor":"#FFF1E8"},
              {"id":"orbitquest-3","title":"Миссии","accentColor":"#B98200","backgroundColor":"#FFF6D8"}
            ]
          },
          {
            "id":"wordforge",
            "name":"WordForge",
            "developer":"Puzzle Mill",
            "category":"Игры",
            "shortDescription":"Словесные головоломки на каждый день",
            "fullDescription":"WordForge предлагает набор коротких словесных задач, ежедневные испытания и спокойный режим без таймера.",
            "ageRating":"6+",
            "iconGradient":["#087B67","#B98200"],
            "popular":false,
            "screenshots":[
              {"id":"wordforge-1","title":"Поле","accentColor":"#087B67","backgroundColor":"#E8F6F2"},
              {"id":"wordforge-2","title":"Задание","accentColor":"#B98200","backgroundColor":"#FFF6D8"},
              {"id":"wordforge-3","title":"Серия","accentColor":"#C94F2D","backgroundColor":"#FFF1E8"}
            ]
          },
          {
            "id":"trackmate",
            "name":"TrackMate",
            "developer":"Urban Routes",
            "category":"Транспорт",
            "shortDescription":"Маршруты транспорта и избранные остановки",
            "fullDescription":"TrackMate показывает ближайшие остановки, строит маршрут по городу и сохраняет часто используемые направления.",
            "ageRating":"0+",
            "iconGradient":["#087B67","#3E6C9A"],
            "popular":true,
            "screenshots":[
              {"id":"trackmate-1","title":"Остановки","accentColor":"#087B67","backgroundColor":"#E8F6F2"},
              {"id":"trackmate-2","title":"Маршрут","accentColor":"#3E6C9A","backgroundColor":"#EBF2F8"},
              {"id":"trackmate-3","title":"Избранное","accentColor":"#B98200","backgroundColor":"#FFF6D8"}
            ]
          },
          {
            "id":"railwaynow",
            "name":"Railway Now",
            "developer":"Station Lab",
            "category":"Транспорт",
            "shortDescription":"Расписание поездов и напоминания о рейсах",
            "fullDescription":"Railway Now помогает отслеживать поездки, сохранять билеты и получать напоминания перед отправлением.",
            "ageRating":"0+",
            "iconGradient":["#C94F2D","#3E6C9A"],
            "popular":false,
            "screenshots":[
              {"id":"railwaynow-1","title":"Расписание","accentColor":"#C94F2D","backgroundColor":"#FFF1E8"},
              {"id":"railwaynow-2","title":"Билет","accentColor":"#3E6C9A","backgroundColor":"#EBF2F8"},
              {"id":"railwaynow-3","title":"Платформа","accentColor":"#087B67","backgroundColor":"#E8F6F2"}
            ]
          },
          {
            "id":"civicpass",
            "name":"CivicPass",
            "developer":"City Digital Office",
            "category":"Государственные",
            "shortDescription":"Городские услуги и цифровые обращения",
            "fullDescription":"CivicPass объединяет записи, обращения и статусы заявлений в одном понятном личном кабинете.",
            "ageRating":"0+",
            "iconGradient":["#3E6C9A","#087B67"],
            "popular":true,
            "screenshots":[
              {"id":"civicpass-1","title":"Услуги","accentColor":"#3E6C9A","backgroundColor":"#EBF2F8"},
              {"id":"civicpass-2","title":"Обращения","accentColor":"#087B67","backgroundColor":"#E8F6F2"},
              {"id":"civicpass-3","title":"Статусы","accentColor":"#B98200","backgroundColor":"#FFF6D8"}
            ]
          },
          {
            "id":"docqueue",
            "name":"DocQueue",
            "developer":"Public Service Hub",
            "category":"Государственные",
            "shortDescription":"Запись в ведомства и контроль очереди",
            "fullDescription":"DocQueue показывает доступные окна приема, хранит талоны и напоминает о визите заранее.",
            "ageRating":"0+",
            "iconGradient":["#B98200","#C94F2D"],
            "popular":false,
            "screenshots":[
              {"id":"docqueue-1","title":"Запись","accentColor":"#B98200","backgroundColor":"#FFF6D8"},
              {"id":"docqueue-2","title":"Талон","accentColor":"#C94F2D","backgroundColor":"#FFF1E8"},
              {"id":"docqueue-3","title":"История","accentColor":"#3E6C9A","backgroundColor":"#EBF2F8"}
            ]
          }
        ]
    """.trimIndent()

    private companion object {
        val JSON = "application/json; charset=utf-8".toMediaType()
    }
}
