package com.cailloutr.rightnews.repository

import com.cailloutr.rightnews.data.network.responses.news.NewsRoot
import com.cailloutr.rightnews.data.network.responses.sections.SectionsEdition
import com.cailloutr.rightnews.data.network.responses.sections.SectionsResponse
import com.cailloutr.rightnews.data.network.responses.sections.SectionsResult
import com.cailloutr.rightnews.data.network.responses.sections.SectionsRoot
import com.cailloutr.rightnews.enums.Code
import com.cailloutr.rightnews.enums.OrderBy
import com.cailloutr.rightnews.other.Resource
import retrofit2.Response

class FakeNewsRepository : NewsRepositoryInterface {

    override suspend fun getAllSections(): Resource<SectionsRoot> {
        return Resource.success(
            data = SectionsRoot(
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
    }

    override suspend fun getNewsOrderedByDate(
        orderBy: OrderBy,
        fields: String,
    ): Response<NewsRoot> {
        TODO("Not yet implemented")
    }
}