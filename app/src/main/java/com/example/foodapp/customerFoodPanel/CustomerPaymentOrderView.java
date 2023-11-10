package com.example.foodapp.customerFoodPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.foodapp.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerPaymentOrderView extends AppCompatActivity {

    RecyclerView recyclerViewdish;
    private List<CustomerFinalOrders> customerFinalOrderList;
    private CustomerPaymentOrderViewAdapter adapter;
    DatabaseReference reference;
    String RandomUID, userid;
    TextView grandtotal, note, address, name, number;
    LinearLayout l1;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_payment_order_view);

        recyclerViewdish = findViewById(R.id.Recycle_viewOrderr);
        grandtotal = findViewById(R.id.rupees);
        note = findViewById(R.id.NOTE);
        address = findViewById(R.id.ad);
        name = findViewById(R.id.nm);
        number = findViewById(R.id.num);
        l1 = findViewById(R.id.button2);
        progressDialog = new ProgressDialog(CustomerPaymentOrderView.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        recyclerViewdish.setHasFixedSize(true);
        recyclerViewdish.setLayoutManager(new LinearLayoutManager(CustomerPaymentOrderView.this));
        customerFinalOrderList = new ArrayList<>();

        CustomerOrderDishesView();
    }

    private void CustomerOrderDishesView() {
        RandomUID = getIntent().getStringExtra("RandomUID");

        reference = FirebaseDatabase.getInstance().getReference("CustomerFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Dishes");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                customerFinalOrderList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CustomerFinalOrders customerFinalOrders = snapshot.getValue(CustomerFinalOrders.class);
                    customerFinalOrderList.add(customerFinalOrders);
                }
                if (customerFinalOrderList.size() == 0) {
                    l1.setVisibility(View.INVISIBLE);
                } else {
                    l1.setVisibility(View.VISIBLE);
                }

                adapter = new CustomerPaymentOrderViewAdapter(CustomerPaymentOrderView.this, customerFinalOrderList);
                recyclerViewdish.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("CustomerFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CustomerPaymentOrders customer = dataSnapshot.getValue(CustomerPaymentOrders.class);
                grandtotal.setText(customer.getGrandTotalPrice() + "vnd");
                note.setText(customer.getNote());
                address.setText(customer.getAddress());
                name.setText(customer.getName());
                number.setText("+84" + customer.getMobileNumber());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}