package hypr.a255bits.com.hypr

import android.content.Context
import android.graphics.Bitmap
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test

class FaceDetection{
    val context: Context = mock()
    val view: MainMvp.view = mock()
    val interactor: MainInteractor = mock()

    val presenter: MainPresenter = MainPresenter(view, interactor, context)

    val testBitmap: Bitmap = any()

    @Test
    fun findingFacesCallsGoogleFaceApi(){
        presenter.findFacesInImage(testBitmap, context)
        verify(interactor).getFacesFromBitmap(testBitmap, testBitmap.width, testBitmap.height, context)
    }
}
