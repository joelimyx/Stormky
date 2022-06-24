package com.example.stormky

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.stormky.ui.HomeFragment
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class UITest {

    private val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
    private val scenario = launchFragmentInContainer<HomeFragment>(themeResId = R.style.Theme_Stormky)

    @Before
    fun setup(){
        scenario.onFragment {
            navController.setGraph(R.navigation.mobile_navigation)
            Navigation.setViewNavController(it.requireView(), navController)
        }
    }


    @Test
    fun viewClick(){

        onView(withId(R.id.searchView))
            .perform(typeText("11365"))
//        assertThat(navController.currentDestination?.id, R.id.navigation_hourly)
    }
}