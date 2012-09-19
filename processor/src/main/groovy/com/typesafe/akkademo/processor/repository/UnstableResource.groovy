package com.typesafe.akkademo.processor.repository

import com.typesafe.akkademo.common.Bet

public interface UnstableResource {
  public void save( int idempotentId, String player, int game, int amount )
  public List<Bet> findAll()
}