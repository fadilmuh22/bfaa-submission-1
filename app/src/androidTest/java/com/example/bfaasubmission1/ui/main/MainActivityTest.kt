package com.example.bfaasubmission1.ui.main

import android.view.KeyEvent
import android.widget.EditText
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.bfaasubmission1.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    private val emptyText = ""
    private val spaceText = "   "
    private val invalidText = "fa"
    private val validText = "fadil"
    private val untrimmedText = "$validText$spaceText"

    @Before
    fun setup() {
        ActivityScenario.launch(MainActivity::class.java)
    }

    // test search input will not trigger progressbar when empty or less than 3 characters
    @Test
    fun testSearchInputValidation() {
        onView(ViewMatchers.withId(R.id.searchBar)).perform(ViewActions.click())
        onView(isAssignableFrom(EditText::class.java)).perform(ViewActions.typeText(emptyText))
            .perform(ViewActions.pressKey(KeyEvent.KEYCODE_ENTER))
        onView(
            ViewMatchers.withId(R.id.progressBarMain),
        ).check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)))

        onView(ViewMatchers.withId(R.id.searchBar)).perform(ViewActions.click())
        onView(isAssignableFrom(EditText::class.java)).perform(ViewActions.typeText(spaceText))
            .perform(ViewActions.pressKey(KeyEvent.KEYCODE_ENTER))
        onView(
            ViewMatchers.withId(R.id.progressBarMain),
        ).check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)))

        onView(ViewMatchers.withId(R.id.searchBar)).perform(ViewActions.click())
        onView(isAssignableFrom(EditText::class.java)).perform(ViewActions.typeText(invalidText))
            .perform(ViewActions.pressKey(KeyEvent.KEYCODE_ENTER))
        onView(
            ViewMatchers.withId(R.id.progressBarMain),
        ).check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
    }

    // test search input will trigger progressbar when more than 3 characters
    @Test
    fun testSearchInputTriggerProgressBar() {
        onView(ViewMatchers.withId(R.id.searchBar)).perform(ViewActions.click())
        onView(isAssignableFrom(EditText::class.java)).perform(ViewActions.typeText(validText))
            .perform(ViewActions.pressKey(KeyEvent.KEYCODE_ENTER))
        onView(
            ViewMatchers.withId(R.id.progressBarMain),
        ).check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

    // test search input will trim the text
    @Test
    fun testSearchInputTrimmed() {
        onView(ViewMatchers.withId(R.id.searchBar)).perform(ViewActions.click())
        onView(
            isAssignableFrom(EditText::class.java),
        ).perform(ViewActions.typeText(untrimmedText)).perform(ViewActions.pressKey(KeyEvent.KEYCODE_ENTER))
        onView(ViewMatchers.withId(R.id.searchBar)).check(
            ViewAssertions.matches(
                ViewMatchers.withChild(
                    ViewMatchers.withText(validText),
                ),
            ),
        )
    }
}
