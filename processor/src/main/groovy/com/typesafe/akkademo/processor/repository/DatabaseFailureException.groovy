package com.typesafe.akkademo.processor.repository

import groovy.transform.InheritConstructors

@InheritConstructors
class DatabaseFailureException extends RuntimeException {}