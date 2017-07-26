package hypr.a255bits.com.hypr

import hypr.a255bits.com.hypr.Network.ModelApi
import org.junit.Assert.*
import org.junit.Test

class ModelServiceTest {

    @Test
    fun downloadingListOfModelsConformToGeneratorClass() {
        val modelApi = ModelApi()
        val listOfGenerators = modelApi.listOfModels()?.execute()?.body()
        listOfGenerators?.forEach { model ->
            assertNotNull(model.name)
            assertNotNull(model.viewer.type)
            assertTrue(model.fileSize > 0)
        }
    }

}