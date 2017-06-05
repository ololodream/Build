package com.example.miya_.myapplication;
import java.util.ArrayList;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnFocusChangeListener;
/**
 * ListAdapterExercises
 * Description:
 *      List adapter for Exercises: customize reps and sets for exercises in different routines
 */
public class ListAdapterExercises extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<ExerciseItem> objects;

    ListAdapterExercises(Context context, ArrayList<ExerciseItem> products) {
        ctx = context;
        objects = products;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }


    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    ExerciseItem getProduct(int position) {
        return ((ExerciseItem) getItem(position));
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();

            view = lInflater.inflate(R.layout.item_pickexercise, parent, false);

            holder.EditText1 = (EditText) view
                    .findViewById(R.id.EditText_itemPickExercise4);

            holder.EditText2 = (EditText) view
                    .findViewById(R.id.EditText_itemPickExercise6);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        ExerciseItem p = getProduct(position);

        ((TextView) view.findViewById(R.id.textView_itemPickExercise2)).setText(p.Name);


        CheckBox cbBuy = (CheckBox) view.findViewById(R.id.CheckBox_itemPickExercise1);
        cbBuy.setOnCheckedChangeListener(myCheckChangList);
        cbBuy.setTag(position);
        cbBuy.setChecked(p.box);

        //holder.EditText1.setText(""+p.getSet());
        holder.EditText1.setId(position);


        //holder.EditText2.setText(""+p.getRept());
        holder.EditText2.setId(position);

        holder.EditText1.setOnFocusChangeListener(myEditText1);
        holder.EditText2.setOnFocusChangeListener(myEditText2);

        return view;
    }


    ArrayList<ExerciseItem> getBox() {
        ArrayList<ExerciseItem> box = new ArrayList<ExerciseItem>();
        for (ExerciseItem p : objects) {
            if (p.box)
                box.add(p);
        }
        return box;
    }

    OnCheckedChangeListener myCheckChangList = new OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            getProduct((Integer) buttonView.getTag()).setBox(isChecked);
        }
    };

    OnFocusChangeListener myEditText1 = new OnFocusChangeListener(){
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus){
                final int position = v.getId();
                final EditText Caption = (EditText) v;
                if(!Caption.getText().toString().isEmpty()) {
                    objects.get(position).setSet(Integer.parseInt(Caption.getText().toString()));
                    //Log.d("CJJAWL", ""+ objects.get(position).getSet());
                    Log.d("CJJAWL", "position = " + position);
                }
            }
        }
    };

    OnFocusChangeListener myEditText2 = new OnFocusChangeListener(){
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus){
                final int position = v.getId();
                final EditText Caption = (EditText) v;
                Log.d("CJJAWL","Caption = "+Caption.getText().toString());
                if(!Caption.getText().toString().isEmpty()) {
                    objects.get(position).setRept(Integer.parseInt(Caption.getText().toString()));

                    //Log.d("CJJAWL", ""+ objects.get(position).getRept());
                }
            }
        }
    };

    class ViewHolder {
        EditText EditText1;
        EditText EditText2;
    }


}
