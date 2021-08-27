package com.goldian.yourfishsingsite.View;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.goldian.yourfishsingsite.Controller.ItemController;
import com.goldian.yourfishsingsite.Model.ItemModel;
import com.goldian.yourfishsingsite.Model.PreferencesModel;
import com.goldian.yourfishsingsite.R;
import com.goldian.yourfishsingsite.View.Adapter.ItemAdapter;
import com.goldian.yourfishsingsite.View.Adapter.JenisAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CariBarangFragment extends Fragment {

    SearchView srcItem;
    RecyclerView recyclerView, recyclerViewTags, recyclerViewRecomendation;
    ItemAdapter itemAdapter;
    JenisAdapter jenisAdapter;
    SwipeRefreshLayout swipeRefresh;
    Spinner spnTipe;
    LinearLayout recomendation;

    ItemController itemController;
    List<ItemModel> itemModels = new ArrayList<>();
    PreferencesModel pref;

    boolean isSearch, isRecomendation;
    String attr;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_search, container, false);
        init(v);
        setListener();
        request("",false, false);
        return v;
    }

    private void init(View v) {
        pref = new PreferencesModel(getContext(), getResources().getString(R.string.login));
        itemController = new ItemController(getContext(),this);

        srcItem = v.findViewById(R.id.srcItem);
        swipeRefresh = v.findViewById(R.id.swipeRefresh);
        spnTipe = v.findViewById(R.id.spnTipe);
        recomendation = v.findViewById(R.id.recomendation);

        setSpinner();
        setRecyclerView(v);
    }

    private void setRecyclerView(View v){
        //items
        recyclerView = v.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        //recomendation
        recyclerViewTags = v.findViewById(R.id.recyclerViewTags);
        recyclerViewTags.setHasFixedSize(true);
        recyclerViewTags.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        //tags
        recyclerViewRecomendation = v.findViewById(R.id.recyclerViewRecomendation);
        recyclerViewRecomendation.setHasFixedSize(true);
        recyclerViewRecomendation.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void setSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.tipe, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTipe.setAdapter(adapter);
    }

    private void setListener(){
        srcItem.setOnQueryTextListener(onQuery);
        swipeRefresh.setOnRefreshListener(() -> request("",false, false));
        spnTipe.setOnItemSelectedListener(onSelected);
    }

    private void setAdapter(RecyclerView recyclerView, List<ItemModel> itemModelList){
        itemAdapter = new ItemAdapter(getContext(), itemModelList, itemModelList.size(), "horizontal");
        recyclerView.setAdapter(itemAdapter);
    }

    private void setAdapter(List<ItemModel> itemModelList){
        if (!isRecomendation){
            itemModels = itemModelList;
            setAdapter(recyclerView, itemModelList);
            isRecomendation = true;
            setTags(itemModelList);
            request(attr, isSearch, isRecomendation);
        }
        else {
            if (itemModelList.size() > 0) {
                recomendation.setVisibility(View.VISIBLE);
                setAdapter(recyclerViewRecomendation, itemModelList);
                isRecomendation = false;
            }
            else
                recomendation.setVisibility(View.GONE);
        }
    }

    private void setTags(List<ItemModel> itemModelList){
        List<String> jenisList = new ArrayList<>();
        for (ItemModel itemModel : itemModelList) {
            String[] separated = itemModel.getJenis().split(",");
            for (String s : separated) {
                if (!jenisList.contains(s))
                    jenisList.add(s);
            }
        }

        jenisAdapter = new JenisAdapter(getContext(), this, jenisList);
        recyclerViewTags.setAdapter(jenisAdapter);
    }

    public void request(String attr, boolean isSearch, boolean isRecomendation){
        this.isSearch = isSearch;
        this.isRecomendation = isRecomendation;
        this.attr = attr;

        swipeRefresh.setRefreshing(true);
        if (!isSearch && isRecomendation){
            itemController
                    .getRecomendation(
                            pref.read("id_pengguna"),
                            attr
                    );
        }
        else if (isSearch && isRecomendation){
            itemController
                .getSearchRecomendation(
                    pref.read("id_pengguna"),
                    attr
                );
        }
        else if (isSearch && !isRecomendation){
            itemController
                    .getSearchItems(
                            attr
                    );
        }
        else {
            itemController
                .getItems(
                    attr
                );
        }
    }

    public void result(List<ItemModel> itemModelList){
        if (itemModelList != null){
            setAdapter(itemModelList);
        }
        swipeRefresh.setRefreshing(false);

    }

    //listener
    //---------------
    AdapterView.OnItemSelectedListener onSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
            swipeRefresh.setRefreshing(true);
            recyclerView.setAdapter(null);
            sort(adapterView.getSelectedItem().toString());
            new Handler().postDelayed(() -> {
                setAdapter(recyclerView, itemModels);
                swipeRefresh.setRefreshing(false);
            },500);
        }
        @Override public void onNothingSelected(AdapterView<?> adapterView) {}
    };

    SearchView.OnQueryTextListener onQuery = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            request(s,true, false);
            return false;
        }
        @Override public boolean onQueryTextChange(String s) {
            return false;
        }
    };

    @SuppressLint({"SimpleDateFormat","compareTo"})
    public void sort(String tipe){
        Collections.sort(itemModels, (itemModel, tempModel) -> {

            if (tipe.equals("rating"))
                return Float.compare(tempModel.getRating(), itemModel.getRating());
            else if (tipe.equals("terbaru")) {
                SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date d1 = sdformat.parse(itemModel.getCreated_at());
                    Date d2 = sdformat.parse(tempModel.getCreated_at());

                    return d1.compareTo(d2);
                }
                catch (ParseException | NullPointerException ignored) {}
            }

            return 0;
        });
    }
}
