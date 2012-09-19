package com.typesafe.akkademo.common

import akka.util.Duration
import static java.util.concurrent.TimeUnit.SECONDS;

public class DurationCategory {
  static Duration seconds( Integer self ) {
    Duration.create( self, SECONDS )
  }
}