package com.example.foodapp.customerFoodPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.example.foodapp.CustomerFoodPanel_BottomNavigation;
import com.example.foodapp.R;
import com.example.foodapp.UpdateDishModel;
import com.example.foodapp.chefFoodPanel.Chef;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class OrderDish extends AppCompatActivity {

    String RandomId, ChefID;
    ImageView imageView;
    NumberPicker numberPicker;
    TextView Foodname, ChefName, ChefLoaction, FoodQuantity, FoodPrice, FoodDescription;
    DatabaseReference databaseReference, dataaa, chefdata, reference, data, dataref;
    String State, City, Sub, dishname;
    int dishprice;
    String custID;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_dish);

        Foodname = (TextView) findViewById(R.id.food_name);
        ChefName = (TextView) findViewById(R.id.chef_name);
        ChefLoaction = (TextView) findViewById(R.id.chef_location);
        FoodQuantity = (TextView) findViewById(R.id.food_quantity);
        FoodPrice = (TextView) findViewById(R.id.food_price);
        FoodDescription = (TextView) findViewById(R.id.food_description);
        imageView = (ImageView) findViewById(R.id.image);
        numberPicker = (NumberPicker) findViewById(R.id.number_btn);
        numberPicker.setMinValue(0);

        final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dataaa = FirebaseDatabase.getInstance().getReference("Customer").child(userid);
        dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Customer cust = dataSnapshot.getValue(Customer.class);
                State = cust.getState();
                City = cust.getCity();

                RandomId = getIntent().getStringExtra("FoodMenu");
                ChefID = getIntent().getStringExtra("ChefId");

                databaseReference = FirebaseDatabase.getInstance().getReference("FoodDetails").child(ChefID).child(RandomId);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UpdateDishModel updateDishModel = dataSnapshot.getValue(UpdateDishModel.class);
                        Foodname.setText(updateDishModel.getDishes());
                        String qua = "<b>" + "Số lượng: " + "</b>" + updateDishModel.getQuantity();

                        numberPicker.setMaxValue(Integer.parseInt(updateDishModel.getQuantity()));

                        FoodQuantity.setText(Html.fromHtml(qua));
                        String ss = "<b>" + "Mô tả: " + "</b>" + updateDishModel.getDescription();
                        FoodDescription.setText(Html.fromHtml(ss));
                        String pri = "<b>" + "Giá: " + updateDishModel.getPrice() + "vnd</b>";
                        FoodPrice.setText(Html.fromHtml(pri));
                        Glide.with(OrderDish.this).load(updateDishModel.getImageURL()).into(imageView);

                        chefdata = FirebaseDatabase.getInstance().getReference("Chef").child(ChefID);
                        chefdata.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Chef chef = dataSnapshot.getValue(Chef.class);

                                String name = "<b>" + "Tên đầu bếp: " + "</b>" + chef.getFname() + " " + chef.getLname();
                                ChefName.setText(Html.fromHtml(name));
                                String loc = "<b>" + "Địa chỉ: " + "</b>" + chef.getHouse();
                                ChefLoaction.setText(Html.fromHtml(loc));
                                custID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                databaseReference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(custID).child(RandomId);
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Cart cart = dataSnapshot.getValue(Cart.class);
                                        if (dataSnapshot.exists()) {
                                            int quantity = 0;
                                            try {
                                                quantity = Integer.parseInt(cart.getDishQuantity());
                                            } catch (NumberFormatException e) {
                                            }
                                            numberPicker.setValue(quantity);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        dataref = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        dataref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Cart cart1= null;
                                if (dataSnapshot.exists()) {
                                    int totalcount=0;
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        totalcount++;
                                    }
                                    int i=0;
                                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                        i++;
                                        if(i==totalcount){
                                            cart1= snapshot.getValue(Cart.class);
                                        }
                                    }

                                    if (ChefID.equals(cart1.getChefId())) {
                                        data = FirebaseDatabase.getInstance().getReference("FoodDetails").child(ChefID).child(RandomId);
                                        data.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                UpdateDishModel update = dataSnapshot.getValue(UpdateDishModel.class);
                                                dishname = update.getDishes();
                                                dishprice = Integer.parseInt(update.getPrice());

                                                int num = newVal;
                                                int totalprice = num * dishprice;
                                                if (num != 0) {
                                                    HashMap<String, String> hashMap = new HashMap<>();
                                                    hashMap.put("DishName", dishname);
                                                    hashMap.put("DishID", RandomId);
                                                    hashMap.put("DishQuantity", String.valueOf(num));
                                                    hashMap.put("Price", String.valueOf(dishprice));
                                                    hashMap.put("Totalprice", String.valueOf(totalprice));
                                                    hashMap.put("ChefId", ChefID);
                                                    custID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                    reference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(custID).child(RandomId);
                                                    reference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(OrderDish.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                } else {
                                                    firebaseDatabase.getInstance().getReference("Cart").child(custID).child(RandomId).removeValue();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(OrderDish.this);
                                        builder.setMessage("Bạn không thể thêm các món ăn của nhiều đầu bếp cùng một lúc\nHãy thêm các món của cùng một đầu bếp");
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                dialog.dismiss();
                                                Intent intent = new Intent(OrderDish.this, CustomerFoodPanel_BottomNavigation.class);
                                                startActivity(intent);
                                                finish();

                                            }
                                        });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                } else {
                                    data = FirebaseDatabase.getInstance().getReference("FoodDetails").child(ChefID).child(RandomId);
                                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            UpdateDishModel update = dataSnapshot.getValue(UpdateDishModel.class);
                                            dishname = update.getDishes();
                                            dishprice = Integer.parseInt(update.getPrice());
                                            int num = newVal;
                                            int totalprice = num * dishprice;

                                            if (num != 0) {
                                                HashMap<String, String> hashMap = new HashMap<>();
                                                hashMap.put("DishName", dishname);
                                                hashMap.put("DishID", RandomId);
                                                hashMap.put("DishQuantity", String.valueOf(num));
                                                hashMap.put("Price", String.valueOf(dishprice));
                                                hashMap.put("Totalprice", String.valueOf(totalprice));
                                                hashMap.put("ChefId", ChefID);
                                                custID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                reference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(custID).child(RandomId);
                                                reference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        Toast.makeText(OrderDish.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            } else {
                                                firebaseDatabase.getInstance().getReference("Cart").child(custID).child(RandomId).removeValue();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}