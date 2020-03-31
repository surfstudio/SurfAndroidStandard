rm -rf _mirror
rm -rf temp
git clone --single-branch --branch dev/G-0.5.0 https://bitbucket.org/surfstudio/android-standard temp
git clone https://github.com/surfstudio/Core-ui _mirror
# latest commit
# ./gradlew deployToMirror -Pcomponent=core-ui -Pcommit=7ceba6838e62bc8c6547482658f9e0866d0c2c01 -PmirrorDir=_mirror -PdepthLimit=100 -PsearchLimit=100
# 29.01
./gradlew deployToMirror -Pcomponent=core-ui -Pcommit=35ea114ec56fe67cfb0398abb92769a8cc34ed54 -PmirrorDir=_mirror -PdepthLimit=100 -PsearchLimit=100
