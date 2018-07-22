package unit.moudle.ques;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.puti.education.base.BaseRVAdapter;
import com.puti.education.base.holder.BaseHolder;
import com.puti.education.base.holder.ViewHolder;
import com.puti.education.util.ToastUtil;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import unit.entity.Question;

/**
 * Created by lei on 2018/6/29.
 */

public class QuestDetailAdapter extends BaseRVAdapter implements WriteAnswerRpc {

    private Context mContext;
    private ArrayList<Question> mData;

    private HashMap<String,String> mQuesAnswerMap;

    public QuestDetailAdapter(Context context, ArrayList<Question> mData) {
        super(context);
        this.mData = mData;
        mQuesAnswerMap = new HashMap<>();
    }

    public QuestDetailAdapter(Context context) {
        super(context);
    }
    @Override
    public void onBindViewHolder(ListHolder holder, int position) {
        View itemView = holder.getView();
        BaseHolder itemHolder = ViewHolder.getHolderTag(itemView);
        itemHolder.setSysTemHolder(holder);
        onBindItemHolder(itemHolder, position);
        if (itemHolder instanceof QuesDetailHolder && getItem(position) instanceof  Question){
            ((QuesDetailHolder) itemHolder).setData((Question) getItem(position),position);
        }else {
            itemHolder.setData(getItem(position));
        }
    }
    @Override
    protected Object getItem(int position) {
        if (mData == null) return null;
        return mData.get(position);
    }



    @Override
    protected int getCount() {
        if (mData == null) return 0;
        return mData.size();
    }

    @Override
    protected BaseHolder getViewHolder(Context context, ViewGroup parent, int viewType) {
        QuesDetailHolder holder = new QuesDetailHolder(context,this);
        return holder;
    }

    @Override
    public void choose(String questionId, String content, int position) {
        mQuesAnswerMap.put(questionId,content);
    }

    public String getAnswers(){
        String str = "";
       if (check()){
           try {
           JSONArray jsonArray = new JSONArray();
           Iterator<Map.Entry<String, String>> iterator = mQuesAnswerMap.entrySet().iterator();
           while (iterator.hasNext()){
               Map.Entry<String, String> entry = iterator.next();
               JSONObject object = new JSONObject();
               object.put("QuestionUID",entry.getKey());
               object.put("Content",entry.getValue());
               jsonArray.add(object);
           }
               JSONObject jsonObject =new JSONObject();
               jsonObject.put("ParentAnswer",jsonArray);
               str = jsonObject.toJSONString();
           } catch (JSONException e) {
               e.printStackTrace();
           }
       }else {
           str = "";
       }
     return str;   
    }

    private boolean  check(){
        for (int i = 0; i < mData.size(); i++) {
            String questionUid = mData.get(i).getQuestionUID();
            if (!mQuesAnswerMap.containsKey(questionUid)){
                int j = i+1;
                ToastUtil.show("请回答第"+j+"道题");
                return false;
            }
        }
        return true;
    }



}
