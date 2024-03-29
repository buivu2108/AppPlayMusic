package com.example.appplaymusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appplaymusic.App;
import com.example.appplaymusic.R;
import com.example.appplaymusic.purchase.TrivialDriveRepository;
import java.util.ArrayList;


public class PurchaseFragment extends Fragment implements BuyItemListener  {
    private TrivialDriveRepository trivialDriveRepository = App.get().appContainer.trivialDriveRepository;

    private ArrayList<PointPackage> packageList = new ArrayList<>();

    private PurchaseAdapter purchaseAdapter = new PurchaseAdapter(this);

    private RecyclerView rclPurchaseList;

    private ImageView btnBackTop;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_purchase, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rclPurchaseList = view.findViewById(R.id.rclPurchaseList);
        rclPurchaseList.setLayoutManager(new LinearLayoutManager(getContext()));
        rclPurchaseList.setAdapter(purchaseAdapter);
        getPointPackageList();
        purchaseAdapter.addAll(packageList);
    }
    private void getPointPackageList() {
        for (String sku : App.INAPP_SKUS) {
            PointPackage pointPackage = new PointPackage();
            pointPackage.setSku(sku);
            pointPackage.setTitle(trivialDriveRepository.getSkuTitle(sku));
            pointPackage.setPrice(trivialDriveRepository.getSkuPrice(sku));
            packageList.add(pointPackage);
        }
    }

    @Override
    public void buyItem(String sku) {
        trivialDriveRepository.buySku(getActivity(), sku);
    }
}
