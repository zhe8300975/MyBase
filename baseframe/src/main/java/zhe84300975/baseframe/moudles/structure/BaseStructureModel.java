package zhe84300975.baseframe.moudles.structure;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

import zhe84300975.baseframe.BaseHelper;
import zhe84300975.baseframe.core.BaseBiz;
import zhe84300975.baseframe.core.Impl;
import zhe84300975.baseframe.display.BaseIDisplay;
import zhe84300975.baseframe.moudles.methodProxy.BaseProxy;
import zhe84300975.baseframe.utils.BaseAppUtils;
import zhe84300975.baseframe.utils.BaseCheckUtils;

/**
 * Created by zhaowencong on 16/8/18.
 * Describe: 结构管理器
 */
public class BaseStructureModel {


    final int mKey;

    BaseProxy mBaseProxy;

    private Object mView;

    private Class mService;

    private Stack<Class> mSupper;

    private ConcurrentHashMap<Class<?>, Object> mStackHttp;

    private ConcurrentHashMap<Class<?>, Object> mStackImpl;

    private ConcurrentHashMap<Class<?>, Object> mStackDisplay;

    public BaseStructureModel(Object view) {
        // 唯一标记
        mKey = view.hashCode();
        // 视图
        this.mView = view;
        // 业务初始化
        this.mService = BaseAppUtils.getClassGenricType(view.getClass(), 0);
        BaseCheckUtils.checkNotNull(mService, "获取不到泛型");
        BaseCheckUtils.validateServiceInterface(mService);

        Object impl = getImplClass(mService);
        // 找到父类
        mSupper = new Stack<>();
        Class tempClass = impl.getClass().getSuperclass();

        if (tempClass != null) {
            while (!tempClass.equals(BaseBiz.class)) {

                if (tempClass.getInterfaces() != null) {
                    Class clazz = tempClass.getInterfaces()[0];
                    mSupper.add(clazz);
                }
                tempClass = tempClass.getSuperclass();
            }
        }

        mBaseProxy = BaseHelper.methodsProxy().create(mService, impl);
        mStackHttp = new ConcurrentHashMap<>();
        mStackImpl = new ConcurrentHashMap<>();
        mStackDisplay = new ConcurrentHashMap<>();
    }


    /**
     * 清空
     */
    public void clearAll() {
        this.mView = null;
        mService = null;
        mBaseProxy.clearProxy();
        mBaseProxy = null;
        mStackHttp.clear();
        mStackHttp = null;
        mStackImpl.clear();
        mStackImpl = null;
        mStackDisplay.clear();
        mStackDisplay = null;
        mSupper.clear();
        mSupper = null;
    }


    /**
     * 调度
     *
     * @param displayClazz
     * @param <D>
     * @return
     */
    public <D extends BaseIDisplay> D display(Class<D> displayClazz) {
        if (mStackDisplay == null) {
            return BaseHelper.display(displayClazz);
        }

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


    /**
     * 网络
     *
     * @param httpClazz
     * @param <H>
     * @return
     */
    public <H> H http(Class<H> httpClazz) {
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
        if (http == null) {
            http = BaseHelper.structureHelper().createNullService(httpClazz);
        }
        return http;
    }

    /**
     * 实现
     *
     * @param implClazz
     * @param <P>
     * @return
     */
    public <P> P impl(Class<P> implClazz) {
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
            // 如果是业务类
            if (o instanceof BaseBiz) {
                ((BaseBiz) o).initUI(this);
            }
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

    public Object getView() {
        return mView;
    }

    public Class getService() {
        return mService;
    }

    public BaseProxy getBaseProxy() {
        return this.mBaseProxy;
    }

    public boolean isSupterClass(Class clazz) {
        return mSupper.search(clazz) != -1;
    }

}
