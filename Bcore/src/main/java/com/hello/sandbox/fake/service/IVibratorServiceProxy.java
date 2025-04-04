package com.hello.sandbox.fake.service;

import android.content.Context;
import android.os.IBinder;
import black.android.os.BRIVibratorManagerServiceStub;
import black.android.os.BRServiceManager;
import black.com.android.internal.os.BRIVibratorServiceStub;
import java.lang.reflect.Method;
import com.hello.sandbox.fake.hook.BinderInvocationStub;
import com.hello.sandbox.utils.MethodParameterUtils;
import com.hello.sandbox.utils.compat.BuildCompat;

/** Created by BlackBox on 2022/3/7. */
public class IVibratorServiceProxy extends BinderInvocationStub {
  private static String NAME;

  static {
    if (BuildCompat.isS()) {
      NAME = "vibrator_manager";
    } else {
      NAME = Context.VIBRATOR_SERVICE;
    }
  }

  public IVibratorServiceProxy() {
    super(BRServiceManager.get().getService(NAME));
  }

  @Override
  protected Object getWho() {
    IBinder service = BRServiceManager.get().getService(NAME);
    if (BuildCompat.isS()) {
      return BRIVibratorManagerServiceStub.get().asInterface(service);
    }
    return BRIVibratorServiceStub.get().asInterface(service);
  }

  @Override
  protected void inject(Object baseInvocation, Object proxyInvocation) {
    replaceSystemService(NAME);
  }

  @Override
  public boolean isBadEnv() {
    return false;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    MethodParameterUtils.replaceFirstUid(args);
    MethodParameterUtils.replaceFirstAppPkg(args);
    return super.invoke(proxy, method, args);
  }
}
