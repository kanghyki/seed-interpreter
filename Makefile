SRC_DIR = src
BUILD_DIR = build
MAIN_CLASS = seed.Seed
SEED_DIR = seed
TOOL_DIR = tool

all: compile

compile: generate-ast
	@mkdir -p $(BUILD_DIR)
	@find $(SRC_DIR)/$(SEED_DIR) -name "*.java" | xargs javac -d $(BUILD_DIR)

generate-ast:
	@mkdir -p $(BUILD_DIR)
	@javac $(SRC_DIR)/$(TOOL_DIR)/GenerateAst.java -d $(BUILD_DIR)
	@java -cp $(BUILD_DIR) tool.GenerateAst $(SRC_DIR)/$(SEED_DIR)

run: compile
	@java -cp $(BUILD_DIR) $(MAIN_CLASS) $(filter-out run,$(MAKECMDGOALS))

print-ast: compile
	@java -cp $(BUILD_DIR) seed.AstPrinter

clean:
	rm -rf $(BUILD_DIR) src/seed/Expr.java

re:
	make clean
	make all

%:
	@:

.PHONY: all compile run clean re generate-ast print-ast
