package com.centaury.jalurangkot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.centaury.jalurangkot.app.AppConfig;
import com.centaury.jalurangkot.app.AppController;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ArmadaTerdekatFragment extends Fragment{

    private static final String TAG = ArmadaTerdekatFragment.class.getSimpleName();

    private RecyclerView rvCategory;
    private List<JalurRute> terdekatList;
    private ListTerdekatAdapter listTerdekatAdapter;

    private GoogleMap mMap;

    public ArmadaTerdekatFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_armada_terdekat, container, false);

        rvCategory = (RecyclerView) view.findViewById(R.id.rv_category);
        rvCategory.setHasFixedSize(true);

        terdekatList = new ArrayList<>();
        terdekatList.addAll(JalurData.getListData());

        showRecyclerList();

        rvCategory.addOnItemTouchListener(new RecyclerTouchListener(getContext(), rvCategory,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                        Context context=view.getContext();
                        final Intent intent;
                        switch (position) {
                            case 0:
                                intent = new Intent(context, Lyn_O.class);
                                break;
                            case 1:
                                intent = new Intent(context, Lyn_O.class);
                                break;
                            case 2:
                                intent = new Intent(context, Lyn_O.class);
                                break;
                            case 3:
                                intent = new Intent(context, Lyn_O.class);
                                break;
                            case 4:
                                intent = new Intent(context, Lyn_O.class);
                                break;
                            case 5:
                                intent = new Intent(context, Lyn_O.class);
                                break;
                            default:
                                intent = new Intent(context, Lyn_O.class);
                                break;
                        }

                        context.startActivity(intent);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                })
        );

        return view;
    }

    private void showRecyclerList(){
        rvCategory.setLayoutManager(new LinearLayoutManager(getActivity()));
        listTerdekatAdapter = new ListTerdekatAdapter(getActivity(), terdekatList);
        rvCategory.setAdapter(listTerdekatAdapter);
    }

    class ListTerdekatAdapter extends RecyclerView.Adapter<ListTerdekatAdapter.MyViewHolder> {
        private Context context;
        private List<JalurRute> terdekatList;

        public ListTerdekatAdapter(Context context, List<JalurRute> terdekatList) {
            this.context = context;
            this.terdekatList = terdekatList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_row_jalurrute, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final JalurRute jalurRute = terdekatList.get(position);
            holder.tvimgName.setText(jalurRute.getName());
            holder.tvName.setText(jalurRute.getName());
            holder.tvDesc.setText(jalurRute.getDesc());

            Glide.with(context)
                    .load(jalurRute.getPhoto())
                    .into(holder.imgPhoto);

        }

        @Override
        public int getItemCount() {
            return terdekatList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tvimgName, tvName, tvDesc;
            public ImageView imgPhoto;

            public MyViewHolder(View view) {
                super(view);
                tvimgName = view.findViewById(R.id.imgname);
                tvName = view.findViewById(R.id.txtname);
                tvDesc = view.findViewById(R.id.txtdesc);
                imgPhoto = view.findViewById(R.id.imgphoto);
            }
        }
    }


}
