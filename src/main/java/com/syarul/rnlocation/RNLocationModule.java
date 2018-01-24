package com.syarul.rnlocation;

import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.os.Bundle;
// import android.widget.Toast;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class RNLocationModule extends ReactContextBaseJavaModule{

    // React Class Name as called from JS
    public static final String REACT_CLASS = "RNLocation";
    // Unique Name for Log TAG
    public static final String TAG = RNLocationModule.class.getSimpleName();
    // Save last Location Provided
    private Location mLastLocation;
    private LocationListener mLocationListener;
    private LocationManager locationManager;

    //The React Native Context
    ReactApplicationContext mReactContext;


    // Constructor Method as called in Package
    public RNLocationModule(ReactApplicationContext reactContext) {
        super(reactContext);
        // Save Context for later use
        mReactContext = reactContext;

        locationManager = (LocationManager) mReactContext.getSystemService(Context.LOCATION_SERVICE);
        mLastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }


        @Override
        public String getName() {
          return REACT_CLASS;
        }
        /*
         * Location permission request (Not implemented yet)
         */
        @ReactMethod
        public void requestWhenInUseAuthorization(){
          Log.i(TAG, "Requesting authorization");
        }
        /*
         * Location Callback as called by JS
         */
        @ReactMethod
        public void startUpdatingLocation(int minTime, float minDistance) {
          mLocationListener = new LocationListener(){
            @Override
            public void onStatusChanged(String str,int in,Bundle bd){
            }

            @Override
            public void onProviderEnabled(String str){
            }

            @Override
            public void onProviderDisabled(String str){
            }

            @Override
            public void onLocationChanged(Location loc){
                mLastLocation = loc;
                if (mLastLocation != null) {
                  try {
                    double longitude;
                    double latitude;
                    double speed;
                    double altitude;
                    double accuracy;
                    double course;

                    // Receive Longitude / Latitude from (updated) Last Location
                    longitude = mLastLocation.getLongitude();
                    latitude = mLastLocation.getLatitude();
                    speed = mLastLocation.getSpeed();
                    altitude = mLastLocation.getAltitude();
                    accuracy = mLastLocation.getAccuracy();
                    course = mLastLocation.getBearing();

                    Log.i(TAG, "Got new location. Lng: " +longitude+" Lat: "+latitude);

                   // Create Map with Parameters to send to JS
                    WritableMap params = Arguments.createMap();
                    params.putDouble("longitude", longitude);
                    params.putDouble("latitude", latitude);
                    params.putDouble("speed", speed);
                    params.putDouble("altitude", altitude);
                    params.putDouble("accuracy", accuracy);
                    params.putDouble("course", course);

                    // Send Event to JS to update Location
                    sendEvent(mReactContext, "locationUpdated", params);

                    // CharSequence text = latitude + " " + longitude + " " + speed;
                    // int duration = Toast.LENGTH_SHORT;
                    //
                    // Toast toast = Toast.makeText(mReactContext, text, duration);
                    // toast.show();
                  } catch (Exception e) {
                    e.printStackTrace();
                    Log.i(TAG, "Location services disconnected.");
                  }
              }


        }};
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, mLocationListener);

        }

//    @ReactMethod
//    public void getLocation() {
//        CharSequence text = "getLocation";
//        int duration = Toast.LENGTH_SHORT;
//
//        Toast toast = Toast.makeText(mReactContext, text, duration);
//        toast.show();
//        mLocationListener = new LocationListener(){
//            @Override
//            public void onStatusChanged(String str,int in,Bundle bd){
//                CharSequence text = "onStatusChanged";
//                int duration = Toast.LENGTH_SHORT;
//
//                Toast toast = Toast.makeText(mReactContext, text, duration);
//                toast.show();
//            }
//
//            @Override
//            public void onProviderEnabled(String str){
//                CharSequence text = "onProviderEnabled";
//                int duration = Toast.LENGTH_SHORT;
//
//                Toast toast = Toast.makeText(mReactContext, text, duration);
//                toast.show();
//            }
//
//            @Override
//            public void onProviderDisabled(String str){
//                CharSequence text = "onProviderDisabled";
//                int duration = Toast.LENGTH_SHORT;
//
//                Toast toast = Toast.makeText(mReactContext, text, duration);
//                toast.show();
//            }
//
//            @Override
//            public void onLocationChanged(Location loc){
//                CharSequence text = "onLocationChanged";
//                int duration = Toast.LENGTH_SHORT;
//
//                Toast toast = Toast.makeText(mReactContext, text, duration);
//                toast.show();
//                mLastLocation = loc;
//                if (mLastLocation != null) {
//                    try {
//                        double longitude;
//                        double latitude;
//                        double speed;
//                        double altitude;
//                        double accuracy;
//                        double course;
//
//                        // Receive Longitude / Latitude from (updated) Last Location
//                        longitude = mLastLocation.getLongitude();
//                        latitude = mLastLocation.getLatitude();
//                        speed = mLastLocation.getSpeed();
//                        altitude = mLastLocation.getAltitude();
//                        accuracy = mLastLocation.getAccuracy();
//                        course = mLastLocation.getBearing();
//
//                        Log.i(TAG, "Got new location. Lng: " +longitude+" Lat: "+latitude);
//
//                        // Create Map with Parameters to send to JS
//                        WritableMap params = Arguments.createMap();
//                        params.putDouble("longitude", longitude);
//                        params.putDouble("latitude", latitude);
//                        params.putDouble("speed", speed);
//                        params.putDouble("altitude", altitude);
//                        params.putDouble("accuracy", accuracy);
//                        params.putDouble("course", course);
//
//                        // Send Event to JS to update Location
//                        sendEvent(mReactContext, "getLocation", params);
//
//                         CharSequence text2 = latitude + " " + longitude + " " + speed + " " + course;
//                         int duration2 = Toast.LENGTH_SHORT;
//                         Toast toast2 = Toast.makeText(mReactContext, text2, duration2);
//                        toast2.show();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Log.i(TAG, "Location services disconnected.");
//                    }
//                }
//
//
//            }};
//
////        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
////        networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
////
////        if(!gpsEnabled && !networkEnabled)
////            return false;
////
////        if(gpsEnabled && networkEnabled) {
////            // DEBUG
////            Log.d("GPS Connection", "Request GPS Data");
////            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, gpsLocationListener, Looper.getMainLooper());
////            gpsGetLocationTimeout = new Timer();
////            gpsGetLocationTimeout.schedule(new GetNetworkLocation(), GPS_MAX_CONNECTION_TIME_MS);
////        }
//        locationManager.requestSingleUpdate( LocationManager.NETWORK_PROVIDER, mLocationListener, null );
//    }

        @ReactMethod
        public void stopUpdatingLocation() {
            try {
                locationManager.removeUpdates(mLocationListener);
                Log.i(TAG, "Location service disabled.");
            }catch(Exception e) {
                e.printStackTrace();
            }
        }

        /*
         * Internal function for communicating with JS
         */
        private void sendEvent(ReactContext reactContext, String eventName, @Nullable WritableMap params) {
          if (reactContext.hasActiveCatalystInstance()) {
            reactContext
              .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
              .emit(eventName, params);
          } else {
            Log.i(TAG, "Waiting for CatalystInstance...");
          }
        }
    }
