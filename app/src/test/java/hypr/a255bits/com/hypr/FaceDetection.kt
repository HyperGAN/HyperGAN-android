package hypr.a255bits.com.hypr

import android.content.Context
import android.graphics.Bitmap
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test

/**
 * Created by ted on 7/17/17.
 */

class FaceDetection{
    val context: Context = mock()
    val view: MainMvp.view = mock()
    val interactor: MainInteractor = mock()

    val presenter: MainPresenter = MainPresenter(view, interactor, applicationContext)

    val testBitmap: Bitmap = any()

    @Test
    fun findingFacesCallsGoogleFaceApi(){
        presenter.findFacesInImage(testBitmap, applicationContext)
        verify(interactor).getFacesFromBitmap(testBitmap, testBitmap.width, testBitmap.height, context)
    }
}
