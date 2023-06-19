package com.example.cafe

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.cafe.adapters.Adapter
import com.example.cafe.adapters.DetailAdapter
import com.example.cafe.databinding.ActivityMainBinding
import com.example.cafe.datas.Data
import com.example.cafe.datas.DetailData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mancj.materialsearchbar.MaterialSearchBar



class MainActivity : AppCompatActivity(), GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener,
    OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    // 위치 권한 요청
    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // 정확한 위치 액세스 권한이 부여되었습니다.
               // updateLocation()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // 대략적인 위치 액세스만 허용됩니다.
            } else -> {
                Toast.makeText(this, "위치 권한 필요합니다.", Toast.LENGTH_SHORT).show()
                Log.e("위치권한에러","에러에러")
            }
        }
    }


    private lateinit var binding : ActivityMainBinding
    private lateinit var database: DatabaseReference    // 데이터베이스
    lateinit var mAdapter : Adapter
    val mData = ArrayList<Data>()
    private lateinit var sydney : LatLng  // 첫 화면 내위치 설정
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var map: GoogleMap? = null
    private val DEFAULT_ZOOM = 15f // 기본 줌 레벨 설정


    override fun onCreate(savedInstanceState: Bundle?) {
        

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        database = Firebase.database.reference  // 데이터베이스
        setContentView(binding.root)



        // 실제 권한 요청을 수행하기 전에 앱이
        // 이미 권한이 있고 앱이 권한을 표시해야 하는지 여부
        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,   //자세한 위치 약 50m??
            Manifest.permission.ACCESS_COARSE_LOCATION)) // 대략적인 위치 약 20km??


//        val mapFragment = supportFragmentManager
////            .findFragmentById(R.id.map) as SupportMapFragment
////        mapFragment.getMapAsync(this)

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)


        searchBar()

