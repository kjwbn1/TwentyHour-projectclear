package com.kjw.twentyhour.view;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import com.kjw.twentyhour.R;
import com.kjw.twentyhour.adapter.FoodViewAdapter;
import com.kjw.twentyhour.adapter.SelectedFoodViewAdapter;
import com.kjw.twentyhour.customlayout.swipemenulistview.SwipeMenuListView;
import com.kjw.twentyhour.data.Food;
import com.kjw.twentyhour.fragment.CalculartorFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MenuSelectionView extends AppCompatActivity {


    private SwipeMenuListView mListView;


    List<Food> foodData;
    List<Food> selectedFoodData;
    Food food[] = new Food[5];
    int totalprice = 0 ;

    String selectedPrice;
    String selectedFoodName;
    int selectedPosition;
    ArrayList<Food> selectedFood;

    static int result = 0;

    Bundle bundle;

    CalculartorFragment calculartorFragment;



    SelectedFoodViewAdapter selectedFoodViewAdapter;

    ListView listView;

    FragmentTransaction fragmentTransaction;

    TextView totalPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_selection_view);


        listView = (ListView)findViewById((R.id.foodlist));
//        mListView = (SwipeMenuListView)findViewById(R.id.selected_Food_list);


//        calculartorFragment = new CalculartorFragment();



        foodData         = new ArrayList<>();

        totalPriceTextView = (TextView) findViewById(R.id.total_price_text_view);


        Food kimbob = new Food("김치1", R.drawable.kimbob, "8000" , "참치들어감");
        Food pizza1 = new Food("김치2", R.drawable.pizza , "8000" , "파인애플 들어감");
        Food pizza2 = new Food("김치3", R.drawable.pizza , "8000" , "파인애플 들어감");
        Food pizza3 = new Food("김치4", R.drawable.pizza , "8000" , "파인애플 들어감");
        Food pizza4 = new Food("김치5", R.drawable.pizza , "8000" , "파인애플 들어감");
        Food pizza5 = new Food("김치6", R.drawable.pizza , "8000" , "파인애플 들어감");

        foodData.add(kimbob);
        foodData.add(pizza1);
        foodData.add(pizza2);
        foodData.add(pizza3);
        foodData.add(pizza4);
        foodData.add(pizza5);

        selectedFood = new ArrayList<>();



        FoodViewAdapter foodViewAdapter = new FoodViewAdapter(getApplicationContext(), R.layout.not_selected_menu_item, foodData);


        listView.setAdapter(foodViewAdapter);





        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                TextView price = view.findViewById(R.id.price);
                TextView foodName = view.findViewById(R.id.food_name);
//



                Food food = new Food(foodName.getText().toString() , price.getText().toString());

                totalprice += Integer.parseInt(price.getText().toString());

                selectedFood.add(food);




                totalPriceTextView.setText("총합계: "+ String.valueOf(totalprice));

                bundle = new Bundle();
                bundle.putSerializable("selectedFood", (Serializable)selectedFood);


                calculartorFragment = new CalculartorFragment();

                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                calculartorFragment.setArguments(bundle);



                fragmentTransaction.replace(R.id.calculator , calculartorFragment);
                fragmentTransaction.commit();







//                foodViewAdapter.notifyDataSetChanged();








//                selectedFoodName = foodName.getText().toString();
//                selectedPrice = price.getText().toString();
//                selectedPosition = position;
//
//                selectedFoodData = new ArrayList<>();
//
//                food[selectedPosition] = new Food(selectedFoodName , selectedPrice);
//
//                selectedFoodData.add(food[selectedPosition]);
//
//
//                SelectedFoodViewAdapter selectedFoodViewAdapter = new SelectedFoodViewAdapter(getApplicationContext(), R.layout.selected_menu_item, selectedFoodData );
//
//                selectedFoodList.setAdapter(selectedFoodViewAdapter);
//
//                selectedFoodViewAdapter.notifyDataSetChanged();
















//                Toast.makeText(getApplicationContext() ,strprice ,Toast.LENGTH_LONG  ).show();
            }
        });



    }

//    @Override
//    public  boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_left) {
//            mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
//            return true;
//        }
//        if (id == R.id.action_right) {
//            mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }




//    public int add(ArrayList<Food> selectedFood) {
//        for (int i = 0; i < selectedFood.size(); i++) {
//
//            int results = Integer.parseInt(selectedFood.get(i).getPrice());
//
//            result +=results;
//
//        }
//        return result;
//    }





}
