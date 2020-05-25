package uz.ferganagroup.arzonibizdadelivery.screen.main.myorders

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ui.IconGenerator
import kotlinx.android.synthetic.main.fragment_my_orders.*

import uz.ferganagroup.arzonibizdadelivery.R
import uz.ferganagroup.arzonibizdadelivery.base.BaseFragment
import uz.ferganagroup.arzonibizdadelivery.base.showError
import uz.ferganagroup.arzonibizdadelivery.base.startActivityToOpenUrlInBrowser
import uz.ferganagroup.arzonibizdadelivery.listener.SnapCenterListener
import uz.ferganagroup.arzonibizdadelivery.model.OrderModel
import uz.ferganagroup.arzonibizdadelivery.screen.main.MainViewModel
import uz.ferganagroup.arzonibizdadelivery.view.adapter.MyOrdersAdapter
import uz.ferganagroup.arzonibizdadelivery.view.adapter.MyOrdersAdapterListener

/**
 * A simple [Fragment] subclass.
 */
class MyOrdersFragment : BaseFragment(), OnMapReadyCallback {
    override fun getLayout(): Int = R.layout.fragment_my_orders
    lateinit var viewModel: MainViewModel

    companion object {
        const val REQUEST_CHECK_SETTINGS = 43
    }

    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    var selectedOrder: OrderModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_orders, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        return view
    }
    override fun setupViews() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        viewModel.updateProgress.observe(this, Observer { inProgress->
            if (inProgress){
                progressAnimationView.visibility = View.VISIBLE
            }else{
                progressAnimationView.visibility = View.GONE
            }

        })

        viewModel.error.observe(this, Observer {
            activity?.showError(it)
        })

        viewModel.myOrdersData.observe(this, Observer {
            setData()
        })

        viewModel.updateStatusData.observe(this, Observer {
            loadData()
        })

        imgRefresh.setOnClickListener {
            loadData()
        }
        getBaseActivity {
            fusedLocationProviderClient = FusedLocationProviderClient(it)
        }

    }

    override fun loadData() {
        viewModel.getMyOrders()
    }

    override fun setData() {
        if (viewModel.myOrdersData.value != null){
            if (googleMap != null){
                val iconGenerator = IconGenerator(activity)
                viewModel.myOrdersData.value!!.forEach {
                    val btm = iconGenerator.makeIcon("Заказ №" + it.number.toString())
                    googleMap.addMarker(MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromBitmap(btm))
                        .position(LatLng(it.lat.toDoubleOrNull() ?: 0.0, it.long.toDoubleOrNull() ?: 0.0))
                    )
                }
            }
            recycler.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            recycler.adapter = MyOrdersAdapter(viewModel.myOrdersData.value ?: emptyList(), object:
                MyOrdersAdapterListener {
                override fun onClickStatus(item: OrderModel) {
                    when(item.status){
                        2->{
                            viewModel.onwayOrder(item.refKey)
                        }
                        3->{
                            viewModel.finishOrder(item.refKey)
                        }

                    }
                }

                override fun onClickCall(item: OrderModel) {
                    selectedOrder = item
                    checkPermission()
                }

                override fun onClickMap(item: OrderModel) {
                    getBaseActivity {
                        it.startActivityToOpenUrlInBrowser("https://www.google.com/maps/dir/Current+Location/${item.lat},${item.long}")
                    }
                }
            })

        }
    }

    override fun onMapReady(map: GoogleMap?) {
        googleMap = map?: return

        var latLng = LatLng(41.3188929,69.2822557)

        val cameraPosition = CameraPosition.Builder()
            .target(latLng)
            .zoom(17f)
            .build()

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        if (isPermissionGiven()){
            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = true
            googleMap.uiSettings.isZoomControlsEnabled = true
            getCurrentLocation()
        } else {
            givePermission()
        }
    }

    private fun isPermissionGiven(): Boolean{
        return context?.let { ActivityCompat.checkSelfPermission(it, android.Manifest.permission.ACCESS_FINE_LOCATION) } == PackageManager.PERMISSION_GRANTED
    }

    private fun givePermission(){
        getBaseActivity {
            ActivityCompat.requestPermissions(it, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CHECK_SETTINGS)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {

        getBaseActivity {
            val locationRequest = LocationRequest()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = (10 * 1000).toLong()
            locationRequest.fastestInterval = 2000

            val builder = LocationSettingsRequest.Builder()
            builder.addLocationRequest(locationRequest)
            val locationSettingsRequest = builder.build()

            val result = LocationServices.getSettingsClient(it).checkLocationSettings(locationSettingsRequest)
            result.addOnCompleteListener { task ->
                try {
                    val response = task.getResult(ApiException::class.java)
                    if (response!!.locationSettingsStates.isLocationPresent){
                        getLastLocation()
                    }
                } catch (exception: ApiException) {
                    when (exception.statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                            val resolvable = exception as ResolvableApiException
                            resolvable.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS)
                        } catch (e: IntentSender.SendIntentException) {
                        } catch (e: ClassCastException) {
                        }

                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> { }
                    }
                }
            }

            val locationManager = it.getSystemService(LOCATION_SERVICE) as LocationManager?
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 10f, object:
                LocationListener{
                override fun onLocationChanged(location: Location?) {
                    viewModel.updateLocation(location?.latitude ?: 0.0, location?.longitude ?: 0.0)
                }

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

                }

                override fun onProviderEnabled(provider: String?) {

                }

                override fun onProviderDisabled(provider: String?) {

                }
            })

        }

    }

    fun getLastLocation() {
        getBaseActivity {
            fusedLocationProviderClient.lastLocation
                .addOnCompleteListener(it) { task ->
                    if (task.isSuccessful && task.result != null) {
                        val mLastLocation = task.result
                        if (mLastLocation != null){
                            var latLng = LatLng(mLastLocation!!.latitude, mLastLocation.longitude)

                            val cameraPosition = CameraPosition.Builder()
                                .target(LatLng(mLastLocation.latitude, mLastLocation.longitude))
                                .zoom(17f)
                                .build()
                            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                        }
                    } else {
                        Toast.makeText(activity, "No current location found", Toast.LENGTH_LONG).show()
                    }
                }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (requestCode) {
            REQUEST_CHECK_SETTINGS -> {
                if (resultCode == Activity.RESULT_OK) {

                    googleMap?.isMyLocationEnabled = true
                    googleMap?.uiSettings.isMyLocationButtonEnabled = true
                    googleMap?.uiSettings.isZoomControlsEnabled = true

                    getCurrentLocation()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun checkPermission() {
        getBaseActivity {
            if (ContextCompat.checkSelfPermission(it,
                    android.Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(it,
                        android.Manifest.permission.CALL_PHONE)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(it,
                        arrayOf(android.Manifest.permission.CALL_PHONE),
                        42)
                }
            } else {
                // Permission has already been granted
                callPhone()
            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 42) {
            // If request is cancelled, the result arrays are empty.
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // permission was granted, yay!
                callPhone()
            } else {
                // permission denied, boo! Disable the
                // functionality
            }
            return
        }
    }

    @SuppressLint("MissingPermission")
    fun callPhone(){
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + selectedOrder?.clientTelephone))
        startActivity(intent)
    }

}
