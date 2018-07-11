package unit.moudle.eventdeal.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.puti.education.R;
import com.puti.education.base.InflateService;
import com.puti.education.util.PopupWindowFactory;
import com.puti.education.util.ToastUtil;
import com.puti.education.util.ViewUtils;

import java.net.URLEncoder;
import java.util.List;

/**
 * Created by lei on 2018/7/10.
 */

public class EvidenceVioceAdapter extends BaseAdapter {
    private Context context;
    private List<String> mData;

    private MediaPlayer player = null;
    public EvidenceVioceAdapter(Context context, List<String> mData) {
        this.context = context;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        if (mData == null){
            return 0;
        }
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rootView = null;
        ViewHolder holder = null;

        if (convertView == null){
            rootView = InflateService.g().inflate(R.layout.puti_evidence_vioce_item);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewUtils.dip2px(context,60),
                    ViewUtils.dip2px(context,60));
            rootView.setLayoutParams(params);
            holder = new ViewHolder();
            holder.icon = (SimpleDraweeView) rootView.findViewById(R.id.icon);
            rootView.setTag(holder);
        }else {
            rootView = convertView;
            holder = (ViewHolder) convertView.getTag();
        }
        holder.icon.setImageURI(mData.get(position));
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNetAudio(mData.get(position));
                if (chooseVioceCallBack != null){
                    chooseVioceCallBack.show();
                }
            }
        });
        return rootView;

    }


    public class  ViewHolder{
        SimpleDraweeView icon;
    }


    private void startNetAudio(String path) {
        if (player == null) {
            player = new MediaPlayer();
        }

        if (player.isPlaying()){
            player.stop();
        }

        String relativeUrl = "";
        if (!TextUtils.isEmpty(path)){
            relativeUrl = path.replace("\\", "/");
        }else{
            ToastUtil.show("路径为空");
            return;
        }

        try {
            player.reset();
            //以下主要针对路径中含有中文或空格的情况
            //对路径进行编码 然后替换路径中所有空格 编码之后空格变成“+”而空格的编码表示是“%20” 所以将所有的“+”替换成“%20”就可以了
            String tempPath = URLEncoder.encode(relativeUrl,"utf-8").replaceAll("\\+", "%20");
            //编码之后的路径中的“/”也变成编码的东西了 所有还有将其替换回来 这样才是完整的路径
            tempPath = tempPath.replaceAll("%3A", ":").replaceAll("%2F", "/");

            player.setDataSource(tempPath);

            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    if (chooseVioceCallBack != null){
                        chooseVioceCallBack.dissmiss();
                    }
                }
            });
            player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    ToastUtil.show("播放出错:"+ i + "," + i1);
                    return false;
                }
            });
            player.prepareAsync();
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void stopAudioPlay(){
        if (player == null) {
            return;
        }
        if (player.isPlaying()){
            player.stop();
        }
    }

    private ChooseVioceCallBack chooseVioceCallBack;

    public void setChooseVioceCallBack(ChooseVioceCallBack chooseVioceCallBack) {
        this.chooseVioceCallBack = chooseVioceCallBack;
    }

    public interface ChooseVioceCallBack{
        void show();
        void dissmiss();
    }

}
