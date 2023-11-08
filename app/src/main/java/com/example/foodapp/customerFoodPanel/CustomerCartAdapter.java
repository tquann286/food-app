package com.example.foodapp.customerFoodPanel;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.MainMenu;
import com.example.foodapp.R;
import com.example.foodapp.chefFoodPanel.FoodDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class CustomerCartAdapter extends RecyclerView.Adapter<CustomerCartAdapter.ViewHolder>  {
    private Context mcontext;
    private List<Cart> cartModellist;
    static int total = 0;
    private final Handler handler = new Handler();
    private final int DEBOUNCE_DELAY = 500;

    public CustomerCartAdapter(Context context, List<Cart> cartModellist) {
        this.cartModellist = cartModellist;
        this.mcontext = context;
        total = 0;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.cart_placeorder, parent, false);
        return new CustomerCartAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView dishname, Price, Qty, Total;
        NumberPicker numberPicker;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dishname = itemView.findViewById(R.id.Dishname);
            Price = itemView.findViewById(R.id.price);
            Qty = itemView.findViewById(R.id.qty);
            Total = itemView.findViewById(R.id.total);
            numberPicker = (NumberPicker) itemView.findViewById(R.id.number_btn);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Cart cart = cartModellist.get(position);
        holder.dishname.setText(cart.getDishName());
        holder.Price.setText("Giá: " + cart.getPrice() + "vnd");
        holder.Qty.setText("× " + cart.getDishQuantity());
        holder.Total.setText("Tổng cộng: " + cart.getTotalprice() + "vnd");

        int dishprice = Integer.parseInt(cart.getPrice());
        holder.numberPicker.setMinValue(0);
        holder.numberPicker.setMaxValue(100);
        holder.numberPicker.setValue(Integer.parseInt(cart.getDishQuantity()));

        holder.numberPicker.setOnValueChangedListener(null);

//        FirebaseDatabase.getInstance().getReference("FoodDetails").child(cart.getChefId()).child(cart.getDishID()).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                FoodDetails food = dataSnapshot.getValue(FoodDetails.class);
//
//                holder.numberPicker.setMaxValue(Integer.parseInt(food.Quantity));
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


        updateTotal(holder);

        NumberPicker.OnValueChangeListener valueChangeListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(() -> {
                    int num = newVal;
                    int totalprice = num * dishprice;
                    if (num != 0) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("DishID", cart.getDishID());
                        hashMap.put("DishName", cart.getDishName());
                        hashMap.put("DishQuantity", String.valueOf(num));
                        hashMap.put("Price", String.valueOf(dishprice));
                        hashMap.put("Totalprice", String.valueOf(totalprice));
                        hashMap.put("ChefId", cart.getChefId());

                        FirebaseDatabase.getInstance().getReference("Cart")
                                .child("CartItems")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child(cart.getDishID())
                                .setValue(hashMap);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
                        builder.setMessage("Bạn có chắc xoá sản phẩm khỏi giỏ hàng ?");
                        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                FirebaseDatabase.getInstance().getReference("Cart")
                                        .child("CartItems")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child(cart.getDishID())
                                        .removeValue();

                            }
                        });
                        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                holder.numberPicker.setValue(oldVal);
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();

                    }
                }, DEBOUNCE_DELAY);
            }
        };

        holder.numberPicker.setOnValueChangedListener(valueChangeListener);
        CustomerCartFragment.grandt.setText("Tổng tiền: " + total + "vnd");
        FirebaseDatabase.getInstance().getReference("Cart").child("GrandTotal").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("GrandTotal").setValue(String.valueOf(total));

    }

    @Override
    public int getItemCount() {
        return cartModellist.size();
    }

    private void updateTotal(@NonNull ViewHolder holder) {
        total = 0;
        for (Cart cart : cartModellist) {
            total += Integer.parseInt(cart.getTotalprice());
        }

        CustomerCartFragment.grandt.setText("Tổng tiền: " + total + "vnd");
        FirebaseDatabase.getInstance().getReference("Cart").child("GrandTotal").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("GrandTotal").setValue(String.valueOf(total));
    }
}
