package hypr.hypergan.com.hypr.Main


import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.RootMatchers
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.test.suitebuilder.annotation.LargeTest
import android.view.View
import android.view.ViewGroup
import hypr.hypergan.com.hypr.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class CameraTest {
// Must be run with clean install

    @get:Rule
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun cameraSnapshotWithNoFaceShowsNoFaceError() {
        val appCompatButton = onView(
                allOf(withId(R.id.chooseImageFromGalleryButton), withText("Choose Image"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.widget.RelativeLayout")),
                                        0),
                                0),
                        isDisplayed()))
        appCompatButton.perform(click())
        val takepicButton = onView(
                allOf(withId(R.id.takePicture)))

        takepicButton.perform(click())

        onView(withText(R.string.select_image_with_face))
                .inRoot(RootMatchers.withDecorView(not(`is`(mActivityTestRule.activity.window.decorView))))
                .check(ViewAssertions.matches(isDisplayed()))

    }

    private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
