package oth.wit.kapk_project.activities

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.SearchView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import oth.wit.kapk_project.R
import oth.wit.kapk_project.adapters.FoodAdapter
import oth.wit.kapk_project.adapters.FoodListener
import oth.wit.kapk_project.databinding.ActivityFoodListBinding
import oth.wit.kapk_project.main.MainApp
import oth.wit.kapk_project.models.FoodModel
import oth.wit.kapk_project.models.FoodStore
import oth.wit.kapk_project.models.MealType
import timber.log.Timber.i
import java.util.*
import kotlin.collections.ArrayList


class FoodListActivity : AppCompatActivity(), FoodListener {
    lateinit var app: MainApp
    private lateinit var binding : ActivityFoodListBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var meal : MealType
    lateinit var tempFoods : ArrayList<FoodModel>
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

        i("ALEX FoodListActivity.onCreate()")
        binding = ActivityFoodListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        meal = MealType.valueOf(intent.getStringExtra("meal").toString())
        binding.toolbarList.title = " Add to " + meal.printableName
        setSupportActionBar(binding.toolbarList)

        app = application as MainApp

        btnScanBarcode = binding.btnScan

        barcodeScanner = BarcodeScanning.getClient()

        cameraLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult(), object :
            ActivityResultCallback<ActivityResult> {
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

        galleryLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult(), object :
            ActivityResultCallback<ActivityResult> {
            override fun onActivityResult(result: ActivityResult?) {
                i("ALEX onActivityResult()")
                val data = result?.data
                try {
                    inputImage = InputImage.fromFilePath(this@FoodListActivity,data?.data)
                    processQr()
                } catch (e : Exception) {i("ALEX onActivityResult: ${e.message}")}
            }
        }
        )

        tempFoods = app.foods.toArrayList()

        i("ALEX create LayoutManager()")
        val layoutManager = LinearLayoutManager(this)

        binding.recyclerView.adapter = FoodAdapter(tempFoods,this)
        binding.recyclerView.layoutManager = layoutManager

        val searchView = binding.productSearch

        btnScanBarcode.setOnClickListener {
            i("ALEX scan button pressed")
            val options = arrayOf("camera", "gallery")

            val builder = AlertDialog.Builder(this@FoodListActivity)
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


        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                i("sucess")

                tempFoods.clear()
                val searchText = newText!!.lowercase(Locale.getDefault())
                if(searchText.isNotEmpty()){

                    app.foods.toArrayList().forEach{
                        if(it.productName.lowercase(Locale.getDefault()).contains(searchText) || it.barcode.contains(searchText)){
                            tempFoods.add(it)
                        }
                    }
                    binding.recyclerView.adapter?.notifyDataSetChanged()

                }else{

                    tempFoods.clear()
                    tempFoods.addAll(app.foods.toArrayList())
                    binding.recyclerView.adapter?.notifyDataSetChanged()

                }

                return false

            }

        })

        //loadFoods()

        registerRefreshCallback()
        i("ALEX finish onCreate()")
    }

    private fun processQr() {
        i("ALEX processQr() called")
        barcodeScanner.process(inputImage).addOnSuccessListener {
            for(barcode : Barcode in it) {
                val format = barcode.format
                i("ALEX valueType: $format ${barcode.displayValue} ${barcode.rawValue}")
                when(format) {
                    Barcode.FORMAT_EAN_8 -> {
                        binding.productSearch.setQuery(barcode.displayValue,true)
                        i("ALEX format ean8 erkannt")
                    }
                    Barcode.FORMAT_EAN_13 -> {
                        binding.productSearch.setQuery(barcode.displayValue,true)
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
                this@FoodListActivity, permission
            ) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this@FoodListActivity, arrayOf(permission), requestCode)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, FoodCreateActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
        }
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }



    override fun onFoodClick(food: FoodModel) {
        i("ALEX FoodListActivity.onFoodClick: $food")
        val launcherIntent = Intent(this, AddConsumedFoodActivity::class.java)
        launcherIntent.putExtra("food", food)
        launcherIntent.putExtra("meal", meal.name)
        startActivity(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadFoods() }
    }

    private fun loadFoods() {
        showFoods(app.foods)
    }

    fun showFoods (foods: FoodStore) {
        binding.recyclerView.adapter = FoodAdapter(foods.toArrayList(), this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

}
