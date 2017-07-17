package hypr.a255bits.com.hypr

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test

/**
 * Created by ted on 7/17/17.
 */

class GettingImageFromGallery{
    val presenter: MainPresenter = mock()
    val view: MainMvp.view = mock()

    @Test
    fun clickingButtonOpensGallery(){
        presenter.displayGallery()
        verify(view).displayGallery()


    }
}
