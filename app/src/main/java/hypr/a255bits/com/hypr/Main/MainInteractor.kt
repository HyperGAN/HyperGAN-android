package hypr.a255bits.com.hypr.Main

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.SparseArray
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.face.Face
import com.google.android.gms.vision.face.FaceDetector
import hypr.a255bits.com.hypr.Generator
import hypr.a255bits.com.hypr.Network.ModelApi
import hypr.a255bits.com.hypr.R
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainInteractor(val context: Context) : MainMvp.interactor {
    val detector: FaceDetector by lazy {
        FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .build()
    }
    var listOfGenerators: List<Generator>? = null


    override fun addModelsToNavBar(param: GeneratorListener) {
        if (listOfGenerators == null) {
            getGeneratorFromNetwork(param)
        } else {
            callListenerForEachGenerator(param, listOfGenerators)
        }
    }

    private fun getGeneratorFromNetwork(param: GeneratorListener) {
        val modelApi = ModelApi()
        val listOfModels = modelApi.listOfModels()
        listOfModels?.enqueue(object : Callback<List<Generator>> {
            override fun onFailure(call: Call<List<Generator>>?, t: Throwable?) {
                t?.printStackTrace()
            }

            override fun onResponse(call: Call<List<Generator>>?, response: Response<List<Generator>>?) {
                listOfGenerators = response?.body()
                callListenerForEachGenerator(param, listOfGenerators)
            }

        })
    }

    private fun callListenerForEachGenerator(param: GeneratorListener, listOfGenerators: List<Generator>?) {
        listOfGenerators?.forEachIndexed { index, generator ->
            param.getGenerator(generator, index)
        }
    }

    override fun getFacesFromBitmap(imageWithFaces: Bitmap, width: Int, height: Int, context: Context): MutableList<Bitmap> {
        val faceLocations: SparseArray<Face>? = getFaceLocations(imageWithFaces, context)
        val croppedFaces = getListOfFaces(faceLocations, imageWithFaces)
        return croppedFaces
    }

    private fun getFaceLocations(imageWithFaces: Bitmap, context: Context): SparseArray<Face>? {
        var locationOfFaces = SparseArray<Face>()
        if (detector.isOperational) {
            val frame = Frame.Builder().setBitmap(imageWithFaces).build()
            locationOfFaces = detector.detect(frame)
        } else {
            context.toast(context.resources.getString(R.string.failed_face_detection))
        }
        return locationOfFaces
    }

    private fun getListOfFaces(faceLocations: SparseArray<Face>?, imageWithFaces: Bitmap): MutableList<Bitmap> {
        val croppedFaces = mutableListOf<Bitmap>()
        val numOfFaces: Int = faceLocations?.size()!!
        repeat(numOfFaces) { index ->
            val faceLocation = faceLocations[index]
            val face = cropFaceOutOfBitmap(faceLocation, imageWithFaces)
            croppedFaces.add(face)
        }
        return croppedFaces
    }

    private fun cropFaceOutOfBitmap(face: Face, imageWithFaces: Bitmap): Bitmap {
        val centerOfFace = face.position
        val x = centerOfFace.x.toInt()
        val y = centerOfFace.y.toInt()

        val croppedFace = Bitmap.createBitmap(imageWithFaces, x, y, face.width.toInt(), face.height.toInt())
        return croppedFace
    }

    override fun uriToBitmap(imageLocation: Uri): Bitmap {
        return MediaStore.Images.Media.getBitmap(context.contentResolver, imageLocation)
    }
}
