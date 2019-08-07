package com.petermunyao.travelmantics.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.petermunyao.travelmantics.repository.FirestoreRepository;

public class FirebaseUtilViewModel extends AndroidViewModel {

    private MutableLiveData<Boolean> mAdminStatus;
    private FirestoreRepository mRepository;

    public FirebaseUtilViewModel(@NonNull Application application) {
        super(application);
        mAdminStatus = new MutableLiveData<>();
        mRepository = new FirestoreRepository(application);
    }

    public MutableLiveData<Boolean> checkAdminStatus(String uid) {
        mRepository.checkAdmin()
                .document(uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            mAdminStatus.setValue(true);
                        } else {
                            mAdminStatus.setValue(false);
                        }
                    }
                });
        return mAdminStatus;
    }

    public StorageReference getStorageRef() {
        return mRepository.getStorageRef();
    }

    public void connectToStorage() {
        mRepository.connectStorage();
    }

    public FirebaseStorage getStorage() {
        return mRepository.connectStorage();
    }
}
