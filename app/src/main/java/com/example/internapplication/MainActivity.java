package com.example.internapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView mitemlist;
    private FirestoreRecyclerAdapter adapter;

    TextView TextView_location;
    LocationManager locationManager;

    private ImageView account;
    private ImageView rewards;
    private ImageView add;
    private ImageView settings;
    private ImageView help;

    public MainActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseFirestore=FirebaseFirestore.getInstance();
        mitemlist= findViewById(R.id.itemlist);
        TextView_location=findViewById(R.id.text_location);

        account=findViewById(R.id.account);
        rewards=findViewById(R.id.rewards);
        add=findViewById(R.id.add);
        settings=findViewById(R.id.settings);
        help=findViewById(R.id.help);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,shopadd.class);
                startActivity(intent);
            }
        });


        Query query=firebaseFirestore.collection("shop info");
        FirestoreRecyclerOptions<productsmodel> options = new FirestoreRecyclerOptions.Builder<productsmodel>()
                .setQuery(query, productsmodel.class)
                .build();

        FirestoreRecyclerAdapter adapter= new FirestoreRecyclerAdapter<productsmodel, productsviewholder>(options) {
            @NonNull
            @Override
            public productsviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_shop,parent,false);
                return new productsviewholder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull productsviewholder holder, int position, @NonNull productsmodel model) {
                holder.shopname.setText(model.getShopname());
                holder.shoptype.setText(model.getShoptype());
                holder.shop_location.setText(model.getShop_location());
                holder.shop_opentime.setText(model.getShop_opentime());
                holder.shop_closetime.setText(model.getShop_closetime()+"");
            }
        };
        mitemlist.setHasFixedSize(true);
        mitemlist.setLayoutManager(new LinearLayoutManager(this));
        mitemlist.setAdapter(adapter);

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },100);
        }
        TextView_location.setOnClickListener(v -> getLocation());
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {

        try {
            locationManager=(LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,MainActivity.this);
        }catch (Exception e){
            e.printStackTrace();
        }



    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Toast.makeText(this,""+location.getLatitude()+","+location.getLongitude(),Toast.LENGTH_SHORT).show();
        try {
            Geocoder geocoder= new Geocoder(MainActivity.this, Locale.getDefault());
            List<Address> addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            String address=addresses.get(0).getAddressLine(0);
            TextView_location.setText(address);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private class productsviewholder extends RecyclerView.ViewHolder {

        private ImageView shop_image;
        private ImageView dialler;
        private ImageView favourites;
        private TextView shopname;
        private TextView shoptype;
        private TextView shop_location;
        private TextView shop_opentime;
        private TextView shop_closetime;
        public productsviewholder(@NonNull View itemView) {
            super(itemView);
            shop_image=itemView.findViewById(R.id.shop_image);
            dialler=itemView.findViewById(R.id.dialler);
            favourites=itemView.findViewById(R.id.favourites);
            shopname=itemView.findViewById(R.id.shopname);
            shoptype=itemView.findViewById(R.id.shoptype);
            shop_location=itemView.findViewById(R.id.shop_location);
            shop_opentime=itemView.findViewById(R.id.shop_opentime);
            shop_closetime=itemView.findViewById(R.id.shop_closetime);


        }
    }

    @Override
    protected void onStop() {
        try {
            super.onStop();
            adapter.stopListening();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onStart() {
        try {
            super.onStart();
            adapter.startListening();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
