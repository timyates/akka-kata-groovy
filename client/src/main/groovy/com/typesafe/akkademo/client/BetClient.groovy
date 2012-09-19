package com.typesafe.akkademo.client

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.dispatch.Await
import akka.dispatch.Future
import akka.pattern.Patterns
import akka.util.Duration
import akka.util.Timeout
import com.typesafe.akkademo.common.Bet
import com.typesafe.akkademo.common.RetrieveBets
import com.typesafe.akkademo.common.DurationCategory as DC ;
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

public class BetClient {

  static main( args ) {
    println "*** STARTING TEST OF BETTING APPLICATION"
    BetClient client = new BetClient()
    client.init().with { system ->
      try {
        if( args && args.head() == 'send' ) {
          client.sendMessages( system )
        } else {
          client.retrieveMessages( system )
        }
      } finally {
        system.shutdown()
      }
    }
  }

  private ActorSystem init() {
    Config config = ConfigFactory.parseString( '''akka {
                                                 |  actor {
                                                 |    provider = "akka.remote.RemoteActorRefProvider"
                                                 |  }
                                                 |  remote {
                                                 |    transport = "akka.remote.netty.NettyRemoteTransport"
                                                 |    netty {
                                                 |      hostname = "127.0.0.1"
                                                 |      port = 2661
                                                 |    }
                                                 |  }
                                                 |}'''.stripMargin() )
    ActorSystem.create( "TestActorSystem", config )
  }

  private void sendMessages( ActorSystem system ) {
    ActorRef service = getService( system )
    200.times { i ->
      service.tell( new Bet( "ready_player_one", i % 10 + 1, i % 100 + 1 ) )
    }
    println( "*** SENDING OK" )
  }

  private void retrieveMessages( ActorSystem system ) {
    use( DC ) {
      ActorRef service = getService( system )
      Timeout timeout = new Timeout( 2.seconds() )
      Future<Object> fBets = Patterns.ask( service, new RetrieveBets(), timeout )
      List<Bet> bets = Await.result( fBets, timeout.duration() )
      assert bets.size() == 200
      println( "*** TESTING OK" )
    }
  }

  private ActorRef getService( ActorSystem system ) {
    system.actorFor( "akka://BettingServiceActorSystem@127.0.0.1:2552/user/bettingService" )
  }
}