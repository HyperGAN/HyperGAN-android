package hypr.hypergan.com.hypr

import android.content.Context
import com.nhaarman.mockito_kotlin.mock
import hypr.hypergan.com.hypr.ModelFragmnt.ModelFragmentMVP
import hypr.hypergan.com.hypr.ModelFragmnt.ModelInteractor
import org.junit.Test

class FaceDetection{
    val context: Context = mock()
    val view: ModelFragmentMVP.view = mock()
    val interactor: ModelInteractor = mock()

    @Test
    fun findingFacesCallsGoogleFaceApi(){
    }
}
