cmake_minimum_required(VERSION 3.3)
project(ifmo_cpp)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++14")

set(HOMEWORK_DIR homework)

# set(SOURCE_FILES main.cpp)
# add_executable(ifmo_cpp ${SOURCE_FILES})

add_executable(hw_1 ${HOMEWORK_DIR}/hw_1/main.c)
add_executable(hw_2 ${HOMEWORK_DIR}/hw_2/main.c)