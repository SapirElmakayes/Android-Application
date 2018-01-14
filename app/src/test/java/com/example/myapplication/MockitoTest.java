package com.example.myapplication;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPropertyAnimatorListener;
import org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class MockitoTest {

    private static final String FAKE_STRING = "123456";
    @Mock
    Context mMockContext;

    @Test
    public void readStringFromContext_LocalizedString () {
        // Given a mocked Context injected into the object under test...

        when(mMockContext.getString(R.id.etPassword))
                .thenReturn(FAKE_STRING);
        User password_length_test = new User();
        Boolean result = password_length_test.isValidPassword(FAKE_STRING);
        // ...when the string is returned from the object under test...

        // ...then the result should be the expected one.
      //  assertThat(result,"false");
        assertThat(FAKE_STRING, result);
    }
}
