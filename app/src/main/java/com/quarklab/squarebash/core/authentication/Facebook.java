package com.quarklab.squarebash.core.authentication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.quarklab.squarebash.GameBoard;
import com.quarklab.squarebash.SquareBash;
import com.quarklab.squarebash.core.http.SquareBashAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * Created by rudy on 7/9/16.
 */
public class Facebook {
    private List<String> permissionNeeds= Arrays.asList("user_photos", "email", "user_birthday", "user_friends");
    private Context context;
    public void authenticate(final Context context){
        this.context = context;
        ((SquareBash)this.context).callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions((Activity)this.context, this.permissionNeeds);
        LoginManager.getInstance().registerCallback(((SquareBash)this.context).callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        if(!((SquareBash)context).setting.isFacebookAccountExists()) {
                            getCurrentUserInfo(loginResult);
                        }else{
                            ((SquareBash)context).displayGameBoard();
                        }
                    }

                    @Override
                    public void onCancel() {
                        Log.e("dd","facebook login canceled");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.e("dd", "facebook login failed error");
                    }
                });
    }
    private void getCurrentUserInfo(LoginResult loginResult){
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        String facebookAccount = object.toString();
                        JSONObject x = SquareBashAPI.post("/player/save",facebookAccount);
                        try {
                            if(x.has("save") && x.getBoolean("save")){
                                ((SquareBash)context).displayGameBoard();
                                ((SquareBash)context).setting.updateFacebookAccount(facebookAccount);
                            }else if(x.has("errors") && x.getString("name").equals("SequelizeUniqueConstraintError")){
                                if(!((SquareBash)context).setting.isFacebookAccountExists()) {
                                    ((SquareBash)context).setting.updateFacebookAccount(facebookAccount);
                                }
                                ((SquareBash)context).displayGameBoard();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender, birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }
}
