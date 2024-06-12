package lab.prog.infinitecraftle;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

import lab.prog.infinitecraftle.domain.Element;
import lab.prog.infinitecraftle.domain.Game;
import lab.prog.infinitecraftle.dto.CraftRequest;
import lab.prog.infinitecraftle.dto.LoginResponse;
import lab.prog.infinitecraftle.viewmodel.CraftViewModel;

public class HomeActivity extends AppCompatActivity {
    private static final int WIN_ACTIVITY_REQUEST_CODE = 1;

    private int newViewIndex = 0;
    private int newElementViewId;
    private CraftViewModel craftViewModel;
    private FrameLayout craftingArea;
    private LinearLayout elementsLayout;
    private HorizontalScrollView elementsScrollView;
    private float initialX, initialY;
    private boolean isHorizontalDrag = false;
    private boolean isVerticalDrag = false;

    // Attributes for the current game
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        craftViewModel = new ViewModelProvider(this).get(CraftViewModel.class);
        craftingArea = findViewById(R.id.crafting_area);
        elementsLayout = findViewById(R.id.elements_layout);
        elementsScrollView = findViewById(R.id.elements_scroll_view);

        craftingArea.setOnDragListener(dragListener);
        LoginResponse loginResponse = (LoginResponse) getIntent().getSerializableExtra("GAME_DATA");
        game = loginResponse.getGame();
        AddAllElements();

        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setText("Limpar"); // Rename the reset button
        buttonReset.setOnClickListener(v -> resetCraftingArea());

        ImageButton buttonLogout = findViewById(R.id.button_logout);
        buttonLogout.setOnClickListener(view -> logout());

        craftViewModel.getCraftResponseLiveData().observe(this, craftResponse -> {
            if(!craftResponse.getError().isEmpty()){
                return;
            }
            Element element = craftResponse.getElement();
            for(int i=0;i<craftingArea.getChildCount();i++){
                View child = craftingArea.getChildAt(i);
                if(child.getId() != newElementViewId) continue;
                if(!(child instanceof TextView)) continue;
                String name = element.getName();
                String emoji = element.getEmoji();
                ((TextView)child).setText(emoji.concat(' ' + name));
                addElement(emoji, name);
                break;
            }
        });
        craftViewModel.getErrorLiveData().observe(this, error -> {
        });
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
        String elementText = emoji + " " + name;

        for (int i = 0; i < elementsLayout.getChildCount(); i++) {
            View child = elementsLayout.getChildAt(i);
            if (child instanceof TextView) {
                TextView existingElement = (TextView) child;
                if (existingElement.getText().toString().equals(elementText)) {
                    return; // Element is already present, so do not add it again
                }
            }
        }

