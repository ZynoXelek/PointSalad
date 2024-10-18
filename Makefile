# Command lines examples
# To compile with Bash (Using Linux or Windows git bash): javac -cp "lib/*" -d bin $(find src/code -name "*.java")
# To compile under Windows: ?
# To run under Linux: java -cp "bin:lib/*" code.main.PointSalad
# To run under Windows: java -cp "bin;lib/*" code.main.PointSalad
# Be wary about the classpath separator, it is different between Windows and Linux

# Directories
SRC_DIR := src/code
BIN_DIR := bin
LIB_DIR := lib

# Source files
SOURCES := $(shell find $(SRC_DIR) -name "*.java")

# Detect OS and set path separator
ifeq ($(OS),Windows_NT)
	PATH_SEPARATOR := ;
else
	PATH_SEPARATOR := :
endif

# Classpath
CLASSPATH := $(LIB_DIR)/*

# Main class
MAIN_CLASS := code.main.PointSalad

# Targets
all: compile

compile:
	javac -cp "$(CLASSPATH)" -d $(BIN_DIR) $(SOURCES)

run:
	ARGS="$(filter-out $@,$(MAKECMDGOALS))" && \
	java -cp "$(BIN_DIR)$(PATH_SEPARATOR)$(CLASSPATH)" $(MAIN_CLASS) $$ARGS

clean:
	rm -rf $(BIN_DIR)/*

.PHONY: all compile run clean
