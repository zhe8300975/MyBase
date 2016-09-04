package zhe84300975.baseframe.moudles.methodProxy;

import android.content.Intent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import zhe84300975.baseframe.BaseHelper;
import zhe84300975.baseframe.core.BaseRunnable;
import zhe84300975.baseframe.core.exception.BaseNotUIPointerException;
import zhe84300975.baseframe.core.plugin.BaseErrorInterceptor;
import zhe84300975.baseframe.core.plugin.BizEndInterceptor;
import zhe84300975.baseframe.core.plugin.BizStartInterceptor;
import zhe84300975.baseframe.moudles.log.L;
import zhe84300975.baseframe.moudles.threadpool.BackgroundType;

/**
 * Created by zhaowencong on 16/8/24.
 * Describe: 代理方法-执行
 */
public class BaseMethod {
    // 执行方法
    public static final int TYPE_INVOKE_EXE = 0;

    public static final int TYPE_DISPLAY_INVOKE_EXE = 4;

    // 执行后台方法
    public static final int TYPE_INVOKE_BACKGROUD_HTTP_EXE = 1;

    public static final int TYPE_INVOKE_BACKGROUD_SINGLEWORK_EXE = 2;

    public static final int TYPE_INVOKE_BACKGROUD_WORK_EXE = 3;


    int mType;

    Object mImpl;

    String mImplName;

    boolean mIsRepeat;

    Method mMethod;

    MethodRunnable mMethodRunnable;

    Class mService;

    int mInterceptor;

    Object mBackgroundResult;

    boolean mIsExe;


    /**
     * 构造函数
     *
     * @param interceptor
     * @param method
     * @param type
     *            执行类型
     * @param isRepeat
     * @param service
     */
    public BaseMethod(int interceptor, Method method, int type, boolean isRepeat, Class service) {
        this.mInterceptor = interceptor;
        this.mType = type;
        this.mIsRepeat = isRepeat;
        this.mMethod = method;
        this.mService = service;
        if (type == TYPE_INVOKE_BACKGROUD_HTTP_EXE || type == TYPE_INVOKE_BACKGROUD_SINGLEWORK_EXE || type == TYPE_INVOKE_BACKGROUD_WORK_EXE) {
            this.mMethodRunnable = new MethodRunnable();
        }
    }

    static BaseMethod createBizMethod(Method method, Class service) {
        // 是否重复
        boolean isRepeat = parseRepeat(method);
        // 拦截方法标记
        int interceptor = parseInterceptor(method);
        // 判断是否是子线程
        int type = parseBackground(method);

        return new BaseMethod(interceptor, method, type, isRepeat, service);
    }

    static <T> BaseMethod createDisplayMethod(Method method, Class<T> service) {
        // 是否重复
        boolean isRepeat = parseRepeat(method);
        // 拦截方法标记
        int interceptor = parseInterceptor(method);
        // 判断是否是子线程
        int type = TYPE_DISPLAY_INVOKE_EXE;

        return new BaseMethod(interceptor, method, type, isRepeat, service);

    }


    private static boolean parseRepeat(Method method) {

        Repeat BaseRepeat = method.getAnnotation(Repeat.class);
        if (BaseRepeat != null && BaseRepeat.value()) {
            return true;
        } else {
            return false;
        }
    }


    private static int parseBackground(Method method) {
        int type = TYPE_INVOKE_EXE;
        Background background = method.getAnnotation(Background.class);

        if (background != null) {
            BackgroundType backgroundType = background.value();

            switch (backgroundType) {
                case HTTP:
                    type = TYPE_INVOKE_BACKGROUD_HTTP_EXE;
                    break;
                case SINGLEWORK:
                    type = TYPE_INVOKE_BACKGROUD_SINGLEWORK_EXE;
                    break;
                case WORK:
                    type = TYPE_INVOKE_BACKGROUD_WORK_EXE;
                    break;
            }
        }

        return type;
    }

    private static int parseInterceptor(Method method) {
        // 拦截方法标记
        Interceptor interceptorClass = method.getAnnotation(Interceptor.class);
        if (interceptorClass != null) {
            return interceptorClass.value();
        } else {
            return 0;
        }
    }

    public <T> T invoke(final Object impl, final Object[] args) throws InterruptedException {
        T result = null;
        if (!mIsRepeat) {
            if (mIsExe) { // 如果存在什么都不做
                if (BaseHelper.getInstance().isLogOpen()) {
                    L.tag("Base-Method");
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(impl.getClass().getSimpleName());
                    stringBuilder.append("build/intermediates/exploded-aar/com.android.support/cardview-v7/23.4.0/res");
                    stringBuilder.append(mMethod.getName());
                    L.i("该方法正在执行 - %s", stringBuilder.toString());
                }
                return result;
            }
            mIsExe = true;
        }
        this.mImpl = impl;
        this.mImplName = impl.getClass().getName();

        switch (mType) {
            case TYPE_INVOKE_EXE:
                defaultMethod(args);
                result = (T) mBackgroundResult;
                break;
            case TYPE_DISPLAY_INVOKE_EXE:
                displayMethod(args);
                result = (T) mBackgroundResult;
                break;
            default:
                if (mIsRepeat) {
                    mMethodRunnable = new MethodRunnable();
                }
                mMethodRunnable.setArgs(args);
                switch (mType) {
                    case TYPE_INVOKE_BACKGROUD_HTTP_EXE:
                        BaseHelper.threadPoolHelper().getHttpExecutorService().execute(mMethodRunnable);
                        break;
                    case TYPE_INVOKE_BACKGROUD_SINGLEWORK_EXE:
                        BaseHelper.threadPoolHelper().getSingleWorkExecutorService().execute(mMethodRunnable);
                        break;
                    case TYPE_INVOKE_BACKGROUD_WORK_EXE:
                        BaseHelper.threadPoolHelper().getWorkExecutorService().execute(mMethodRunnable);
                        break;
                }
                break;
        }

        return result;
    }

