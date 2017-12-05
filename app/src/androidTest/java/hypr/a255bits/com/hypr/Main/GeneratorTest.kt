package hypr.a255bits.com.hypr.Main


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.test.suitebuilder.annotation.LargeTest
import hypr.a255bits.com.hypr.GeneratorLoader.EasyGeneratorLoader
import hypr.a255bits.com.hypr.Util.ImageSaver
import hypr.a255bits.com.hypr.Util.JsonReader
import hypr.a255bits.com.hypr.Util.toBitmap

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.BufferedInputStream
import java.io.InputStream

@LargeTest
@RunWith(AndroidJUnit4::class)
open class GeneratorTest {

    @get:Rule open val mActivityTestRule = ActivityTestRule(MainActivity::class.java)
    private val IMAGES_TO_TRANSFER_FOLDERNAME = "beforeImages"
    val gen = JsonReader().getGeneratorsFromJson(mActivityTestRule.activity)
    val generator = EasyGeneratorLoader(gen?.get(0)!!, mActivityTestRule.activity)

    @Test
    fun generatorTest() {
        mActivityTestRule.activity.requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        val listOfImages = mActivityTestRule.activity.applicationContext.resources.assets.list(IMAGES_TO_TRANSFER_FOLDERNAME)
        writeTransformedBitmapsToAndroidGallery(listOfImages)
    }

    private fun writeTransformedBitmapsToAndroidGallery(listOfImages: Array<out String>?) {
        listOfImages?.forEach { filename ->
            val imageFile = openFile("$IMAGES_TO_TRANSFER_FOLDERNAME/$filename")
            val image = getBitmapFromFile(imageFile)
            val encodedBitmap = encodeImage(image)
            writeImageToFileOnAndroidDeviceGallery(encodedBitmap, mActivityTestRule.activity.applicationContext)
        }
    }

    private fun openFile(filenamePath: String): InputStream? {
        return mActivityTestRule.activity.applicationContext.resources.assets.open(filenamePath)
    }


    private fun getBitmapFromFile(imageFile: InputStream?): Bitmap? {
        val imageReaderFromFile = BufferedInputStream(imageFile)
        return BitmapFactory.decodeStream(imageReaderFromFile)
    }

    private fun encodeImage(image: Bitmap?): Bitmap {
        generator.load(mActivityTestRule.activity.assets)
        val scaled = Bitmap.createScaledBitmap(image, 128, 128, false)
        val encoded = generator.encode(scaled)
        val imageFace = generator.sampleImageWithoutImage(index)
        return imageFace.toBitmap(generator.width, generator.height)
    }

    private fun writeImageToFileOnAndroidDeviceGallery(encodedBitmap: Bitmap, context: Context) {
        ImageSaver().saveImageToInternalStorage(encodedBitmap, context, "after")
    }

}
