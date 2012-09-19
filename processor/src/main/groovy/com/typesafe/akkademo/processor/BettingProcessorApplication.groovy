package com.typesafe.akkademo.processor

import akka.actor.ActorSystem
import akka.actor.Props
import com.typesafe.akkademo.processor.service.BettingProcessor
import com.typesafe.config.ConfigFactory

class BettingProcessorApplication {
  static main( args ) {
    ActorSystem.create( 'BettingProcessorActorSystem', ConfigFactory.load() )
               .actorOf( new Props( BettingProcessor ), 'bettingProcessor' )
  }
}