package com.example.quanlycuahangtrasua.DesignPattern.Builder;

import android.app.Activity;
import android.app.ProgressDialog;

public class ValidateUsernameBuilder {
    private String username;
    private String phone;
    private String password;
    private ProgressDialog progressBar;
    private Activity activity;

    private ValidateUsernameBuilder(Builder builder) {
        this.username = builder.username;
        this.phone = builder.phone;
        this.password = builder.password;
        this.progressBar = builder.progressBar;
        this.activity = builder.activity;
    }

    public String getUsername() {
        return username;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public ProgressDialog getProgressBar() {
        return progressBar;
    }

    public Activity getActivity() {
        return activity;
    }

    public static class Builder {
        private String username;
        private String phone;
        private String password;
        private ProgressDialog progressBar;
        private Activity activity;

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setProgressBar(ProgressDialog progressBar) {
            this.progressBar = progressBar;
            return this;
        }

        public Builder setActivity(Activity activity) {
            this.activity = activity;
            return this;
        }

        public ValidateUsernameBuilder build() {
            return new ValidateUsernameBuilder(this);
        }
    }
}
