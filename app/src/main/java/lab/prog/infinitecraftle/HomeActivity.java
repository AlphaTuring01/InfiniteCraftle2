package lab.prog.infinitecraftle;

import android.content.ClipData;
import android.graphics.Rect;
import android.content.Intent;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import lab.prog.infinitecraftle.domain.Element;
import lab.prog.infinitecraftle.domain.Game;

public class HomeActivity extends AppCompatActivity {

    private FrameLayout craftingArea;
    private LinearLayout elementsLayout;
    //atributos do jogo atual
    private Game game;

    /*public HomeActivity(Game game){
        this.game = game;
    }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        craftingArea = findViewById(R.id.crafting_area);
        elementsLayout = findViewById(R.id.elements_layout);

        craftingArea.setOnDragListener(dragListener);

        ArrayList <Element> elements = new ArrayList<>();
        elements.add(new Element("💧","Water"));
        elements.add(new Element("🔥","Fire"));
        elements.add(new Element("💨","Wind"));
        elements.add(new Element("🌍","Earth"));
        game = new Game(elements);
        AddAllElements();
        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setText("Limpar"); // Rename the reset button
        buttonReset.setOnClickListener(v -> resetCraftingArea());

        ImageButton buttonLogout = findViewById(R.id.button_logout);
        buttonLogout.setOnClickListener(view -> logout());
    }
    private void moveToLoginActivity() {
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    private void logout() {
        SharedPreferencesHandler preferencesHandler = new SharedPreferencesHandler();
        preferencesHandler.clearUserData(this);
        moveToLoginActivity();
    }

    protected void AddAllElements(){
        for(Element e : game.getElements()){
            addElement(e.getEmoji(), e.getName());
        }
    }
    private void addElement(String emoji, String name) {
        TextView element = new TextView(this);
        element.setText(emoji + " " + name);
        element.setPadding(20, 20, 20, 20);
        element.setBackground(ContextCompat.getDrawable(this, R.drawable.element_background));
        element.setTextSize(18);
        element.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        element.setGravity(View.TEXT_ALIGNMENT_CENTER);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMarginStart(16);
        params.setMarginEnd(16);
        element.setLayoutParams(params);
        element.setOnTouchListener(touchListener);
        elementsLayout.addView(element);
    }

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDragAndDrop(data, shadowBuilder, v, 0);

                // Make the view invisible if it is being dragged from the crafting area
                if (v.getParent() == craftingArea) {
                    v.setVisibility(View.INVISIBLE);
                }
                return true;
            }
            return false;
        }
    };

    private View.OnDragListener dragListener = (v, event) -> {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                return true;
            case DragEvent.ACTION_DRAG_ENTERED:
                return true;
            case DragEvent.ACTION_DRAG_EXITED:
                return true;
            case DragEvent.ACTION_DRAG_ENDED:
                View draggedView = (View) event.getLocalState();
                if (draggedView.getParent() == craftingArea) {
                    draggedView.setVisibility(View.VISIBLE); // Make the view visible again if it was in the crafting area
                }
                return true;
            case DragEvent.ACTION_DROP:
                View view = (View) event.getLocalState();

                if (view.getParent() == craftingArea) {
                    // If the view is already in the crafting area, move it
                    view.setX(event.getX() - ((float) view.getWidth() / 2));
                    view.setY(event.getY() - ((float) view.getHeight() / 2));
                    isCloseToOtherView(view);
                } else {
                    // Clone the view being dragged
                    TextView clonedView = new TextView(this);
                    clonedView.setText(((TextView) view).getText());
                    clonedView.setPadding(30, 30, 30, 30); // Increase padding for larger size
                    clonedView.setTextSize(24); // Increase text size
                    clonedView.setBackground(view.getBackground());

                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.leftMargin = (int) event.getX() - (view.getWidth() / 2);
                    layoutParams.topMargin = (int) event.getY() - (view.getHeight() / 2);

                    clonedView.setOnTouchListener(touchListener);
                    craftingArea.addView(clonedView, layoutParams);
                    clonedView.setVisibility(View.VISIBLE);
                    isCloseToOtherView((View)clonedView);
                }
                return true;
            default:
                break;
        }
        return true;
    };

    private boolean isCloseToOtherView(View view) {
        // Loop through all child views of the crafting area
        for (int i = 0; i < craftingArea.getChildCount(); i++) {
            View child = craftingArea.getChildAt(i);
            // Check if the child view is the same as the dragged view
            if (child != view) {
                // Calculate the bounds (rectangles) of the dragged view and the child view
                Rect rectDragged = new Rect((int) view.getX(), (int) view.getY(), (int) (view.getX() + view.getWidth()), (int) (view.getY() + view.getHeight()));
                Rect rectChild = new Rect((int) child.getX(), (int) child.getY(), (int) (child.getX() + child.getWidth()), (int) (child.getY() + child.getHeight()));
                // Check if the rectangles overlap (collision detection)
                if (rectDragged.intersect(rectChild)) {
                    deleteAndCreateNewViews(view, child);
                    return true; // Return true if the rectangles overlap
                }
            }
        }
        return false; // Return false if no overlap is found
    }

    private void deleteAndCreateNewViews(View view1, View view2) {
        // Remove as duas views do craftingArea
        craftingArea.removeView(view1);
        craftingArea.removeView(view2);

        // Crie uma nova view para substituir as duas views excluídas
        TextView newView = new TextView(this);
        newView.setText("Nova View");
        newView.setPadding(20, 20, 20, 20);
        newView.setBackground(ContextCompat.getDrawable(this, R.drawable.element_background));
        newView.setTextSize(18);
        newView.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        newView.setGravity(View.TEXT_ALIGNMENT_CENTER);

        // Adicione a nova view à craftingArea
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        craftingArea.addView(newView, params);
    }


    private void resetCraftingArea() {
        craftingArea.removeAllViews();
    }
}
