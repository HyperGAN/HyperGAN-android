package hypr.a255bits.com.hypr

import android.content.Context
import com.nhaarman.mockito_kotlin.mock
import hypr.a255bits.com.hypr.Network.ModelApi
import org.junit.Assert
import org.junit.Test

class DownloadGeneratorTest{
    val context: Context = mock()

    @Test
    fun jsonParse(){
        val modelApi = ModelApi()
        val listOfModels = modelApi.listOfModels(context)
        val generatorInput = listOfModels!![0].generator!!.input
        val generatorInputTwo = listOfModels[1].generator!!.input
        Assert.assertNotNull(generatorInput!!.z_dims)

    }
}
