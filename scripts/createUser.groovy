import jenkins.model.*
import java.util.logging.*
import hudson.security.*
import org.acegisecurity.userdetails.*

void log(msg) {
  java.util.logging.Logger.info(String.format(msg));
}

String randomPasswordGenerator() {
  return org.apache.commons.lang.RandomStringUtils.random(16, true, true)
}

if (!USERNAME || (USERNAME == '')) {
  throw new Exception("USERNAME must be provided")
}
else {
  if (!PASSWORD || (PASSWORD == '')) {
    PASSWORD = randomPasswordGenerator()
    println("New user $USERNAME created with random password: $PASSWORD. Please change.")
  }
  if (!PERMISSION || PERMISSION == '' || PERMISSION.lower() == 'read') {
    permissionToUse = Jenkins.READ
  } else if (PERMISSION.lower() == 'administer') {
    permissionToUse = Jenkins.ADMINISTER
  }
  else {
    permissionToUse = Jenkins.READ
  }
  def instance = Jenkins.getInstance()
  def existingUser = hudson.model.User.get(USERNAME, false)
  if (existingUser != null) println "INFO: Skipping account creation for existing user."
  AuthorizationStrategy strategy = instance.getAuthorizationStrategy()
  SecurityRealm sr = instance.getSecurityRealm();
  if (existingUser == null) {
    sr.createAccount(USERNAME, PASSWORD)
    instance.setSecurityRealm(sr)
  }
  try{
    sr.loadUserByUsername(USERNAME)
  } catch (UsernameNotFoundException e) {
    println "You need to add this user"
  }
  def currentStrategy = strategy.getDescriptor()
  println strategy.add(permissionToUse, USERNAME)
  instance.setAuthorizationStrategy(strategy)

  instance.save()
}
