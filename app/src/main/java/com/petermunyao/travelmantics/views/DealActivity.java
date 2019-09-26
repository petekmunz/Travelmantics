package com.petermunyao.travelmantics.views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.petermunyao.travelmantics.R;
import com.petermunyao.travelmantics.databinding.ActivityDealBinding;
import com.petermunyao.travelmantics.dialogFragment.UploadImageAlert;
import com.petermunyao.travelmantics.model.TravelDeal;
import com.petermunyao.travelmantics.viewmodels.DealActivityViewModel;
import com.petermunyao.travelmantics.viewmodels.FirebaseUtilViewModel;


public class DealActivity extends AppCompatActivity {

    public static final int IMAGE_REQUEST_CODE = 002;
    private ActivityDealBinding mBinding;
    private DealActivityViewModel mViewModel;
    private FirebaseUtilViewModel mUtilViewModel;
    private StorageReference mStorageReference;
    private TravelDeal mDeal;
    private boolean isAdmin;
    private String mImageUrl = "";
    private String mImageName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_deal);
        mViewModel = new ViewModelProvider(this).get(DealActivityViewModel.class);
        mUtilViewModel = new ViewModelProvider(this).get(FirebaseUtilViewModel.class);
        mBinding.setLifecycleOwner(this);

        mUtilViewModel.connectToStorage();
        mStorageReference = mUtilViewModel.getStorageRef();

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("IsAdmin Pref", MODE_PRIVATE);
        isAdmin = preferences.getBoolean("Valid", false); //Default value if fails or preferences is not set.

        Intent intent = getIntent();
        mDeal = intent.getParcelableExtra("travelDeal");

        if (mDeal != null) {
            mImageUrl = mDeal.getImageUrl();
            showImage();
            mBinding.etxtTitle.setText(mDeal.getTitle());
            mBinding.etxtDescription.setText(mDeal.getDescription());
            mBinding.etxtPrice.setText(mDeal.getPrice());
        }

        mBinding.btnImage.setOnClickListener(v -> {
            Intent imageIntent = new Intent(Intent.ACTION_GET_CONTENT);
            imageIntent.setType("image/jpeg");
            imageIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            startActivityForResult(Intent.createChooser(imageIntent, "Insert Picture"), IMAGE_REQUEST_CODE);
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_menu:
                saveDeal();
                break;
            case R.id.delete_menu:
                deleteDeal();

        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteDeal() {
        if (mDeal == null) {
            Toast.makeText(DealActivity.this, "Please save deal before deleting", Toast.LENGTH_SHORT).show();
        } else {
            mViewModel.deleteDealfromFirestore(mDeal);
            mStorageReference = mUtilViewModel.getStorage().getReference();
            mStorageReference.child(mDeal.getImageName())
                    .delete()
                    .addOnSuccessListener(aVoid -> {

                    })
                    .addOnFailureListener(e -> Toast.makeText(DealActivity.this, "Error deleting the deal image", Toast.LENGTH_SHORT).show());
            backToList();
        }
    }

    private void backToList() {
        Intent intent = new Intent(DealActivity.this, ListActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.deal_activity_menu, menu);
        MenuItem deleteMenu = menu.findItem(R.id.delete_menu);
        MenuItem saveMenu = menu.findItem(R.id.save_menu);

        if (isAdmin == true) {
            deleteMenu.setVisible(true);
            saveMenu.setVisible(true);
            mBinding.btnImage.setVisibility(View.VISIBLE);
            enableEditTexts(true);
        } else {
            deleteMenu.setVisible(false);
            saveMenu.setVisible(false);
            mBinding.btnImage.setVisibility(View.GONE);
            enableEditTexts(false);
        }
        return true;
    }

    private void saveDeal() {
        String title = mBinding.etxtTitle.getText().toString().trim();
        String price = mBinding.etxtPrice.getText().toString().trim();
        String description = mBinding.etxtDescription.getText().toString().trim();

        if (title.isEmpty()) {
            mBinding.etxtTitle.setError(getResources().getString(R.string.title_error));
        } else if (price.isEmpty()) {
            mBinding.etxtPrice.setError(getResources().getString(R.string.price_error));
        } else if (description.isEmpty()) {
            mBinding.etxtDescription.setError(getResources().getString(R.string.description_error));
        } else if (mImageUrl.equals("")) {
            alertUser();
        } else {
            TravelDeal travelDeal = new TravelDeal(title, description, price, mImageUrl, mImageName);
            mViewModel.saveDealtoFirestore(travelDeal);
            clean();
            backToList();
        }


    }

    private void clean() {
        mBinding.etxtDescription.setText("");
        mBinding.etxtPrice.setText("");
        mBinding.etxtTitle.setText("");
    }

    private void enableEditTexts(boolean isEnabled) {
        mBinding.etxtTitle.setEnabled(isEnabled);
        mBinding.etxtPrice.setEnabled(isEnabled);
        mBinding.etxtDescription.setEnabled(isEnabled);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();
            mStorageReference.child(imageUri.getLastPathSegment())
                    .putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                        String pictureName = taskSnapshot.getStorage().getPath();
                        Task<Uri> imagestoredUri = taskSnapshot.getStorage().getDownloadUrl();
                        imagestoredUri.addOnSuccessListener(uri -> {
                            String url = uri.toString();
                            mImageUrl = url;
                            mImageName = pictureName;
                            showImage();
                            Toast.makeText(DealActivity.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();
                        });
                    });
        }
    }

    private void showImage() {
        Glide.with(DealActivity.this)
                .load(mImageUrl)
                .centerCrop()
                .into(mBinding.image);
    }

    private void alertUser() {
        UploadImageAlert dialog = new UploadImageAlert();
        dialog.show(getSupportFragmentManager(), "Alert_Dialog");
    }
}
