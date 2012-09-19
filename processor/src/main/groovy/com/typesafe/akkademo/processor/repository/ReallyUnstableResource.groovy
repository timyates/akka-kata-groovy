package com.typesafe.akkademo.processor.repository

import com.typesafe.akkademo.common.Bet
import com.typesafe.akkademo.common.PlayerBet

class ReallyUnstableResource implements UnstableResource {
  HashMap<Integer, Bet> bets = [:]
  File store = new File( 'persistent_store' )
  Random randomizer = new Random()

  ReallyUnstableResource() {
    init()
  }

  private void init() {
    if( store.exists() ) {
      store.eachLine { line ->
        deserialize( line ).with { PlayerBet playerBet ->
          if( !bets[ playerBet.id ] ) {
            bets[ playerBet.id ] = playerBet.bet
          }
        }
      }
    }
  }

  @Override
  void save( int idempotentId, String player, int game, int amount ) {
    // Fraudulent instability
    if( idempotentId % ( randomizer.nextInt(10) + 10 ) == 0 ) {
      throw new RuntimeException( 'Hey, I did not count on this happening...' )
    }
    else if( idempotentId % ( randomizer.nextInt(17) + 17 ) == 0 ) {
      throw new DatabaseFailureException( 'Help! The database has gone haywire.' )
    }
    else if( idempotentId % ( randomizer.nextInt(121) + 50 ) == 0 ) {
      System.exit( 1 )
    }

    if( !bets[ idempotentId ] ) {
      persist( idempotentId, new Bet( player, game, amount ) )
    }
  }

  @Override
  List<Bet> findAll() {
    bets.values() as List
  }

  private PlayerBet deserialize( String line ) {
    line.tokenize( ':' ).with { id, player, game, amount ->
      new PlayerBet( id as int, new Bet( player, game as int, amount as int ) )
    }
  }

  private String serialize(int id, Bet bet) {
    "$id:$bet.player:$bet.game:$bet.amount\n"
  }

  private void persist(int id, Bet bet) {
    bets[ id ] = bet
    store << serialize( id, bet )
  }
}