package com.typesafe.akkademo.common

import groovy.transform.Immutable

@Immutable class PlayerBet implements Serializable {
  int id
  Bet bet
}