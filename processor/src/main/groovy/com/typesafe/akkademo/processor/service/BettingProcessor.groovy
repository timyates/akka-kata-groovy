package com.typesafe.akkademo.processor.service

import static akka.actor.SupervisorStrategy.*

import akka.actor.ActorRef
import akka.actor.Cancellable
import akka.actor.Props
import akka.actor.UntypedActor
import akka.actor.SupervisorStrategy
import akka.actor.OneForOneStrategy
import akka.event.Logging
import akka.event.LoggingAdapter
import akka.japi.Function
import akka.util.Duration

import com.typesafe.akkademo.common.DurationCategory
import com.typesafe.akkademo.common.PlayerBet
import com.typesafe.akkademo.common.RegisterProcessor
import com.typesafe.akkademo.common.RetrieveBets
import com.typesafe.akkademo.processor.repository.DatabaseFailureException


public class BettingProcessor extends UntypedActor {
  private LoggingAdapter log = Logging.getLogger( context.system(), this )
  private Cancellable heartbeat
  private ActorRef service
  private ActorRef worker

  public BettingProcessor() {
    service = context.actorFor( context.system().settings().config().getString( "betting-service-actor" ) )
    worker = context.actorOf( new Props( ProcessorWorker ), "theWorker" )
    use( DurationCategory ) {
      heartbeat = context.system().scheduler().schedule( 0.seconds(), 2.seconds(),
                                                         self, new RegisterProcessor() )
    }
  }

  @Override
  public void postStop() {
    heartbeat.cancel();
  }

  @Override
  public SupervisorStrategy supervisorStrategy() {
    strategy
  }

  /**
   * Sets up the supervisor strategy to be applied to all child actors of this actor.
   */
  private static SupervisorStrategy strategy = new OneForOneStrategy( -1, Duration.Inf(),
            [ apply:{ Throwable t ->
                switch( t ) {
                  case DatabaseFailureException :
                    return restart()
                  case RuntimeException :
                    return restart()
                  default :
                    return escalate()
                }
            } ] as Function )

  public void onReceive( message ) {
    switch( message ) {
      case PlayerBet :
        worker.tell( message, sender )
        break
      case RetrieveBets :
        worker.tell( message, sender )
        break
      case RegisterProcessor :
        service.tell( message, self )
        break
      default :
        unhandled( message )
    }
  }
}