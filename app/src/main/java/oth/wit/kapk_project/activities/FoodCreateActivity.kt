package oth.wit.kapk_project.activities

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import oth.wit.kapk_project.R
import oth.wit.kapk_project.databinding.ActivityMainBinding
import oth.wit.kapk_project.models.FoodModel
import oth.wit.kapk_project.main.MainApp
import oth.wit.kapk_project.models.NutritionalValues
import oth.wit.kapk_project.models.ProductCategory
import oth.wit.kapk_project.models.UnitSpecification
import timber.log.Timber
import timber.log.Timber.i

class FoodCreateActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    lateinit var food : FoodModel
    lateinit var app: MainApp
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var spinner : Spinner
    private var productCategory: ProductCategory = ProductCategory.MISCELLANEOUS
    lateinit var btnScanBarcode : Button

    private val CAMERA_PERMISSION_CODE=123
    private val READ_STORAGE_PERMISSION_CODE=113
    private val WRITE_STORAGE_PERMISSION_CODE=113

    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>

    lateinit var inputImage : InputImage
    lateinit var barcodeScanner: BarcodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var edit = false

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarContinue.title = "Register Food"
        setSupportActionBar(binding.toolbarContinue)


        app = application as MainApp

        i("ALEX Food Activity started...")


        spinner = binding.spinner
        btnScanBarcode = binding.buttonScan

        barcodeScanner = BarcodeScanning.getClient()

        cameraLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult(), object : ActivityResultCallback<ActivityResult>{
            override fun onActivityResult(result: ActivityResult?) {
                i("ALEX onActivityResult()")
                val data = result?.data
                try {
                    val photo = data?.extras?.get("data") as Bitmap
                    inputImage = InputImage.fromBitmap(photo, 0)
                    processQr()
                } catch (e : Exception) {i("ALEX onActivityResult: ${e.message}")}
            }
        }
        )

        galleryLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult(), object : ActivityResultCallback<ActivityResult>{
            override fun onActivityResult(result: ActivityResult?) {
                i("ALEX onActivityResult()")
                val data = result?.data
                try {
                    inputImage = InputImage.fromFilePath(this@FoodCreateActivity,data?.data)
                    processQr()
                } catch (e : Exception) {i("ALEX onActivityResult: ${e.message}")}
            }
        }
        )



        //val categories = ProductCategory.values().forEach { ProductCategory -> ProductCategory.printableName }

        val categories = ProductCategory.values().map{ ProductCategory -> ProductCategory.printableName }

        //val categories = arrayOf("Miscellaneous", "Energy Drink", "Baked Goods", "Fish Products", "Meat", "Vegetables",
         //  "Vegetables", "Vegetarian", "Vegan", "Dessert", "Cereal Products", "Beverages", "Pulses", "Potato Products",
           // "Dairy Products", "Pasta", "Fruit", "Sauces", "Soy Products", "Confectionery", "Oil & Fat", "Fast Food")

        spinner.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categories)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                productCategory = ProductCategory.values()[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        btnScanBarcode.setOnClickListener {
            i("ALEX scan button pressed")
            val options = arrayOf("camera", "gallery")

            val builder = AlertDialog.Builder(this@FoodCreateActivity)
            builder.setTitle("Pick an option")

            builder.setItems(options, DialogInterface.OnClickListener{
                dialog, which ->
                if (which==0) {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    cameraLauncher.launch(cameraIntent)
                }
                else {
                    val storageIntent = Intent()
                    storageIntent.setType("image/*")
                    storageIntent.setAction(Intent.ACTION_GET_CONTENT)
                    galleryLauncher.launch(storageIntent)
                }
            })
            builder.show()

        }

        binding.btnContinue.setOnClickListener(){
            i("ALEX start OnClickListener")

            var productName = binding.productName.text.toString()

            if (productName.isEmpty()) {
                i("ALEX Empty")
                Snackbar
                    .make(it,R.string.enter_food_information, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    //app.foods.update(food.copy())
                } else {
                    food = FoodModel(
                        binding.brand.text.toString(),
                        productName,
                        productCategory,
                        binding.barcodeTextView.text.toString())
                    i("ALEX $it")
                }
            }
            i("ALEX continue Button Pressed: $food.brand ${food.productName}")
            setResult(RESULT_OK)
            onContinueButtonPressed()
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_food, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> { finish() }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun processQr() {
        i("ALEX processQr() called")
        barcodeScanner.process(inputImage).addOnSuccessListener {
            for(barcode : Barcode in it) {
                val format = barcode.format
                i("ALEX valueType: $format ${barcode.displayValue} ${barcode.rawValue}")
                when(format) {
                    Barcode.FORMAT_EAN_8 -> {
                        binding.barcodeTextView.setText(barcode.displayValue)
                        i("ALEX format ean8 erkannt")
                    }
                    Barcode.FORMAT_EAN_13 -> {
                        binding.barcodeTextView.setText(barcode.displayValue)
                        i("ALEX format ean13 erkannt")
                    }
                }
            }
        }.addOnFailureListener {
            i("ALEX process failed ${it.message}")
        }
    }

    override fun onResume() {
        super.onResume()
        i("ALEX onResume() called")

        checkPermission(android.Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE)
    }

    private fun checkPermission(permission : String, requestCode : Int) {
        i("ALEX checkPermission() called")
        if(ContextCompat.checkSelfPermission(
                this@FoodCreateActivity, permission
        ) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this@FoodCreateActivity, arrayOf(permission), requestCode)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        i("ALEX onRequestPermissionsResult() called")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode ==CAMERA_PERMISSION_CODE) {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkPermission(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE, READ_STORAGE_PERMISSION_CODE
                )
            }
        }
        else if (requestCode == READ_STORAGE_PERMISSION_CODE) {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkPermission(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_STORAGE_PERMISSION_CODE
                )
            }
        }
    }

    //TO-DO: rename this method
    private fun onContinueButtonPressed() {
        i("onContinueButtonPressed")
        val launcherIntent = Intent(this, NutritionalValuesActivity::class.java)
        launcherIntent.putExtra("food", food)
        if (intent.hasExtra("food_edit"))
            launcherIntent.putExtra("food_edit", true)
        startActivity(launcherIntent)
        //refreshIntentLauncher.launch(launcherIntent)
    }
}