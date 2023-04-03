package com.cailloutr.rightnews.usecases

import com.cailloutr.rightnews.other.Resource

class FakeGetRecentNewsUseCase {

    operator fun invoke(
        successResult: Boolean
    ): Resource<Nothing> {
        if (successResult) {
            return Resource.success(data = null)
        }
        return Resource.loading(data = null)
    }
}