# Akka Kata in Groovy with Gradle

This repository contains a Groovy/Gradle version of the solution branch of the 
akka-kata-java [repo to be found here](https://github.com/henrikengstrom/akka-kata-java):

## Prerequisites

* Java
* Gradle

## Running

### Service 

To run the service, type:

```
> gradle service:exec
```

### Processor

To run the processor, type:

```
> gradle processor:exec
```

> Note!  The processor (by design) will throw random exceptions and sometimes
> fall over.  If this happens, you will need to re-run this command, and it will
> continue where it left off.

### Client send

To send bets to the system, type:

```
> gradle client:send
```

### Client receive

And to receive the bets back from the system;

```
> gradle client:receive
```

## Notes

See the [java version of this](https://github.com/henrikengstrom/akka-kata-java)
for a more in-depth description of the system
