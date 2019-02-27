# FSE19 replication package

## Automatic Setup

A virtual machine running Ubuntu server 16.04 is available for download at https://drive.google.com/file/d/1O94guDkO5EQLtK-jVMH6r_mJpdbc3OEt/view?usp=sharing. The virtual machine contains this repository and all the dependencies needed to run TEDD on the test suite subjects. 

The virtual machine was created with VMWare Fusion and was exported in the `.ova` format, a platform-independent distribution format for virtual machines. It can be imported by any virtualization software although it was tested only on VirtualBox and VMWare Fusion. Instructions on how to import an `.ova` format virtual machine in VirtualBox and VMWare Fusion are listed below:

- VirtualBox: https://www.techjunkie.com/ova-virtualbox/
- VMWare Fusion: https://pubs.vmware.com/fusion-5/index.jsp?topic=%2Fcom.vmware.fusion.help.doc%2FGUID-275EF202-CF74-43BF-A9E9-351488E16030.html

The minimum requirement to run the experiments inside the virtual machine is `4GB` of RAM.

Login credentials:
- username: `anonymous`
- password: `fse19`

## Manual Setup

In case there is any problem with the virtual machine, below there are instructions on how to configure the environment to run TEDD on the test suite subjects.

### TEDD and the test suite subjects have the following dependencies:

1. Java JDK 1.8 (https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
2. Maven 3.5.4 (https://maven.apache.org/download.cgi)
3. Chrome browser (https://www.google.com/intl/it_ALL/chrome/)
4. Firefox browser (https://www.mozilla.org/it/firefox/new/)
5. Docker CE (https://docs.docker.com/install/)
6. Wordnet (http://wordnetcode.princeton.edu/3.0/WordNet-3.0.tar.gz). Uncompress the folder and move it (`WordNet-3.0`) in `Desktop`.

TEDD has been tested in MacOS Mojave 10.14.3 and Ubuntu (18.04 LTS and 16.04 LTS).

### Clone repo and donwload docker images
Before running the experiments: 
- clone the repository (`git clone https://github.com/anonymous-fse19-submitter/FSE19-submission-material.git`) in `<path-to-your-home>/workspace` (create the folder `workspace` if it does not exist)
- `cd FSE19-submission-material/tedd && mvn clean compile`
- `cd FSE19-submission-material/testsuite-<application_name> && mvn clean compile` where `<application_name>` is `claroline|addressbook|ppma|collabtive|mrbs|mantisbt`
- download docker web application images. The instructions to run each web application are in the relative folders (`FSE19-submission-material/testsuite-<application_name>`):
  - `docker pull dockercontainervm/claroline:1.11.10`,
  - `docker pull dockercontainervm/addressbook:8.0.0.0`,
  - `docker pull dockercontainervm/ppma:0.6.0`,
  - `docker pull dockercontainervm/collabtive:3.1`,
  - `docker pull dockercontainervm/mrbs:1.4.9`,
  - `docker pull dockercontainervm/mantisbt:1.2.0`
  
### Setup app.properties

Rename the `app.example.properties` in `app.properties` in folder `FSE19-submission-material/tedd/src/main/resources`. Replace all occurences of `/home/anonymous` with `<path-to-your-home>` directory.
  

## Run the experiments (validation - after the setup)

The main script to run the experiments is `FSE19-submission-material/tedd/run-experiment.sh`. The first argument is the `application_name`; the available values are listed above. The second argument is the `main_class` that is going to be executed and the available values are `baseline_complete|tedd`. The third argument is the `mode`, or the desired configuration of the tool. The available values are `baseline_complete|string_analysis|nlp_verb_only_baseline|nlp_verb_only_string|nlp_dobj_baseline|nlp_dobj_string|nlp_noun_matching_baseline|nlp_noun_matching_string`.

The possible combinations of those arguments are listed below (leaving unspecified the `application_name`):
1. `cd FSE19-submission-material/tedd ./run-experiment.sh <application_name> baseline_complete baseline_complete` to run the baseline (original order graph extraction)
2. `cd FSE19-submission-material/tedd ./run-experiment.sh <application_name> tedd string_analysis`
3. `cd FSE19-submission-material/tedd ./run-experiment.sh <application_name> tedd nlp_verb_only_baseline`
4. `cd FSE19-submission-material/tedd ./run-experiment.sh <application_name> tedd nlp_verb_only_string`
5. `cd FSE19-submission-material/tedd ./run-experiment.sh <application_name> tedd nlp_dobj_baseline`
6. `cd FSE19-submission-material/tedd ./run-experiment.sh <application_name> tedd nlp_dobj_string`
7. `cd FSE19-submission-material/tedd ./run-experiment.sh <application_name> tedd nlp_noun_matching_baseline`
8. `cd FSE19-submission-material/tedd ./run-experiment.sh <application_name> tedd nlp_noun_matching_string`

Alternatively, the `FSE19-submission-material/tedd/run-experiments.sh` runs all the available configurations (including the baseline). The only required parameter is the application name. To run it type `cd FSE19-submission-material/tedd && ./run-experiments <application_name>`.

The `FSE19-submission-material/tedd/run-experiment.sh` starts the docker container for the given application and removes it when the validation ends. While the script is running the logs are saved in the `Desktop` as `logs_main_class_application_name.txt`; you can inspect the logs to see the details of the validation. 

It is possible to stop the computation by typing `^C` once on the terminal. Typing `^C` once will end the validation and the script takes care of saving the results computed so far and it removes the docker container for the given application. If you press `^C` twice the results are not saved and the docker container is not removed. In order to remove it, first you need to stop it `docker stop $(docker ps -aq)` and then remove it `docker rm $(docker ps -aq)` (specifically, the previous commands will not stop and remove only one container but all containers running on the system).

When the validation ends, the script saves a directory on the `Desktop` folder with the application name containing the results of the validation. The results folder contains the logs, the final dependency graph (only if the computation was not stopped with `^C`) and the intermediate graphs obtained during the validation.

## Run the experiments (parallelization - after the setup)

The repository has a directory called `FSE19-submission-material/ready-to-run-parallelization` that contains the final dependencies graphs for all the possible configurations of TEDD and for the baseline approach.

The script `FSE19-submission-material/tedd/run-parallelization` extracts and execute all the possible test suites from each dependency graph in the `FSE19-submission-material/ready-to-run-parallelization` folder for each application.

To run it type `cd FSE19-submission-material/tedd && ./run-parallelization.sh <application_name>`. The script starts the docker container for the given application and stops it when the computation finishes. The unique test suites for each dependency graph are executed sequentially and the speed-up factors are computed (worst case and average case) by executing the test suite in its original order after the sequential executions.

As in the previous script the results are saved in the `Desktop` folder. The script creates a folder with the application name that contains the results directory with the logs. The logs can be inspected to see the details of the computation.
