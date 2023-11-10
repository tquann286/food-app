package com.example.foodapp.customerFoodPanel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import android.content.Intent;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.R;

import java.util.List;

public class PayableOrderAdapter extends RecyclerView.Adapter<PayableOrderAdapter.ViewHolder> {

    private Context context;
    private List<CustomerPaymentOrders> customerPaymentOrderslist;

    public PayableOrderAdapter(Context context, List<CustomerPaymentOrders> customerPendingOrderslist) {
        this.customerPaymentOrderslist = customerPendingOrderslist;
        this.context = context;
    }

    @NonNull
    @Override
    public PayableOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.customer_payableorder, parent, false);
        return new PayableOrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PayableOrderAdapter.ViewHolder holder, int position) {

        CustomerPaymentOrders customer = customerPaymentOrderslist.get(position);
        holder.Address.setText(customer.getAddress());
        holder.grandtotalprice.setText("Tổng tiền: " + customer.getGrandTotalPrice() + "vnd");
        final String random = customer.getRandomUID();
        holder.Vieworder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CustomerPaymentOrderView.class);
                intent.putExtra("RandomUID", random);
                context.startActivity(intent);
                ((PayableOrders) context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return customerPaymentOrderslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Address, grandtotalprice;
        Button Vieworder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Address = itemView.findViewById(R.id.cust_address);
            grandtotalprice = itemView.findViewById(R.id.Grandtotalprice);
            Vieworder = itemView.findViewById(R.id.View_order);
        }
    }
}
