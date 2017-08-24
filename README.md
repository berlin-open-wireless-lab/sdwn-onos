ONOS : Open Network Operating System
====================================

## SDWN extension
This version of ONOS has diverged from the upstream version. This is due to compatibility issues related to [loxigen](https://github.com/floodlight/loxigen) that had to be adressed
to use ONOS for SDWN during Google Summer of Code 2017.
We are working on resolving them to merge with upstream ONOS.

## Building ONOS
To build this modified ONOS, you first have to install the necesary OpenFlow library in your local maven repository. You can find it [here](https://github.com/berlin-open-wireless-lab/sdwn-loxigen) along with installation instructions.
In order to build ONOS, you will need [buck](https://buckbuild.com/).

1. clone this repository
2. Navigate to the onos source root directory: ```cd sdwn-onos```
3. Set environment variables: ```ONOS_ROOT=$(pwd); export ONOS_ROOT; source ${ONOS_ROOT}/tools/dev/bash_profile```
4. Compile ONOS: ```onos-buck build onos```

### Running ONOS locally
Run ```onos-buck run onos-local -- clean``` to start an ONOS instance locally. If you want to install and run the SDWN controller, please refer to the instructions [here](https://github.com/berlin-open-wireless-lab/sdwn-controller/).

### Deploying ONOS on a remote machine
The ONOS Wiki has instructions on how to package, deploy and run ONOS on a remote machine. Find them [here](https://wiki.onosproject.org/display/ONOS/ONOS+from+Scratch).
