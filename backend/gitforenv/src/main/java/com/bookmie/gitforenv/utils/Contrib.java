package com.bookmie.gitforenv.utils;

import java.util.Random;

public class Contrib {

  static public Integer generateOtpCode() {
    return new Random().nextInt(100000, 999999);
  }
}
