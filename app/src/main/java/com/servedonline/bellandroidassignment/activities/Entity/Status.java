package com.servedonline.bellandroidassignment.activities.Entity;


import android.os.Parcel;
import android.os.Parcelable;

public class Status implements Parcelable {

    private String created_at, id_str, text, source, lang;
    private Coordinates coordinates;
    private long id;
    private int retweet_count, favorite_count;
    private Entities entities;
    private User user;
    private Place place;

    protected Status(Parcel in) {
        created_at = in.readString();
        id_str = in.readString();
        text = in.readString();
        source = in.readString();
        lang = in.readString();
        id = in.readLong();
        retweet_count = in.readInt();
        favorite_count = in.readInt();
    }

    public static final Creator<Status> CREATOR = new Creator<Status>() {
        @Override
        public Status createFromParcel(Parcel in) {
            return new Status(in);
        }

        @Override
        public Status[] newArray(int size) {
            return new Status[size];
        }
    };

    public Place getPlace() {
        return place;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getId_str() {
        return id_str;
    }

    public void setId_str(String id_str) {
        this.id_str = id_str;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRetweet_count() {
        return retweet_count;
    }

    public void setRetweet_count(int retweet_count) {
        this.retweet_count = retweet_count;
    }

    public int getFavorite_count() {
        return favorite_count;
    }

    public void setFavorite_count(int favorite_count) {
        this.favorite_count = favorite_count;
    }

    public Entities getEntities() {
        return entities;
    }

    public void setEntities(Entities entities) {
        this.entities = entities;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(created_at);
        parcel.writeString(id_str);
        parcel.writeString(text);
        parcel.writeString(source);
        parcel.writeString(lang);
        parcel.writeLong(id);
        parcel.writeInt(retweet_count);
        parcel.writeInt(favorite_count);
    }
}
