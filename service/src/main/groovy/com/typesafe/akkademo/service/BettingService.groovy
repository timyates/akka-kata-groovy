package com.typesafe.akkademo.service

import akka.actor.ActorRef
import akka.actor.Cancellable
import akka.actor.UntypedActor
import akka.event.Logging
import akka.event.LoggingAdapter
import com.typesafe.akkademo.common.*
import com.typesafe.akkademo.common.DurationCategory as DC

class BettingService extends UntypedActor {
  LoggingAdapter log = Logging.getLogger( context.system(), this )
  private int sequence = 1
  private ActorRef processor
  private long lastUpdate = 0
  private static final long ACTIVE_PERIOD = 5000L
  private static final String HANDLE_UNPROCESSED_BETS = 'unhandledBets'
  private Cancellable scheduler
  
  // Note: To make this solution (even) more bullet proof you would have to persist the incoming bets.
  private Map<Integer, Bet> bets = [:]

  @Override
  void preStart() {
    use( DC ) {
      scheduler = context.system().scheduler().schedule( 5.seconds(), 3.seconds(),
                                                         self, HANDLE_UNPROCESSED_BETS )
    }
  }

  @Override
  void postStop() {
    scheduler.cancel()
  }

  void onReceive( Object message ) {
    switch( message ) {
      case Bet :
        activeProcessor?.tell( processBet( message ), self )
        break
      case ConfirmationMessage :
        handleProcessedBet( message.id )
        break
      case RetrieveBets :
        activeProcessor?.tell( message, sender )
        break
      case String :
        if( message == HANDLE_UNPROCESSED_BETS ) {
          handleUnprocessedBets()
        }
        break
      case RegisterProcessor :
        registerProcessor( sender )
        break
      default :
        unhandled(message);
    }
  }

  private PlayerBet processBet( Bet bet ) {
    int id = ++sequence
    bets << [ (id): bet ]
    new PlayerBet( id, bet )
  }

  private void handleProcessedBet( int id ) {
    log.info( "processed bet: $id" )
    bets.remove( id )
  }

  private void registerProcessor( ActorRef processor ) {
    this.processor = processor
    lastUpdate = System.currentTimeMillis()
  }

  private ActorRef getActiveProcessor() {
    if( ( System.currentTimeMillis() - lastUpdate ) < ACTIVE_PERIOD) {
      processor
    }
    else {
      null
    }
  }

  private void handleUnprocessedBets() {
    log.info( "handling unprocessed bets (size): ${bets.size()}" )
    activeProcessor?.with { p ->
      bets.each { key, value ->
        p.tell( new PlayerBet( key, value ), self )
      }
    }
  }
}
