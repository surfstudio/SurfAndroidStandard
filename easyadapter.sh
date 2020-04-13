COMMIT_HASH=e6f6bea373f7a577cb8c71feafc21b2f4db74e94 # 87
rm -rf _mirror temp
git clone https://github.com/surfstudio/EasyAdapter _mirror
git clone --single-branch --branch dev/G-0.5.0 https://gitlab.com/surfstudio/projects/standard/android-standard temp
./gradlew deployToMirror -Pcomponent=easyadapter -Pcommit=$COMMIT_HASH -PmirrorDir=_mirror -PdepthLimit=100 -PsearchLimit=100 -PmirrorUrl=https://github.com/surfstudio/EasyAdapter
