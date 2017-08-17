package hypr.a255bits.com.hypr

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import hypr.a255bits.com.hypr.Main.MainActivity
import hypr.a255bits.com.hypr.Util.ImageSaver
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.BufferedInputStream
import java.io.File
import java.io.InputStream

@Config(manifest = "/src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner::class)
open class GeneratorLoaderTest {
    private val IMAGES_TO_TRANSFER_FOLDERNAME = "beforeImages"
    var activity: MainActivity? = null
    private val generator = GeneratorLoader()

    @Test
    fun generatorTest() {
        activity = Robolectric.setupActivity(MainActivity::class.java)
        val beforeImagesFolder = this.javaClass.classLoader.getResource(IMAGES_TO_TRANSFER_FOLDERNAME).file
        val listOfFiles = File(beforeImagesFolder).listFiles()
        writeTransformedBitmapsToAndroidGallery(listOfFiles)
    }

    private fun writeTransformedBitmapsToAndroidGallery(listOfImages: Array<File>) {
        listOfImages.forEach { file ->
            val image = getBitmapFromFile(file.inputStream())
            val encodedBitmap = encodeImage(image)
            writeImageToFileOnAndroidDeviceGallery(encodedBitmap, activity!!.applicationContext)
        }
    }

    private fun openFile(filenamePath: String): InputStream? {
        return activity!!.applicationContext.resources.assets.open(filenamePath)
    }


    private fun getBitmapFromFile(imageFile: InputStream?): Bitmap? {
        val imageReaderFromFile = BufferedInputStream(imageFile)
        return BitmapFactory.decodeStream(imageReaderFromFile)
    }

    private fun encodeImage(image: Bitmap?): Bitmap {
        generator.load(activity!!.assets)
        val scaled = Bitmap.createScaledBitmap(image, 128, 128, false)
        val encoded = generator.encode(scaled)
        return generator.sample(encoded)
    }

    private fun writeImageToFileOnAndroidDeviceGallery(encodedBitmap: Bitmap, context: Context) {
        ImageSaver().saveImageToInternalStorage(encodedBitmap, context, "after")
    }

    @Test
    fun testImagesDoneThrowErrorWithGenerator() {
        val file = File(javaClass.classLoader.getResource("image.jpg").toURI())
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        val bitmap = BitmapFactory.decodeFile(file.absolutePath, options)
        checkNotNull(bitmap)
    }
}
