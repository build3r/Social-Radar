package com.builder.devconnect.model;

import com.parse.ParseObject;

import java.io.Serializable;

/**
 * Created by mrven on 16-11-2017.
 */

public class ProfileVo implements Serializable {
    String name;
    String email;
    String shortBio;
    String profilePicURL;
    String linkedinURL;
    String fbURL;
    String twitterURL;

    public ProfileVo() {
    }

    public ProfileVo(String name, String email, String shortBio, String profilePicURL, String linkedinURL, String fbURL, String twitterURL) {
        this.name = name;
        this.email = email;
        this.shortBio = shortBio;
        this.profilePicURL = profilePicURL;
        this.linkedinURL = linkedinURL;
        this.fbURL = fbURL;
        this.twitterURL = twitterURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getShortBio() {
        return shortBio;
    }

    public void setShortBio(String shortBio) {
        this.shortBio = shortBio;
    }

    public String getProfilePicURL() {
        return profilePicURL;
    }

    public void setProfilePicURL(String profilePicURL) {
        this.profilePicURL = profilePicURL;
    }

    public String getLinkedinURL() {
        return linkedinURL;
    }

    public void setLinkedinURL(String linkedinURL) {
        this.linkedinURL = linkedinURL;
    }

    public String getFbURL() {
        return fbURL;
    }

    public void setFbURL(String fbURL) {
        this.fbURL = fbURL;
    }

    public String getTwitterURL() {
        return twitterURL;
    }

    public void setTwitterURL(String twitterURL) {
        this.twitterURL = twitterURL;
    }

    public static ProfileVo castToProfileVo(ParseObject object) {
        return new ProfileVo(object.getString("name"),
                object.getString("email"), object.getString("short_bio"),
                object.getString("profile_pic_url"), object.getString("linkedin_url"),
                object.getString("fb_url"), object.getString("twitter_url"));
    }
    @Override
    public String toString() {
        return "ProfileVo{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", shortBio='" + shortBio + '\'' +
                ", profilePicURL='" + profilePicURL + '\'' +
                ", linkedinURL='" + linkedinURL + '\'' +
                ", fbURL='" + fbURL + '\'' +
                ", twitterURL='" + twitterURL + '\'' +
                '}';
    }
}
