package com.typesafe.akkademo.common

import groovy.transform.Immutable

@Immutable public class Bet implements Serializable {
  String player
  int game
  int amount
}