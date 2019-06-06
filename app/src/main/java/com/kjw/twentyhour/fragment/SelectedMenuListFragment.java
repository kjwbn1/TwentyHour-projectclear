package com.kjw.twentyhour.fragment;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kjw.twentyhour.R;
import com.kjw.twentyhour.adapter.SelectedFoodViewAdapter;
import com.kjw.twentyhour.customlayout.swipemenulistview.SwipeMenu;
import com.kjw.twentyhour.customlayout.swipemenulistview.SwipeMenuCreator;
import com.kjw.twentyhour.customlayout.swipemenulistview.SwipeMenuItem;
import com.kjw.twentyhour.customlayout.swipemenulistview.SwipeMenuListView;
import com.kjw.twentyhour.data.Food;
import com.kjw.twentyhour.model.OrderSheet;
import com.kjw.twentyhour.model.Product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectedMenuListFragment extends Fragment {


    Bundle savedState;
//    OrderSheet orderSheet;
    private List<Product> selectedMenuData;
    public SelectedFoodViewAdapter mAdapter;
    private SwipeMenuListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calculartor, container, false);

        if (savedInstanceState != null && savedState == null) {
            savedState = savedInstanceState.getBundle("selectedMenuData");


            selectedMenuData = (ArrayList<Product>) savedState.getSerializable("selectedMenuData");

        }
        if (savedState != null) {

            selectedMenuData = (ArrayList<Product>) savedState.getSerializable("selectedMenuData");

        }



        selectedMenuData = (ArrayList<Product>) getArguments().getSerializable("selectedMenuData");

//        orderSheet = new OrderSheet();
//
//        if(selectedMenuData != null) {
//            for (int i = 0; selectedMenuData.size() > i; i++) {
//                orderSheet.setProducts(selectedMenuData.get(i));
//
//            }
//        }

//        orderSheet.setTotalPrice();
        mAdapter = new SelectedFoodViewAdapter(getContext(), R.layout.selected_menu_item, selectedMenuData);
        mListView = view.findViewById(R.id.selected_Food_list);
        mListView.setAdapter(mAdapter);

        SwipeMenuCreator creator = menu -> {
            SwipeMenuItem openItem = new SwipeMenuItem(getContext());

            openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));

            openItem.setWidth(dp2px(90));

            openItem.setTitle("Open");

            openItem.setTitleSize(18);

            openItem.setTitleColor(Color.WHITE);

            menu.addMenuItem(openItem);

            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    getContext());

            deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));

            deleteItem.setWidth(dp2px(90));

            deleteItem.setIcon(R.drawable.ic_delete);

            menu.addMenuItem(deleteItem);
        };

        mListView.setMenuCreator(creator);

        mListView.setOnMenuItemClickListener((position, menu, index) -> {

            switch (index) {
                case 0:

                    break;

                case 1:

                    break;
            }
            return false;
        });

        mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        savedState = savedState();
    }

    private Bundle savedState() {
        Bundle state = new Bundle();
        state.putSerializable("selectedMenuData", (Serializable) selectedMenuData);
        return state;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBundle("selectedMenuData", (savedState != null) ? savedState : savedState());
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}









