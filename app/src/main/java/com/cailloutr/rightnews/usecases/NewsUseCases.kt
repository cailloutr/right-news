package com.cailloutr.rightnews.usecases

data class NewsUseCases(
    val getSectionsFilteredByIdUseCase: GetSectionsFilteredByIdUseCase,
    val getRecentNewsUseCase: GetRecentNewsUseCase
)
