package zhe84300975.baseframe.moudles.structure;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.SimpleArrayMap;
import android.view.KeyEvent;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import zhe84300975.baseframe.BaseHelper;
import zhe84300975.baseframe.R;
import zhe84300975.baseframe.core.BaseIBiz;
import zhe84300975.baseframe.core.BaseICommonBiz;
import zhe84300975.baseframe.core.Impl;
import zhe84300975.baseframe.display.BaseIDisplay;
import zhe84300975.baseframe.moudles.log.L;
import zhe84300975.baseframe.moudles.methodProxy.BaseProxy;
import zhe84300975.baseframe.utils.BaseCheckUtils;
import zhe84300975.baseframe.view.BaseActivity;
import zhe84300975.baseframe.view.BaseFragment;

/**
 * Created by zhaowencong on 16/8/18.
 * Describe: 结构管理器
 */
public class BaseStructureManage implements BaseStructureIManage{

    private final ConcurrentHashMap<Class<?>, Object> mStackDisplay;

    private final ConcurrentHashMap<Class<?>, Object> mStackHttp;

    private final ConcurrentHashMap<Class<?>, Object> mStackCommonBiz;

    private final ConcurrentHashMap<Class<?>, Object> mStackImpl;

    private final ConcurrentHashMap<Class<?>, SimpleArrayMap<Integer, BaseStructureModel>> mStatckRepeatBiz;

    public BaseStructureManage() {
        /** 初始化集合 **/
        mStackHttp = new ConcurrentHashMap<>();
        mStackCommonBiz = new ConcurrentHashMap<>();
        mStackDisplay = new ConcurrentHashMap<>();
        mStackImpl = new ConcurrentHashMap<>();
        mStatckRepeatBiz = new ConcurrentHashMap<>();

    }

    @Override public synchronized void attach(BaseStructureModel view) {
        synchronized (mStatckRepeatBiz) {
            SimpleArrayMap<Integer, BaseStructureModel> stack = mStatckRepeatBiz.get(view.getService());
            if (stack == null) {
                stack = new SimpleArrayMap();
            }
            stack.put(view.mKey, view);
            mStatckRepeatBiz.put(view.getService(), stack);
        }
    }

    @Override public void detach(BaseStructureModel view) {
        synchronized (mStatckRepeatBiz) {
            SimpleArrayMap<Integer, BaseStructureModel> stack = mStatckRepeatBiz.get(view.getService());
            if (stack != null) {
                BaseStructureModel BaseStructureModel = stack.get(view.mKey);
                if (BaseStructureModel == null) {
                    return;
                }
                stack.remove(BaseStructureModel.mKey);
                if (stack.size() < 1) {
                    mStatckRepeatBiz.remove(view.getService());
                }
                BaseStructureModel.clearAll();
            }
        }
        synchronized (mStackImpl) {
            mStackImpl.clear();
        }
        synchronized (mStackDisplay) {
            mStackDisplay.clear();
        }
        synchronized (mStackCommonBiz) {
            mStackCommonBiz.clear();
        }
        synchronized (mStackHttp) {
            mStackHttp.clear();
        }
    }

    /**
     * 获取实现类
     *
     * @param service
     * @param <D>
     * @return
     */
    private <D> Object getImplClass(@NotNull Class<D> service) {
        validateServiceClass(service);
        try {
            // 获取注解
            Impl impl = service.getAnnotation(Impl.class);
            BaseCheckUtils.checkNotNull(impl, "该接口没有指定实现类～");
            /** 加载类 **/
            Class clazz = Class.forName(impl.value().getName());
            Constructor c = clazz.getDeclaredConstructor();
            c.setAccessible(true);
            BaseCheckUtils.checkNotNull(clazz, "业务类为空～");
            /** 创建类 **/
            Object o = c.newInstance();
            return o;
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(String.valueOf(service) + "，没有找到业务类！");
        } catch (InstantiationException e) {
            throw new IllegalArgumentException(String.valueOf(service) + "，实例化异常！");
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(String.valueOf(service) + "，访问权限异常！");
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(String.valueOf(service) + "，没有找到构造方法！");
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException(String.valueOf(service) + "，反射异常！");
        }
    }

