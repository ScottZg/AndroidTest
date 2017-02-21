package com.zhanggui.listviewtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

public class ListViewMainActivity extends AppCompatActivity {

    private String[] data = {"Apple","Banana","Orange","Watermelon","Mango","Cherry","Strawberry",
            "Apple","Banana","Orange","Watermelon","Mango","Cherry","Strawberry"};

    private List<Fruit> fruitList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_main_layout);


        initFruits();
        FruitAdapter fruitAdapter = new FruitAdapter(ListViewMainActivity.this,R.layout.fruit_item,
                fruitList);


        ListView listView= (ListView) findViewById(R.id.list_view);

        listView.setAdapter(fruitAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fruit fruit = fruitList.get(position);
                Toast.makeText(ListViewMainActivity.this,fruit.getName(),Toast.LENGTH_SHORT).show();
            }
        });

    }
    private  void initFruits() {
        for (int i=0;i<4;i++) {
            Fruit apple = new Fruit("Apple",R.drawable.fruit1);
            fruitList.add(apple);

            Fruit banana = new Fruit("Banana",R.drawable.fruit2);
            fruitList.add(banana);

            Fruit orange = new Fruit("Orange",R.drawable.fruit3);
            fruitList.add(orange);

            Fruit watermelon = new Fruit("Watermelon",R.drawable.fruit4);
            fruitList.add(watermelon);

            Fruit mango = new Fruit("Mango",R.drawable.fruit5);
            fruitList.add(mango);

            Fruit cherry = new Fruit("Cherry",R.drawable.fruit6);
            fruitList.add(cherry);

            Fruit strawberry = new Fruit("Strawberry",R.drawable.fruit7);
            fruitList.add(strawberry);
        }

    }
}
