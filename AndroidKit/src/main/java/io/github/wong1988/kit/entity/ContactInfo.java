package io.github.wong1988.kit.entity;

import java.util.Collections;
import java.util.List;

public class ContactInfo {

    // 基础字段
    private final String name;
    private String phone;
    // 附加字段
    // 电话号码相同的字符
    private String phoneSameChar;
    // 姓名相同的字符下标（因为有可能会跳跃特殊字符，所以下标返回）
    private List<Integer> nameSameChar;
    // 特殊字段
    private final ContactPinYin contactPinYin;

    public ContactInfo(String name, ContactPinYin contactPinYin) {

        if (name == null)
            name = "";
        this.name = name;

        if (contactPinYin == null)
            contactPinYin = new ContactPinYin(null, null, null);

        this.contactPinYin = contactPinYin;
    }

    public String getName() {
        return name;
    }

    public ContactPinYin getContactPinYin() {
        return contactPinYin;
    }

    public String getPhone() {
        return phone == null ? "" : phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneSameChar() {
        return phoneSameChar == null ? "" : phoneSameChar;
    }

    public void setPhoneSameChar(String phoneSameChar) {
        this.phoneSameChar = phoneSameChar;
    }

    public List<Integer> getNameSameChar() {
        return nameSameChar == null ? Collections.EMPTY_LIST : nameSameChar;
    }

    public void setNameSameChar(List<Integer> nameSameChar) {
        this.nameSameChar = nameSameChar;
    }

    @Override
    public String toString() {
        return "ContactInfo{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", phoneSameChar='" + phoneSameChar + '\'' +
                ", nameSameChar=" + nameSameChar +
                ", contactPinYin=" + contactPinYin +
                '}';
    }
}
