package zhe84300975.baseframe.moudles.methodProxy;

import android.os.Build;
import android.os.Looper;
import android.os.Trace;
import android.util.Log;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import zhe84300975.baseframe.BaseHelper;
import zhe84300975.baseframe.core.plugin.BaseActivityInterceptor;
import zhe84300975.baseframe.core.plugin.BaseErrorInterceptor;
import zhe84300975.baseframe.core.plugin.BaseFragmentInterceptor;
import zhe84300975.baseframe.core.plugin.BizEndInterceptor;
import zhe84300975.baseframe.core.plugin.BizStartInterceptor;
import zhe84300975.baseframe.core.plugin.DisplayEndInterceptor;
import zhe84300975.baseframe.core.plugin.DisplayStartInterceptor;
import zhe84300975.baseframe.core.plugin.ImplEndInterceptor;
import zhe84300975.baseframe.core.plugin.ImplStartInterceptor;
import zhe84300975.baseframe.utils.BaseCheckUtils;

/**
 * Created by zhaowencong on 16/8/24.
 * Describe: 方法代理处理
 */
public final class BaseMethods {
    final BaseActivityInterceptor mBaseActivityInterceptor;

    final BaseFragmentInterceptor mBaseFragmentInterceptor;

    final ArrayList<BizStartInterceptor> mBizStartInterceptor;		// 方法开始拦截器

    final DisplayStartInterceptor mDisplayStartInterceptor;	// 方法开始拦截器

    final ArrayList<BizEndInterceptor> mBizEndInterceptor;			// 方法结束拦截器

    final DisplayEndInterceptor mDisplayEndInterceptor;		// 方法结束拦截器

    private ArrayList<ImplStartInterceptor> mImplStartInterceptors;		// 方法开始拦截器

    private ArrayList<ImplEndInterceptor> mImplEndInterceptors;		// 方法结束拦截器

    final ArrayList<BaseErrorInterceptor>		mBaseErrorInterceptor;		// 方法错误拦截器

    public BaseMethods(BaseActivityInterceptor baseActivityInterceptor, BaseFragmentInterceptor baseFragmentInterceptor, ArrayList<BizStartInterceptor> bizStartInterceptor,
                      DisplayStartInterceptor displayStartInterceptor, ArrayList<BizEndInterceptor> bizEndInterceptor, DisplayEndInterceptor displayEndInterceptor,
                      ArrayList<ImplStartInterceptor> implStartInterceptors, ArrayList<ImplEndInterceptor> implEndInterceptors, ArrayList<BaseErrorInterceptor> baseErrorInterceptor) {
        this.mBizEndInterceptor = bizEndInterceptor;
        this.mDisplayEndInterceptor = displayEndInterceptor;
        this.mDisplayStartInterceptor = displayStartInterceptor;
        this.mBizStartInterceptor = bizStartInterceptor;
        this.mBaseErrorInterceptor = baseErrorInterceptor;
        this.mImplStartInterceptors = implStartInterceptors;
        this.mImplEndInterceptors = implEndInterceptors;
        this.mBaseActivityInterceptor = baseActivityInterceptor;
        this.mBaseFragmentInterceptor = baseFragmentInterceptor;
    }

    /**
     * 创建 BIZ
     *
     * @param service
     * @param <T>
     * @return
     */
    public <T> BaseProxy create(final Class<T> service, Object impl) {
        BaseCheckUtils.validateServiceInterface(service);

        final BaseProxy baseProxy = new BaseProxy();
        baseProxy.impl = impl;
        baseProxy.proxy = Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[] { service }, new BaseInvocationHandler() {

            @Override public Object invoke(Object proxy, Method method, Object... args) throws Throwable {
                // 如果有返回值 - 直接执行
                if (!method.getReturnType().equals(void.class)) {
                    return method.invoke(baseProxy.impl, args);
                }

                BaseMethod baseMethod = loadBaseMethod(baseProxy, method, service);
                // 开始
                if (!BaseHelper.getInstance().isLogOpen()) {
                    return baseMethod.invoke(baseProxy.impl, args);
                }
                enterMethod(method, args);
                long startNanos = System.nanoTime();

                Object result = baseMethod.invoke(baseProxy.impl, args);

                long stopNanos = System.nanoTime();
                long lengthMillis = TimeUnit.NANOSECONDS.toMillis(stopNanos - startNanos);
                exitMethod(method, result, lengthMillis);

                return result;
            }
        });

