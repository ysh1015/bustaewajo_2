package com.hk.transportProject;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hk.transportProject.viewmodel.AuthViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AuthViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private AuthRepository authRepository;
    private AuthViewModel authViewModel;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        authViewModel = new AuthViewModel(authRepository);
    }

    @Test
    public void testLoginSuccess(){
        // Given
        User user = new User("testUser", "testPassword");
        LoginResponse mockResponse = new LoginResponse(true, "로그인 성공");

        MutableLiveData<LoginResponse> liveData = new MutableLiveData<>();
        liveData.setValue(mockResponse);

        when(authRepository.login(any(User.class))).thenAnswer(invocation -> {
            // 전달된 User 객체를 콘솔에 출력
            User userArgument = invocation.getArgument(0);
            System.out.println("AuthViewModelTestSuccess - ID: " + userArgument.getUserId());
            System.out.println("AuthViewModelTestSuccess - Password: " + userArgument.getPassword());

            return liveData;
        });

        // When - 로그인 요청
        LiveData<LoginResponse> responseLiveData = authViewModel.login(user.getUserId(), user.getPassword());
        // Then
        assertNotNull(responseLiveData);
        assertEquals(true, responseLiveData.getValue().isSuccess());
        assertEquals("로그인 성공", responseLiveData.getValue().getMessage());
    }

    @Test
    public void testLoginFailure() {
        // Given: 실패한 로그인 응답
        User user = new User("wrongUser", "wrongPassword");
        LoginResponse mockResponse = new LoginResponse(false, "로그인 실패");

        MutableLiveData<LoginResponse> liveData = new MutableLiveData<>();
        liveData.setValue(mockResponse);

        // Repository의 로그인 요청이 실패할 때 반환할 LiveData 설정
        when(authRepository.login(any(User.class))).thenReturn(liveData);

        // When: ViewModel에서 로그인 요청을 수행
        LiveData<LoginResponse> responseLiveData = authViewModel.login(user.getUserId(), user.getPassword());

        // Then: ViewModel이 반환하는 LiveData에서 실패 여부 확인
        assertNotNull(responseLiveData);
        assertNotNull(responseLiveData.getValue());
        assertEquals(false, responseLiveData.getValue().isSuccess());
        assertEquals("로그인 실패", responseLiveData.getValue().getMessage());
    }

}