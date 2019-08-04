package com.petermunyao.travelmantics.repository;

import android.app.Application;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.petermunyao.travelmantics.model.TravelDeal;

public class FirestoreRepository {

    private FirebaseFirestore mdB;
    private FirebaseStorage mStorage;
    private StorageReference mStorageReference;

    public FirestoreRepository(Application application){
        mdB = FirebaseFirestore.getInstance();
        mStorage = connectStorage();
        mStorageReference = getStorageRef();
    }

    public CollectionReference getTravelDealsList(){
        final CollectionReference collRef = mdB.collection("TravelDeals");
        return collRef;
    }

    public Task<Void> saveDeal(TravelDeal travelDeal){
        final DocumentReference documentReference = mdB.collection("TravelDeals").document(travelDeal.getTitle());
        return documentReference.set(travelDeal);
    }

    public DocumentReference deleteDeal(TravelDeal travelDeal){
        final DocumentReference documentReference = mdB.collection("TravelDeals").document(travelDeal.getTitle());
        return documentReference;
    }

    public CollectionReference checkAdmin(){
        final CollectionReference adminRef = mdB.collection("Administrator");
        return adminRef;
    }

    public StorageReference getStorageRef(){
        StorageReference storageRef = mStorage.getReference().child("Deals");
        return storageRef;
    }

    public FirebaseStorage connectStorage(){
       mStorage = FirebaseStorage.getInstance();
       return mStorage;
    }

}
