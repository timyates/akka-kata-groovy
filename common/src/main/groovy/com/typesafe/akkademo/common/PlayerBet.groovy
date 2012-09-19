package com.typesafe.akkademo.common

import groovy.transform.Immutable

@Immutable public class PlayerBet implements Serializable {
  int id
  Bet bet
}