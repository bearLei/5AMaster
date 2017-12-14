package com.puti.education.util.citychoose;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.MainThread;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.puti.education.netFrame.CommonSubscriber;
import com.puti.education.util.GetJsonDataUtil;
import com.puti.education.util.LogUtil;
import com.puti.education.util.ToastUtil;

import org.json.JSONArray;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lenovo on 2017/12/14.
 * 省市区三级联动
 */

public class CityChooseDialog {

    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private boolean isLoadData = false;
    private Thread thread;

//    public void show(final Context context){
//       Observable<Boolean> observable = Observable.create(new Observable.OnSubscribe<Boolean>() {
//           @Override
//           public void call(Subscriber<? super Boolean> subscriber) {
//                initJsonData(context);
//           }
//       });
//        Subscriber<Boolean> subscriber = new Subscriber<Boolean>() {
//            @Override
//            public void onCompleted() {
//                LogUtil.d("lei","onCompleted");
//                showPickerView(context);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//            @Override
//            public void onNext(Boolean aBoolean) {
//                LogUtil.d("lei","OnNext");
//                showPickerView(context);
//            }
//        };
//
//        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
//    }

    public void showPickerView(final Context context, final ChooseResultCallBack resultCallBack) {
        if (!isLoadData) {
            // 弹出选择器
            OptionsPickerView pvOptions = new OptionsPickerView.Builder(context, new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    //返回的分别是三个级别的选中位置
                    StringBuilder builder = new StringBuilder();
                    builder.append(options1Items.get(options1).getPickerViewText()).append("/")
                            .append(options2Items.get(options1).get(options2)).append("/")
                            .append(options3Items.get(options1).get(options2).get(options3));
                    if (resultCallBack != null){
                        resultCallBack.result(builder.toString(),builder.append("/").toString());
                    }
                }
            })

                    .setTitleText("城市选择")
                    .setDividerColor(Color.BLACK)
                    .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                    .setContentTextSize(20)
                    .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
            pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
            pvOptions.show();
        }else {
            ToastUtil.show("正在查询数据中");
        }
    }


    public void init(final Context context){
        if (thread==null){//如果已创建就不再重新创建子线程了

            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    // 写子线程中的操作,解析省市区数据
                    initJsonData(context);
                }
            });
            thread.start();
        }
    }

    public void initJsonData(Context context) {
        //解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        isLoadData = true;
        String JsonData = new GetJsonDataUtil().getJson(context, "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市
                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {
                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
            isLoadData = false;
        }
    }
    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

    public interface ChooseResultCallBack{
        void result(String s,String detail);
    }
}
