package com.lyl.myallforyou.data;

import com.litesuits.orm.db.annotation.NotNull;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by lyl on 2017/4/18.
 */
@Table("user_info")
public class UserInfo {

    // 指定自增，每个对象需要有一个主键
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;

    private String object_id;

    @NotNull
    private String my_id;

    private String family_id;

    private String family_name;

    private String family_sign;

    private String my_name;

    private String my_icon;


    public String getFamily_name() {
        return family_name;
    }


    public void setFamily_name(String family_name) {
        this.family_name = family_name;
    }


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getFamily_sign() {
        return family_sign;
    }


    public void setFamily_sign(String family_sign) {
        this.family_sign = family_sign;
    }


    public String getMy_name() {
        return my_name;
    }


    public void setMy_name(String my_name) {
        this.my_name = my_name;
    }


    public String getMy_icon() {
        return my_icon;
    }


    public void setMy_icon(String my_icon) {
        this.my_icon = my_icon;
    }


    public String getMy_id() {
        return my_id;
    }


    public void setMy_id(String my_id) {
        this.my_id = my_id;
    }


    public String getFamily_id() {
        return family_id;
    }


    public void setFamily_id(String family_id) {
        this.family_id = family_id;
    }


    public String getObject_id() {
        return object_id;
    }


    public void setObject_id(String object_id) {
        this.object_id = object_id;
    }
}
