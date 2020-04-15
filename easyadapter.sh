COMMIT_HASH=64e2d1934b340d552e9e51b3b8af189a021d1a2a # 112
rm -rf _mirror temp
git clone https://github.com/surfstudio/EasyAdapter _mirror
git clone --single-branch --branch dev/G-0.5.0 https://gitlab.com/surfstudio/projects/standard/android-standard temp
./gradlew deployToMirror -Pcomponent=easyadapter -Pcommit=$COMMIT_HASH -PmirrorDir=_mirror -PdepthLimit=100 -PsearchLimit=100 -PmirrorUrl=https://github.com/surfstudio/EasyAdapter
