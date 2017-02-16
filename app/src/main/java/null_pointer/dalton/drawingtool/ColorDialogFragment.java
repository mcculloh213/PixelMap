package null_pointer.dalton.drawingtool;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * Created by dalton on 1/23/17.
 */

public class ColorDialogFragment extends DialogFragment {
    private SeekBar alphaSeekBar;
    private SeekBar redSeekBar;
    private SeekBar greenSeekBar;
    private SeekBar blueSeekBar;
    private Button redButton;
    private Button orangeButton;
    private Button yellowButton;
    private Button greenButton;
    private Button blueButton;
    private Button indigoButton;
    private Button violetButton;
    private Button whiteButton;
    private Button blackButton;
    private View colorView;
    private int color;

    private MainActivityFragment getMAFragment() {
        return (MainActivityFragment) getFragmentManager().findFragmentById(R.id.drawingFragment);
    }

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        // Initialize dialog
        AlertDialog.Builder build = new AlertDialog.Builder(getActivity());
        View colorDialogView = getActivity().getLayoutInflater().inflate(R.layout.fragment_color,
                null);

        // Add GUI to dialog
        build.setView(colorDialogView);

        build.setTitle(R.string.title_color_dialog);
        build.setCancelable(true);

        // Set onChange listeners to SeekBars
        alphaSeekBar = (SeekBar) colorDialogView.findViewById(R.id.alphaSeekBar);
        redSeekBar = (SeekBar) colorDialogView.findViewById(R.id.redSeekBar);
        greenSeekBar = (SeekBar) colorDialogView.findViewById(R.id.greenSeekBar);
        blueSeekBar = (SeekBar) colorDialogView.findViewById(R.id.blueSeekBar);

        // Get buttons
        redButton = (Button) colorDialogView.findViewById(R.id.cdf_preset_color_redButton);
        orangeButton = (Button) colorDialogView.findViewById(R.id.cdf_preset_color_orangeButton);
        yellowButton = (Button) colorDialogView.findViewById(R.id.cdf_preset_color_yellowButton);
        greenButton = (Button) colorDialogView.findViewById(R.id.cdf_preset_color_greenButton);
        blueButton = (Button) colorDialogView.findViewById(R.id.cdf_preset_color_blueButton);
        indigoButton = (Button) colorDialogView.findViewById(R.id.cdf_preset_color_indigoButton);
        violetButton = (Button) colorDialogView.findViewById(R.id.cdf_preset_color_violetButton);
        whiteButton = (Button) colorDialogView.findViewById(R.id.cdf_preset_color_whiteButton);
        blackButton = (Button) colorDialogView.findViewById(R.id.cdf_preset_color_blackButton);

        // Register SeekBar event listeners
        alphaSeekBar.setOnSeekBarChangeListener(colorChangedListener);
        redSeekBar.setOnSeekBarChangeListener(colorChangedListener);
        greenSeekBar.setOnSeekBarChangeListener(colorChangedListener);
        blueSeekBar.setOnSeekBarChangeListener(colorChangedListener);

        // Register Button event listeners
        redButton.setOnClickListener(colorButtonListener);
        orangeButton.setOnClickListener(colorButtonListener);
        yellowButton.setOnClickListener(colorButtonListener);
        greenButton.setOnClickListener(colorButtonListener);
        blueButton.setOnClickListener(colorButtonListener);
        indigoButton.setOnClickListener(colorButtonListener);
        violetButton.setOnClickListener(colorButtonListener);
        whiteButton.setOnClickListener(colorButtonListener);
        blackButton.setOnClickListener(colorButtonListener);

        colorView = colorDialogView.findViewById(R.id.colorView);

        // Set SeekBar values based on current color
        final DrawingView dv = getMAFragment().getDrawingView();
        color = dv.getDrawingColor();
        alphaSeekBar.setProgress(Color.alpha(color));
        redSeekBar.setProgress(Color.red(color));
        greenSeekBar.setProgress(Color.green(color));
        blueSeekBar.setProgress(Color.blue(color));

        // Set color
        build.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dv.setDrawingColor(color);
            }
        });

        return build.create();
    }

    @Override
    public void onAttach(Context ctxt) {
        super.onAttach(ctxt);
        MainActivityFragment fgmt = getMAFragment();

        if (fgmt != null) {
            fgmt.setDialogOnScreen(true);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        MainActivityFragment fgmt = getMAFragment();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M && fgmt != null) {
            fgmt.setDialogOnScreen(true);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        MainActivityFragment fgmt = getMAFragment();

        if (fgmt != null) {
            fgmt.setDialogOnScreen(false);
        }
    }

    private final OnSeekBarChangeListener colorChangedListener = new OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // Change color based on user input
            if (fromUser) {
                color = Color.argb(alphaSeekBar.getProgress(), redSeekBar.getProgress(),
                        greenSeekBar.getProgress(), blueSeekBar.getProgress());
            }
            colorView.setBackgroundColor(color);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // Implement abstract
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // Implement abstract
        }

    };

    private View.OnClickListener colorButtonListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            ColorDrawable background = (ColorDrawable) ((Button) view).getBackground();
            color = background.getColor();
            alphaSeekBar.setProgress(Color.alpha(color));
            redSeekBar.setProgress(Color.red(color));
            greenSeekBar.setProgress(Color.green(color));
            blueSeekBar.setProgress(Color.blue(color));
            colorView.setBackgroundColor(color);
        }

    };

}
