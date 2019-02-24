package com.denta.android.myapi.model;

import java.util.List;

public class UsersResponse {
    private boolean error;
    private List<User>userList;

    public UsersResponse(boolean error, List<User> userList) {
        this.error = error;
        this.userList = userList;
    }

    public boolean isError() {
        return error;
    }

    public List<User> getUserList() {
        return userList;
    }
}
