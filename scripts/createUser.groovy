/*** BEGIN META {
 "name" : "createUser",
 "comment" : "Script to allow remote user creation given a Username, Password, and Permission (optional)",
 "parameters" : [ 'username', 'password', 'base64', 'permission' ],
 "authors" : [
 { name : "Peter Springsteen" }
 ]
 } END META**/
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

String base64Decode(String decodeme) {
  return new String(decodeme.decodeBase64())
}

if (!username || (username == '')) {
  throw new Exception("'username' must be provided")
}
else {
  if (!password || (password == '')) {
    password = randomPasswordGenerator()
    println("New user $username created with random password: $password. Please change.")
  } else {
    if(base64 || (base64 != '')) {
      if (Boolean.valueOf(base64.toLowerCase())) {
        password = base64Decode(password)
      }
    }
  }
  if (!permission || permission == '' || permission.toLowerCase() == 'read') {
    permissionToUse = Jenkins.READ
  } else if (permission.toLowerCase() == 'administer') {
    permissionToUse = Jenkins.ADMINISTER
  }
  else {
    permissionToUse = Jenkins.READ
  }
  def instance = Jenkins.getInstance()
  def existingUser = hudson.model.User.get(username, false)
  if (existingUser != null) println "INFO: Skipping account creation for existing user."
  AuthorizationStrategy strategy = instance.getAuthorizationStrategy()
  SecurityRealm sr = instance.getSecurityRealm();
  if (existingUser == null) {
    sr.createAccount(username, password)
    instance.setSecurityRealm(sr)
  }
  try{
    sr.loadUserByUsername(username)
  } catch (UsernameNotFoundException e) {
    println "You need to add this user"
  }
  def currentStrategy = strategy.getDescriptor()
  println strategy.add(permissionToUse, username)
  instance.setAuthorizationStrategy(strategy)

  instance.save()
}
