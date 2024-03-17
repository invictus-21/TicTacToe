#!/bin/bash

export DISPLAY=:1.0
cd tictactoe
echo "Compiling..."
mvn clean install
echo "Executing Program..."
mvn clean javafx:run -Djavafx.verbose=true
