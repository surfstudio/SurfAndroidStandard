#COMMIT_HASH=6bc48efdf07ca04866803ae35f0ff02ba71d1c42 # 116
COMMIT_HASH=3879ea36b27d414c3311647150ed4752d0359185 # problem commit
rm -rf _mirror temp
git clone https://github.com/surfstudio/Core-ui _mirror
git clone --single-branch --branch dev/G-0.5.0 https://gitlab.com/surfstudio/projects/standard/android-standard temp
./gradlew deployToMirror -Pcomponent=core-ui -Pcommit=$COMMIT_HASH -PmirrorDir=_mirror -PdepthLimit=100 -PsearchLimit=100 -PmirrorUrl=https://github.com/surfstudio/Core-ui
