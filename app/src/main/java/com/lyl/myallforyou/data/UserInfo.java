package com.lyl.myallforyou.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lyl on 2017/4/18.
 */
public class UserInfo implements Parcelable {

    private String objid;

    private String uuid;

    private String name;

    private String sign;

    private String icon;


    public String getObjid() {
        return objid;
    }


    public void setObjid(String objid) {
        this.objid = objid;
    }


    public String getUuid() {
        return uuid;
    }


    public void setUuid(String uuid) {
        this.uuid = uuid;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getSign() {
        return sign;
    }


    public void setSign(String sign) {
        this.sign = sign;
    }


    public String getIcon() {
        return icon;
    }


    public void setIcon(String icon) {
        this.icon = icon;
    }


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.objid);
        dest.writeString(this.uuid);
        dest.writeString(this.name);
        dest.writeString(this.sign);
        dest.writeString(this.icon);
    }


    public UserInfo() {
    }


    protected UserInfo(Parcel in) {
        this.objid = in.readString();
        this.uuid = in.readString();
        this.name = in.readString();
        this.sign = in.readString();
        this.icon = in.readString();
    }


    public static final Parcelable.Creator<UserInfo> CREATOR = new Parcelable.Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel source) {
            return new UserInfo(source);
        }


        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
}
