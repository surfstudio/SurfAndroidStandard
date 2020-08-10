#!/bin/bash

### Copy LICENSE to all android packages if necessary.
### must be run from root of repository
### region FUNC

## check is android module directory
## $1 - input dir
## written for ci. dont run by hand
function hasBuildGradle() {
  ls -la $1 | grep -q build.gradle
}

### endregion

for dir in */; do
  if [ -d "$dir" ]; then
    cd "$dir"
    for dir2 in */; do
      if [ -d "$dir2" ]; then
        if ! $(hasBuildGradle $dir2); then
          continue
        fi
        cd "$dir2"
        if [ ! -f LICENSE.txt ]; then
          echo No license in $dir2. Adding new one...
          cp ../../LICENSE.txt LICENSE.txt
        fi
        cd ..
      fi
    done
    cd ..
  fi
done
