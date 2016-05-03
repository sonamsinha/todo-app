package com.work.todoapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.work.todoapp.session.AppSession;
import com.work.todoapp.utils.AppConstants;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private Context context;

    private Button loginRegisterButton;

    private EditText nameTextBox;

    private EditText emailTextBox;

    private EditText passwordTextBox;

    private SharedPreferences sharedPreferences;

    private AppSession appSession;

    private Set<String> user_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initializing all text inputs.
        nameTextBox = (EditText)findViewById(R.id.name);
        emailTextBox = (EditText)findViewById(R.id.email);
        passwordTextBox = (EditText)findViewById(R.id.password);
        loginRegisterButton = (Button)findViewById(R.id.button);
        hideTextOnScreenLoad();
        setButtonTextOnScreenLoad();
        //hideButton(loginRegisterButton);
        TextView errorText = (TextView)findViewById(R.id.error);
        errorText.setText("Login/Register");
        context = this;
        setTextWatcher();
        setButtonHandler(loginRegisterButton);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //clearSharedPreferences();
    }

    private void hideButton(Button button)
    {
        button.setVisibility(View.INVISIBLE);
    }

    /**
     *
     * @param button
     */
    private void setButtonHandler(Button button)
    {
        button.setOnClickListener(new ButtonHandler());
    }

    /**
     *
     */
    private void setTextWatcher()
    {
        nameTextBox.addTextChangedListener(new LoginInfoTextWatcher());
        emailTextBox.addTextChangedListener(new LoginInfoTextWatcher());
        passwordTextBox.addTextChangedListener(new LoginInfoTextWatcher());
    }

    /**
     *
     *
     */
    private Boolean checkIfUserExists()
    {
        if(null != sharedPreferences)
        {
            String email_id = emailTextBox.getText().toString();
            user_details = sharedPreferences.getStringSet(email_id, null);
            if (user_details == null)
            {
                return Boolean.FALSE;
            }
            else
            {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     *
     */
    private void enableFieldsForRegister()
    {
        nameTextBox.setVisibility(View.VISIBLE);
        passwordTextBox.setVisibility(View.VISIBLE);
        loginRegisterButton.setText(AppConstants.REGISTER_BUTTON);
    }

    private void enableFieldsForLogin()
    {
        nameTextBox.setVisibility(View.INVISIBLE);
        loginRegisterButton.setText(AppConstants.LOGIN_BUTTON);
        passwordTextBox.setVisibility(View.VISIBLE);
    }

    private void addUserToLocalDB()
    {
        String username = nameTextBox.getText().toString();
        username = AppConstants.username_key + username;
        String email = emailTextBox.getText().toString();
        String password = passwordTextBox.getText().toString();
        password = AppConstants.password_key + password;
        if (null != sharedPreferences)
        {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putStringSet(email, new HashSet<String>(Arrays.asList(username, password)));
            edit.apply();
            addUserToSession(sharedPreferences, username, email);
        }
    }

    /**
     *
     */
    private void hideTextOnScreenLoad(){
        nameTextBox.setVisibility(View.INVISIBLE);
        passwordTextBox.setVisibility(View.INVISIBLE);
        loginRegisterButton.setVisibility(View.INVISIBLE);
    }

    /**
     *
     */
    private void setButtonTextOnScreenLoad(){
        loginRegisterButton.setText(AppConstants.NEXT_BUTTON);
    }

    /**
     *
     * @param intent
     */
    private void navigateUserToNextActivity(Intent intent)
    {
        intent = new Intent(context, TodoActivity.class);
        startActivity(intent);
    }

    /**
     *
     * @return
     */
    private boolean validateUserInfo()
    {
        String password = passwordTextBox.getText().toString();
        String email = emailTextBox.getText().toString();
        if(null != user_details)
        {
            for (String details : user_details)
            {
                if (details.contains(AppConstants.password_key))
                {
                    if (password.equalsIgnoreCase(details.substring(AppConstants.password_key.length(), details.length())))
                    {
                        addUserToSession(sharedPreferences, null, email);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     *
     * @param sharedPreferences
     * @param username
     * @param email
     */
    private void addUserToSession(SharedPreferences sharedPreferences, String username, String email)
    {
        appSession = AppSession.getAppSessionInstance();
        appSession.setSessionUser(sharedPreferences, username, email);
    }

    /**
     *
     */
    private void clearSharedPreferences()
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    private void showErrorMessage()
    {
        TextView errorText = (TextView)findViewById(R.id.error);
        errorText.setText(AppConstants.INVALID_CREDENTIALS);
    }


    class ButtonHandler implements View.OnClickListener{

        @Override
        public void onClick(View view){
            Intent intent = null;
            if (view.getId() == R.id.button){
                Button button = (Button)findViewById(R.id.button);
                if(AppConstants.NEXT_BUTTON.equalsIgnoreCase(button.getText().toString()))
                {
                    if (checkIfUserExists())
                    {
                        enableFieldsForLogin();
                    }
                    else
                    {
                        enableFieldsForRegister();
                    }
                }
                else if(AppConstants.LOGIN_BUTTON.equalsIgnoreCase(button.getText().toString()))
                {
                    if (!checkIfUserExists())
                    {
                        enableFieldsForRegister();
                    }
                    else
                    {
                        if (validateUserInfo())
                        {
                            navigateUserToNextActivity(intent);
                        }
                        else
                        {
                            showErrorMessage();
                        }
                    }
                }
                else if(AppConstants.REGISTER_BUTTON.equalsIgnoreCase(button.getText().toString()))
                {
                    if (!checkIfUserExists())
                    {
                        addUserToLocalDB();
                        navigateUserToNextActivity(intent);
                    }
                    else
                    {
                        enableFieldsForLogin();
                    }
                }
            }
        }
    }

    class LoginInfoTextWatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after){

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count){

        }

        @Override
        public void afterTextChanged(Editable editable){
            if (null != editable.toString()){
                if (!editable.toString().isEmpty()){
                    loginRegisterButton.setVisibility(View.VISIBLE);
                } else
                {
                    loginRegisterButton.setVisibility(View.INVISIBLE);
                }

            }
        }
    }
}
