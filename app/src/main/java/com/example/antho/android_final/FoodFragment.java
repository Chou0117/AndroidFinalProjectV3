package com.example.antho.android_final;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static com.example.antho.android_final.FoodActivity.mTwoPane;

public class FoodFragment extends Fragment {
    FoodActivity foodactivity = (FoodActivity) getActivity();
    Bundle arg;

    public FoodFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        arg = getArguments();
        View v = inflater.inflate(R.layout.activity_food_fragment, container, false);
        String d = "No Date";
        String n = "No Food Name";
        String c = "No Calories";
        String f = "No Fat";
        String h = "No Carbohydrate";
        if(arg != null) {
            d = arg.getString("DATE");
            n = arg.getString("NAME");
            c = arg.getString("CALORIES");
            f = arg.getString("FAT");
            h = arg.getString("CARBOHYDRATE");
        }
        TextView dateView = v.findViewById(R.id.fragFoodDate);
        dateView.setText(d);
        TextView nameView = v.findViewById(R.id.fragFoodName);
        nameView.setText(n);
        TextView calorieView = v.findViewById(R.id.fragFoodCalorie);
        calorieView.setText(c);
        TextView fatView = v.findViewById(R.id.fragFoodFat);
        fatView.setText(f);
        TextView carbohydrateView = v.findViewById(R.id.fragFoodCarbohydrate);
        carbohydrateView.setText(h);

        Button delete = v.findViewById(R.id.messageDeleteButton);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FoodActivity.class);
                getActivity().setResult(Integer.parseInt((arg.getString("_ID"))), intent);
                if(!mTwoPane)
                    getActivity().finish();
                else{
                    foodactivity.deleteItem();
                }
            }
        });

        return v;
    }
}
