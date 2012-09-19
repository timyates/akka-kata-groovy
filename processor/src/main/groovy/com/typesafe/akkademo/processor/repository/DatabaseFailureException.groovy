package com.typesafe.akkademo.processor.repository

import groovy.transform.InheritConstructors

@InheritConstructors
public class DatabaseFailureException extends RuntimeException {}