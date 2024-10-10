# Variables
SRC_DIR = src
CODE_DIR = $(SRC_DIR)/java
RES_DIR = $(SRC_DIR)/resources
BIN_DIR = bin
LIB_DIR = lib
MAIN_CLASS = PointSalad
JAVAC = javac
JAVA = java

# Define the classpath to include all .jar files in the lib directory
CLASSPATH = $(LIB_DIR)/*

# Detect the operating system
ifeq ($(OS),Windows_NT)
	MKDIR = if not exist $(subst /,\,$(dir $@)) mkdir $(subst /,\,$(dir $@))
	RM = del /Q $(subst /,\,$(BIN_DIR)\*.class)
	CPSEP = ;
else
	MKDIR = mkdir -p $(dir $@)
	RM = rm -rf $(BIN_DIR)/*.class
	CPSEP = :
endif

# Default target: compiles the project
all: $(BIN_DIR)/$(MAIN_CLASS).class

# Compiling .java files in .class files
$(BIN_DIR)/%.class: $(CODE_DIR)/%.java
	@$(MKDIR)
	$(JAVAC) -cp $(CLASSPATH) -d $(BIN_DIR) $<

# Running the project
run: all
	$(JAVA) -cp $(BIN_DIR)$(CPSEP)$(CLASSPATH) $(MAIN_CLASS)

# Connecting an online client player
connect: all
	$(JAVA) -cp $(BIN_DIR)$(CPSEP)json.jar $(MAIN_CLASS) 127.0.0.1

# Connecting an online client player with a custom IP: "make connect-ip IP=xxx.xxx.xxx.xxx"
connect-ip: all
	$(JAVA) -cp $(BIN_DIR)$(CPSEP)json.jar $(MAIN_CLASS) $(IP)

# Cleaning the project
clean:
	$(RM)

# Phony targets to avoid conflicts with files named as the targets
.PHONY: all clean run