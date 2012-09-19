ruleset {
  description 'Rules for the akka-kata-groovy project'

  ruleset('rulesets/basic.xml') 
  ruleset('rulesets/braces.xml')
  ruleset('rulesets/exceptions.xml') {
    'ThrowRuntimeException' enabled: false // We throw these in the processor to mimic instability
  }
  ruleset('rulesets/groovyism.xml')
  ruleset('rulesets/imports.xml')
  ruleset('rulesets/logging.xml')
  ruleset('rulesets/naming.xml')
  ruleset('rulesets/unnecessary.xml')
  ruleset('rulesets/unused.xml') 
}
