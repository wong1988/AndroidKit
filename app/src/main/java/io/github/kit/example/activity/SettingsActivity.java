package io.github.kit.example.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import io.github.kit.example.R;
import io.github.wong1988.kit.receiver.WifiApStateReceiver;
import io.github.wong1988.kit.task.WifiApAddress;
import io.github.wong1988.kit.utils.SettingsUtils;

public class SettingsActivity extends AppCompatActivity {

    // 地址1是否可用
    private boolean address1;
    // 地址2是否可用
    private boolean address2;
    // 热点是否开启
    private boolean wifiApEnable;

    private TextView tv;
    private ImageView iv;
    private WifiApStateReceiver wifiApStateReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        address1 = SettingsUtils.isWifiApActivityExists(WifiApAddress.NORMAL);
        address2 = SettingsUtils.isWifiApActivityExists(WifiApAddress.SPECIAL);

        tv = findViewById(R.id.tv);
        iv = findViewById(R.id.iv);

        initText();

        wifiApStateReceiver = new WifiApStateReceiver(new WifiApStateReceiver.WifiApStateListener() {
            @Override
            public void state(boolean isOpen) {
                if (wifiApEnable != isOpen) {
                    wifiApEnable = isOpen;
                    initText();
                }
            }
        });
        wifiApStateReceiver.registerReceiver();
    }


    public void initText() {

        if (wifiApEnable) {
            iv.setVisibility(View.GONE);
            tv.setVisibility(View.GONE);
        } else {
            // 热点不可用

            iv.setOnClickListener(null);
            tv.setVisibility(View.VISIBLE);

            SpannableStringBuilder spannable = new SpannableStringBuilder();

            if (address1 && address2) {
                spannable.append("创建热点");
                spannable.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View view) {
                        SettingsUtils.startWifiApActivity(SettingsActivity.this, WifiApAddress.NORMAL, new SettingsUtils.SettingsActivityNotFoundException() {
                            @Override
                            public void notFound() {
                                Toast.makeText(SettingsActivity.this, "打开设置页面失败，请手动创建热点", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }, 0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.setSpan(new ForegroundColorSpan(Color.GREEN), 0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.append("  开始传输");
                iv.setVisibility(View.VISIBLE);
                // 弹窗是 提示使用另一地址
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder b = new AlertDialog.Builder(SettingsActivity.this);//this为上下文，如果在本类里显示，通常使用this
                        b.setTitle("温馨提醒");
                        b.setMessage("如果点击 创建热点 后，页面没有开启热点选项，可尝试使用方式二或手动使用系统设置开启。");
                        b.setNegativeButton("方式二", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SettingsUtils.startWifiApActivity(SettingsActivity.this, WifiApAddress.SPECIAL, new SettingsUtils.SettingsActivityNotFoundException() {
                                    @Override
                                    public void notFound() {
                                        Toast.makeText(SettingsActivity.this, "打开设置页面失败，请手动创建热点", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                        b.setPositiveButton("我知道了", null);
                        b.show();
                    }
                });
            } else if (!address1 && !address2) {
                // 手动开启
                spannable.append("请手动创建热点");
                spannable.setSpan(new ForegroundColorSpan(Color.GRAY), 0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.append("  开始传输");
                iv.setVisibility(View.GONE);
            } else {
                spannable.append("创建热点");
                spannable.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View view) {
                        SettingsUtils.startWifiApActivity(SettingsActivity.this, address1 ? WifiApAddress.NORMAL : WifiApAddress.SPECIAL, new SettingsUtils.SettingsActivityNotFoundException() {
                            @Override
                            public void notFound() {
                                Toast.makeText(SettingsActivity.this, "打开设置页面失败，请手动创建热点", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }, 0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.setSpan(new ForegroundColorSpan(Color.GREEN), 0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.append("  开始传输");
                iv.setVisibility(View.VISIBLE);
                // 弹窗是 手动创建热点
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder b = new AlertDialog.Builder(SettingsActivity.this);//this为上下文，如果在本类里显示，通常使用this
                        b.setTitle("温馨提醒");
                        b.setMessage("如果点击 创建热点 后，页面没有开启热点选项，请去系统设置去开启。");
                        b.setPositiveButton("我知道了", null);
                        b.show();
                    }
                });
            }

            tv.setText(spannable);
            tv.setMovementMethod(LinkMovementMethod.getInstance());

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wifiApStateReceiver != null)
            wifiApStateReceiver.unregisterReceiver();
    }
}