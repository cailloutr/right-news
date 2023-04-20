package com.cailloutr.rightnews.constants

import com.cailloutr.rightnews.data.network.responses.news.NewsFields
import com.cailloutr.rightnews.data.network.responses.news.NewsResponse
import com.cailloutr.rightnews.data.network.responses.news.NewsResult
import com.cailloutr.rightnews.data.network.responses.news.NewsRoot
import com.cailloutr.rightnews.data.network.responses.sections.SectionsEdition
import com.cailloutr.rightnews.data.network.responses.sections.SectionsResponse
import com.cailloutr.rightnews.data.network.responses.sections.SectionsResult
import com.cailloutr.rightnews.data.network.responses.sections.SectionsRoot
import com.cailloutr.rightnews.enums.Code
import retrofit2.Response

object Constants {
    const val BASE_URL = "https://content.guardianapis.com/"

    const val API_CALL_FIELDS = "trailText,thumbnail,headline,body"

    const val FIRST_SECTIONS_ID = "books"

    const val NETWORK_ERROR_MESSAGE = "Error loading new articles"

    const val SEARCH_QUERY = "home"

    const val TEST_DB = "test_db"

    const val LATEST_NEWS = "latest_news"

    const val SEARCH_RESULT = "search_result"

    const val API_INITIAL_INDEX = 1

    const val PAGE_SIZE = 10

    const val DATABASE_NAME = "news_db"

    const val ROOM_NEWS_CONTAINER_DEFAULT_SECTION = "article"

    const val NETWORK_ERROR = "network"

    val fakeArticle = NewsRoot(
        response = NewsResponse(
            status = "ok",
            userTier = "developer",
            total = 9526,
            startIndex = 1,
            pages = 10,
            pageSize = 50,
            currentPage = 1,
            orderBy = "newest",
            results = listOf(
                NewsResult(
                    id = "games/2023/mar/28/super-mario-lush-soaps1",
                    type = "article",
                    sectionId = "article",
                    sectionName = "Games",
                    webPublicationDate = "2023-03-28T12:15:18Z",
                    webTitle = "Luigi has sweet notes of apple’: testing out Lush’s unlikely Super Mario soaps",
                    webUrl = "https://www.theguardian.com/games/2023/mar/28/super-mario-lush-soaps",
                    apiUrl = "https://content.guardianapis.com/games/2023/mar/28/super-mario-lush-soaps",
                    fields = NewsFields(
                        headline = "Luigi has sweet notes of apple’: testing out Lush’s unlikely Super Mario soaps",
                        trailText = "Animal-friendly cosmetics brand Lush is releasing a range of Mario-themed products – so our reporter tried them, for science",
                        thumbnail = "https://media.guim.co.uk/c6436ebbf6e5eceee60a496698e4fc5004c176db/0_327_2000_1200/500.jpg",
                        body = "Luigi has sweet notes of apple’: testing out Lush’s unlikely Super Mario soaps"
                    ),
                    isHosted = false,
                    pillarId = "pillar/arts",
                    pillarName = "Arts"
                ),
                NewsResult(
                    id = "games/2023/mar/28/super-mario-lush-soaps2",
                    type = "article",
                    sectionId = "news",
                    sectionName = "Article",
                    webPublicationDate = "2023-03-28T12:15:18Z",
                    webTitle = "Luigi has sweet notes of apple’: testing out Lush’s unlikely Super Mario soaps",
                    webUrl = "https://www.theguardian.com/games/2023/mar/28/super-mario-lush-soaps",
                    apiUrl = "https://content.guardianapis.com/games/2023/mar/28/super-mario-lush-soaps",
                    fields = NewsFields(
                        headline = "Luigi has sweet notes of apple’: testing out Lush’s unlikely Super Mario soaps",
                        trailText = "Animal-friendly cosmetics brand Lush is releasing a range of Mario-themed products – so our reporter tried them, for science",
                        thumbnail = "https://media.guim.co.uk/c6436ebbf6e5eceee60a496698e4fc5004c176db/0_327_2000_1200/500.jpg",
                        body = "Luigi has sweet notes of apple’: testing out Lush’s unlikely Super Mario soaps"
                    ),
                    isHosted = false,
                    pillarId = "pillar/arts",
                    pillarName = "Arts"
                )
            )
        )
    )

