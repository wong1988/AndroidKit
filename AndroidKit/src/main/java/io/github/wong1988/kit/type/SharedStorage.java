package io.github.wong1988.kit.type;

import android.os.Environment;

public enum SharedStorage {

    DIRECTORY_PICTURES(Environment.DIRECTORY_PICTURES);

    public String type;

    SharedStorage(String type) {
        this.type = type;
    }

}
