package com.example.foodapp.customerFoodPanel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class CustomerCartAdapter extends RecyclerView.Adapter<CustomerCartAdapter.ViewHolder>  {
    private Context mcontext;
    private List<Cart> cartModellist;
    static int total = 0;

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
        total += Integer.parseInt(cart.getTotalprice());
        holder.numberPicker.setValue(Integer.parseInt(cart.getDishQuantity()));
        holder.numberPicker.setMaxValue(Integer.parseInt(cart.getDishQuantity()));
        final int dishprice = Integer.parseInt(cart.getPrice());

        holder.numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                int num = newVal;
                int totalprice = num * dishprice;
                if (num != 0) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("DishID", cart.getDishID());
                    hashMap.put("DishName", cart.getDishName());
                    hashMap.put("DishQuantity", String.valueOf(num));
                    hashMap.put("Price", String.valueOf(dishprice));
                    hashMap.put("Totalprice", String.valueOf(totalprice));
                    hashMap.put("ChefId",cart.getChefId());

                    FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(cart.getDishID()).setValue(hashMap);
                } else {
                    FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(cart.getDishID()).removeValue();
                }
            }
        });
        CustomerCartFragment.grandt.setText("Tổng tiền: " + total + "vnd");
        FirebaseDatabase.getInstance().getReference("Cart").child("GrandTotal").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("GrandTotal").setValue(String.valueOf(total));

    }

    @Override
    public int getItemCount() {
        return cartModellist.size();
    }
}
