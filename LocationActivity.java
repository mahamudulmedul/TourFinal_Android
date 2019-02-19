package bd.org.bitm.mad.batch33.tourmate;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

import bd.org.bitm.mad.batch33.tourmate.Direction.DirectionResponse;
import bd.org.bitm.mad.batch33.tourmate.Direction.DirectionService;
import bd.org.bitm.mad.batch33.tourmate.Direction.Step;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnPolylineClickListener {
    private GoogleMap map;
    private GoogleMapOptions options;
    private GeoDataClient geoDataClient;
    private PlaceDetectionClient placeDetectionClient;
    private ClusterManager<MyItem> clusterManager;
    private List<MyItem> clusterItems = new ArrayList<>();
    private String[] instructions;
    private Button btn;
    private FusedLocationProviderClient client;

    public static double latitude;
    public static double longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        btn = findViewById(R.id.btn);
        geoDataClient = Places.getGeoDataClient(this);
        placeDetectionClient = Places.getPlaceDetectionClient(this);
        options = new GoogleMapOptions();
        options.zoomControlsEnabled(true);
        SupportMapFragment mapFragment = SupportMapFragment.newInstance(options);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction()
                .replace(R.id.mapLocation, mapFragment);
        ft.commit();
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

      if(checkLocationPermission())
        map.setMyLocationEnabled(true);
        clusterManager = new ClusterManager<MyItem>(this,map);
        map.setOnCameraIdleListener(clusterManager);
        map.setOnMarkerClickListener(clusterManager);
        map.setOnPolylineClickListener(this);
        LatLng latLng = new LatLng(latitude,longitude);
        map.addMarker(new MarkerOptions().position(latLng).title("BITM").snippet("BDBL Bhaban, Karwanbazar, Dhaka"));

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));

map.clear();
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {


            @Override
            public void onMapLongClick(LatLng latLng) {
                map.addMarker(new MarkerOptions().position(latLng).title("").snippet(""));
            }

        });

       // getDirections();

    }

    private void getDirections(LatLng startPoint, LatLng endPoint) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        DirectionService service = retrofit.create(DirectionService.class);
        String key = getString(R.string.direction_api_key);
        String url = String.format("directions/json?origin=%f,%f&destination=%d,%d&alternatives=true&key=%s",startPoint.latitude,
                startPoint.longitude,
                endPoint.latitude,
                endPoint.longitude,key);
        Call<DirectionResponse> call = service.getDirections(url);

        call.enqueue(new Callback<DirectionResponse>() {
            @Override
            public void onResponse(Call<DirectionResponse> call, Response<DirectionResponse> response) {
                if(response.code() == 200){
                    DirectionResponse directionResponse = response.body();
                    List<Step>steps = directionResponse.getRoutes()
                            .get(0).getLegs().get(0)
                            .getSteps();
                    btn.setVisibility(View.VISIBLE);
                    instructions = new String[steps.size()];
                    for(int i = 0; i < steps.size(); i++){
                        double startLatitude = steps.get(i).getStartLocation().getLat();
                        double startLongitude = steps.get(i).getStartLocation().getLng();

                        LatLng startPoint = new LatLng(startLatitude,startLongitude);

                        double endLatitude = steps.get(i).getEndLocation().getLat();
                        double endLongitude = steps.get(i).getEndLocation().getLng();

                        LatLng endPoint = new LatLng(endLatitude,endLongitude);

                        Polyline polyline = map.addPolyline(new PolylineOptions()
                                .add(startPoint)
                                .add(endPoint)
                                .color(Color.BLUE)
                                .clickable(true));



                        String instructionsLine = String.valueOf(Html.fromHtml(steps.get(i).getHtmlInstructions()));
                        String distance = steps.get(i).getDistance().getText();
                        String time = steps.get(i).getDuration().getText();
                        instructions[i] = instructionsLine+"-"+distance+", "+time;
                        polyline.setTag(instructionsLine);

                    }

                }
            }

            @Override
            public void onFailure(Call<DirectionResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 555 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "granted", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkLocationPermission(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION},555);
            return false;
        }
        return true;
    }

    public void getCurrentPlaces(View view) {
        if(checkLocationPermission())
            placeDetectionClient.getCurrentPlace(null)
                    .addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
                        @Override
                        public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                            if(task.isSuccessful() && task.getResult() != null){
                                PlaceLikelihoodBufferResponse response = task.getResult();
                                int count = response.getCount();
                                for(int i = 0; i < count; i++){
                                    PlaceLikelihood placeLikelihood = response.get(i);
                                    String name = (String) placeLikelihood.getPlace().getName();
                                    String address = (String) placeLikelihood.getPlace().getAddress();
                                    LatLng latLng = placeLikelihood.getPlace().getLatLng();
                                    addCurrentPlaceMarker(name,address,latLng);
                                }
                                clusterManager.addItems(clusterItems);
                                clusterManager.cluster();
                            }
                        }
                    });
    }
    public void getLocations() {
        checkLocationPermission();
        client.getLastLocation()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LocationActivity.this, "Finding location failed", Toast.LENGTH_SHORT).show();
                        Log.e("Location error: ", e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();

                        Log.d("lat: ", latitude + "");
                        Log.d("lon: ", longitude + "");
                    }
                });
    }



    private void addCurrentPlaceMarker(String name, String address, LatLng latLng) {
        map.addMarker(new MarkerOptions().position(latLng).title(name).snippet(address));
        MyItem item = new MyItem(latLng,name,address);
        clusterItems.add(item);
    }

    public void getInstructions(View view) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setItems(instructions,null)
                .show();
    }

    @Override
    public void onPolylineClick(Polyline polyline) {
        String instruction = polyline.getTag().toString();
        Toast.makeText(this, instruction, Toast.LENGTH_SHORT).show();
    }
}
