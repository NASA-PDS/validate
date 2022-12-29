# 🪐 Docker Image and Container for Validate Tool

The docker image of Validate Tool provides an easy way to deploy the Validate Tool, without having to 
worry about the Java/JDK versions installed and operating systems supported. Also, this helps to 
easily use the Validate Tool in other execution environments such as docker compose and Amazon ECS.

The only prerequisite to use the dockerized Validate Tool is the availability of docker software in the host computer.
At the time of writing this, the Validate Tool was tested with Docker version 20.10.12.

## 🏃 Steps to build the docker image of the Validate Tool

1. Clone the GIT Repository containing the validate tool.
```shell
git clone https://github.com/NASA-PDS/validate.git
```

2. Update (if required) the following version in the `Dockerfile` with the required Validate Tool version.

| Variable            | Description |
| ------------------- | ------------|
| validate_version     | The version of the Validate Tool release to be included in the docker image |

```    
# Set following argument with the required validate tool version
ARG validate_version=3.0.3
```

3. Open a terminal and change the current working directory to `validate/docker`.

4. Build the docker image as follows (make sure that docker is installed and running in your computer).

```
    docker image build --tag nasapds/validate .
```

5. Test the `nasapds/validate` docker image as follows.

```
    docker container run --rm nasapds/validate --help
```
The above command should display the help content of the Validate Tool.


## 🏃 Steps to run a docker container of the Validate Tool

The [Operation documentation](https://nasa-pds.github.io/validate/operate/index.html) of the Validate 
tool provides a detailed guide on executing the Validate Tool.

The same commandline arguments can be used to execute the docker container of the Validate Tool with 
the following docker specific change.

- Docker containers cannot directly see the files and directories in the host machine. Therefore, it is required to map 
the files and directories as volumes, so the docker container can access them.
- Read more about docker volumes at https://docs.docker.com/storage/volumes/ 

For example:

A PDS bundle can be validated with Validate Tool as follows as per the 
[Operation documentation](https://nasa-pds.github.io/validate/operate/index.html) of the Validate tool.

```shell
% validate $HOME/pds/bundle -M checksum-manifest.txt -r validate-report.txt -R pds4.bundle
```

However, when using the dockerized Validate Tool, above command can be updated as follows.

```shell
docker container run \
                 --rm \
                 --volume "${HOME}/pds/bundle":/tmp/pds \
                 --volume "${HOME}/pds/bundle/checksum-manifest.txt":/tmp/pds/checksum-manifest.txt \
                 --volume "${HOME}/validate-report":/tmp/validate-report \
                 nasapds/validate /tmp/pds -M /tmp/pds/checksum-manifest.txt -r /tmp/validate-report/validate-report.txt -R pds4.bundle
```


## 🏃 Steps to execute the run.sh with default arguments

An example shell script called `run.sh` is provided in the docker directory of Validate repository. 
This script provides an easier way execute the dockerized Validate Tool to validate a PDS bundle with default arguments. 
It is possible to make copies of this `run.sh` script, modify the default arguments and execute those 
scripts as a simplified shell script based option.

1. To use the `run.sh`, update the following environment variables (if necessary) in the `run.sh`.


| Variable                    | Description |
| --------------------------- | ----------- |
| PDS_BUNDLE_PATH             | PDS bundle directory location in the host machine, which is used to execute the dockerized Validate Tool |
| CHECKSUM_MANIFEST_FILE_NAME | Checksum file name provided for the PDS bundle |
| VALIDATE_REPORT_PATH        | Validate report directory path |
| VALIDATE_REPORT_FILE_NAME   | Validate report file name |
| DEFAULT_ARGS                | Default arguments provided as an array. This can be updated based on the requirements. |

```    
# Update following environment variable to match with your environment

# PDS bundle directory location in the host machine, which is used to execute the dockerized Validate Tool
PDS_BUNDLE_PATH=${HOME}/urn-nasa-pds-insight_rad

# Checksum file name provided for the PDS bundle
CHECKSUM_MANIFEST_FILE_NAME=urn-nasa-pds-insight_rad.md5

# Validate report directory path
VALIDATE_REPORT_PATH=${HOME}

# Validate report file name
VALIDATE_REPORT_FILE_NAME=validate-report.txt

# Default arguments provided as an array. This can be updated based on the requirements.
DEFAULT_ARGS=(/tmp/pds -M /tmp/pds/${CHECKSUM_MANIFEST_FILE_NAME} -r /tmp/validate-report/${VALIDATE_REPORT_FILE_NAME} -R pds4.bundle)

```

2. Open a terminal and change the current working directory to `validate/docker`.

3. If executing for the first time, change the execution permissions of `run.sh` file as follows.

```
chmod u+x run.sh
```

4. Execute the `run.sh` as follows.

```
./run.sh
```

Above steps will execute a docker container of the Validate Tool with default arguments provided in 
the `run.sh` file.
