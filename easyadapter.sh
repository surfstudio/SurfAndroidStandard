COMMIT_HASH=6bc48efdf07ca04866803ae35f0ff02ba71d1c42 # problem 116
rm -rf _mirror temp
git clone https://github.com/surfstudio/EasyAdapter _mirror
git clone --single-branch --branch dev/G-0.5.0 https://gitlab.com/surfstudio/projects/standard/android-standard temp
./gradlew deployToMirror -Pcomponent=easyadapter -Pcommit=$COMMIT_HASH -PmirrorDir=_mirror -PdepthLimit=100 -PsearchLimit=100 -PmirrorUrl=https://github.com/surfstudio/EasyAdapter
