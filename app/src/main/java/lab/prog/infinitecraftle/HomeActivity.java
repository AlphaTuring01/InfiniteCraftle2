package lab.prog.infinitecraftle;

import android.content.ClipData;
import android.graphics.Color;
import android.graphics.Rect;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import lab.prog.infinitecraftle.domain.Element;
import lab.prog.infinitecraftle.domain.Game;
import lab.prog.infinitecraftle.domain.User;
import lab.prog.infinitecraftle.dto.CraftRequest;
import lab.prog.infinitecraftle.dto.CraftResponse;
import lab.prog.infinitecraftle.dto.LoginResponse;
import lab.prog.infinitecraftle.viewmodel.ChangeDateViewModel;
import lab.prog.infinitecraftle.viewmodel.CraftViewModel;

public class HomeActivity extends AppCompatActivity {
    private int newViewIndex = 10;
    private static final int WIN_ACTIVITY_REQUEST_CODE = 1;
    private int newElementViewId;
    private CraftViewModel craftViewModel;
    private ChangeDateViewModel dateChanger;

    private FrameLayout craftingArea;
    private ImageView bin;
    private RecyclerView elementsRecyclerView;
    private ElementAdapter elementAdapter;
    private float initialX, initialY;
    private boolean isHorizontalDrag = false;
    private boolean isVerticalDrag = false;

    // Attributes for the current game
    private Game game;
    private List<Element> elementList;
    private ArrayList<String> dateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        craftViewModel = new ViewModelProvider(this).get(CraftViewModel.class);
        dateChanger = new ViewModelProvider(this).get(ChangeDateViewModel.class);
        craftingArea = findViewById(R.id.crafting_area);
        elementsRecyclerView = findViewById(R.id.elements_recycler_view);
        TextView wordView = findViewById(R.id.wordView);
        bin = findViewById(R.id.bin);
        TextView dateView = findViewById(R.id.wordViewDate);

        craftingArea.setOnDragListener(dragListener);
        LoginResponse loginResponse = (LoginResponse) getIntent().getSerializableExtra("GAME_DATA");
        game = loginResponse.getGame();
        if(game != null){
            wordView.setText(loginResponse.getGame().getTargetElement().getName());
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
            dateView.setText(formatter.format(game.getDate()));
            if(game.isWin()) ((ImageView) findViewById(R.id.dayWon)).setVisibility(View.VISIBLE);
            else ((ImageView) findViewById(R.id.dayWon)).setVisibility(View.INVISIBLE);;
        }
        dateList = loginResponse.getListDates();
        elementList = game.getElements();

