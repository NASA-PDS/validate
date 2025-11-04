FROM  eclipse-temurin:17-jdk-slim

ARG tar_file

# Install Validate tool
COPY $tar_file /tmp/validate-bin.tar.gz

RUN  mkdir /opt/validate  \
     && tar xzf /tmp/validate-bin.tar.gz  -C /opt/validate --strip-components 1 \
     && rm -f /tmp/validate-bin.tar.gz \
     && apt-get remove --purge \
     && rm -rf /var/lib/apt/lists/*

ENV PATH="$PATH:/opt/validate/bin"

ENTRYPOINT ["validate"]