//        val reference = database.child("0") // 읽어올 데이터의 경로를 지정합니다.
//
//        // 데이터를 읽어온다.
//        val valueEventListener = object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                val store = dataSnapshot.getValue(Data::class.java)
//                if (store != null) {
//                    // 데이터를 성공적으로 가져왔을 때 처리할 로직
//                    Log.d(TAG, "hardness: ${store.hardness}")
//                    Log.d(TAG, "latitude: ${store.latitude}")
//                    Log.d(TAG, "newAddress: ${store.newAddress}")
//                    Log.d(TAG, "oldAddress: ${store.oldAddress}")
//                    Log.d(TAG, "storeName: ${store.storeName}")
//
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // 데이터 읽기 도중 에러가 발생했을 때 호출됩니다.
//                Log.e(TAG, "Database error: ${databaseError.message}")
//            }
//        }
//
//        reference.addValueEventListener(valueEventListener)
    }



    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap) {

        // TODO: Before enabling the My Location layer, you must request
        // 사용자의 위치 권한. 이 샘플은 포함하지 않습니다
        // 위치 권한 요청.
        map.isMyLocationEnabled = true // 기본 마커
        map.setOnMyLocationButtonClickListener(this)
        map.setOnMyLocationClickListener(this)


        // 위치서비스 클라이언트 만들기
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        // 마지막으로 알려진 위치 가져오기
        fusedLocationClient?.lastLocation?.addOnSuccessListener { location ->
            // 위치 가져오기 성공 시 호출되는 블록
            sydney = LatLng(location.latitude, location.longitude)
            Log.e("초기 센디값 : ",sydney.toString())

            // 맵에 마커 추가
            map?.addMarker(
                MarkerOptions()
                    .position(sydney)
                    .title("현재 내 위치")
            )

            //카메라 줌
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(sydney, DEFAULT_ZOOM)
            // 맵 카메라 이동
            map?.moveCamera(cameraUpdate)

        }


        //sydney = LatLng(35.12, 22.15)
         //지도의 카메라를 같은 위치로 이동합니다.
//        map.addMarker(
//            MarkerOptions()
//                .position(sydney)
//                .title("Marker in Sydney")
//        )
//        map.moveCamera(CameraUpdateFactory.newLatLng(sydney))

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
        // 이벤트를 소비하지 않고 기본 동작이 계속 발생하도록 false를 반환합니다.
        // (카메라는 사용자의 현재 위치로 움직입니다).
        return false
    }

    private fun searchBar(){
        mAdapter = Adapter(this,R.layout.item_list,mData)

                for(i : Int in 0..7323){
            val reference = database.child("$i") // 읽어올 데이터의 경로를 지정합니다.

            // 데이터를 읽어온다.
            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val store = dataSnapshot.getValue(Data::class.java)
                    if (store != null) {
                        // 데이터를 성공적으로 가져왔을 때 처리할 로직
                        //Log.d(TAG, "hardness: ${store.hardness}")
                        //Log.d(TAG, "latitude: ${store.latitude}")
                        //Log.d(TAG, "newAddress: ${store.newAddress}")
                        //Log.d(TAG, "oldAddress: ${store.oldAddress}")
                        //Log.d(TAG, "storeName: ${store.storeName}")
                        mData.add(Data(store.hardness,store.latitude,store.newAddress,store.oldAddress,store.storeName))

                    }
                    binding.mListView.adapter = mAdapter
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // 데이터 읽기 도중 에러가 발생했을 때 호출됩니다.
                    Log.e(TAG, "Database error: ${databaseError.message}")
                }
            }

            reference.addValueEventListener(valueEventListener)
        }

                //View - 변수 연결
        val lv =binding.mListView as ListView
        val searchBar = binding.searchBar
        searchBar.setHint("상호명을 입력하세요")

        //리스트뷰 초기에 안보이게 설정
        lv.visibility = View.INVISIBLE
        //SearchBar와 ListView 연동
        lv.setAdapter(mAdapter)
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
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                mAdapter.filter.filter(s)
//
//            }
            //검색어 변경하면 ListView 내용 변경
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchQuery = s.toString()
                val query = database.orderByChild("storeName").startAt(searchQuery).endAt(searchQuery + "\uf8ff")

                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        mData.clear() // 기존 데이터를 지우고 새로운 데이터로 대체하기 위해 목록을 초기화합니다.
                        for (snapshot in dataSnapshot.children) {
                            val store = snapshot.getValue(Data::class.java)
                            if (store != null) {
                                mData.add(store)
                            }
                        }
                        mAdapter.notifyDataSetChanged() // 데이터가 변경되었음을 어댑터에 알려서 UI를 업데이트합니다.
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.e(TAG, "Database error: ${databaseError.message}")
                    }
                })
            }
        })

//        lv.setOnItemClickListener { parent, view, position, id ->
//            val selectedItem = mAdapter.getItem(position)
//            val latitude = selectedItem?.latitude
//            val hardness = selectedItem?.hardness
//
//            if (latitude != null && hardness != null) {
//                // 위도와 경도 사용
//                Toast.makeText(this@MainActivity, "Latitude: $latitude, Longitude: $hardness", Toast.LENGTH_SHORT).show()
//
//                // 맵에 마커 추가
//                val sydney = LatLng(latitude, hardness)
//                Log.e("클릭 시 센디값 : ",sydney.toString())
//                map?.let {
//                    it.clear() // 기존 마커 제거
//                    it.addMarker(
//                        MarkerOptions()
//                            .position(sydney)
//                            .title("카페위치")
//                    )
//                    // 카메라 줌
//                    val cameraUpdate = CameraUpdateFactory.newLatLng(sydney)
//                    it.moveCamera(cameraUpdate)
//                    it.animateCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM))
//                }
//            }
//        }







        //ListView 내의 아이템 누르면 Toast 발생
        lv.setOnItemClickListener(object : AdapterView.OnItemClickListener{

            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(this@MainActivity, mAdapter.getItem(position)!!.toString(), Toast.LENGTH_SHORT).show()
                Log.e("aa", mAdapter.getPosition(Data())!!.toString())


                // 맵에 마커 추가
                map?.addMarker(
                    MarkerOptions()
                        .position(sydney)
                        .title("현재 내 위치")
                )

            }

        })














