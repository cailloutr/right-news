package com.cailloutr.rightnews.usecases

import com.cailloutr.rightnews.model.SectionWrapper
import com.cailloutr.rightnews.model.NewsContainer
import com.cailloutr.rightnews.repository.NewsRepositoryInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class GetNewsBySectionUseCase(
    private val repository: NewsRepositoryInterface,
) {

    operator fun invoke(
        context: CoroutineDispatcher,
        section: SectionWrapper,
    ): Flow<NewsContainer> {
        CoroutineScope(context).launch {
            repository.refreshArticles(context, section)
        }
        return repository.getNewsOrderedByDate(context, section)
    }
}

