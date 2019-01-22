package com.example.beardwulf.reva

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.support.test.InstrumentationRegistry
import android.support.test.InstrumentationRegistry.getContext
import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import org.junit.Before
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.closeSoftKeyboard
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.Intents.intending
import android.support.test.espresso.intent.matcher.IntentMatchers.toPackage
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.RootMatchers.withDecorView
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import android.widget.ImageView
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.stream.Collectors

import com.example.beardwulf.reva.activities.MainActivity
import android.support.test.rule.ActivityTestRule
import com.example.beardwulf.reva.activities.registreren.Registreren
import org.junit.After


@RunWith(AndroidJUnit4::class)
class MainActivityTest{
    //@get:Rule
    //val grantCameraPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.CAMERA)


    @get:Rule
    val activityRule = IntentsTestRule<MainActivity>(MainActivity::class.java)

    @Test
    fun mainScreen_loads() {
        onView(withId(R.id.textView)).check(matches(isDisplayed()))

    }
    @Test
    fun launchTest() {
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()))
    }

    @Test
    fun mainScreenWrongInputCode(){
        onView(withId(R.id.txtInput)).perform(ViewActions.typeText("!")).perform(closeSoftKeyboard())
        onView(withId(R.id.btnLogin)).perform(click());
        onView(withId(R.id.txtInput)).check(matches(withHint("Voer enkel cijfers en letters in aub")))
    }

    @Test
    fun passMainScreenrightCode(){
        onView(withId(R.id.txtInput)).perform(ViewActions.typeText("1234")).perform(closeSoftKeyboard())
        onView(withId(R.id.btnLogin)).perform(click())
        onView(withId(R.id.cmdNeemFoto)).perform(click())
    }

    @Before
    fun registerIdlingResource(){
        IdlingRegistry.getInstance().register(activityRule.activity.getIdlingResourceInTest())
    }

    @After
    fun unregisterIdlingResource(){
        IdlingRegistry.getInstance().unregister(activityRule.activity.getIdlingResourceInTest())
    }

    //camera
     @Test
    fun validateCameraScenario() {
        // Some vendors use a camera app different from the stock Android camera app
        // This will be checked before executing this instrumentation test

        val data = Intent()
        val image = BitmapFactory.decodeResource(
                InstrumentationRegistry.getTargetContext().resources,
                R.drawable.medisch)
        data.putExtra("data", image)

        val resultActivity = Instrumentation.ActivityResult(Activity.RESULT_OK, data)
        onView(withId(R.id.txtInput)).perform(ViewActions.typeText("1234")).perform(closeSoftKeyboard())
        onView(withId(R.id.btnLogin)).perform(click())
        onView(withId(R.id.cmdNeemFoto)).perform(click())
        onView(withId(R.id.imageView)).check(matches(DrawableMatcher(R.drawable.medisch)))
    }
    class DrawableMatcher internal constructor(private val expectedId: Int) : TypeSafeMatcher<View>(View::class.java) {
        private var resourceName: String? = null

        override fun matchesSafely(target: View): Boolean {
            if (target !is ImageView) {
                return false
            }
            if (expectedId == EMPTY) {
                return target.drawable == null
            }
            if (expectedId == ANY) {
                return target.drawable != null
            }
            val resources = target.getContext().resources
            val expectedDrawable = resources.getDrawable(expectedId, null)
            resourceName = resources.getResourceEntryName(expectedId)

            if (expectedDrawable == null) {
                return false
            }

            val bitmap = getBitmap(target.drawable)
            val otherBitmap = getBitmap(expectedDrawable)
            return bitmap.sameAs(otherBitmap)
        }

        private fun getBitmap(drawable: Drawable): Bitmap {
            val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
            drawable.draw(canvas)
            return bitmap
        }

        override fun describeTo(description: Description) {
            description.appendText("with drawable from resource id: ")
            description.appendValue(expectedId)
            if (resourceName != null) {
                description.appendText("[")
                description.appendText(resourceName)
                description.appendText("]")
            }
        }

        companion object {
            internal val EMPTY = -1
            internal val ANY = -2
        }
    }
}