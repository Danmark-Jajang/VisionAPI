package com.example.visionapi

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.visionapi.databinding.ActivityCameraBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import kotlinx.coroutines.currentCoroutineContext
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {
    lateinit var binding: ActivityCameraBinding
    lateinit var itemList : ArrayList<OCRText>
    lateinit var textAdapter: TextAdapter
    private var imageCapture: ImageCapture? = null

    private lateinit var cameraExecutor: ExecutorService
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.e("Intent", "CameraActivity On")
        if (allPermissionsGranted()) {
            Log.e("Camera", "starting camera")
            startCamera()

        } else {
            Log.e("Camera", "camera permission problem")
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        cameraExecutor = Executors.newSingleThreadExecutor()

        binding.btnPhoto.setOnClickListener { takePhoto() }

        //recyclerView 초기화
        itemList = ArrayList()
        textAdapter = TextAdapter(itemList)
        binding.tvResult.layoutManager = LinearLayoutManager(this)
        binding.tvResult.adapter = textAdapter

        //recyclerView 커스텀 Listener
        var searcher : SearchItem = SearchItem(this@CameraActivity)
        textAdapter.setOnItemClickListener(object : TextAdapter.onItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                searcher.searchItem(itemList[position])
            }

        })


    }
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture)
                Log.e("Camera", "Camera Binding Success")

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
        Log.e("test", "starting camera")
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        // Create time stamped name and MediaStore entry.
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
//        val contentValues = ContentValues().apply {
//            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
//            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
//            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
//                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
//            }
//        }

        imageCapture.takePicture(
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    Log.e("OCR", "Image Successfully Captured!")
                    textAnalize(image)
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("OCR", "Something wrong...")
                }
            }
        )
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()

    }

    @SuppressLint("NotifyDataSetChanged")
    @OptIn(ExperimentalGetImage::class)
    fun textAnalize(img : ImageProxy){
        val recognizer = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())
        val mediaImage = img.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, img.imageInfo.rotationDegrees)
            val result = recognizer.process(image)
                .addOnSuccessListener { result ->
                    itemList.clear()
                    val resultText = result.text
                    Toast.makeText(this, "OCR Success", Toast.LENGTH_SHORT).show()
                    for (block in result.textBlocks) {
                        //나중에 추가적인 기능을 위해 나머지 분석 결과도 남겨둠
                        val blockText = block.text
                        itemList.add(OCRText(blockText))
                        val blockCornerPoints = block.cornerPoints
                        val blockFrame = block.boundingBox
                        for (line in block.lines) {
                            val lineText = line.text
                            val lineCornerPoints = line.cornerPoints
                            val lineFrame = line.boundingBox
                            for (element in line.elements) {
                                val elementText = element.text
                                val elementCornerPoints = element.cornerPoints
                                val elementFrame = element.boundingBox
                            }
                        }
                    }
                    textAdapter.notifyDataSetChanged()
                    Log.e("OCR", "analyze Success...")
                }
                .addOnFailureListener { e ->
                    Log.e("OCR", "analyze error occur")
                }
                .addOnCompleteListener {
                    img.close()
                }
        }
    }
}
