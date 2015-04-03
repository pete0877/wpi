@echo off
cls
echo Building DBC ...
cd database
javac *.java
cd ..

echo Building Office509 Framework and the simple-driver ...
javac *.java

@echo on

