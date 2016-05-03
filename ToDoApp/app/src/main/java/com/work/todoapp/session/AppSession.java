package com.work.todoapp.session;

import android.content.SharedPreferences;
import android.util.Log;

import com.work.todoapp.utils.AppConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by shivendrasrivastava on 4/24/16.
 */
public class AppSession {

    private String username;

    private String email;

    private SharedPreferences sharedPreferences;

    private static AppSession appSession = null;

    private Map<String, List<String>> todoMap = new HashMap<String, List<String>>();

    public static AppSession getAppSessionInstance(){
        if (null == appSession )
        {
            appSession = new AppSession();
        }
        return appSession;
    }

    private AppSession()
    {

    }

    public String getUsername() {
        return username;
    }

    private void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    private void setEmail(String email) {
        this.email = email;
    }


    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    private void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public Boolean isUserLoggedIn()
    {
        if (null != getUsername() && null != getEmail())
        {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public void setSessionUser(SharedPreferences sharedPreferences, String username, String email)
    {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        if (null == username)
        {
            Set<String> userdetails = sharedPreferences.getStringSet(email, null);
            for (String details : userdetails)
            {
                if (details.contains(AppConstants.username_key))
                {
                    username = details.substring(AppConstants.username_key.length(), details.length());
                }
            }
        }else
        {
            username = username.substring(AppConstants.username_key.length(), username.length());
        }
        Log.w("APP SESSION", "User name being set is " + username);
        Log.w("APP SESSION", "Email being set is "+email);
        setUsername(username);
        setEmail(email);
        setSharedPreferences(sharedPreferences);

        edit.putStringSet(AppConstants.SESSION_USER, new HashSet(Arrays.asList(username, email)));
        edit.apply();
    }

    public void clearSession()
    {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.remove(AppConstants.SESSION_USER);
        edit.apply();
    }

    public void setTodoItemInList(String todoItem, String username)
    {
        if (null != todoItem && null != username)
        {
            if (todoMap.containsKey(username))
            {
                List<String> todoList = todoMap.get(username);
                todoList.add(todoItem);
            }
            else
            {
                List<String> todoList = new ArrayList<String>();
                todoList.add(todoItem);
                todoMap.put(username, todoList);
            }
            if (null != sharedPreferences)
            {
                SharedPreferences.Editor edit = getSharedPreferencesEditor();
                List<String> todoList = todoMap.get(username);
                Set<String> todoSet = new HashSet<String>();
                todoSet.addAll(todoList);
                edit.putStringSet(username, todoSet);
                edit.apply();
            }
        }

    }

    public void setTodoDetails(String todoItem, String details)
    {
        if (null != todoItem && null != details )
        {
            if (null != sharedPreferences)
            {
                SharedPreferences.Editor edit = getSharedPreferencesEditor();
                edit.putString(getUsername()+":"+todoItem, details);
                edit.apply();
            }
        }
    }

    public String getTodoDetails(String todoItem)
    {
        if (null != todoItem && !todoItem.isEmpty())
        {
            if (null != sharedPreferences)
            {
                return sharedPreferences.getString(getUsername()+":"+todoItem, "");
            }
        }
        return null;
    }

    public void updateTodoList(List<String> newTodoList, String username)
    {
        if (null != username && null != newTodoList){
            if(todoMap.containsKey(username))
            {
                todoMap.put(username, newTodoList);
                if (null != sharedPreferences)
                {
                    SharedPreferences.Editor edit = getSharedPreferencesEditor();
                    Set<String> todoSet = new HashSet<String>();
                    todoSet.addAll(newTodoList);
                    edit.putStringSet(username, todoSet);
                    edit.apply();
                }
            }
        }
    }

    public List<String> getToDoList(String username)
    {
        if (null != username){
            if (null != sharedPreferences){
                Set<String> todoSet = sharedPreferences.getStringSet(username, null);
                List<String> todoList = new ArrayList<String>();
                if (null != todoSet)
                {
                    todoList.addAll(todoSet);
                }
                return todoList;
            }
            else if (!todoMap.isEmpty())
            {
                if (todoMap.containsKey(username))
                {
                    return todoMap.get(username);
                }
            }
        }
        return null;
    }

    private SharedPreferences.Editor getSharedPreferencesEditor()
    {
        if (null != sharedPreferences){
            return sharedPreferences.edit();
        }
        return null;
    }

}
