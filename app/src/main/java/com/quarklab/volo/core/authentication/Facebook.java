package com.quarklab.volo.core.authentication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.quarklab.volo.R;
import com.quarklab.volo.SquareBash;
import com.quarklab.volo.core.http.SquareBashAPI;

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
    private GraphRequest request;
    private CallbackManager callbackManager;
    private SquareBash squareBash;

    public void authenticate(final Context context){
        this.context = context;
        this.squareBash = ((SquareBash)this.context);
        this.squareBash.callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions((Activity)this.context, this.permissionNeeds);
        LoginManager.getInstance().registerCallback(this.squareBash.callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        squareBash.callbackManager = null;
                        if(!squareBash.setting.isFacebookAccountExists()) {
                            getCurrentUserInfo(loginResult);
                        }else{
                            squareBash.displayGameBoard();
                            squareBash = null;
                        }
                    }

                    @Override
                    public void onCancel() {
                        Log.e("dd","facebook login canceled");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        squareBash.displayGameBoard();
                        squareBash = null;
                        Log.e("dd", "facebook login failed error");
                    }
                });
    }
    private void getCurrentUserInfo(LoginResult loginResult){
        request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        String facebookAccount = object.toString();
                        request = null;
                        try {
                            JSONObject x = SquareBashAPI.postObject(context.getString(R.string.save_player_api),facebookAccount);
                            if(x.getBoolean("status")){
                                squareBash.displayGameBoard();
                                squareBash.setting.setFacebookId(object.getString("id"));
                                squareBash.setting.setToken(x.getString("token"));
                                squareBash.setting.updateFacebookAccount(facebookAccount);
                                squareBash = null;
                            }else{
                                if(!squareBash.setting.isFacebookAccountExists()) {
                                    squareBash.setting.setFacebookId(object.getString("id"));
                                    squareBash.setting.updateFacebookAccount(facebookAccount);
                                }
                                squareBash.displayGameBoard();
                                squareBash = null;
                            }
                        } catch (JSONException e) {
                            squareBash.displayGameBoard();
                            request = null;
                            squareBash = null;
                        } catch (Exception e){
                            squareBash.displayGameBoard();
                            request = null;
                            squareBash = null;
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender, birthday");
        this.request.setParameters(parameters);
        parameters = null;
        this.request.executeAsync();
    }
}
