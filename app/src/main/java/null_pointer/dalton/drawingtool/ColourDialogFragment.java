package null_pointer.dalton.drawingtool;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * Created by dalton on 1/23/17.
 */

public class ColourDialogFragment extends DialogFragment {
    private SeekBar alphaSeekBar;
    private SeekBar redSeekBar;
    private SeekBar greenSeekBar;
    private SeekBar blueSeekBar;
    private View colourView;
    private int colour;

    private MainActivityFragment getMAFragment() {
        return (MainActivityFragment) getFragmentManager().findFragmentById(R.id.drawingFragment);
    }

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        // Initialize dialog
        AlertDialog.Builder build = new AlertDialog.Builder(getActivity());
        View colourDialogView = getActivity().getLayoutInflater().inflate(R.layout.fragment_colour,
                null);

        // Add GUI to dialog
        build.setView(colourDialogView);

        build.setTitle(R.string.title_colour_dialog);
        build.setCancelable(true);

        // Set onChange listeners to SeekBars
        alphaSeekBar = (SeekBar) colourDialogView.findViewById(R.id.alphaSeekBar);
        redSeekBar = (SeekBar) colourDialogView.findViewById(R.id.redSeekBar);
        greenSeekBar = (SeekBar) colourDialogView.findViewById(R.id.greenSeekBar);
        blueSeekBar = (SeekBar) colourDialogView.findViewById(R.id.blueSeekBar);

        // Register SeekBar event listeners
        alphaSeekBar.setOnSeekBarChangeListener(colourChangedListener);
        redSeekBar.setOnSeekBarChangeListener(colourChangedListener);
        greenSeekBar.setOnSeekBarChangeListener(colourChangedListener);
        blueSeekBar.setOnSeekBarChangeListener(colourChangedListener);
        colourView = colourDialogView.findViewById(R.id.colourView);

        // Set SeekBar values based on current colour
        final DrawingView dv = getMAFragment().getDrawingView();
        colour = dv.getDrawingColour();
        alphaSeekBar.setProgress(Color.alpha(colour));
        redSeekBar.setProgress(Color.red(colour));
        greenSeekBar.setProgress(Color.green(colour));
        blueSeekBar.setProgress(Color.blue(colour));

        // Set Colour
        build.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dv.setDrawingColour(colour);
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

    private final OnSeekBarChangeListener colourChangedListener = new OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // Change colour based on user input
            if (fromUser) {
                colour = Color.argb(alphaSeekBar.getProgress(), redSeekBar.getProgress(),
                        greenSeekBar.getProgress(), blueSeekBar.getProgress());
            }
            colourView.setBackgroundColor(colour);
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
}
