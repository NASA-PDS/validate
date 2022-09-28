# Validate Tool

[![DOI](https://zenodo.org/badge/DOI/10.5281/zenodo.5721506.svg)](https://doi.org/10.5281/zenodo.5721506) [![🤪 Unstable integration & delivery](https://github.com/NASA-PDS/validate/actions/workflows/unstable-cicd.yaml/badge.svg)](https://github.com/NASA-PDS/validate/actions/workflows/unstable-cicd.yaml) [![😌 Stable integration & delivery](https://github.com/NASA-PDS/validate/actions/workflows/stable-cicd.yaml/badge.svg)](https://github.com/NASA-PDS/validate/actions/workflows/stable-cicd.yaml)

Project containing software for validating PDS4 products and PDS3 volumes. 
The software is packaged in a JAR file with a command-line wrapper script
to execute validation.

Visit the project's website at: https://nasa-pds.github.io/validate/


## 👥 Contributing

Within the NASA Planetary Data System, we value the health of our community as much as the code. Towards that end, we ask that you read and practice what's described in these documents:

-   Our [contributor's guide](https://github.com/NASA-PDS/.github/blob/main/CONTRIBUTING.md) delineates the kinds of contributions we accept.
-   Our [code of conduct](https://github.com/NASA-PDS/.github/blob/main/CODE_OF_CONDUCT.md) outlines the standards of behavior we practice and expect by everyone who participates with our software.


# Documentation
The [documentation for the latest release of the Validate Tool, including release notes, installation and operation of the software are online](https://NASA-PDS.github.io/validate/). If you would like to get the latest documentation, including any updates since the last release, you can execute the "mvn site:run" command and view the documentation locally at http://localhost:8080.

# Build
The software can be compiled and built with the "mvn compile" command but in order 
to create the JAR file, you must execute the "mvn compile jar:jar" command. 

In order to create a complete distribution package, execute the 
following commands: 

```console
$ mvn site
$ mvn package
```

# Debugging Notes
Since Validate re-uses logging for it's reporting, as of now, there is no easy way to see the debug log messages scattered throughout the code. To see them while debugging/testing your implementation, you will need to replace the SLF4J NOP dependency with the SimpleLogger dependency and enable the DEBUG level.

Here is how to do it via command-line. This may differ if you use Eclipse for debugging:

1. Open pom.xml and comment this out:
```
<!--
     <dependency>
      <groupId>org.slf4j</groupId>
      <artifactId>slf4j-nop</artifactId>
      <version>1.7.28</version>
    </dependency>
-->
```

2. Uncomment this to enable the simplelogger:
```
    <dependency>
      <groupId>org.slf4j</groupId>
      <artifactId>slf4j-simple</artifactId>
      <version>1.7.28</version>
    </dependency>
```

2. Add this to the validate CLI script anywhere before the "$@" OR add to `JAVA_TOOL_OPTIONS` environment variable:
```
-Dorg.slf4j.simpleLogger.defaultLogLevel=DEBUG

export JAVA_TOOL_OPTIONS=-Dorg.slf4j.simpleLogger.defaultLogLevel=DEBUG
```
I think you can also add this to your JAVA_OPTS prior to execution.

3. Build the software while disabling tests (they may fail with all the extra logging):
```
mvn clean package -DskipTests
```
4. Then untar and test the software.


# Operational Release

A release candidate should be created after the community has determined that a release should occur. The Planetary Data System automates the release of software using GitHub Actions. The instructions below are kept for posterity.

## Clone fresh repo
```console
git clone git@github.com:NASA-PDS/validate.git
```


## Run pre-build software

Until we automate this, we must manually generate this file. Follow [semantic versioning](https://semver.org/) for version numbers.

```console
build/pre-build.sh

+ rm -fr validate-1.22.0-SNAPSHOT/
+ mvn clean package -DskipTests
...
+ cp validate-1.22.0-SNAPSHOT/resources/registered_context_products.json src/main/resources/util/
+ git add src/main/resources/util/
+ git commit -m 'Update context products for release'
[main a54fc34] Update context products for release
 3 files changed, 4842 insertions(+), 18478 deletions(-)
 rename build/{build.sh => pre-build.sh} (100%)
 rewrite src/main/resources/util/registered_context_products.json (73%)
+ set +x
```

## Release with ConMan

For internal JPL use, the ConMan software package can be used for releasing software, otherwise the following sections outline how to complete these steps manually.

## Manual Release

### Update Version Numbers

Update pom.xml for the release version or use the Maven Versions Plugin, e.g.:

```console
# Skip this step if this is a RELEASE CANDIDATE, we will deploy as SNAPSHOT version for testing
VERSION=1.15.0
mvn versions:set -DnewVersion=$VERSION
git add pom.xml
git add */pom.xml
```

### Update Changelog
Update Changelog using [Github Changelog Generator](https://github.com/github-changelog-generator/github-changelog-generator). Note: Make sure you set `$CHANGELOG_GITHUB_TOKEN` in your `.bash_profile` or use the `--token` flag.
```console
# For RELEASE CANDIDATE, set VERSION to future release version.
GITHUB_ORG=NASA-PDS
GITHUB_REPO=validate
github_changelog_generator --future-release v$VERSION --user $GITHUB_ORG --project $GITHUB_REPO --configure-sections '{"improvements":{"prefix":"**Improvements:**","labels":["Epic"]},"defects":{"prefix":"**Defects:**","labels":["bug"]},"deprecations":{"prefix":"**Deprecations:**","labels":["deprecation"]}}' --no-pull-requests --token $GITHUB_TOKEN

git add CHANGELOG.md
```

### Commit Changes
Commit changes using following template commit message:
```console
# For operational release
git commit -m "[RELEASE] Validate v$VERSION"

# Push changes to main
git push -u origin main
```

### Build and Deploy Software to [Sonatype Maven Repo](https://repo.maven.apache.org/maven2/gov/nasa/pds/).

```console
# For operational release
mvn clean site site:stage package deploy -P release

# For release candidate
mvn clean site site:stage package deploy
```

Note: If you have issues with GPG, be sure to make sure you've created your GPG key, sent to server, and have the following in your `~/.m2/settings.xml`:
```xml
<profiles>
  <profile>
    <activation>
      <activeByDefault>true</activeByDefault>
    </activation>
    <properties>
      <gpg.executable>gpg</gpg.executable>
      <gpg.keyname>KEY_NAME</gpg.keyname>
      <gpg.passphrase>KEY_PASSPHRASE</gpg.passphrase>
    </properties>
  </profile>
</profiles>

```

### Push Tagged Release
```console
# For Release Candidate, you may need to delete old SNAPSHOT tag
git push origin :v$VERSION

# Now tag and push
REPO=validate
git tag v${VERSION} -m "[RELEASE] $REPO v$VERSION" -m "See [CHANGELOG](https://github.com/NASA-PDS/$REPO/blob/main/CHANGELOG.md) for more details."
git push --tags
```

### Deploy Site to Github Pages

From cloned repo:
```console
git checkout gh-pages

# Copy the over to version-specific and default sites
rsync -av target/staging/ .

git add .

# For operational release
git commit -m "Deploy v$VERSION docs"

# For release candidate
git commit -m "Deploy v${VERSION}-rc${CANDIDATE_NUM} docs"

git push origin gh-pages
```

### Update Versions For Development

Update `pom.xml` with the next SNAPSHOT version either manually or using Github Versions Plugin.

For RELEASE CANDIDATE, ignore this step.

```console
git checkout main

# For release candidates, skip to push changes to main
VERSION=1.16.0-SNAPSHOT
mvn versions:set -DnewVersion=$VERSION
git add pom.xml
git commit -m "Update version for $VERSION development"

# Push changes to main
git push -u origin main
```

### Complete Release in Github
Currently the process to create more formal release notes and attach Assets is done manually through the [Github UI](https://github.com/NASA-PDS/validate/releases/new) but should eventually be automated via script.

*NOTE: Be sure to add the `tar.gz` and `zip` from the `target/` directory to the release assets, and use the CHANGELOG generated above to create the RELEASE NOTES.*

# Snapshot Release

Deploy software to Sonatype SNAPSHOTS Maven repo:

```console
# Operational release
mvn clean site deploy
```

# Maven JAR Dependency Reference

## Operational Releases
https://search.maven.org/search?q=g:gov.nasa.pds%20AND%20a:validate&core=gav

## Snapshots
https://oss.sonatype.org/content/repositories/snapshots/gov/nasa/pds/validate/

If you want to access snapshots, add the following to your `~/.m2/settings.xml`:
```xml
<profiles>
  <profile>
     <id>allow-snapshots</id>
     <activation><activeByDefault>true</activeByDefault></activation>
     <repositories>
       <repository>
         <id>snapshots-repo</id>
         <url>https://oss.sonatype.org/content/repositories/snapshots</url>
         <releases><enabled>false</enabled></releases>
         <snapshots><enabled>true</enabled></snapshots>
       </repository>
     </repositories>
   </profile>
</profiles>
```
