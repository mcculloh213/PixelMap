package null_pointer.dalton.drawingtool;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

/**
 * Created by dalton on 1/23/17.
 */

public class EraseImageDialogFragment extends DialogFragment {

    private MainActivityFragment getMAFragment() {
        return (MainActivityFragment) getFragmentManager().findFragmentById(R.id.drawingFragment);
    }

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        // Initialize dialog
        AlertDialog.Builder build = new AlertDialog.Builder(getActivity());

        // Set dialog message
        build.setMessage(R.string.message_erase);

        // Add erase button
        build.setPositiveButton(R.string.button_erase, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                getMAFragment().getDrawingView().clear();
            }
        });

        // Add cancel button
        build.setNegativeButton(android.R.string.cancel, null);

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
}