        // Set up the RecyclerView with a LinearLayoutManager for horizontal scrolling
        elementsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        elementAdapter = new ElementAdapter(elementList, touchListener);
        elementsRecyclerView.setAdapter(elementAdapter);

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
                addElementToRecyclerView(element);
                break;
            }
            if(craftResponse.getGame().isWin() && !game.isWin()){
                moveToWinActivity(craftResponse);
            }
        });
        craftViewModel.getErrorLiveData().observe(this, error -> {
        });

        dateChanger.getChangeDateResponseLiveData().observe(this, changeDataResponse -> {
            game = changeDataResponse.getGame();
            if(game != null){
                wordView.setText(game.getTargetElement().getName());
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
                dateView.setText(formatter.format(game.getDate()));
                elementList = game.getElements();
                elementAdapter.notifyDataSetChanged();
                if(game.isWin()) ((ImageView) findViewById(R.id.dayWon)).setVisibility(View.VISIBLE);
                else ((ImageView) findViewById(R.id.dayWon)).setVisibility(View.INVISIBLE);
            }
        });
        dateChanger.getErrorLiveData().observe(this, error -> {
        });
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDropdown(dateView);
            }
        });
    }

    private void moveToWinActivity(CraftResponse response) {
        Intent intent = new Intent(HomeActivity.this, WinActivity.class);
        intent.putExtra("SCORE", response.getGame().getScore());
        intent.putExtra("TIME", formatDuration(response.getGame().getTimeMillis()));
        intent.putExtra("RANKING", response.getRanking());
        startActivity(intent);
        finish();
    }

    public static String formatDuration(long millis) {
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60;
        long milliseconds = millis % 1000;

        return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, milliseconds);
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

    private void addElementToRecyclerView(Element element) {
        for (Element existingElement : elementList) {
            if (existingElement.getName().equals(element.getName())) {
                return; // Element already exists, do not add again
            }
        }
        elementList.add(element);
        elementAdapter.notifyDataSetChanged();
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
                    if (v.getParent() == craftingArea) {
                        ClipData data = ClipData.newPlainText("", "");
                        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                        v.startDragAndDrop(data, shadowBuilder, v, 0);
                        v.setVisibility(View.INVISIBLE);
                        return true;
                    }
                    else if (isHorizontalDrag) {
                        elementsRecyclerView.requestDisallowInterceptTouchEvent(false);
                        return false;
                    } else if (isVerticalDrag) {
                        elementsRecyclerView.requestDisallowInterceptTouchEvent(true);
                        ClipData data = ClipData.newPlainText("", "");
                        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                        v.startDragAndDrop(data, shadowBuilder, v, 0);
                        return true;
                    }

                case MotionEvent.ACTION_UP:
                    elementsRecyclerView.requestDisallowInterceptTouchEvent(false);
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
                if (isInBin(draggedView)){
                    craftingArea.removeView(draggedView);
                    return true;
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
                    clonedTextView.setTextSize(18);
                    clonedTextView.setPadding(20, 20, 20, 20);
                    int strokeWidth = 2;
                    int strokeColor = ContextCompat.getColor(this, android.R.color.darker_gray);
                    int cornerRadius = 20;
                    GradientDrawable borderDrawable = new GradientDrawable();
                    borderDrawable.setStroke(strokeWidth, strokeColor);
                    borderDrawable.setColor(Color.WHITE);
                    borderDrawable.setCornerRadius(cornerRadius);
                    clonedTextView.setBackground(borderDrawable);
                    clonedTextView.setTextColor(Color.BLACK);

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

                    String text = ((TextView) view).getText().toString();
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

    private boolean isInBin(View mainView) {
        Rect mainRect = new Rect();
        mainView.getGlobalVisibleRect(mainRect);

        Rect binRect = new Rect();
        bin.getGlobalVisibleRect(binRect);

        return mainRect.intersect(binRect);
    }

    private boolean isCloseToOtherView(TextView view) {
        for(int i = craftingArea.getChildCount() - 1; i > 0; i--) {
            View child = craftingArea.getChildAt(i);
            if(!(child instanceof TextView)) continue;
            int a = child.getId();
            int b = view.getId();
            if (child == view) continue;
            Rect rectDragged = new Rect((int) view.getX(), (int) view.getY(), (int) (view.getX() + child.getWidth()), (int) (view.getY() + child.getHeight()));
            Rect rectChild = new Rect((int) child.getX(), (int) child.getY(), (int) (child.getX() + child.getWidth()), (int) (child.getY() + child.getHeight()));
            if (Rect.intersects(rectDragged, rectChild)) {
                craftView(view, child);
                String view1Text = ((TextView)view).getText().toString();
                String view2Text = ((TextView)child).getText().toString();
                craftNewElement(view1Text, view2Text);
                return true;
            }
        }
        return false;
    }

    private void craftView(View view1, View view2) {
        craftingArea.removeView(view1);
        newElementViewId = view2.getId();
        view2.setX((view1.getX() + view2.getX())/2);
        view2.setY((view1.getY() + view2.getY())/2);
    }

    void craftNewElement(String parent1, String parent2){
        CraftRequest request = new CraftRequest(removeEmoji(parent1), removeEmoji(parent2));
        SharedPreferencesHandler preferencesHandler = new SharedPreferencesHandler();
        String date = preferencesHandler.getGameDate(this);
        request.setGameDate(date);
        request.setUserId(preferencesHandler.getUser(this).getId());
        craftViewModel.craftElement(request);
    }

    public static String removeEmoji(String input) {
        return input.trim().split("\\s+")[1];
    }

    private void resetCraftingArea() {
        for(int i = craftingArea.getChildCount() - 1; i > 0; i--) {
            if(craftingArea.getChildAt(i).getId() != R.id.bin) craftingArea.removeViewAt(i);
        }
    }

    private void showDateDropdown(TextView dateView) {
        SharedPreferencesHandler handler = new SharedPreferencesHandler();
        User user = handler.getUser(this);
        PopupMenu popupMenu = new PopupMenu(this, dateView);
        for (String date : dateList) popupMenu.getMenu().add(date);

        popupMenu.setOnMenuItemClickListener(item -> {
            dateChanger.changeDate(user.getId(), item.getTitle().toString());
            SharedPreferencesHandler preferencesHandler = new SharedPreferencesHandler();
            preferencesHandler.setGameDate(this, item.getTitle().toString());
            resetCraftingArea();
            elementList.clear();
            elementList = game.getElements();
            elementAdapter.notifyDataSetChanged();
            return true;
        });

        popupMenu.show();
    }
}
