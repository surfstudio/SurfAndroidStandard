cmake_minimum_required(VERSION 3.4.1)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")

set(SOURCES src/main/cpp/NativeRootChecker.cpp)

add_library(root_checker_jni SHARED ${SOURCES})

find_library(M m)
find_library(LOG log)

target_link_libraries(root_checker_jni ${M} ${LOG})