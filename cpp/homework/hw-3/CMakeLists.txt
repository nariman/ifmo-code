cmake_minimum_required(VERSION 3.3)
project(ifmo_cpp_rational)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

add_library(rational rational.cpp)
install(TARGETS rational DESTINATION lib)
install(FILES rational.h DESTINATION include)
