package hypr.a255bits.com.hypr.Main


import android.app.Activity
import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.test.suitebuilder.annotation.LargeTest
import hypr.a255bits.com.hypr.GeneratorLoader
import hypr.a255bits.com.hypr.Util.ImageSaver

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.BufferedInputStream

@LargeTest
@RunWith(AndroidJUnit4::class)
open class GeneratorTest {

    @get:Rule open val mActivityTestRule = ActivityTestRule(MainActivity::class.java)
    val generator = GeneratorLoader()

    @Test
    fun generatorTest() {
        val imageFile = mActivityTestRule.activity.applicationContext.resources.assets.open("images/image.jpg")
        val imageReaderFromFile = BufferedInputStream(imageFile)
        val image = BitmapFactory.decodeStream(imageReaderFromFile)

        generator.load(mActivityTestRule.activity.assets)
        val scaled = Bitmap.createScaledBitmap(image, 128, 128, false)
        val encoded = generator.encode(scaled)
        val encodedBitmap = generator.sample(encoded)
        mActivityTestRule.activity.requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)

        writeBitmapToFile(encodedBitmap, mActivityTestRule.activity.applicationContext)
    }

    private fun writeBitmapToFile(encodedBitmap: Bitmap, context: Context) {
        ImageSaver().saveImageToInternalStorage(encodedBitmap, context)
        Thread.sleep(1000)
    }

}
