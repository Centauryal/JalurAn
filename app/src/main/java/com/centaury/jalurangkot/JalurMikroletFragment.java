package com.centaury.jalurangkot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


public class JalurMikroletFragment extends Fragment {

    private static final String TAG = JalurMikroletFragment.class.getSimpleName();

    private RecyclerView rvJalurMikrolet;
    private List<JalurRute> jalurList;
    private ListJalurAdapter listJalurAdapter;

    public JalurMikroletFragment() {
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
        View view = inflater.inflate(R.layout.fragment_jalur_mikrolet, container, false);

        rvJalurMikrolet = (RecyclerView) view.findViewById(R.id.rv_jalur_mikrolet);
        rvJalurMikrolet.setHasFixedSize(true);

        jalurList = new ArrayList<>();
        jalurList.addAll(JalurData.getListData());

        showRecyclerList();

        rvJalurMikrolet.addOnItemTouchListener(new RecyclerTouchListener(getContext(), rvJalurMikrolet,
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
        rvJalurMikrolet.setLayoutManager(new LinearLayoutManager(getActivity()));
        listJalurAdapter = new ListJalurAdapter(getActivity(), jalurList);
        rvJalurMikrolet.setAdapter(listJalurAdapter);
    }

    class ListJalurAdapter extends RecyclerView.Adapter<JalurMikroletFragment.ListJalurAdapter.MyViewHolder> {
        private Context context;
        private List<JalurRute> jalurList;

        public ListJalurAdapter(Context context, List<JalurRute> jalurList) {
            this.context = context;
            this.jalurList = jalurList;
        }

        @Override
        public JalurMikroletFragment.ListJalurAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_row_jalurrute, parent, false);

            return new JalurMikroletFragment.ListJalurAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(JalurMikroletFragment.ListJalurAdapter.MyViewHolder holder, int position) {
            final JalurRute jalurRute = jalurList.get(position);
            holder.tvimgName.setText(jalurRute.getName());
            holder.tvName.setText(jalurRute.getName());
            holder.tvDesc.setText(jalurRute.getDesc());

            Glide.with(context)
                    .load(jalurRute.getPhoto())
                    .into(holder.imgPhoto);

        }

        @Override
        public int getItemCount() {
            return jalurList.size();
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
