COMMIT_HASH=59d7af1376db6e4f37f5bfd2a3139bd3b0730421 # fuckin last
rm -rf _mirror temp
git clone https://github.com/surfstudio/EasyAdapter _mirror
git clone --single-branch --branch dev/G-0.5.0 https://gitlab.com/surfstudio/projects/standard/android-standard temp
./gradlew deployToMirror -Pcomponent=easyadapter -Pcommit=$COMMIT_HASH -PmirrorDir=_mirror -PdepthLimit=100 -PsearchLimit=100 -PmirrorUrl=https://github.com/surfstudio/EasyAdapter
