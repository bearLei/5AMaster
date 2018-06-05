package unit.eventbus;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by lei on 2018-04-28. <br/>
 */
public class PutiEventBus {

    public interface InitCallback {
        boolean init();
    }

    private static InitCallback sInitCallback;

    public static void setInitCallback(InitCallback callback) {
        sInitCallback = callback;
    }

    public static EventBus g() {

        if (EventBus.getDefault() == null) {
            synchronized (PutiEventBus.class) {
                if (EventBus.getDefault() == null) {
                    //在这里进行初始化的操作
                    if (sInitCallback != null) {
                        if (sInitCallback.init()) {
                            // do nothing
                        } else {

                        }
                    }
                }
            }
        }
        return EventBus.getDefault();
    }

    public static void post(Object event) {
        g().post(event);
    }

    public static void postSticky(Object event) {
        g().postSticky(event);
    }

    public static void register(Object subscriber) {
        g().register(subscriber);
    }

    public static void unregister(Object subscriber) {
        g().unregister(subscriber);
    }

    public static boolean isRegistered(Object subscriber) {
        return g().isRegistered(subscriber);
    }
}
