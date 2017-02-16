package null_pointer.dalton.drawingtool;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private ActionBar mActionBar;
    private boolean menuHidden = false;
    private CoordinatorLayout.LayoutParams mParams;
    private FloatingActionButton mMenuButton;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Initialize Main Activity using activity_main.xml layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inject camera layout into activity_main.xml
        if (null == savedInstanceState) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, CameraPreview.newInstance())
                    .commit();
        }

        // Set Toolbar and Action Bar variables
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();

        // Set FAB variable and set listener
        mMenuButton = (FloatingActionButton) findViewById(R.id.fab);
        mMenuButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Get layout params of current FAB
                if (menuHidden) {
                    showActionBar();
                } else {
                    hideActionBar();
                }

                menuHidden = !menuHidden;
            }

        });

        // Set FAB LayoutParams
        mParams = (CoordinatorLayout.LayoutParams) mMenuButton.getLayoutParams();

    }

    protected void hideActionBar() {
        if (mActionBar != null) {
            if (mToolbar != null) {
                mToolbar.animate()
                        .translationX(-1500)
                        .setDuration(600L)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                mActionBar.hide();
                                mParams.setMargins((int) getResources().getDimension(R.dimen.fab_margin),
                                        (int) getResources().getDimension(R.dimen.fab_margin),
                                        (int) getResources().getDimension(R.dimen.fab_margin),
                                        (int) getResources().getDimension(R.dimen.fab_margin));
                            }
                        })
                        .start();
            }
            else {
                mActionBar.hide();
            }
        }
    }

    protected void showActionBar() {
        if (mActionBar != null) {
            mActionBar.show();
            if (mToolbar != null) {
                mParams.setMargins((int) getResources().getDimension(R.dimen.fab_margin),
                        (int) getResources().getDimension(R.dimen.fab_margin),
                        (int) getResources().getDimension(R.dimen.fab_margin),
                        (int) getResources().getDimension(R.dimen.fab_high_margin));
                mToolbar.animate()
                        .translationX(0)
                        .setDuration(600L)
                        .start();
            }
        }
    }

}
