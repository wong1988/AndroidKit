package io.github.wong1988.kit.utils;

import android.Manifest;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;

import androidx.annotation.RequiresPermission;

import com.github.promeg.pinyinhelper.Pinyin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.github.wong1988.kit.entity.ContactCompareInfo;
import io.github.wong1988.kit.entity.ContactInfo;
import io.github.wong1988.kit.entity.ContactPinYinInfo;
import io.github.wong1988.kit.lexicons.PolyphonicDict;

public class ContactsUtils {

    /**
     * 获取通讯录，需要读取联系人权限
     */
    @RequiresPermission(allOf = {Manifest.permission.READ_CONTACTS})
    public static List<ContactInfo> getContacts(Context context) {

        List<ContactInfo> list = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, "display_name COLLATE LOCALIZED");

        if (cursor == null || cursor.getCount() == 0) {
            return list;
        }

        // 加载识别多音字的字库，仅加载一次，否则很耗时(耗时约100-200ms)
        Pinyin.init(Pinyin.newConfig().with(PolyphonicDict.getInstance(context)));

        // 联系人拼音
        ContactPinYinInfo temp = new ContactPinYinInfo(null, null, null);

        while (cursor.moveToNext()) {

            // 获取联系人姓名
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            if (name == null)
                name = "";

            // 获取联系人手机号
            String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            if (phone == null)
                phone = "";
            // 去除空格
            phone = phone.replace(" ", "");

            if (!name.equals(temp.getOriginalText()))
                temp = convert2SearchPinYin(name);

            // 新建一个联系人实例
            ContactInfo contact = new ContactInfo(name, temp);
            contact.setPhone(phone);

            list.add(contact);
        }

        cursor.close();

