package io.github.wong1988.kit.entity;

import java.util.Collections;
import java.util.List;

/**
 * Android通讯录联系人信息
 */
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
    private final ContactPinYinInfo contactPinYinInfo;

    public ContactInfo(String name, ContactPinYinInfo contactPinYinInfo) {

        if (name == null)
            name = "";
        this.name = name;

        if (contactPinYinInfo == null)
            contactPinYinInfo = new ContactPinYinInfo(null, null, null);

        this.contactPinYinInfo = contactPinYinInfo;
    }

    public String getName() {
        return name;
    }

    public ContactPinYinInfo getContactPinYin() {
        return contactPinYinInfo;
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
                ", contactPinYinInfo=" + contactPinYinInfo +
                '}';
    }
}
