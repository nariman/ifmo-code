cmake_minimum_required(VERSION 3.3)
project(ifmo_cpp_lazy_string)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++17")

add_library(lazy_string lazy_string.cpp)
install(TARGETS lazy_string DESTINATION lib)
install(FILES lazy_string.h DESTINATION include)