    val fakeSearchArticle = NewsRoot(
        response = NewsResponse(
            status = "ok",
            userTier = "developer",
            total = 9526,
            startIndex = 1,
            pages = 10,
            pageSize = 50,
            currentPage = 1,
            orderBy = "newest",
            results = listOf(
                NewsResult(
                    id = "games/2023/mar/28/super-mario-lush-soaps",
                    type = "article",
                    sectionId = "article",
                    sectionName = "Games",
                    webPublicationDate = "2023-03-28T12:15:18Z",
                    webTitle = "Luigi has sweet $SEARCH_QUERY of apple’: testing out Lush’s unlikely Super Mario soaps",
                    webUrl = "https://www.theguardian.com/games/2023/mar/28/super-mario-lush-soaps",
                    apiUrl = "https://content.guardianapis.com/games/2023/mar/28/super-mario-lush-soaps",
                    fields = NewsFields(
                        headline = "Luigi has sweet notes of apple’: testing out Lush’s unlikely Super Mario soaps",
                        trailText = "Animal-friendly cosmetics brand Lush is releasing a range of Mario-themed products – so our reporter tried them, for science",
                        thumbnail = "https://media.guim.co.uk/c6436ebbf6e5eceee60a496698e4fc5004c176db/0_327_2000_1200/500.jpg",
                        body = "Luigi has sweet notes of apple’: testing out Lush’s unlikely Super Mario soaps"
                    ),
                    isHosted = false,
                    pillarId = "pillar/arts",
                    pillarName = "Arts"
                ),
                NewsResult(
                    id = "games/2023/mar/28/super-mario-lush-soaps",
                    type = "article",
                    sectionId = "news",
                    sectionName = "Article",
                    webPublicationDate = "2023-03-28T12:15:18Z",
                    webTitle = "Luigi has sweet $SEARCH_QUERY of apple’: testing out Lush’s unlikely Super Mario soaps",
                    webUrl = "https://www.theguardian.com/games/2023/mar/28/super-mario-lush-soaps",
                    apiUrl = "https://content.guardianapis.com/games/2023/mar/28/super-mario-lush-soaps",
                    fields = NewsFields(
                        headline = "Luigi has sweet notes of apple’: testing out Lush’s unlikely Super Mario soaps",
                        trailText = "Animal-friendly cosmetics brand Lush is releasing a range of Mario-themed products – so our reporter tried them, for science",
                        thumbnail = "https://media.guim.co.uk/c6436ebbf6e5eceee60a496698e4fc5004c176db/0_327_2000_1200/500.jpg",
                        body = "Luigi has sweet notes of apple’: testing out Lush’s unlikely Super Mario soaps"
                    ),
                    isHosted = false,
                    pillarId = "pillar/arts",
                    pillarName = "Arts"
                )
            )
        )
    )

    val fakeResponseSectionRoot: Response<SectionsRoot> = Response.success(
        SectionsRoot(
            response = SectionsResponse(
                status = "ok",
                userTier = "developer",
                total = 3L,
                results = listOf(
                    SectionsResult(
                        id = "about",
                        webTitle = "About",
                        webURL = "https://www.theguardian.com/about",
                        apiURL = "https://content.guardianapis.com/about",
                        editions = listOf(
                            SectionsEdition(
                                id = "about",
                                webTitle = "About",
                                webURL = "https://www.theguardian.com/about",
                                apiURL = "https://content.guardianapis.com/about",
                                code = Code.Default.value
                            )
                        )
                    ),
                    SectionsResult(
                        id = "music",
                        webTitle = "Music",
                        webURL = "https://www.theguardian.com/music",
                        apiURL = "https://content.guardianapis.com/music\"",
                        editions = listOf(
                            SectionsEdition(
                                id = "music",
                                webTitle = "Music",
                                webURL = "https://www.theguardian.com/music",
                                apiURL = "https://content.guardianapis.com/music",
                                code = Code.Default.value
                            )
                        )
                    ),
                    SectionsResult(
                        id = "books",
                        webTitle = "Books",
                        webURL = "https://www.theguardian.com/books",
                        apiURL = "https://content.guardianapis.com/books",
                        editions = listOf(
                            SectionsEdition(
                                id = "books",
                                webTitle = "Books",
                                webURL = "https://www.theguardian.com/books",
                                apiURL = "https://content.guardianapis.com/books",
                                code = Code.Default.value
                            )
                        )
                    )
                )
            )

        )
    )

    val DEFAULT_SECTIONS = listOf(
        "games",
        "sport",
        "tech",
        "books",
        "world-news",
        "politics",
        "culture",
        "education"
    )
}