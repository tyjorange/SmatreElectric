package com.base.library.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;

/**
 * 专门用来个textview显示html的工具类
 *
 * 使用方法:
 * struct();
 * textView.setText(Html.fromHtml(content, getImgGerrer(context), null));
 */

public class TextViewHtmlUtil {
    /**
     * 用来给textview设置html，新建一个线程，
     * */
    public void setTextViewHtmlForRunnable(final Context context, final String html, final TextView textView){
        textView.post(new Runnable() {
            @Override
            public void run() {
                textView.setText(getImgGerrer(context,html));
            }
        });
    }
    /**
     * 用来给textview显示，不带线程
     * */
    public  Spanned getImgGerrer(final Context context, String html){
        struct();
        Spanned sp = Html.fromHtml(html, new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                InputStream is = null;
                try {
                    is = (InputStream) new URL(source).getContent();
                    //Drawable d = Drawable.createFromStream(is, "src");
                    Drawable d=Drawable.createFromResourceStream
                            (context.getResources(), null, is, "src", null);

                    pictureMagnify(context,d);
                    is.close();
                    return d;
                } catch (Exception e) {
                    return null;
                }
            }
        }, null);

        return sp;
    }

    /**
     * 对试卷中的图片进行处理
     * */
    public static void pictureMagnify(Context context, Drawable drawable){
        int Screenwidth=ScreenUtils.getScreenWidth();
        int dpi= (int)ScreenUtils.getScreenDensity();//屏幕dpi
        double picPxZoom=(double)dpi/(double)160;//dpi和像素的差值比
        int picWidth=(int)(drawable.getIntrinsicWidth()*picPxZoom);//xp转换成dpi
        int picHeight=(int)(drawable.getIntrinsicHeight()*picPxZoom);//xp转换成dpi

        double zoom = (double) picWidth/(double) Screenwidth;//图片跟屏幕的比值
        Log.d("qiuqiu",Screenwidth+"--"+drawable.getIntrinsicWidth()+"--"+zoom);
        if(picWidth>=Screenwidth){
            drawable.setBounds(0, 0, (int)(picWidth/zoom) ,
                    (int)(picHeight/zoom));//全屏
        }else{
            drawable.setBounds(0, 0, picWidth ,picHeight);//转换成dpi之后显示
        }
        Log.d("qiuqiu","Screenwidth="+Screenwidth+"--picPxZoom="+picPxZoom+"--picWidth="+picWidth
        +"--picHeight="+picHeight+"--zoom="+zoom+"--dpi="+dpi);
    }

    private  void struct() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork() // or
                // .detectAll()
                // for
                // all
                // detectable
                // problems
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects() // 探测SQLite数据库操作
                .penaltyLog() // 打印logcat
                .penaltyDeath().build());
    }
}
