package com.work.todoapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.work.todoapp.session.AppSession;
import com.work.todoapp.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

public class TodoActivity extends AppCompatActivity {

    private Context context;

    private AppSession appSession;

    private TextView userName;

    private Button logOutButton;

    private ListView itemList;

    private ArrayList<String> items;

    private ArrayAdapter<String> itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        context = this;
        appSession = AppSession.getAppSessionInstance();
        if (appSession.isUserLoggedIn())
        {
            Log.w("TODO ACTIVITY", "User is logged in");
            setUserName();
            setLogOutButton();
        }
        setButtonHandler();
        fillItems();
        addToDoLongClickListListener();
        addToDoOneClickListener();
    }

    /**
     *
     * @param v
     */
    public void addItem(View v)
    {
        EditText text = (EditText)findViewById(R.id.editText);
        String todoItem = text.getText().toString();
        if (todoItem != null && !todoItem.isEmpty())
        {
            itemsAdapter.add(todoItem);
            addToDoLongClickListListener();
            addToDoOneClickListener();
            addToDoToSession(todoItem, appSession.getUsername());
            text.setText(AppConstants.EMPTY);
        }
    }

    /**
     *
     */
    private void addToDoLongClickListListener()
    {
        if (null != itemList)
        {
            itemList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> adapter, View view, int pos, long id) {
                    items.remove(pos);
                    updateTodoInSession();
                    itemsAdapter.notifyDataSetChanged();
                    return true;
                }
            });
        }
    }

    private void addToDoOneClickListener()
    {
        if (null != itemList)
        {
            itemList.setOnItemClickListener( new AdapterView.OnItemClickListener(){

                @Override
                public void onItemClick(AdapterView<?> adapter, View view, int pos, long id)
                {
                    String selectedValue = (String) adapter.getAdapter().getItem(pos);
                    if (null != selectedValue && !selectedValue.isEmpty())
                    {
                        Intent intent = new Intent(context, DetailsActivity.class);
                        intent.putExtra(AppConstants.TODO_HEADING, selectedValue);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    /**
     *
     */
    private void updateTodoInSession()
    {
        if (null != appSession)
        {
            appSession.updateTodoList(items, appSession.getUsername());
        }
    }

    /**
     *
     * @param todoItem
     * @param username
     */
    private void addToDoToSession(String todoItem, String username)
    {
        if (null != appSession)
        {
            appSession.setTodoItemInList(todoItem, username);
        }
    }


    /**
     *
     * @param username
     * @return
     */
    private List<String> getToDoList(String username)
    {
        return appSession.getToDoList(username);
    }

    /**
     *
     */
    private void fillItems()
    {
        itemList = (ListView) findViewById(R.id.listView);
        items = new ArrayList<String>();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        itemList.setAdapter(itemsAdapter);
        List<String> todoList = getToDoList(appSession.getUsername());
        if (null != todoList)
        {
            for (String toDo : todoList)
            {
                items.add(toDo);
            }
        }
    }

    /**
     *
     */
    private void setButtonHandler(){
        logOutButton = getLogOutButton();
        logOutButton.setOnClickListener(new ButtonHandler());
    }

    /**
     *
     */
    private void setUserName()
    {
        userName = (TextView)findViewById(R.id.userName);
        Log.w("TODO ACTIVITY", "User name is "+appSession.getUsername());
        userName.setText(AppConstants.SALUTATION + appSession.getUsername());
    }

    /**
     *
     */
    private void setLogOutButton()
    {
        logOutButton = getLogOutButton();
        logOutButton.setText(AppConstants.LOGOUT);
        logOutButton.setVisibility(View.VISIBLE);
    }

    private void logOut()
    {
        appSession.clearSession();
        resetUserInfo();
    }

    private void resetUserInfo(){
        userName.setText(AppConstants.EMPTY);
        userName.setVisibility(View.INVISIBLE);
        logOutButton.setVisibility(View.INVISIBLE);
    }

    private Button getLogOutButton(){
        return (Button) findViewById(R.id.logOut);
    }

    class ButtonHandler implements View.OnClickListener{

        @Override
        public void onClick(View view){
            Intent intent = null;
            logOut();
            intent = new Intent(context, MainActivity.class);
            startActivity(intent);
        }
    }

}
