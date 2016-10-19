cmake_minimum_required(VERSION 3.3)
project(ifmo_cpp_contact_list)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++14")

set(SOURCE_FILES main.cpp)
add_executable(hw_2 ${SOURCE_FILES})
