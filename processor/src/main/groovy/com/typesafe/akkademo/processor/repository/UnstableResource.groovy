package com.typesafe.akkademo.processor.repository

import com.typesafe.akkademo.common.Bet

interface UnstableResource {
  void save( int idempotentId, String player, int game, int amount )
  List<Bet> findAll()
}