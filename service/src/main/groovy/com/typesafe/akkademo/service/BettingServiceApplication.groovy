package com.typesafe.akkademo.service

import akka.actor.Props
import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

class BettingServiceApplication {
  static main( args ) {
    ActorSystem.create( 'BettingServiceActorSystem', ConfigFactory.load() )
               .actorOf( new Props( BettingService ), 'bettingService' )
  }
}