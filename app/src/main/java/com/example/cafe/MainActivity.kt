package com.example.cafe

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.example.cafe.databinding.ActivityMainBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.JsonObject
import com.mancj.materialsearchbar.MaterialSearchBar

class MainActivity : AppCompatActivity(), GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener,
    OnMapReadyCallback {

    private lateinit var binding : ActivityMainBinding
    private lateinit var database: DatabaseReference    // 데이터베이스

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        database = Firebase.database.reference  // 데이터베이스
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        searchBar()

        val reference = database.child("0") // 읽어올 데이터의 경로를 지정합니다.

        // 데이터를 읽어온다.
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val store = dataSnapshot.getValue(Data::class.java)
                if (store != null) {
                    // 데이터를 성공적으로 가져왔을 때 처리할 로직
                    Log.d(TAG, "hardness: ${store.hardness}")
                    Log.d(TAG, "latitude: ${store.latitude}")
                    Log.d(TAG, "newAddress: ${store.newAddress}")
                    Log.d(TAG, "oldAddress: ${store.oldAddress}")
                    Log.d(TAG, "storeName: ${store.storeName}")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 데이터 읽기 도중 에러가 발생했을 때 호출됩니다.
                Log.e(TAG, "Database error: ${databaseError.message}")
            }
        }

        reference.addValueEventListener(valueEventListener)




    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap) {
        // TODO: Before enabling the My Location layer, you must request
        // location permission from the user. This sample does not include
        // a request for location permission.
        map.isMyLocationEnabled = true
        map.setOnMyLocationButtonClickListener(this)
        map.setOnMyLocationClickListener(this)
    }

    // 마커클릭
    override fun onMyLocationClick(location: Location) {
        Toast.makeText(this, "현재 내 위치:\n$location", Toast.LENGTH_LONG)
            .show()
    }

    // 내 위치 클릭
    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "내 위치 클릭", Toast.LENGTH_SHORT)
            .show()
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false
    }

    private fun searchBar(){
        //View - 변수 연결
        val lv =binding.mListView as ListView
        val searchBar = binding.searchBar
        searchBar.setHint("상호명을 입력하세요")
        //검색어 목록 넣기
        var galaxies = arrayOf("Sombrero", "Cartwheel", "Pinwheel", "StarBust", "Whirlpool", "Ring Nebular", "Own Nebular", "Centaurus A", "Virgo Stellar Stream", "Canis Majos Overdensity", "Mayall's Object", "Leo", "Milky Way", "IC 1011", "Messier 81", "Andromeda", "Messier 87")

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, galaxies)
        //리스트뷰 초기에 안보이게 설정
        lv.visibility = View.INVISIBLE
        //SearchBar와 ListView 연동
        lv.setAdapter(adapter)
        searchBar.setOnSearchActionListener(object : MaterialSearchBar.OnSearchActionListener{
            override fun onButtonClicked(buttonCode: Int) {
                TODO("Not yet implemented")
            }
            //검색창 누른 상태 여부 확인
            override fun onSearchStateChanged(enabled: Boolean) {
                //맞으면 리스트뷰 보이게 설정
                if(enabled){
                    lv.visibility = View.VISIBLE
                }else{ //아니면 안 보이게
                    lv.visibility = View.INVISIBLE
                }
            }

            override fun onSearchConfirmed(text: CharSequence?) {
                TODO("Not yet implemented")
            }

        })

        searchBar.addTextChangeListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            //검색어 변경하면 ListView 내용 변경
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.getFilter().filter(s)
            }

        })

        //ListView 내의 아이템 누르면 Toast 발생
        lv.setOnItemClickListener(object : AdapterView.OnItemClickListener{
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(this@MainActivity, adapter.getItem(position)!!.toString(), Toast.LENGTH_SHORT).show()
            }

        })
    }




}