package com.example.contentprovider

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.contentprovider.databinding.ActivityMainBinding
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private var customAdapter: CustomAdapter? = null
    private var contactModelList: MutableList<ContactModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED)
        {
            permissionContact.launch(Manifest.permission.READ_CONTACTS)
            customAdapter?.notifyDataSetChanged()
        } else {
            getContact()
        }



    }

    @SuppressLint("Range")
    private fun getContact() {
        contactModelList = ArrayList()
        val phones = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + "ASC"
        )
        while (phones!!.moveToNext()){
            val name =
                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val phoneNumber =
                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            val contactModel = ContactModel(name, phoneNumber)
            contactModelList?.add(contactModel)

        }
        phones.close()
        customAdapter = CustomAdapter(this,contactModelList!!)
        binding.listView.adapter = customAdapter
        binding.listView.onItemClickListener = AdapterView.OnItemClickListener{ parent, view, position, id ->
            val person = (contactModelList as ArrayList<ContactModel>)[position]
            val number = person.phone
            if (ActivityCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE) !=
                PackageManager.PERMISSION_GRANTED) {
                permissionOfCall.launch(Manifest.permission.CALL_PHONE)
            } else {
                callTheNumber(number)
            }


        }


    }

    private fun callTheNumber(number: String?){
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$number")
        startActivity(intent)
    }

    private val permissionContact = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        isGranted ->
        if (isGranted) {
            Toast.makeText(this@MainActivity,
                "Получен доступ к контактам", Toast.LENGTH_LONG).show()
            getContact()
        } else {
            Toast.makeText(this@MainActivity,
                "Не получен доступ к контактам", Toast.LENGTH_LONG).show()
        }
    }

    private val permissionOfCall = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
            isGranted ->
        if (isGranted) {
            Toast.makeText(this@MainActivity,
                "Получен доступ к звонкам", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this@MainActivity,
                "Не получен доступ к звонкам", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}