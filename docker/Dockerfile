# Alpine + JRE: validate only needs a JRE to run its pre-built jar, not
# the full JDK/OS that "eclipse-temurin:25-jdk" ships.
FROM eclipse-temurin:25-jre-alpine

# bash, GNU parallel, and bc for the bundled validate-bundle helper
# script (bin/validate-bundle), which needs all three and isn't
# POSIX-sh-only.
RUN apk update && apk upgrade --no-cache && apk add --no-cache bash parallel bc

ARG tar_file

# Install Validate tool
COPY $tar_file /tmp/validate-bin.tar.gz

RUN  mkdir /opt/validate  \
     && tar xzf /tmp/validate-bin.tar.gz  -C /opt/validate --strip-components 1 \
     && rm -f /tmp/validate-bin.tar.gz

ENV PATH="$PATH:/opt/validate/bin"

ENTRYPOINT ["validate"]
