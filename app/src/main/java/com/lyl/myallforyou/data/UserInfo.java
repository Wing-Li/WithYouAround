package com.lyl.myallforyou.data;

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.annotation.Unique;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by lyl on 2017/4/18.
 */
@Table("user_info")
public class UserInfo {

    // 指定自增，每个对象需要有一个主键
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;

    @Unique
    private String objid;

    @Unique
    private String uuid;

    private String name;

    private String nameNote;

    private String sign;

    private String icon;


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getNameNote() {
        return nameNote;
    }


    public void setNameNote(String nameNote) {
        this.nameNote = nameNote;
    }


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

}
