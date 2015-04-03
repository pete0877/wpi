ADAPTJAR = jars/adapt.jar

MAINFILE = \
	 FinalApp.class

all: guiJAR spreadsheetJAR $(MAINFILE)

guiJAR:
	make -f gui/Makefile

spreadsheetJAR:
	make -f spreadsheet/Makefile

# Must be tab separators
%.class: %.java
	export CLASSPATH; CLASSPATH=.:$(ADAPTJAR) ; \
	javac $<

clean:
	(cd gui; make clean)
	(cd spreadsheet; make clean)

run:
	export CLASSPATH; CLASSPATH=.:jars/adapt.jar:jars/table.jar:jars/text.jar:jars/spreadsheet.jar; \
	appletviewer Final.html