//        for(i : Int in 0..30){
//            val reference = database.child("$i") // 읽어올 데이터의 경로를 지정합니다.
//
//            // 데이터를 읽어온다.
//            val valueEventListener = object : ValueEventListener {
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    val store = dataSnapshot.getValue(Data::class.java)
//                    if (store != null) {
//                        // 데이터를 성공적으로 가져왔을 때 처리할 로직
//                        //Log.d(TAG, "hardness: ${store.hardness}")
//                        //Log.d(TAG, "latitude: ${store.latitude}")
//                        Log.d(TAG, "newAddress: ${store.newAddress}")
//                        //Log.d(TAG, "oldAddress: ${store.oldAddress}")
//                        Log.d(TAG, "storeName: ${store.storeName}")
//                        mData.add(Data(store.newAddress,store.storeName))
//                    }
//                    binding.mListView.adapter = mAdapter
//                }
//
//                override fun onCancelled(databaseError: DatabaseError) {
//                    // 데이터 읽기 도중 에러가 발생했을 때 호출됩니다.
//                    Log.e(TAG, "Database error: ${databaseError.message}")
//                }
//            }
//
//            reference.addValueEventListener(valueEventListener)
//        }





























//        //View - 변수 연결
//        val lv =binding.mListView as ListView
//        val searchBar = binding.searchBar
//        searchBar.setHint("상호명을 입력하세요")
//        //검색어 목록 넣기
//        var galaxies = arrayOf("Sombrero", "Cartwheel", "Pinwheel", "StarBust", "Whirlpool", "Ring Nebular", "Own Nebular", "Centaurus A", "Virgo Stellar Stream", "Canis Majos Overdensity", "Mayall's Object", "Leo", "Milky Way", "IC 1011", "Messier 81", "Andromeda", "Messier 87")
//
//        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, galaxies)
//        //리스트뷰 초기에 안보이게 설정
//        lv.visibility = View.INVISIBLE
//        //SearchBar와 ListView 연동
//        lv.setAdapter(adapter)
//        searchBar.setOnSearchActionListener(object : MaterialSearchBar.OnSearchActionListener{
//            override fun onButtonClicked(buttonCode: Int) {
//                TODO("Not yet implemented")
//            }
//            //검색창 누른 상태 여부 확인
//            override fun onSearchStateChanged(enabled: Boolean) {
//                //맞으면 리스트뷰 보이게 설정
//                if(enabled){
//                    lv.visibility = View.VISIBLE
//                }else{ //아니면 안 보이게
//                    lv.visibility = View.INVISIBLE
//                }
//            }
//
//            override fun onSearchConfirmed(text: CharSequence?) {
//                TODO("Not yet implemented")
//            }
//
//        })
//
//        searchBar.addTextChangeListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//            //검색어 변경하면 ListView 내용 변경
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                adapter.getFilter().filter(s)
//            }
//
//        })
//
//        //ListView 내의 아이템 누르면 Toast 발생
//        lv.setOnItemClickListener(object : AdapterView.OnItemClickListener{
//            override fun onItemClick(
//                parent: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long
//            ) {
//                Toast.makeText(this@MainActivity, adapter.getItem(position)!!.toString(), Toast.LENGTH_SHORT).show()
//            }
//
//        })

    }



    private fun updateLocation(){
        // 위치서비스 클라이언트 만들기
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 실제 권한 요청을 수행하기 전에 앱이
            // 이미 권한이 있고 앱이 권한을 표시해야 하는지 여부
            locationPermissionRequest.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,   //자세한 위치 약 50m??
                Manifest.permission.ACCESS_COARSE_LOCATION)) // 대략적인 위치 약 20km??
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener {// 지속적으로 위치권한을 요청하면 휴대폰에 부하가 걸린다. 그래서 마지막 장소를 불러온다.
         //   sydney = LatLng(it.latitude,it.longitude)
        }
    }
}