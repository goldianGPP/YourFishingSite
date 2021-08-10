package com.goldian.yourfishsingsite.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.goldian.yourfishsingsite.Controller.ItemController;
import com.goldian.yourfishsingsite.Model.ItemModel;
import com.goldian.yourfishsingsite.Model.PreferencesModel;
import com.goldian.yourfishsingsite.R;
import com.goldian.yourfishsingsite.View.Adapter.ItemAdapter;
import com.goldian.yourfishsingsite.View.Adapter.JenisAdapter;

import java.util.ArrayList;
import java.util.List;

public class CariBarangFragment extends Fragment {

    SearchView srcItem;
    RecyclerView recyclerView, recyclerView2;
    LinearLayout fieldNotFound;
    ItemAdapter itemAdapter;
    JenisAdapter jenisAdapter;
    PreferencesModel pref;
    ProgressBar progressBar;
    Button btnReload;
    boolean isSearch;
    ItemController itemController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_search, container, false);
        init(v);
        setListener();
        request("",false);
        return v;
    }

    private void init(View v) {
        pref = new PreferencesModel(getContext(), getResources().getString(R.string.login));
        itemController = new ItemController(getContext(),this);

        srcItem = v.findViewById(R.id.srcItem);
        btnReload = v.findViewById(R.id.btnReload);
        progressBar = v.findViewById(R.id.progressBar);
        fieldNotFound = v.findViewById(R.id.fieldNotFound);

        recyclerView = v.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView2 = v.findViewById(R.id.recyclerView2);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void setListener(){
        btnReload.setOnClickListener(onClick);
        srcItem.setOnQueryTextListener(onQuery);
    }

    public void request(String attr, boolean isSearch){
        this.isSearch = isSearch;
        if (isSearch){
            itemController
                .getSearchRecomendation(
                    pref.read("id_pengguna"),
                    attr
                );
        }
        else {
            itemController
                .getRecomendation(
                    pref.read("id_pengguna"),
                    attr
                );
        }
    }

    private void setTags(List<ItemModel> itemModelList){
        List<String> jenisList = new ArrayList<>();
        for (ItemModel itemModel : itemModelList) {
            if (!jenisList.contains(itemModel.getJenis()))
                jenisList.add(itemModel.getJenis());
        }

        jenisAdapter = new JenisAdapter(getContext(), this, jenisList);
        recyclerView2.setAdapter(jenisAdapter);
    }

    public void result(List<ItemModel> itemModelList){
        if (itemModelList.size()>0){
            progressBar.setVisibility(View.GONE);
            fieldNotFound.setVisibility(View.GONE);

            if (isSearch){
                srcItem.setQuery("", false);
                srcItem.clearFocus();
                //srcItem.setIconified(true);
            }
        }
        else {
            progressBar.setVisibility(View.GONE);
            if (isSearch)
                fieldNotFound.setVisibility(View.VISIBLE);
            else
                btnReload.setVisibility(View.VISIBLE);
        }
        itemAdapter = new ItemAdapter(getContext(), itemModelList, itemModelList.size(), "horizontal");
        recyclerView.setAdapter(itemAdapter);
        setTags(itemModelList);

    }

    //listener
    //---------------
    View.OnClickListener onClick = view -> {
        if (view == btnReload) {
            progressBar.setVisibility(View.VISIBLE);
            btnReload.setVisibility(View.GONE);
            request("",false);
        }
    };

    SearchView.OnQueryTextListener onQuery = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            request(s,true);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            return false;
        }
    };
}
