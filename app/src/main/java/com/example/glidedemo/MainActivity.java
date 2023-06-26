package com.example.glidedemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import cn.gd.snm.annotation.BindView;
import cn.gd.snm.annotation.CustomButterKnife;
import cn.gd.snm.annotation.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.iv_8888)
    ImageView iv8888;
    @BindView(R.id.tv_8888_size)
    TextView tv8888;
    @BindView(R.id.iv_565)
    ImageView iv565;
    @BindView(R.id.tv_565_size)
    TextView tv565;

    //    private String url = "http://thirdqq.qlogo.cn/g?b=oidb&k=xy8mfLIyJN7LM5es7eQhxw&s=640&t=1600761975";
    private String url = "https://img2.baidu.com/it/u=3796832385,1217813110&fm=253&fmt=auto&app=138&f=JPEG?w=546&h=500";

    @OnClick({R.id.iv_8888, R.id.tv_8888_size, R.id.iv_565, R.id.tv_565_size})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_8888:
                Toast.makeText(this, "iv8888", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_8888_size:
                Toast.makeText(this, "tv8888", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_565:
                Toast.makeText(this, "iv565", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_565_size:
                Toast.makeText(this, "tv565", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        iv565 = findViewById(R.id.iv_565);
//        tv565 = findViewById(R.id.tv_565_size);
//        iv8888 = findViewById(R.id.iv_8888);
//        tv8888 = findViewById(R.id.tv_8888_size);

        CustomButterKnife.bind(this);

        Glide.with(this).asBitmap()
                .load(url)
                .format(DecodeFormat.PREFER_ARGB_8888)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        int byteCount = resource.getByteCount();
                        int rowBytes = resource.getRowBytes();
                        iv8888.setImageBitmap(resource);
                        tv8888.setText("图片大小 = " + byteCount + ",rowBytes = " + rowBytes);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });

        Glide.with(this).asBitmap()
                .load(url)
                .format(DecodeFormat.PREFER_RGB_565)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        int byteCount = resource.getByteCount();
                        int rowBytes = resource.getRowBytes();
                        iv565.setImageBitmap(resource);
                        tv565.setText("图片大小 = " + byteCount + ",rowBytes = " + rowBytes);
//                        tv565.setText("图片大小 = " + byteCount);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }
}