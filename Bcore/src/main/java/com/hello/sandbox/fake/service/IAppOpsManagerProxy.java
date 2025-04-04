package com.hello.sandbox.fake.service;

import android.app.AppOpsManager;
import android.content.Context;
import android.os.IBinder;
import black.android.app.BRAppOpsManager;
import black.android.os.BRServiceManager;
import black.com.android.internal.app.BRIAppOpsServiceStub;
import com.hello.sandbox.SandBoxCore;
import com.hello.sandbox.fake.hook.BinderInvocationStub;
import com.hello.sandbox.fake.hook.MethodHook;
import com.hello.sandbox.fake.hook.ProxyMethod;
import com.hello.sandbox.utils.MethodParameterUtils;
import java.lang.reflect.Method;

/** Created by Milk on 4/2/21. * ∧＿∧ (`･ω･∥ 丶　つ０ しーＪ 此处无Bug */
public class IAppOpsManagerProxy extends BinderInvocationStub {
  public IAppOpsManagerProxy() {
    super(BRServiceManager.get().getService(Context.APP_OPS_SERVICE));
  }

  @Override
  protected Object getWho() {
    IBinder call = BRServiceManager.get().getService(Context.APP_OPS_SERVICE);
    return BRIAppOpsServiceStub.get().asInterface(call);
  }

  @Override
  protected void inject(Object baseInvocation, Object proxyInvocation) {
    if (BRAppOpsManager.get(null)._check_mService() != null) {
      AppOpsManager appOpsManager =
          (AppOpsManager) SandBoxCore.getContext().getSystemService(Context.APP_OPS_SERVICE);
      try {
        BRAppOpsManager.get(appOpsManager)._set_mService(getProxyInvocation());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    replaceSystemService(Context.APP_OPS_SERVICE);
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    MethodParameterUtils.replaceFirstAppPkg(args);
    MethodParameterUtils.replaceLastUid(args);
    return super.invoke(proxy, method, args);
  }

  @Override
  public boolean isBadEnv() {
    return false;
  }

  @ProxyMethod("noteProxyOperation")
  public static class NoteProxyOperation extends MethodHook {
    @Override
    protected Object hook(Object who, Method method, Object[] args) throws Throwable {
      return AppOpsManager.MODE_ALLOWED;
    }
  }

  @ProxyMethod("checkPackage")
  public static class CheckPackage extends MethodHook {
    @Override
    protected Object hook(Object who, Method method, Object[] args) throws Throwable {
      // todo
      return AppOpsManager.MODE_ALLOWED;
    }
  }

  @ProxyMethod("checkOperation")
  public static class CheckOperation extends MethodHook {
    @Override
    protected Object hook(Object who, Method method, Object[] args) throws Throwable {
      MethodParameterUtils.replaceLastUid(args);
      return method.invoke(who, args);
    }
  }

  @ProxyMethod("noteOperation")
  public static class NoteOperation extends MethodHook {
    @Override
    protected Object hook(Object who, Method method, Object[] args) throws Throwable {
      return method.invoke(who, args);
    }
  }
}
