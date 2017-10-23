package hypr.a255bits.com.hypr.MultiFaceSelection

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hypr.a255bits.com.hypr.DrawableImageViewTouchInBoundsListener
import hypr.a255bits.com.hypr.Main.MainActivity
import hypr.a255bits.com.hypr.R
import hypr.a255bits.com.hypr.Util.toBitmap
import kotlinx.android.synthetic.main.fragment_multi_face.*
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor
import java.io.File

class MultiFaceFragment : Fragment(), MultiFaceMVP.view, DrawableImageViewTouchInBoundsListener{

    private var imageOfPeoplesFaces: Bitmap? = null
    private var faceLocations: Array<PointF>? = null
    private val presenter by lazy{MultiFacePresenter(this)}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            imageOfPeoplesFaces = arguments.getByteArray(IMAGE).toBitmap()
            faceLocations = arguments.getParcelableArray(FACE_LOCATIONS) as Array<PointF>?
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_multi_face, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageWithBoxesAroundFaces = presenter.addFaceBoxesToMultipleFacesImage(context, imageOfPeoplesFaces)
        drawableImageView.setBoundsTouchListener(this)
        presenter.displayImageWithFaces(imageWithBoxesAroundFaces)
    }

    override fun onBoundsTouch(image: Bitmap, index: Int) {
        val croppedFace = presenter.cropFaceFromImage(presenter.imageOfPeoplesFaces!!, index, context)
        presenter.sendCroppedFaceToMultiModel(croppedFace)
    }
    override fun sendImageToModel(file: File, fullImage: File) {
        startActivity(activity.intentFor<MainActivity>
        ("indexInJson" to 0, "image" to file.path, "fullimage" to fullImage.path).clearTop())
    }

    override fun addFaceLocationToImage(rect: Rect) {
        drawableImageView.addFaceLocation(rect)
    }

    override fun addBoxAroundFace(rect: Rect, canvasImageWithFaces: Canvas) {
        val paint = Paint()
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1.0f
        canvasImageWithFaces.drawRect(rect, paint)
    }

    override fun displayImageWithFaces(imageOfPeople: Bitmap) {
        drawableImageView.bitmap = imageOfPeople
    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    companion object {
        private val IMAGE = "param1"
        private val FACE_LOCATIONS = "param2"

        fun newInstance(image: ByteArray?, faceLocations: Array<PointF>?): MultiFaceFragment {
            val fragment = MultiFaceFragment()
            val args = Bundle()
            args.putByteArray(IMAGE, image)
            args.putParcelableArray(FACE_LOCATIONS, faceLocations)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
