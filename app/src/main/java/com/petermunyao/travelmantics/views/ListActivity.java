package com.petermunyao.travelmantics.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.petermunyao.travelmantics.DealAdapter;
import com.petermunyao.travelmantics.ListInterface;
import com.petermunyao.travelmantics.R;
import com.petermunyao.travelmantics.databinding.ActivityListBinding;
import com.petermunyao.travelmantics.model.TravelDeal;
import com.petermunyao.travelmantics.viewmodels.FirebaseUtilViewModel;
import com.petermunyao.travelmantics.viewmodels.ListActivityViewModel;

public class ListActivity extends AppCompatActivity implements ListInterface {

    private ActivityListBinding mBinding;
    private DealAdapter mAdapter;
    private boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_list);
        ListActivityViewModel mViewModel = new ViewModelProvider(this).get(ListActivityViewModel.class);
        FirebaseUtilViewModel mUtilViewModel = new ViewModelProvider(this).get(FirebaseUtilViewModel.class);
        mBinding.setLifecycleOwner(this);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(ListActivity.this, RecyclerView.VERTICAL, false));
        mBinding.recyclerView.setHasFixedSize(true);

        //Initialize activity by ensuring preferences is back to default
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("IsAdmin Pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("Valid", false);
        editor.apply();

        mUtilViewModel.checkAdminStatus(FirebaseAuth.getInstance().getUid()).observe(ListActivity.this, aBoolean -> {
            if (aBoolean) {
                isAdmin = true;
                invalidateOptionsMenu();
                SharedPreferences preferences1 = getApplicationContext().getSharedPreferences("IsAdmin Pref", MODE_PRIVATE);
                SharedPreferences.Editor editor1 = preferences1.edit();
                editor1.putBoolean("Valid", true);
                editor1.apply();
            } else {
                isAdmin = false;
            }
        });

        mViewModel.getLiveData().observe(this, list -> {
            if (!list.isEmpty()) {
                if (mAdapter == null) {
                    mBinding.recyclerView.setVisibility(View.VISIBLE);
                    mBinding.txtNoDeals.setVisibility(View.GONE);
                    mAdapter = new DealAdapter(list, ListActivity.this);
                    mBinding.setAdapter(mAdapter);
                } else {
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_activity_menu, menu);
        MenuItem insertMenu = menu.findItem(R.id.insert_menu);

        if (isAdmin == true) {
            insertMenu.setVisible(true);
        } else {
            insertMenu.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.insert_menu:
                Intent intent = new Intent(ListActivity.this, DealActivity.class);
                startActivity(intent);
                break;
            case R.id.logout_menu:
                logout();
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task -> {
                    Intent logoutIntent = new Intent(ListActivity.this, SignInActivity.class);
                    logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(logoutIntent);
                });

    }

    @Override
    public void inflateDealActivity(TravelDeal travelDeal) {
        Intent intent = new Intent(ListActivity.this, DealActivity.class);
        intent.putExtra("travelDeal", travelDeal);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
