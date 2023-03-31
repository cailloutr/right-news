package com.cailloutr.rightnews.ui.newsfragment

import com.cailloutr.rightnews.ui.viewmodel.NewsViewModel
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NewsFragmentTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var viewModel: NewsViewModel

    @Before
    fun setUp() {
    }

    @Test
    fun test_onViewCreated() {
    }
}