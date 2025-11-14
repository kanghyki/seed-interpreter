SRC_DIR = src
BUILD_DIR = build
MAIN_CLASS = seed.Seed

all: compile

compile:
	@mkdir -p $(BUILD_DIR)
	find $(SRC_DIR) -name "*.java" | xargs javac -d $(BUILD_DIR)

run: compile
	java -cp $(BUILD_DIR) $(MAIN_CLASS) $(filter-out run,$(MAKECMDGOALS))

generate-ast:
	@mkdir -p $(BUILD_DIR)
	javac src/tool/GenerateAst.java -d $(BUILD_DIR)
	java -cp $(BUILD_DIR) tool/GenerateAst $(SRC_DIR)/seed

clean:
	rm -rf $(BUILD_DIR)

re:
	make clean
	make all

%:
	@:

.PHONY: all compile run clean re
