package com.cailloutr.rightnews.usecases

data class NewsUseCases(
    val getSectionsUseCase: GetSectionsUseCase,
    val getRecentNewsUseCase: GetRecentNewsUseCase,
    val getNewsBySectionUseCase: GetNewsBySectionUseCase
)
