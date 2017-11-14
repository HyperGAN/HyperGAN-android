package hypr.a255bits.com.hypr.Main

import android.support.test.rule.ActivityTestRule
import org.junit.Rule

open class CameraTest{

    @get:Rule open val activity = ActivityTestRule(MainActivity::class.java)
}