    /**
     * 验证类 - 判断是否是一个接口
     *
     * @param service
     * @param <T>
     */
    private <T> void validateServiceClass(Class<T> service) {
        if (service == null || !service.isInterface()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(service);
            stringBuilder.append("，该类不是接口！");
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    @Override public <B extends BaseIBiz> B biz(Class<B> biz) {
        SimpleArrayMap<Integer, BaseStructureModel> stack = mStatckRepeatBiz.get(biz);
        if (stack == null) {
            return createNullService(biz);
        }
        BaseStructureModel BaseStructureModel = stack.valueAt(0);
        if (BaseStructureModel == null) {
            return createNullService(biz);
        }
        if (BaseStructureModel.getBaseProxy() == null || BaseStructureModel.getBaseProxy().proxy == null) {
            return createNullService(biz);
        }
        return (B) BaseStructureModel.getBaseProxy().proxy;
    }

    @Override public <B extends BaseIBiz> boolean isExist(Class<B> biz) {
        SimpleArrayMap<Integer, BaseStructureModel> stack = mStatckRepeatBiz.get(biz);
        if (stack == null) {
            return false;
        }
        BaseStructureModel BaseStructureModel = stack.valueAt(0);
        if (BaseStructureModel == null) {
            return false;
        }
        if (BaseStructureModel.getBaseProxy() == null || BaseStructureModel.getBaseProxy().proxy == null) {
            return false;
        }
        return true;
    }

    @Override public <D extends BaseIDisplay> D display(Class<D> displayClazz) {
        D display = (D) mStackDisplay.get(displayClazz);
        if (display == null) {
            synchronized (mStackDisplay) {
                if (display == null) {
                    BaseCheckUtils.checkNotNull(displayClazz, "display接口不能为空");
                    BaseCheckUtils.validateServiceInterface(displayClazz);
                    Object impl = getImplClass(displayClazz);
                    BaseProxy baseProxy = BaseHelper.methodsProxy().createDisplay(displayClazz, impl);
                    mStackDisplay.put(displayClazz, baseProxy.proxy);
                    display = (D) baseProxy.proxy;
                }
            }
        }
        return display;
    }

    @Override public <B extends BaseICommonBiz> B common(Class<B> service) {
        B b = (B) mStackCommonBiz.get(service);
        if (b == null) {
            synchronized (mStackCommonBiz) {
                if (b == null) {
                    BaseCheckUtils.checkNotNull(service, "biz接口不能为空～");
                    BaseCheckUtils.validateServiceInterface(service);
                    Object impl = getImplClass(service);
                    BaseProxy baseProxy = BaseHelper.methodsProxy().create(service, impl);
                    mStackCommonBiz.put(service, baseProxy.proxy);
                    b = (B) baseProxy.proxy;
                }
            }
        }
        return b;
    }

    @Override public <B extends BaseIBiz> List<B> bizList(Class<B> service) {
        SimpleArrayMap<Integer, BaseStructureModel> stack = mStatckRepeatBiz.get(service);
        List list = new ArrayList();
        if (stack == null) {
            return list;
        }
        int count = stack.size();
        for (int i = 0; i < count; i++) {
            BaseStructureModel BaseStructureModel = stack.valueAt(i);
            if (BaseStructureModel == null || BaseStructureModel.getBaseProxy() == null || BaseStructureModel.getBaseProxy().proxy == null) {
                list.add(createNullService(service));
            } else {
                list.add(BaseStructureModel.getBaseProxy().proxy);
            }
        }
        return list;
    }

    @Override public <H> H http(Class<H> httpClazz) {
        H http = (H) mStackHttp.get(httpClazz);
        if (http == null) {
            synchronized (mStackHttp) {
                if (http == null) {
                    BaseCheckUtils.checkNotNull(httpClazz, "http接口不能为空");
                    BaseCheckUtils.validateServiceInterface(httpClazz);
                    http = BaseHelper.httpAdapter().create(httpClazz);
                    mStackHttp.put(httpClazz, http);
                }
            }
        }
        return http;
    }


    @Override public <P> P impl(Class<P> implClazz) {
        P impl = (P) mStackImpl.get(implClazz);
        if (impl == null) {
            synchronized (mStackImpl) {
                if (impl == null) {
                    BaseCheckUtils.checkNotNull(implClazz, "impl接口不能为空");
                    BaseCheckUtils.validateServiceInterface(implClazz);
                    impl = BaseHelper.methodsProxy().createImpl(implClazz, getImplClass(implClazz));
                    mStackImpl.put(implClazz, impl);
                }
            }
        }
        return impl;
    }

    @Override public <T> T createMainLooper(Class<T> service, final Object ui) {
        BaseCheckUtils.validateServiceInterface(service);
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[] { service }, new InvocationHandler() {

            @Override public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {
                // 如果有返回值 - 直接执行
                if (!method.getReturnType().equals(void.class)) {
                    if(ui == null){
                        return null;
                    }
                    return method.invoke(ui, args);
                }

                // 如果是主线程 - 直接执行
                if (!BaseHelper.isMainLooperThread()) {// 子线程
                    if(ui == null){
                        return null;
                    }
                    return method.invoke(ui, args);
                }
                Runnable runnable = new Runnable() {

                    @Override public void run() {
                        try {
                            if(ui == null){
                                return;
                            }
                            method.invoke(ui, args);
                        } catch (Exception throwable) {
                            if (BaseHelper.getInstance().isLogOpen()) {
                                throwable.printStackTrace();
                            }
                            return;
                        }
                    }
                };
                BaseHelper.mainLooper().execute(runnable);
                return null;
            }
        });
    }


    public <U> U createNullService(final Class<U> service) {
        BaseCheckUtils.validateServiceInterface(service);
        return (U) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[] { service }, new InvocationHandler() {

            @Override public Object invoke(Object proxy, Method method, Object... args) throws Throwable {

                if (BaseHelper.getInstance().isLogOpen()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("UI被销毁,回调接口继续执行");
                    stringBuilder.append("方法[");
                    stringBuilder.append(method.getName());
                    stringBuilder.append("]");
                    L.tag(service.getSimpleName());
                    L.i(stringBuilder.toString());
                }

                if (method.getReturnType().equals(int.class) || method.getReturnType().equals(long.class) || method.getReturnType().equals(float.class) || method.getReturnType().equals(double.class)
                        || method.getReturnType().equals(short.class)) {
                    return 0;
                }

                if (method.getReturnType().equals(boolean.class)) {
                    return false;
                }
                if (method.getReturnType().equals(byte.class)) {
                    return Byte.parseByte(null);
                }
                if (method.getReturnType().equals(char.class)) {
                    return ' ';
                }
                return null;
            }
        });
    }


