# jenkins-scriptler-libs
## Description
This project is intended to overcome some of the limitations of the Jenkins API
by implementing custom endpoints on a Jenkins server using the Scriptler plugin.
This repo to contains groovy scripts to be used with Scriptler, with optional
corresponding configuration files in `.json` format which contain advanced
metadata properties.

## Exposed scripts
| Name       | Description                              |
|------------|------------------------------------------|
| createUser | Script to allow remote user creation given a Username, Password, and Permission (optional) |

### createUser
| Parameter  | Description                                              |
|------------|----------------------------------------------------------|
| username   | *String* Username of the user to be added                |
| password   | *String* Password of the user (supports Base64 encoding) |
| base64     | *Boolean* Whether the password is Base64 encoded `true` or `false`|
| permission | Can override default READ permission with 'ADMINISTER'   |

## Usage
### Scripts
Groovy scripts to be created as "Scriptler Scripts" in the `scripts` directory.
Each of these files must have a `.groovy` extension. This file name becomes the
`id` of the script within Scriptler, and follows the default convention of the
plugin.

#### Metadata
These scripts can include a header with basic metadata that should be associated
with the script. The full description can be found at https://github.com/jenkinsci/jenkins-scripts/tree/master/scriptler

These properties include:

| Property   | Description                                              |
|------------|----------------------------------------------------------|
| name       | *String* The name of the script                          |
| comment    | *String* Description of the script                       |
| parameters | *Array* Parameters required in the script                |
| authors    | *Array* Authors/contributors to the script               |

#### Format
```java
/*** BEGIN META {
 "name" : "createUser",
 "comment" : "Script to allow remote user creation given a Username, Password, and Permission (optional)",
 "parameters" : [ 'username', 'password', 'base64', 'permission' ],
 "authors" : [
 { name : "Peter Springsteen" }
 ]
 } END META**/

// BEGIN code
println "Creating a user..."
```

### Configurations
Optional script configurations are stored in the `configurations` directory as
`.json` files. Each file must be given a name that corresponds with the
`.groovy` script that it is supposed to be associated with. As an example, if
there is a script at `scripts/createUser.groovy`, the corresponding
configuration would be named `configurations/createUser.json`. These files
contain advanced metadata about the script that can be set within Scriptler.
This information includes:

| Field                     | Description |
|---------------------------|-------------------------------------|
| parameters                | *Array* An array of hashes containing the name and default value for each parameter. E.g. `{"name": "string1", "default": "foo"}`. To be used only when a default value is desired to be set through Scriptler |
| allowNonAdminExecution    | *Boolean* Allow execution by user with RunScripts permission. Defaults to `false` |
| onlyExecuteOnMaster       | *Boolean* Script is always executed on Master. Defaults to `false` |