        return list;
    }

    // 联系人转可搜索的拼音
    private static ContactPinYinInfo convert2SearchPinYin(String name) {

        if (name == null)
            return new ContactPinYinInfo(null, null, null);

        // 原文转换成数组
        char[] charArr = name.toCharArray();

        List<List<String>> pinyin = new ArrayList<>();

        List<Integer> indexes = new ArrayList<>();

        for (int i = 0; i < charArr.length; i++) {
            // 当前循环的字符
            char c = charArr[i];
            if (Pinyin.isChinese(c)) {
                // 中文字符
                String s = Pinyin.toPinyin(String.valueOf(c), "#");
                // 分割文字的所有读法
                String[] split = s.split("#");
                for (int j = 0; j < split.length; j++) {
                    // 循环取出读音
                    String pronunciation = split[j];
                    if (pronunciation.length() > 1) {
                        // 首字母大写，其余小写
                        split[j] = Character.toUpperCase(pronunciation.charAt(0)) + pronunciation.substring(1).toLowerCase();
                    } else {
                        // 转大写
                        split[j] = pronunciation.toUpperCase();
                    }
                }
                // 存入当前字符的读音集合
                pinyin.add(Arrays.asList(split));
                // 存入符合字符的下标
                indexes.add(i);
            } else {
                boolean matches = RegexUtils.onlyEnglishAndNumbers(String.valueOf(c));
                if (matches) {
                    // 单个英文或数字统一按照转大写处理
                    pinyin.add(Collections.singletonList(String.valueOf(c).toUpperCase()));
                    // 存入符合字符的下标
                    indexes.add(i);
                }
            }
        }
        // 返回可检索的实体类（拼音检索仅支持数字、汉字、英文，其他字符一律被剔除检索范围）
        return new ContactPinYinInfo(name, pinyin, indexes);
    }

    /**
     * 根据关键字搜索联系人，支持拼音搜索
     * 支持规则：
     * 1. 关键字的特殊字符会被忽略
     * 2. 王BA*#b 支持精准搜索并忽略大小写、忽略特殊字符，如 王Ba=b
     * 3. 王BA*#b 支持拼音比对（忽略特殊字符，汉字拼音搜索需要首字母开头）,wba、waba、wanba、wangba、wbab、bab、wba*、w**ba、angba(搜索不到，汉字需以拼音首字母开始)
     */
    public static List<ContactInfo> searchContacts(List<ContactInfo> contacts, String keyWord) {

        if (contacts == null || contacts.size() == 0 || TextUtils.isEmpty(keyWord))
            return new ArrayList<>();

        List<ContactInfo> list = new ArrayList<>();

        for (int i = 0; i < contacts.size(); i++) {


            // 获取当前的check的联系人
            ContactInfo contactInfo = contacts.get(i);

            ContactPinYinInfo contactPinYin = contactInfo.getContactPinYinInfo();
            String phone = contactInfo.getPhone();

            // 获取比对结果
            ContactCompareInfo contactCompareInfo = getSearchPinYinResult(contactPinYin, keyWord);

            if (TextUtils.isEmpty(contactCompareInfo.getTargetValue())) {
                // 与姓名无相同，检查是否与手机号相关
                if (phone.contains(keyWord)) {
                    ContactInfo findContact = new ContactInfo(contactInfo.getName(), contactInfo.getContactPinYinInfo());
                    findContact.setPhone(phone);
                    findContact.setPhoneSameChar(keyWord);
                    list.add(findContact);
                }
            } else {
                ContactInfo findContact = new ContactInfo(contactInfo.getName(), contactInfo.getContactPinYinInfo());
                findContact.setPhone(phone);
                findContact.setNameSameChar(contactCompareInfo.getTargetIndexes());
                // 检查是否与手机号相关
                if (phone.contains(keyWord)) {
                    findContact.setPhoneSameChar(keyWord);
                }
                list.add(findContact);
            }
        }

        return list;

    }

    // 获取拼音检索的结果
    private static ContactCompareInfo getSearchPinYinResult(ContactPinYinInfo contactPinYinInfo, String keyWord) {

        if (contactPinYinInfo == null)
            return new ContactCompareInfo();

        // 原文（可以携带特殊字符）
        String originalText = contactPinYinInfo.getOriginalText();

        // 关键字,忽略特殊字符
        keyWord = RegexUtils.removeSpecialChars(keyWord);

        if (TextUtils.isEmpty(originalText) || TextUtils.isEmpty(keyWord))
            // 都空无需比对
            return new ContactCompareInfo();

        // 先进行精确比对,忽略大小写
        String keyWordLower = keyWord.toLowerCase();
        // 精确对比的下标
        int startIndex = RegexUtils.removeSpecialChars(originalText).toLowerCase().indexOf(keyWordLower);
        if (startIndex >= 0) {
            List<Integer> indexes = new ArrayList<>();
            indexes.add(startIndex);
            StringBuilder buffer = new StringBuilder(String.valueOf(originalText.charAt(startIndex)));

            int temp = startIndex;
            String tempOriginalText = originalText.toLowerCase();
            for (int i = 1; i < keyWordLower.length(); i++) {
                String c = String.valueOf(keyWordLower.charAt(i));
                int e = tempOriginalText.indexOf(c, temp);
                indexes.add(e);
                buffer.append(originalText.charAt(e));
                temp = e;
            }
            return new ContactCompareInfo(buffer.toString(), indexes);
        }

        // 开始进行拼音比对
        // 获取所有可能的组合【主要是应对多音字处理】
        String[] pinYinCombination = contactPinYinInfo.getPinYinCombination();

        if (pinYinCombination.length < 1) {
            // 无法进行拼音比对，应该使用忽略大小写比对，上方代码已进行了非拼音比对
            // 进入此方法代表非拼音比对失败
            return new ContactCompareInfo();
        } else {

            // 验证组合是否对应关键字

            // 关键字对应的下标集合
            List<Integer> indexes = null;
            // 关键字对应的某个组合
            String pinYinResult = "";

            // 进行比对
            for (String pinYinAll : pinYinCombination) {
                List<Integer> temp = getCorrectIndexInPinyin(pinYinAll, keyWordLower, 0);
                if (temp != null && temp.size() > 0) {
                    // 符合跳出循环
                    indexes = temp;
                    pinYinResult = pinYinAll;
                    break;
                }
            }

            if (indexes == null) {
                return new ContactCompareInfo();
            } else {

                // 获取关键字对应的首个下标
                int start = indexes.get(0);

                char[] chars = pinYinResult.toCharArray();

                // 获取首个关键字下标对应的符合搜索的原文，前面跳过了几个字符
                int realStart = 0;
                for (int i = 0; i < start; i++) {
                    char aChar = chars[i];

                    boolean matches1 = RegexUtils.onlyUppercaseAndNumbers(String.valueOf(aChar));

                    if (matches1)
                        realStart++;
                }

                // 获取最后一个关键字下标对应的符合搜索的原文，结束时符合搜索原文的下标
                int realEnd = realStart;

                for (int i = 0; i < indexes.size(); i++) {
                    Integer index = indexes.get(i);
                    char aChar = chars[index];

                    boolean matches1 = RegexUtils.onlyUppercaseAndNumbers(String.valueOf(aChar));

                    if (matches1)
                        realEnd++;
                }

                List<Integer> targetIndexes = contactPinYinInfo.getFitCharIndexes().subList(realStart, realEnd);
                StringBuffer buffer = new StringBuffer();
                for (int i = 0; i < targetIndexes.size(); i++) {
                    buffer.append(originalText.charAt(targetIndexes.get(i)));
                }
                return new ContactCompareInfo(buffer.toString(), targetIndexes);
            }
        }
    }


    // 获取正确的下标
    private static List<Integer> getCorrectIndexInPinyin(String pinYinAll, String keyWordLower, int index) {

        // 关键字首个字符出现的位置
        // 大于剩余的检查字符长度无需检查了
        if (index > pinYinAll.length() - keyWordLower.length()) {
            return Collections.EMPTY_LIST;
        }

        // 对应的下标集合
        List<Integer> integers = new ArrayList<>();

        String pinYinAllLower = pinYinAll.toLowerCase();

        // 关键字转字符数组
        char[] lowerKeyChars = keyWordLower.toCharArray();

        int i1 = -1;

        int firstCharIndexOf = -1;

        // 循环关键字
        for (int i = 0; i < lowerKeyChars.length; i++) {

            if (i == 0) {
                // 首字母用index去检索[忽略大小写]
                i1 = pinYinAllLower.indexOf(String.valueOf(lowerKeyChars[0]), index);
                // 首字母出现的位置
                firstCharIndexOf = i1;

                if (i1 == -1)
                    return Collections.EMPTY_LIST;

            } else {
                // 非首字母根据上一个字母的index+1去检索
                i1 = pinYinAllLower.indexOf(String.valueOf(lowerKeyChars[i]), i1 + 1);
            }

            if (i1 == -1) {
                // 只要出现比对错误就清空集合
                integers.clear();
                // 重新开始循环关键字
                return getCorrectIndexInPinyin(pinYinAll, keyWordLower, firstCharIndexOf + 1);
            } else {
                // 关键字对应的位置添加到集合
                integers.add(i1);
            }
        }

        // 开始判断是否合规
        for (int j = 0; j < integers.size(); j++) {

            // 获取关键字对应的下表
            Integer charIndex = integers.get(j);

            if (j == 0) {
                // 首字母需要是大写字母或者数字
                boolean matches = RegexUtils.onlyUppercaseAndNumbers(String.valueOf(pinYinAll.charAt(charIndex)));

                if (!matches) {
                    integers.clear();
                    // 重新开始循环关键字
                    return getCorrectIndexInPinyin(pinYinAll, keyWordLower, charIndex + 1);
                }
            }

            // 关键字字符间进行检查
            // 上方已经判断首字符为符合要求的
            // 此处判断是否是连续的，非连续要求中间跳过的字符是小写，且无数字
            // 并且要求跳过的下一个字符需要是大写或者数字
            if (j < integers.size() - 1) {

                // 下一个字符的对应下表
                Integer charIndex2 = integers.get(j + 1);

                if (charIndex + 1 != charIndex2) {
                    // 非连续 判断中间跳过的字符是否都是小写并且无数字
                    String substring = pinYinAll.substring(charIndex + 1, charIndex2);
                    // 转换成小写不相等【说明中间有大写，证明非连续字符搜索】 或者 包含数字【说明非连续字符搜索】
                    if (!substring.equals(substring.toLowerCase()) || RegexUtils.containNum(substring)) {
                        integers.clear();
                        // 重新开始循环关键字
                        return getCorrectIndexInPinyin(pinYinAll, keyWordLower, firstCharIndexOf + 1);
                    }

                    // 并且判断下一个是否是大写或者数字
                    String substring2 = String.valueOf(pinYinAll.charAt(charIndex2));
                    // 既不是大写【证明可能是非连续搜索】也不是数字【证明可能是非连续搜索】不符合要求
                    if (!substring2.equals(substring2.toUpperCase()) && !RegexUtils.containNum(substring2)) {
                        integers.clear();
                        // 重新开始循环关键字
                        return getCorrectIndexInPinyin(pinYinAll, keyWordLower, firstCharIndexOf + 1);
                    }
                }
            }
        }

        return integers;
    }

}
