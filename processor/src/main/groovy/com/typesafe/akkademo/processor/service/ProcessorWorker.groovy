package com.typesafe.akkademo.processor.service

import akka.actor.UntypedActor;
import com.typesafe.akkademo.common.ConfirmationMessage
import com.typesafe.akkademo.common.PlayerBet
import com.typesafe.akkademo.common.RetrieveBets
import com.typesafe.akkademo.processor.repository.ReallyUnstableResource
import com.typesafe.akkademo.processor.repository.UnstableResource

public class ProcessorWorker extends UntypedActor {
  private UnstableResource resource;

  public ProcessorWorker() {
    resource = new ReallyUnstableResource();
  }

  @Override
  public void onReceive(Object message) {
    switch( message ) {
      case PlayerBet :
        resource.save( message.id, message.bet.player, message.bet.game, message.bet.amount )
        sender.tell( new ConfirmationMessage( message.id ) )
        break
      case RetrieveBets :
        sender.tell( resource.findAll() )
        break
      default :
        unhandled( message )
    }
  }
}