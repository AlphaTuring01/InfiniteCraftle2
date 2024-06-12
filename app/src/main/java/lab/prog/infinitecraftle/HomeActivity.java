package lab.prog.infinitecraftle;

import android.annotation.SuppressLint;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lab.prog.infinitecraftle.domain.Element;
import lab.prog.infinitecraftle.domain.Game;
import lab.prog.infinitecraftle.dto.CraftRequest;
import lab.prog.infinitecraftle.dto.LoginResponse;
import lab.prog.infinitecraftle.viewmodel.CraftViewModel;
import lab.prog.infinitecraftle.viewmodel.LoginViewModel;

public class HomeActivity extends AppCompatActivity {
    private int newViewIndex = 10;
    private int newElementViewId;
    private CraftViewModel craftViewModel;
    private FrameLayout craftingArea;

    private ImageView bin;
    private FrameLayout rootLayout;
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

        craftViewModel = new ViewModelProvider(this).get(CraftViewModel.class);
        craftingArea = findViewById(R.id.crafting_area);
        elementsLayout = findViewById(R.id.elements_layout);
        TextView wordView = findViewById(R.id.wordView);
        bin = findViewById(R.id.bin);
        bin.setId(newViewIndex);
        newViewIndex++;
        TextView dateView = findViewById(R.id.wordViewDate);
        craftingArea.setOnDragListener(dragListener);
        LoginResponse loginResponse = (LoginResponse) getIntent().getSerializableExtra("GAME_DATA");
        wordView.setText(loginResponse.getElementDay().getName());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
        dateView.setText(formatter.format(new Date()));
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

        // Verifica se o elemento já está presente
        for (int i = 0; i < elementsLayout.getChildCount(); i++) {
            View child = elementsLayout.getChildAt(i);
            if (child instanceof TextView) {
                TextView existingElement = (TextView) child;
                if (existingElement.getText().toString().equals(elementText)) {
                    return; // Elemento já está presente, não adiciona novamente
                }
            }
        }

        // Cria um novo TextView para o elemento
        TextView element = new TextView(this);
        element.setText(elementText);
        element.setPadding(20, 20, 20, 20);
        element.setTextSize(18);
        element.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        element.setGravity(View.TEXT_ALIGNMENT_CENTER);

        // Define o contorno (borda) arredondada
        int strokeWidth = 3; // Largura da borda em pixels
        int strokeColor = ContextCompat.getColor(this, android.R.color.black); // Cor da borda
        int cornerRadius = 20; // Raio dos cantos em pixels
        GradientDrawable borderDrawable = new GradientDrawable();
        borderDrawable.setStroke(strokeWidth, strokeColor);
        borderDrawable.setColor(Color.WHITE); // Cor de fundo
        borderDrawable.setCornerRadius(cornerRadius); // Raio dos cantos
        element.setBackground(borderDrawable);

        // Adiciona o TextView ao LinearLayout
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMarginStart(16);
        layoutParams.setMarginEnd(16);
        element.setLayoutParams(layoutParams);

        element.setOnTouchListener(touchListener);
        element.setId(newViewIndex);
        newViewIndex++;
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
                    clonedTextView.setPadding(20, 20, 20, 20);// Increase text size
                    int strokeWidth = 3; // Largura da borda em pixels
                    int strokeColor = ContextCompat.getColor(this, android.R.color.black); // Cor da borda
                    int cornerRadius = 20; // Raio dos cantos em pixels
                    GradientDrawable borderDrawable = new GradientDrawable();
                    borderDrawable.setStroke(strokeWidth, strokeColor);
                    borderDrawable.setColor(Color.WHITE); // Cor de fundo
                    borderDrawable.setCornerRadius(cornerRadius); // Raio dos cantos
                    clonedTextView.setBackground(borderDrawable);

                    //clonedTextView.setBackground(view.getBackground());

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

    private boolean isOff(View mainView) {
        Rect mainRect = new Rect();
        mainView.getGlobalVisibleRect(mainRect);

        Rect craftingAreaRect = new Rect();
        craftingArea.getGlobalVisibleRect(craftingAreaRect);

        // Verifica se algum canto da mainView está fora do craftingArea
        return mainRect.left < craftingAreaRect.left ||
                mainRect.top < craftingAreaRect.top ||
                mainRect.right > craftingAreaRect.right ||
                mainRect.bottom > craftingAreaRect.bottom;
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
                return true; // Return true if the rectangles overlap
            }
        }
        return false; // Return false if no overlap is found
    }

    private void craftView(View view1, View view2) {
        //craftingArea.removeView(view1);
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
        for(int i = craftingArea.getChildCount() - 1; i > 0; i--) {
            craftingArea.removeViewAt(i);
        }
    }
}
