COMMIT_HASH=4da918a98ddfa97e5a0d796233309c5fc51b6ad0
./gradlew deployToMirror -Pcomponent=core-ui -Pcommit=$COMMIT_HASH -PmirrorDir=_mirror -PdepthLimit=100 -PsearchLimit=100 -PmirrorUrl=https://github.com/surfstudio/Core-ui
