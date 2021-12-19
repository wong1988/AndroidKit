package io.github.wong1988.kit.lexicons;

import android.content.Context;

import com.github.promeg.tinypinyin.android.asset.lexicons.AndroidAssetDict;

public class PolyphonicDict extends AndroidAssetDict {

    static volatile PolyphonicDict singleton = null;

    public PolyphonicDict(Context context) {
        super(context);
    }

    @Override
    protected String assetFileName() {
        return "polyphonic.txt";
    }

    public static PolyphonicDict getInstance(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context == null");
        }
        if (singleton == null) {
            synchronized (PolyphonicDict.class) {
                if (singleton == null) {
                    singleton = new PolyphonicDict(context);
                }
            }
        }
        return singleton;
    }
}
