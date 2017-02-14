package null_pointer.dalton.drawingtool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dalton on 1/23/17.
 */

public class DrawingView extends View {

    private static final float TOUCH_TOLERANCE = 10;

    private Bitmap bitmap; // Drawing area
    private Canvas bitmapCanvas; // Draw on bitmap
    private final Paint paintScreen; // Draw bitmap onto screen
    private final Paint paintLine; // Draw lines on the bitmap

    // Current paths begin drawn and points in those path
    // TODO: Look into SparseArray -- Better performance?
    private final Map<Integer, Path> pathMap = new HashMap<>();
    private final Map<Integer, Point> previousPointMap = new HashMap<>();

    // CONSTRUCTOR
    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paintScreen = new Paint(); // Initialize bitmap

        paintScreen.setAlpha(255); // Completely transparent

        /**
         * Default settings:
         *     Anti Alias: true
         *     Colour: Black
         *     Style: Stroke
         *     Stroke Width: 5
         *     Stroke Cap: Round
         */
        paintLine = new Paint(); // Initialize drawing tool
        paintLine.setAntiAlias(true);
        paintLine.setColor(Color.BLACK);
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setStrokeWidth(5);
        paintLine.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);
        bitmap.eraseColor(Color.TRANSPARENT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO: Use a picture as the background bitmap
        canvas.drawBitmap(bitmap, 0, 0, paintScreen); // Draw background

        // For All paths being drawn, draw the line
        for (Integer k : pathMap.keySet()) {
            canvas.drawPath(pathMap.get(k), paintLine);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        // Get event type
        int action = evt.getActionMasked();
        int actionIndex = evt.getActionIndex();

        // Event start, moving, or ended?
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                touchStarted(evt.getX(actionIndex), evt.getY(actionIndex),
                        evt.getPointerId(actionIndex));
                invalidate(); // Redraw
                return true;
            case MotionEvent.ACTION_POINTER_DOWN:
                touchStarted(evt.getX(actionIndex), evt.getY(actionIndex),
                        evt.getPointerId(actionIndex));
                invalidate(); // Redraw
                return true;
            case MotionEvent.ACTION_UP:
                touchEnded(evt.getPointerId(actionIndex));
                invalidate(); // Redraw
                return true;
            case MotionEvent.ACTION_POINTER_UP:
                touchEnded(evt.getPointerId(actionIndex));
                invalidate(); // Redraw
                return true;
            default:
                touchMoved(evt);
                invalidate(); // Redraw
                return true;
        }
    }

    private void touchStarted(float x, float y, int lineId) {
        Path path; // Store path
        Point pt; // Store latest point in path

        if (pathMap.containsKey(lineId)) {
            // pathMap contains a path for the given lineId
            path = pathMap.get(lineId);
            path.reset(); // Reset -- new touch has started
            pt = previousPointMap.get(lineId); // Pull path's last point
        } else {
            path = new Path(); // New path
            pathMap.put(lineId, path); // Add new path to pathMap
            pt = new Point(); // New point
            previousPointMap.put(lineId, pt); // Add new point to previousPointMap
        }

        // Move to coordinates of touch and store points
        path.moveTo(x, y);
        pt.x = (int) x;
        pt.y = (int) y;
    }

    private void touchMoved(MotionEvent evt) {
        // For All pointers in evt, get pointer Id and index
        for (int i = 0; i < evt.getPointerCount(); i ++) {
            int ptrId = evt.getPointerId(i);
            int ptrIndex = evt.findPointerIndex(ptrId);

            // If there exists a path in pathMap with key ptrId
            if (pathMap.containsKey(ptrId)) {
                // Pull new coordinates for ptr
                float nX = evt.getX(ptrIndex);
                float nY = evt.getY(ptrIndex);

                // Pull previous path and point associated with ptrId
                Path path = pathMap.get(ptrId);
                Point pt = previousPointMap.get(ptrId);

                // Calculate coordinate deltas
                float dX = Math.abs(nX - pt.x);
                float dY = Math.abs(nY - pt.y);

                if (dX >= TOUCH_TOLERANCE || dY >= TOUCH_TOLERANCE) {
                    // Delta is significant -- Move path to new location
                    path.quadTo(pt.x, pt.y, (nX + pt.x) / 2, (nY + pt.y) / 2);

                    // Store new points
                    pt.x = (int) nX;
                    pt.y = (int) nY;
                }
            }
        }
    }

    private void touchEnded(int lineId) {
        Path path = pathMap.get(lineId); // Pull path
        bitmapCanvas.drawPath(path, paintLine); // Draw path on canvas
        path.reset();
    }

    public void saveImage() {
        final String name = "img_" + System.currentTimeMillis() + ".jpg";

        // Save image on device
        String loc = MediaStore.Images.Media.insertImage(getContext().getContentResolver(),
                bitmap, name, "DrawTest");

        if (loc != null) {
            // Message saved
            Toast msg = Toast.makeText(getContext(), R.string.message_saved, Toast.LENGTH_SHORT);
            msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
            msg.show();
        }
    }

    public void clear() {
        pathMap.clear(); // Empty paths
        previousPointMap.clear(); // Empty previous points
        bitmap.eraseColor(Color.WHITE);
        invalidate(); // Refresh
    }

    public void setDrawingColour(int colour) {
        paintLine.setColor(colour);
    }

    public int getDrawingColour() {
        return paintLine.getColor();
    }

    public void setLineWidth(int width) {
        paintLine.setStrokeWidth(width);
    }

    public int getLineWidth() {
        return (int) paintLine.getStrokeWidth();
    }
}
