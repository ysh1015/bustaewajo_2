package com.hk.transportProject.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
/*
import com.hk.transportProject.R;
import com.hk.transportProject.databinding.ActivityLoginBinding;
import com.hk.transportProject.viewmodel.AuthViewModel;

import androidx.lifecycle.ViewModelProvider;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // View Binding 초기화
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ViewModel 초기화
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        binding.btnLogin.setOnClickListener(v -> {
            String userId = binding.etUserId.getText().toString();
            String password = binding.etPassword.getText().toString();

            authViewModel.login(userId, password).observe(this, loginResponse-> {
                if (loginResponse != null && loginResponse.isSuccess()) {
                    Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this,"아이디와 비밀번호를 다시 확인하세요.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}


 */