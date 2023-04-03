package com.cailloutr.rightnews.ui.newsfragment

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.cailloutr.rightnews.R
import com.cailloutr.rightnews.launchFragmentInHiltContainer
import com.cailloutr.rightnews.ui.viewmodel.NewsViewModel
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
@HiltAndroidTest
class NewsFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    lateinit var viewModel: NewsViewModel

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertThat("com.cailloutr.rightnews").isEqualTo(appContext.packageName)
    }


    @Test
    fun test_isShowingBannersViewsOnViewModelSuccess() {
        val navController = mockk<NavController>()
        launchFragmentInHiltContainer<NewsFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.banners_view_pager)).check(matches(isDisplayed()))
    }
}