    private class MethodRunnable extends BaseRunnable {

        Object[] objects;

        public MethodRunnable() {
            super("MethodRunnable");
        }

        public void setArgs(Object[] objects) {
            this.objects = objects;
        }

        @Override
        protected void execute() {
            defaultMethod(objects);
        }
    }

    private void displayMethod(Object[] objects) {
        try {
            exeDisplayMethod(mMethod, mImpl, objects);
        } catch (Throwable throwable) {
            exeError(mMethod, throwable);
        } finally {
            mIsExe = false;
        }
    }

    private void defaultMethod(Object[] objects) {
        try {
            exeMethod(mMethod, mImpl, objects);
        } catch (Throwable throwable) {
            exeError(mMethod, throwable);
        } finally {
            mIsExe = false;
        }
    }

    private void exeDisplayMethod(final Method method, final Object impl, final Object[] objects) throws InvocationTargetException, IllegalAccessException {
        boolean isExe = true;
        String clazzName = null;
        // 业务拦截器 - 前
        if (BaseHelper.methodsProxy().mDisplayStartInterceptor != null) {
            String name = method.getName();
            if (name.startsWith("intent")) {
                Object object = objects == null || objects.length < 1 ? null : objects[0];
                if (object != null) {
                    if (object instanceof Class) {
                        clazzName = ((Class) object).getName();
                    } else if (object instanceof Intent) {
                        clazzName = ((Intent) object).getComponent().getClassName();
                    }
                }
            }

            isExe = BaseHelper.methodsProxy().mDisplayStartInterceptor.interceptStart(mImplName, mService, method, mInterceptor, clazzName, objects);
        }

        if (isExe) {
            // 如果是主线程 - 直接执行
            if (!BaseHelper.isMainLooperThread()) { // 主线程
                mBackgroundResult = method.invoke(impl, objects);
                return;
            }
            Runnable runnable = new Runnable() {

                @Override
                public void run() {
                    try {
                        method.invoke(impl, objects);
                    } catch (Exception throwable) {
                        if (BaseHelper.getInstance().isLogOpen()) {
                            throwable.printStackTrace();
                        }
                        return;
                    }
                }
            };
            BaseHelper.mainLooper().execute(runnable);
            mBackgroundResult = null;// 执行
            // 业务拦截器 - 后
            if (BaseHelper.methodsProxy().mDisplayEndInterceptor != null) {
                BaseHelper.methodsProxy().mDisplayEndInterceptor.interceptEnd(mImplName, mService, method, mInterceptor, clazzName, objects, mBackgroundResult);
            }
        } else {
            if (BaseHelper.getInstance().isLogOpen()) {
                Object[] parameterValues = objects;
                StringBuilder builder = new StringBuilder("\u21E2 ");
                builder.append(method.getName()).append('(');
                if (parameterValues != null) {
                    for (int i = 0; i < parameterValues.length; i++) {
                        if (i > 0) {
                            builder.append(", ");
                        }
                        builder.append(Strings.toString(parameterValues[i]));
                    }
                }

                builder.append(')');
                L.i("该方法被过滤 - %s", builder.toString());
            }
        }
    }

    public void exeMethod(Method method, Object impl, Object[] objects) throws InvocationTargetException, IllegalAccessException {
        // 业务拦截器 - 前
        for (BizStartInterceptor item : BaseHelper.methodsProxy().mBizStartInterceptor) {
            item.interceptStart(mImplName, mService, method, mInterceptor, objects);
        }
        mBackgroundResult = method.invoke(impl, objects);// 执行
        // 业务拦截器 - 后
        for (BizEndInterceptor item : BaseHelper.methodsProxy().mBizEndInterceptor) {
            item.interceptEnd(mImplName, mService, method, mInterceptor, objects, mBackgroundResult);
        }
    }

    public void exeError(Method method, Throwable throwable) {
        if (BaseHelper.getInstance().isLogOpen()) {
            throwable.printStackTrace();
        }
//		if (throwable.getCause() instanceof BaseError) {
//			// 网络错误拦截器
//			for (BaseHttpErrorInterceptor item : BaseHelper.methodsProxy().BAseHttpErrorInterceptor) {
//				item.methodError(service, method, interceptor, (BaseError) throwable.getCause());
//			}
//		} else
        if (throwable.getCause() instanceof BaseNotUIPointerException) {
            // 忽略
            return;
        } else {
            // 业务错误拦截器
            for (BaseErrorInterceptor item : BaseHelper.methodsProxy().mBaseErrorInterceptor) {
                item.interceptorError(mImplName, mService, method, mInterceptor, throwable);
            }
        }
    }
}


