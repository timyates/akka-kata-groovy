package com.typesafe.akkademo.common

import static java.util.concurrent.TimeUnit.SECONDS

import akka.util.Duration

class DurationCategory {
  static Duration seconds( Integer self ) {
    Duration.create( self, SECONDS )
  }
}