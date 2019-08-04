package com.petermunyao.travelmantics.viewmodels;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.petermunyao.travelmantics.model.TravelDeal;
import com.petermunyao.travelmantics.repository.FirestoreRepository;

public class DealActivityViewModel extends AndroidViewModel {

    private FirestoreRepository mRepository;

    public DealActivityViewModel(@NonNull Application application) {
        super(application);
        mRepository = new FirestoreRepository(application);
    }

    public void saveDealtoFirestore(TravelDeal travelDeal){
        mRepository.saveDeal(travelDeal)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getApplication(), "Deal successfully saved!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplication(), "Error saving deal", Toast.LENGTH_LONG).show();
                });
    }

    public void deleteDealfromFirestore(TravelDeal travelDeal){
        mRepository.deleteDeal(travelDeal)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getApplication(), "Deal successfully deleted!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplication(), "Error deleting the deal", Toast.LENGTH_SHORT).show();
                });
    }
}