    @Override public boolean onKeyBack(int keyCode, FragmentManager fragmentManager, BaseActivity baseActivity) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            int idx = fragmentManager.getBackStackEntryCount();
            if (idx > 1) {
                FragmentManager.BackStackEntry entry = fragmentManager.getBackStackEntryAt(idx - 1);
                Object view = fragmentManager.findFragmentByTag(entry.getName());
                if (view instanceof BaseFragment) {
                    return ((BaseFragment) view).onKeyBack();
                }
            } else {

                Object view = fragmentManager.findFragmentById(R.id.base_home);
                if (view instanceof BaseFragment) {
                    return ((BaseFragment) view).onKeyBack();
                }
            }
            if (baseActivity != null) {
                return baseActivity.onKeyBack();
            }
        }
        return false;
    }

    @Override public void printBackStackEntry(FragmentManager fragmentManager) {
        if (BaseHelper.getInstance().isLogOpen()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[");
            for (Fragment fragment : fragmentManager.getFragments()) {
                if (fragment != null) {
                    stringBuilder.append(",");
                    stringBuilder.append(fragment.getClass().getSimpleName());
                }
            }
            stringBuilder.append("]");
            stringBuilder.deleteCharAt(1);
            L.tag("Activity FragmentManager:");
            L.i(stringBuilder.toString());
        }
    }

}
