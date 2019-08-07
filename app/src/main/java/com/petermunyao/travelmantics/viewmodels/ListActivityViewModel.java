package com.petermunyao.travelmantics.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.petermunyao.travelmantics.model.TravelDeal;
import com.petermunyao.travelmantics.repository.FirestoreRepository;

import java.util.ArrayList;
import java.util.List;

public class ListActivityViewModel extends AndroidViewModel {

    private FirestoreRepository mRepository;
    private MutableLiveData<List<TravelDeal>> mLiveData;


    public ListActivityViewModel(@NonNull Application application) {
        super(application);
        mLiveData = new MutableLiveData<>();
        mRepository = new FirestoreRepository(application);
    }

    public MutableLiveData<List<TravelDeal>> getLiveData() {
        List<TravelDeal> list = new ArrayList<>();
        mRepository.getTravelDealsList().addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e == null && queryDocumentSnapshots != null) {
                list.clear();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    TravelDeal travelDeal = doc.toObject(TravelDeal.class);
                    list.add(travelDeal);
                }
                mLiveData.setValue(list);
            }
        });
        return mLiveData;
    }

}
