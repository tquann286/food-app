package com.example.foodapp.customerFoodPanel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.R;
import java.util.List;

public class CustomerPaymentOrderViewAdapter extends RecyclerView.Adapter<CustomerPaymentOrderViewAdapter.ViewHolder> {

    private Context mcontext;
    private List<CustomerFinalOrders> customerPaymentOrderslist;

    public CustomerPaymentOrderViewAdapter(Context context, List<CustomerFinalOrders> customerPaymentOrderslist) {
        this.customerPaymentOrderslist = customerPaymentOrderslist;
        this.mcontext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.customer_paymentorder_view, parent, false);
        return new CustomerPaymentOrderViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerPaymentOrderViewAdapter.ViewHolder holder, int position) {

        final CustomerFinalOrders customerPaymentOrders = customerPaymentOrderslist.get(position);
        holder.dishname.setText(customerPaymentOrders.getDishName());
        holder.price.setText("Giá: " + customerPaymentOrders.getDishPrice() + "vnd");
        holder.quantity.setText("× " + customerPaymentOrders.getDishQuantity());
        holder.totalprice.setText("Tổng tiền: " + customerPaymentOrders.getTotalPrice() + "vnd");
    }

    @Override
    public int getItemCount() {
        return customerPaymentOrderslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView dishname, price, totalprice, quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dishname = itemView.findViewById(R.id.Dname);
            price = itemView.findViewById(R.id.Dprice);
            totalprice = itemView.findViewById(R.id.Tprice);
            quantity = itemView.findViewById(R.id.Dqty);
        }
    }


}
