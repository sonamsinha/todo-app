package com.work.todoapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.work.todoapp.session.AppSession;
import com.work.todoapp.utils.AppConstants;

public class DetailsActivity extends AppCompatActivity {

    private Context context;

    private AppSession appSession;

    private String todoItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        appSession = AppSession.getAppSessionInstance();
        setContentView(R.layout.activity_details);
        Bundle bundle = getIntent().getExtras();
        setToDoHeading(bundle);
        setToDoDetails(todoItem);
        setButtonHandler();
    }

    private void setToDoHeading(Bundle bundle)
    {
        TextView todoHeading = (TextView)findViewById(R.id.todoHeading);
        if (null != bundle)
        {
            todoItem = bundle.getString(AppConstants.TODO_HEADING);
            todoHeading.setText(todoItem);
        }
    }

    private void setToDoDetails(String todoItem)
    {
        String details = appSession.getTodoDetails(todoItem);
        EditText detailsText = (EditText)findViewById(R.id.details);
        detailsText.setText(details);
    }

    @Override
    public void onBackPressed()
    {
        Log.w("DETAILS ACTIVITY ===", "Clicked on back button");
        addDetailsForToDo();
        super.onBackPressed();
    }

    private void addDetailsForToDo()
    {
        EditText details = (EditText)findViewById(R.id.details);
        String todo_details = details.getText().toString();
        Log.w("DETAILS ACTIVITY ===", "Details entered are " + details.getText());
        if (null != todo_details && !todo_details.isEmpty())
        {
            saveTodoDetails(todo_details);
        }
    }

    private void saveTodoDetails(String todo_details)
    {
        appSession.setTodoDetails(todoItem, todo_details);
    }

    private void setButtonHandler()
    {
        Button logOut = (Button)findViewById(R.id.detail_logOut);
        logOut.setOnClickListener(new ButtonHandler());

        Button call = (Button)findViewById(R.id.call);
        call.setOnClickListener(new ButtonHandler());

        Button textEmail = (Button)findViewById(R.id.textEmail);
        textEmail.setOnClickListener(new ButtonHandler());

    }

    private void logOut()
    {
        appSession.clearSession();
    }

    class ButtonHandler implements View.OnClickListener
    {
        @Override
        public void onClick(View view)
        {
            Intent intent = null;
            if (view.getId() == R.id.detail_logOut)
            {
                logOut();
                intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
            else if (view.getId() == R.id.call)
            {
                Log.w("DETAILS ACTIVITY ===", "Call needs to be made");
                try{
                    intent = new Intent(Intent.ACTION_DIAL);
                    startActivity(intent);
                }catch(SecurityException e){
                    Log.e("Details Activity ===", "Could not make a call");
                }

            }
            else if (view.getId() == R.id.textEmail)
            {
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, ""));
            }
        }
    }


    /**Matcher call_matcher = AppConstants.CALL_PATTERN.matcher(selectedValue);
    Matcher talk_matcher = AppConstants.TALK_PATTERN.matcher(selectedValue);
    Matcher text_matcher = AppConstants.TEXT_PATTERN.matcher(selectedValue);
    if (call_matcher.find() || talk_matcher.find())
    {
        Log.w("TODOACTIVITY ===", "Call/Text needs to be made");
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, 1);
    }
    else if (text_matcher.find())
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, ""));
    }**/
}
