package unit.location;

import com.baidu.location.BDLocation;

/**
 * Created by lei on 18/6/03.
 */
public class MapEvent {
    public boolean isSuccess = false; //是否定位成功
    public BDLocation bdLocation ; //定位成功时会返回location字段
}
