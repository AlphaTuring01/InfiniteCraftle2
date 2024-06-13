package lab.prog.infinitecraftle;

import android.content.ClipData;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lab.prog.infinitecraftle.domain.Element;

public class ElementAdapter extends RecyclerView.Adapter<ElementAdapter.ElementViewHolder> {

    private List<Element> elementList;
    private View.OnTouchListener touchListener;

    public ElementAdapter(List<Element> elementList, View.OnTouchListener touchListener) {
        this.elementList = elementList;
        this.touchListener = touchListener;
    }

    @Override
    public ElementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_element, parent, false);
        return new ElementViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ElementViewHolder holder, int position) {
        Element element = elementList.get(position);
        String elementText = element.getEmoji() + " " + element.getName();
        holder.elementTextView.setText(elementText);

        int strokeWidth = 2;
        int strokeColor = ContextCompat.getColor(holder.itemView.getContext(), android.R.color.darker_gray);
        int cornerRadius = 20;
        GradientDrawable borderDrawable = new GradientDrawable();
        borderDrawable.setStroke(strokeWidth, strokeColor);
        borderDrawable.setColor(Color.WHITE);
        borderDrawable.setCornerRadius(cornerRadius);
        holder.elementTextView.setBackground(borderDrawable);

        holder.elementTextView.setOnTouchListener(touchListener);
    }

    @Override
    public int getItemCount() {
        return elementList.size();
    }

    public static class ElementViewHolder extends RecyclerView.ViewHolder {
        public TextView elementTextView;

        public ElementViewHolder(View view) {
            super(view);
            elementTextView = view.findViewById(R.id.elementTextView);
        }
    }
}