        return baseProxy;
    }


    /**
     * 创建 Display
     *
     * @param service
     * @param <T>
     * @return
     */
    public <T> BaseProxy createDisplay(final Class<T> service, Object impl) {
        BaseCheckUtils.validateServiceInterface(service);

        final BaseProxy baseProxy = new BaseProxy();
        baseProxy.impl = impl;
        baseProxy.proxy = Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[] { service }, new BaseInvocationHandler() {

            @Override public Object invoke(Object proxy, Method method, Object... args) throws Throwable {
                // 如果有返回值 - 直接执行
                if (!method.getReturnType().equals(void.class)) {
                    return method.invoke(baseProxy.impl, args);

                }
                BaseMethod baseMethod = loadDisplayBaseMethod(baseProxy, method, service);
                // 开始
                if (!BaseHelper.getInstance().isLogOpen()) {
                    return baseMethod.invoke(baseProxy.impl, args);
                }
                enterMethod(method, args);
                long startNanos = System.nanoTime();

                Object result = baseMethod.invoke(baseProxy.impl, args);

                long stopNanos = System.nanoTime();
                long lengthMillis = TimeUnit.NANOSECONDS.toMillis(stopNanos - startNanos);
                exitMethod(method, result, lengthMillis);

                return result;
            }
        });

        return baseProxy;
    }

    private void enterMethod(Method method, Object... args) {
        Class<?> cls = method.getDeclaringClass();
        String methodName = method.getName();
        Object[] parameterValues = args;
        StringBuilder builder = new StringBuilder("\u21E2 ");
        builder.append(methodName).append('(');
        if (parameterValues != null) {
            for (int i = 0; i < parameterValues.length; i++) {
                if (i > 0) {
                    builder.append(", ");
                }
                builder.append(Strings.toString(parameterValues[i]));
            }
        }

        builder.append(')');

        if (Looper.myLooper() != Looper.getMainLooper()) {
            builder.append(" [Thread:\"").append(Thread.currentThread().getName()).append("\"]");
        }
        Log.v(cls.getSimpleName(), builder.toString());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            final String section = builder.toString().substring(2);
            Trace.beginSection(section);
        }
    }

    private void exitMethod(Method method, Object result, long lengthMillis) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Trace.endSection();
        }
        Class<?> cls = method.getDeclaringClass();
        String methodName = method.getName();
        boolean hasReturnType = method.getReturnType() != void.class;

        StringBuilder builder = new StringBuilder("\u21E0 ").append(methodName).append(" [").append(lengthMillis).append("ms]");

        if (hasReturnType) {
            builder.append(" = ");
            builder.append(Strings.toString(result));
        }
        Log.v(cls.getSimpleName(), builder.toString());
    }

    /**
     * 获取方法唯一标记
     *
     * @param method
     * @param classes
     * @return
     */
    private String getKey(Method method, Class[] classes) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(method.getName());
        stringBuilder.append("(");
        for (Class clazz : classes) {
            stringBuilder.append(clazz.getSimpleName());
            stringBuilder.append(",");
        }
        if (stringBuilder.length() > 2) {
            stringBuilder.deleteCharAt(stringBuilder.toString().length() - 1);
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    /**
     * 创建 IMPL
     *
     * @param service
     * @param <T>
     * @return
     */
    public <T> T createImpl(final Class<T> service, final Object impl) {
        BaseCheckUtils.validateServiceInterface(service);
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[] { service }, new BaseInvocationHandler() {

            @Override public Object invoke(Object proxy, Method method, Object... args) throws Throwable {
                // 业务拦截器 - 前
                for (ImplStartInterceptor item : BaseHelper.methodsProxy().mImplStartInterceptors) {
                    item.interceptStart(impl.getClass().getName(), service, method, args);
                }
                Object backgroundResult;
                if (!BaseHelper.getInstance().isLogOpen()) {
                    backgroundResult = method.invoke(impl, args);// 执行
                } else {
                    enterMethod(method, args);
                    long startNanos = System.nanoTime();
                    backgroundResult = method.invoke(impl, args);// 执行
                    long stopNanos = System.nanoTime();
                    long lengthMillis = TimeUnit.NANOSECONDS.toMillis(stopNanos - startNanos);
                    exitMethod(method, backgroundResult, lengthMillis);
                }

                // 业务拦截器 - 后
                for (ImplEndInterceptor item : BaseHelper.methodsProxy().mImplEndInterceptors) {
                    item.interceptEnd(impl.getClass().getName(), service, method, args, backgroundResult);
                }
                return backgroundResult;
            }
        });
    }

    /**
     * 获取拦截器
     *
     * @return
     */
    public BaseActivityInterceptor activityInterceptor() {
        return mBaseActivityInterceptor;
    }

    /**
     * 获取拦截器
     *
     * @return
     */
    public BaseFragmentInterceptor fragmentInterceptor() {
        return mBaseFragmentInterceptor;
    }

    /**
     * 加载接口
     *
     * @param baseProxy
     * @param method
     * @param service
     * @param <T>
     * @return
     */
    private <T> BaseMethod loadBaseMethod(BaseProxy baseProxy, Method method, Class<T> service) {
        synchronized (baseProxy.methodCache) {
            String methodKey = getKey(method, method.getParameterTypes());
            BaseMethod baseMethod = baseProxy.methodCache.get(methodKey);
            if (baseMethod == null) {
                baseMethod = BaseMethod.createBizMethod(method, service);
                baseProxy.methodCache.put(methodKey, baseMethod);
            }
            return baseMethod;
        }
    }


    /**
     * 加载接口
     *
     * @param baseProxy
     * @param method
     * @param service
     * @param <T>
     * @return
     */
    private <T> BaseMethod loadDisplayBaseMethod(BaseProxy baseProxy, Method method, Class<T> service) {
        synchronized (baseProxy.methodCache) {
            String methodKey = getKey(method, method.getParameterTypes());
            BaseMethod baseMethod = baseProxy.methodCache.get(methodKey);
            if (baseMethod == null) {
                baseMethod = BaseMethod.createDisplayMethod(method, service);
                baseProxy.methodCache.put(methodKey, baseMethod);
            }
            return baseMethod;
        }
    }


    public static class Builder {

        private BaseActivityInterceptor mBaseActivityInterceptor;		// activity拦截器

        private BaseFragmentInterceptor mBaseFragmentInterceptor;		// activity拦截器

        private ArrayList<BizStartInterceptor>		mBaseStartInterceptors;		// 方法开始拦截器

        private ArrayList<BizEndInterceptor>		mBizEndInterceptors;			// 方法结束拦截器

        private ArrayList<ImplStartInterceptor>		mImplStartInterceptors;		// 方法开始拦截器

        private ArrayList<ImplEndInterceptor>		mImplEndInterceptors;		// 方法结束拦截器

        private ArrayList<BaseErrorInterceptor>		mBaseErrorInterceptors;		// 方法错误拦截器

        private DisplayStartInterceptor				mDisplayStartInterceptor;	// 方法开始拦截器

        private DisplayEndInterceptor				mDisplayEndInterceptor;		// 方法结束拦截器

        public void setActivityInterceptor(BaseActivityInterceptor baseActivityInterceptor) {
            this.mBaseActivityInterceptor = baseActivityInterceptor;
        }

        public void setFragmentInterceptor(BaseFragmentInterceptor baseFragmentInterceptor) {
            this.mBaseFragmentInterceptor = baseFragmentInterceptor;

        }

        public Builder addStartInterceptor(BizStartInterceptor bizStartInterceptor) {
            if (mBaseStartInterceptors == null) {
                mBaseStartInterceptors = new ArrayList<>();
            }
            if (!mBaseStartInterceptors.contains(bizStartInterceptor)) {
                mBaseStartInterceptors.add(bizStartInterceptor);
            }
            return this;
        }

        public Builder addEndInterceptor(BizEndInterceptor bizEndInterceptor) {
            if (mBizEndInterceptors == null) {
                mBizEndInterceptors = new ArrayList<>();
            }
            if (!mBizEndInterceptors.contains(bizEndInterceptor)) {
                mBizEndInterceptors.add(bizEndInterceptor);
            }
            return this;
        }

        public Builder setDisplayStartInterceptor(DisplayStartInterceptor displayStartInterceptor) {
            this.mDisplayStartInterceptor = displayStartInterceptor;
            return this;
        }

        public Builder setDisplayEndInterceptor(DisplayEndInterceptor displayEndInterceptor) {
            this.mDisplayEndInterceptor = displayEndInterceptor;
            return this;
        }

        public Builder addStartImplInterceptor(ImplStartInterceptor implStartInterceptor) {
            if (mImplStartInterceptors == null) {
                mImplStartInterceptors = new ArrayList<>();
            }
            if (!mImplStartInterceptors.contains(implStartInterceptor)) {
                mImplStartInterceptors.add(implStartInterceptor);
            }
            return this;
        }

        public Builder addEndImplInterceptor(ImplEndInterceptor implEndInterceptor) {
            if (mImplEndInterceptors == null) {
                mImplEndInterceptors = new ArrayList<>();
            }
            if (!mImplEndInterceptors.contains(implEndInterceptor)) {
                mImplEndInterceptors.add(implEndInterceptor);
            }
            return this;
        }

        public void addErrorInterceptor(BaseErrorInterceptor baseErrorInterceptor) {
            if (mBaseErrorInterceptors == null) {
                mBaseErrorInterceptors = new ArrayList<>();
            }
            if (!mBaseErrorInterceptors.contains(baseErrorInterceptor)) {
                mBaseErrorInterceptors.add(baseErrorInterceptor);
            }
        }

        public BaseMethods build() {
            // 默认值
            ensureSaneDefaults();
            return new BaseMethods(mBaseActivityInterceptor, mBaseFragmentInterceptor, mBaseStartInterceptors, mDisplayStartInterceptor, mBizEndInterceptors,mDisplayEndInterceptor, mImplStartInterceptors,
                    mImplEndInterceptors, mBaseErrorInterceptors);
        }

        private void ensureSaneDefaults() {
            if (mBaseStartInterceptors == null) {
                mBaseStartInterceptors = new ArrayList<>();
            }
            if (mBizEndInterceptors == null) {
                mBizEndInterceptors = new ArrayList<>();
            }
            if (mBaseErrorInterceptors == null) {
                mBaseErrorInterceptors = new ArrayList<>();
            }
            if (mBaseFragmentInterceptor == null) {
                mBaseFragmentInterceptor = BaseFragmentInterceptor.NONE;
            }
            if (mBaseActivityInterceptor == null) {
                mBaseActivityInterceptor = BaseActivityInterceptor.NONE;
            }
            if (mImplStartInterceptors == null) {
                mImplStartInterceptors = new ArrayList<>();
            }
            if (mImplEndInterceptors == null) {
                mImplEndInterceptors = new ArrayList<>();
            }
        }

    }

}
