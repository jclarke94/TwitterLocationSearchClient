package com.servedonline.bellandroidassignment.activities.Entity;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private long id;
    private int followers_count, friends_count, listed_count, favorites_count, statuses_count;
    private String name, screen_name, location, description, url, created_at, lang, profile_image_url, profile_image_url_https;
    private Entities entities;
    private boolean geo_enabled, verified;

    protected User(Parcel in) {
        id = in.readLong();
        followers_count = in.readInt();
        friends_count = in.readInt();
        listed_count = in.readInt();
        favorites_count = in.readInt();
        statuses_count = in.readInt();
        name = in.readString();
        screen_name = in.readString();
        location = in.readString();
        description = in.readString();
        url = in.readString();
        created_at = in.readString();
        lang = in.readString();
        profile_image_url = in.readString();
        profile_image_url_https = in.readString();
        geo_enabled = in.readByte() != 0;
        verified = in.readByte() != 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public int getStatuses_count() {
        return statuses_count;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getFollowers_count() {
        return followers_count;
    }

    public void setFollowers_count(int followers_count) {
        this.followers_count = followers_count;
    }

    public int getFriends_count() {
        return friends_count;
    }

    public void setFriends_count(int friends_count) {
        this.friends_count = friends_count;
    }

    public int getListed_count() {
        return listed_count;
    }

    public void setListed_count(int listed_count) {
        this.listed_count = listed_count;
    }

    public int getFavorites_count() {
        return favorites_count;
    }

    public void setFavorites_count(int favorites_count) {
        this.favorites_count = favorites_count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getProfile_image_url_https() {
        return profile_image_url_https;
    }

    public void setProfile_image_url_https(String profile_image_url_https) {
        this.profile_image_url_https = profile_image_url_https;
    }

    public Entities getEntities() {
        return entities;
    }

    public void setEntities(Entities entities) {
        this.entities = entities;
    }

    public boolean isGeo_enabled() {
        return geo_enabled;
    }

    public void setGeo_enabled(boolean geo_enabled) {
        this.geo_enabled = geo_enabled;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeInt(followers_count);
        parcel.writeInt(friends_count);
        parcel.writeInt(listed_count);
        parcel.writeInt(favorites_count);
        parcel.writeInt(statuses_count);
        parcel.writeString(name);
        parcel.writeString(screen_name);
        parcel.writeString(location);
        parcel.writeString(description);
        parcel.writeString(url);
        parcel.writeString(created_at);
        parcel.writeString(lang);
        parcel.writeString(profile_image_url);
        parcel.writeString(profile_image_url_https);
        parcel.writeByte((byte) (geo_enabled ? 1 : 0));
        parcel.writeByte((byte) (verified ? 1 : 0));
    }
}
