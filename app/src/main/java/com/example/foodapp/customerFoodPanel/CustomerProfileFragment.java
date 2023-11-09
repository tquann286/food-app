package com.example.foodapp.customerFoodPanel;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.foodapp.MainMenu;
import com.example.foodapp.R;
import com.example.foodapp.Registration;
import com.example.foodapp.chefFoodPanel.UpdateDelete_Dish;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerProfileFragment extends Fragment {

    EditText firstname, lastname, address;
    Spinner State, City, Suburban;
    TextView mobileno, Email;
    EditText Area;
    Button Update;
    LinearLayout password, LogOut;
    DatabaseReference databaseReference, data;
    FirebaseDatabase firebaseDatabase;
    String statee, cityy, email, passwordd, confirmpass;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_customerprofile, null);
        getActivity().setTitle("Trang cá nhân");

        firstname = (EditText) v.findViewById(R.id.fnamee);
        lastname = (EditText) v.findViewById(R.id.lnamee);
        address = (EditText) v.findViewById(R.id.address);
        Email = (TextView) v.findViewById(R.id.emailID);
        State = (Spinner) v.findViewById(R.id.statee);
        City = (Spinner) v.findViewById(R.id.cityy);
        Area = (EditText) v.findViewById(R.id.area);
        mobileno = (TextView) v.findViewById(R.id.mobilenumber);
        Update = (Button) v.findViewById(R.id.update);
        password = (LinearLayout) v.findViewById(R.id.passwordlayout);
        LogOut = (LinearLayout) v.findViewById(R.id.logout_layout);

        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Customer").child(userid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Customer customer = dataSnapshot.getValue(Customer.class);

                Map<String, List<String>> state = new HashMap<>();

                List<String> haNoiList = new ArrayList<>();
                haNoiList.add("Quận Hoàn Kiếm");
                haNoiList.add("Quận Ba Đình");
                haNoiList.add("Quận Tây Hồ");

                List<String> thuaThienHueList = new ArrayList<>();
                thuaThienHueList.add("Thành phố Huế");
                thuaThienHueList.add("Huyện Nam Đông");
                thuaThienHueList.add("Huyện Phong Điền");

                List<String> saiGonList = new ArrayList<>();
                saiGonList.add("Quận 1");
                saiGonList.add("Quận Bình Thạnh");
                saiGonList.add("Quận Tân Bình");

                state.put("Hà Nội", haNoiList);
                state.put("Thừa Thiên Huế", thuaThienHueList);
                state.put("TP. Hồ Chí Minh", saiGonList);

                firstname.setText(customer.getFirstName());
                lastname.setText(customer.getLastName());
                address.setText(customer.getLocalAddress());
                mobileno.setText(customer.getMobileNo());
                Email.setText(customer.getEmailId());
                Area.setText(customer.getArea());

                State.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        Object value = parent.getItemAtPosition(position);
                        statee = value.toString().trim();

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,state.get(statee));
                        City.setAdapter(arrayAdapter);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                City.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Object value = parent.getItemAtPosition(position);
                        cityy = value.toString().trim();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                State.setSelection(getIndexByString(State, customer.getState()));
                City.setSelection(getIndexByString(City, customer.getCity()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        updateInformation();
        return v;
    }
    private void updateInformation(){
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String useridd = FirebaseAuth.getInstance().getCurrentUser().getUid();
                data = FirebaseDatabase.getInstance().getReference("Customer").child(useridd);
                data.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Customer customer = dataSnapshot.getValue(Customer.class);


                        confirmpass = customer.getConfirmPassword();
                        email = customer.getEmailId();
                        passwordd = customer.getPassword();
                        long mobilenoo = Long.parseLong(customer.getMobileNo());

                        String Fname = firstname.getText().toString().trim();
                        String Lname = lastname.getText().toString().trim();
                        String Address = address.getText().toString().trim();
                        String area = address.getText().toString().trim();

                        HashMap<String, String> hashMappp = new HashMap<>();
                        hashMappp.put("City", cityy);
                        hashMappp.put("ConfirmPassword", confirmpass);
                        hashMappp.put("EmailID", email);
                        hashMappp.put("FirstName", Fname);
                        hashMappp.put("LastName", Lname);
                        hashMappp.put("MobileNo", String.valueOf(mobilenoo));
                        hashMappp.put("Password", passwordd);
                        hashMappp.put("LocalAddress", Address);
                        hashMappp.put("State", statee);
                        hashMappp.put("Area", area);
                        firebaseDatabase.getInstance().getReference("Customer").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(hashMappp).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getActivity(),"Cập nhật thành công!",Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), CustomerPassword.class);
                startActivity(intent);
            }
        });

        mobileno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CustomerPhoneNumber.class);
                startActivity(i);
            }
        });

        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to Logout ?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getActivity(), MainMenu.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);


                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();


            }
        });

    }
    private int getIndexByString(Spinner st, String spist) {
        int index = 0;
        for (int i = 0; i < st.getCount(); i++) {
            if (st.getItemAtPosition(i).toString().equalsIgnoreCase(spist)) {
                index = i;
                break;
            }
        }
        return index;
    }
}
