package lab.prog.infinitecraftle;

import android.content.ClipData;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private FrameLayout craftingArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        craftingArea = findViewById(R.id.crafting_area);

        TextView elementWater = findViewById(R.id.element_water);
        TextView elementFire = findViewById(R.id.element_fire);
        TextView elementWind = findViewById(R.id.element_wind);
        TextView elementEarth = findViewById(R.id.element_earth);
        Button buttonReset = findViewById(R.id.button_reset);

        elementWater.setOnLongClickListener(longClickListener);
        elementFire.setOnLongClickListener(longClickListener);
        elementWind.setOnLongClickListener(longClickListener);
        elementEarth.setOnLongClickListener(longClickListener);

        craftingArea.setOnDragListener(dragListener);

        buttonReset.setOnClickListener(v -> resetCraftingArea());
    }

    private View.OnLongClickListener longClickListener = v -> {
        ClipData data = ClipData.newPlainText("", "");
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
        v.startDragAndDrop(data, shadowBuilder, v, 0);
        return true;
    };

    private View.OnDragListener dragListener = (v, event) -> {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
            case DragEvent.ACTION_DRAG_ENTERED:
            case DragEvent.ACTION_DRAG_EXITED:
            case DragEvent.ACTION_DRAG_ENDED:
                return true;
            case DragEvent.ACTION_DROP:
                View view = (View) event.getLocalState();

                if (view.getParent() == craftingArea) {
                    // If the view is already in the crafting area, move it
                    view.setX(event.getX() - (view.getWidth() / 2));
                    view.setY(event.getY() - (view.getHeight() / 2));
                } else {
                    // Clone the view being dragged
                    TextView clonedView = new TextView(this);
                    clonedView.setText(((TextView) view).getText());
                    clonedView.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
                    clonedView.setBackground(view.getBackground());

                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.leftMargin = (int) event.getX() - (view.getWidth() / 2);
                    layoutParams.topMargin = (int) event.getY() - (view.getHeight() / 2);

                    clonedView.setOnLongClickListener(longClickListener);
                    craftingArea.addView(clonedView, layoutParams);
                    clonedView.setVisibility(View.VISIBLE);
                }
                return true;
            default:
                break;
        }
        return true;
    };

    private void resetCraftingArea() {
        craftingArea.removeAllViews();
    }
}
