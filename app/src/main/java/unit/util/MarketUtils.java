package unit.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;



/**
 * Android应用商店相关工具类
 */
public class MarketUtils {

    public static void goScore(Context context) {
        goMarket(context, context.getPackageName());
    }

    public static void goMarket(Context context, String packageName) {
        //这里开始执行一个应用市场跳转逻辑，默认this为Context上下文对象
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + packageName)); //跳转到应用市场，非Google Play市场一般情况也实现了这个接口
        //存在手机里没安装应用市场的情况，跳转会包异常，做一个接收判断
        if (intent.resolveActivity(context.getPackageManager()) != null) { //可以接收
            context.startActivity(intent);
        } else { //没有应用市场，我们通过浏览器跳转到Google Play
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + packageName));
            //这里存在一个极端情况就是有些用户浏览器也没有，再判断一次
            if (intent.resolveActivity(context.getPackageManager()) != null) { //有浏览器
                context.startActivity(intent);
            } else { //没有应用市场，没有浏览器
                Toast.makeText(context, "无应用市场", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
