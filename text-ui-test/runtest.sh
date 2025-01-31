#!/usr/bin/env bash

# Define directories
SRC_DIR="../src/main/java"
BIN_DIR="../bin"

# Create bin directory if it doesn't exist
if [ ! -d "$BIN_DIR" ]; then
    mkdir -p "$BIN_DIR"
fi

# Delete output from previous run
if [ -e "./ACTUAL.TXT" ]; then
    rm ACTUAL.TXT
fi

# Compile all Java files inside the correct package structure
if ! javac -cp "$SRC_DIR" -Xlint:none -d "$BIN_DIR" $(find "$SRC_DIR" -name "*.java"); then
    echo "********** BUILD FAILURE **********"
    exit 1
fi

# Run the program with package name and feed commands from input.txt
java -classpath "$BIN_DIR" nimbus.Nimbus < input.txt > ACTUAL.TXT

# Convert to UNIX format (ensure dos2unix is installed)
cp EXPECTED.TXT EXPECTED-UNIX.TXT
dos2unix ACTUAL.TXT EXPECTED-UNIX.TXT

# Compare actual output with expected output
diff ACTUAL.TXT EXPECTED-UNIX.TXT
if [ $? -eq 0 ]; then
    echo "Test result: PASSED"
    exit 0
else
    echo "Test result: FAILED"
    exit 1
fi