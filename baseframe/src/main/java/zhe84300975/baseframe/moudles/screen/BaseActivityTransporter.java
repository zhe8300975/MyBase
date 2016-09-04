package zhe84300975.baseframe.moudles.screen;

import java.util.ArrayList;

/**
 * Created by zhaowencong on 16/8/17.
 * Describe:
 */
public class BaseActivityTransporter {
    private Class<?> toClazz;
    private ArrayList<BaseActivityExtra> extras;

    public BaseActivityTransporter(Class<?> toClazz) {
        this.toClazz = toClazz;
    }

    /**
     * It is only possible to send strings as extra.
     */
    public BaseActivityTransporter addExtra(String key, String value) {
        if (extras == null)
            extras = new ArrayList<>();

        extras.add(new BaseActivityExtra(key, value));
        return this;
    }

    public Class<?> toClazz() {
        return toClazz;
    }

    public ArrayList<BaseActivityExtra> getExtras() {
        return extras;
    }

}
