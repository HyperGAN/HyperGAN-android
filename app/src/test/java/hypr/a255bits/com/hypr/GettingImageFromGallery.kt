package hypr.a255bits.com.hypr

import android.content.Context
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test

/**
 * Created by ted on 7/17/17.
 */

class GettingImageFromGallery{
    val context: Context = mock()
    val view: MainMvp.view = mock()

    val presenter: MainPresenter = MainPresenter(context, view)

    @Test
    fun clickingButtonOpensGallery(){
        presenter.displayGallery()
        verify(view).displayGallery()


    }
}
