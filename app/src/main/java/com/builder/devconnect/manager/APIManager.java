package com.builder.devconnect.manager;

import android.support.annotation.NonNull;
import android.util.Log;

import com.builder.devconnect.global.Constants;
import com.builder.devconnect.model.ProfileVo;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.parse.SendCallback;

public class APIManager {

    private static APIManager sAPIManager;

    private APIManager() {

    }

    public static APIManager newInstance() {
        if (sAPIManager == null) {
            sAPIManager = new APIManager();
        }
        return sAPIManager;
    }

    /*
    Needed to receive notifications
     */
    public void registerDevice() {
        final ParseInstallation parseInstallation = ParseInstallation.getCurrentInstallation();
        parseInstallation.put("GCMSenderId", "740117062175");
        if (SharedPreferenceManager.newInstance().getSavedValue(Constants.UserLocalData.USER_ID)
                != null) {
            parseInstallation.put("device_id",
                    SharedPreferenceManager.newInstance()
                            .getSavedValue(Constants.UserLocalData.USER_ID));
        }
        parseInstallation.saveInBackground();
    }

    /*
    Save user profile data
     */
    public void saveProfileData(ProfileVo profileData,
                                final Callback callback) {
        final ParseObject profileObj = new ParseObject("UserData");

        if (profileData.getName() != null)
            profileObj.put("name", profileData.getName());
        if (profileData.getEmail() != null)
            profileObj.put("email", profileData.getEmail().toLowerCase());
        if (profileData.getShortBio() != null)
            profileObj.put("short_bio", profileData.getShortBio());
        if (profileData.getProfilePicURL() != null)
            profileObj.put("profile_pic_url", profileData.getProfilePicURL());
        if (profileData.getLinkedinURL() != null)
            profileObj.put("linkedin_url", profileData.getLinkedinURL());
        if (profileData.getFbURL() != null)
            profileObj.put("fb_url", profileData.getFbURL());
        if (profileData.getTwitterURL() != null)
            profileObj.put("twitter_url", profileData.getTwitterURL());

        profileObj.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    callback.success(profileObj);
                } else {
                    callback.failure(e.toString());
                }
            }
        });
    }

    public void getProfileData(@NonNull String email, final Callback callback) {
        ParseQuery query = new ParseQuery("UserData");
        query.whereEqualTo("objectId", email.trim().toLowerCase());
        query.getFirstInBackground(new GetCallback() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    // Access the data here.
                    ProfileVo profileVo = new ProfileVo(object.getString("name"),
                            object.getString("email"), object.getString("short_bio"),
                            object.getString("profile_pic_url"), object.getString("linkedin_url"),
                            object.getString("fb_url"), object.getString("twitter_url"));
                    callback.getProfileData(profileVo);
                } else {
                    callback.failure(e.toString());
                }
            }

            @Override
            public void done(Object o, Throwable e) {
                if (e == null) {
                    callback.getProfileData(o);
                } else {
                    callback.failure(e.toString());
                }
            }
        });

    }


    public void sendPush(String userId, String message) {
        ParseQuery query = ParseInstallation.getQuery();
        query.whereEqualTo("device_id", userId);
        ParsePush push = new ParsePush();
        push.setQuery(query);
        push.setMessage(message);
        push.sendInBackground(new SendCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.d("tony", "Error: " + e.toString());
                }
            }
        });
    }

    public interface Callback {
        void success(Object obj);

        void getProfileData(Object p);

        void failure(String error);
    }

}
