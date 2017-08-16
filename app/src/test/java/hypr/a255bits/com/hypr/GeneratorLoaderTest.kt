package hypr.a255bits.com.hypr

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.graphics.BitmapCompat
import android.test.ActivityTestCase
import org.junit.Test
import java.io.File
import junit.framework.Assert
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException


class GeneratorLoaderTest {
    val generatorLoader = GeneratorLoader()


    fun load_and_feed() {
//        generatorLoader.load(activity.assets)
//
//        generatorLoader.sample(encoded)
//        assertNotNull(generatorLoader)
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
