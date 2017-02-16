package null_pointer.dalton.drawingtool;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * Created by dalton on 1/23/17.
 */

public class LineWidthDialogFragment extends DialogFragment {
    private ImageView widthImageView;

    private MainActivityFragment getMAFragment() {
        return (MainActivityFragment) getFragmentManager().findFragmentById(R.id.drawingFragment);
    }

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        // Initialize Dialog
        AlertDialog.Builder build = new AlertDialog.Builder(getActivity());
        View lineWidthDialogView = getActivity().getLayoutInflater().inflate(
                R.layout.fragment_line_width, null);

        // Add GUI to dialog
        build.setView(lineWidthDialogView);

        // Set dialog message
        build.setTitle(R.string.title_line_width_dialog);
        build.setCancelable(true);

        // Get ImageView
        widthImageView = (ImageView) lineWidthDialogView.findViewById(R.id.widthImageView);

        // Setup SeekBar
        final DrawingView dv = getMAFragment().getDrawingView();
        final SeekBar widthSeekBar = (SeekBar) lineWidthDialogView.findViewById(R.id.widthSeekBar);
        widthSeekBar.setOnSeekBarChangeListener(lineWidthChanged);
        widthSeekBar.setProgress(dv.getLineWidth());

        // Add button
        build.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dv.setLineWidth(widthSeekBar.getProgress());
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

    public final OnSeekBarChangeListener lineWidthChanged = new OnSeekBarChangeListener() {
        final Bitmap bitmap = Bitmap.createBitmap(400, 100, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            Paint p = new Paint();
            p.setColor(getMAFragment().getDrawingView().getDrawingColor());
            p.setStrokeCap(Paint.Cap.ROUND);
            p.setStrokeWidth(progress);

            // Erase and redraw line on bitmap
            bitmap.eraseColor(getResources().getColor(android.R.color.transparent,
                    getContext().getTheme()));
            canvas.drawLine(30, 50, 370, 50, p);
            widthImageView.setImageBitmap(bitmap);
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
