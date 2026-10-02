cmake_minimum_required(VERSION 3.4.1)

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -fstack-protector-all")
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Wl,-z,relro -Wl,-z,now")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -fstack-protector-all")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -Wl,-z,relro -Wl,-z,now -Wl,-z,max-page-size=16384")
set(CMAKE_POSITION_INDEPENDENT_CODE ON)

# C++17 — нужен для std::atomic, structured bindings и т.д.
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# ---- Наша .so ----
add_library(toolChecker SHARED
		toolChecker.cpp
)


target_link_libraries(toolChecker
		PRIVATE
			android
			log
			dl
)
