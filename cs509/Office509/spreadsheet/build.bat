rem This will build the installation

echo  "compiling graphical libraries"
javac gui\*.java

echo  "compiling spreadsheet"
javac spreadsheet\*.java

echo  "compiling skeletal structure"
javac *.java
