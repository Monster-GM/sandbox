package com.hello.sandbox.core.system.pm;

/** Created by Milk on 5/2/21. * ∧＿∧ (`･ω･∥ 丶　つ０ しーＪ 此处无Bug */
public interface PackageMonitor {
  void onPackageUninstalled(String packageName, boolean isRemove, int userId);

  void onPackageInstalled(String packageName, int userId);
}
