package com.example.kyscanner.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserListViewModel extends ViewModel {
    private static final MutableLiveData<Integer> userList = new MutableLiveData<>();

    public LiveData<Integer> getUserListSize() {
        return userList;
    }

    public static void setUserListSize(int size) {
        userList.postValue(size); // Update LiveData
    }
}
