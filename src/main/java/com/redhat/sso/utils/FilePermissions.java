package com.redhat.sso.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;

public class FilePermissions{

  public static void set(File file, PosixFilePermission... perms) throws IOException{
    Set<PosixFilePermission> perms2 = new HashSet<PosixFilePermission>();
    for(PosixFilePermission p:perms)
      perms2.add(p);
    Files.setPosixFilePermissions(file.toPath(), perms2);
  }
}