        TextView element = new TextView(this);
        element.setText(elementText);
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
        element.setId(newViewIndex);
        newViewIndex++;
        elementsLayout.addView(element);
    }

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    initialX = event.getX();
                    initialY = event.getY();
                    isHorizontalDrag = false;
                    isVerticalDrag = false;
                    return true;

                case MotionEvent.ACTION_MOVE:
                    float deltaX = event.getX() - initialX;
                    float deltaY = event.getY() - initialY;

                    if (!isHorizontalDrag && !isVerticalDrag) {
                        if (Math.abs(deltaX) > Math.abs(deltaY)) {
                            isHorizontalDrag = true;
                        } else {
                            isVerticalDrag = true;
                        }
                    }

                    if (isHorizontalDrag) {
                        elementsScrollView.requestDisallowInterceptTouchEvent(false);
                        return false;
                    } else if (isVerticalDrag) {
                        elementsScrollView.requestDisallowInterceptTouchEvent(true);
                        ClipData data = ClipData.newPlainText("", "");
                        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                        v.startDragAndDrop(data, shadowBuilder, v, 0);
                        return true;
                    }

                case MotionEvent.ACTION_UP:
                    elementsScrollView.requestDisallowInterceptTouchEvent(false);
                    return true;
            }
            return false;
        }
    };

    private View.OnDragListener dragListener = (v, event) -> {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
            case DragEvent.ACTION_DRAG_ENTERED:
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
                    isCloseToOtherView((TextView) view);
                } else {
                    // Clone the view being dragged
                    TextView clonedTextView = new TextView(this);
                    clonedTextView.setText(((TextView) view).getText());
                    clonedTextView.setPadding(30, 30, 30, 30); // Increase padding for larger size
                    clonedTextView.setTextSize(24); // Increase text size
                    clonedTextView.setBackground(view.getBackground());

                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.leftMargin = (int) event.getX() - (view.getWidth() / 2);
                    layoutParams.topMargin = (int) event.getY() - (view.getHeight() / 2);

                    clonedTextView.setOnTouchListener(touchListener);
                    craftingArea.addView(clonedTextView, layoutParams);
                    clonedTextView.setVisibility(View.VISIBLE);
                    clonedTextView.setId(newViewIndex);
                    newViewIndex++;
                    TextView clonedViewCopy = new TextView(this);
                    clonedViewCopy.setX(event.getX() - (float) view.getWidth() / 2);
                    clonedViewCopy.setY(event.getY() - (float) view.getHeight() / 2);

                    TextView thisViewText = (TextView) view;
                    String text = thisViewText.getText().toString();
                    clonedViewCopy.setText(text);
                    clonedViewCopy.setId(clonedTextView.getId());

                    if(isCloseToOtherView(clonedViewCopy)){
                        craftingArea.removeView(clonedTextView);
                    }
                }
                return true;
            default:
                break;
        }
        return true;
    };

    private boolean isCloseToOtherView(TextView view) {
        for (int i = 0; i < craftingArea.getChildCount(); i++) {
            View child = craftingArea.getChildAt(i);
            if(!(child instanceof TextView)) continue;
            int a = child.getId();
            int b = view.getId();
            if (child == view) continue;
            Rect rectDragged = new Rect((int) view.getX(), (int) view.getY(), (int) (view.getX() + child.getWidth()), (int) (view.getY() + child.getHeight()));
            Rect rectChild = new Rect((int) child.getX(), (int) child.getY(), (int) (child.getX() + child.getWidth()), (int) (child.getY() + child.getHeight()));
            if (Rect.intersects(rectDragged, rectChild)) {
                deleteAndCreateNewViews(view, child);
                String view1Text = ((TextView)view).getText().toString();
                String view2Text = ((TextView)child).getText().toString();
                craftNewElement(view1Text, view2Text);
                return true; // Return true if the rectangles overlap
            }
        }
        return false; // Return false if no overlap is found
    }

    private void deleteAndCreateNewViews(View view1, View view2) {
        craftingArea.removeView(view1);

        newElementViewId = view2.getId();
        view2.setX((view1.getX() + view2.getX())/2);
        view2.setY((view1.getY() + view2.getY())/2);
    }

    void craftNewElement(String parent1, String parent2){
        CraftRequest request = new CraftRequest(removeEmoji(parent1), removeEmoji(parent2));
        SharedPreferencesHandler preferencesHandler = new SharedPreferencesHandler();
        request.setGameDate(preferencesHandler.getGameDate(this));
        request.setUserId(preferencesHandler.getUser(this).getId());
        craftViewModel.craftElement(request);
    }

    public static String removeEmoji(String input) {
        return input.trim().split("\\s+")[1];
    }

    private void resetCraftingArea() {
        craftingArea.removeAllViews();
    }

    // Add this method to start the WinActivity
    private void startWinActivity(int score, String time) {
        Intent intent = new Intent(HomeActivity.this, WinActivity.class);
        intent.putExtra("SCORE", score);
        intent.putExtra("TIME", time);
        startActivityForResult(intent, WIN_ACTIVITY_REQUEST_CODE);
    }

    // Handle the result from WinActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WIN_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            // Handle returning to the game state
        }
    }

    // Call this method when the user wins
    private void checkForWin() {
        // Your logic to check for win condition
        boolean hasWon = true; // Replace this with your actual condition

        if (hasWon) {
            int score = 100; // Replace this with actual score calculation
            String time = "00:10:00"; // Replace this with actual time calculation
            startWinActivity(score, time);
        }
    }
}
