package hypr.a255bits.com.hypr

import android.content.Context
import com.nhaarman.mockito_kotlin.mock
import hypr.a255bits.com.hypr.ModelFragmnt.ModelFragmentMVP
import hypr.a255bits.com.hypr.ModelFragmnt.ModelFragmentPresenter
import hypr.a255bits.com.hypr.ModelFragmnt.ModelInteractor
import org.junit.Test

class FaceDetection{
    val context: Context = mock()
    val view: ModelFragmentMVP.view = mock()
    val interactor: ModelInteractor = mock()
    val presenter: ModelFragmentPresenter = ModelFragmentPresenter(view, interactor, context)

    @Test
    fun findingFacesCallsGoogleFaceApi(){
    }
}
