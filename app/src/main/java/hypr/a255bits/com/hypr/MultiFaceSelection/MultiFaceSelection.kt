package hypr.a255bits.com.hypr.MultiFaceSelection

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import hypr.a255bits.com.hypr.R

class MultiFaceSelection : AppCompatActivity() {
    val presenter: MultiFacePresenter by lazy{MultiFacePresenter()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_face_selection)
        presenter.getInfoFromExtras(intent.extras)
    }
}
