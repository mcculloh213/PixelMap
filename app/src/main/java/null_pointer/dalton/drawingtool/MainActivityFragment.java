package null_pointer.dalton.drawingtool;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private DrawingView dv;
    private float acceleration;
    private float currentAcceleration;
    private float lastAcceleration;
    private boolean dialogOnScreen = false;

    private static final int ACCELERATION_THRESHOLD = 100000;

    private static final int SAVE_IMAGE_PERMISSION_REQUEST_CODE = 1;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        setHasOptionsMenu(true); // Show menu items in fragment

        dv = (DrawingView) view.findViewById(R.id.drawingView);

        // SHAKE TO ERASE -- Utilizes handheld accelerometer
        acceleration = 0.00f;
        currentAcceleration = SensorManager.GRAVITY_EARTH;
        lastAcceleration = SensorManager.GRAVITY_EARTH;
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        enableAccelerometerListening();
    }

    @Override
    public void onPause() {
        super.onPause();
        disableAccelerometerListening();
    }

    private void enableAccelerometerListening() {
        // Pull SensorManager
        SensorManager sm = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        // Register Listener for SensorManager
        sm.registerListener(sel, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void disableAccelerometerListening() {
        // Pull SensorManager
        SensorManager sm = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        
        // Deregister Listner for SensorManager
        sm.unregisterListener(sel, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
    }

    private void confirmErase() {
        EraseImageDialogFragment fgt = new EraseImageDialogFragment();
        fgt.show(getFragmentManager(), "Erase Dialog");
    }

    private void saveImage() {
        if (getContext().checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder build = new AlertDialog.Builder(getActivity());

                // AlertDialog message
                build.setMessage(R.string.permission_explanation);

                // Add accept button
                build.setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(new String[]{
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        SAVE_IMAGE_PERMISSION_REQUEST_CODE);
                            }
                        }
                );

                build.create().show();
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        SAVE_IMAGE_PERMISSION_REQUEST_CODE);
            }
        } else {
            // App already has permissions to write to external drive
            dv.saveImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case SAVE_IMAGE_PERMISSION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dv.saveImage();
                    return;
                }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.drawing_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.colour:
                ColourDialogFragment cd = new ColourDialogFragment();
                cd.show(getFragmentManager(), "Colour Dialog");
                return true;
            case R.id.line_width:
                LineWidthDialogFragment lwd = new LineWidthDialogFragment();
                lwd.show(getFragmentManager(), "Line Width Dialog");
                return true;
            case R.id.delete_drawing:
                confirmErase();
                return true;
            case R.id.save:
                dv.saveImage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public DrawingView getDrawingView() {
        return dv;
    }

    public void setDialogOnScreen(boolean visible) {
        dialogOnScreen = visible;
    }

    // Accelerometer event handler
    private final SensorEventListener sel = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent evt) {
            if (!dialogOnScreen) {
                
                // Get coordinate values of SensorEvent
                float x = evt.values[0];
                float y = evt.values[1];
                float z = evt.values[2];
                
                // Replace lastAcceleration
                lastAcceleration = currentAcceleration;
                
                // Update currentAcceleration
                currentAcceleration = x * x + y * y + z * z;
                
                // Calculate delta
                acceleration = currentAcceleration * (currentAcceleration - lastAcceleration);
                
                // If acceleration is greater than ACCELERATION_THRESHOLD
                if (acceleration > ACCELERATION_THRESHOLD) {
                    // Erase drawing dialog
                    confirmErase();
                }
            }
        }
        
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Implement empty method of abstract SensorEventListener    
        }
    };
}
