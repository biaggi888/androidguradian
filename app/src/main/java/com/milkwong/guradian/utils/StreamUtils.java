package com.milkwong.guradian.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Leo on 27/02/16.
 */

public class StreamUtils
{
  public static String getJsonStream(InputStream ips) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] b = new byte[1024];
    try {
      for (int len = -1; (len = (ips.read(b))) != -1;baos.write(b,0,len)){
      }
      return baos.toString();
    } catch (IOException e) {
      e.printStackTrace();
    }finally {
      try {
        ips.close();
        baos.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  public static File download(String dowmloadUrl) {
    
    return null;
  }
}
