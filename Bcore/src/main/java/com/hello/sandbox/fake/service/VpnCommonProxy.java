package com.hello.sandbox.fake.service;

import black.com.android.internal.net.BRVpnConfig;
import black.com.android.internal.net.VpnConfigContext;
import com.hello.sandbox.SandBoxCore;
import com.hello.sandbox.app.BActivityThread;
import com.hello.sandbox.fake.hook.MethodHook;
import com.hello.sandbox.fake.hook.ProxyMethod;
import com.hello.sandbox.proxy.ProxyVpnService;
import com.hello.sandbox.utils.MethodParameterUtils;
import java.lang.reflect.Method;
import java.util.List;

/** Created by BlackBox on 2022/2/26. */
public class VpnCommonProxy {
  @ProxyMethod("setVpnPackageAuthorization")
  public static class setVpnPackageAuthorization extends MethodHook {

    @Override
    protected Object hook(Object who, Method method, Object[] args) throws Throwable {
      MethodParameterUtils.replaceFirstAppPkg(args);
      return method.invoke(who, args);
    }
  }

  @ProxyMethod("prepareVpn")
  public static class PrepareVpn extends MethodHook {

    @Override
    protected Object hook(Object who, Method method, Object[] args) throws Throwable {
      MethodParameterUtils.replaceFirstAppPkg(args);
      return method.invoke(who, args);
    }
  }

  @ProxyMethod("establishVpn")
  public static class establishVpn extends MethodHook {

    @Override
    protected Object hook(Object who, Method method, Object[] args) throws Throwable {
      VpnConfigContext vpnConfigContext = BRVpnConfig.get(args[0]);
      vpnConfigContext._set_user(ProxyVpnService.class.getName());

      handlePackage(vpnConfigContext.allowedApplications());
      handlePackage(vpnConfigContext.disallowedApplications());
      return method.invoke(who, args);
    }

    private void handlePackage(List<String> applications) {
      if (applications == null) return;
      if (applications.contains(BActivityThread.getAppPackageName())) {
        applications.add(SandBoxCore.getHostPkg());
      }
    }
  }
}
